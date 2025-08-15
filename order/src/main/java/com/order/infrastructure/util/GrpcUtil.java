package com.order.infrastructure.util;

import com.order.infrastructure.exception.CustomAuthenticationException;
import com.order.infrastructure.exception.CustomAuthorizationException;
import com.order.infrastructure.exception.CustomBusinessException;
import com.order.infrastructure.exception.CustomNotFoundException;
import com.order.infrastructure.exception.CustomServerException;
import io.grpc.Status;

public class GrpcUtil {

    public static Status getStatus(Exception e) {
        if (e instanceof CustomAuthorizationException) {
            return Status.UNAUTHENTICATED
                .withDescription(((CustomAuthorizationException) e).getErrorCode().getMessage());
        }
        if (e instanceof CustomAuthenticationException) {
            return Status.PERMISSION_DENIED
                .withDescription(((CustomAuthenticationException) e).getErrorCode().getMessage());
        }
        if (e instanceof CustomBusinessException) {
            return Status.FAILED_PRECONDITION
                .withDescription(((CustomBusinessException) e).getErrorCode().getMessage());
        }
        if (e instanceof CustomNotFoundException) {
            return Status.NOT_FOUND
                .withDescription(((CustomNotFoundException) e).getErrorCode().getMessage());
        }
        if (e instanceof CustomServerException) {
            return Status.INTERNAL
                .withDescription(((CustomServerException) e).getErrorCode().getMessage());
        }
        return Status.INTERNAL
            .withDescription(e.getMessage());
    }
}
