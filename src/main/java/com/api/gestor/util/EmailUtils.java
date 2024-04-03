package com.api.gestor.util;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.List;

@Service
public class EmailUtils {


    @Autowired
    private JavaMailSender javaMailSender;


    public void sendSimpleMessage(String to, String subject, String text, List<String> list){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("dylan1811vallejo@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        if(list != null && !list.isEmpty()){
            message.setCc(getCcArray(list));

        }
        javaMailSender.send(message);

    }


    private String[] getCcArray(List<String> cclist){
        String [] cc = new String[cclist.size()];
        for(int i = 0; i < cclist.size();i++){
            cc[i] = cclist.get(i);
        }
        return cc;
    }

    public void recuperarPasswordViaEmail(String to, String subject, String password) throws MessagingException, jakarta.mail.MessagingException {
        MimeMessage  message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("dylan1811vallejo@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);

        String htmlMessage = " <p><b>Sus detalles de inicio de sesion para el sistema gestor de facturas </b> <br> <b>Email: </b>"
                + to + "<br><b>Password : </b> "
                + password + "</p>";

        message.setContent(htmlMessage, "text/html");
        javaMailSender.send(message);

    }

}
