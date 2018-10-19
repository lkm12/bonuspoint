package com.fuzamei.bonuspoint.entity.po.data.version;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-08-02 14:57
 **/
@Data
@Table(name = "bp_app_version")
public class AppVersioinPO {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    private String version;

    private String code;

    private String downUrl;

    private String description;

    private Long createdAt;

    private Integer forcedUpdate;

    private String system;


}
