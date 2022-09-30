package io.gateways.server.utils;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthRequest {
    String username;
    String password;
}
