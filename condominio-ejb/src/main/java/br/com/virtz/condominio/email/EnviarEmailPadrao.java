package br.com.virtz.condominio.email;

import java.util.Properties;

import javax.ejb.Stateless;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import br.com.virtz.condominio.bean.Email;

@Stateless
public class EnviarEmailPadrao implements EnviarEmail {

	public boolean enviar(Email email) {
        try {
        	Session sessaoEmail = getSessao();
            MimeMessage m = new MimeMessage(sessaoEmail);
            Address de = new InternetAddress(email.getDe());
            Address[] para = InternetAddress.parse(email.getParaToString());  
            m.setFrom(de);
            m.setRecipients(Message.RecipientType.TO, para);
            m.setSubject(email.getAssunto());
            m.setContent(email.getMensagem(), "text/html; charset=utf-8");
            Transport.send(m);
            return Boolean.TRUE;
        } catch (javax.mail.MessagingException e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }
	

	

	private Session getSessao() {
        Properties props = new Properties();
        /** Parâmetros de conexão com servidor Gmail */
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator() {
                         protected PasswordAuthentication getPasswordAuthentication()   {
                               return new PasswordAuthentication("contatovirtz@gmail.com", "#xupagalo");
                         }
                    });

        /** Ativa Debug para sessão */
        session.setDebug(true);
        return session;
	}
	
	/*private Session getSessaoG2() {
        Properties props = new Properties();
        *//** Parâmetros de conexão com servidor Gmail *//*
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator() {
                         protected PasswordAuthentication getPasswordAuthentication()   {
                               return new PasswordAuthentication("fabioaugustosl@gmail.com", "Fabio159216");
                         }
                    });

        *//** Ativa Debug para sessão *//*
        session.setDebug(true);
        return session;
	}*/
}


