package com.agutierrezl.easy_enrollment_api.security;

import java.util.Date;

public record AuthResponse (
        String token,
        Date expiration
) {
}
