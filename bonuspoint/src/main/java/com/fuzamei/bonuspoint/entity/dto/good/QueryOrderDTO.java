package com.fuzamei.bonuspoint.entity.dto.good;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单查询类
 * @author liumeng
 * @create 2018年4月25日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryOrderDTO {  
    /** 订单号*/
    private Long id ; 
    /** 用户标识*/
    private Long uid;
    /** 集团标识*/
    private Long gid;
    /** 平台标识*/
    private Long pid;
    /** 集团名称，支持模糊查询*/
    private String companyName;
    /** 商品名称（模糊查询）*/
    private String goodName;
    /**订单状态 0->待结算,1->待发货,2->运输中,3->待确认收货,4->成功交易,5->退货中待集团确认,6->退货成功,99->买家关闭交易,100->超时失效,101->隐藏订单*/
    private Integer status;
    /** 分页信息*/
    private Integer currentPage;
    /** 每页大小 */
    private Integer pageSize;
    
}
