package br.com.csc.util;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EnviadorEmail{

	public void enviar( String de,
	 String para,
	 String assunto,
	 String mensagem,
	 String responderPara) {
        try {
        	Session sessaoEmail = null;

        	try {
        		System.out.println(" [[[ INFO ]]] Vai tentar pegar sessão mail AWS");
        		sessaoEmail = getSessaoAWS();
        	} catch (Exception e) {
        		e.printStackTrace();
        		try {
        			System.out.println(" [[[ INFO ]]] Vai tentar pegar sessão Email Alternativa");
        			sessaoEmail = getSessao();
        		}catch (Exception es) {
        			es.printStackTrace();
				}
			}

        	if(sessaoEmail == null){
        		System.out.println(" [[[ E R R O  ]]]  Não conseguiu recuperar sessão de email");
        		 try{
        	        	this.sendAws(de, para, assunto, mensagem, responderPara, getSessaoAWS2());
        	        }catch (Exception e) {
        	        	System.out.println("[[E R R O ]] Não foi também pelo tipo 2");
        	        	e.printStackTrace();

        	        	try{
            	        	this.sendAws2(de, para, assunto, mensagem, responderPara, getSessaoAWS2());
            	        }catch (Exception e2) {
            	        	System.out.println("[[E R R O ]] Não foi também pelo tipo 2");
            	        	e2.printStackTrace();
            			}

        			}

        		return;
        	}

            MimeMessage m = new MimeMessage(sessaoEmail);
            Address deMail = new InternetAddress(de);
            Address[] paraMail = InternetAddress.parse(para);
            m.setFrom(deMail);
            m.setRecipients(Message.RecipientType.TO, paraMail);
            m.setSubject(assunto, "ISO-8859-1");

            if(responderPara != null){
            	Address[] respPara = InternetAddress.parse(responderPara);
            	m.setReplyTo(respPara);
            }
            //m.setSubject(email.getAssunto());

            Multipart multiparteEmail = new MimeMultipart();

            // creates body part for the message
            MimeBodyPart msgEmail = new MimeBodyPart();

            msgEmail.setContent(mensagem, "text/html; charset=ISO-8859-1");

            // adds parts to the multipart
            multiparteEmail.addBodyPart(msgEmail);

            // se tiver anexo
          /*  try {
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
            }*/

            m.setContent(multiparteEmail,"text/html; charset=ISO-8859-1");

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
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
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

	// Usando o SES da AWS - email da amazon
	private Session getSessaoAWS2() {
	        Properties props = new Properties();

	    	props.put("mail.transport.protocol", "smtp");
	    	props.put("mail.smtp.port", 465);
	    	props.put("mail.smtp.ssl.enable", "true");
	    	props.put("mail.smtp.auth", "true");

	    	return Session.getDefaultInstance(props);
	}


	private void sendAws(String de,
	 String para,
	 String assunto,
	 String mensagem,
	 String responderPara, Session session) throws UnsupportedEncodingException, MessagingException{

        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(de,"Condomínio Sob Controle"));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(para));
        msg.setSubject(assunto);
        msg.setContent(mensagem,"text/html");

        // Add a configuration set header. Comment or delete the
        // next line if you are not using a configuration set
        //msg.setHeader("X-SES-CONFIGURATION-SET", CONFIGSET);

        // Create a transport.
        Transport transport = session.getTransport();

        // Send the message.
        try
        {
            System.out.println("Sending...");

            // Connect to Amazon SES using the SMTP username and password you specified above.
            transport.connect("email-smtp.us-west-2.amazonaws.com", "AKIAJTZZ2ONI3FZG3EXQ", "AqJ12mRGp0cKnbWTJQfpwd8zS8kKePnJ7Ql0");

            // Send the email.
            transport.sendMessage(msg, msg.getAllRecipients());
            System.out.println("Email sent!");
        }
        catch (Exception ex) {
            System.out.println("The email was not sent.");
            System.out.println("Error message: " + ex.getMessage());
        }
        finally
        {
            // Close and terminate the connection.
            transport.close();
        }

	}


	private void sendAws2(String de,
			 String para,
			 String assunto,
			 String mensagem,
			 String responderPara, Session session) throws UnsupportedEncodingException, MessagingException{

		        MimeMessage msg = new MimeMessage(session);
		        msg.setFrom(new InternetAddress(de,"Condomínio Sob Controle"));
		        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(para));
		        msg.setSubject(assunto);
		        msg.setContent(mensagem,"text/html");

		        // Add a configuration set header. Comment or delete the
		        // next line if you are not using a configuration set
		        //msg.setHeader("X-SES-CONFIGURATION-SET", CONFIGSET);

		        // Create a transport.
		        Transport transport = session.getTransport();

		        // Send the message.
		        try
		        {
		            System.out.println("Sending...");

		            // Connect to Amazon SES using the SMTP username and password you specified above.
		            transport.connect("email-smtp.us-west-2.amazonaws.com", "AKIAJ4WLWXCQ7FUIDUMA", "K4XJchpcPQVZC/rTudCbs7eUKak3jU7aG9t300sM");

		            // Send the email.
		            transport.sendMessage(msg, msg.getAllRecipients());
		            System.out.println("Email sent!");
		        }
		        catch (Exception ex) {
		            System.out.println("The email was not sent.");
		            System.out.println("Error message: " + ex.getMessage());
		        }
		        finally
		        {
		            // Close and terminate the connection.
		            transport.close();
		        }

			}
}


