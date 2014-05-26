package org.gunisalvo.smartJarro.modelo;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="jarro")
public class Jarro {
	
	@XmlEnum
	public enum Estado{
		@XmlEnumValue(value="aberto") ABERTO,
		@XmlEnumValue(value="fechado") FECHADO
		;
	}
	
	private String email;
	
	private String senha;
	
	private Boolean protegido;
	
	private Estado estado;

	private String username;

	public Jarro() {
	}
	
	public Jarro(String email,String senha,Boolean protegido,Estado estado,String username) {
		this.email = email;
		this.senha = senha;
		this.protegido = protegido;
		this.username = username;
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

}
