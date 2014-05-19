package org.gunisalvo.smartJarro.http.bean;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.gunisalvo.grappa.Barramento;
import org.gunisalvo.grappa.Grappa;
import org.gunisalvo.grappa.gpio.BarramentoGpio;
import org.gunisalvo.grappa.modelo.MapaEletrico;
import org.gunisalvo.grappa.modelo.PacoteGrappa;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Conexao;
import org.gunisalvo.grappa.modelo.PacoteGrappa.TipoAcao;
import org.gunisalvo.grappa.modelo.RegistradoresGrappa;
import org.gunisalvo.grappa.registradores.BarramentoRegistradores;
import org.gunisalvo.smartJarro.http.InterfaceHttp;

public class InterfaceHttpJaxRS implements InterfaceHttp{
	
	@Override
	public Response lerLog() {
		return Response.ok(Grappa.getAplicacao().getLog(), MediaType.TEXT_PLAIN).build();
	}
	
	@Override
	public RegistradoresGrappa lerMapaRegistradores() {
		return BarramentoRegistradores.getBarramento().getRegistradores();
	}
	
	@Override
	public PacoteGrappa postarPacote(PacoteGrappa requisicao) {
		return Barramento.processarPacote(requisicao);
	}

	@Override
	public PacoteGrappa postarPacotePorFormulario( Integer endereco, Conexao conexao, TipoAcao tipo, String corpo) {
		return postarPacote(new PacoteGrappa(endereco, conexao, tipo, corpo));
	}

	@Override
	public Response limparMapaRegistradores() {
		BarramentoRegistradores.getBarramento().limparRegistradores();
		return Response.ok().build();
	}

	@Override
	public MapaEletrico lerEstadoGpio() {
		return BarramentoGpio.getBarramento().getEstado();
	}
}