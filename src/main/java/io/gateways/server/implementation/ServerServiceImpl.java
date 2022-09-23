package io.gateways.server.implementation;

import io.gateways.server.dto.ServerDto;
import io.gateways.server.enumeration.Status;
import io.gateways.server.exception.ResourceNotfoundException;
import io.gateways.server.model.Server;
import io.gateways.server.repo.ServerRepository;
import io.gateways.server.service.ServerService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class ServerServiceImpl implements ServerService {

    @Autowired
    private ServerRepository serverRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public ServerDto createServer(ServerDto serverDto) {

        Server server = new Server();
//                this.modelMapper.map(serverDto, Server.class);
        BeanUtils.copyProperties(serverDto, server);
        log.info("Saving server: {}", server.getName());
        server.setImageUrl(SetServerImageUrl());
        Server newServer = serverRepository.save(server);
        ServerDto serverDto1 = new ServerDto();
        BeanUtils.copyProperties(newServer, serverDto1);
        return serverDto1;
    }


    @Override
    public ServerDto pingServer(String ipAddress) throws IOException, UnknownHostException {
        log.info("Pinging server IP: {}", ipAddress);
        Server server = serverRepository.findByIpAddress(ipAddress);
        InetAddress address = InetAddress.getByName(ipAddress);
        server.setStatus(address.isReachable(100000) ? Status.SERVER_UP : Status.SERVER_DOWN);
        serverRepository.save(server);
        return this.modelMapper.map(server, ServerDto.class);
    }

    @Override
    public Collection<ServerDto> getServerListByRequiredLimit(int limit) {
        log.info("Fetching all servers");
        Collection<Server> servers = serverRepository.findAll(PageRequest.of(0, limit)).toList();
        Collection<ServerDto> serverDtos = servers.stream().map((server)-> this.modelMapper.map(server, ServerDto.class)).collect(Collectors.toList());
        return serverDtos;
    }

    @Override
    public ServerDto getServerById(Long id) {
        log.info("Fetching server by id {}:", id);
        Server server = serverRepository.findById(id).orElseThrow(()-> new ResourceNotfoundException("Server", "server id", id));
        return this.modelMapper.map(server, ServerDto.class);
    }

    @Override
    public ServerDto updateServer(ServerDto serverDto) {
        Server server = this.modelMapper.map(serverDto, Server.class);
        log.info("Updating server: {}", server.getName());
        Server newServer = serverRepository.save(server);
        return this.modelMapper.map(newServer, ServerDto.class);
    }

    @Override
    public Boolean deleteServer(Long id) {
        log.info("Deleting server by ID: {}", id);
        Server server = serverRepository.findById(id).orElseThrow(()-> new ResourceNotfoundException("Server", "server id", id));
        serverRepository.delete(server);
        return Boolean.TRUE;
    }

    private String SetServerImageUrl() {
        String[] imageNames = {"server1.png", "server2.png", "server3.png", "server4.png"};
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/server/image/"+ imageNames[new Random().nextInt(4)]).toUriString();
    }

}
