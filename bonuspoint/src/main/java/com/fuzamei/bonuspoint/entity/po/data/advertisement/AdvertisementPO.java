package com.fuzamei.bonuspoint.entity.po.data.advertisement;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-09-13 15:49
 **/
@Data
@Table(name = "bp_advertisement")
public class AdvertisementPO {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;
    private String imgUrl;
    private String jumpUrl;
    private Long createdAt;
    private Long updatedAt;
    private Integer isDelete;
}
