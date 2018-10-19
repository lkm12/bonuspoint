package com.fuzamei.bonuspoint.service.impl.user;

import com.fuzamei.bonuspoint.constant.PointInfoConstant;
import com.fuzamei.bonuspoint.constant.PointType;
import com.fuzamei.bonuspoint.constant.RewardRuleStatus;
import com.fuzamei.bonuspoint.dao.common.mapper.CompanyInfoMapper;
import com.fuzamei.bonuspoint.dao.common.mapper.InviteMapper;
import com.fuzamei.bonuspoint.dao.common.mapper.PointInfoMapper;
import com.fuzamei.bonuspoint.dao.reward.RewardMapper;
import com.fuzamei.bonuspoint.dao.user.UserDao;
import com.fuzamei.bonuspoint.entity.dto.block.BlockInfoDTO;
import com.fuzamei.bonuspoint.entity.po.account.AccountPO;
import com.fuzamei.bonuspoint.entity.po.point.PointInfoPO;
import com.fuzamei.bonuspoint.entity.po.reward.RewardPO;
import com.fuzamei.bonuspoint.entity.po.user.CompanyInfoPO;
import com.fuzamei.bonuspoint.entity.po.user.InvitePO;
import com.fuzamei.bonuspoint.entity.po.user.PlatformInfoPO;
import com.fuzamei.bonuspoint.entity.vo.user.APP.InviteAPPVO;
import com.fuzamei.bonuspoint.util.RC4;
import com.fuzamei.bonuspoint.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Component
@Transactional(rollbackFor = Exception.class)
public class UserServiceMethodDan {



    private final InviteMapper inviteMapper;

    private final CompanyInfoMapper companyInfoMapper;
    private final RewardMapper rewardMapper;

    private final PointInfoMapper pointInfoMapper;
    private final UserDao userDao;


    @Autowired
    public UserServiceMethodDan(InviteMapper inviteMapper, RewardMapper rewardMapper, CompanyInfoMapper companyInfoMapper,
                                PointInfoMapper pointInfoMapper, UserDao userDao) {

        this.inviteMapper = inviteMapper;
        this.companyInfoMapper = companyInfoMapper;
        this.rewardMapper = rewardMapper;
        this.pointInfoMapper = pointInfoMapper;

        this.userDao = userDao;
    }


    private Map<String, String> sendGeneralPointToUser(Long uid, Long pId, BigDecimal point, String j, Map<String, String> map) {

        map.put("general" + j , point + ":" + uid+":"+pId);

        return map;
    }


    public Map<String, String> inviteCodeMethodDan( InviteAPPVO invitePO, PlatformInfoPO platformInfoPO, AccountPO accountPO) {

        //取出插入用户表后的用户id
        Long uid = accountPO.getId();

        InvitePO invitePORegister = null;
        //准备邀请码数据

        //插入bp_relation表
        invitePORegister = saveInviteCode(invitePO, uid, TimeUtil.timestamp());

        Long platformId = platformInfoPO.getId();

        Example exampleRew = new Example(RewardPO.class);
        Example.Criteria criteria = exampleRew.createCriteria();
        criteria.andEqualTo("platformId", platformId);
        criteria.andEqualTo("status", RewardRuleStatus.EFFECTIVE);
        //通过平台id查找相应的奖励
        List<RewardPO> rewardPOList = null;

        rewardPOList = rewardMapper.selectByExample(exampleRew);

        Map<String, String> map = new HashMap();
        if (rewardPOList.size() != 0) {
            String chains = null;
            //该邀请码的上级链
            chains = invitePORegister.getChains();

            String[] chainsString = chains.split("->");
            if (chainsString.length == 0) {
                throw new RuntimeException("上级不存在");
            }

            for (int i = 0; i < rewardPOList.size(); i++) {

                RewardPO rewardPO = rewardPOList.get(i);
                //奖励的list
                List<BigDecimal> rewardList = new ArrayList<>();
                rewardList.add(rewardPO.getRegisterReward());
                rewardList.add(rewardPO.getFirstInviteReward());
                rewardList.add(rewardPO.getSecondInviteReward());
                rewardList.add(rewardPO.getThirdInviteReward());

                //将邀请码上级链并反转
                String[] chainsStringReverse = new String[chainsString.length];
                int k = 0;
                for (int x = chainsString.length - 1; x >= 0; x--) {
                    chainsStringReverse[k] = chainsString[x];
                    k++;
                }
                List<String> chainsStringReverseList = new ArrayList<>();

                Arrays.stream(chainsStringReverse).forEach(x -> chainsStringReverseList.add(x));

                //将注册者的id放在最前面
                chainsStringReverseList.add(0, String.valueOf(uid));

                if (rewardPO.getPointType().equals(PointType.GENERAL_POINT)) {

                    for (int j = 0; j < rewardList.size(); j++) {

                        if (chainsStringReverseList.size() < j + 1) {
                            break;
                        }
                        BigDecimal rewardNum = rewardList.get(j);

                        String userId = chainsStringReverseList.get(j);

                        if (rewardNum == null || String.valueOf(rewardNum).equals("0.0000")) {
                            continue;
                        }
                        try {
                            map = sendGeneralPointToUser(Long.valueOf(userId), platformId, rewardNum, j + "" + i, map);
                        } catch (Exception e) {
                            throw new RuntimeException(e.getMessage());
                        }


                    }

                } else if (rewardPO.getPointType().equals(PointType.COMPANY_POINT)) {
                    BigDecimal sum = new BigDecimal(0);
                    for (int r = 0; r < rewardList.size(); r++) {
                        if (chainsStringReverseList.size() < r + 1) {
                            break;
                        }
                        BigDecimal bi = rewardList.get(r);
                        if (bi != null) {
                            sum = sum.add(bi);
                        }
                    }

                    CompanyInfoPO companyInfoPO = companyInfoMapper.selectByPrimaryKey(rewardPO.getCompanyId());
                    Long companyId = rewardPO.getCompanyId();

                    Long pointId = rewardPO.getPointId();

                    PointInfoPO pointInfo = new PointInfoPO();
                    pointInfo.setCompany(companyId);
                    pointInfo.setId(pointId);
                    pointInfo.setStatus(PointInfoConstant.CHECK_PASS);
                    PointInfoPO pointInfoPO = pointInfoMapper.selectOne(pointInfo);

                    //商户没有持有该积分就返回
                    if (pointInfoPO == null) {
                        RewardPO reward = new RewardPO();
                        reward.setId(rewardPO.getId());
                        reward.setStatus(RewardRuleStatus.DELETE);
                        int v = rewardMapper.updateByPrimaryKeySelective(reward);
                        if (v < 0) {
                            throw new RuntimeException("修改奖励标志失败");
                        }
                        continue;
                    }
                    //集团积分不够就返回
                    if (pointInfoPO.getNumRemain().compareTo(sum) == -1) {
                        RewardPO reward = new RewardPO();
                        reward.setId(rewardPO.getId());
                        reward.setStatus(RewardRuleStatus.DELETE);
                        int v = rewardMapper.updateByPrimaryKeySelective(reward);
                        if (v < 0) {
                            throw new RuntimeException("修改奖励标志失败");
                        }
                        continue;
                    }

                    for (int u = 0; u < rewardList.size(); u++) {

                        if (chainsStringReverseList.size() < u + 1) {
                            break;
                        }
                        BigDecimal rewardNum = rewardList.get(u);
                        String userId = chainsStringReverseList.get(u);
                        if (rewardNum == null || String.valueOf(rewardNum).equals("0.0000")) {
                            continue;
                        }
                        try {
                            map = sendPointToUser(Long.valueOf(userId), rewardNum, pointInfoPO, companyInfoPO, u + "" + i, map);
                        } catch (Exception e) {
                            throw new RuntimeException(e.getMessage());
                        }

                    }
                }
            }
            String code = null;
            while (true){
                //生成4位随机数
                String st = String.format("%.0f", Math.random() * 8999 + 1000);
                //生成8位邀请码
                code = RC4.encry_RC4_string(st, UUID.randomUUID().toString()).toUpperCase();
                Example examplet= new Example(InvitePO.class);
                Example.Criteria criteriat = examplet.createCriteria();
                criteriat.andEqualTo("inviteCode",code);
                List<InvitePO> listInvi = inviteMapper.selectByExample(examplet);
                if(listInvi.size() == 0){
                    break;
                }
            }

            InvitePO inviteDTO = new InvitePO();
            inviteDTO.setInviteCode(code);
            inviteDTO.setCreatedAt(TimeUtil.timestamp());
            inviteDTO.setId(invitePORegister.getId());
            int i = inviteMapper.updateByPrimaryKeySelective(inviteDTO);
            if (i < 0) {
                log.info("更新邀请码失败");

                throw new RuntimeException("更新邀请码失败");
            }

            return map;

        }else {
            String code = null;
            while (true){
                //生成4位随机数
                String st = String.format("%.0f", Math.random() * 8999 + 1000);
                //生成8位邀请码
                code = RC4.encry_RC4_string(st, UUID.randomUUID().toString()).toUpperCase();
                Example examplet= new Example(InvitePO.class);
                Example.Criteria criteriat = examplet.createCriteria();
                criteriat.andEqualTo("inviteCode",code);
                List<InvitePO> listInvi = inviteMapper.selectByExample(examplet);
                if(listInvi.size() == 0){
                    break;
                }
            }

            InvitePO inviteDTO = new InvitePO();
            inviteDTO.setInviteCode(code);
            inviteDTO.setCreatedAt(TimeUtil.timestamp());
            inviteDTO.setId(invitePORegister.getId());
            int i = inviteMapper.updateByPrimaryKeySelective(inviteDTO);
            if (i < 0) {
                log.info("更新邀请码失败");

                throw new RuntimeException("更新邀请码失败");
            }
            return map;
        }

    }
    private Map<String, String> sendPointToUser(Long userId, BigDecimal rewardNum, PointInfoPO pointInfoPO, CompanyInfoPO companyInfoPO, String u,
                                                Map<String, String> map) {
        Long pointId = pointInfoPO.getId();
        Long companyId = companyInfoPO.getId();

        map.put("point" + u , companyId + ":" + rewardNum + ":" + pointId + ":" + pointInfoPO.getEndAt() + ":"+userId);

        return map;
    }

    public InvitePO saveInviteCode(InviteAPPVO invitePO, Long uid, Long currentTime) {

        //邀请码的上级链
        String chain =  (invitePO.getChains() == null?"":invitePO.getChains()) + (invitePO.getChains() == null?"":"->") + invitePO.getUid();


        InvitePO invitePO1 = new InvitePO();
        invitePO1.setUid(uid);
        invitePO1.setChains(chain);
        invitePO1.setCreatedAt(currentTime);
        invitePO1.setPId(invitePO.getUid());
        invitePO1.setGpid(invitePO.getPId());

        //插入邀请码
        int i =  inviteMapper.insertUseGeneratedKeys(invitePO1);
        if(i < 0){
            throw new RuntimeException("邀请码插入失败");
        }
        return  invitePO1;
    }


    public void addBlockInfo(String hash,Long height, Integer operationType, Long uid){

        BlockInfoDTO blockInfoDTO = new BlockInfoDTO();

        blockInfoDTO.setUid(uid);
        blockInfoDTO.setHash(hash);
        blockInfoDTO.setHeight(height);
        blockInfoDTO.setOperationType(operationType);
        blockInfoDTO.setCreatedAt(TimeUtil.timestamp());
        //将上链信息插入bp_block_info表
        Integer i =  userDao.addBlockInfo(blockInfoDTO);
        if(i < 1){
            throw new RuntimeException("插入操作哈希记录表失败");
        }
    }
}
