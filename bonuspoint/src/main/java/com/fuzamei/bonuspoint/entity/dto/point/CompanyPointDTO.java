/**
 * FileName: CompanyDTO
 * Author: wangtao
 * Date: 2018/4/25 15:54
 * Description:
 */
package com.fuzamei.bonuspoint.entity.dto.point;

import lombok.Data;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

/**
 *
 *
 * @author wangtao
 * @create 2018/4/25
 *
 */
@Data
public class CompanyPointDTO {

    /** 对方用户id */
    private Long toid;
    /** 用户id **/
    private Long uid;
    /** 交易密码 */
    private String payword;
    /** 积分数量 */
    @Min(value = 0, message = "POINT_NUM_NULL")
    private BigDecimal num;
    /** 备注 */
    private String memo;
    /** 起始时间 */
    private String startTime;
    /** 结束时间 */
    private String endTime;
    /** 是否有有效期 */
    private Integer isLife;
    /** 积分名称 */
    private String pointName;
    /** 积分id */
    private Long pointId;
    /** 集团id */
    private Long companyId;
    /** 当前页 */
    private Integer currentPage;
    /** 每页记录数 */
    private Integer pageSize;
    /** 账户手机号 */
    private String mobile;
    /** 账户昵称 */
    private String userName;
    /** 账户公钥 */
    private String publicKey;

}
