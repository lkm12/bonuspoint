package com.fuzamei.bonuspoint.service.impl.user.company;

import brokerpoints.Brokerpoints;
import com.fuzamei.bonuspoint.blockchain.bc.PlatformBC;
import com.fuzamei.bonuspoint.blockchain.util.BlockChainUtil;
import com.fuzamei.bonuspoint.constant.*;
import com.fuzamei.bonuspoint.dao.account.AccountDao;
import com.fuzamei.bonuspoint.dao.block.BlockInfoDao;
import com.fuzamei.bonuspoint.dao.common.mapper.CompanyInfoMapper;
import com.fuzamei.bonuspoint.dao.common.mapper.GeneralPointRelationMapper;
import com.fuzamei.bonuspoint.dao.common.mapper.GoodMapper;
import com.fuzamei.bonuspoint.dao.common.mapper.PointInfoMapper;
import com.fuzamei.bonuspoint.dao.good.GoodDao;
import com.fuzamei.bonuspoint.dao.reward.RewardMapper;
import com.fuzamei.bonuspoint.dao.user.CompanyInfoDao;
import com.fuzamei.bonuspoint.entity.dto.account.AccountDTO;
import com.fuzamei.bonuspoint.entity.dto.user.CompanyInfoDTO;
import com.fuzamei.bonuspoint.entity.dto.user.KeyDTO;
import com.fuzamei.bonuspoint.entity.po.account.AccountPO;
import com.fuzamei.bonuspoint.entity.po.block.BlockInfoPO;
import com.fuzamei.bonuspoint.entity.po.good.GoodPO;
import com.fuzamei.bonuspoint.entity.po.point.GeneralPointRelationPO;
import com.fuzamei.bonuspoint.entity.po.point.PointInfoPO;
import com.fuzamei.bonuspoint.entity.po.reward.RewardPO;
import com.fuzamei.bonuspoint.entity.po.user.CompanyInfoPO;
import com.fuzamei.bonuspoint.entity.vo.user.CompanyBaseInfoVO;
import com.fuzamei.bonuspoint.enums.BlockChainResponseEnum;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.enums.SafeResponseEnum;
import com.fuzamei.bonuspoint.enums.UserResponseEnum;
import com.fuzamei.bonuspoint.service.point.PointInfoService;
import com.fuzamei.bonuspoint.service.user.company.CompanyInfoService;
import com.fuzamei.bonuspoint.util.StringUtil;
import com.fuzamei.common.bean.PageBean;
import com.fuzamei.common.model.vo.ResponseVO;
import com.fzm.blockchain.entity.ResponseBean;
import com.fzm.blockchain.entity.TransactionResultBean;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/5/14 14:59
 */
@Slf4j
@Service
@RefreshScope
@Transactional(rollbackFor = {RuntimeException.class, Exception.class})
public class CompanyInfoServiceImpl implements CompanyInfoService {

    private static final String RESULT = "result";

    @Value("${page.currentPage}")
    private Integer currentPage;

    @Value("${page.pageSize}")
    private Integer pageSize;

    @Value("${method.chain}")
    private boolean chainSwitch;

    private final CompanyInfoDao companyInfoDao;
    private final CompanyInfoMapper companyInfoMapper;

    private final BlockInfoDao blockInfoDao;
    private final AccountDao accountDao;
    private final GeneralPointRelationMapper generalPointRelationMapper;
    private final PlatformBC platformBC;
    private final BlockChainUtil blockChainUtil;
    private final PointInfoMapper pointInfoMapper;
    private final PointInfoService pointInfoService;
    private final GoodMapper goodMapper;
    private final GoodDao goodDao;
    private final RewardMapper rewardMapper;

    @Autowired
    public CompanyInfoServiceImpl(CompanyInfoDao companyInfoDao, CompanyInfoMapper companyInfoMapper, BlockInfoDao blockInfoDao, AccountDao accountDao, GeneralPointRelationMapper generalPointRelationMapper, PlatformBC platformBC, BlockChainUtil blockChainUtil, PointInfoMapper pointInfoMapper, PointInfoService pointInfoService, GoodMapper goodMapper, GoodDao goodDao, RewardMapper rewardMapper) {
        this.companyInfoDao = companyInfoDao;
        this.companyInfoMapper = companyInfoMapper;
        this.blockInfoDao = blockInfoDao;
        this.accountDao = accountDao;
        this.generalPointRelationMapper = generalPointRelationMapper;
        this.platformBC = platformBC;
        this.blockChainUtil = blockChainUtil;
        this.pointInfoMapper = pointInfoMapper;
        this.pointInfoService = pointInfoService;
        this.goodMapper = goodMapper;
        this.goodDao = goodDao;
        this.rewardMapper = rewardMapper;
    }

    /**
     * 获取单个集团信息
     *
     * @param uid uid
     * @return 请求响应
     */
    @Override
    public ResponseVO getCompanyInfo(Long uid) {
        CompanyInfoPO companyInfoPO = companyInfoDao.getCompanyInfo(uid);
        log.info("集团查询成功");
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, companyInfoPO);
    }

    @Override
    public ResponseVO getCompanyInfoList(CompanyInfoDTO companyInfoDTO) {
        List<String> fields = new ArrayList<>(8);
        fields.add("bp_company_info.id");
        fields.add("uid");
        fields.add("username");
        fields.add("company_name");
        fields.add("company_leader");
        fields.add("company_leader_mobile");
        fields.add("company_telephone");
        fields.add("company_email");
        fields.add("company_address");
        fields.add("bp_user.created_at");
        if (companyInfoDTO.getPage() == null || companyInfoDTO.getPage() < 1) {
            companyInfoDTO.setPage(1);
        }
        if (companyInfoDTO.getPageSize() == null) {
            companyInfoDTO.setPageSize(pageSize);
        }

        Page page = PageHelper.startPage(companyInfoDTO.getPage(), companyInfoDTO.getPageSize());
        List<CompanyInfoDTO> companyInfoList = companyInfoDao.getCampanyInfoList(fields, companyInfoDTO);
        Integer total = Integer.parseInt(String.valueOf(page.getTotal()));
        PageBean pageBean = new PageBean<>(companyInfoList, companyInfoDTO.getPage(), companyInfoDTO.getPageSize(), total);
        return new ResponseVO<>(CommonResponseEnum.QUERY_SUCCESS, pageBean);
    }

    /**
     * 获取集团信息列表
     *
     * @param
     * @return 请求响应
     */
 /*   @Override
    public ResponseVO listCompanyInfo() {
        List<CompanyInfoPO> companyInfoPOList = companyInfoDao.listCompanyInfo();
        log.info("集团查询成功");
        return new ResponseVO<>(ResponseEnum.SUCCESS, companyInfoPOList);
    }*/
    @Override
    public Long getCompanyIdByUid(Long uid) {
        CompanyInfoPO companyInfoPO = companyInfoDao.getCompanyInfo(uid);
        Long id = 0L;
        if (companyInfoPO != null) {
            id = companyInfoPO.getId();
        } else {
            log.info("CompanyInfoServiceImpl  getCompanyIdByUid uid:{}，未查到id", uid);
        }
        return id;
    }


    /**
     * 修改集团信息
     *
     * @param companyInfoDTO
     * @return
     */
    @Override
    public ResponseVO updateCompanyInfo(CompanyInfoDTO companyInfoDTO) {
        int result = companyInfoDao.updateCompanyInfo(companyInfoDTO);
        if (result == 1) {
            Map<String, Long> map = new HashMap<>(4);
            map.put("uid", companyInfoDTO.getId());
            return new ResponseVO<>(SafeResponseEnum.UPDATE_SUCCESS, map);
        }
        return new ResponseVO(SafeResponseEnum.UPDATE_FAIL);
    }


    @Override
    public ResponseVO setCompanyCashRate(CompanyInfoDTO companyInfoDTO) {
        int result = companyInfoDao.updateCompanyInfo(companyInfoDTO);
        if (result == 1) {
            if (chainSwitch) {
                KeyDTO platformKey = accountDao.getUserKeyById(companyInfoDTO.getPId());

                ResponseBean<String> responseBean = platformBC.setCashRate(companyInfoDTO.getId(), companyInfoDTO.getCashRate().toString(), platformKey.getPublicKey(), platformKey.getPrivateKey());

                ResponseBean<TransactionResultBean> queryTransaction = blockChainUtil.queryTransaction(responseBean.getResult());
                if (queryTransaction == null) {
                    log.info("上链失败");
                    throw new RuntimeException(BlockChainResponseEnum.ADD_BLOCK_FAILED.getMessage());
                }
                if (StringUtil.isNotBlank(queryTransaction.getError())) {
                    log.info("上链失败");
                    throw new RuntimeException(BlockChainResponseEnum.ADD_BLOCK_FAILED.getMessage());
                }
                log.info("上链成功");
                Long height = queryTransaction.getResult().getHeight();
                String hash = responseBean.getResult();

                BlockInfoPO blockInfoPO = new BlockInfoPO(companyInfoDTO.getPId(), height, hash, System.currentTimeMillis(), Brokerpoints.ActionType.SetCashRate_VALUE);
                blockInfoDao.insertBlockInfo(blockInfoPO);

            }
            return new ResponseVO<>(SafeResponseEnum.UPDATE_SUCCESS);
        }

        return new ResponseVO<>(SafeResponseEnum.UPDATE_FAIL);
    }

    @Override
    public ResponseVO setCompanyPointRate(CompanyInfoDTO companyInfoDTO) {
        int result = companyInfoDao.updateCompanyInfo(companyInfoDTO);
        if (result == 1) {
            if (chainSwitch) {

                KeyDTO platformKey = accountDao.getUserKeyById(companyInfoDTO.getPId());
                ResponseBean<String> responseBean = platformBC.setCompanyPointRate(companyInfoDTO.getId(), companyInfoDTO.getPointRate().toString(), platformKey.getPublicKey(), platformKey.getPrivateKey());

                ResponseBean<TransactionResultBean> queryTransaction = blockChainUtil.queryTransaction(responseBean.getResult());
                if (queryTransaction == null) {
                    log.info("上链失败");
                    throw new RuntimeException(BlockChainResponseEnum.ADD_BLOCK_FAILED.getMessage());
                }
                if (StringUtil.isNotBlank(queryTransaction.getError())) {
                    log.info("上链失败");
                    throw new RuntimeException(BlockChainResponseEnum.ADD_BLOCK_FAILED.getMessage());
                }
                log.info("上链成功");
                Long height = queryTransaction.getResult().getHeight();
                String hash = responseBean.getResult();

                BlockInfoPO blockInfoPO = new BlockInfoPO(companyInfoDTO.getPId(), height, hash, System.currentTimeMillis(), Brokerpoints.ActionType.SetPointRate_VALUE);
                blockInfoDao.insertBlockInfo(blockInfoPO);
            }
            return new ResponseVO<>(SafeResponseEnum.UPDATE_SUCCESS);
        }
        return new ResponseVO<>(SafeResponseEnum.UPDATE_FAIL);
    }

    @Override
    public ResponseVO createCompany(AccountDTO accountDTO, CompanyInfoDTO companyInfoDTO) {
        accountDTO.setStatus(UserStatus.AVAILABLE);
        accountDTO.setIsInitialize(UserStatus.UNINIT);
        accountDTO.setCreatedAt(System.currentTimeMillis());
        AccountPO accountPO = new AccountPO();
        BeanUtils.copyProperties(accountDTO, accountPO);
        int result = accountDao.addAccount(accountPO);
        if (result == 1) {
            //添加通用积分数量
            GeneralPointRelationPO generalPointRelationPO = new GeneralPointRelationPO();
            generalPointRelationPO.setNum(BigDecimal.ZERO);
            generalPointRelationPO.setPlatformId(accountDTO.getPId());
            generalPointRelationPO.setUserId(accountPO.getId());
            generalPointRelationMapper.insertSelective(generalPointRelationPO);


            companyInfoDTO.setUid(accountPO.getId());
            CompanyInfoPO companyInfoPO = new CompanyInfoPO();
            BeanUtils.copyProperties(companyInfoDTO, companyInfoPO);
            companyInfoPO.setCreatedAt(System.currentTimeMillis());
            result = companyInfoDao.addCompanyInfo(companyInfoPO);


            if (result == 1) {
                if (chainSwitch) {
                    KeyDTO platformKey = accountDao.getUserKeyById(accountDTO.getPId());
                    ResponseBean<String> responseBean = platformBC.createGroup(companyInfoPO.getId(), accountPO.getPId(), companyInfoDTO.getCashRate().toString(), companyInfoDTO.getPointRate().toString(), platformKey.getPublicKey(), platformKey.getPrivateKey());
                    ResponseBean<TransactionResultBean> queryTransaction = blockChainUtil.queryTransaction(responseBean.getResult());
                    if (queryTransaction == null) {
                        log.info("上链失败");
                        throw new RuntimeException(BlockChainResponseEnum.ADD_BLOCK_FAILED.getMessage());
                    }
                    if (StringUtil.isNotBlank(queryTransaction.getError())) {
                        log.info("上链失败");
                        throw new RuntimeException(BlockChainResponseEnum.ADD_BLOCK_FAILED.getMessage());
                    }
                    log.info("上链成功");
                    Long height = queryTransaction.getResult().getHeight();
                    String hash = responseBean.getResult();
                    BlockInfoPO blockInfoPO = new BlockInfoPO(accountDTO.getPId(), height, hash, System.currentTimeMillis(), Brokerpoints.ActionType.CreateMerchant_VALUE);
                    blockInfoDao.insertBlockInfo(blockInfoPO);

                }
                return new ResponseVO<>(CommonResponseEnum.ADD_SUCCESS, accountPO);
            } else {
                throw new RuntimeException(UserResponseEnum.COMPANY_ADD_FAIL.getMessage());
            }
        }
        return new ResponseVO(UserResponseEnum.COMPANY_ADD_FAIL);
    }

    @Override
    public ResponseVO getCompanyCashRateList(CompanyInfoDTO companyInfoDTO) {
        companyInfoDTO.setOrderBy("cash_rate");
        ArrayList<String> fields = new ArrayList<>(3);
        fields.add("bp_company_info.id");
        fields.add("company_name");
        fields.add("cash_rate");

        if (companyInfoDTO.getPage() != null && companyInfoDTO.getPage() == -1 && companyInfoDTO.getPageSize() != null && companyInfoDTO.getPageSize() == -1) {
            List<CompanyInfoDTO> companyInfoDTOList = companyInfoDao.getCampanyInfoList(fields, companyInfoDTO);
            PageBean pageBean = new PageBean<>(companyInfoDTOList, 1, companyInfoDTOList.size(), companyInfoDTOList.size());
            return new ResponseVO<>(CommonResponseEnum.SUCCESS, pageBean);
        }
        if (companyInfoDTO.getPage() == null || companyInfoDTO.getPage() < 1) {
            companyInfoDTO.setPage(1);
        }
        if (companyInfoDTO.getPageSize() == null || companyInfoDTO.getPageSize() < 1) {
            companyInfoDTO.setPageSize(pageSize);
        }

        Page page = PageHelper.startPage(companyInfoDTO.getPage(), companyInfoDTO.getPageSize());
        List<CompanyInfoDTO> companyInfoPOList = companyInfoDao.getCampanyInfoList(fields, companyInfoDTO);
        int total = Integer.valueOf(String.valueOf(page.getTotal()));
        PageBean pageBean = new PageBean<>(companyInfoPOList, companyInfoDTO.getPage(), companyInfoDTO.getPageSize(), total);
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, pageBean);

    }

    @Override
    public ResponseVO getCompanyPointRateList(CompanyInfoDTO companyInfoDTO) {
        companyInfoDTO.setOrderBy("point_rate");
        ArrayList<String> fields = new ArrayList<>(3);
        fields.add("bp_company_info.id");
        fields.add("company_name");
        fields.add("point_rate");
        //不分页
        if (companyInfoDTO.getPage() != null && companyInfoDTO.getPage() == -1 && companyInfoDTO.getPageSize() != null && companyInfoDTO.getPageSize() == -1) {
            List<CompanyInfoDTO> companyInfoDTOList = companyInfoDao.getCampanyInfoList(fields, companyInfoDTO);
            PageBean pageBean = new PageBean<>(companyInfoDTOList, 1, companyInfoDTOList.size(), companyInfoDTOList.size());
            return new ResponseVO<>(CommonResponseEnum.SUCCESS, pageBean);
        }
        //分页
        if (companyInfoDTO.getPage() == null || companyInfoDTO.getPage() < 1) {
            companyInfoDTO.setPage(1);
        }
        if (companyInfoDTO.getPageSize() == null || companyInfoDTO.getPageSize() < 1) {
            companyInfoDTO.setPageSize(pageSize);
        }
        Page page = PageHelper.startPage(companyInfoDTO.getPage(), companyInfoDTO.getPageSize());
        List<CompanyInfoDTO> companyInfoDTOList = companyInfoDao.getCampanyInfoList(fields, companyInfoDTO);
        int total = Integer.valueOf(String.valueOf(page.getTotal()));
        PageBean pageBean = new PageBean<>(companyInfoDTOList, companyInfoDTO.getPage(), companyInfoDTO.getPageSize(), total);
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, pageBean);

    }

    @Override
    public ResponseVO<CompanyBaseInfoVO> getCompanyBaseInfo(Long uid) {
        CompanyInfoPO companyInfoPO = companyInfoDao.getCompanyInfo(uid);
        CompanyBaseInfoVO companyBaseInfoVO = new CompanyBaseInfoVO();
        BeanUtils.copyProperties(companyInfoPO, companyBaseInfoVO);
        return new ResponseVO<>(CommonResponseEnum.QUERY_SUCCESS, companyBaseInfoVO);
    }

    @Override
    public ResponseVO updateCompanyBaseInfo(CompanyInfoDTO companyInfoDTO) {

        int result = companyInfoDao.updateCompanyInfo(companyInfoDTO);
        if (result == 1) {
            return new ResponseVO(SafeResponseEnum.UPDATE_SUCCESS);
        }
        return new ResponseVO(SafeResponseEnum.UPDATE_FAIL);
    }

    @Override
    public ResponseVO deleteCompany(Long platformUid, Long companyId) {
        int result = companyInfoDao.deleteCompany(platformUid, companyId);
        if (result == 0){
            return new ResponseVO(CommonResponseEnum.DELETE_FAIL);
        }
        //该商家所有积分过期
        Example companyInfoExample = new Example(PointInfoPO.class);
        Example.Criteria companyInfoCriteria = companyInfoExample.createCriteria();
        companyInfoCriteria.andEqualTo("company", companyId);
        companyInfoCriteria.andNotEqualTo("status", PointInfoConstant.point_out_time);
        List<PointInfoPO> pointInfoPOList = pointInfoMapper.selectByExample(companyInfoExample);
        if (pointInfoPOList!=null && pointInfoPOList.size()>0) {
            pointInfoPOList.stream().map(pointInfoPO -> pointInfoService.setPointExpired(pointInfoPO.getId())).toArray();
        }

        //该商家所有商品下架
        Example goodExample = new Example(GoodPO.class);
        Example.Criteria goodCriteria = goodExample.createCriteria();
        goodCriteria.andEqualTo("gid", companyId);
        goodCriteria.andNotEqualTo("status", GoodStatusConstant.DROP);
        goodCriteria.andNotEqualTo("status", GoodStatusConstant.DELETE);
        List<GoodPO> goodPOList = goodMapper.selectByExample(goodExample);
        if (goodPOList!=null && goodPOList.size()>0){
            List<Long> goodIdList = goodPOList.stream().map(GoodPO::getId).collect(Collectors.toList());
            goodDao.dropGoods(goodIdList);
        }

        //该商户的奖励措施失效
        Example rewardExample = new Example(RewardPO.class);
        Example.Criteria rewardCriteria = rewardExample.createCriteria();
        rewardCriteria.andEqualTo("companyId", companyId);
        rewardCriteria.andNotEqualTo("status", RewardRuleStatus.DELETE);
        List<RewardPO>rewardPOList = rewardMapper.selectByExample(rewardExample);
        if (rewardPOList!=null && rewardPOList.size()>0){
            rewardPOList.stream().map(rewardPO -> {
                rewardPO.setStatus(RewardRuleStatus.DELETE);
                return  rewardMapper.updateByPrimaryKeySelective(rewardPO);
            }).toArray();
        }
        return new ResponseVO(CommonResponseEnum.DELETE_SUCCESS);
    }

    @Override
    public List<CompanyInfoDTO> getAllCompanyInSamePlatform(Long platformUid) {

        CompanyInfoDTO companyInfoDTO = new CompanyInfoDTO();
        companyInfoDTO.setPId(platformUid);
        List<String> fields = Arrays.asList("bp_company_info.id","company_name");
        return  companyInfoDao.getCampanyInfoList(fields,companyInfoDTO);
    }
}
