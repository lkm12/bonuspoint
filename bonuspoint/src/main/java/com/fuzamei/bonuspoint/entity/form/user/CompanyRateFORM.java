package com.fuzamei.bonuspoint.entity.form.user;

import com.fuzamei.bonuspoint.validation.Payword;
import com.fuzamei.bonuspoint.validation.group.CompanyInfo;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * @program: bonus-point-cloud
 * @description: 集团积分兑换比例和备付金比例
 * @author: WangJie
 * @create: 2018-07-23 10:17
 **/
@Data
public class CompanyRateFORM {

    /**集团id*/
    @NotNull(message = "{PARAMETER_ERROR}" ,groups = CompanyInfo.class)
    private Long id;
    /**交易密码*/
    @NotBlank(message = "{PARAMETER_ERROR}" ,groups = CompanyInfo.class)
    @Payword(groups = CompanyInfo.class)
    private String payword;
    /** 备付金比例 */
    @NotNull(message = "{PARAMETER_ERROR}" ,groups = CompanyInfo.updateCashRate.class)
    @Min(value = 0,message ="{CASH_RATE_ERROR}",groups = CompanyInfo.updateCashRate.class)
    @Max(value = 1,message ="{CASH_RATE_ERROR}",groups = CompanyInfo.updateCashRate.class)
    @Null(message = "{PARAMETER_ERROR}" ,groups = CompanyInfo.updatePointRate.class)
    private Float cashRate;
    /** 通用积分兑换比例（N：1通用积分） */
    @NotNull(message = "{PARAMETER_ERROR}" ,groups = CompanyInfo.updatePointRate.class)
    @Min(value = 0,message ="{POINT_RATE_ERROR}",groups = CompanyInfo.updatePointRate.class)
    @Null(message = "{PARAMETER_ERROR}" ,groups = CompanyInfo.updateCashRate.class)
    private Float pointRate;
}
