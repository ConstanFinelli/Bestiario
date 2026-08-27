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
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            System.out.println("⚠️ No se puede enviar email: EMAIL_USER o EMAIL_PASS no están configurados en el archivo .env");
            return;
        }

        if (destinatario == null || destinatario.trim().isEmpty()) {
            System.out.println("⚠️ No se puede enviar email: el destinatario está vacío");
            return;
        }

        Properties props = new Properties();
        // Configuración para Gmail con SSL directo en puerto 465 (más estable en Java 21)
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.writetimeout", "10000");

        Session session = Session.getInstance(props,
            new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username.trim(), password.trim());
                }
            });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username.trim()));
            message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(destinatario.trim())
            );
            message.setSubject(asunto);
            message.setContent(mensajeHtml, "text/html; charset=UTF-8");

            Transport.send(message);

            System.out.println("✅ Email enviado exitosamente a " + destinatario);

        } catch (MessagingException e) {
            System.out.println("❌ Error enviando email a " + destinatario + ": " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("❌ Error inesperado enviando email a " + destinatario + ": " + e.getMessage());
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
