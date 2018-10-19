package com.fuzamei.bonuspoint.service.impl.good;

import brokerpoints.Brokerpoints;
import com.fuzamei.bonuspoint.blockchain.bc.GoodBc;
import com.fuzamei.bonuspoint.blockchain.bean.Commodity;
import com.fuzamei.bonuspoint.blockchain.bean.MerchantPoint;
import com.fuzamei.bonuspoint.blockchain.util.BlockChainUtil;
import com.fuzamei.bonuspoint.constant.*;
import com.fuzamei.bonuspoint.dao.block.BlockInfoDao;
import com.fuzamei.bonuspoint.dao.common.mapper.*;
import com.fuzamei.bonuspoint.dao.good.GoodDao;
import com.fuzamei.bonuspoint.dao.good.GoodOrderDao;
import com.fuzamei.bonuspoint.dao.point.GeneralPointRecordDao;
import com.fuzamei.bonuspoint.dao.point.GeneralPointRelationDao;
import com.fuzamei.bonuspoint.dao.user.CompanyInfoDao;
import com.fuzamei.bonuspoint.dao.user.CompanyMemberDao;
import com.fuzamei.bonuspoint.dao.user.UserAddressDao;
import com.fuzamei.bonuspoint.dao.user.UserDao;
import com.fuzamei.bonuspoint.entity.dto.good.GoodOrderDTO;
import com.fuzamei.bonuspoint.entity.dto.good.GoodPayDTO;
import com.fuzamei.bonuspoint.entity.dto.good.QueryOrderDTO;
import com.fuzamei.bonuspoint.entity.dto.point.CompanyPointDTO;
import com.fuzamei.bonuspoint.entity.po.account.AccountPO;
import com.fuzamei.bonuspoint.entity.po.block.BlockInfoPO;
import com.fuzamei.bonuspoint.entity.po.good.GoodOrderPO;
import com.fuzamei.bonuspoint.entity.po.good.GoodPO;
import com.fuzamei.bonuspoint.entity.po.good.GoodRebatePO;
import com.fuzamei.bonuspoint.entity.po.point.*;
import com.fuzamei.bonuspoint.entity.po.user.CompanyInfoPO;
import com.fuzamei.bonuspoint.entity.po.user.InvitePO;
import com.fuzamei.bonuspoint.entity.po.user.UserPO;
import com.fuzamei.bonuspoint.entity.po.user.UserPrivatePo;
import com.fuzamei.bonuspoint.entity.vo.good.GoodOrderVO;
import com.fuzamei.bonuspoint.entity.vo.point.PointOrderVO;
import com.fuzamei.bonuspoint.entity.vo.user.AddressDetailVo;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.enums.GoodResponseEnum;
import com.fuzamei.bonuspoint.service.account.AccountService;
import com.fuzamei.bonuspoint.service.good.GoodOrderService;
import com.fuzamei.bonuspoint.service.point.CompanyPointService;
import com.fuzamei.bonuspoint.util.SendSmsUtils;
import com.fuzamei.bonuspoint.util.StringUtil;
import com.fuzamei.bonuspoint.util.SubmailSMSUtil;
import com.fuzamei.common.model.vo.ResponseVO;
import com.fzm.blockchain.entity.ResponseBean;
import com.fzm.blockchain.entity.TransactionResultBean;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class GoodOrderServiceImpl implements GoodOrderService {


    private static final String ID = "id";

    private final GoodDao goodDao;

    private final GoodOrderDao goodOrderDao;

    private final UserAddressDao userAddressDao;

    private final CompanyInfoDao companyInfoDao;

    private final UserDao userDao;

    private final PointRelationMapper pointRelationDao;

    private final PointRecordMapper pointRecordMapper;

    private final GeneralPointRelationDao generalPointRelationDao;

    private final PointInfoMapper pointInfoMapper;

    private final GeneralPointRelationMapper generalPointRelationMapper;

    private final GeneralPointRecordMapper generalPointRecordMapper;

    private final CompanyMemberDao companyMemberDao;


    private final BlockInfoDao blockInfoDao;

    private final AccountService accountService;

    private final GeneralPointRecordDao generalPointRecordDao;

    private final GoodBc goodBc;

    private final BlockChainUtil blockChainUtil;

    private final GoodRebateMapper goodRebateMapper;

    private final CompanyPointService companyPointService;

    private final InviteMapper inviteMapper;

    @Autowired
    public GoodOrderServiceImpl(GoodDao goodDao, GoodOrderDao goodOrderDao, UserAddressDao userAddressDao, CompanyInfoDao companyInfoDao, UserDao userDao, PointRelationMapper pointRelationDao, PointRecordMapper pointRecordMapper, GeneralPointRelationDao generalPointRelationDao, PointInfoMapper pointInfoMapper, GeneralPointRelationMapper generalPointRelationMapper, GeneralPointRecordMapper generalPointRecordMapper, CompanyMemberDao companyMemberDao, BlockInfoDao blockInfoDao, AccountService accountService, GeneralPointRecordDao generalPointRecordDao, GoodBc goodBc, BlockChainUtil blockChainUtil, GoodRebateMapper goodRebateMapper, CompanyPointService companyPointService, InviteMapper inviteMapper) {
        this.goodDao = goodDao;
        this.goodOrderDao = goodOrderDao;
        this.userAddressDao = userAddressDao;
        this.companyInfoDao = companyInfoDao;
        this.userDao = userDao;
        this.pointRelationDao = pointRelationDao;
        this.pointRecordMapper = pointRecordMapper;
        this.generalPointRelationDao = generalPointRelationDao;
        this.pointInfoMapper = pointInfoMapper;
        this.generalPointRelationMapper = generalPointRelationMapper;
        this.generalPointRecordMapper = generalPointRecordMapper;
        this.companyMemberDao = companyMemberDao;
        this.blockInfoDao = blockInfoDao;
        this.accountService = accountService;
        this.generalPointRecordDao = generalPointRecordDao;
        this.goodBc = goodBc;
        this.blockChainUtil = blockChainUtil;
        this.goodRebateMapper = goodRebateMapper;
        this.companyPointService = companyPointService;
        this.inviteMapper = inviteMapper;
    }

    /**
     * 用户订购商品 （待付款）
     *
     * @param goodOrderDTO 商品信息
     * @param uid          用户id
     * @return
     */
    @Override
    public ResponseVO orderGood(GoodOrderDTO goodOrderDTO, Long uid) {
        GoodOrderPO goodOrderPO = new GoodOrderPO();
        if (uid == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_UID);
        }
        goodOrderPO.setUid(uid);
        if (goodOrderDTO.getGoodId() == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_GID);
        }
        GoodPO goodPO = goodDao.getGood(goodOrderDTO.getGoodId());
        if (goodPO == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_GID_NOEXIT);
        }
        if(goodPO.getStatus() != GoodStatusConstant.SALE) {
            return new ResponseVO(GoodResponseEnum.GOOD_NOT_MATCH);
        }

        if (goodPO.getIsLife() && goodPO.getStartAt() > System.currentTimeMillis()){
            return  new ResponseVO(GoodResponseEnum.GOOD_PAY_TIME_NOT_COMING);
        }

        goodOrderPO.setGid(goodPO.getGid());
        goodOrderPO.setGoodId(goodPO.getId());
        goodOrderPO.setGoodPrice(goodPO.getPrice());
        goodOrderPO.setGoodGlobalPrice(goodPO.getGlobalPrice());
        if (goodOrderDTO.getNum() == null || goodOrderDTO.getNum() <= 0) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_NUM);
        }
        goodOrderPO.setNum(goodOrderDTO.getNum());

        if (goodOrderDTO.getAddressId() == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_ADDRESS);
        }


//        if (!userAddressDao.isExit(goodOrderDTO.getAddressId(), uid)) {
//            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_ADDRESS_NOEXIT);
//        }
//        //选择用户默认收货地址
//        Long defaultAdressId = userAddressDao.getDafultAddressId(uid);
//        if (defaultAdressId == null){
//            return  new ResponseVO(GoodResponseEnum.USER_DEFAULT_ADDRESS_NOT_EXIT);
//        }
//        goodOrderPO.setAddressId(defaultAdressId);
        AddressDetailVo addressDetail = userAddressDao.getAdressDetail(uid,goodOrderDTO.getAddressId());
        if (addressDetail == null){
            return  new ResponseVO(GoodResponseEnum.USER_DEFAULT_ADDRESS_NOT_EXIT);
        }
        goodOrderPO.setAddressId(goodOrderDTO.getAddressId());
        goodOrderPO.setAddressName(addressDetail.getAddressName());
        goodOrderPO.setAddressMobile(addressDetail.getAddressMobile());
        goodOrderPO.setAddressDistrict(addressDetail.getAddressDistrict());
        goodOrderPO.setAddressDetail(addressDetail.getAddressDetail());
        goodOrderPO.setDistribution(goodOrderDTO.getDistribution());
        goodOrderPO.setMessage(goodOrderDTO.getMessage());
        goodOrderPO.setStatus(GoodOrderConstant.GOOD_TO_SETTLED);

        if ((goodPO.getNumUsed() + goodOrderPO.getNum()) > goodPO.getNum()) { //订购数量大于库存量
            return new ResponseVO<>(GoodResponseEnum.GOOD_STOCK_NOT_ENOUGH);
        }
        if (goodDao.orderGood(goodOrderPO.getGoodId(), goodOrderPO.getNum()) != 1) {
            throw new RuntimeException("更新商品销量出错");
        }
        if (goodOrderDao.addGoodOrder(goodOrderPO) != 1) {
            throw new RuntimeException("写入订单信息出错");
        }

        Map<String, Long> map = new HashMap<>(16);
        map.put(ID, goodOrderPO.getId());
        log.info("购买商品成功！");
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, map);
    }

    /**
     * 用户取消订单
     *
     * @param id  订单id
     * @param uid 用户id
     * @return
     */
    @Override
    public ResponseVO cancelOrder(Long id, Long uid) {
        if (id == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_ID);
        }
        if (uid == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_UID);
        }
        GoodOrderPO goodOrderPO = goodOrderDao.getGoodOrder(id);
        if (goodOrderPO == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_NOEXIT);
        }
        if (!goodOrderPO.getUid().equals(uid)) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_UID_DIFF);
        }
        if (goodOrderPO.getStatus() != GoodOrderConstant.GOOD_TO_SETTLED) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_CANNOT_BACK);
        }
        GoodOrderPO goodOrderNew = new GoodOrderPO();
        goodOrderNew.setId(id);
        goodOrderNew.setStatus(GoodOrderConstant.CLOSE_GOOD_ORDER);
        goodOrderDao.updateGoodOrder(goodOrderNew);
        goodDao.cancelOrderGood(goodOrderPO.getGoodId(), goodOrderPO.getNum());
        return new ResponseVO(CommonResponseEnum.SUCCESS);
    }


    /**
     * 商户卖家发货
     *
     * @param id            流水单号
     * @param uid           商户uid
     * @param logisticsInfo 物流订单号
     * @return
     */
    @Override
    public ResponseVO sendGood(Long id, Long uid, String logisticsInfo) {


        GoodOrderPO goodOrderPO = goodOrderDao.getGoodOrder(id);
        if (goodOrderPO == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_NOEXIT);
        }
        CompanyInfoPO companyInfoPO = companyInfoDao.getCompanyInfo(uid);
        if (!companyInfoPO.getId().equals(goodOrderPO.getGid())) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_NOT_MATCH);
        }
        if (goodOrderPO.getStatus() != GoodOrderConstant.GOOD_TO_DELIVERED) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_NOT_MATCH);
        }
        if (StringUtil.isBlank(logisticsInfo)) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_LOGISTICSINFO);
        }
        GoodOrderPO goodOrderNew = new GoodOrderPO();
        goodOrderNew.setId(id);
        goodOrderNew.setLogisticsInfo(logisticsInfo);
        goodOrderNew.setStatus(GoodOrderConstant.GOOD_IN_TRANST);
        goodOrderDao.updateGoodOrder(goodOrderNew);
        log.info("商户发货成功！");
        return new ResponseVO(CommonResponseEnum.SUCCESS);
    }

    @Override
    public ResponseVO updateGoodLogisticsInfo(Long id, Long uid, String logisticsInfo) {
        GoodOrderPO goodOrderPO = goodOrderDao.getGoodOrder(id);
        if (goodOrderPO == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_NOEXIT);
        }
        CompanyInfoPO companyInfoPO = companyInfoDao.getCompanyInfo(uid);
        if (!companyInfoPO.getId().equals(goodOrderPO.getGid())) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_NOT_MATCH);
        }
        if (goodOrderPO.getStatus() != GoodOrderConstant.GOOD_IN_TRANST) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_NOT_MATCH);
        }
        if (StringUtil.isBlank(logisticsInfo)) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_LOGISTICSINFO);
        }
        GoodOrderPO goodOrderNew = new GoodOrderPO();
        goodOrderNew.setId(id);
        goodOrderNew.setLogisticsInfo(logisticsInfo);
        goodOrderDao.updateGoodOrder(goodOrderNew);
        log.info("商户修改发货物流单号成功！");
        return new ResponseVO(CommonResponseEnum.SUCCESS);
    }

    /**
     * 用户确认收货
     *
     * @param uid 用户id
     * @param id  流水单号
     * @return
     */
    @Override
    public ResponseVO confirmGood(Long id, Long uid) {
        if (id == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_ID);
        }
        if (uid == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_UID);
        }
        GoodOrderPO goodOrderPO = goodOrderDao.getGoodOrder(id);
        if (goodOrderPO == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_NOEXIT);
        }
        if (!goodOrderPO.getUid().equals(uid)) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_UID_DIFF);
        }

        UserPrivatePo userPrivatePo = userDao.getUserPrivateInfo(uid);
        Long companyUid = companyInfoDao.queryUserIdByCompanyId(goodOrderPO.getGid());
        UserPrivatePo companyPrivatePo = userDao.getUserPrivateInfo(companyUid);
        Integer pointType = null;
        //   if (goodOrderPO.getPayMode().intValue() == GoodOrderConstant.PAY_MODEL_PRICE) {
        //      pointType = Api.PointType.POINT_GROUP_VALUE;
        //  } else if (goodOrderPO.getPayMode().intValue() == GoodOrderConstant.PAY_MODEL_GLOBAL_PRICE) {
        //      pointType = Api.PointType.POINT_GENERAL_VALUE;
        //  } else {
        //     return new ResponseVO(CommonResponseEnum.FAILURE);
        // }
        // String result = goodProtobuf.buyGood(userPrivatePo.getId(), userPrivatePo.getPublicKey(),
        //         userPrivatePo.getPrivateKey(), companyPrivatePo.getPublicKey(),
        //         String.valueOf(goodOrderPO.getPayTotal()), Api.PointType.POINT_GENERAL, goodOrderPO.getGoodId(),
        //         goodOrderPO.getNum());
        //保留上链记录
        try {
            //上链失败
            //      if (!ResultUtil.checkResult(result)) {
            //       log.info(ResultUtil.resultMessage(result));
            //        return new ResponseVO(CommonResponseEnum.FAILURE);
            //    }
            //   BlockInfoPO blockInfoPO = new BlockInfoPO();
            //   blockInfoPO.setOperationType(Api.MessageType.MsgBuyCommodity_VALUE);
            //   blockInfoPO.setHeight(ResultUtil.resultHeight(result).longValue());
            //   blockInfoPO.setHash(ResultUtil.resultHash(result));
            //   blockInfoPO.setCreatedAt(System.currentTimeMillis());
            //   if (blockInfoDao.insertSelective(blockInfoPO) != 1) {
            //      throw new Exception("插入上链记录失败！");
            //  }
            GoodOrderPO goodOrderNew = new GoodOrderPO();
            goodOrderNew.setId(id);
            goodOrderNew.setStatus(GoodOrderConstant.GOOD_SUCCESSED_TRADE);
            goodOrderNew.setBargainAt(System.currentTimeMillis());
            //  goodOrderNew.setHeight(blockInfoPO.getHeight());
            //  goodOrderNew.setHash(blockInfoPO.getHash());
            if (goodOrderDao.updateGoodOrder(goodOrderNew) != 1) {
                throw new Exception("更新订单信息失败！");
            }
            log.info("用户收货成功！");
        } catch (Exception e) {
            throw new RuntimeException("上链失败！", e);
        }
        return new ResponseVO(CommonResponseEnum.SUCCESS);
    }

    /**
     * 用户提出退货
     *
     * @param id       流水单号
     * @param uid      用户id
     * @param backMemo 退货理由
     * @return
     */
    @Override
    public ResponseVO applyBackGood(Long id, Long uid, String backMemo) {
        if (id == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_ID);
        }
        if (uid == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_UID);
        }
        GoodOrderPO goodOrderPO = goodOrderDao.getGoodOrder(id);
        if (goodOrderPO == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_NOEXIT);
        }
        if (!goodOrderPO.getUid().equals(uid)) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_UID_DIFF);
        }
        if (goodOrderPO.getStatus() != GoodOrderConstant.GOOD_SUCCESSED_TRADE) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_NOT_MATCH);
        }
        GoodOrderPO goodOrderNew = new GoodOrderPO();
        goodOrderNew.setId(id);
        goodOrderNew.setStatus(GoodOrderConstant.BACK_GOOD_CONFIRM);
        goodOrderNew.setBackMemo(backMemo);
        goodOrderDao.updateGoodOrder(goodOrderNew);
        log.info("用户提出退货成功！");
        return new ResponseVO(CommonResponseEnum.SUCCESS);

    }

    /**
     * 商户确认退货
     *
     * @param id
     * @param uid 用户id
     * @return
     */
    @Override
    public ResponseVO confirmBackGood(Long id, Long uid) {
        if (id == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_ID);
        }
        if (uid == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_UID);
        }
        GoodOrderPO goodOrderPO = goodOrderDao.getGoodOrder(id);
        if (goodOrderPO == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_NOEXIT);
        }
        //带修正
        CompanyInfoPO companyInfoPO = companyInfoDao.getCompanyInfo(uid);
        if (!companyInfoPO.getId().equals(goodOrderPO.getGid())) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_NOT_MATCH);
        }
        if (goodOrderPO.getStatus() != GoodOrderConstant.BACK_GOOD_CONFIRM) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_NOT_MATCH);
        }
        GoodOrderPO goodOrderNew = new GoodOrderPO();
        goodOrderNew.setId(id);
        goodOrderNew.setStatus(GoodOrderConstant.BACK_GOOD_SURE);
        goodOrderDao.updateGoodOrder(goodOrderNew);
        log.info("商户确定退货！");
        return new ResponseVO(CommonResponseEnum.SUCCESS);
    }

    /**
     * 用户退货，添加物流单号
     *
     * @param id                流水单号
     * @param uid               用户id
     * @param backLogisticsInfo 退货物流单号
     * @return
     */
    @Override
    public ResponseVO backGood(Long id, Long uid, String backLogisticsInfo) {
        if (id == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_ID);
        }
        if (uid == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_UID);
        }
        GoodOrderPO goodOrderPO = goodOrderDao.getGoodOrder(id);
        if (goodOrderPO == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_NOEXIT);
        }
        if (!goodOrderPO.getUid().equals(uid)) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_UID_DIFF);
        }
        if (goodOrderPO.getStatus() != GoodOrderConstant.BACK_GOOD_SURE) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_NOT_MATCH);
        }
        if (StringUtil.isBlank(backLogisticsInfo)) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_BACK_LOGISTICSINFO);
        }
        GoodOrderPO goodOrderNew = new GoodOrderPO();
        goodOrderNew.setId(id);
        goodOrderNew.setStatus(GoodOrderConstant.BACK_GOOD_SEND);
        goodOrderNew.setBackLogisticsInfo(backLogisticsInfo);
        goodOrderDao.updateGoodOrder(goodOrderNew);
        log.info("退货添加物流单号！");
        return new ResponseVO(CommonResponseEnum.SUCCESS);
    }

    /**
     * 商家确认成功退 货
     *
     * @param id  物流单号
     * @param uid 用户id
     * @return
     */
    @Override
    public ResponseVO backGoodSuccess(Long id, Long uid) {
        if (id == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_ID);
        }
        if (uid == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_UID);
        }
        GoodOrderPO goodOrderPO = goodOrderDao.getGoodOrder(id);
        if (goodOrderPO == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_NOEXIT);
        }
        CompanyInfoPO companyInfoPO = companyInfoDao.getCompanyInfo(uid); //带修正
        if (!companyInfoPO.getId().equals(goodOrderPO.getGid())) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_NOT_MATCH);
        }
        if (goodOrderPO.getStatus() != GoodOrderConstant.BACK_GOOD_SEND) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_NOT_MATCH);
        }
        GoodOrderPO goodOrderNew = new GoodOrderPO();
        goodOrderNew.setId(id);
        goodOrderNew.setStatus(GoodOrderConstant.BACK_GOOD_SUCCESS);
        goodOrderDao.updateGoodOrder(goodOrderNew);
        log.info("商户确认退货成功！");
        return new ResponseVO(CommonResponseEnum.SUCCESS);
    }

    /**
     * 用户查询接口接口
     *
     * @param queryOrderDTO 查询条件
     * @param uid           用户标识
     * @return
     */
    @Override
    public ResponseVO queryGoodOrder(QueryOrderDTO queryOrderDTO, Long uid) {
        if (uid == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_ID);
        }

        UserPO userPO = userDao.getUserById(uid);
        if (userPO == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_USER_NOT_EXIT);
        }
        queryOrderDTO.setUid(uid);
        if (queryOrderDTO.getCurrentPage() == null || queryOrderDTO.getPageSize() == null) {
            List<GoodOrderVO> goodOrderVOs = goodOrderDao.queryGoodOrder(queryOrderDTO);
            log.info("查询订单成功！");
            return new ResponseVO<>(CommonResponseEnum.SUCCESS, goodOrderVOs);
        } else {
            PageHelper.startPage(queryOrderDTO.getCurrentPage(), queryOrderDTO.getPageSize());
            List<GoodOrderVO> goodOrderVOs = goodOrderDao.queryGoodOrder(queryOrderDTO);
            PageInfo<GoodOrderVO> pageInfo = new PageInfo<>(goodOrderVOs);
            log.info("查询订单成功！");
            return new ResponseVO<>(CommonResponseEnum.SUCCESS, pageInfo);
        }
    }

    /**
     * 获取对应订单信息
     *
     * @param id  订单信息
     * @param uid 用户id
     * @return
     */
    @Override
    public ResponseVO getGoodOrder(Long id, Long uid) {
        if (id == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_ID);
        }
        if (uid == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_UID);
        }
        GoodOrderVO goodOrderVO = goodOrderDao.getUserOrderInfo(id, uid);
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, goodOrderVO);
    }

    /**
     * 用户支付商品
     *
     * @param id       商品id
     * @param uid      用户id
     * @Param goodPayDTO 用户付款信息
     * @return
     */
    @Override
    public ResponseVO payGoodOrder(Long id, Long uid, GoodPayDTO goodPayDTO) {
        Integer payModel = goodPayDTO.getPayModel();
        if (payModel == null || (payModel != GoodOrderConstant.PAY_MODEL_GLOBAL_PRICE
                && payModel != GoodOrderConstant.PAY_MODEL_PRICE)) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_NOT_MATCH);
        }
        //获取订单信息
        GoodOrderPO goodOrderPO = goodOrderDao.getGoodOrder(id);
        if (goodOrderPO == null || (!uid.equals(goodOrderPO.getUid()))) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_NOT_MATCH);
        }
        if (goodOrderPO.getStatus() != GoodOrderConstant.GOOD_TO_SETTLED) {
            return new ResponseVO(GoodResponseEnum.GOOD_ORDER_NOT_MATCH);
        }

        boolean payRight = accountService.checkPayword(goodPayDTO.getPayword(), uid);
        if (!payRight) {
            return new ResponseVO(GoodResponseEnum.GOOD_PAYWORD_ERROR);
        }

        GoodRebatePO goodRebatePO = new GoodRebatePO() ;
        goodRebatePO.setGoodId(id);
        goodRebatePO = goodRebateMapper.selectOne(goodRebatePO);
        if (goodOrderPO!=null){
            BigDecimal rebate = goodOrderPO.getGoodPrice().multiply(new BigDecimal(goodRebatePO.getRate()));
            Example example = new Example(PointInfoPO.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("company",goodOrderPO.getGid());
            criteria.andEqualTo("status",PointInfoConstant.CHECK_PASS);
            criteria.andEqualTo("isLife",PointInfoConstant.LIFE_NOT_HAVING);
            criteria.andGreaterThanOrEqualTo("numRemain",rebate);
            List<PointInfoPO> pointInfoPOList = pointInfoMapper.selectByExample(example);
            if (pointInfoPOList== null || pointInfoPOList.isEmpty()){
                Long companyAdminId = companyInfoDao.queryUserIdByCompanyId(goodOrderPO.getGid());
                AccountPO accountPO = accountService.getUserById(companyAdminId);
                SubmailSMSUtil.sendSMS(accountPO.getMobile(),GoodRebateConstant.NO_REBATE_POINT_PROMPT_MESSAGE);
            }else {
                InvitePO invitePO = new InvitePO();
                invitePO.setUid(uid);
                invitePO = inviteMapper.selectOne(invitePO);
                CompanyPointDTO companyPointDTO = new CompanyPointDTO();
                companyPointDTO.setToid(invitePO.getPId());
                companyPointDTO.setPointId(pointInfoPOList.get(0).getId());
                companyPointDTO.setNum(rebate);
                companyPointDTO.setMemo(GoodRebateConstant.REBEAT_MEMO);
                companyPointService.sendPointToUser(companyPointDTO);
            }
        }

        //会员积分支付
        if (GoodOrderConstant.PAY_MODEL_PRICE == payModel) {
            //集团会员积分支付
            return this.payPoint(goodOrderPO);
        } else { //通用积分支付
            return this.payGeneralPoint(goodOrderPO);
        }
    }

    /**
     * 集团积分购买商品
     *
     * @param goodOrderPO 订单信息
     * @return
     */
    private ResponseVO payPoint(GoodOrderPO goodOrderPO) {
        // 获取集团积分总数
        BigDecimal returnPoint = pointRelationDao.queryPointTotal(goodOrderPO.getUid(), goodOrderPO.getGid());
        if (returnPoint == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_PAY_NOT_POINT);
        }
        BigDecimal totalPoint = returnPoint;
        //商品单价乘以数量就是付款总额
        BigDecimal goodPoint = goodOrderPO.getGoodPrice().multiply(new BigDecimal(goodOrderPO.getNum()));
        if (goodPoint.compareTo(totalPoint) == 1) {
            return new ResponseVO(GoodResponseEnum.GOOD_PAY_POINT_OUTOF);
        }
        // 获取用户持有积分信息
        List<PointOrderVO> pointOrderVOs = pointRelationDao.listPointOrders(goodOrderPO.getUid(), goodOrderPO.getGid());
        if (pointOrderVOs == null || pointOrderVOs.isEmpty()) {
            return new ResponseVO(CommonResponseEnum.FAILURE);
        }

        BigDecimal pointSums = goodPoint;
        BigDecimal onePay = new BigDecimal(0);

        List<PointRecordPO> todoRecords = new ArrayList<>(pointOrderVOs.size());
        List<MerchantPoint> merchantPoints = new ArrayList<>(pointOrderVOs.size());
        for (PointOrderVO pointOrderVO : pointOrderVOs) {
            BigDecimal oneSum = pointOrderVO.getNum();
            if (pointSums.compareTo(oneSum) == 1) {
                onePay = oneSum;
            } else {
                onePay = pointSums;
            }
            //更新bp_point_relation,用户消费积分减少
            pointRelationDao.spendPoint(pointOrderVO.getId(), onePay);
            //添加 bp_point_record
            PointRecordPO pointRecordPO = new PointRecordPO();
            pointRecordPO.setUid(pointOrderVO.getUserId());
            pointRecordPO.setOppositeUid(pointOrderVO.getCompanyUid());
            pointRecordPO.setType(PointRecordConstant.POINT_SUB);
            pointRecordPO.setCategory(PointRecordConstant.CATEGORY_USER_BUY);
            pointRecordPO.setPointId(pointOrderVO.getPointId());
            pointRecordPO.setPointRate(pointOrderVO.getPointRate());
            pointRecordPO.setNum(onePay);
            pointRecordPO.setOrderId(goodOrderPO.getId());
            pointRecordPO.setMemo(goodOrderPO.getMessage());
            pointRecordPO.setCreatedAt(System.currentTimeMillis());
            pointRecordPO.setUpdatedAt(System.currentTimeMillis());
            pointRecordMapper.insert(pointRecordPO);
            //更新bp_point_info,添加用户使用量
            pointInfoMapper.addNumUsed(pointOrderVO.getPointId(), onePay);

            //保存上链信息
            todoRecords.add(pointRecordPO);
            MerchantPoint merchantPoint = new MerchantPoint();
            merchantPoint.setId(pointOrderVO.getPointId());
            merchantPoint.setQuantity(onePay);
            merchantPoint.setExpiration(pointOrderVO.getEndAt());
            merchantPoints.add(merchantPoint);

            pointSums = pointSums.subtract(onePay);
            if (pointSums.compareTo(BigDecimal.ZERO) == 0) {
                break;
            }

        }
        //交易记录上链
        UserPrivatePo userPrivate = userDao.getUserPrivateInfo(goodOrderPO.getUid());
        ResponseBean<String> responseBean = null;
        ResponseBean<TransactionResultBean> queryTransaction = null;
        try {
            Commodity commodity = new Commodity();
            commodity.setMerchantId(goodOrderPO.getGid());
            commodity.setPointType(Brokerpoints.PointType.MPOINT_VALUE);
            commodity.setMerchantPoints(merchantPoints);
            responseBean = goodBc.buyCommodity(goodOrderPO.getUid(),userPrivate.getPublicKey(), userPrivate.getPrivateKey(), commodity);
            if (responseBean == null || responseBean.getError() != null || responseBean.getResult() == null) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.info("集团积分购买商品上链失败！");
                return new ResponseVO(CommonResponseEnum.CHAIN_FAILE);
            }
            queryTransaction = blockChainUtil.queryTransaction(responseBean.getResult());
            if (queryTransaction == null || StringUtil.isNotBlank(queryTransaction.getError())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return new ResponseVO(CommonResponseEnum.FAILURE, queryTransaction.getError());
            }
            log.info("集团积分购买商品上链成功！");
        } catch (Exception e) {
            log.info("集团积分购买商品上链失败: {}", e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseVO(CommonResponseEnum.CHAIN_FAILE);
        }

        //向数据库插入上链hash和height
        for (PointRecordPO pointRecordPO : todoRecords) {
            PointRecordPO recordPO = new PointRecordPO();
            recordPO.setId(pointRecordPO.getId());
            recordPO.setHeight(queryTransaction.getResult().getHeight());
            recordPO.setHash(responseBean.getResult());
            recordPO.setUpdatedAt(System.currentTimeMillis());
            pointRecordMapper.updateByPrimaryKeySelective(recordPO);
        }

        //插入积分上链记录表
        BlockInfoPO blockInfoPO = new BlockInfoPO();
        blockInfoPO.setUid(goodOrderPO.getUid());
        blockInfoPO.setOperationType(Brokerpoints.ActionType.BuyCommodity_VALUE);
        blockInfoPO.setHash(responseBean.getResult());
        blockInfoPO.setHeight(queryTransaction.getResult().getHeight());
        blockInfoPO.setCreatedAt(System.currentTimeMillis());
        blockInfoDao.insertBlockInfo(blockInfoPO);

        //更新订单为完成
        GoodOrderPO goodOrderPONew = new GoodOrderPO();
        goodOrderPONew.setId(goodOrderPO.getId());
        goodOrderPONew.setPayMode(GoodOrderConstant.PAY_MODEL_PRICE);
        goodOrderPONew.setPayTotal(goodPoint);
        goodOrderPONew.setStatus(GoodOrderConstant.GOOD_TO_DELIVERED);
        goodOrderPONew.setPayedAt(System.currentTimeMillis());
        goodOrderPONew.setHeight(queryTransaction.getResult().getHeight());
        goodOrderPONew.setHash(responseBean.getResult());
        goodOrderDao.updateGoodOrder(goodOrderPONew);
        //添加用户为会员
        boolean isExit = companyMemberDao.isMember(goodOrderPO.getUid(), goodOrderPO.getGid());
        if (!isExit) {
            companyMemberDao.saveMember(goodOrderPO.getUid(), goodOrderPO.getGid(), System.currentTimeMillis());
        }
        return new ResponseVO(CommonResponseEnum.SUCCESS);
    }

    /**
     * 通用积分购买商品
     *
     * @param goodOrderPO 订单信息
     */
    private ResponseVO payGeneralPoint(GoodOrderPO goodOrderPO) {
        //获取用户和私密信息
        Long uid = goodOrderPO.getUid();
        UserPrivatePo userPrivatePo = userDao.getUserPrivateInfo(uid);
        //获取集团对应的管理用户id和私密信息
        Long companyUid = companyInfoDao.queryUserIdByCompanyId(goodOrderPO.getGid());
        //判断用户是否持有通用积分，不存添加
        boolean isGeneralRelationExist = generalPointRelationDao.isGeneralPointRelationExist(uid, 1L);
        if (!isGeneralRelationExist) {
            generalPointRelationDao.createGeneralPointRelation(uid, 1L);
        }
        //查询用户持有通用积分
        GeneralPointRelationPO g = new GeneralPointRelationPO();
        g.setUserId(uid);
        GeneralPointRelationPO generalPointRelationPO = generalPointRelationMapper.selectOne(g);
        //获取通用积分信息
        GeneralPointInfoPO generalPointInfoPO = generalPointRecordDao.getGeneralPointInfoByPlatformId(generalPointRelationPO.getPlatformId());
        //商品值多少通用积分
        BigDecimal goodGeneralpointTotal = goodOrderPO.getGoodGlobalPrice().multiply(new BigDecimal(goodOrderPO.getNum()));
        if (generalPointRelationPO == null || goodGeneralpointTotal.compareTo(generalPointRelationPO.getNum()) == 1) {
            return new ResponseVO(GoodResponseEnum.GOOD_PAY_GENERAL_POINT_OUTOF);
        }

        //减少用户通用积分
        generalPointRecordDao.reduceGeneralPoint(uid, goodGeneralpointTotal);
        //增加商户通用积分
        generalPointRecordDao.increaseGeneralPoint(companyUid, goodGeneralpointTotal);
        //添加对应交易记录
        GeneralPointRecordPO generalPointRecordPO = new GeneralPointRecordPO();
        generalPointRecordPO.setUid(uid);
        generalPointRecordPO.setOppositeUid(companyUid);
        generalPointRecordPO.setPointId(generalPointInfoPO.getId());
        generalPointRecordPO.setType(GeneralPointRecordConstant.POINT_SUB);
        generalPointRecordPO.setCategory(GeneralPointRecordConstant.CATEGORY_USER_SPEND);
        generalPointRecordPO.setNum(goodGeneralpointTotal);
        generalPointRecordPO.setOrderId(goodOrderPO.getId());
        generalPointRecordPO.setMemo(goodOrderPO.getMessage());
        generalPointRecordPO.setCreatedAt(System.currentTimeMillis());
        generalPointRecordPO.setUpdatedAt(System.currentTimeMillis());
        generalPointRecordMapper.insert(generalPointRecordPO);
        GeneralPointRecordPO generalPointRecordPO2 = new GeneralPointRecordPO();
        generalPointRecordPO2.setUid(companyUid);
        generalPointRecordPO2.setOppositeUid(uid);
        generalPointRecordPO2.setPointId(generalPointInfoPO.getId());
        generalPointRecordPO2.setType(GeneralPointRecordConstant.POINT_ADD);
        generalPointRecordPO2.setCategory(GeneralPointRecordConstant.CATEGORY_COMPANY_INCOME);
        generalPointRecordPO2.setNum(goodGeneralpointTotal);
        generalPointRecordPO2.setOrderId(goodOrderPO.getId());
        generalPointRecordPO2.setMemo(goodOrderPO.getMessage());
        generalPointRecordPO2.setCreatedAt(System.currentTimeMillis());
        generalPointRecordPO2.setUpdatedAt(System.currentTimeMillis());
        generalPointRecordMapper.insert(generalPointRecordPO2);

        //交易上链
        ResponseBean<String> responseBean = null;
        ResponseBean<TransactionResultBean> queryTransaction = null;
        try {
            Commodity commodity = new Commodity();
            commodity.setMerchantId(goodOrderPO.getGid());
            commodity.setPointType(Brokerpoints.PointType.PPOINT_VALUE);
            commodity.setGeneral(goodGeneralpointTotal);
            responseBean = goodBc.buyCommodity(goodOrderPO.getUid(), userPrivatePo.getPublicKey(), userPrivatePo.getPrivateKey(), commodity);
            if (responseBean == null || responseBean.getError() != null || responseBean.getResult() == null) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.info("通用积分购买商品上链失败！");
                return new ResponseVO(CommonResponseEnum.CHAIN_FAILE);
            }
            queryTransaction = blockChainUtil.queryTransaction(responseBean.getResult());
            if (queryTransaction == null || StringUtil.isNotBlank(queryTransaction.getError())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return new ResponseVO(CommonResponseEnum.FAILURE, queryTransaction.getError());
            }
            log.info("通用积分购买商品上链成功！");


        } catch (Exception e) {
            log.info("通用积分购买商品上链失败: {}", e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseVO(CommonResponseEnum.CHAIN_FAILE);
        }

        //保存上链记录
        GeneralPointRecordPO savePo = new GeneralPointRecordPO();
        savePo.setId(generalPointRecordPO.getId());
        savePo.setHeight(queryTransaction.getResult().getHeight());
        savePo.setHash(responseBean.getResult());
        savePo.setUpdatedAt(System.currentTimeMillis());
        generalPointRecordMapper.updateByPrimaryKeySelective(savePo);

        savePo.setId(generalPointRecordPO2.getId());
        generalPointRecordMapper.updateByPrimaryKeySelective(savePo);

        //插入积分上链记录表
        BlockInfoPO blockInfoPO = new BlockInfoPO();
        blockInfoPO.setUid(uid);
        blockInfoPO.setOperationType(Brokerpoints.ActionType.BuyCommodity_VALUE);
        blockInfoPO.setHash(responseBean.getResult());
        blockInfoPO.setHeight(queryTransaction.getResult().getHeight());
        blockInfoPO.setCreatedAt(System.currentTimeMillis());
        blockInfoDao.insertBlockInfo(blockInfoPO);

        //更新订单为完成
        GoodOrderPO goodOrderPONew = new GoodOrderPO();
        goodOrderPONew.setId(goodOrderPO.getId());
        goodOrderPONew.setPayMode(GoodOrderConstant.PAY_MODEL_GLOBAL_PRICE);
        goodOrderPONew.setPayTotal(goodGeneralpointTotal);
        goodOrderPONew.setStatus(GoodOrderConstant.GOOD_TO_DELIVERED);
        goodOrderPONew.setPayedAt(System.currentTimeMillis());
        goodOrderPONew.setHash(responseBean.getResult());
        goodOrderPONew.setHeight(queryTransaction.getResult().getHeight());
        goodOrderDao.updateGoodOrder(goodOrderPONew);
        //添加用户为会员
        boolean isExit = companyMemberDao.isMember(goodOrderPO.getUid(), goodOrderPO.getGid());
        if (!isExit) {
            companyMemberDao.saveMember(goodOrderPO.getUid(), goodOrderPO.getGid(), System.currentTimeMillis());
        }
        return new ResponseVO(CommonResponseEnum.SUCCESS);
    }

    /**
     * 自动取消过期订单
     * @param outTime 过期时间
     */
    @Override
    public void autoCancelOrder(Long outTime) {
        //订单超时失效
        List<GoodOrderPO> outOrders = goodOrderDao.listOutTimeOrders(outTime);
        for (GoodOrderPO goodOrderPO : outOrders) {
            goodOrderDao.cancelOrder(goodOrderPO.getId());
            goodDao.cancelOrderGood(goodOrderPO.getGoodId(), goodOrderPO.getNum());
        }
    }

}
