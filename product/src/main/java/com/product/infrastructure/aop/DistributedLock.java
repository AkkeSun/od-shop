package com.product.infrastructure.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    String key();

    /**
     * true 인 경우 입력 파라미터를 기반으로 유니크한 키를 생성합니다. example. API 중복 호출 방지
     */
    boolean isUniqueKey() default false;

    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 10초 동안 락을 획득하기 위해 시도합니다. 다른 프로세스가 이미 락을 가지고 있다면 10초동안 기다립니다. 10초가 지나도 획득하지 못한다면 false 를 리턴합니다
     */
    long waitTime() default 10L;

    /**
     * 락을 5초간 유지합니다. 5초가 지난 후에는 자동으로 락을 해제합니다.
     */
    long leaseTime() default 5L;
}