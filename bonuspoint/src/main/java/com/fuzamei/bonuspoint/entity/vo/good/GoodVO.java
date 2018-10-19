package com.fuzamei.bonuspoint.entity.vo.good;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodVO {
    /** 商品id */
    private Long id;
    /** 商品所属集团id */
    private Long gid;
    /** 所属集团名称 */
    private String companyName;
    /** 商品父分类tid */
    private Long tid;
    /** 商品父分类名称 */
    private String typeName;
    /** 商品子分类sid*/
    private Long sid;
    /** 商品子分类名称*/
    private String subTypeName;
    /** 商品名称 */
    private String name;
    /** 集团积分单价 */
    private BigDecimal price;
    /** 通用积分单价 */
    private BigDecimal globalPrice;
    /** 发布数量 */
    private Integer num;
    /** 已售数量 */
    private Integer numUsed;
    /** 单品价值 */
    private BigDecimal worth;
    /** 商品封面图片地址,多个用逗号分隔 */
    private String imgSrc;
    /** 商品详情 */
    private String details;
    /** 置顶等级 */
    private Integer topLevel;
    /** 排序等级 */
    private Integer orderLevel;
    /** 是否有有效期 true 有有效期 false 无有效期 */
    private Boolean islife;
    /** 开始时间 */
    private Long startAt;
    /** 过期时间 */
    private Long endAt;
    /** 商品状态（1->正常销售,0->下架，2->售完  3-> 删除*/
    private Integer status;
    /** 商品状态名称*/
    private String statusName;
    /** 创建时间 */
    private Long createdAt;
    /** 修改时间 */
    private Long updatedAt;


    /** 分红比例 */
    private Float rate;
}
