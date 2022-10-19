package io.gateways.server;

import io.gateways.server.enumeration.Status;
import io.gateways.server.model.Role;
import io.gateways.server.model.Server;
import io.gateways.server.repo.RoleRepository;
import io.gateways.server.repo.ServerRepository;
import io.gateways.server.service.EmailService;
import io.gateways.server.utils.AppConstants;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class ServerApplication implements CommandLineRunner{
    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ServerRepository serverRepository;
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
    @EventListener(ApplicationReadyEvent.class)
    public void triggerEmail() throws MessagingException {
//        emailService.sendSimpleEmail("al.shariar@bjitgroup.com",
//                "This is Email body....",
//                "Email subject");
//        emailService.sendEmailWithAttachment("al.shariar@bjitgroup.com",
//                "This is Email body....",
//                "Email subject",
//                "D:\\All programming TEXT NOTE OF MINE\\Muntakim vai crud\\report\\servers.pdf");
    }

//    @Bean
//    CommandLineRunner run(ServerRepository serverRepository){
//        return args -> {
//            serverRepository.save(new Server(null, "192.168.1.160","Ubuntu Linux", "16 GB",
//                    "Personal PC", "http://localhost:8080/server/image/server1.png", Status.SERVER_UP));
//            serverRepository.save(new Server(null, "192.168.1.58","Fedora Linux", "16 GB",
//                    "Dell tower", "http://localhost:8080/server/image/server2.png", Status.SERVER_DOWN));
//            serverRepository.save(new Server(null, "192.168.1.21","MS 2008", "64 GB",
//                    "Web Srver", "http://localhost:8080/server/image/server3.png", Status.SERVER_UP));
//            serverRepository.save(new Server(null, "192.168.1.14","Red hat enterprise linux", "64 GB",
//                    "Mail Srver", "http://localhost:8080/server/image/server4.png", Status.SERVER_DOWN));
//            serverRepository.save(new Server(null, "192.168.55.215","BJIT", "64 GB",
//                    "Personal PC", "http://localhost:8080/server/image/server4.png", Status.SERVER_DOWN));
//        };
//    }
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

//    @Bean
//    public CorsFilter corsFilter(){
//        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.setAllowCredentials(true);
//        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:4200"));
//        corsConfiguration.setAllowedHeaders(Arrays.asList("Origin","Access-Control-Allow-Origin",
//                "Content-Type","Accept", "Jwt-Token","Authorization","Origin","Accept","X-Requested-With",
//                "Access-Control-Request-Method","Access-Control-Request-Headers"));
//        corsConfiguration.setExposedHeaders(Arrays.asList("Origin","Content-Type","Accept","Jwt-Token",
//                "Authorization","Access-Control-Allow-Origin","Access-Control-Allow-Credentials","Filename"));
//        corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
//        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
//        return new CorsFilter(urlBasedCorsConfigurationSource);
//    }

    @Bean
    public TaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
//        taskScheduler.setPoolSize(5);
        taskScheduler.setThreadNamePrefix("ThreadPoolTaskSchedular");
        return taskScheduler;
    }

    @Override
    public void run(String... args) throws Exception {
//        try{
//            List<Server> servers = serverRepository.findByBill("not paid");
//            System.out.println("servers"+servers);
//            List<String> email = new ArrayList<>();
//
//            servers.forEach((server)->{
//                if(server.getBill().equals("non-paid")){
//                    email.add(server.getUser().getEmail());
//                }
//            });
//            System.out.println(email);
//            String[] bcc = {};
//            for (int i = 0; i < email.size(); i++) {
//                bcc[i] = email.get(i);
//            }
//
//        }catch(Exception e){
//            e.printStackTrace();
//        }
        System.out.println(this.passwordEncoder.encode("123456"));
        try{
            Role role1 = new Role();
            role1.setId(AppConstants.ROLE_ADMIN);
            role1.setName("ROLE_ADMIN");

            Role role2 = new Role();
            role2.setId(AppConstants.ROLE_USER);
            role2.setName("ROLE_USER");
            List<Role> roles = List.of(role1, role2);
            List<Role> resultRoles = this.roleRepository.saveAll(roles);
            resultRoles.forEach((role)->{
                System.out.println(role.getName());
            });
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
