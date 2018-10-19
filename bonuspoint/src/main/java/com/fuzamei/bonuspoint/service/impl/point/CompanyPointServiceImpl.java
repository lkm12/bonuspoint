/**
 * FileName: CompanyServiceImpl
 * Author: wangtao
 * Date: 2018/4/25 16:18
 * Description:
 */
package com.fuzamei.bonuspoint.service.impl.point;

import brokerpoints.Brokerpoints;
import com.fuzamei.bonuspoint.blockchain.bc.CompanyBC;
import com.fuzamei.bonuspoint.blockchain.bc.PublishPointBC;
import com.fuzamei.bonuspoint.blockchain.util.BlockChainUtil;
import com.fuzamei.bonuspoint.constant.*;
import com.fuzamei.bonuspoint.dao.block.BlockInfoDao;
import com.fuzamei.bonuspoint.dao.common.mapper.*;
import com.fuzamei.bonuspoint.dao.point.CompanyPointDao;
import com.fuzamei.bonuspoint.dao.user.CompanyInfoDao;
import com.fuzamei.bonuspoint.dao.user.UserDao;
import com.fuzamei.bonuspoint.entity.converter.CashRecordPOAssembly;
import com.fuzamei.bonuspoint.entity.converter.GeneralPointRecordPOAssembly;
import com.fuzamei.bonuspoint.entity.converter.PointRecordPOAssembly;
import com.fuzamei.bonuspoint.entity.converter.PointRelationPOAssembly;
import com.fuzamei.bonuspoint.entity.dto.data.excel.GoodExcelDTO;
import com.fuzamei.bonuspoint.entity.dto.point.*;
import com.fuzamei.bonuspoint.entity.po.account.AccountPO;
import com.fuzamei.bonuspoint.entity.po.asset.CashRecordPO;
import com.fuzamei.bonuspoint.entity.po.block.BlockInfoPO;
import com.fuzamei.bonuspoint.entity.po.data.excel.ExcelPO;
import com.fuzamei.bonuspoint.entity.po.point.*;
import com.fuzamei.bonuspoint.entity.po.user.CompanyInfoPO;
import com.fuzamei.bonuspoint.entity.po.user.PlatformInfoPO;
import com.fuzamei.bonuspoint.entity.po.user.UserPO;
import com.fuzamei.bonuspoint.entity.po.user.UserPrivatePo;
import com.fuzamei.bonuspoint.entity.vo.point.CompanyPointAssetsVO;
import com.fuzamei.bonuspoint.entity.vo.point.CompanyPointVO;
import com.fuzamei.bonuspoint.entity.vo.point.PointActivityVO;
import com.fuzamei.bonuspoint.enums.BlockChainResponseEnum;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.enums.PointResponseEnum;
import com.fuzamei.bonuspoint.enums.UserResponseEnum;
import com.fuzamei.bonuspoint.service.point.CompanyPointService;
import com.fuzamei.bonuspoint.util.MD5HashUtil;
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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangtao
 * @create 2018/4/25
 */
@Slf4j
@Service
@RefreshScope
@Transactional
public class CompanyPointServiceImpl implements CompanyPointService {

    private static final String RESULT = "result";

    private static final String ID = "id";

    @Value("${page.currentPage}")
    private Integer currentPage;

    @Value("${page.pageSize}")
    private Integer pageSize;

    @Value("${md5.salt}")
    private String MD5Salt;

    private final CompanyPointDao companyPointDao;

    private final UserDao userDao;

    private final UserMapper userMapper;

    private final CashRecordMapper cashRecordMapper;

    private final PointInfoMapper pointInfoMapper;

    private final PlatformInfoMapper platformInfoMapper;

    private final PointRecordMapper pointRecordMapper;

    private final GeneralPointRecordMapper generalPointRecordMapper;

    private final PointRelationMapper pointRelationMapper;

    private final CompanyInfoDao companyInfoDao;

    private final CompanyInfoMapper companyInfoMapper;

    private final GeneralPointRelationMapper generalPointRelationMapper;

    private final BlockChainUtil blockChainUtil;

    private final PublishPointBC publishPointBC;

    private final BlockInfoDao blockInfoDao;

    private final CompanyBC companyBC;

    private final BlockInfoMapper blockInfoMapper;

    @Autowired
    public CompanyPointServiceImpl(CompanyPointDao companyPointDao, UserDao userDao, UserMapper userMapper,
                                   CashRecordMapper cashRecordMapper, PointInfoMapper pointInfoMapper,
                                   PlatformInfoMapper platformInfoMapper, PointRecordMapper pointRecordMapper,
                                   GeneralPointRecordMapper generalPointRecordMapper, PointRelationMapper pointRelationMapper,
                                   CompanyInfoDao companyInfoDao, CompanyInfoMapper companyInfoMapper,
                                   GeneralPointRelationMapper generalPointRelationMapper, BlockChainUtil blockChainUtil,
                                   PublishPointBC publishPointBC, BlockInfoDao blockInfoDao, CompanyBC companyBC, BlockInfoMapper blockInfoMapper) {
        this.companyPointDao = companyPointDao;
        this.userDao = userDao;
        this.userMapper = userMapper;
        this.cashRecordMapper = cashRecordMapper;
        this.pointInfoMapper = pointInfoMapper;
        this.platformInfoMapper = platformInfoMapper;
        this.pointRecordMapper = pointRecordMapper;
        this.generalPointRecordMapper = generalPointRecordMapper;
        this.pointRelationMapper = pointRelationMapper;
        this.companyInfoDao = companyInfoDao;
        this.companyInfoMapper = companyInfoMapper;
        this.generalPointRelationMapper = generalPointRelationMapper;
        this.blockChainUtil = blockChainUtil;
        this.publishPointBC = publishPointBC;
        this.blockInfoDao = blockInfoDao;
        this.companyBC = companyBC;
        this.blockInfoMapper = blockInfoMapper;
    }


    /**
     * @param uid 集团id
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取通用积分结算信息
     */
    @Override
    public ResponseVO<CompanyPointVO> getBalanceInfoRecord(Long uid) {
        //获取通用积分结算信息
        CompanyPointVO companyPointVO = companyPointDao.getBalanceInfoRecord(uid);
        //返回数据
        log.info("获取通用积分结算信息成功");
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, companyPointVO);
    }

    /**
     * @param companyPointDTO 集团积分分页查询数据传输类
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取集团已兑换通用积分记录
     */
    @Override
    public ResponseVO<PageBean<GeneralPointRecordPO>> listPointExchangeRecord(CompanyPointDTO companyPointDTO) {
        //设置当前页，没有则使用默认值1
        if (companyPointDTO.getCurrentPage() == null) {
            companyPointDTO.setCurrentPage(currentPage);
        }
        //设置每页显示的总条数，没有则使用默认值10
        if (companyPointDTO.getPageSize() == null) {
            companyPointDTO.setPageSize(pageSize);
        }
        //使用PageHelper分页插件进行分页查询，只对下一行的查询结果进行分页
        Page<T> page = PageHelper.startPage(companyPointDTO.getCurrentPage(), companyPointDTO.getPageSize());
        //获取集团已兑换通用积分记录
        List<GeneralPointRecordPO> GeneralPointRecordPOList = companyPointDao.listPointExchangeRecord(companyPointDTO);
        //获取查询结果总条数
        int totalNum = Integer.parseInt(String.valueOf(page.getTotal()));
        //使用自定义分页器进行分页
        PageBean<GeneralPointRecordPO> pageBean = new PageBean<>(GeneralPointRecordPOList, companyPointDTO.getCurrentPage(), companyPointDTO.getPageSize(), totalNum);
        //返回数据
        log.info("获取集团已兑换通用积分记录成功");
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, pageBean);
    }

    /**
     * @param companyPointDTO 集团积分数据传输类
     * @return 请求响应
     * @author qbanxiaoli
     * @description 集团发放积分给用户
     */
    @Override
    public ResponseVO sendPointToUser(CompanyPointDTO companyPointDTO) {

        CompanyInfoPO companyInfoPO = new CompanyInfoPO();
        companyInfoPO.setUid(companyPointDTO.getUid());
        companyInfoPO = companyInfoMapper.selectOne(companyInfoPO);
        //检查商户是否被关闭
        if (companyInfoPO.getCompanyStatus()!=null && companyInfoPO.getCompanyStatus().equals(CompanyStatus.COMPANY_SHOT_DOWN)){
            return new ResponseVO(UserResponseEnum.COMPANY_HAS_BEEN_DELETED);
        }

        //设置查询条件
        AccountPO u = new AccountPO();
        u.setId(companyPointDTO.getToid());
        //根据用户id查询用户
        AccountPO userPO = userMapper.selectByPrimaryKey(u);
        //设置查询条件
        PointInfoPO i = new PointInfoPO();
        i.setCompany(companyInfoPO.getId());
        i.setId(companyPointDTO.getPointId());
        //根据集团id和积分id查询集团是否持有该积分
        PointInfoPO pointInfoPO = pointInfoMapper.selectOne(i);
        //设置查询条件
        PointRelationPO r = new PointRelationPO();
        r.setUserId(userPO.getId());
        r.setPointId(companyPointDTO.getPointId());
        //查询会员用户是否持有该积分
        PointRelationPO pointRelationPO = pointRelationMapper.selectOne(r);
        //判断集团是否有足够的该积分
        if (pointInfoPO.getNumRemain().compareTo(companyPointDTO.getNum()) < 0) {
            log.info("集团积分不足");
            return new ResponseVO(PointResponseEnum.COMPANY_POINT_NOT_ENOUGH);
        }
        //设置集团积分发放后的剩余数量
        pointInfoPO.setNumRemain(pointInfoPO.getNumRemain().subtract(companyPointDTO.getNum()));
        //集团发放积分
        try {
            pointInfoMapper.updateByPrimaryKeySelective(pointInfoPO);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("发放积分失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseVO(PointResponseEnum.SEND_POINT_FAILURE);
        }
        //用户接收积分
        try {
            if (pointRelationPO == null) {
                //装配用户接收积分关联实体类
                pointRelationPO = PointRelationPOAssembly.toDomain(userPO.getId(), companyPointDTO);
                //用户没有该积分，则直接保存积分记录
                pointRelationMapper.insert(pointRelationPO);
            } else {
                //设置会员用户接收该积分后的积分数量
                pointRelationPO.setNum(pointRelationPO.getNum().add(companyPointDTO.getNum()));
                //用户有该积分，则更新积分记录
                pointRelationMapper.updateByPrimaryKeySelective(pointRelationPO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("用户接收积分失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseVO(PointResponseEnum.SEND_POINT_FAILURE);
        }
        //设置查询条件
        AccountPO a = new AccountPO();
        a.setId(companyPointDTO.getUid());
        //查询集团信息
        AccountPO company = userMapper.selectByPrimaryKey(a);
        //发放积分上链
        ResponseBean<String> responseBean = companyBC.giveUserPoints(companyInfoPO.getId().toString(), companyPointDTO.getPointId().toString(), pointInfoPO.getEndAt(),
                companyPointDTO.getNum().toString(), userPO.getId().toString(), company.getPublicKey(), company.getPrivateKey());
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
        //装配集团发放积分记录
        PointRecordPO pointRecordPO = PointRecordPOAssembly.toDomain(userPO.getId(), companyPointDTO);
        //积分减少
        pointRecordPO.setType(PointRecordConstant.POINT_SUB);
        //集团发放积分给用户
        pointRecordPO.setCategory(PointRecordConstant.CATEGORY_GROUP_ISSUED);
        //设置积分兑换比例
        pointRecordPO.setPointRate(companyInfoPO.getPointRate());
        pointRecordPO.setHash(hash);
        pointRecordPO.setHeight(height);
        //保存发放积分记录
        try {
            pointRecordMapper.insert(pointRecordPO);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("保存发放积分记录失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseVO(PointResponseEnum.SAVE_POINTRECORD_ERROR);
        }
        //装配用户接收集团积分记录
        PointRecordPO p = PointRecordPOAssembly.toDomain(pointRecordPO);
        //积分增加
        p.setType(PointRecordConstant.POINT_ADD);
        //用户接收集团积分
        p.setCategory(PointRecordConstant.CATEGORY_USER_INCOME);
        p.setHash(hash);
        p.setHeight(height);
        //保存接收积分记录
        try {
            pointRecordMapper.insert(p);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("保存接收积分记录失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseVO(PointResponseEnum.SAVE_POINTRECORD_ERROR);
        }
        //装配上链实体类
        BlockInfoPO blockInfoPO = new BlockInfoPO(companyPointDTO.getUid(), height, hash, System.currentTimeMillis(), Brokerpoints.ActionType.GiveUserPoints_VALUE);
        //保存上链记录
        try {
            blockInfoMapper.insert(blockInfoPO);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("保存发放积分上链记录失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseVO(PointResponseEnum.SAVE_POINTRECORD_ERROR);
        }
        // map最好规定好大小
        Map<String, Boolean> map = new HashMap<>(16);
        map.put(RESULT, true);
        //返回数据
        log.info("发放积分成功");
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, map);
    }

    /**
     * @param companyPointDTO 集团积分数据传输类
     * @return 请求响应
     * @author qbanxiaoli
     * @description 结算通用积分
     */
    @Override
    public ResponseVO balanceCommonPoint(CompanyPointDTO companyPointDTO) {
        //对交易密码使用md5加密
        String md5Hash;
        try {
            md5Hash = MD5HashUtil.md5SaltEncrypt(companyPointDTO.getPayword(), MD5Salt);
        } catch (Exception e) {
            log.info("交易密码加密失败");
            return new ResponseVO(CommonResponseEnum.PAYWORD_WRONG);
        }
        //根据集团管理用户uid和交易密码哈希判断是否存在
        int isPayWord = companyPointDao.isPayWord(companyPointDTO.getUid(), md5Hash);
        if (isPayWord < 1) {
            log.info("交易密码错误");
            return new ResponseVO(PointResponseEnum.COMPANY_OR_PAYWORD_ERROR);
        }
        //设置查询条件
        GeneralPointRelationPO g = new GeneralPointRelationPO();
        g.setUserId(companyPointDTO.getUid());
        //查询集团是否持有通用积分
        GeneralPointRelationPO generalPointRelationPO = generalPointRelationMapper.selectOne(g);
        if (generalPointRelationPO == null) {
            log.info("集团未持有该积分");
            return new ResponseVO(PointResponseEnum.COMPANY_POINT_NOT_HOLD);
        }
        //判断集团是否有足够的通用积分
        if (generalPointRelationPO.getNum().compareTo(companyPointDTO.getNum()) < 0) {
            log.info("集团通用积分不足");
            return new ResponseVO(PointResponseEnum.COMPANY_POINT_NOT_ENOUGH);
        }
        //设置查询条件
        AccountPO u = new AccountPO();
        u.setId(companyPointDTO.getUid());
        //查询用户信息
        AccountPO userPO = userMapper.selectByPrimaryKey(u);
        //设置查询条件
        PlatformInfoPO p = new PlatformInfoPO();
        p.setId(userPO.getPId());
        //查询用户所在平台信息
        PlatformInfoPO platformInfoPO = platformInfoMapper.selectByPrimaryKey(p);
        //计算集团结算通用积分价值备付金数量
        BigDecimal amount = companyPointDTO.getNum().multiply(new BigDecimal(platformInfoPO.getPointRate()));
        //设置查询条件
        CompanyInfoPO c = new CompanyInfoPO();
        c.setUid(companyPointDTO.getUid());
        //查询集团信息
        CompanyInfoPO companyInfoPO = companyInfoMapper.selectOne(c);
        //设置集团通用积分结算后的剩余数量
        generalPointRelationPO.setNum(generalPointRelationPO.getNum().subtract(companyPointDTO.getNum()));
        //扣除集团通用积分
        try {
            generalPointRelationMapper.updateByPrimaryKeySelective(generalPointRelationPO);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("结算通用积分失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseVO(PointResponseEnum.BALANCE_POINTRE_FAILURE);
        }
        //设置集团结算通用积分后的当前备付金
        companyInfoPO.setAmount(companyInfoPO.getAmount().add(amount));
        //增加集团备付金
        try {
            companyInfoMapper.updateByPrimaryKeySelective(companyInfoPO);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("增加集团备付金失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseVO(PointResponseEnum.BALANCE_POINTRE_FAILURE);
        }
        //结算通用积分上链
        ResponseBean<String> responseBean = companyBC.sellGenerelPoints(companyInfoPO.getId().toString(), companyPointDTO.getNum().toString(), amount.toString(),
                userPO.getPublicKey(), userPO.getPrivateKey());
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
        //装配通用积分记录实体类
        GeneralPointRecordPO generalPointRecordPO = GeneralPointRecordPOAssembly.toDomain(companyPointDTO);
        //设置对方用户id
        generalPointRecordPO.setOppositeUid(platformInfoPO.getId());
        //积分减少
        generalPointRecordPO.setType(GeneralPointRecordConstant.POINT_SUB);
        //集团向平台结算通用积分
        generalPointRecordPO.setCategory(GeneralPointRecordConstant.CATEGORY_COMPANY_SETTLE);
        generalPointRecordPO.setHash(hash);
        generalPointRecordPO.setHeight(height);
        generalPointRecordPO.setPlatformPointRate(platformInfoPO.getPointRate());
        //保存结算通用积分记录
        try {
            generalPointRecordMapper.insert(generalPointRecordPO);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("保存通用积分结算记录失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseVO(PointResponseEnum.SAVE_POINTRECORD_ERROR);
        }
        //装配通用资产记录实体类
        CashRecordPO cashRecordPO = CashRecordPOAssembly.toDomain(amount, userPO);
        //收入资产
        cashRecordPO.setType(CashRecordConstant.INCOME_ASSETS);
        //集团结算通用积分
        cashRecordPO.setCategory(CashRecordConstant.POINT_BALANCE);
        cashRecordPO.setHash(hash);
        cashRecordPO.setHeight(height);
        //保存资产记录
        try {
            cashRecordMapper.insert(cashRecordPO);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("保存资产记录失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseVO(PointResponseEnum.SAVE_POINTRECORD_ERROR);
        }
        //装配上链实体类
        BlockInfoPO blockInfoPO = new BlockInfoPO(companyPointDTO.getUid(), height, hash, System.currentTimeMillis(), Brokerpoints.ActionType.SellGeneralPoints_VALUE);
        //保存上链记录
        try {
            blockInfoMapper.insert(blockInfoPO);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("保存结算通用积分上链记录失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseVO(PointResponseEnum.SAVE_POINTRECORD_ERROR);
        }
        // map最好规定好大小
        Map<String, Boolean> map = new HashMap<>(16);
        map.put(RESULT, true);
        //返回数据
        log.info("结算通用积分成功");
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, map);
    }

    /**
     * 申请发行积分
     *
     * @param uid           uid
     * @param applyPointDTO 数据传输类
     * @return 响应类
     * @// 添加上链操作 TODO: 2018/7/9
     */
    @Override
    public ResponseVO releasePoint(Long uid, ApplyPointDTO applyPointDTO) {
        CompanyInfoPO companyInfoPO = new CompanyInfoPO();
        companyInfoPO.setUid(uid);
        companyInfoPO = companyInfoMapper.selectOne(companyInfoPO);
        if (companyInfoPO.getCompanyStatus()!=null && companyInfoPO.getCompanyStatus().equals(CompanyStatus.COMPANY_SHOT_DOWN)){
            return new ResponseVO(UserResponseEnum.COMPANY_HAS_BEEN_DELETED);
        }
        if (applyPointDTO.getPointName() == null) {
            return new ResponseVO(PointResponseEnum.POINT_NAME_IS_NULL);
        }

        if (applyPointDTO.getNum() == null || applyPointDTO.getNum().intValue() == 0 ){
            return  new  ResponseVO(PointResponseEnum.POINT_NUM_ZERO);
        }

        //设置积分有效期
        if (applyPointDTO.getStartTime() != null || applyPointDTO.getEndTime() != null) {
            applyPointDTO.setIsLife(1);
        } else {
            applyPointDTO.setIsLife(0);
        }
        //判断时间
        if (applyPointDTO.getEndTime() != null) {
            if (applyPointDTO.getEndTime() < System.currentTimeMillis()) {
                return new ResponseVO(PointResponseEnum.POINT_TIME_ERROR);
            }
            if (applyPointDTO.getStartTime() != null && applyPointDTO.getEndTime() < applyPointDTO.getStartTime()) {
                return new ResponseVO(PointResponseEnum.POINT_TIME_ERROR);
            }
        }
        //校验发行积分权限
        CompanyInfoPO companyInfo = companyInfoDao.getCompanyInfo(uid);
        if (companyInfo == null) {
            return new ResponseVO(CommonResponseEnum.FAILURE);
        }
        UserPO userPO = userDao.getUserById(uid);
        //校验交易密码
        String md5Hash;
        try {
            md5Hash = MD5HashUtil.md5SaltEncrypt(applyPointDTO.getPayWord(), MD5Salt);
        } catch (Exception e) {
            log.info("交易密码加密失败");
            return new ResponseVO(CommonResponseEnum.PAYWORD_WRONG);
        }
        int isPayWord = companyPointDao.isPayWord(companyInfo.getUid(), md5Hash);
        if (isPayWord < 1) {
            log.info("集团不存在或交易密码错误");
            return new ResponseVO(PointResponseEnum.COMPANY_OR_PAYWORD_ERROR);
        }
        //获取集团所属平台id
        Long paltformId = companyInfoDao.queryCompanyPlatformId(uid);

        //获取集团流通量
        BigDecimal trunResult = pointInfoMapper.companyPointLiquidity(companyInfo.getId());
        BigDecimal turnVolume = (trunResult == null ? BigDecimal.ZERO : trunResult);
        // 获取集团待审核积分量
        BigDecimal checkResult = pointInfoMapper.companyPointToCheck(companyInfo.getId());
        BigDecimal toCheckPoints = (checkResult == null ? BigDecimal.ZERO : checkResult);
        //计算集团可发行积分总量
        if (companyInfo.getAmount() == null || companyInfo.getCashRate() == null ||
                companyInfo.getPointRate() == null || companyInfo.getCashRate() == 0F) {
            return new ResponseVO(PointResponseEnum.COMPANY_INFO_NOT_ENOUGH);
        }
        BigDecimal middleResult = companyInfo.getAmount().divide(new BigDecimal(companyInfo.getCashRate()), 4, BigDecimal.ROUND_DOWN);
        BigDecimal pointTotal = middleResult.multiply(new BigDecimal(companyInfo.getPointRate()));
        BigDecimal canApplyPoint = pointTotal.subtract(turnVolume).subtract(toCheckPoints);
        if (applyPointDTO.getNum() == null || applyPointDTO.getNum().compareTo(canApplyPoint) == 1) {
            return new ResponseVO(PointResponseEnum.POINT_APPLY_NUM_ERROR);
        }

        // 构建积分信息类
        PointInfoPO pointInfoPO = new PointInfoPO();
        pointInfoPO.setName(applyPointDTO.getPointName());
        pointInfoPO.setCompany(companyInfo.getId());
        pointInfoPO.setIssuePlatform(paltformId);
        pointInfoPO.setMemo(applyPointDTO.getMemo());
        pointInfoPO.setNum(applyPointDTO.getNum());
        pointInfoPO.setNumRemain(applyPointDTO.getNum());
        // 设置已审核
        pointInfoPO.setStatus(PointInfoConstant.CHECK_PASS);
        // 设置积分时间状态
        pointInfoPO.setIsLife(applyPointDTO.getIsLife());
        if (applyPointDTO.getStartTime() != null) {
            pointInfoPO.setStartTime(applyPointDTO.getStartTime().toString());
        }
        if (applyPointDTO.getEndTime() != null) {
            pointInfoPO.setEndTime(applyPointDTO.getEndTime().toString());
        }
        // 保存积分信息
        int savePointInfo = pointInfoMapper.savePointInfo(pointInfoPO);
        if (savePointInfo < 1) {
            log.info("保存积分信息失败");
            return new ResponseVO(PointResponseEnum.SAVE_POINT_ERROR);
        }
        //插入积分交易记录
        PointRecordPO pointRecordPO = new PointRecordPO();
        pointRecordPO.setUid(uid);
        pointRecordPO.setOppositeUid(userPO.getPId());
        pointRecordPO.setType(PointRecordConstant.POINT_ADD);
        pointRecordPO.setNum(pointInfoPO.getNum());
        pointRecordPO.setCategory(PointRecordConstant.CATEGORY_GROUP_APPLY);
        pointRecordPO.setPointId(pointInfoPO.getId());
        pointRecordPO.setPointRate(companyInfo.getPointRate());
        pointRecordPO.setCreatedAt(System.currentTimeMillis());
        pointRecordPO.setUpdatedAt(System.currentTimeMillis());
        int info = pointRecordMapper.insert(pointRecordPO);
        if (info != 1) {
            throw new RuntimeException("插入积分交易记录失败！");
        }
        //添加上链
        UserPrivatePo userPrivatePo = userDao.getUserPrivateInfo(uid);

        Long unixTime = pointInfoPO.getEndTime() == null ? null : Long.valueOf(pointInfoPO.getEndTime()) / 1000;
        try {
            log.info("集团发行积分参数：{}，{}，{}，{},{}",userPrivatePo.getPublicKey(),userPrivatePo.getPrivateKey(),companyInfo.getId(),pointInfoPO.getId(),pointInfoPO.getNum());
            ResponseBean<String> responseBean = publishPointBC.publish(userPrivatePo.getPublicKey(), userPrivatePo.getPrivateKey(), companyInfo.getId(), pointInfoPO.getId(), pointInfoPO.getNum(), unixTime);
            if (responseBean == null || responseBean.getError() != null || responseBean.getResult() == null) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.info("发行积分上链失败！");
                return new ResponseVO(CommonResponseEnum.CHAIN_FAILE);
            }

            ResponseBean<TransactionResultBean> queryTransaction = blockChainUtil.queryTransaction(responseBean.getResult());
            if (queryTransaction == null || StringUtil.isNotBlank(queryTransaction.getError())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return new ResponseVO(CommonResponseEnum.FAILURE, queryTransaction.getError());
            }

            log.info("发行积分上链成功");
//            //更新数据库对应hash 和 height
            PointRecordPO recordPO = new PointRecordPO();
            recordPO.setId(pointRecordPO.getId());
            recordPO.setHeight(queryTransaction.getResult().getHeight());
            recordPO.setHash(responseBean.getResult());
            recordPO.setUpdatedAt(System.currentTimeMillis());
            pointRecordMapper.updateByPrimaryKeySelective(recordPO);
            //插入积分上链记录表
            BlockInfoPO blockInfoPO = new BlockInfoPO();
            blockInfoPO.setUid(uid);
            blockInfoPO.setOperationType(Brokerpoints.ActionType.CreateMerchantPoint_VALUE);
            blockInfoPO.setHash(responseBean.getResult());
            blockInfoPO.setHeight(queryTransaction.getResult().getHeight());
            blockInfoPO.setCreatedAt(System.currentTimeMillis());
            blockInfoDao.insertBlockInfo(blockInfoPO);
        } catch (Exception e) {
            log.info("集团发行积分上链失败: {}", e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseVO(CommonResponseEnum.CHAIN_FAILE);

        }

        log.info("申请发行积分成功");
        Map<String, Long> map = new HashMap<>(16);
        map.put(ID, pointInfoPO.getId());
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, map);
    }

    /**
     * 集团申请积分转换
     * @param companyId 集团id
     * @param nums 申请积分数量
     */
    @Override
    public void tranterReleasePoint(Long companyId, BigDecimal nums) {
        ApplyPointDTO applyPointDTO = new ApplyPointDTO();
        applyPointDTO.setPointName("初始化积分");
        applyPointDTO.setNum(nums);
        applyPointDTO.setMemo("积分转移集团积分初始化");
        applyPointDTO.setIsLife(0);
        Long uid = companyInfoDao.queryUserIdByCompanyId(companyId);
        UserPO userPO = userDao.getUserById(uid);
        CompanyInfoPO companyInfo = companyInfoDao.getCompanyInfoById(companyId);
        if (userPO == null || companyInfo == null) {
            log.info("集团或用户不存在！");
            throw new RuntimeException();
        }
//        //计算集团可发行积分总量
//        if (companyInfo.getAmount() == null || companyInfo.getCashRate() == null ||
//                companyInfo.getPointRate() == null || companyInfo.getCashRate() == 0F) {
//            log.info("集团信息不全");
//            throw new RuntimeException();
//        }
//        //获取集团所属平台id
//        Long paltformId = companyInfoDao.queryCompanyPlatformId(uid);
//        //获取集团流通量
//        BigDecimal trunResult = pointInfoMapper.companyPointLiquidity(companyInfo.getId());
//        BigDecimal turnVolume = (trunResult == null ? BigDecimal.ZERO : trunResult);
//        // 获取集团待审核积分量
//        BigDecimal checkResult = pointInfoMapper.companyPointToCheck(companyInfo.getId());
//        BigDecimal toCheckPoints = (checkResult == null ? BigDecimal.ZERO : checkResult);
//        BigDecimal middleResult = companyInfo.getAmount().divide(new BigDecimal(companyInfo.getCashRate()), 4, BigDecimal.ROUND_DOWN);
//        BigDecimal pointTotal = middleResult.multiply(new BigDecimal(companyInfo.getPointRate()));
//        BigDecimal canApplyPoint = pointTotal.subtract(turnVolume).subtract(toCheckPoints);
//        if (applyPointDTO.getNum() == null || applyPointDTO.getNum().compareTo(canApplyPoint) == 1) {
//            log.info("积分发行量有误");
//            throw new RuntimeException();
//        }
        Long paltformId = 1L;
        // 构建积分信息类
        PointInfoPO pointInfoPO = new PointInfoPO();
        pointInfoPO.setName(applyPointDTO.getPointName());
        pointInfoPO.setCompany(companyInfo.getId());
        pointInfoPO.setIssuePlatform(paltformId);
        pointInfoPO.setMemo(applyPointDTO.getMemo());
        pointInfoPO.setNum(applyPointDTO.getNum());
        pointInfoPO.setNumRemain(applyPointDTO.getNum());
        // 设置已审核
        pointInfoPO.setStatus(PointInfoConstant.CHECK_PASS);
        // 设置积分时间状态(无有效期)
        pointInfoPO.setIsLife(applyPointDTO.getIsLife());
        // 保存积分信息
        int savePointInfo = pointInfoMapper.savePointInfo(pointInfoPO);
        if (savePointInfo < 1) {
            log.info("保存积分信息失败");
            throw new RuntimeException();
        }
        //插入积分交易记录
        PointRecordPO pointRecordPO = new PointRecordPO();
        pointRecordPO.setUid(uid);
        pointRecordPO.setOppositeUid(userPO.getPId());
        pointRecordPO.setType(PointRecordConstant.POINT_ADD);
        pointRecordPO.setNum(pointInfoPO.getNum());
        pointRecordPO.setCategory(PointRecordConstant.CATEGORY_GROUP_APPLY);
        pointRecordPO.setPointId(pointInfoPO.getId());
        pointRecordPO.setPointRate(companyInfo.getPointRate());
        pointRecordPO.setCreatedAt(System.currentTimeMillis());
        pointRecordPO.setUpdatedAt(System.currentTimeMillis());
        int info = pointRecordMapper.insert(pointRecordPO);
        if (info != 1) {
            throw new RuntimeException("插入积分交易记录失败！");
        }
        //添加上链
        UserPrivatePo userPrivatePo = userDao.getUserPrivateInfo(uid);

        Long unixTime = pointInfoPO.getEndTime() == null ? null : Long.valueOf(pointInfoPO.getEndTime()) / 1000;
        try {
            log.info("集团发行积分参数：{}，{}，{}，{},{}",userPrivatePo.getPublicKey(),userPrivatePo.getPrivateKey(),companyInfo.getId(),pointInfoPO.getId(),pointInfoPO.getNum());
            ResponseBean<String> responseBean = publishPointBC.publish(userPrivatePo.getPublicKey(), userPrivatePo.getPrivateKey(), companyInfo.getId(), pointInfoPO.getId(), pointInfoPO.getNum(), unixTime);

            if (responseBean == null || responseBean.getError() != null || responseBean.getResult() == null) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.info("发行积分上链失败！");
                throw new RuntimeException();
            }

            ResponseBean<TransactionResultBean> queryTransaction = blockChainUtil.queryTransaction(responseBean.getResult());
            if (queryTransaction == null || StringUtil.isNotBlank(queryTransaction.getError())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new RuntimeException();
            }

            log.info("发行积分上链成功");
//            //更新数据库对应hash 和 height
            PointRecordPO recordPO = new PointRecordPO();
            recordPO.setId(pointRecordPO.getId());
            recordPO.setHeight(queryTransaction.getResult().getHeight());
            recordPO.setHash(responseBean.getResult());
            recordPO.setUpdatedAt(System.currentTimeMillis());
            pointRecordMapper.updateByPrimaryKeySelective(recordPO);
            //插入积分上链记录表
            BlockInfoPO blockInfoPO = new BlockInfoPO();
            blockInfoPO.setUid(uid);
            blockInfoPO.setOperationType(Brokerpoints.ActionType.CreateMerchantPoint_VALUE);
            blockInfoPO.setHash(responseBean.getResult());
            blockInfoPO.setHeight(queryTransaction.getResult().getHeight());
            blockInfoPO.setCreatedAt(System.currentTimeMillis());
            blockInfoDao.insertBlockInfo(blockInfoPO);
        } catch (Exception e) {
            log.info("集团发行积分上链失败: {}", e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new RuntimeException();

        }
    }

    /**
     * 用户积分兑换记录（商户）
     * @param goodExcelDTO
     * @return
     */
    @Override
    public ResponseVO exchangeGeneralCompany(GoodExcelDTO goodExcelDTO) {

        Long groupId = companyPointDao.findGroupIdByUid(goodExcelDTO);
        if (groupId == null) {
            return new ResponseVO(CommonResponseEnum.COMPANY_NOT_EXIST);
        }
        goodExcelDTO.setGroupId(groupId);
        if (goodExcelDTO.getPage() == null || goodExcelDTO.getPage() < 1) {
            goodExcelDTO.setPage(1);
        }
        if (goodExcelDTO.getPageSize() == null) {
            goodExcelDTO.setPageSize(pageSize);
        }

        Page page = PageHelper.startPage(goodExcelDTO.getPage(), goodExcelDTO.getPageSize());

        List<ExcelPO> excelPOList = companyPointDao.findExchangeGeneralByPlatform(goodExcelDTO);


        excelPOList.stream().forEach(e ->{

            e.setPointRateStr(e.getPointRate() + ":1");
            if(e.getCashNum() != null){
                e.setCashNumStr("-"+e.getCashNum());
            }

        });
        Integer total = Integer.parseInt(String.valueOf(page.getTotal()));
        PageBean pageBean = new PageBean<>(excelPOList, goodExcelDTO.getPage(), goodExcelDTO.getPageSize(), total);
        return new ResponseVO<>(CommonResponseEnum.FIND_BUY_GENERAL_POINTS_SUCCESS, pageBean);

    }


    /**
     * 获取集团集团积分资产信息
     *
     * @param uid 用户id
     * @return 响应类
     */
    @Override
    public ResponseVO companyPointAsset(Long uid) {
        //获取用户管理的集团
        CompanyInfoPO companyInfoPO = companyInfoDao.getCompanyInfo(uid);
        if (companyInfoPO == null) {
            return new ResponseVO(CommonResponseEnum.COMPANY_NOT_EXIST);
        }

        CompanyPointAssetsVO assetsVO = pointInfoMapper.selectCompanyPointAsset(companyInfoPO.getId());
        if (assetsVO.getAmount() == null || assetsVO.getCashRate() == null || assetsVO.getPointRate() == null) {
            return new ResponseVO(PointResponseEnum.POINT_INFO_ERROR);
        }
        if (assetsVO.getSums() == null) {
            assetsVO.setSums(new BigDecimal(0));
        }
        if (assetsVO.getNumRemains() == null) {
            assetsVO.setNumRemains(new BigDecimal(0));
        }
        if (assetsVO.getNumUseds() == null) {
            assetsVO.setNumUseds(new BigDecimal(0));
        }
        //流通量
        BigDecimal circulateAmount = assetsVO.getSums().subtract(assetsVO.getNumUseds());
        /* 可申请总量*/
        BigDecimal cashRate = new BigDecimal(assetsVO.getCashRate());
        BigDecimal applyTotal = assetsVO.getAmount().divide(cashRate, 4, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(assetsVO.getPointRate()));
        assetsVO.setTotalAmount(applyTotal.subtract(circulateAmount));
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, assetsVO);
    }

    /**
     * 集团已发放积分列表
     *
     * @param uid            uid
     * @param pointRecordDTO 数据传输类
     * @return 响应类
     */
    @Override
    public ResponseVO grantPointList(Long uid, PointRecordDTO pointRecordDTO) {
        CompanyInfoPO companyInfo = companyInfoDao.getCompanyInfo(uid);
        if (companyInfo == null) {
            return new ResponseVO(CommonResponseEnum.COMPANY_NOT_EXIST);
        }
        pointRecordDTO.setType(PointRecordConstant.POINT_SUB);
        pointRecordDTO.setCategory(PointRecordConstant.CATEGORY_GROUP_ISSUED);
        Page page = PageHelper.startPage(pointRecordDTO.getPage(), pointRecordDTO.getPageSize());
        List<PointRecordDTO> pointRecordDTOList = pointRecordMapper.queryPointRecordList(pointRecordDTO);
        int total = Integer.valueOf(String.valueOf(page.getTotal()));
        PageBean<PointRecordDTO> pageBean = new PageBean<>(pointRecordDTOList, pointRecordDTO.getPage(), pointRecordDTO.getPageSize(), total);

        return new ResponseVO<>(CommonResponseEnum.QUERY_SUCCESS, pageBean);

    }

    /**
     * 集团查看积分行放记录
     *
     * @param queryPointDTO
     * @return
     * @wangjie
     */
    @Override
    public ResponseVO<PageBean<PointInfoDTO>> pointIssueList(QueryPointDTO queryPointDTO) {
        Page page = PageHelper.startPage(queryPointDTO.getPage(), queryPointDTO.getPageSize());
        List<PointInfoDTO> pointInfoDTOList = pointInfoMapper.pointIssueList(queryPointDTO);
        int total = Integer.valueOf(String.valueOf(page.getTotal()));
        PageBean<PointInfoDTO> pageBean = new PageBean<>(pointInfoDTOList, queryPointDTO.getPage(), queryPointDTO.getPageSize(), total);
        log.info("集团查看积分行放记录");
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, pageBean);
    }

    /**
     * 列出集团活动信息
     *
     * @param uid         集团管理用户
     * @param showOutTime 是否包括已过期活动
     * @return
     */
    @Override
    public ResponseVO listActivity(Long uid, Boolean showOutTime) {
        List<PointActivityVO> pointActivityVOS = companyPointDao.listActivity(uid, showOutTime);
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, pointActivityVOS);
    }

}
