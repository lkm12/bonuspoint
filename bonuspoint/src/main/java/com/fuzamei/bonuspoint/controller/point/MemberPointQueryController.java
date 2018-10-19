package com.fuzamei.bonuspoint.controller.point;

import com.fuzamei.bonuspoint.aop.annotation.LogAnnotation;
import com.fuzamei.bonuspoint.constant.PointInfoConstant;
import com.fuzamei.bonuspoint.entity.Token;
import com.fuzamei.bonuspoint.entity.dto.point.ExchangePointDTO;
import com.fuzamei.bonuspoint.entity.dto.point.PointRecordDTO;
import com.fuzamei.bonuspoint.entity.dto.point.QueryPointDTO;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.service.point.MemberPointService;
import com.fuzamei.bonuspoint.validation.group.Point;
import com.fuzamei.common.bean.PageBean;
import com.fuzamei.common.model.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/5/9 10:31
 */
@Slf4j
@RestController
@RequestMapping("/bonus-point/point/member")
public class MemberPointQueryController {

    @Value("${page.pageSize}")
    private Integer size;

    @Value("${page.maxSize}")
    private Integer maxSize;



    private final MemberPointService memberPointService;

    @Autowired
    public MemberPointQueryController(  MemberPointService memberPointService) {
        this.memberPointService = memberPointService;
    }


    /**
     * 会员获取积分明细，支持分页
     *
     * @param queryDTO 接收参数
     *                 {
     *                 type: 出入标志（1>入，2>出）
     *                 startTime: 查询起始时间
     *                 endTime: 查询结束时间
     *                 pointType: 积分类型（1>集团商户积分，2>通用积分) 通用积分与集团积分的流水分开查询，此处pointType为必需值
     *                 page: 当前页，默认1
     *                 pageSize: 每页记录数，默认15
     *                 }
     * @return 响应信息
     * @author wangjei
     */
    @LogAnnotation(note = "会员获取积分明细")
    @PostMapping("point-list-detail")
    public ResponseVO<PageBean<PointRecordDTO>> memberPointListDetail(@RequestAttribute("token") Token token, @RequestBody QueryPointDTO queryDTO) {


        // 判断出入标志是否合法
        Boolean flag = queryDTO.getType() == null || queryDTO.getType() == PointInfoConstant.POINT_IN || queryDTO.getType() == PointInfoConstant.POINT_OUT;
        if (!flag) {
            log.info("出入标志不合法");
            return new ResponseVO<>(CommonResponseEnum.PARAMETER_ERROR);
        }
        // 判断积分类型是否合法
        flag = queryDTO.getPointType() == PointInfoConstant.GENERAL_POINT || queryDTO.getPointType() == PointInfoConstant.COMPANY_POINT;
        if (!flag) {
            log.info("积分类型不合法");
            return new ResponseVO<>(CommonResponseEnum.PARAMETER_ERROR);
        }

        // 设置默认值
        if (queryDTO.getPage() == null || queryDTO.getPage() <= 0) {
            queryDTO.setPage(1);
        }
        // 设置默认值
        if (queryDTO.getPageSize() == null || queryDTO.getPageSize() <= 0) {
            queryDTO.setPageSize(size);
        }
        if (queryDTO.getPageSize() > maxSize) {
            queryDTO.setPageSize(maxSize);
        }

        queryDTO.setUid(token.getUid());
        return memberPointService.memberPointListDetail(queryDTO);
    }


    /**
     * 兑换通用积分的积分列表获取
     *
     * @return 响应数据{
     * [
     * companyId:   公司编号
     * companyName: 公司名称
     * publicKey:   公钥
     * img:         头像图片地址
     * pointRate:   积分兑换比例
     * num:         积分持有数量
     * status:      积分状态 3->已过期 其他->可兑换
     * ]
     * }
     * @author wangjie
     */
    @LogAnnotation(note = "会员获取兑换通用积分的积分列表")
    @GetMapping("point-list-relation")
    public ResponseVO memberPointListRelation(@RequestAttribute("token") Token token) {
        QueryPointDTO queryPointDTO = new QueryPointDTO();
        queryPointDTO.setUid(token.getUid());

        return memberPointService.memberPointListRelation(queryPointDTO);
    }




    /**
     * 获取可转积分列表
     * lkm
     *
     * @param
     *                 {
     *                 opPubKey: 对方公钥
     *                 }
     * @return 响应
     */
    @PostMapping("tranpoint-list")
    public ResponseVO memberTranPointList(@RequestAttribute("token") Token token, @RequestBody @Validated(Point.PointList.class) ExchangePointDTO exchangeDTO,
                                          BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            log.info("参数错误：{}", bindingResult.getFieldError().getDefaultMessage());
            return new ResponseVO<>(CommonResponseEnum.FAILURE, bindingResult.getFieldError().getDefaultMessage());
        }

        exchangeDTO.setUid(token.getUid());
        return memberPointService.memberTranPointList(exchangeDTO);
    }



    /**
     * 获取积分使用明细详情（用户）
     * lkm
     *
     * @param
     *                 {
     *
     *                 pointType:积分类型
     *                 id:流水号
     *
     *                 }
     * @return 响应
     */
    @PostMapping("point-list-info")
    public ResponseVO pointListInfo(@RequestAttribute("token") Token token, @RequestBody @Validated(Point.PointInfo.class) ExchangePointDTO exchangePointDTO,
                                    BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            log.info("参数错误：{}", bindingResult.getFieldError().getDefaultMessage());
            return new ResponseVO<>(CommonResponseEnum.FAILURE, bindingResult.getFieldError().getDefaultMessage());
        }

        return memberPointService.pointListInfo(exchangePointDTO);
    }

}
