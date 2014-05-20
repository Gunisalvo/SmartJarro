package org.gunisalvo.smartJarro.servico.registrador;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.gunisalvo.grappa.registradores.RegistradorListener;
import org.gunisalvo.grappa.registradores.ServicoRegistrador;
import org.gunisalvo.smartJarro.modelo.Jarro;

@RegistradorListener(endereco=1)
public class ServicoSeguranca implements ServicoRegistrador{
	
	@Override
	public void processarServico(Object valorEndereco) {
		Jarro jarro = (Jarro) valorEndereco;
		if(jarro.violado()){
			enviarAlerta(jarro.getUsername(),jarro.getEmail(),jarro.getSenha());
		}
		
	}
	
	private void enviarAlerta(final String username, String email, final String senha){
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
 
		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username,senha);
				}
			});
 
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(email));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(email));
			message.setSubject("[SmartJarro] Segurança Comprometida!");
			message.setText("Seu jarro está sendo roubado!");
 
			Transport.send(message);
 
			System.out.println("Done");
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

}
