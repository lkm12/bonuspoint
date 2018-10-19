package com.fuzamei.bonuspoint.blockchain.bc;

import brokerpoints.Brokerpoints;
import com.fuzamei.bonuspoint.blockchain.util.BlockChainUtil;
import com.fuzamei.bonuspoint.constant.Roles;
import com.fuzamei.bonuspoint.dao.common.mapper.CashRecordMapper;
import com.fuzamei.bonuspoint.dao.common.mapper.GeneralPointRecordMapper;
import com.fuzamei.bonuspoint.dao.common.mapper.PointRecordMapper;
import com.fuzamei.bonuspoint.dao.user.UserDao;
import com.fuzamei.bonuspoint.entity.po.point.GeneralPointRecordPO;
import com.fuzamei.bonuspoint.entity.po.point.PointPO;
import com.fuzamei.bonuspoint.entity.po.point.PointRecordPO;
import com.fuzamei.bonuspoint.enums.BlockChainResponseEnum;
import com.fuzamei.bonuspoint.service.impl.user.UserServiceMethodDan;
import com.fuzamei.bonuspoint.util.StringUtil;
import com.fzm.blockchain.entity.ResponseBean;
import com.fzm.blockchain.entity.TransactionResultBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/6/19 18:55
 */
@Slf4j
@Component
public class MemberBC {


    private final BlockChainUtil blockChainUtil;
    private final UserDao userDao;
    private final GeneralPointRecordMapper generalPointRecordMapper;
    private final PointRecordMapper pointRecordMapper;
    private final CashRecordMapper cashRecordMapper;
    private final UserServiceMethodDan userServiceMethodDan;

    @Autowired
    public MemberBC(BlockChainUtil blockChainUtil, UserDao userDao, GeneralPointRecordMapper generalPointRecordMapper, PointRecordMapper pointRecordMapper, CashRecordMapper cashRecordMapper, UserServiceMethodDan userServiceMethodDan) {

        this.blockChainUtil = blockChainUtil;
        this.userDao = userDao;
        this.generalPointRecordMapper = generalPointRecordMapper;
        this.pointRecordMapper = pointRecordMapper;
        this.cashRecordMapper = cashRecordMapper;
        this.userServiceMethodDan = userServiceMethodDan;
    }

    /**
     * @param companyId  商户id
     * @param pointId    积分id
     * @param quantity   积分数量
     * @param expiration 过期时间
     * @param userId     用户id
     * @param publicKey  商户公钥
     * @param privateKey 商户私钥
     * @return 上链结果
     * @author
     * @description 向用户发放积分
     */
    public Map<String ,Object> giveUserPoints(String companyId, String pointId, Long expiration,
                                              String quantity, String userId, String publicKey, String privateKey) {
        log.info("向用户发放积分上链开始：");
        Brokerpoints.PointsGiveUserPoints.Builder builder = Brokerpoints.PointsGiveUserPoints.newBuilder();
        builder.setId(companyId);
        builder.setPointId(pointId);
        if(expiration == 0){
            builder.setExpiration(Long.MAX_VALUE);
        }else {
            builder.setExpiration(expiration);
        }
        builder.setQuantity(quantity);
        builder.setUserId(userId);
        builder.setAccountType(Brokerpoints.AccountType.MERCHANT);
        Brokerpoints.PointsAction.Builder request = Brokerpoints.PointsAction.newBuilder();
        request.setGiveUserPoints(builder);
        request.setTy(Brokerpoints.ActionType.GiveUserPoints_VALUE);
        ResponseBean<String> responseBean = null;

        try {
            responseBean = blockChainUtil.sendTransaction(publicKey, privateKey, request.build());
        } catch (Exception e) {
            log.error(BlockChainResponseEnum.BLOCK_CHAIN_ERROR.getMessage());
        }
        if (responseBean == null) {
            log.error("向用户发放积分上链失败：null");
            log.error(BlockChainResponseEnum.ADD_BLOCK_FAILED.getMessage());
        } else if (responseBean.getError() != null || responseBean.getResult() == null) {
            log.error("向用户发放积分上链失败：{}", responseBean.getError());
            log.error(BlockChainResponseEnum.ADD_BLOCK_FAILED.getMessage());
        }
        ResponseBean<TransactionResultBean> queryTransaction = blockChainUtil.queryTransaction(responseBean.getResult());
        if (queryTransaction == null) {
            log.info("向用户发放积分上链失败");
            throw new RuntimeException("向用户发放积分上链失败");
        }
        if (StringUtil.isNotBlank(queryTransaction.getError())) {
            log.info("向用户发放积分上链失败");
            throw new RuntimeException("向用户发放积分上链失败");
        }

        log.info("向用户发放积分上链成功");
        Long height = queryTransaction.getResult().getHeight();
        String hash = responseBean.getResult();
        Map<String ,Object> map = new HashMap<>();
        map.put("hash",hash);
        map.put("height",height);
        map.put("type",Brokerpoints.ActionType.GiveUserPoints.getNumber());

        return map;
    }

    /**
     * 用商户积分兑换所在平台（仅限商户所在平台）通用积分
     * lkm
     * @param
     * @param
     * @param
     * @param generalPoint
     * @return
     */
    public void pointsMerchantToGeneral(List<PointRecordPO> pointRecordPOList,GeneralPointRecordPO generalPointRecordPO,
                                        String id, String merchantId, BigDecimal generalPoint, List<PointPO> list,
                                        String publicKey, String privateKey) throws Exception {

        Brokerpoints.PointsMerchantToGeneral.Builder builder = Brokerpoints.PointsMerchantToGeneral.newBuilder();
        //商户id
        builder.setMerchantId(merchantId);
        //兑换者（商户或用户）id
        builder.setId(id);
        //兑换者账户类型
        builder.setAccountType(Brokerpoints.AccountType.USER);
        //兑换获得的通用积分
        builder.setGeneralPoints(String.valueOf(generalPoint));

       List<PointPO> list1 = list.stream().filter(x -> !String.valueOf(x.getNumSend()).equals("0.0000")).collect(Collectors.toList());

        for (PointPO po: list1) {
            Brokerpoints.MerchantPoint.Builder merchantPoints = Brokerpoints.MerchantPoint.newBuilder();
            merchantPoints.setId(String.valueOf(po.getPointId()));
            merchantPoints.setQuantity( Float.valueOf(String.valueOf(po.getNumSend())));
            if(po.getEndTime().equals("0")){
                merchantPoints.setExpiration(Long.MAX_VALUE);
            }else{
                merchantPoints.setExpiration(Long.valueOf(po.getEndTime()));
            }
            builder.addMerchantPoints(merchantPoints);
        }

        Brokerpoints.PointsAction.Builder builder1 = Brokerpoints.PointsAction.newBuilder();
        builder1.setMerchantToGeneral(builder);
        builder1.setTy(Brokerpoints.ActionType.MerchantToGeneral_VALUE);

        //发送区块链
       ResponseBean<String> responseBean = blockChainUtil.sendTransaction(publicKey,privateKey,builder1.build());

        if (responseBean == null) {

            log.info("兑换通用积分上链失败：null");
            throw new RuntimeException("兑换通用积分上链失败");
        }
        if (responseBean.getError() != null || responseBean.getResult() == null) {

            log.info("兑换通用积分上链失败：{}", responseBean.getError());
            throw new RuntimeException("兑换通用积分上链失败");
        }

        //查询结果
        ResponseBean<TransactionResultBean> queryTransaction = blockChainUtil.queryTransaction(responseBean.getResult());
        if (queryTransaction == null) {

            throw new RuntimeException("兑换通用积分上链失败");
        }
        if (StringUtil.isNotBlank(queryTransaction.getError())) {

            throw new RuntimeException("兑换通用积分上链失败");
        }
        log.info("兑换通用积分上链成功");

        String hash = responseBean.getResult();
        Long height = queryTransaction.getResult().getHeight();

        userServiceMethodDan.addBlockInfo(hash,height,Brokerpoints.ActionType.MerchantToGeneral.getNumber(),Long.valueOf(id));

        GeneralPointRecordPO generalPointRecordPO1 = new GeneralPointRecordPO();
        generalPointRecordPO1.setId(generalPointRecordPO.getId());
        generalPointRecordPO1.setHash(hash);
        generalPointRecordPO1.setHeight(height);

        //更新hash与height
       int i = generalPointRecordMapper.updateByPrimaryKeySelective(generalPointRecordPO1);
       if(i < 0){
           log.info("更新区块链回执失败");
           throw new RuntimeException("更新区块链回执失败");
       }

        for (PointRecordPO pointRecordPO:pointRecordPOList) {
            PointRecordPO pointRecordPO1 = new PointRecordPO();
            pointRecordPO1.setId(pointRecordPO.getId());
            pointRecordPO1.setHash(hash);
            pointRecordPO1.setHeight(height);
            i = pointRecordMapper.updateByPrimaryKeySelective(pointRecordPO1);
            if(i < 0){
                log.info("更新区块链回执失败");
                throw new RuntimeException("更新区块链回执失败");
            }
        }

    }

/**
 * 平台向用户发放通用积分
 * lkm
 *
 * @param
 * @param
 *
 * @param
 * @param userPublicKey
 * @return
 */

public Map<String, Object> pointsGiveUserPoints(String id,Integer role,Long userId,BigDecimal quantity,String userPublicKey,String userPrivateKey) throws Exception {

    Brokerpoints.PointsGiveUserPoints.Builder builder = Brokerpoints.PointsGiveUserPoints.newBuilder();

    //积分发放者id
    builder.setId(id);

    //发放者账户类型（总平台，平台，商户）
    if(role == Roles.SUPER_MANAGER){
        builder.setAccountType(Brokerpoints.AccountType.TPLATFORM);
    }else if(role == Roles.PLATFORM){
        builder.setAccountType(Brokerpoints.AccountType.PLATFORM);
    }

    //积分数量
    builder.setQuantity(String .valueOf(quantity));
    //获得发放积分的用户id
    builder.setUserId(String.valueOf(userId));

    Brokerpoints.PointsAction.Builder builder1 = Brokerpoints.PointsAction.newBuilder();
    builder1.setGiveUserPoints(builder);
    builder1.setTy(Brokerpoints.ActionType.GiveUserPoints_VALUE);

    //发送区块链
    ResponseBean<String> responseBean = blockChainUtil.sendTransaction(userPublicKey,userPrivateKey,builder1.build());

    if(responseBean == null){
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        log.info("发送通用积分上链失败：null");
        throw new RuntimeException("发送通用积分上链失败");
    }
    if(responseBean.getError() != null || responseBean.getResult() == null){
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        log.info("发送通用积分上链失败：{}",responseBean.getError());
        throw new RuntimeException("发送通用积分上链失败");
    }
    //查询结果
    ResponseBean<TransactionResultBean> queryTransaction =  blockChainUtil.queryTransaction(responseBean.getResult());

    if(queryTransaction == null){
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        throw new RuntimeException("发送通用积分上链失败");
    }
    if(StringUtil.isNotBlank(queryTransaction.getError())){
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        throw new RuntimeException("发送通用积分上链失败");
    }
    log.info("发送通用积分上链上链成功");

    String hash = responseBean.getResult();
    Long height = queryTransaction.getResult().getHeight();
    Map<String, Object> map = new HashMap<>();
    map.put("hash",hash);
    map.put("height",height);
    map.put("type",Brokerpoints.ActionType.GiveUserPoints.getNumber());
    return map;
}

    /**
     * 用户注册
     */
    public void userRegister(String id,String userPublicKey,String userPrivateKey) throws Exception {
        Brokerpoints.PointsUserRegister.Builder builder = Brokerpoints.PointsUserRegister.newBuilder();

        builder.setId(id);

        Brokerpoints.PointsAction.Builder builder1 = Brokerpoints.PointsAction.newBuilder();
        builder1.setUserRegister(builder);
        builder1.setTy(Brokerpoints.ActionType.UserRegister_VALUE);

        //发送区块链
        ResponseBean<String> responseBean = blockChainUtil.sendTransaction(userPublicKey,userPrivateKey,builder1.build());

        if(responseBean == null){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.info("用户注册上链失败：null");
            throw new RuntimeException("用户注册上链失败");
        }
        if(responseBean.getError() != null || responseBean.getResult() == null){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.info("用户注册上链失败：{}",responseBean.getError());
            throw new RuntimeException("用户注册上链失败");
        }
        //查询结果
        ResponseBean<TransactionResultBean> queryTransaction =  blockChainUtil.queryTransaction(responseBean.getResult());

        if(queryTransaction == null){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new RuntimeException("用户注册上链失败");
        }
        if(StringUtil.isNotBlank(queryTransaction.getError())){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new RuntimeException("用户注册上链失败");
        }
        log.info("用户注册上链成功");

        String hash = responseBean.getResult();
        Long height = queryTransaction.getResult().getHeight();
        //将哈希记录插表
        userServiceMethodDan.addBlockInfo(hash,height,Brokerpoints.ActionType.UserRegister.getNumber(),Long.valueOf(id));
}

    /**
     * 用户之间转积分
     * @param from
     * @param to
     * @param role
     * @param list
     * @param platformId
     * @param general
     * @param tgeneral
     */
        public void pointsTransferPoints(Map<String ,GeneralPointRecordPO> mapGeneral,List<PointRecordPO> pointRecordPOSend,List<PointRecordPO> pointRecordPOAccept,String from,String to,Integer role,List<PointPO> list,String merchantId,String platformId,String general,String tgeneral,
                                         String userPublicKey,String userPrivateKey) throws Exception {

            Brokerpoints.PointsTransferPoints.Builder builder = Brokerpoints.PointsTransferPoints.newBuilder();
            //转出用户id
            builder.setFrom(from);
            //转入用户id
            builder.setTo(to);
            //积分类型
            if(role == Roles.PLATFORM){
                builder.setPointType(Brokerpoints.PointType.PPOINT);
            }else if(role == Roles.COMPANY){
                builder.setPointType(Brokerpoints.PointType.MPOINT);
            }else if(role == Roles.SUPER_MANAGER){
                builder.setPointType(Brokerpoints.PointType.TPOINT);
            }


            if(role == Roles.COMPANY){

                //商户积分
                Brokerpoints.UserMPoint.Builder builder1 = Brokerpoints.UserMPoint.newBuilder();
                //商户id
                builder1.setMerchantId(merchantId);

                List<PointPO> list1 = list.stream().filter(x -> !String.valueOf(x.getNumSend()).equals("0.0000")).collect(Collectors.toList());

                for (PointPO pointPO:list1) {
                    //商户积分
                    Brokerpoints.MerchantPoint.Builder builder2 = Brokerpoints.MerchantPoint.newBuilder();
                    //积分数量
                    builder2.setQuantity(Float.valueOf(String.valueOf(pointPO.getNumSend())));

                    //积分id
                    builder2.setId(String.valueOf(pointPO.getPointId()));

                    //积分过期时间
                    if(pointPO.getEndTime().equals("0")){
                        builder2.setExpiration(Long.MAX_VALUE);
                    }else {
                        builder2.setExpiration(Long.valueOf(pointPO.getEndTime()));
                    }
                    builder1.addMerchantPoints(builder2);
                }
                //商户积分
                builder.setUserMPoint(builder1);

                //发送至区块链
               Map<String,Object> map = send(userPublicKey,userPrivateKey,builder,from);
                String hash = (String) map.get("hash");
                Long height = (Long) map.get("height");


                for (PointRecordPO pointRecordPO: pointRecordPOSend) {
                    PointRecordPO pointRecordPO1 = new PointRecordPO();
                    pointRecordPO1.setId(pointRecordPO.getId());
                    pointRecordPO1.setHash(hash);
                    pointRecordPO1.setHeight(height);
                    //改变商户积分操作表中的hash与height
                   int i = pointRecordMapper.updateByPrimaryKeySelective(pointRecordPO1);
                   if(i < 0){
                       log.info("改变发送者商户积分操作表失败");
                      throw new RuntimeException("改变发送者商户积分操作表失败");
                   }
                }

                for (PointRecordPO pointRecordPO:pointRecordPOAccept) {
                    PointRecordPO pointRecordPO1 = new PointRecordPO();
                    pointRecordPO1.setId(pointRecordPO.getId());
                    pointRecordPO1.setHash(hash);
                    pointRecordPO1.setHeight(height);
                    //改变商户积分操作表中的hash与height
                    int i = pointRecordMapper.updateByPrimaryKeySelective(pointRecordPO1);
                    if(i < 0){
                        log.info("改变接收者商户积分操作表失败");
                        throw new RuntimeException("改变接收者商户积分操作表失败");
                    }
                }

            }else if(role == Roles.PLATFORM ){
                builder.setPlatformId(platformId);
                builder.setGeneral(general);


                //发送至区块链
                Map<String,Object> map = send(userPublicKey,userPrivateKey,builder,from);
                String hash = (String) map.get("hash");
                Long height = (Long) map.get("height");
                GeneralPointRecordPO generalPointRecordPOSend = mapGeneral.get("sendRecord");
                GeneralPointRecordPO generalPointRecordPOAccept = mapGeneral.get("acceptRecord");

                GeneralPointRecordPO generalPointRecordPO = new GeneralPointRecordPO();
                generalPointRecordPO.setId(generalPointRecordPOSend.getId());
                generalPointRecordPO.setHash(hash);
                generalPointRecordPO.setHeight(height);
                //改变通用积分操作记录中的hash与height
               int i = generalPointRecordMapper.updateByPrimaryKeySelective(generalPointRecordPO);
                if(i < 0){
                    log.info("改变发送者通用积分操作记录失败");
                    throw new RuntimeException("改变发送者通用积分操作记录失败");
                }

                generalPointRecordPO.setId(generalPointRecordPOAccept.getId());
                generalPointRecordPO.setHeight(height);
                generalPointRecordPO.setHash(hash);
                i = generalPointRecordMapper.updateByPrimaryKeySelective(generalPointRecordPO);
                if(i < 0){
                    log.info("改变接收者通用积分操作记录失败");
                    throw new RuntimeException("改变接收者通用积分操作记录失败");
                }
            }else{
                builder.setPlatformId(platformId);
                builder.setTgeneral(tgeneral);

                //发送至区块链
                try {
                    send(userPublicKey,userPrivateKey,builder,from);
                }catch (Exception e){
                    throw new RuntimeException();
                }

            }



            }

            public Map send(String userPublicKey,String userPrivateKey,Brokerpoints.PointsTransferPoints.Builder builder,String from) throws Exception {

                Brokerpoints.PointsAction.Builder request = Brokerpoints.PointsAction.newBuilder();

                request.setTy(Brokerpoints.ActionType.TransferPoints_VALUE);
                request.setTransferPoints(builder);
                //发送区块链
                ResponseBean<String> responseBean = blockChainUtil.sendTransaction(userPublicKey,userPrivateKey,request.build());

                if(responseBean == null){

                    log.info("用户之间转积分上链失败：null");
                    throw new RuntimeException("用户之间转积分上链失败");
                }
                if(responseBean.getError() != null || responseBean.getResult() == null){

                    log.info("用户之间转积分上链失败：{}",responseBean.getError());
                    throw new RuntimeException("用户之间转积分上链失败");
                }
                //查询结果
                ResponseBean<TransactionResultBean> queryTransaction =  blockChainUtil.queryTransaction(responseBean.getResult());

                if(queryTransaction == null){

                    throw new RuntimeException("用户之间转积分上链失败");
                }
                if(StringUtil.isNotBlank(queryTransaction.getError())){

                    throw new RuntimeException("用户之间转积分上链失败");
                }
                log.info("用户之间转积分上链成功");

                String hash = responseBean.getResult();
                Long height = queryTransaction.getResult().getHeight();

                Map map = new HashMap();
                map.put("hash",hash);
                map.put("height",height);
                userServiceMethodDan.addBlockInfo(hash,height,Brokerpoints.ActionType.TransferPoints.getNumber(),Long.valueOf(from));

                return map;
            }


}

