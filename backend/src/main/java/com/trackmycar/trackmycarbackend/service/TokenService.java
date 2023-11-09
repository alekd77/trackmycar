package com.trackmycar.trackmycarbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class TokenService {
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    @Autowired
    public TokenService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public String generateJwt(Authentication auth) {
        Instant now = Instant.now();

        String scope = auth
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet
                .builder()
                .issuer("self")
                .issuedAt(now)
                .subject(auth.getName())
                .claim("roles", scope)
                .build();

        return jwtEncoder
                .encode(JwtEncoderParameters.from(claims))
                .getTokenValue();
    }

    public String getUsernameFromToken(String token) {
        if(!token.startsWith("Bearer")) {
            throw new InvalidBearerTokenException("Token is not a Bearer token");
        }

        String strippedToken = token.substring(7);
        Jwt decoded = jwtDecoder.decode(strippedToken);

        return decoded.getSubject();
    }
}
