package com.fuzamei.bonuspoint.controller.safe;

import com.fuzamei.bonuspoint.aop.annotation.LogAnnotation;
import com.fuzamei.bonuspoint.entity.Token;
import com.fuzamei.bonuspoint.entity.dto.user.UserDTO;
import com.fuzamei.common.model.vo.ResponseVO;
import com.fuzamei.bonuspoint.service.safe.SafeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-04-18 17:31
 **/
@Slf4j
@RestController
@RequestMapping("/bonus-point/safe")
public class SafeQueryController {


    private final SafeService safeService;

    @Autowired
    public SafeQueryController( SafeService safeService) {
        this.safeService = safeService;
    }

    /**
     *
     * 获取安全状态信息
     * @return
     */
    @LogAnnotation(note = "获取安全状态信息")
    @GetMapping("/index")
    public ResponseVO<UserDTO> getSafeStatusInfo(@RequestAttribute("token") Token token){
        log.info("获取安全状态信息开始");

        return safeService.getSafeStatusInfo(token.getUid());
    }

}
