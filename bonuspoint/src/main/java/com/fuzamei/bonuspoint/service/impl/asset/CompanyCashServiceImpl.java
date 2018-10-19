package com.fuzamei.bonuspoint.service.impl.asset;

import brokerpoints.Brokerpoints;
import com.fuzamei.bonuspoint.blockchain.bc.CompanyBC;
import com.fuzamei.bonuspoint.blockchain.util.BlockChainUtil;
import com.fuzamei.bonuspoint.dao.common.dao.CompanyInfoDao;
import com.fuzamei.bonuspoint.dao.common.mapper.*;
import com.fuzamei.bonuspoint.entity.converter.CashRecordPOAssembly;
import com.fuzamei.bonuspoint.entity.po.account.AccountPO;
import com.fuzamei.bonuspoint.entity.po.asset.CashRecordPO;
import com.fuzamei.bonuspoint.entity.po.block.BlockInfoPO;
import com.fuzamei.bonuspoint.entity.po.user.CompanyInfoPO;
import com.fuzamei.bonuspoint.entity.po.user.PlatformInfoPO;
import com.fuzamei.bonuspoint.entity.vo.asset.CashFlowVO;
import com.fuzamei.bonuspoint.enums.AssetResponseEnum;
import com.fuzamei.bonuspoint.enums.BlockChainResponseEnum;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.enums.PointResponseEnum;
import com.fuzamei.common.bean.PageBean;
import com.fuzamei.bonuspoint.constant.CashRecordConstant;
import com.fuzamei.bonuspoint.dao.asset.CompanyCashDao;
import com.fuzamei.bonuspoint.dao.point.CompanyPointDao;
import com.fuzamei.bonuspoint.entity.dto.asset.CompanyCashRecordDTO;
import com.fuzamei.common.model.vo.ResponseVO;
import com.fuzamei.bonuspoint.entity.vo.asset.CompanyCashRecordVO;
import com.fuzamei.bonuspoint.service.asset.CompanyCashService;
import com.fuzamei.bonuspoint.util.*;
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
 * @create 2018/4/17
 */
@Slf4j
@Service
@RefreshScope
@Transactional
public class CompanyCashServiceImpl implements CompanyCashService {

    private static final String RESULT = "result";

    @Value("${page.currentPage}")
    private Integer currentPage;

    @Value("${page.pageSize}")
    private Integer pageSize;

    @Value("${md5.salt}")
    private String MD5Salt;

    private final CompanyInfoMapper companyInfoMapper;

    private final CashRecordMapper cashRecordMapper;

    private final CompanyPointDao companyPointDao;

    private final CompanyCashDao companyCashDao;

    private final CompanyInfoDao companyInfoDao;

    private final CompanyBC companyBC;

    private final BlockChainUtil blockChainUtil;

    private final BlockInfoMapper blockInfoMapper;

    private final AccountMapper accountMapper;

    private final PlatformInfoMapper platformInfoMapper;

    @Autowired
    public CompanyCashServiceImpl(CompanyInfoMapper companyInfoMapper, CashRecordMapper cashRecordMapper,
                                  CompanyPointDao companyPointDao, CompanyCashDao companyCashDao,
                                  CompanyInfoDao companyInfoDao, CompanyBC companyBC, BlockChainUtil blockChainUtil, BlockInfoMapper blockInfoMapper, AccountMapper accountMapper, PlatformInfoMapper platformInfoMapper) {
        this.companyInfoMapper = companyInfoMapper;
        this.cashRecordMapper = cashRecordMapper;
        this.companyPointDao = companyPointDao;
        this.companyCashDao = companyCashDao;
        this.companyInfoDao = companyInfoDao;
        this.companyBC = companyBC;
        this.blockChainUtil = blockChainUtil;
        this.blockInfoMapper = blockInfoMapper;
        this.accountMapper = accountMapper;
        this.platformInfoMapper = platformInfoMapper;
    }

    /**
     * @param companyCashRecordDTO 充值记录分页查询数据传输类
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取单个集团充值记录
     */
    @Override
    public ResponseVO<PageBean<CompanyCashRecordVO>> getRechargeCashRecord(CompanyCashRecordDTO companyCashRecordDTO) {
        //设置当前页，没有则使用默认值1
        if (companyCashRecordDTO.getCurrentPage() == null) {
            companyCashRecordDTO.setCurrentPage(currentPage);
        }
        //设置每页显示的总条数，没有则使用默认值10
        if (companyCashRecordDTO.getPageSize() == null) {
            companyCashRecordDTO.setPageSize(pageSize);
        }
        //使用PageHelper分页插件进行分页查询，只对下一行的查询结果进行分页
        Page<T> page = PageHelper.startPage(companyCashRecordDTO.getCurrentPage(), companyCashRecordDTO.getPageSize());
        //获取单个集团充值记录
        List<CompanyCashRecordVO> companyCashRecordVOList = companyCashDao.getRechargeCashRecord(companyCashRecordDTO);
        //获取查询结果总条数
        int totalNum = Integer.parseInt(String.valueOf(page.getTotal()));
        //使用自定义分页器进行分页
        PageBean<CompanyCashRecordVO> pageBean = new PageBean<>(companyCashRecordVOList, companyCashRecordDTO.getCurrentPage(), companyCashRecordDTO.getPageSize(), totalNum);
        //返回数据
        log.info("获取单个集团充值记录成功");
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, pageBean);
    }

    /**
     * @param companyCashRecordDTO 提现记录分页查询数据传输类
     * @return 请求响应                                                                                                                           <
     * @author qbanxiaoli
     * @description 获取单个集团提现记录
     */
    @Override
    public ResponseVO<PageBean<CompanyCashRecordVO>> getWithdrawCashRecord(CompanyCashRecordDTO companyCashRecordDTO) {
        //设置当前页，没有则使用默认值1
        if (companyCashRecordDTO.getCurrentPage() == null) {
            companyCashRecordDTO.setCurrentPage(currentPage);
        }
        //设置每页显示的总条数，没有则使用默认值10
        if (companyCashRecordDTO.getPageSize() == null) {
            companyCashRecordDTO.setPageSize(pageSize);
        }
        //使用PageHelper分页插件进行分页查询，只对下一行的查询结果进行分页
        Page<T> page = PageHelper.startPage(companyCashRecordDTO.getCurrentPage(), companyCashRecordDTO.getPageSize());
        //获取单个集团提现记录
        List<CompanyCashRecordVO> companyCashRecordVOList = companyCashDao.getWithdrawCashRecord(companyCashRecordDTO);
        //获取查询结果总条数
        int totalNum = Integer.parseInt(String.valueOf(page.getTotal()));
        //使用自定义分页器进行分页
        PageBean<CompanyCashRecordVO> pageBean = new PageBean<>(companyCashRecordVOList, companyCashRecordDTO.getCurrentPage(), companyCashRecordDTO.getPageSize(), totalNum);
        //返回数据
        log.info("获取单个集团提现记录成功");
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, pageBean);
    }

    /**
     * @param uid 用户id
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取单个集团资产信息
     */
    @Override
    public ResponseVO<CompanyCashRecordVO> getCompanyCashRecord(Long uid) {
        //获取单个集团资产信息
        CompanyCashRecordVO companyCashRecordVO = companyCashDao.getCompanyCashRecord(uid);
        //返回数据
        log.info("获取单个集团资产信息成功");
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, companyCashRecordVO);
    }

    /**
     * @param uid 集团id
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取备付金信息
     */
    @Override
    public ResponseVO<CompanyInfoPO> getProvisionsCashRecord(Long uid) {
        //获取备付金信息
        CompanyInfoPO companyInfoPO = companyCashDao.getProvisionsCashRecord(uid);
        //返回数据
        log.info("获取备付金信息成功");
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, companyInfoPO);
    }

    /**
     * @param companyCashRecordDTO 充值信息数据传输类
     * @return 请求响应
     * @author qbanxiaoli
     * @description 申请充值备付金
     */
    @Override
    public ResponseVO saveRechargeCashRecord(CompanyCashRecordDTO companyCashRecordDTO) {
        String md5Hash;
        try {
            md5Hash = MD5HashUtil.md5SaltEncrypt(companyCashRecordDTO.getPayword(), MD5Salt);
        } catch (Exception e) {
            log.info("交易密码加密失败");
            return new ResponseVO(CommonResponseEnum.PAYWORD_WRONG);
        }
        //根据集团id和交易密码哈希判断是否存在
        int isPayWord = companyPointDao.isPayWord(companyCashRecordDTO.getUid(), md5Hash);
        if (isPayWord < 1) {
            log.info("集团不存在或交易密码错误");
            return new ResponseVO(PointResponseEnum.COMPANY_OR_PAYWORD_ERROR);
        }
        //设置查询条件
        AccountPO a = new AccountPO();
        a.setId(companyCashRecordDTO.getUid());
        //查询用户信息
        AccountPO accountPO = accountMapper.selectByPrimaryKey(a);
        //装配备付金充值资产记录
        CashRecordPO cashRecordPO = CashRecordPOAssembly.toDomain(companyCashRecordDTO);
        //设置对方用户id
        cashRecordPO.setOppositeUid(accountPO.getPId());
        //收入资产
        cashRecordPO.setType(CashRecordConstant.INCOME_ASSETS);
        //设置充值状态
        cashRecordPO.setStatus(CashRecordConstant.STATUS_NO_EXAM);
        //充值备付金
        cashRecordPO.setCategory(CashRecordConstant.RECHARGE_CASH);
        //保存备付金充值记录
        cashRecordMapper.insert(cashRecordPO);
        // map最好规定好大小
        Map<String, Boolean> map = new HashMap<>(16);
        map.put(RESULT, true);
        //返回数据
        log.info("申请充值备付金成功");
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, map);
    }

    /**
     * @param companyCashRecordDTO 提现信息数据传输类
     * @return 请求响应
     * @author qbanxiaoli
     * @description 集团提现
     */
    @Override
    public ResponseVO saveWithdrawCashRecord(CompanyCashRecordDTO companyCashRecordDTO) {
        String md5Hash;
        try {
            md5Hash = MD5HashUtil.md5SaltEncrypt(companyCashRecordDTO.getPayword(), MD5Salt);
        } catch (Exception e) {
            log.info("交易密码加密失败");
            return new ResponseVO(CommonResponseEnum.PAYWORD_WRONG);
        }
        //根据集团id和交易密码哈希判断是否存在
        int isPayWord = companyPointDao.isPayWord(companyCashRecordDTO.getUid(), md5Hash);
        if (isPayWord < 1) {
            log.info("集团不存在或交易密码错误");
            return new ResponseVO(PointResponseEnum.COMPANY_OR_PAYWORD_ERROR);
        }
        //设置查询条件
        AccountPO a = new AccountPO();
        a.setId(companyCashRecordDTO.getUid());
        //查询用户信息
        AccountPO accountPO = accountMapper.selectByPrimaryKey(a);
        //设置查询条件
        PlatformInfoPO p = new PlatformInfoPO();
        p.setId(accountPO.getPId());
        //查询平台信息
        PlatformInfoPO platformInfoPO = platformInfoMapper.selectByPrimaryKey(p);
        //设置查询条件
        CompanyInfoPO t = new CompanyInfoPO();
        t.setUid(companyCashRecordDTO.getUid());
        //查询集团信息
        CompanyInfoPO companyInfoPO = companyInfoMapper.selectOne(t);
        //判断集团备付金是否足够
        if (companyInfoPO.getAmount().compareTo(companyCashRecordDTO.getAmount()) < 0) {
            log.info("集团备付金不足");
            return new ResponseVO(AssetResponseEnum.COMPANY_PROVISIONS_LACK);
        }
        //更新集团备付金
        try {
            companyInfoDao.UpdateProvisions(companyCashRecordDTO.getUid(), companyInfoPO.getAmount().subtract(companyCashRecordDTO.getAmount()));
        } catch (Exception e) {
            e.printStackTrace();
            log.info("更新集团备付金失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseVO(AssetResponseEnum.UPDATE_CASH_FAILURE);
        }
//        //设置集团提现后平台备付金
//        platformInfoPO.setAmount(platformInfoPO.getAmount().subtract(companyCashRecordDTO.getAmount()));
//        //减少平台备付金
//        try {
//            platformInfoMapper.updateByPrimaryKeySelective(platformInfoPO);
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.info("减少平台备付金失败");
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            return new ResponseVO(PointResponseEnum.BALANCE_POINTRE_FAILURE);
//        }
        //集团提现上链
        ResponseBean<String> responseBean = companyBC.accessCash(companyInfoPO.getId().toString(), companyCashRecordDTO.getAmount().toString(),
                false, accountPO.getPublicKey(), accountPO.getPrivateKey());
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
        //装配集团提现资产记录
        CashRecordPO cashRecordPO = CashRecordPOAssembly.toDomain(companyCashRecordDTO);
        //设置对方用户id
        cashRecordPO.setOppositeUid(platformInfoPO.getId());
        //支出资产
        cashRecordPO.setType(CashRecordConstant.COST_ASSETS);
        //设置提现状态
        cashRecordPO.setStatus(CashRecordConstant.SUCCESS);
        //备付金提现
        cashRecordPO.setCategory(CashRecordConstant.WITHDRAW_CASH);
        cashRecordPO.setHash(hash);
        cashRecordPO.setHeight(height);
        //保存集团提现记录
        try {
            cashRecordMapper.insert(cashRecordPO);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("保存集团提现记录失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseVO(AssetResponseEnum.SAVE_RECORD_FAILURE);
        }
        //装配上链实体类
        BlockInfoPO blockInfoPO = new BlockInfoPO(companyCashRecordDTO.getUid(), height, hash, System.currentTimeMillis(), Brokerpoints.ActionType.AccessCash_VALUE);
        //保存上链记录
        try {
            blockInfoMapper.insert(blockInfoPO);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("保存集团提现上链记录失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseVO(AssetResponseEnum.SAVE_RECORD_FAILURE);
        }
        // map最好规定好大小
        Map<String, Boolean> map = new HashMap<>(16);
        map.put(RESULT, true);
        //返回数据
        log.info("集团提现成功");
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, map);
    }

    @Override
    public ResponseVO getCompanyCashFlow(CompanyCashRecordDTO companyCashRecordDTO) {

        //设置当前页，没有则使用默认值1
        if (companyCashRecordDTO.getCurrentPage() == null) {
            companyCashRecordDTO.setCurrentPage(currentPage);
        }
        //设置每页显示的总条数，没有则使用默认值10
        if (companyCashRecordDTO.getPageSize() == null) {
            companyCashRecordDTO.setPageSize(pageSize);
        }

        //使用PageHelper分页插件进行分页查询，只对下一行的查询结果进行分页
        Page<T> page = PageHelper.startPage(companyCashRecordDTO.getCurrentPage(), companyCashRecordDTO.getPageSize());
        //获取会员用户资产信息列表
        List<CashFlowVO> companyCashRecordVOList = companyCashDao.getCompanyCashFlow(companyCashRecordDTO);
        //获取查询结果总条数
        Integer totalNum = Integer.parseInt(String.valueOf(page.getTotal()));
        //使用自定义分页器进行分页
        PageBean<CashFlowVO> pageBean = new PageBean<>(companyCashRecordVOList, companyCashRecordDTO.getCurrentPage(), companyCashRecordDTO.getPageSize(), totalNum);
        //返回数据
        log.info("获取商户备付金流水成功");
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, pageBean);

    }

}
