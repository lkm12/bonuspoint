package com.fuzamei.bonuspoint.entity.dto.user;

import com.fuzamei.bonuspoint.validation.Phone;
import com.fuzamei.bonuspoint.validation.group.Address;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-04-20 15:44
 **/
@Log
@Data
@NoArgsConstructor
public class UserAddressDTO {
    /** 地址id*/
    @NotNull(message = "{PARAMETER_ERROR}" ,groups = {Address.UpdateAddress.class})
    @Min(value = 1,groups = {Address.UpdateAddress.class})
    private Long id;
    /** 用户id*/
    private Long uid;
    /** 收件人*/
    @NotBlank(message = "{PARAMETER_ERROR}" ,groups = {Address.class})
    private String receiver;
    /** mobile*/
    @Phone(groups = {Address.class})
    @NotBlank(message = "{PARAMETER_ERROR}",groups = {Address.class})
    private String mobile;
    /** 街道编码*/
    @Min(value = 0 ,message = "{PARAMETER_ERROR}" , groups = {Address.class})
    private Long districtCode;
    /** 详细地址*/
    @NotBlank(message = "{PARAMETER_ERROR}",groups = {Address.class})
    private String areaDetail;
    /** 创建时间*/
    private Long createdAt;
    /** 修改时间 */
    private Long updateAt;

    private String provinceName;
    private String cityName;
    private String districtName;
    private String streetName;
    private Integer zipCode;

    //是否是默认收货地址 1是，0不是
    private Integer isDefaultAddress;

}