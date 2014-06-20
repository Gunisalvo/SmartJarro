package org.entrementes.smartJarro.modelo;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.entrementes.grappa.gpio.Raspberry;
import org.entrementes.grappa.marcacao.Dispositivo;
import org.entrementes.grappa.marcacao.Hardware;
import org.entrementes.grappa.marcacao.ObservadorGpio;
import org.entrementes.grappa.modelo.InstrucaoGrappa;
import org.entrementes.grappa.modelo.instrucao.InstrucaoLogica;
import org.entrementes.smartJarro.SmartJarro;

@XmlRootElement(name="smart-jarro")
@Dispositivo(nome="smart-jarro")
public class Jarro {
	
	@XmlEnum
	public enum Estado{
		@XmlEnumValue(value="aberto") ABERTO,
		@XmlEnumValue(value="fechado") FECHADO
		;
	}
	
	@Hardware
	private Raspberry pi;
	
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
		return Estado.ABERTO.equals(estado) && this.protegido;
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

	@ObservadorGpio(endereco=1)
	public void processarEventoFotoresistor(Integer sinal){
		SmartJarro.info("fotoresistor: " +sinal);
		if(sinal.intValue() == 1){
			setEstado(Estado.ABERTO);
		}else{
			setEstado(Estado.FECHADO);
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
			message.setSubject("[SmartJarro] Seguranca Comprometida!");
			message.setText("Seu jarro esta sendo roubado!");
 
			Transport.send(message);
 
			System.out.println("Done");
 
		} catch (MessagingException e) {
			SmartJarro.erro(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	public boolean validarCredencial(CredencialSeguranca credencial) {
		return this.senha != null && this.senha.equals(credencial.getSenhaAberta());
	}

	public void atualizarEstado() {
		InstrucaoGrappa resposta = this.pi.processarInstrucao(new InstrucaoLogica().endereco(1).ler());
		processarEventoFotoresistor(resposta.getCorpo());
	}
}
