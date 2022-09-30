package io.gateways.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    public void sendSimpleEmail(String[] bcc,
                                String body,
                                String subject){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("itmasjoy@gmail.com");
//        message.setTo(toEmail);
        message.setBcc(String.valueOf(bcc));
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);
        System.out.println("Mail Send...");
    }

    public void sendEmailWithAttachment(String toEmail,
                                        String body,
                                        String subject,
                                        String attachment) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage(); // eitar maddhome amra mail e attachment add korte parbo

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setFrom("itmasjoy@gmail.com");
        mimeMessageHelper.setText(body);
        mimeMessageHelper.setTo(toEmail);
        mimeMessageHelper.setSubject(subject);

        FileSystemResource  fileSystemResource = new FileSystemResource(new File(attachment));

        mimeMessageHelper.addAttachment(fileSystemResource.getFilename(), fileSystemResource);
        // ekhane amra file not exception o handle korte parbo, eshara amra bivinno exception handle korte parbo ekhane

        mailSender.send(mimeMessage);
        System.out.println("Mail send with attachment...");
    }
}

// ekhane amra cc option and bcc option add korte pari
// ekhane amra multiple attchment option add korte pari
// attchment er size amra add korte pari
