/**
 * FileName: ExchangeDTO
 * Author: wangtao
 * Date: 2018/4/24 21:41
 * Description:
 */
package com.fuzamei.bonuspoint.entity.dto.point;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fuzamei.bonuspoint.validation.ExNum;
import com.fuzamei.bonuspoint.validation.PointType;
import com.fuzamei.bonuspoint.validation.group.Point;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 兑换类
 *
 * @author wangtao
 * @create 2018/4/24
 *可作为bp_point_record的DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExchangePointDTO {
    /**用户id*/
    @NotNull(message = "{CASH_RECORD_ID}",groups = {Point.PointInfo.class})
    private Long id;
    /** 操作者id 或 集团与平台的uid*/
    private Long uid;
    /** 所要兑换的集团积分所属集团id */
    @NotNull(message = "{POINT_COMPANY_NOT_BANK}",groups = {Point.PointExchange.class})
    private Long groupId;

    /** 兑换数量 */

    @NotNull(message = "{POINT_NUM_NULL}",groups = {Point.PointTranfer.class,Point.PointExchange.class})
    @ExNum(message = "{EXNUM_BLANK}",groups = {Point.PointTranfer.class,Point.PointExchange.class})
    private BigDecimal exNum;
    /** 交易密码 */

    @NotBlank(message = "{CASH_RECORD_PASSWORD}",groups = {Point.PointExchange.class})
    private String payWord;
    /** 对方公钥 */

    @NotBlank(message = "{USER_PUBLICKEY_NULL}",groups = {Point.PointTranfer.class,Point.PointList.class})
    private String opPubKey;
    /** 积分类型（1>集团商户积分，2>平台积分，3->大平台积分) */
    @NotNull(message = "{POINTTYPE_BLANK}",groups = {Point.PointTranfer.class,Point.PointInfo.class})
    @PointType(message = "{POINTTYPE_FORMAT_ERROR}",groups = {Point.PointTranfer.class,Point.PointInfo.class})
    private Integer pointType;
    /** 积分类型（1>集团商户积分，2>平台积分，3->大平台积分) string */
    private String pointTypeStr;

    /** 对方用户id */
    private Long oppositeUid;
    /** 加减积分标志（1->加积分,2->减积分） */
    private Integer type;
    /** 加减积分标志（1->加积分,2->减积分） string */
    private String typeStr;
    /** 积分交易类型（1->集团申请积分,2->集团发放积分给用户,3->用户收入集团发放的积分,
     * 4->用户兑换通用积分,5->平台结算通用积分,6->用户购买集团服务,7->用户转出积分,8->他人转入积分,9->退货支出，10->退货返还 */
    private Integer category;
    /**区块高度*/
    private Long height;
    /**区块hash*/
    private String hash;
    /** 积分Id */
    private Long pointId;
    /** 平台积分Id */
    private Long generalPointId;
    /** 集团积分与通用积分的兑换比 */
    private Float pointRate;
    /** 积分数量 */
    private BigDecimal num;
    /** 是否外部交易订单(0->否,1->是) */
    private Integer isOutOrder;
    /** 用户交易订单Id */
    private String orderId;
    /** 交易备注信息 */
    private String memo;
    /** 状态（0->未审核,1->成功，101->失败） */
    private Integer status;
    /** 原因 */
    private String reason;
    /** 创建时间 */
    private Long createdAt;
    /** 修改时间 */
    private Long updatedAt;
    /** 备付金 */
    private BigDecimal amount;
    /** 所要兑换的通用积分所属平台id */
   // @NotNull(message = "{PLATFORMID_BLANK}",groups = {Point.PointExchange.class})
    private Long platformId;
    /** 用户手机 */
    private String mobile;
    /** 交易密码加密 */
    private String payWordHash;
    /** 平台uid */
    private Long platformUid;

    private String name;




}
