package br.com.virtz.condominio.email;

import javax.annotation.Resource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EnviarEmail {

	@Resource(lookup = "java:jboss/mail/APPEmail")
    private Session mailSession;
	
	
	public void enviar(String email) {
	        try {
	            MimeMessage m = new MimeMessage(mailSession);
	            Address from = new InternetAddress("condominio@meucondominio.com");
	            Address[] to = new InternetAddress[] { new InternetAddress(email) };
	            m.setFrom(from);
	            m.setRecipients(Message.RecipientType.TO, to);
	            m.setSubject("Teste");
	            m.setContent("Este é apenas um teste imbecil", "text/plain");
	            Transport.send(m);

	        } catch (javax.mail.MessagingException e) {
	            e.printStackTrace();
	        }
	    }
}
