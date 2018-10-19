package com.fuzamei.bonuspoint.aop;

import com.fuzamei.bonuspoint.aop.annotation.LogAnnotation;
import com.fuzamei.bonuspoint.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @program: bonus-point-cloud
 * @description: 日志切面
 * @author: WangJie
 * @create: 2018-09-05 15:21
 **/
@Aspect
@Slf4j
@Component
public class LogAspect {

    @Around("@annotation(logAnnotation)")
    public Object doAround(ProceedingJoinPoint point,LogAnnotation logAnnotation) throws Throwable {
        log.info("进入{}接口",logAnnotation.note());
        log.info("处理方法为：{}.{}",point.getSignature().getDeclaringTypeName(),point.getSignature().getName());
        long startTime = System.currentTimeMillis();
        log.info("入参：{}",point.getArgs());
        //执行，获取结果
        Object returnValue = point.proceed(point.getArgs());

        log.info("执行返回结果为：{}",returnValue);
        long endTime = System.currentTimeMillis();
        log.info("{}接口执行用时：{}ms",logAnnotation.note(),endTime-startTime);
        return returnValue;
    }
}
