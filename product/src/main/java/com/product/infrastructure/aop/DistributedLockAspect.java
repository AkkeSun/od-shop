package com.product.infrastructure.aop;

import com.product.infrastructure.exception.CustomServerException;
import com.product.infrastructure.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@RequiredArgsConstructor
public class DistributedLockAspect {

    private static final String LOCK_PREFIX = "LOCK:";
    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;

    @Around("@annotation(distributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint,
        DistributedLock distributedLock) throws Throwable {

        String key = distributedLock.isUniqueKey() ? LOCK_PREFIX + distributedLock.key()
            + joinPoint.getArgs()[0] : LOCK_PREFIX + distributedLock.key();
        RLock rLock = redissonClient.getLock(key);

        try {
            boolean available = rLock.tryLock(
                distributedLock.waitTime(),
                distributedLock.leaseTime(),
                distributedLock.timeUnit());

            if (!available) {
                return false;
            }
            return aopForTransaction.proceed(joinPoint);
        } catch (Exception e) {
            log.error("[DistributedLockAspect] {}", e.getMessage());
            throw new CustomServerException(ErrorCode.SERVER_DISTRIBUTE_LOCK);
        } finally {
            if (rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }
    }
}
