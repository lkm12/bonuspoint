package com.fuzamei.bonuspoint.entity.po.data.version;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @program: bonus-point-cloud
 * @description: ios版本信息
 * @author: WangJie
 * @create: 2018-08-21 15:35
 **/
@Data
@Table(name = "bp_app_ios_version")
public class IOSVersionPO {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    private String version;

    private String code;

    private String description;

    private Long createdAt;

    private String downUrl;

    /**
     *     是否强制更新 0否，1是
     */

    private Integer forcedUpdate;
}
