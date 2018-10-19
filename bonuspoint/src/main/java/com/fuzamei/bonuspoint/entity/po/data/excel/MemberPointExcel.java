package com.fuzamei.bonuspoint.entity.po.data.excel;


import com.xuxueli.poi.excel.annotation.ExcelField;
import com.xuxueli.poi.excel.annotation.ExcelSheet;
import lombok.Data;
import org.apache.poi.hssf.util.HSSFColor;


import java.math.BigDecimal;

/**
 * @program: bonus-point-cloud-2
 * @description:
 * @author: WangJie
 * @create: 2018-10-08 14:51
 **/
@Data
@ExcelSheet(name = "商户列表", headColor = HSSFColor.HSSFColorPredefined.LIGHT_GREEN)
public class MemberPointExcel {
    /**
     * 用户id
     */
    @ExcelField(name = "用户id")

    private Long id;
    /**
     * 用户手机号
     */
    @ExcelField(name = "用户手机号")
    private String mobile;

    /** 用户注册时间**/
    @ExcelField(name = "用户注册时间")
    private String createdAt;
    /**
     * 地址
     */
    @ExcelField(name = "用户公钥")
    private String  publicKey;
    /**
     * 拥有会员身份所属集团的积分总量
     */
    @ExcelField(name = "商户积分总量")
    private BigDecimal totalCompanyPoint;
    /**
     * 拥有通用积分总量
     */
    @ExcelField(name = "通用积分总量")
    private BigDecimal totalGeneralPoint;
}
