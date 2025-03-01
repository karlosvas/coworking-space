package com.grupo05.coworking_space.service;

import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Servicio para el envío de correos electrónicos mediante SMTP.
 * Utiliza autenticación y configuración de servidor SMTP para enviar correos electrónicos.
 */
@Service
public class EmailSender {


    private String username;
    private String password;
    private static final Logger logger = LoggerFactory.getLogger(EmailSender.class);

    public EmailSender(@Value("${email.username}") String username,
                       @Value("${email.password}") String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Envía un correo electrónico a la dirección especificada.
     *
     * @param to      Dirección de correo electrónico del destinatario.
     * @param subject Asunto del correo.
     * @param body    Cuerpo del mensaje.
     */
    public void sendEmail(String to, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,new Authenticator(){
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try{
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            logger.info("Correo enviado correctamente a {}", to);
        }catch(MessagingException e){
            logger.error("Error al enviar el correo",e);
        }
    }
}
