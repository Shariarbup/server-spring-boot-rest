package io.gateways.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.gateways.server.dto.ServerDto;
import io.gateways.server.enumeration.Status;
import io.gateways.server.model.Server;
import io.gateways.server.service.ReportService;
import io.gateways.server.service.ServerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ServerControllerIntregationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ServerService serverService;

    @MockBean
    private ReportService reportService;


    @Test
    void shouldCreateServer()throws Exception{
        ServerDto server = new ServerDto(null,"192.168.10.689","bjit ww","in memory", "type test","url test","SERVER_UP");
        when(serverService.createServer(server)).thenReturn(server);
        mockMvc.perform(post("/api/v1/servers/", server)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(server)))
//              .andExpect(jsonPath("$.message").value("Server created"))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void shouldReturnListOfServers() throws Exception,InterruptedException {
        List<ServerDto> serverDtos = new ArrayList<>(
                Arrays.asList(new ServerDto(50L,"192.168.10.688","bjit ww","in memory", "type test","url test","SERVER_UP"),
                        new ServerDto(51L,"192.168.10.689","bjit ww","in memory", "type test","url test","SERVER_UP"),
                        new ServerDto(52L,"192.168.10.690","bjit ww","in memory", "type test","url test","SERVER_UP"),
                        new ServerDto(53L,"192.168.10.691","bjit ww","in memory", "type test","url test","SERVER_UP"),
                        new ServerDto(54L,"192.168.10.692","bjit ww","in memory", "type test","url test","SERVER_UP")
                ));

        when(serverService.getServerListByRequiredLimit(5)).thenReturn(serverDtos);
        mockMvc.perform(get("/api/v1/servers/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(serverDtos.size()))
                .andDo(print());
    }
    @Test
    void shouldGetServerById() throws Exception {
        Long id= 10L;
        ServerDto server = new ServerDto(10L,"192.168.10.680","bjit ww","in memory", "type test",null,"SERVER_UP");
        when(serverService.getServerById(id)).thenReturn(server);
        mockMvc.perform(get("/api/v1/servers/{id}",id))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void shouldReturnServer() throws Exception{
        long id = 10L;
        ServerDto server = new ServerDto(10L,"192.168.10.688","bjit ww","in memory", "type test","url test","SERVER_UP");
        Server server1 = new Server(10L,"192.168.10.688","bjit ww","in memory", "type test","url test", Status.SERVER_UP);
        when(serverService.getServerById(id)).thenReturn(server);
        mockMvc.perform(get("/api/v1/servers/{id}",id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.server.id").value(id))
                .andExpect(jsonPath("$.data.server.ipAddress").value(server.getIpAddress()))
                .andExpect(jsonPath("$.data.server.name").value(server.getName()))
                .andExpect(jsonPath("$.data.server.memory").value(server.getMemory()))
                .andExpect(jsonPath("$.data.server.type").value(server.getType()))
                .andExpect(jsonPath("$.data.server.imageUrl").value(server.getImageUrl()))
                .andExpect(jsonPath("$.data.server.status").value(server.getStatus()))
                .andDo(print());

    }
    @Test
    void shouldDeleteServer() throws Exception{
        Long id = 9L;
        when(serverService.deleteServer(id)).thenReturn(Boolean.TRUE);
        mockMvc.perform(delete("/api/v1/servers/{id}",id))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    void shouldGenerateReport() throws Exception{
        String format = "pdf";
        String path= "D:\\All programming TEXT NOTE OF MINE\\Muntakim vai crud\\report";
        when(reportService.exportReport(format)).thenReturn("report generated in this path: "+path);
        mockMvc.perform(get("/api/v1/servers/report/{format}",format))
                .andExpect(status().isOk())
                .andDo(print());

    }
    @Test
    void shouldPingServerTest() throws Exception{
        String ipAddress = "192.168.55.215";
        ServerDto serverDto = new ServerDto(10L,"192.168.55.215","bjit ww","in memory", "type test","url test","SERVER_UP");
        when(serverService.pingServer(ipAddress)).thenReturn(serverDto);
        mockMvc.perform(get("/api/v1/servers/ping/{ipAddress}",ipAddress))
                .andExpect(jsonPath("$.message").value("Ping success"))
                .andExpect(status().isOk())
                .andDo(print());
    }



}