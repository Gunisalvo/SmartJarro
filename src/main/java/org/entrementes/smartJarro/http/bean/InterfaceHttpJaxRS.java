package org.entrementes.smartJarro.http.bean;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.entrementes.smartJarro.SmartJarro;
import org.entrementes.smartJarro.http.InterfaceHttp;
import org.entrementes.smartJarro.modelo.CredencialSeguranca;
import org.entrementes.smartJarro.modelo.Jarro;

public class InterfaceHttpJaxRS implements InterfaceHttp{
	
	@Override
	public Response lerLog() {
		return Response.ok(SmartJarro.getConteudoLog(), MediaType.TEXT_PLAIN).build();
	}

	@Override
	public Jarro lerEstadoJarro() {
		return SmartJarro.carregarDispositivo();
	}

	@Override
	public Jarro postarJarro(Jarro jarro) {
		Jarro atual = SmartJarro.carregarDispositivo();
		atual.setEmail(jarro.getEmail());
		atual.setSenha(jarro.getSenhaAberta());
		atual.setUsername(jarro.getUsername());
		atual.atualizarEstado();
		atual.setProtegido(jarro.getProtegido() == null ? true : jarro.getProtegido());
		atual.setMensagemEnviada(jarro.getMensagemEnviada() == null ? false : jarro.getMensagemEnviada());
		SmartJarro.info("novo estado jarro: protegido -> " +atual.getProtegido());
		return atual;
	}

	@Override
	public Response protegerDesproteger(CredencialSeguranca credencial) {
		Jarro atual = SmartJarro.carregarDispositivo();
		if(atual.validarCredencial(credencial)){
			atual.setProtegido(!atual.getProtegido());
			if(atual.getProtegido()){
				atual.setMensagemEnviada(false);
			}
			return Response.ok().type(MediaType.TEXT_PLAIN).entity("ok").build();
		}else{
			return Response.status(Status.UNAUTHORIZED).type(MediaType.TEXT_PLAIN).entity("nao autorizado").build();
		}
	}
	
	@Override
	public Response protegerDesprotegerFormulario(String senha) {
		return protegerDesproteger(new CredencialSeguranca(senha));
	}

	@Override
	public Jarro postarJarroFormulario(String username, String email, String senha) {
		return postarJarro(new Jarro(email, senha, null, null, username));
	}

	@Override
	public Response abrirJarro() {
		Jarro atual = SmartJarro.carregarDispositivo();
		atual.abrir();
		return Response.ok().build();
	}
}
