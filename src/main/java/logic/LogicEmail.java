package logic;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
import helpers.EmailTemplates;
import helpers.EnvHelper;
import java.util.LinkedList;
import entities.Registro;

public class LogicEmail {
	
	private final String username;
    private final String password;

    public LogicEmail() {
        this.username = EnvHelper.get("EMAIL_USER");
        this.password = EnvHelper.get("EMAIL_PASS");
    }
	
    public void enviarEmail(String destinatario, String asunto, String mensajeHtml) {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props,
            new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(destinatario)
            );
            message.setSubject(asunto);
            message.setContent(mensajeHtml, "text/html; charset=UTF-8");

            Transport.send(message);

            System.out.println("✅ Email enviado a " + destinatario);

        } catch (MessagingException e) {
        	e.printStackTrace();
            System.out.println("❌ Error enviando email");
        }
    }

     
    public void notificarNuevaNoticia(String emailUsuario, String tituloNoticia) {
        String asunto = "📰 Nueva noticia en Bestiario";
        String mensajeHtml = EmailTemplates.nuevaNoticia(tituloNoticia);

        enviarEmail(emailUsuario, asunto, mensajeHtml);
    }
    
    public void notificarRegistrosAprobadosHoy(
            String emailUsuario,
            LinkedList<Registro> registros) {

        String asunto = "📋 Registros aprobados del día - Bestiario";

        String mensajeHtml =
                EmailTemplates.registrosAprobadosHoy(registros);

        enviarEmail(emailUsuario, asunto, mensajeHtml);
    }
    
    public void notificarCambioContraseña(String emailUsuario, String link) {
    	String asunto = "Recuperar Contraseña";
        	
    	String mensajeHtml = EmailTemplates.recuperacionContraseña(link);
    	
        enviarEmail(emailUsuario, asunto, mensajeHtml);
    }
    
    
}
