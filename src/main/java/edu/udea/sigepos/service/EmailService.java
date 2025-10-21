package edu.udea.sigepos.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

@Service
public class EmailService {

    @Value("${sendgrid.api.key}")
    private String sendgridApiKey;

    @Value("${sendgrid.from.email}")
    private String fromEmail;

    public void enviarCorreoConAdjunto(String toEmail, String subject, String body, File attachment) throws IOException {
        Email from = new Email(fromEmail);
        Email to = new Email(toEmail);
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(from, subject, to, content);

        if (attachment != null) {
            Attachments attach = new Attachments();
            byte[] fileContent = Files.readAllBytes(attachment.toPath());
            String encoded = Base64.getEncoder().encodeToString(fileContent);
            attach.setContent(encoded);
            attach.setType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            attach.setFilename(attachment.getName());
            attach.setDisposition("attachment");
            mail.addAttachments(attach);
        }

        SendGrid sg = new SendGrid(sendgridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println("Status Code: " + response.getStatusCode());
        } catch (IOException ex) {
            throw ex;
        }
    }
}
