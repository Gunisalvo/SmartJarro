package org.entrementes.smartJarro.modelo;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="credencial")
public class CredencialSeguranca {
	
	private String senha;

	CredencialSeguranca() {
	}
	
	public CredencialSeguranca(String senha) {
		this.senha = senha;
	}

	public String getSenha() {
		return senha == null ? null : String.valueOf(senha.hashCode());
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	@XmlTransient
	public String getSenhaAberta(){
		return this.senha;
	}
	
	

}
