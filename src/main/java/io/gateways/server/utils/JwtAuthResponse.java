package io.gateways.server.utils;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthResponse {
//    private static final long serialVersionUID = 1L;
    private String token;
    private String username;
}
