package com.order.infrastructure.util;

import io.jsonwebtoken.Claims;

public interface JwtUtil {

    boolean validateTokenExceptExpiration(String token);

    Claims getClaims(String token);
}
