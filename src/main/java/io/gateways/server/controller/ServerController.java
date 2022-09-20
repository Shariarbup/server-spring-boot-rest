package io.gateways.server.controller;

import io.gateways.server.dto.ServerDto;
import io.gateways.server.enumeration.Status;
import io.gateways.server.model.Server;
import io.gateways.server.service.ReportService;
import io.gateways.server.service.ServerService;
import io.gateways.server.utils.Response;
import net.sf.jasperreports.engine.JRException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.Map.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@CrossOrigin(value = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/servers")
public class ServerController {
    @Autowired
    private ServerService serverService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/")
    public ResponseEntity<Response> getAllServersList() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
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
		return ResponseEntity.ok().body(reportService.exportReport(format));
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
