package io.gateways.server.service;

import io.gateways.server.dto.ServerDto;
import io.gateways.server.model.Server;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Collection;

public interface ServerService {
    ServerDto createServer(ServerDto server);
    ServerDto pingServer(String ipAddress) throws IOException;
    Collection<ServerDto> getServerListByRequiredLimit(int limit);
    ServerDto getServerById(Long id);
    ServerDto updateServer(ServerDto server);
    Boolean deleteServer(Long id);
}
