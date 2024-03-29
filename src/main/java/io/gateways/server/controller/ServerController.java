package io.gateways.server.controller;

import io.gateways.server.dto.ServerDto;
import io.gateways.server.enumeration.Status;
import io.gateways.server.model.Server;
import io.gateways.server.repo.ServerRepository;
import io.gateways.server.service.ReportService;
import io.gateways.server.service.ServerService;
import io.gateways.server.utils.Response;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.Map.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

//@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST},maxAge = 3600)
//@CrossOrigin(origins = {"http://localhost:4200"}, methods = {RequestMethod.GET, RequestMethod.POST})
@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/servers")
@Slf4j
public class ServerController {
    @Autowired
    private ServerService serverService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ServerRepository serverRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @GetMapping("/")
    public ResponseEntity<Response> getAllServersList() throws InterruptedException {
//        TimeUnit.SECONDS.sleep(2);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(of("servers", serverService.getServerListByRequiredLimit(30)))
                        .message("Server retrieved")
                        .status(OK)
                        .statusCode(OK.value()).build()
        );
    }

    @GetMapping("/ping/{ipAddress}")
    public ResponseEntity<Response> pingServer(@PathVariable("ipAddress") String ipAddress) throws IOException {
        ServerDto serverDto = serverService.pingServer(ipAddress);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(of("server", serverDto))
                        .message(serverDto.getStatus().equals("SERVER_UP")  ? "Ping success" : "Ping failed")
                        .status(OK)
                        .statusCode(OK.value()).build()
        );
    }

    @GetMapping("/report/{format}")
	public ResponseEntity<String> generateReport(@PathVariable String format) throws JRException, FileNotFoundException {
        List<Server> servers = serverRepository.findAll();
		return ResponseEntity.ok().body(reportService.exportReport(format, servers));
	}

    @PostMapping("/")
    public ResponseEntity<Response> saveServer( @Valid @RequestBody ServerDto serverDto){
        ServerDto saveServer = serverService.createServer(serverDto);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(of("server", saveServer))
                        .message("Server created")
                        .status(CREATED)
                        .statusCode(CREATED.value()).build()
        );
    }

    @PutMapping ("/{id}")
    public ResponseEntity<Response> updateServer( @Valid @RequestBody ServerDto serverDto, @PathVariable(value = "id") Long id){
        ServerDto getServerById = serverService.getServerById(id);
        getServerById.setIpAddress(serverDto.getIpAddress());
        getServerById.setName(serverDto.getName());
        getServerById.setMemory(serverDto.getMemory());
        getServerById.setType(serverDto.getType());
        getServerById.setStatus(serverDto.getStatus());
        getServerById.setUser(serverDto.getUser());
        ServerDto updateServer = serverService.updateServer(getServerById);
        log.info("Updated server", updateServer);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(of("server", updateServer))
                        .message("Server updated")
                        .status(OK)
                        .statusCode(OK.value()).build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getServer(@PathVariable("id") Long id){
        ServerDto getServerById = serverService.getServerById(id);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(of("server", getServerById))
                        .message("Server Retrieved")
                        .status(OK)
                        .statusCode(OK.value()).build()
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteServer(@PathVariable("id") Long id){
        Boolean deleteServer = serverService.deleteServer(id);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(of("server", deleteServer))
                        .message("Server Deleted")
                        .status(OK)
                        .statusCode(OK.value()).build()
        );
    }


    @GetMapping(path = "/image/{fileName}", produces = IMAGE_PNG_VALUE)
    public byte[] getServerImage(@PathVariable("fileName") String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(System.getProperty("user.home")+"/Downloads/images/"+fileName));
    }
}
