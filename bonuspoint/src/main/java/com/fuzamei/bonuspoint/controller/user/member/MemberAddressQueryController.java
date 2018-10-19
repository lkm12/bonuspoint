package com.fuzamei.bonuspoint.controller.user.member;

import com.fuzamei.bonuspoint.aop.annotation.LogAnnotation;
import com.fuzamei.bonuspoint.entity.Token;
import com.fuzamei.bonuspoint.entity.po.account.AccountPO;
import com.fuzamei.bonuspoint.enums.AddressResponseEnum;
import com.fuzamei.bonuspoint.service.account.AccountService;
import com.fuzamei.common.model.dto.PageDTO;
import com.fuzamei.common.model.vo.ResponseVO;
import com.fuzamei.bonuspoint.service.user.UserAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @program: bonus-point-cloud
 * @description: 用户收货地址查询controller
 * @author: WangJie
 * @create: 2018-04-20 16:55
 **/
@Slf4j
@RestController
@RequestMapping("/bonus-point/member/address")
public class MemberAddressQueryController {

    private final UserAddressService userAddressService;

    private final AccountService accountService;

    @Autowired
    public MemberAddressQueryController(UserAddressService userAddressService, AccountService accountService) {
        this.userAddressService = userAddressService;
        this.accountService = accountService;
    }

    /**
     * 查看收货地址
     * @param token
     * @param pageDTO {
     *              pageSize
     *              page
     *
     * @return
     */
    @LogAnnotation(note = "查看收货地址列表")
    @PostMapping("/list")
    public ResponseVO getUserAddressList(@RequestAttribute("token") Token token, @RequestBody PageDTO pageDTO) {
        return userAddressService.getUserAddressList(token.getUid(),pageDTO);

    }

    /**
     * 获取默认收货地址
     * @param token
     * @return
     */
    @LogAnnotation(note = "获取用户默认收货地址")
    @GetMapping("/get-default-address")
    public ResponseVO getDefaultAddress(@RequestAttribute("token") Token token){
        AccountPO accountPO = accountService.getUserById(token.getUid());
        if (accountPO.getDefaultAddress()== null){
            return  new ResponseVO(AddressResponseEnum.NO_DEFAULT_ADDRESS_YET);
        }
        return userAddressService.getDefaultAddress(accountPO.getDefaultAddress());
    }

}
