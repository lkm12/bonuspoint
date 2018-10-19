package com.fuzamei.bonuspoint.controller.point;

import com.fuzamei.bonuspoint.entity.Token;
import com.fuzamei.bonuspoint.entity.dto.point.ApplyPointDTO;
import com.fuzamei.bonuspoint.entity.dto.point.CompanyPointDTO;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.enums.PointResponseEnum;
import com.fuzamei.bonuspoint.service.account.AccountService;
import com.fuzamei.bonuspoint.service.point.CompanyPointService;
import com.fuzamei.bonuspoint.util.MD5HashUtil;
import com.fuzamei.bonuspoint.util.StringUtil;
import com.fuzamei.common.model.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/5/7 10:33
 */
@Slf4j
@RestController
@RequestMapping("/bonus-point/point/company")
public class CompanyPointManageController {

    @Value("${md5.salt}")
    private String MD5Salt;
    private final CompanyPointService companyPointService;
    private final AccountService accountService ;

    @Autowired
    public CompanyPointManageController(AccountService accountService ,CompanyPointService companyPointService) {
        this.companyPointService = companyPointService;
        this.accountService = accountService;
    }

    /**
     * @param token           令牌 (required = true)
     * @param companyPointDTO 请求参数
     *                        {
     *                        toid：对方用户id(required = true)
     *                        pointId: 积分id(required = true)
     *                        payword: 交易密码 (required = true)
     *                        num: 积分数量 (required = true)
     *                        memo：备注 (required = false)
     *                        }
     * @return 请求响应
     * @author qbanxiaoli
     * @description 集团发放积分接口
     */
    @PostMapping("/send")
    public ResponseVO sendPointToUser(@RequestAttribute("token") Token token,
                                      @RequestBody CompanyPointDTO companyPointDTO) {
        log.info("集团发放积分");
        companyPointDTO.setUid(token.getUid());
        // 参数处理判断
        if (StringUtil.isBlank(companyPointDTO.getPayword())) {
            log.info("用户交易密码不能为空");
            return new ResponseVO(PointResponseEnum.USER_PASSWORD_NULL);
        }
        if (companyPointDTO.getNum() == null || companyPointDTO.getNum().compareTo(BigDecimal.ZERO) <= 0) {
            log.info("积分数量不能为空");
            return new ResponseVO(PointResponseEnum.POINT_NUM_NULL);
        }
        if (companyPointDTO.getPointId() == null) {
            log.info("积分id不能为空");
            return new ResponseVO(PointResponseEnum.POINT_ID_NULL);
        }
        if (companyPointDTO.getToid() == null) {
            log.info("对方用户id不能为空");
            return new ResponseVO(PointResponseEnum.USER_ACCOUNT_NULL);
        }

        //对交易密码使用md5加密
        String md5Hash;
        try {
            md5Hash = MD5HashUtil.md5SaltEncrypt(companyPointDTO.getPayword(), MD5Salt);
        } catch (Exception e) {
            log.info("交易密码加密失败");
            return new ResponseVO(CommonResponseEnum.PAYWORD_WRONG);
        }
        //根据集团管理用户uid和交易密码哈希判断是否存在
        boolean isPayWordRight = accountService.checkPayword(companyPointDTO.getPayword(),token.getUid());
        if (!isPayWordRight ) {
            log.info("交易密码错误");
            return new ResponseVO(PointResponseEnum.COMPANY_OR_PAYWORD_ERROR);
        }

        return companyPointService.sendPointToUser(companyPointDTO);
    }

    /**
     * @param token           令牌 (required = true)
     * @param companyPointDTO 请求参数
     *                        {
     *                        payword: 交易密码 (required = true)
     *                        num: 积分数量 (required = true)
     *                        memo：备注 (required = false)
     *                        }
     * @return 请求响应
     * @author qbanxiaoli
     * @description 结算通用积分接口
     */
    @PostMapping("/balance")
    public ResponseVO balanceCommonPoint(@RequestAttribute("token") Token token,
                                         @RequestBody CompanyPointDTO companyPointDTO) {
        log.info("结算通用积分");
        companyPointDTO.setUid(token.getUid());
        // 参数处理判断
        if (StringUtil.isBlank(companyPointDTO.getPayword())) {
            log.info("用户交易密码不能为空");
            return new ResponseVO(PointResponseEnum.USER_PASSWORD_NULL);
        }
        if (companyPointDTO.getNum() == null) {
            log.info("积分数量不能为空");
            return new ResponseVO(PointResponseEnum.POINT_NUM_NULL);
        }
        // 请求服务
        log.info("参数校验正常");
        return companyPointService.balanceCommonPoint(companyPointDTO);
    }

    /**
     * 集团申请发行积分
     *
     * @param token         token
     * @param applyPointDTO 积分信息
     * @return
     */
    @PostMapping("/apply")
    public ResponseVO releasePoint(@RequestAttribute("token") Token token, @RequestBody ApplyPointDTO applyPointDTO) {
        log.info("申请发行积分");

        return companyPointService.releasePoint(token.getUid(), applyPointDTO);
    }

}
