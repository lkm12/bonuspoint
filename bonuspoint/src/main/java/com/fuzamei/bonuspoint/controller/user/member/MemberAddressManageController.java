package com.fuzamei.bonuspoint.controller.user.member;

import com.fuzamei.bonuspoint.aop.annotation.LogAnnotation;
import com.fuzamei.bonuspoint.entity.Token;
import com.fuzamei.bonuspoint.entity.dto.user.UserAddressDTO;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.enums.SafeResponseEnum;
import com.fuzamei.bonuspoint.enums.UserResponseEnum;
import com.fuzamei.bonuspoint.validation.group.Address;
import com.fuzamei.common.model.vo.ResponseVO;
import com.fuzamei.bonuspoint.service.user.UserAddressService;
import com.fuzamei.bonuspoint.service.location.LocationService;
import com.fuzamei.bonuspoint.util.RegxUtils;
import com.fuzamei.bonuspoint.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @program: bonus-point-cloud
 * @description: 用户收货地址controller
 * @author: WangJie
 * @create: 2018-04-20 18:20
 **/
@Slf4j
@RestController
@RequestMapping("/bonus-point/member/address")
public class MemberAddressManageController {

    private final UserAddressService userAddressService;

    private final LocationService locationService;

    @Autowired
    public MemberAddressManageController(UserAddressService userAddressService, LocationService locationService) {
        this.userAddressService = userAddressService;
        this.locationService = locationService;
    }

    /**
     * 添加收货地址
     * @param userAddressDTO
     *                      {
     *                          receiver              收货人
     *                          mobile                收货人名称
     *                          streetCode            街道编码
     *                          areaDetail            详细地址
     *                      }
     * @return
     */
    @LogAnnotation(note = "添加收货地址")
    @PostMapping("/create")
    public ResponseVO createUserAddress(@RequestAttribute("token") Token token ,
                                        @RequestBody @Validated(Address.CreateAddress.class)  UserAddressDTO userAddressDTO,
                                        BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            log.info("参数错误：{}", bindingResult.getFieldError().getDefaultMessage());
            return new ResponseVO<>(CommonResponseEnum.FAILURE, bindingResult.getFieldError().getDefaultMessage());
        }
        boolean realAddress;
        realAddress = locationService.isRealDistrict(userAddressDTO.getDistrictCode());
        if(!realAddress){
            return new ResponseVO(UserResponseEnum.WRONG_ADDRESS);
        }
        userAddressDTO.setUid(token.getUid());
        userAddressDTO.setCreatedAt(TimeUtil.timestamp());
        return userAddressService.saveUserAddress(userAddressDTO);
    }

    /**
     * 更新收货地址
     * @param userAddressDTO
     *                      {
     *                          id               收货地址id
     *                          receiver         收货人
     *                          mobile           收货人手机号
     *                          streetCode       街道编码
     *                          areaDetail       详细地址
     *                      }
     * @return
     */
    @LogAnnotation(note = "更新收货地址")
    @RequestMapping(value = "/update",method = {RequestMethod.POST , RequestMethod.PUT})
    public ResponseVO updateUserAddress(@RequestAttribute("token") Token token,
                                        @RequestBody @Validated(Address.UpdateAddress.class) UserAddressDTO userAddressDTO,
                                        BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            log.info("参数错误：{}", bindingResult.getFieldError().getDefaultMessage());
            return new ResponseVO<>(CommonResponseEnum.FAILURE, bindingResult.getFieldError().getDefaultMessage());
        }
        boolean realAddress;
        realAddress = locationService.isRealDistrict(userAddressDTO.getDistrictCode());
        if(!realAddress){
            return new ResponseVO(UserResponseEnum.WRONG_ADDRESS);
        }
        userAddressDTO.setUid(token.getUid());
        userAddressDTO.setUpdateAt(TimeUtil.timestamp());
        return userAddressService.updateUserAddress(userAddressDTO);

    }

    /**
     * 删除收货地址
     *
     * @return
     */
    @LogAnnotation(note = "删除收货地址")
    //@DeleteMapping("/delete/{id}")
    @RequestMapping(value = "/delete/{id}",method = {RequestMethod.GET , RequestMethod.DELETE})
    public ResponseVO deleteUserAddress(@RequestAttribute("token") Token token,@PathVariable("id") Long id){
        return userAddressService.deleteUserAddress( id,token.getUid());
    }

    /**
     * 设置默认收货地址
     * @param token
     * @param addressId
     * @return
     */
    @LogAnnotation(note = "设置默认收货地址")
    @GetMapping("/set-default-address/{addressId}")
    public ResponseVO updateDefaultAddress(@RequestAttribute("token") Token token,@PathVariable("addressId") Long addressId){
        return userAddressService.updateDefaultAddress(token.getUid(),addressId);
    }
}
