package com.fuzamei.bonuspoint.entity.dto.good;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liumeng
 * @create 2018年4月20日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryGoodDTO {
    /**集团标识  */
    private Long gid;
    /** 父分类标识 */
    private Long tid;
    /** 子分类标识*/
    private Long sid;
    /** 平台id*/
    private Long pid;
    /** 商品名称 支持模糊查询 */
    private String goodName;
    /** 商户名称 支持模糊查询*/
    private String companyName;
    /** 查询商品状态（（1->正常销售,0->下架，2->售完 ，3->删除））*/
    private String status;
    /** 当前页  */
    private Integer currentPage;
    /** 每页大小 */
    private Integer pageSize;
    /** 置顶等级排序(1->倒序,2->正序) */
    private String topOrder;
    /** 排序等级排序(1->倒序,2->正序) */
    private String orderOrder;
    /** 时间排序(1->倒序,2->正序) */
    private String timeOrder;
    /** 销量排序(1->倒序,2->正序) */
    private String sellOrder;
    /** 价格排序(1->倒序,2->正序) */
    private String priceOrder;

}
