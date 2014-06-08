package org.entrementes.smartJarro.http.bean;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.entrementes.grappa.ContextoGrappa;
import org.entrementes.grappa.gpio.BarramentoGpio;
import org.entrementes.grappa.modelo.MapaEletrico;
import org.entrementes.grappa.modelo.InstrucaoGrappa;
import org.entrementes.grappa.modelo.InstrucaoGrappa.Conexao;
import org.entrementes.grappa.modelo.InstrucaoGrappa.Resultado;
import org.entrementes.grappa.modelo.InstrucaoGrappa.TipoAcao;
import org.entrementes.grappa.modelo.RegistradoresGrappa;
import org.entrementes.grappa.modelo.ValorSinalDigital;
import org.entrementes.grappa.registradores.BarramentoRegistradores;
import org.entrementes.smartJarro.http.InterfaceHttp;
import org.entrementes.smartJarro.modelo.Jarro;
import org.entrementes.smartJarro.modelo.Jarro.Estado;

public class InterfaceHttpJaxRS implements InterfaceHttp{
	
	@Override
	public Response lerLog() {
		return Response.ok(ContextoGrappa.getAplicacao().getLog(), MediaType.TEXT_PLAIN).build();
	}
	
	@Override
	public RegistradoresGrappa lerMapaRegistradores() {
		return BarramentoRegistradores.getBarramento().getEstado();
	}
	
	@Override
	public InstrucaoGrappa postarPacote(InstrucaoGrappa requisicao) {
		return ContextoGrappa.processarInstrucao(requisicao);
	}

	@Override
	public InstrucaoGrappa postarPacotePorFormulario( Integer endereco, Conexao conexao, TipoAcao tipo, String corpo) {
		return postarPacote(new InstrucaoGrappa(endereco, conexao, tipo, corpo));
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
		InstrucaoGrappa resultado = postarPacote(new InstrucaoGrappa(1, Conexao.REGISTRADOR, TipoAcao.LEITURA, null));
		if(Resultado.SUCESSO.equals(resultado.getResultado())){
			return (Jarro) resultado.getCorpoValor();
		}else{
			return new Jarro();
		}
	}

	@Override
	public Jarro postarJarro(Jarro jarro) {
		InstrucaoGrappa resultado = postarPacote(new InstrucaoGrappa(1, Conexao.GPIO, TipoAcao.LEITURA, null));
		jarro.setProtegido(true);
		switch((ValorSinalDigital)resultado.getCorpoValor()){
		case ALTO:
			jarro.setEstado(Estado.ABERTO);
			break;
		case BAIXO:
			jarro.setEstado(Estado.FECHADO);
			break;
		default:
			break;
		}
		postarPacote(new InstrucaoGrappa(1, Conexao.REGISTRADOR, TipoAcao.ESCRITA, jarro));
		return jarro;
	}
}
