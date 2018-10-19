package com.fuzamei.bonuspoint.controller.point;

import com.fuzamei.bonuspoint.dao.common.mapper.PlatformInfoMapper;
import com.fuzamei.bonuspoint.entity.Token;
import com.fuzamei.bonuspoint.entity.dto.point.ExchangePointDTO;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.service.impl.point.MemberPointServiceImplDan;
import com.fuzamei.bonuspoint.service.point.MemberPointService;
import com.fuzamei.bonuspoint.service.user.UserService;
import com.fuzamei.bonuspoint.util.MD5HashUtil;
import com.fuzamei.bonuspoint.util.StringUtil;
import com.fuzamei.bonuspoint.validation.group.Point;
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
public class MemberPointManageController {

    @Value("${page.pageSize}")
    private Integer size;

    @Value("${page.maxSize}")
    private Integer maxSize;

    @Value("${md5.salt}")
    private String salt;



    private final UserService userService;

    private final MemberPointService memberPointService;
    private final MemberPointServiceImplDan memberPointServiceImplDan;
    private final PlatformInfoMapper platformInfoMapper;
    @Autowired
    public MemberPointManageController(UserService userService, MemberPointService memberPointService,
                                       MemberPointServiceImplDan memberPointServiceImplDan,PlatformInfoMapper platformInfoMapper ) {

        this.userService = userService;
        this.memberPointService = memberPointService;
        this.memberPointServiceImplDan = memberPointServiceImplDan;
        this.platformInfoMapper = platformInfoMapper;
    }

    /**
     * 积分转账
     *
     * @param
     *                 {
     *                 groupId： 所要转账积分的集团id（非必要）
     *                 exNum: 兑换数量
     *                 payword: 交易密码(非必需)
     *                 opPubKey: 对方公钥
     *                 pointType: 积分类型（1>集团商户积分，2>通用积分)
     *
     *                 memo:备注
     *                 }
     *                 lkm
     * @return 响应
     */
    @PostMapping("transfer")
    public ResponseVO memberTranPoint(@RequestAttribute("token") Token token, @RequestBody @Validated(Point.PointTranfer.class) ExchangePointDTO exchangeDTO,
                                      BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            log.info("参数错误：{}", bindingResult.getFieldError().getDefaultMessage());
            return new ResponseVO<>(CommonResponseEnum.FAILURE, bindingResult.getFieldError().getDefaultMessage());
        }

        //将密码加密以进行比较
        try {
            if(StringUtil.isNotBlank(exchangeDTO.getPayWord())){
                exchangeDTO.setPayWordHash(MD5HashUtil.md5SaltEncrypt(exchangeDTO.getPayWord(), salt));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        exchangeDTO.setUid(token.getUid());

        //平台的uid
        Long pid = token.getPId();

        exchangeDTO.setPlatformUid(pid);

        return memberPointServiceImplDan.memberTranPoint(exchangeDTO);

    }

    /**
     * 兑换平台积分
     * lkm
     *
     * @param
     *                 {
     *                 groupId: 所要兑换的集团积分所属集团id
     *                 exNum: 兑换数量
     *                 payword: 交易密码
     *
     *                 }
     * @return 响应
     */
    @PostMapping("exchange")
    public ResponseVO memberExchange(@RequestAttribute("token") Token token, @RequestBody @Validated(Point.PointExchange.class) ExchangePointDTO exchangeDTO,
                                     BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            log.info("参数错误：{}", bindingResult.getFieldError().getDefaultMessage());
            return new ResponseVO<>(CommonResponseEnum.FAILURE, bindingResult.getFieldError().getDefaultMessage());
        }

        //操作者id
        exchangeDTO.setUid(token.getUid());
        try {
            exchangeDTO.setPayWordHash(MD5HashUtil.md5SaltEncrypt(exchangeDTO.getPayWord(), salt));
        } catch (Exception e) {
            e.printStackTrace();
        }

        exchangeDTO.setUid(token.getUid());

        //平台的uid
        Long pid = token.getPId();

        exchangeDTO.setPlatformUid(pid);
        return memberPointServiceImplDan.exchange(exchangeDTO);
    }

}

