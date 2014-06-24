package org.entrementes.smartJarro.modelo;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.entrementes.smartJarro.SmartJarro;

import br.com.caelum.grappa.annotation.Device;
import br.com.caelum.grappa.annotation.Hardware;
import br.com.caelum.grappa.annotation.PinListener;
import br.com.caelum.grappa.model.GrappaInstruction;
import br.com.caelum.grappa.model.builder.LogicInstruction;
import br.com.caelum.grappa.pin.PhysicalDevice;

@XmlRootElement(name="smart-jarro")
@Device(nome="smart-jarro")
public class Jarro {
	
	@XmlEnum
	public enum Estado{
		@XmlEnumValue(value="aberto") ABERTO,
		@XmlEnumValue(value="fechado") FECHADO
		;
	}
	
	@Hardware
	private PhysicalDevice pi;
	
	private String email;
	
	private String senha;
	
	private Boolean protegido;
	
	private Estado estado;

	private String username;

	private Boolean mensagemEnviada;

	public Jarro() {
	}
	
	public Jarro(String email,String senha,Boolean protegido,Estado estado,String username) {
		this.email = email;
		this.senha = senha;
		this.protegido = protegido;
		this.username = username;
		this.mensagemEnviada = false;
	}	
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha == null ? null : String.valueOf(senha.hashCode());
	}
	
	@XmlTransient
	public String getSenhaAberta() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Boolean getProtegido() {
		return protegido;
	}

	public void setProtegido(Boolean protegido) {
		this.protegido = protegido;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
		if(isViolado() && credenciaisCadastradas() && !this.mensagemEnviada){
			this.mensagemEnviada = true;
			enviarAlerta(this.username, this.email, this.senha);
		}
	}
	
	private boolean credenciaisCadastradas() {
		return this.username != null && this.email != null && this.senha != null;
	}

	public boolean isViolado(){
		return Estado.ABERTO.equals(estado) && (this.protegido == null ? false : this.protegido);
	}

	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public Boolean getMensagemEnviada() {
		return mensagemEnviada;
	}

	public void setMensagemEnviada(Boolean mensagemEnviada) {
		this.mensagemEnviada = mensagemEnviada;
	}

	@PinListener(addresses=1)
	public void processarEventoFotoresistor(Integer sinal){
		SmartJarro.info("fotoresistor: " +sinal);
		if(sinal.intValue() == 1){
			setEstado(Estado.ABERTO);
		}else{
			setEstado(Estado.FECHADO);
		}
	}
	
	private void enviarAlerta(final String username, String email, final String senha){
		
		try {
			buscarCulpado();
		} catch (Exception ex) {
			SmartJarro.erro(ex.getMessage());
		}
		
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
			Message mensagem = new MimeMessage(session);
			mensagem.setFrom(new InternetAddress(email));
			mensagem.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(email));
			mensagem.setSubject("[SmartJarro] Seguranca Comprometida!");

			MimeMultipart multipart = new MimeMultipart();
			
			MimeBodyPart imagem = new MimeBodyPart();
			imagem.attachFile("/home/pi/camera/culpado.jpg");
			
			MimeBodyPart texto = new MimeBodyPart();
			texto.setText("Seu jarro esta sendo roubado. Suspeito principal:");

			multipart.addBodyPart(texto);
			multipart.addBodyPart(imagem);
			
			mensagem.setContent(multipart);
			Transport.send(mensagem);
 
			SmartJarro.info("Mensagem enviada.");
 
		} catch (MessagingException ex) {
			SmartJarro.erro(ex.getMessage());
			SmartJarro.erro(ex.getNextException().getMessage());
			throw new RuntimeException(ex);
		} catch (IOException ex) {
			SmartJarro.erro(ex.getMessage());
			SmartJarro.erro(ex.getCause().getMessage());
			throw new RuntimeException(ex);
		}
	}

	private void buscarCulpado() throws Exception {
		Process script = new ProcessBuilder("/home/pi/camera/./fotografar.sh").start();
		script.waitFor();
	}

	public boolean validarCredencial(CredencialSeguranca credencial) {
		return this.senha != null && this.senha.equals(credencial.getSenhaAberta());
	}

	public void atualizarEstado() {
		GrappaInstruction resposta = this.pi.processInstruction(new LogicInstruction().address(1).read());
		processarEventoFotoresistor(resposta.getBody());
	}

	public void abrir() {
		this.pi.processInstruction(new LogicInstruction().address(1).write(1));
	}
}
