package com.fuzamei.bonuspoint.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-04-20 14:09
 **/
@Slf4j
@RestController
@RequestMapping("/bonus-point")
public class UserManageController {

    @Autowired
    public UserManageController() {

    }

}
