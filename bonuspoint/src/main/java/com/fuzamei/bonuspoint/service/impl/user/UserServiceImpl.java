package com.fuzamei.bonuspoint.service.impl.user;

import com.fuzamei.bonuspoint.blockchain.bc.MemberBC;
import com.fuzamei.bonuspoint.constant.CodeType;
import com.fuzamei.bonuspoint.constant.RewardRuleStatus;
import com.fuzamei.bonuspoint.constant.Roles;
import com.fuzamei.bonuspoint.constant.UserStatus;
import com.fuzamei.bonuspoint.dao.common.mapper.*;
import com.fuzamei.bonuspoint.dao.point.MemberPointDao;
import com.fuzamei.bonuspoint.dao.reward.RewardMapper;
import com.fuzamei.bonuspoint.dao.user.UserDao;
import com.fuzamei.bonuspoint.entity.dto.common.Message;
import com.fuzamei.bonuspoint.entity.dto.point.ExchangePointDTO;
import com.fuzamei.bonuspoint.entity.dto.user.PagePointDTO;
import com.fuzamei.bonuspoint.entity.dto.user.UserDTO;
import com.fuzamei.bonuspoint.entity.po.account.AccountPO;
import com.fuzamei.bonuspoint.entity.po.reward.RewardPO;
import com.fuzamei.bonuspoint.entity.po.user.InvitePO;
import com.fuzamei.bonuspoint.entity.po.user.PlatformInfoPO;
import com.fuzamei.bonuspoint.entity.po.user.UserPO;
import com.fuzamei.bonuspoint.entity.vo.user.APP.InviteAPPVO;
import com.fuzamei.bonuspoint.entity.vo.user.APP.UserInfoAPPVO;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.enums.SafeResponseEnum;
import com.fuzamei.bonuspoint.enums.UserResponseEnum;
import com.fuzamei.bonuspoint.service.user.UserService;
import com.fuzamei.bonuspoint.util.QrCodeUtil;
import com.fuzamei.bonuspoint.util.RandomUtil;
import com.fuzamei.bonuspoint.util.RedisTemplateUtil;
import com.fuzamei.bonuspoint.util.TimeUtil;
import com.fuzamei.common.bean.FastDFSClient;
import com.fuzamei.common.bean.PageBean;
import com.fuzamei.common.model.vo.ResponseVO;
import com.fzm.blockchain.util.KeyUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-04-20 14:25
 **/
@Slf4j
@Service
@RefreshScope
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    @Value("${page.pageSize}")
    private Integer pageSize;

    @Value("${blockChain.url}")
    private String blockChainUrl;

    @Value("${md5.salt}")
    private String salt;

    @Value("${key.platform.privateKey}")
    private String platformPrivateKey;
    @Value("${key.platform.publicKey}")
    private String platFormPublicKey;

    @Value(("${token.client.app}"))
    private String appClient;
    @Value("${token.client.browser}")
    private String browserClient;
    @Value("${token.outTime.browser}")
    private Long browserOutTime;

    @Value("${token.outTime.app}")
    private Long appOutTime;

    private final RedisTemplate redisTemplate;

    private final QrCodeUtil qrCodeUtil;
    private final MemberBC memberBC;
    private final RewardMapper rewardMapper;
    private final BlockInfoMapper blockInfoMapper;
    private final GeneralPointRecordMapper generalPointRecordMapper;
    private final PointRecordMapper pointRecordMapper;
    private final CompanyInfoMapper companyInfoMapper;
    private final PointInfoMapper pointInfoMapper;
    private final PointRelationMapper pointRelationMapper;
    private final InsertRe insertRe;


    /**
     * 加密密钥
     */
    @Value("${token.encrypt.key}")
    private String encryptKey;

    private final InviteMapper inviteMapper;

    private final MemberPointDao memberPointDao;


    private final UserDao userDao;


    private final PlatformInfoMapper platformInfoMapper;

    private final UserServiceMethodDan userServiceMethodDan;

    private final AccountMapper accountMapper;

    private final RedisTemplateUtil redisTemplateUtil;


    private final GeneralPointRelationMapper generalPointRelationMapper;

    @Autowired
    public UserServiceImpl(RedisTemplate redisTemplate, QrCodeUtil qrCodeUtil, MemberBC memberBC, RewardMapper rewardMapper, BlockInfoMapper blockInfoMapper, GeneralPointRecordMapper generalPointRecordMapper, PointRecordMapper pointRecordMapper, CompanyInfoMapper companyInfoMapper, PointInfoMapper pointInfoMapper, PointRelationMapper pointRelationMapper, InsertRe insertRe, InviteMapper inviteMapper, MemberPointDao memberPointDao, UserDao userDao, PlatformInfoMapper platformInfoMapper, UserServiceMethodDan userServiceMethodDan, AccountMapper accountMapper, RedisTemplateUtil redisTemplateUtil, GeneralPointRelationMapper generalPointRelationMapper) {
        this.redisTemplate = redisTemplate;
        this.qrCodeUtil = qrCodeUtil;
        this.memberBC = memberBC;
        this.rewardMapper = rewardMapper;
        this.blockInfoMapper = blockInfoMapper;
        this.generalPointRecordMapper = generalPointRecordMapper;
        this.pointRecordMapper = pointRecordMapper;
        this.companyInfoMapper = companyInfoMapper;
        this.pointInfoMapper = pointInfoMapper;
        this.pointRelationMapper = pointRelationMapper;
        this.insertRe = insertRe;
        this.inviteMapper = inviteMapper;
        this.memberPointDao = memberPointDao;
        this.userDao = userDao;
        this.platformInfoMapper = platformInfoMapper;
        this.userServiceMethodDan = userServiceMethodDan;
        this.accountMapper = accountMapper;
        this.redisTemplateUtil = redisTemplateUtil;
        this.generalPointRelationMapper = generalPointRelationMapper;
    }

    private ResponseVO getResponseVOByResult(int result, UserDTO userDTO) {
        if (result == 1) {
            Map<String, Long> map = new HashMap<>(16);
            map.put("uid", userDTO.getId());
            return new ResponseVO<>(CommonResponseEnum.SET_FAIL, map);
        }
        return new ResponseVO(CommonResponseEnum.SET_FAIL);
    }


    @Override
    public UserPO getUserRoleById(Long id) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        return userDao.getUserById(id);
    }

    @Override
    public UserPO getUser(List<String> fields, UserDTO userDTO) {

        return userDao.getUser(fields, userDTO);
    }


    @Override
    public ResponseVO addUser(UserDTO userDTO) {

        Message message = new Message();

        message.setMobile(userDTO.getUsername());

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        //从redis中取出验证码
        String codeRedis = valueOperations.get(CodeType.REGISETER + "_" + message.getMobile());

        if (!userDTO.getCode().equals(codeRedis)) {
            return new ResponseVO(SafeResponseEnum.CAPTCHA_WRONG);
        }

        Example examplePlatform = new Example(PlatformInfoPO.class);
        examplePlatform.createCriteria().andEqualTo("mark", userDTO.getMark());

        //通过平台uuid查找平台信息
        PlatformInfoPO platform = platformInfoMapper.selectOneByExample(examplePlatform);

        if (platform == null) {
            return new ResponseVO(CommonResponseEnum.PLATFORM_NOT_EXIST);
        }

        Example exampleAccountPO = new Example(AccountPO.class);
        Example.Criteria criteria = exampleAccountPO.createCriteria();
        criteria.andEqualTo("username", userDTO.getUsername());
        criteria.andEqualTo("status", UserStatus.AVAILABLE);
        criteria.andEqualTo("pId", platform.getUid());
        criteria.andEqualTo("role", Roles.MEMBER);
        //校验是否有相同的用户名
        int num = accountMapper.selectCountByExample(exampleAccountPO);

        if (num != 0) {
            return new ResponseVO(UserResponseEnum.EXIST_NAME);
        }


        Long currentTime = System.currentTimeMillis();

        AccountPO accountPO = new AccountPO();
        accountPO.setCreatedAt(currentTime);
        accountPO.setUpdatedAt(currentTime);
        accountPO.setIsInitialize(UserStatus.UNINIT);
        accountPO.setRole(Roles.MEMBER);
        accountPO.setStatus(UserStatus.AVAILABLE);
        accountPO.setLoginAt(currentTime);
        accountPO.setMobile(userDTO.getUsername());
        accountPO.setCountry("CN");
        accountPO.setUsername(userDTO.getUsername());
        accountPO.setPasswordHash(userDTO.getPasswordHash());
        accountPO.setPId(platform.getUid());
        //插入user表
        int i = accountMapper.insertUseGeneratedKeys(accountPO);
        if (i < 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseVO(CommonResponseEnum.ADD_FAIL);
        }

        String random = RandomUtil.getRandomString(32);
        String privatekey = KeyUtil.privateKey(userDTO.getPassword(), random);
        String publickey = KeyUtil.publicKey(privatekey);

        AccountPO accountPOKey = new AccountPO();
        accountPOKey.setPublicKey(publickey);
        accountPOKey.setPrivateKey(privatekey);
        accountPOKey.setUpdatedAt(TimeUtil.timestamp());
        accountPOKey.setId(accountPO.getId());

        //将bp_user表里的公钥与私钥赋值
        i = accountMapper.updateByPrimaryKeySelective(accountPOKey);
        if (i < 0) {
            log.info("公私钥更新失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseVO(CommonResponseEnum.UPDATE_FALL);

        }

        String inviteCode = userDTO.getInviteCode();
        Map<String, String> mapBC = null;
        if (inviteCode != null && !inviteCode.equals("")) {
            //查询该邀请码的上级的邀请码信息
            InviteAPPVO invitePO = userDao.findChainAndUidByInviteCode(inviteCode);
            //若用户信息为null说明此邀请码无效
            if (invitePO == null) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return new ResponseVO(UserResponseEnum.INVITEDCODE_NOT_EXIST_FAIL);
            }

            try {
                mapBC = userServiceMethodDan.inviteCodeMethodDan(invitePO, platform, accountPO);
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new RuntimeException(e.getMessage());
            }


        }
/*
            try {
                mapBC =  userServiceMethodDan.sendRegistPoint(platformInfoPO,accountPO);
                }catch (Exception e){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new RuntimeException(e.getMessage());
            }

            //生成4位随机数
            String st = String.format("%.0f", Math.random() * 8999 + 1000);
            //生成8位邀请码
            String code = RC4.encry_RC4_string(st, UUID.randomUUID().toString()).toUpperCase();

            InvitePO invitePO = new InvitePO();
            invitePO.setUid(accountPO.getId());
            invitePO.setCreatedAt(TimeUtil.timestamp());
            invitePO.setInviteCode(code);

            //添加邀请码信息
            i = inviteMapper.insertSelective(invitePO);
            if (i < 0) {
                log.info("添加邀请码信息失败");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return new ResponseVO(CommonResponseEnum.ADD_FAIL);
            }
*/


        //注册完成，删除缓存中的验证码
        redisTemplateUtil.delStr(CodeType.REGISETER + "_" + message.getMobile());


        /**
         * 注册上链
         */

        try {
            memberBC.userRegister(String.valueOf(accountPO.getId()), publickey, privatekey);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new RuntimeException(e.getMessage());
        }
        //查找用户信息
        AccountPO accountPO2 = accountMapper.selectByPrimaryKey(accountPO.getId());

        /**
         * 平台发放积分奖励上链
         */
        if (mapBC != null) {

            List<String> pointList = new ArrayList<>();
            List<String> generalValue = new ArrayList<>();

            Map<String, Object> generalMap = new HashMap<>();
            Map<String, Object> pointMap = new HashMap<>();

            for (String key : mapBC.keySet()) {
                if (key.contains("general")) {

                    generalMap.put(key, mapBC.get(key));

                } else if (key.contains("point")) {

                    pointMap.put(key, mapBC.get(key));

                }
            }

            Map<String, Object> generalMapBC = new HashMap<>();
            Map<String, Object> pointMapBC = new HashMap<>();

            Example exampleRew = new Example(RewardPO.class);
            Example.Criteria criteria1 = exampleRew.createCriteria();
            criteria1.andEqualTo("platformId", platform.getId());
            criteria1.andEqualTo("status", RewardRuleStatus.EFFECTIVE);

            //通过平台id查找相应的奖励
            List<RewardPO> rewardPOList = rewardMapper.selectByExample(exampleRew);


            for (int s = 0; s < generalMap.size(); s++) {
                for (int s1 = 0; s1 < rewardPOList.size(); s1++) {
                    for (String key : generalMap.keySet()) {

                        if (key.contains("general" + s + s1)) {
                            generalMapBC.put(key, generalMap.get(key));
                            generalValue.add((String) generalMap.get(key));
                            break;
                        }

                    }
                }

            }
            for (int z = 0; z < pointMap.size(); z++) {

                for (int s1 = 0; s1 < rewardPOList.size(); s1++) {
                    for (String key : pointMap.keySet()) {

                        if (key.contains("point" + z + s1)) {
                            pointMapBC.put(key, pointMap.get(key));
                            pointList.add((String) pointMap.get(key));
                            break;
                        }
                    }
                }
            }
            if (generalValue.size() != 0) {

                generalValue.stream().forEach(s -> {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {

                           insertRe.insertGeneralPoint(s,accountPO2,platform);

                        }
                    });
                    thread.start();
                });
            }

            if (pointList.size() != 0) {
                pointList.stream().forEach(s -> {

                    Thread thread = new Thread(new Runnable() {

                        @Override
                        public void run() {

                            insertRe.insertPoint(s,accountPO2);
                        }

                    });
                    thread.start();
                });
            }


        }

        Map map = new HashMap();
        map.put("role", "Member");
        map.put("userId", String.valueOf(accountPO.getId()));
        map.put("username", userDTO.getUsername());
        return new ResponseVO<>(UserResponseEnum.USER_REGISTER_SUCCESS, map);

    }



    @Override
    public ResponseVO findUserInfoById(Long id) {
        AccountPO accountPO = accountMapper.selectByPrimaryKey(id);

        if (accountPO == null) {
            return new ResponseVO(UserResponseEnum.USER_NOT_EXIST);
        }
        //查询用户信息
        UserPO userPO = userDao.findUserInfoById(id);

        log.info("查询成功");
        return new ResponseVO<>(UserResponseEnum.MEMBER_INFO_SUCCESS, userPO);
    }

    @Override
    public ResponseVO findInviteInfo(Long id) {

        Example example = new Example(InvitePO.class);
        example.createCriteria().andEqualTo("uid",id);

        //查找邀请码信息
        InvitePO invitePO = inviteMapper.selectOneByExample(example);
        String code = invitePO.getInviteCode();
        if (code == null) {
            return new ResponseVO(UserResponseEnum.INVITEDCODE_EMPTY);
        }
        Map<String, Object> map = new HashMap<>();
        //平台的注册地址
        String path = "https://worldtradebase.com/reg/?code=" + code;

        Integer num = 0;

        num = userDao.selectInviteNum(id);

        if(num == null){
            num = 0;
        }
        map.put("code", code);
        map.put("num", String.valueOf(num));
        map.put("qrurl", path);
        return new ResponseVO<>(UserResponseEnum.MEMBER_INVITE_SUCCESS, map);
    }

    @Override
    public ResponseVO findQrcodeById(Long id) {

        UserPO userPO = userDao.findQrcodeById(id);
        if (userPO == null) {
            return new ResponseVO(UserResponseEnum.USER_NOT_EXIST);
        }

        String fileName = "jpg";
        //用户公钥
        String qrcode = userPO.getPublickey();

        if (qrcode == null) {
            return new ResponseVO(UserResponseEnum.QRCODE_NOT_EXIST);
        }
        //将用户公钥制作成二维码，并返回二维码文件地址
        String fileUrl = qrCodeUtil.createQrCode(qrcode, fileName);
        //拼接二维码存储地址
        String filePath = FastDFSClient.getResAccessUrl(fileUrl);

        Map<String, Object> map = new HashMap<>();
        map.put("uid", String.valueOf(userPO.getUid()));

        map.put("qrcode", filePath);
        map.put("name", userPO.getName());

        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setQrCode(fileUrl);
        //将生成的二维码图片存入bp_user表
        userDao.updateUser(userDTO);
        return new ResponseVO<>(UserResponseEnum.MEMBER_QRCODE_SUCCESS, map);
    }

    @Override
    public ResponseVO getUserInfoListFromPlatform(PagePointDTO pagePointDTO) {

        Example example = new Example(PlatformInfoPO.class);
        example.createCriteria().andEqualTo("uid",pagePointDTO.getId());
        PlatformInfoPO platformInfoPO = platformInfoMapper.selectOneByExample(example);
       if(platformInfoPO == null){
           return new ResponseVO(CommonResponseEnum.PLATFORM_NOT_EXIST);
       }

        if (pagePointDTO.getPage() == null || pagePointDTO.getPage() < 1) {
            pagePointDTO.setPage(1);
        }
        if (pagePointDTO.getPageSize() == null) {
            pagePointDTO.setPageSize(pageSize);
        }
        Page page = PageHelper.startPage(pagePointDTO.getPage(), pagePointDTO.getPageSize());
        List<UserPO> userPOList = userDao.findUserInfoFromPlatform(pagePointDTO);
        Integer total = Integer.parseInt(String.valueOf(page.getTotal()));
        PageBean pageBean = new PageBean<>(userPOList, pagePointDTO.getPage(), pagePointDTO.getPageSize(), total);
        return new ResponseVO<>(UserResponseEnum.FIND_USERINFO_PLATFORM_SUCCESS, pageBean);
    }

    @Override
    public ResponseVO findInvite(PagePointDTO pagePointDTO) {

        ExchangePointDTO exchangeDTO = new ExchangePointDTO();
        exchangeDTO.setUid(pagePointDTO.getId());
        Long platFormId = memberPointDao.findPlatformIdByPlatformUid(exchangeDTO);
        //平台不存在则报错
        if (platFormId == null) {
            return new ResponseVO(CommonResponseEnum.PLATFORM_NOT_EXIST);
        }

        if (pagePointDTO.getPage() == null || pagePointDTO.getPage() < 1) {
            pagePointDTO.setPage(1);
        }
        if (pagePointDTO.getPageSize() == null) {
            pagePointDTO.setPageSize(pageSize);
        }
        Page page = PageHelper.startPage(pagePointDTO.getPage(), pagePointDTO.getPageSize());

        //查询平台下的邀请码信息
        List<InvitePO> invitePOList = userDao.findInviteList(pagePointDTO);

        Integer total = Integer.parseInt(String.valueOf(page.getTotal()));
        PageBean pageBean = new PageBean<>(invitePOList, pagePointDTO.getPage(), pagePointDTO.getPageSize(), total);
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, pageBean);
    }

    @Override
    public ResponseVO findUserInfoByIdAPP(Long id) {

        //查询用户
        AccountPO accountPO = accountMapper.selectByPrimaryKey(id);

        if (accountPO == null) {
            return new ResponseVO(UserResponseEnum.USER_NOT_EXIST);
        }

        UserInfoAPPVO userPO;
        //查找用户信息
        userPO = userDao.findUserInfoByIdAPP(id);

        String fileName = "jpg";
        //用户公钥
        String qrcode = userPO.getPublickey();

        if (qrcode == null) {
            return new ResponseVO(UserResponseEnum.QRCODE_NOT_EXIST);
        }
        //将用户公钥制作成二维码，并返回二维码文件地址
        String fileUrl = qrCodeUtil.createQrCode(qrcode, fileName);
        //拼接二维码存储地址
        String filePath = FastDFSClient.getResAccessUrl(fileUrl);

        userPO.setFilePath(filePath);



        log.info("查询成功");
        return new ResponseVO<>(UserResponseEnum.MEMBER_INFO_SUCCESS, userPO);

    }

    /**
     * 查询用户持有对应集团的可用会员积分总数
     * @param uid uid
     * @param gid gid
     * @return
     */
    @Override
    public ResponseVO getCompanyPoint(Long uid, Long gid) {
        BigDecimal points = memberPointDao.getCompanyPointSum(uid, gid);
        if (points == null) {
            points = new BigDecimal("0");
        }
        Map<String, BigDecimal> map = new HashMap<>(4);
        map.put("points", points);
        return new ResponseVO(UserResponseEnum.QUERY_POINT_SUCCESS, map);
    }




}