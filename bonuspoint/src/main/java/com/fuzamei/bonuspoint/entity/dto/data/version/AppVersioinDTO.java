package com.fuzamei.bonuspoint.entity.dto.data.version;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-08-02 14:57
 **/
@Data
public class AppVersioinDTO {

    @NotBlank(message = "PARAMETER_ERROR" )
    private String version;

    @NotBlank(message = "PARAMETER_ERROR" )
    private String code;

    @NotBlank(message = "PARAMETER_ERROR" )
    private String downUrl;

    @NotBlank(message = "PARAMETER_ERROR" )
    private String description;

    @NotBlank(message = "PARAMETER_ERROR")
    @Pattern(regexp = "Andorid|IOS",message = "PARAMETER_ERROR")
    private String system;

    @NotBlank(message = "PARAMETER_ERROR")
    @Pattern(regexp = "[0-1]",message = "PARAMETER_ERROR")
    private String forcedUpdate;

}
