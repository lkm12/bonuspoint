package com.fuzamei.bonuspoint.entity.dto.block;

import lombok.Data;

/**
 * @program: bonus-point-cloud
 * @description: 区块信息
 * @author: WangJie
 * @create: 2018-06-25 14:35
 **/
@Data
public class BlockInfoDTO {


    /**
     * 用户id
     */
    private Long uid;

    /**
     * 区块高度
     */
    private Long height;
    /**
     * 区块hash
     */
    private String hash;

    /**
     * 上链操作类型取以下数值
     *               //初始化平台
     * 	MsgInitPlatform = 4;
     * 	//创建新集团
     * 	MsgCreateGroup = 5;
     * 	//用户注册
     * 	MsgUserRegister = 6;
     * 	//设置集团备付金比率
     * 	MsgSetCashRate = 7;
     * 	//设置集团积分兑换比率
     * 	MsgSetPointRate = 8;
     * 	//集团发行新积分
     * 	MsgCreateGroupPoint = 9;
     * 	//积分过期
     * 	MsgExpirePoints = 10;
     * 	//集团赠予用户积分
     * 	MsgGroupToUserPoints = 11;
     * 	//通用积分兑换现金
     * 	MsgSellGeneralPoints = 12;
     * 	//集团存款（增加备付金）
     * 	MsgGroupDeposit = 13;
     * 	//集团取款
     * 	MsgWithdraw = 14;
     * 	//用户兑换通用积分
     * 	MsgBuyGeneralPoints = 15;
     * 	//用户购买商品
     * 	MsgBuyCommodity = 16;
     * 	//设置管理员账户
     * 	MsgSetAdmin = 17;
     * 	//账户信息转移
     * 	MsgTransferAccount = 18;
     * 	//增加银行资金
     * 	MsgIncreaseBankRmb = 19;
     * 	//用户付款
     * 	MsgUserPay = 20;
     * 	//添加商品
     * 	MsgCreateCommodity = 21;
     * 	//编辑商品
     * 	MsgEditCommodity = 22;
     * 	//上架商品
     * 	MsgEnableCommodity = 23;
     * 	//下架商品
     * 	MsgDisableCommodity = 24;
     * 	//删除商品
     * 	MsgDeleteCommodity = 25;
     * 	//添加商品分类
     * 	MsgCreateCommodityType = 26;
     * 	//删除商品分类
     * 	MsgDeleteCommodityType = 27;
     * 	//添加商品子分类
     * 	MsgCreateCommoditySubtype = 28;
     * 	//删除商品子分类
     * 	MsgDeleteCommoditySubtype = 29;
     */
    private Integer operationType;
    /**创建时间*/
    private Long createdAt;
}
