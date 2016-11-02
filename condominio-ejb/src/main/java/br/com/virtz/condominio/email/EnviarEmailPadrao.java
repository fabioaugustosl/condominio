package br.com.virtz.condominio.email;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.lang.StringUtils;

import br.com.virtz.condominio.bean.Email;

@Stateless
public class EnviarEmailPadrao implements EnviarEmail {

	@Asynchronous
	public void enviar(Email email) {
        try {
        	Session sessaoEmail = null;
        	try {
				String host = InetAddress.getLocalHost().getHostAddress();
				if("127.0.0.1".equals(host)
						|| "192.168.1.3".equals(host)
						|| "192.168.1.4".equals(host)
						|| "192.168.0.5".equals(host)
						|| "192.168.1.5".equals(host)
						|| "192.168.1.6".equals(host)
						|| "192.168.1.8".equals(host)){
					sessaoEmail = getSessao();
				} else {
					sessaoEmail = getSessaoAWS();
				}
			} catch (UnknownHostException e1) {
				sessaoEmail = getSessaoAWS();
			}
        	//getSessaoAWS();
            MimeMessage m = new MimeMessage(sessaoEmail);
            Address de = new InternetAddress(email.getDe());
            Address[] para = InternetAddress.parse(email.getParaToString());
            m.setFrom(de);
            m.setRecipients(Message.RecipientType.TO, para);
            m.setSubject(email.getAssunto());

            Multipart multiparteEmail = new MimeMultipart();

            // creates body part for the message
            MimeBodyPart msgEmail = new MimeBodyPart();
            String msgEnviar= null;
//			try {
//				msgEnviar = new String(email.getMensagem().getBytes(), "ISO-8859-1");
//			} catch (UnsupportedEncodingException e1) {
				msgEnviar = email.getMensagem();
//			}
            msgEmail.setContent(msgEnviar, "text/html;");

            // adds parts to the multipart
            multiparteEmail.addBodyPart(msgEmail);

            // se tiver anexo
            try {
	            if(email.getAnexo() != null && email.getAnexo().length > 0){
	            	// creates body part for the attachment
	            	MimeBodyPart anexoEmail = new MimeBodyPart();
	            	DataSource source = new ByteArrayDataSource(email.getAnexo(), "application/octet-stream");
	            	anexoEmail.setDataHandler(new DataHandler(source));
	            	anexoEmail.setFileName(email.getNomeAnexo());
	            	multiparteEmail.addBodyPart(anexoEmail);

	            	// sets the multipart as message's content
	            }
            } catch(Exception e ){
            	e.printStackTrace();
            }

            m.setContent(multiparteEmail,"text/html");

            Transport.send(m);
//            return Boolean.TRUE;
        } catch (javax.mail.MessagingException e) {
            e.printStackTrace();
//            return Boolean.FALSE;
        }

    }


	private Session getSessao() {
	    Properties props = new Properties();
	    //Parâmetros de conexão com servidor Gmail

	    props.put ("mail.smtp.host", "in-v3.mailjet.com"); //in-v3.mailjet.com
		props.put ("mail.smtp.socketFactory.port", "587");
		props.put ("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put ("mail.smtp.auth", "true");
		props.put ("mail.smtp.port", "587");

	    Session session = Session.getDefaultInstance(props,
	                new javax.mail.Authenticator() {
	                     protected PasswordAuthentication getPasswordAuthentication()   {
	                           return new PasswordAuthentication("75126758f304eb7c71d30c3810ec3a49", "f15bafbffae7d3fe106e51f2c9a56bf9");
	                     }
	                });

	    // Ativa Debug para sessão
	    session.setDebug(true);
	    return session;
	}


	// Usando o SES da AWS - email da amazon
	private Session getSessaoAWS() {
        Properties props = new Properties();

        props.put("mail.smtp.host", "email-smtp.us-west-2.amazonaws.com");
        props.put("mail.smtp.auth", "true");
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.port", "587");
		props.put ("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.starttls.required", "true");

        Session session = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator() {
                         protected PasswordAuthentication getPasswordAuthentication()   {
                               return new PasswordAuthentication("AKIAJTZZ2ONI3FZG3EXQ", "AqJ12mRGp0cKnbWTJQfpwd8zS8kKePnJ7Ql0/Num/GhY");
                         }
                    });

        // Ativa Debug para sessão
        session.setDebug(true);
        return session;
	}

}


