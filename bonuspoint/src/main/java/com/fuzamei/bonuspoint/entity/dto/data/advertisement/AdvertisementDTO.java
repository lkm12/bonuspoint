package com.fuzamei.bonuspoint.entity.dto.data.advertisement;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @program: bonus-point-cloud
 * @description: 广告
 * @author: WangJie
 * @create: 2018-09-13 15:40
 **/
@Data
public class AdvertisementDTO {
    private Integer id;
    @NotBlank(message = "PARAMETER_BLANK")
    private String imgUrl;
    private String jumpUrl;
}
