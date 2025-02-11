package com.github.paicoding.forum.core.async;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * 异步执行切面（简化版）
 *
 * @author XuYifei
 * @date 2024-07-12
 */
@Slf4j
@Aspect
@Component
public class AsyncExecuteAspect {

    /**
     * 异步执行的切面
     *
     * @param joinPoint
     * @param asyncExecute
     * @return
     * @throws Throwable
     */
    @Around("@annotation(asyncExecute)")
    public Object handle(ProceedingJoinPoint joinPoint, AsyncExecute asyncExecute) throws Throwable {
        if (!asyncExecute.value()) {
            // 如果不支持异步执行，直接执行方法
            return joinPoint.proceed();
        }

        try {
            // 执行异步操作
            return AsyncUtil.submit(() -> {
                try {
                    return joinPoint.proceed();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            // 超时或执行异常时，返回默认的超时响应
            if (asyncExecute.timeOutRsp() != null && !asyncExecute.timeOutRsp().isEmpty()) {
                return asyncExecute.timeOutRsp();
            } else {
                throw e;
            }
        }
    }
}
