package com.fuzamei.bonuspoint.service.impl.asset;

import brokerpoints.Brokerpoints;
import com.fuzamei.bonuspoint.blockchain.bc.CompanyBC;
import com.fuzamei.bonuspoint.blockchain.util.BlockChainUtil;
import com.fuzamei.bonuspoint.constant.CashRecordConstant;
import com.fuzamei.bonuspoint.dao.asset.PlatformCashDao;
import com.fuzamei.bonuspoint.dao.common.dao.CompanyInfoDao;
import com.fuzamei.bonuspoint.dao.common.mapper.*;
import com.fuzamei.bonuspoint.entity.dto.asset.PlatformCashRecordDTO;
import com.fuzamei.bonuspoint.entity.po.account.AccountPO;
import com.fuzamei.bonuspoint.entity.po.asset.CashRecordPO;
import com.fuzamei.bonuspoint.entity.po.block.BlockInfoPO;
import com.fuzamei.bonuspoint.entity.po.user.CompanyInfoPO;
import com.fuzamei.bonuspoint.entity.po.user.PlatformInfoPO;
import com.fuzamei.bonuspoint.entity.vo.asset.CashFlowVO;
import com.fuzamei.bonuspoint.entity.vo.asset.PlatformCashRecordVO;
import com.fuzamei.bonuspoint.enums.AssetResponseEnum;
import com.fuzamei.bonuspoint.enums.BlockChainResponseEnum;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.service.asset.PlatformCashService;
import com.fuzamei.bonuspoint.util.StringUtil;
import com.fuzamei.common.bean.PageBean;
import com.fuzamei.common.model.vo.ResponseVO;
import com.fzm.blockchain.entity.ResponseBean;
import com.fzm.blockchain.entity.TransactionResultBean;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/4/23
 */
@Slf4j
@Service
@RefreshScope
@Transactional
public class PlatformCashServiceImpl implements PlatformCashService {

    private static final String RESULT = "result";

    @Value("${page.currentPage}")
    private Integer currentPage;

    @Value("${page.pageSize}")
    private Integer pageSize;

    private final PlatformCashDao platformCashDao;

    private final CashRecordMapper cashRecordMapper;

    private final CompanyInfoMapper companyInfoMapper;

    private final CompanyInfoDao companyInfoDao;

    private final CompanyBC companyBC;

    private final BlockInfoMapper blockInfoMapper;

    private final BlockChainUtil blockChainUtil;

    private final AccountMapper accountMapper;

    private final PlatformInfoMapper platformInfoMapper;

    @Autowired
    public PlatformCashServiceImpl(PlatformCashDao platformCashDao, CashRecordMapper cashRecordMapper,
                                   CompanyInfoMapper companyInfoMapper, CompanyInfoDao companyInfoDao,
                                   CompanyBC companyBC, BlockInfoMapper blockInfoMapper,
                                   BlockChainUtil blockChainUtil, AccountMapper accountMapper, PlatformInfoMapper platformInfoMapper) {
        this.platformCashDao = platformCashDao;
        this.cashRecordMapper = cashRecordMapper;
        this.companyInfoMapper = companyInfoMapper;
        this.companyInfoDao = companyInfoDao;
        this.companyBC = companyBC;
        this.blockInfoMapper = blockInfoMapper;
        this.blockChainUtil = blockChainUtil;
        this.accountMapper = accountMapper;
        this.platformInfoMapper = platformInfoMapper;
    }

    /**
     * @param platformCashRecordDTO 分页查询数据传输类
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取会员用户资产信息
     */
    @Override
    public ResponseVO<PageBean<PlatformCashRecordVO>> listMemberCashRecord(PlatformCashRecordDTO platformCashRecordDTO) {
        //设置当前页，没有则使用默认值1
        if (platformCashRecordDTO.getCurrentPage() == null) {
            platformCashRecordDTO.setCurrentPage(currentPage);
        }
        //设置每页显示的总条数，没有则使用默认值10
        if (platformCashRecordDTO.getPageSize() == null) {
            platformCashRecordDTO.setPageSize(pageSize);
        }
        //使用PageHelper分页插件进行分页查询，只对下一行的查询结果进行分页
        Page<T> page = PageHelper.startPage(platformCashRecordDTO.getCurrentPage(), platformCashRecordDTO.getPageSize());
        //获取会员用户资产信息列表
        List<PlatformCashRecordVO> platformCashRecordVOList = platformCashDao.listMemberCashRecord(platformCashRecordDTO);
        //获取查询结果总条数
        Integer totalNum = Integer.parseInt(String.valueOf(page.getTotal()));
        //使用自定义分页器进行分页
        PageBean<PlatformCashRecordVO> pageBean = new PageBean<>(platformCashRecordVOList, platformCashRecordDTO.getCurrentPage(), platformCashRecordDTO.getPageSize(), totalNum);
        //返回数据
        log.info("获取会员用户资产信息成功");
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, pageBean);
    }

    /**
     * @param platformCashRecordDTO 分页查询数据传输类
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取集团资产信息
     */
    @Override
    public ResponseVO<PageBean<PlatformCashRecordVO>> listCompanyCashRecord(PlatformCashRecordDTO platformCashRecordDTO) {
        //设置当前页，没有则使用默认值1
        if (platformCashRecordDTO.getCurrentPage() == null) {
            platformCashRecordDTO.setCurrentPage(currentPage);
        }
        //设置每页显示的总条数，没有则使用默认值10
        if (platformCashRecordDTO.getPageSize() == null) {
            platformCashRecordDTO.setPageSize(pageSize);
        }
        //使用PageHelper分页插件进行分页查询，只对下一行的查询结果进行分页
        Page<T> page = PageHelper.startPage(platformCashRecordDTO.getCurrentPage(), platformCashRecordDTO.getPageSize());
        //获取集团资产信息
        List<PlatformCashRecordVO> platformCashRecordVOList = platformCashDao.listCompanyCashRecord(platformCashRecordDTO);
        //获取查询结果总条数
        Integer totalNum = Integer.parseInt(String.valueOf(page.getTotal()));
        //使用自定义分页器进行分页
        PageBean<PlatformCashRecordVO> pageBean = new PageBean<>(platformCashRecordVOList, platformCashRecordDTO.getCurrentPage(), platformCashRecordDTO.getPageSize(), totalNum);
        //返回数据
        log.info("获取集团资产信息成功");
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, pageBean);
    }

    /**
     * @param platformCashRecordDTO 分页查询数据传输类
     * @return 请求响应
     * @author qbanxiaolis
     * @description 获取集团充值记录
     */
    @Override
    public ResponseVO<PageBean<PlatformCashRecordVO>> getRechargeCashRecord(PlatformCashRecordDTO platformCashRecordDTO) {
        //设置当前页，没有则使用默认值1
        if (platformCashRecordDTO.getCurrentPage() == null) {
            platformCashRecordDTO.setCurrentPage(currentPage);
        }
        //设置每页显示的总条数，没有则使用默认值10
        if (platformCashRecordDTO.getPageSize() == null) {
            platformCashRecordDTO.setPageSize(pageSize);
        }
        //使用PageHelper分页插件进行分页查询，只对下一行的查询结果进行分页
        Page<T> page = PageHelper.startPage(platformCashRecordDTO.getCurrentPage(), platformCashRecordDTO.getPageSize());
        //获取集团充值记录
        List<PlatformCashRecordVO> platformCashRecordVOList = platformCashDao.getRechargeCashRecord(platformCashRecordDTO);
        //获取查询结果总条数
        int totalNum = Integer.parseInt(String.valueOf(page.getTotal()));
        //使用自定义分页器进行分页
        PageBean<PlatformCashRecordVO> pageBean = new PageBean<>(platformCashRecordVOList, platformCashRecordDTO.getCurrentPage(), platformCashRecordDTO.getPageSize(), totalNum);
        //返回数据
        log.info("获取集团充值记录成功");
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, pageBean);
    }

    /**
     * @param platformCashRecordDTO 分页查询数据传输类
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取集团提现记录
     */
    @Override
    public ResponseVO<PageBean<PlatformCashRecordVO>> getWithdrawCashRecord(PlatformCashRecordDTO platformCashRecordDTO) {
        //设置当前页，没有则使用默认值1
        if (platformCashRecordDTO.getCurrentPage() == null) {
            platformCashRecordDTO.setCurrentPage(currentPage);
        }
        //设置每页显示的总条数，没有则使用默认值10
        if (platformCashRecordDTO.getPageSize() == null) {
            platformCashRecordDTO.setPageSize(pageSize);
        }
        //使用PageHelper分页插件进行分页查询，只对下一行的查询结果进行分页
        Page<T> page = PageHelper.startPage(platformCashRecordDTO.getCurrentPage(), platformCashRecordDTO.getPageSize());
        //获取集团提现记录
        List<PlatformCashRecordVO> platformCashRecordVOList = platformCashDao.getWithdrawCashRecord(platformCashRecordDTO);
        //获取查询结果总条数
        int totalNum = Integer.parseInt(String.valueOf(page.getTotal()));
        //使用自定义分页器进行分页
        PageBean<PlatformCashRecordVO> pageBean = new PageBean<>(platformCashRecordVOList, platformCashRecordDTO.getCurrentPage(), platformCashRecordDTO.getPageSize(), totalNum);
        //返回数据
        log.info("获取单个集团提现记录列表成功");
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, pageBean);
    }

    /**
     * @param platformCashRecordDTO 分页查询数据传输类
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取备付金充值列表
     */
    @Override
    public ResponseVO listRechargeCashRecord(PlatformCashRecordDTO platformCashRecordDTO) {
        if (platformCashRecordDTO.getCurrentPage() != null) {
            currentPage = platformCashRecordDTO.getCurrentPage();
        }
        if (platformCashRecordDTO.getPageSize() != null) {
            pageSize = platformCashRecordDTO.getPageSize();
        }
        Page<?> page = PageHelper.startPage(currentPage, pageSize);
        List<CompanyInfoPO> cashRecordPOList = platformCashDao.listRechargeCashRecord(platformCashRecordDTO);
        int totalNum = Integer.parseInt(String.valueOf(page.getTotal()));
        PageBean pageBean = new PageBean<>(cashRecordPOList, currentPage, pageSize, totalNum);
        //返回数据
        log.info("获取单个集团提现记录列表");
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, pageBean);
    }

    /**
     * @param uid 平台id
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取平台资产信息
     */
    @Override
    public ResponseVO<PlatformCashRecordVO> getPlatformCashRecord(Long uid) {
        //获取平台资产信息
        PlatformCashRecordVO platformCashRecord = platformCashDao.getPlatformCashRecord(uid);
        //返回数据
        log.info("获取平台资产信息成功");
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, platformCashRecord);
    }

    /**
     * @param platformCashRecordDTO 分页查询数据传输类
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取集团列表
     */
    @Override
    public ResponseVO listCompany(PlatformCashRecordDTO platformCashRecordDTO) {
        if (platformCashRecordDTO.getCurrentPage() != null) {
            currentPage = platformCashRecordDTO.getCurrentPage();
        }
        if (platformCashRecordDTO.getPageSize() != null) {
            pageSize = platformCashRecordDTO.getPageSize();
        }
        Page<?> page = PageHelper.startPage(currentPage, pageSize);
        List<CompanyInfoPO> cashRecordPOList = platformCashDao.listCompany(platformCashRecordDTO);
        int totalNum = Integer.parseInt(String.valueOf(page.getTotal()));
        PageBean pageBean = new PageBean<>(cashRecordPOList, currentPage, pageSize, totalNum);
        //返回数据
        log.info("获取集团列表成功");
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, pageBean);
    }

    /**
     * @param platformCashRecordDTO 分页查询数据传输类
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取备付金预警通知列表
     */
    @Override
    public ResponseVO<PageBean<PlatformCashRecordVO>> listProvisionsNotice(PlatformCashRecordDTO platformCashRecordDTO) {
        //设置当前页，没有则使用默认值1
        if (platformCashRecordDTO.getCurrentPage() == null) {
            platformCashRecordDTO.setCurrentPage(currentPage);
        }
        //设置每页显示的总条数，没有则使用默认值10
        if (platformCashRecordDTO.getPageSize() == null) {
            platformCashRecordDTO.setPageSize(pageSize);
        }
        //使用PageHelper分页插件进行分页查询，只对下一行的查询结果进行分页
        Page<T> page = PageHelper.startPage(platformCashRecordDTO.getCurrentPage(), platformCashRecordDTO.getPageSize());
        //获取备付金预警通知列表
        List<PlatformCashRecordVO> platformCashRecordVOList = platformCashDao.listProvisionsNotice(platformCashRecordDTO);
        //获取查询结果总条数
        int totalNum = Integer.parseInt(String.valueOf(page.getTotal()));
        //使用自定义分页器进行分页
        PageBean<PlatformCashRecordVO> pageBean = new PageBean<>(platformCashRecordVOList, platformCashRecordDTO.getCurrentPage(), platformCashRecordDTO.getPageSize(), totalNum);
        //返回数据
        log.info("获取备付金预警通知列表成功");
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, pageBean);
    }

    /**
     * @param platformCashRecordDTO 分页查询数据传输类
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取会员用户集团积分列表
     */
    @Override
    public ResponseVO listMemberPointCashRecord(PlatformCashRecordDTO platformCashRecordDTO) {
        //设置当前页，没有则使用默认值1
        if (platformCashRecordDTO.getCurrentPage() == null) {
            platformCashRecordDTO.setCurrentPage(currentPage);
        }
        //设置每页显示的总条数，没有则使用默认值10
        if (platformCashRecordDTO.getPageSize() == null) {
            platformCashRecordDTO.setPageSize(pageSize);
        }
        //使用PageHelper分页插件进行分页查询，只对下一行的查询结果进行分页
        Page<T> page = PageHelper.startPage(platformCashRecordDTO.getCurrentPage(), platformCashRecordDTO.getPageSize());
        //获取会员用户集团积分列表
        List<CompanyInfoPO> cashRecordPOList = platformCashDao.listMemberPointCashRecord(platformCashRecordDTO);
        int totalNum = Integer.parseInt(String.valueOf(page.getTotal()));
        PageBean pageBean = new PageBean<>(cashRecordPOList, platformCashRecordDTO.getCurrentPage(), platformCashRecordDTO.getPageSize(), totalNum);
        //返回数据
        log.info("获取会员用户集团积分列表成功");
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, pageBean);
    }

    /**
     * @param platformCashRecordDTO 数据传输类
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取会员用户集团积分详情
     */
    @Override
    public ResponseVO getMemberPointCashRecordDetail(PlatformCashRecordDTO platformCashRecordDTO) {
        CompanyInfoPO cashRecordPO = platformCashDao.getMemberPointCashRecordDetail(platformCashRecordDTO);
        //返回数据
        log.info("平台获取会员用户集团积分详情成功");
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, cashRecordPO);
    }

    /**
     * @param platformCashRecordDTO 分页查询数据传输类
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取集团比例信息列表
     */
    @Override
    public ResponseVO<PageBean<PlatformCashRecordVO>> listCompanyRateCashRecord(PlatformCashRecordDTO platformCashRecordDTO) {
        //设置当前页，没有则使用默认值1
        if (platformCashRecordDTO.getCurrentPage() == null) {
            platformCashRecordDTO.setCurrentPage(currentPage);
        }
        //设置每页显示的总条数，没有则使用默认值10
        if (platformCashRecordDTO.getPageSize() == null) {
            platformCashRecordDTO.setPageSize(pageSize);
        }
        //使用PageHelper分页插件进行分页查询，只对下一行的查询结果进行分页
        Page<T> page = PageHelper.startPage(platformCashRecordDTO.getCurrentPage(), platformCashRecordDTO.getPageSize());
        //获取集团比例信息列表
        List<PlatformCashRecordVO> platformCashRecordVOList = platformCashDao.listCompanyRateCashRecord(platformCashRecordDTO);
        //获取查询结果总条数
        int totalNum = Integer.parseInt(String.valueOf(page.getTotal()));
        //使用自定义分页器进行分页
        PageBean<PlatformCashRecordVO> pageBean = new PageBean<>(platformCashRecordVOList, platformCashRecordDTO.getCurrentPage(), platformCashRecordDTO.getPageSize(), totalNum);
        //返回数据
        log.info("获取集团比例信息列表成功");
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, pageBean);
    }

    /**
     * @param uid 会员id
     * @return 请求响应
     * @author qbanxiaoli
     * @description 备付金审核通过
     */
    @Override
    public ResponseVO checkRechargeCashRecord(Long uid) {
        //设置查询条件
        CashRecordPO c = new CashRecordPO();
        c.setId(uid);
        //查询备付金充值记录信息
        CashRecordPO cashRecordPO = cashRecordMapper.selectByPrimaryKey(c);
        if (cashRecordPO.getStatus() != CashRecordConstant.WAIT_FOR_CHECK) {
            return new ResponseVO(CommonResponseEnum.SUCCESS);
        }
        //设置查询条件
        AccountPO a = new AccountPO();
        a.setId(cashRecordPO.getUid());
        //查询用户信息
        AccountPO accountPO = accountMapper.selectByPrimaryKey(a);
        //设置查询条件
        PlatformInfoPO p = new PlatformInfoPO();
        p.setId(accountPO.getPId());
        //查询平台信息
        PlatformInfoPO platformInfoPO = platformInfoMapper.selectByPrimaryKey(p);
        //设置查询条件
        CompanyInfoPO i = new CompanyInfoPO();
        i.setUid(cashRecordPO.getUid());
        //查询集团信息
        CompanyInfoPO companyInfoPO = companyInfoMapper.selectOne(i);
        //设置备付金审核状态为通过
        cashRecordPO.setStatus(CashRecordConstant.CHECK_PASS);
        //更新集团备付金
        try {
            companyInfoDao.UpdateProvisions(companyInfoPO.getUid(), companyInfoPO.getAmount().add(cashRecordPO.getAmount()));
        } catch (Exception e) {
            e.printStackTrace();
            log.info("更新集团备付金失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseVO(CommonResponseEnum.FAILURE);
        }
//        //设置集团充值后平台备付金
//        platformInfoPO.setAmount(platformInfoPO.getAmount().add(cashRecordPO.getAmount()));
//        //增加平台备付金
//        try {
//            platformInfoMapper.updateByPrimaryKeySelective(platformInfoPO);
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.info("增加平台备付金失败");
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            return new ResponseVO(PointResponseEnum.BALANCE_POINTRE_FAILURE);
//        }
        //充值备付金上链
        ResponseBean<String> responseBean = companyBC.accessCash(companyInfoPO.getId().toString(), cashRecordPO.getAmount().toString(),
                true, accountPO.getPublicKey(), accountPO.getPrivateKey());
        ResponseBean<TransactionResultBean> queryTransaction = blockChainUtil.queryTransaction(responseBean.getResult());
        if (queryTransaction == null) {
            log.info("上链失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseVO(BlockChainResponseEnum.ADD_BLOCK_FAILED);
        }
        if (StringUtil.isNotBlank(queryTransaction.getError())) {
            log.info("上链失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseVO(BlockChainResponseEnum.ADD_BLOCK_FAILED);
        }
        log.info("上链成功");
        Long height = queryTransaction.getResult().getHeight();
        String hash = responseBean.getResult();
        cashRecordPO.setHash(hash);
        cashRecordPO.setHeight(height);
        //更新备付金充值记录
        try {
            cashRecordMapper.updateByPrimaryKeySelective(cashRecordPO);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("更新备付金充值审核通过记录失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseVO(CommonResponseEnum.FAILURE);
        }
        //装配上链实体类
        BlockInfoPO blockInfoPO = new BlockInfoPO(cashRecordPO.getUid(), height, hash, System.currentTimeMillis(), Brokerpoints.ActionType.AccessCash_VALUE);
        //保存上链记录
        try {
            blockInfoMapper.insert(blockInfoPO);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("保存充值备付金上链记录失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseVO(AssetResponseEnum.SAVE_RECORD_FAILURE);
        }
        // map最好规定好大小
        Map<String, Boolean> map = new HashMap<>(16);
        //备付金审核通过
        map.put(RESULT, true);
        //返回数据
        log.info("备付金审核已通过");
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, map);
    }

    /**
     * @param platformCashRecordDTO 审核信息数据传输类
     * @return 请求响应
     * @author qbanxiaoli
     * @description 备付金审核拒绝
     */
    @Override
    public ResponseVO refuseRechargeCashRecord(PlatformCashRecordDTO platformCashRecordDTO) {
        //设置查询条件
        CashRecordPO c = new CashRecordPO();
        c.setId(platformCashRecordDTO.getId());
        //查询备付金充值记录信息
        CashRecordPO cashRecordPO = cashRecordMapper.selectByPrimaryKey(c);
        if (cashRecordPO.getStatus() != CashRecordConstant.WAIT_FOR_CHECK) {
            return new ResponseVO(CommonResponseEnum.SUCCESS);
        }
        //设置备付金审核状态为不通过
        cashRecordPO.setStatus(CashRecordConstant.CHECK_NOT_PASS);
        //更新备付金充值记录
        try {
            cashRecordMapper.updateByPrimaryKeySelective(cashRecordPO);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("更新拒绝充值备付金记录失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseVO(CommonResponseEnum.FAILURE);
        }
        // map最好规定好大小
        Map<String, Boolean> map = new HashMap<>(16);
        map.put(RESULT, true);
        //返回数据
        log.info("备付金审核已拒绝");
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, map);
    }

    @Override
    public ResponseVO getPlatformCashFlow(PlatformCashRecordDTO platformCashRecordDTO) {
        //设置当前页，没有则使用默认值1
        if (platformCashRecordDTO.getCurrentPage() == null) {
            platformCashRecordDTO.setCurrentPage(currentPage);
        }
        //设置每页显示的总条数，没有则使用默认值10
        if (platformCashRecordDTO.getPageSize() == null) {
            platformCashRecordDTO.setPageSize(pageSize);
        }
        //使用PageHelper分页插件进行分页查询，只对下一行的查询结果进行分页
        Page<T> page = PageHelper.startPage(platformCashRecordDTO.getCurrentPage(), platformCashRecordDTO.getPageSize());
        //获取会员用户资产信息列表
        List<CashFlowVO> platformCashRecordVOList = platformCashDao.getPlatformCashFlow(platformCashRecordDTO);
        //获取查询结果总条数
        Integer totalNum = Integer.parseInt(String.valueOf(page.getTotal()));
        //使用自定义分页器进行分页
        PageBean<CashFlowVO> pageBean = new PageBean<>(platformCashRecordVOList, platformCashRecordDTO.getCurrentPage(), platformCashRecordDTO.getPageSize(), totalNum);
        //返回数据
        log.info("获取平台备付金流水成功");
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, pageBean);
    }


}
