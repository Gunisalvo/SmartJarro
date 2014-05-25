package org.gunisalvo.smartJarro.http.bean;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.gunisalvo.grappa.Barramento;
import org.gunisalvo.grappa.Grappa;
import org.gunisalvo.grappa.gpio.BarramentoGpio;
import org.gunisalvo.grappa.modelo.MapaEletrico;
import org.gunisalvo.grappa.modelo.PacoteGrappa;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Conexao;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Resultado;
import org.gunisalvo.grappa.modelo.PacoteGrappa.TipoAcao;
import org.gunisalvo.grappa.modelo.ValorSinalDigital;
import org.gunisalvo.grappa.modelo.RegistradoresGrappa;
import org.gunisalvo.grappa.registradores.BarramentoRegistradores;
import org.gunisalvo.smartJarro.http.InterfaceHttp;
import org.gunisalvo.smartJarro.modelo.Jarro;
import org.gunisalvo.smartJarro.modelo.Jarro.Estado;

public class InterfaceHttpJaxRS implements InterfaceHttp{
	
	@Override
	public Response lerLog() {
		return Response.ok(Grappa.getAplicacao().getLog(), MediaType.TEXT_PLAIN).build();
	}
	
	@Override
	public RegistradoresGrappa lerMapaRegistradores() {
		return BarramentoRegistradores.getBarramento().getEstado();
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

	@Override
	public Jarro lerEstadoJarro() {
		PacoteGrappa resultado = postarPacote(new PacoteGrappa(1, Conexao.REGISTRADOR, TipoAcao.LEITURA, null));
		if(Resultado.SUCESSO.equals(resultado.getResultado())){
			return (Jarro) resultado.getCorpo();
		}else{
			return new Jarro();
		}
	}

	@Override
	public Jarro postarJarro(Jarro jarro) {
		PacoteGrappa resultado = postarPacote(new PacoteGrappa(1, Conexao.GPIO, TipoAcao.LEITURA, null));
		jarro.setProtegido(true);
		switch((ValorSinalDigital)resultado.getCorpo()){
		case ALTO:
			jarro.setEstado(Estado.ABERTO);
			break;
		case BAIXO:
			jarro.setEstado(Estado.FECHADO);
			break;
		default:
			break;
		}
		postarPacote(new PacoteGrappa(1, Conexao.REGISTRADOR, TipoAcao.ESCRITA, jarro));
		return jarro;
	}
}
