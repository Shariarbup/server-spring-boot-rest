package io.gateways.server.model;

import io.gateways.server.enumeration.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Server {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    @NotEmpty(message = "IP address can not be empty or null")
    private String ipAddress;
    private String name;
    private String memory;
    private String type;
    private String imageUrl;
    private Status status;
    private String bill;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private User user;
}
