package io.gateways.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServerDto {
    private Long id;
    @NotEmpty(message = "IP address should not be blank.")
    private String ipAddress;
    @NotEmpty(message = "Name should not be blank.")
    private String name;
    @NotEmpty(message = "Memory should not be blank.")
    private String memory;
    @NotEmpty(message = "Type should not be blank.")
    private String type;
    private String imageUrl;
//    @NotEmpty(message = "Status should not be blank.")
    private String status;

    private UserDto user;

    public ServerDto(String ipAddress, String name, String memory, String type, String imageUrl, String status) {
        this.ipAddress = ipAddress;
        this.name = name;
        this.memory = memory;
        this.type = type;
        this.imageUrl = imageUrl;
        this.status = status;
    }
}
