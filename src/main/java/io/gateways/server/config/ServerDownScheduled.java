package io.gateways.server.config;

import io.gateways.server.enumeration.Status;
import io.gateways.server.model.Server;
import io.gateways.server.repo.ServerRepository;
import io.gateways.server.service.EmailService;
import io.gateways.server.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class ServerDownScheduled {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ServerRepository serverRepository;

    @Autowired
    private EmailService emailService;

//    @Scheduled(fixedDelayString = "PT30H")
    public void schedular() throws InterruptedException, JRException, FileNotFoundException, MessagingException {
        List<Server> servers = serverRepository.findByStatus(Status.SERVER_DOWN);
        String reportMessage = reportService.exportReport("pdf", servers);
        emailService.sendEmailWithAttachment("al.shariar@bjitgroup.com",
                "In the attachment section you can find all down server list.",
                "All Down server List",
                "D:\\All programming TEXT NOTE OF MINE\\Muntakim vai crud\\report\\servers.pdf");
    }
//    @Scheduled(cron = "${cron.expression.value}")
//    @Scheduled(fixedDelayString = "PT05S")
    public void scheduledForNonPaidUser() throws InterruptedException, JRException, FileNotFoundException, MessagingException {
        List<Server> servers = serverRepository.findByBillContainsIgnoreCase("not paid");
        List<String> email = new ArrayList<>();
        servers.forEach((server)->{
            if(server.getBill().equals("non-paid")){
                email.add(server.getUser().getEmail());
            }
        });
        String[] bcc = {};
        for (int i = 0; i < email.size(); i++) {
            bcc[i] = email.get(i);
        }
        String[] newbcc = {"al.shariar@bjitgroup.com","jewel.chowdhury@bjitgroup.com"};
        emailService.sendSimpleEmail(
                newbcc,
                "Your internet bill is not paid yet, please pay before 10th of this month.",
                "Internet Bill"
        );
    }
}
