/**
 * 
 */
package com.fuzamei.bonuspoint.entity.dto.good;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liumeng
 * @create 2018年4月18日
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodDTO {
    /** 商品id */
    private Long id;
    /** 商品所属集团id */
    private Long gid;
    /** 商品子分类id */
    private Long sid;
    /** 商品分类名称 */
    private String name;
    /** 集团会员积分单价 */
    private BigDecimal price;
    /** 通用积分单价 */
    private BigDecimal globalPrice;
    /** 发布数量 */
    private Integer num;
    /** 单品价值 */
    private BigDecimal worth;
    /** 商品封面图片地址,多个用逗号分隔 */
    private String imageUrls;
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

    /** 商品返利比率*/
    private Float rate;

}
