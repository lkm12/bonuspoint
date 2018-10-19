package com.fuzamei.bonuspoint.service.impl.reward;

import com.fuzamei.bonuspoint.constant.PointInfoConstant;
import com.fuzamei.bonuspoint.constant.PointType;
import com.fuzamei.bonuspoint.constant.RewardRuleStatus;
import com.fuzamei.bonuspoint.dao.common.mapper.CompanyInfoMapper;
import com.fuzamei.bonuspoint.dao.common.mapper.GeneralPointInfoMapper;
import com.fuzamei.bonuspoint.dao.common.mapper.PointInfoMapper;
import com.fuzamei.bonuspoint.dao.reward.RewardMapper;
import com.fuzamei.bonuspoint.entity.dto.reward.RewardDTO;
import com.fuzamei.bonuspoint.entity.po.point.GeneralPointInfoPO;
import com.fuzamei.bonuspoint.entity.po.point.PointInfoPO;
import com.fuzamei.bonuspoint.entity.po.reward.RewardPO;
import com.fuzamei.bonuspoint.entity.po.user.CompanyInfoPO;
import com.fuzamei.bonuspoint.entity.query.RewardQuery;
import com.fuzamei.bonuspoint.entity.vo.RewardVO.RewardVO;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.service.reward.RewardService;
import com.fuzamei.bonuspoint.util.ThreadPoolUtil;
import com.fuzamei.bonuspoint.util.TimeUtil;
import com.fuzamei.common.bean.PageBean;
import com.fuzamei.common.model.vo.ResponseVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @program: bonus-point-cloud
 * @description: 邀请奖励service
 * @author: WangJie
 * @create: 2018-09-11 18:10
 **/
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class RewardServiceImpl implements RewardService {

    @Value("${page.pageSize}")
    private Integer pageSize;
    @Value("${page.maxSize}")
    private Integer maxSize;
    @Value("${reward.minPointNum}")
    private Integer minPointNum;
    private final RewardMapper rewardMapper;
    private final GeneralPointInfoMapper generalPointInfoMapper;
    private final PointInfoMapper pointInfoMapper;
    private final CompanyInfoMapper companyInfoMapper;

    @Autowired
    public RewardServiceImpl(RewardMapper rewardMapper, GeneralPointInfoMapper generalPointInfoMapper, PointInfoMapper pointInfoMapper, CompanyInfoMapper companyInfoMapper) {
        this.rewardMapper = rewardMapper;
        this.generalPointInfoMapper = generalPointInfoMapper;
        this.pointInfoMapper = pointInfoMapper;
        this.companyInfoMapper = companyInfoMapper;
    }

    @Override
    public ResponseVO addRewardRule(RewardDTO rewardDTO) {
        if (PointType.GENERAL_POINT.equals(rewardDTO.getPointType())) {
            GeneralPointInfoPO generalPointInfoPO = new GeneralPointInfoPO();
            generalPointInfoPO.setPlatformId(rewardDTO.getPlatformId());
            generalPointInfoPO = generalPointInfoMapper.selectOne(generalPointInfoPO);
            rewardDTO.setPointId(generalPointInfoPO.getId());
            rewardDTO.setCompanyId(null);
        } else {
            if (rewardDTO.getPointId() == null || rewardDTO.getCompanyId() == null) {
                return new ResponseVO(CommonResponseEnum.PARAMETER_BLANK);
            }
            PointInfoPO pointInfoPO = new PointInfoPO();
            pointInfoPO.setIssuePlatform(rewardDTO.getPlatformId());
            pointInfoPO.setCompany(rewardDTO.getCompanyId());
            pointInfoPO.setId(rewardDTO.getPointId());
            pointInfoPO.setStatus(PointInfoConstant.CHECK_PASS);
            int result = pointInfoMapper.selectCount(pointInfoPO);
            if ( result!= 1)
            {
                log.info("添加奖励规则service，商户积分不存在，或状态不是已审核，添加失败。");
                return new ResponseVO(CommonResponseEnum.ADD_FAIL);
            }
        }
        RewardPO rewardPO = new RewardPO();
        rewardPO.setStatus(RewardRuleStatus.PENDING_EFFECTIVE);
        BeanUtils.copyProperties(rewardDTO, rewardPO);
        rewardPO.setCreatedAt(TimeUtil.timestamp());
        int result = rewardMapper.insertSelective(rewardPO);
        if (result == 1) {
            return new ResponseVO(CommonResponseEnum.ADD_SUCCESS);
        } else {
            log.error("添加奖励规则，写入数据库失败");
            return new ResponseVO(CommonResponseEnum.ADD_FAIL);
        }
    }

    @Override
    public ResponseVO effectiveRewardRule(Long id, Long platformId) {
        Example example = new Example(RewardPO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", id);
        criteria.andEqualTo("platformId", platformId);
        criteria.andEqualTo("status", RewardRuleStatus.PENDING_EFFECTIVE);
        RewardPO rewardPO = new RewardPO();
        rewardPO.setStatus(RewardRuleStatus.EFFECTIVE);
        int result = rewardMapper.updateByExampleSelective(rewardPO, example);
        if (result == 1) {
            return new ResponseVO(CommonResponseEnum.SUCCESS);
        }
        return new ResponseVO(CommonResponseEnum.UPDATE_FALL);
    }

    @Override
    public ResponseVO stopReward(Long id, Long platformId) {
        Example example = new Example(RewardPO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", id);
        criteria.andEqualTo("platformId", platformId);
        criteria.andEqualTo("status", RewardRuleStatus.EFFECTIVE);
        RewardPO rewardPO = new RewardPO();
        rewardPO.setStatus(RewardRuleStatus.PENDING_EFFECTIVE);
        int result = rewardMapper.updateByExampleSelective(rewardPO, example);
        if (result == 1) {
            return new ResponseVO(CommonResponseEnum.SUCCESS);
        }
        return new ResponseVO(CommonResponseEnum.UPDATE_FALL);
    }

    @Override
    public ResponseVO deleteReward(Long id, Long platformId) {
        Example example = new Example(RewardPO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", id);
        criteria.andEqualTo("platformId", platformId);
        criteria.andEqualTo("status", RewardRuleStatus.PENDING_EFFECTIVE);
        RewardPO rewardPO = new RewardPO();
        rewardPO.setStatus(RewardRuleStatus.DELETE);
        int result = rewardMapper.updateByExampleSelective(rewardPO, example);
        if (result == 1) {
            return new ResponseVO(CommonResponseEnum.DELETE_SUCCESS);
        }
        return new ResponseVO(CommonResponseEnum.DELETE_FAIL);
    }

    @Override
    public ResponseVO listRewardRules(RewardQuery rewardQuery) {

        if (rewardQuery.getPage() == null || rewardQuery.getPage() < 1) {
            rewardQuery.setPage(1);
        }
        if (rewardQuery.getPageSize() == null || rewardQuery.getPageSize() < 1) {
            rewardQuery.setPageSize(pageSize);
        }
        if (rewardQuery.getPageSize() > maxSize) {
            rewardQuery.setPageSize(maxSize);
        }
        Example example = new Example(RewardPO.class);
        Example.Criteria criteria = example.createCriteria();
        example.setOrderByClause("created_at desc");
        criteria.andEqualTo("platformId", rewardQuery.getPlatformId());
        if (rewardQuery.getFuzzyMatch() != null) {
            criteria.andEqualTo("rewardName", rewardQuery.getFuzzyMatch());
        }
        if (rewardQuery.getStatus() != null) {
            criteria.andEqualTo("status", rewardQuery.getStatus());
        }
        Page page = PageHelper.startPage(rewardQuery.getPage(), rewardQuery.getPageSize());
        List<RewardPO> rewardPOList = rewardMapper.selectByExample(example);
        List<RewardVO> rewardVOList = new ArrayList<>();
        CompletableFuture[] futures = rewardPOList.stream()
                .map(rewardPO -> CompletableFuture.runAsync(
                        () -> {
                            RewardVO rewardVO = new RewardVO();
                            BeanUtils.copyProperties(rewardPO, rewardVO);
                            if (rewardPO.getPointType().equals(PointType.COMPANY_POINT)) {
                                CompanyInfoPO companyInfoPO = companyInfoMapper.selectByPrimaryKey(rewardPO.getCompanyId());
                                rewardVO.setCompanyName(companyInfoPO.getCompanyName());

                                PointInfoPO pointInfoPO = pointInfoMapper.selectByPrimaryKey(rewardPO.getPointId());
                                rewardVO.setNumRemain(pointInfoPO.getNumRemain());
                                rewardVO.setPointName(pointInfoPO.getName());
                            }
                            rewardVOList.add(rewardVO);
                        }, ThreadPoolUtil.getExecutorService())
                )
                .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(futures).join();
        int total = Integer.valueOf(String.valueOf(page.getTotal()));
        PageBean<RewardVO> pageBean = new PageBean<>(rewardVOList, rewardQuery.getPage(), rewardQuery.getPageSize(), total);
        return new ResponseVO<>(CommonResponseEnum.QUERY_SUCCESS,pageBean);
    }

    @Override
    public ResponseVO listCompanyRewardPoints(Long companyId,Long platformId) {
        Example example = new Example(PointInfoPO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("issuePlatform",platformId);
        criteria.andEqualTo("company",companyId);
        criteria.andEqualTo("status",PointInfoConstant.CHECK_PASS);
        criteria.andGreaterThanOrEqualTo("numRemain",minPointNum);
        List<PointInfoPO> pointInfoPOList = pointInfoMapper.selectByExample(example);
        return new ResponseVO(CommonResponseEnum.QUERY_SUCCESS,pointInfoPOList);
    }
}
