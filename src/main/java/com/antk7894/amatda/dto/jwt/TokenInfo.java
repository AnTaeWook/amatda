package com.antk7894.amatda.dto.jwt;

public record TokenInfo (
        String grantType,
        String accessToken
) {}
