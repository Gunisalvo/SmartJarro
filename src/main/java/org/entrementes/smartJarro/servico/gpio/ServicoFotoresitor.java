package org.entrementes.smartJarro.servico.gpio;

import org.entrementes.grappa.ContextoGrappa;
import org.entrementes.grappa.ContextoGrappa.NivelLog;
import org.entrementes.grappa.gpio.ObservadorGpio;
import org.entrementes.grappa.gpio.ServicoGpio;
import org.entrementes.grappa.modelo.InstrucaoGrappa;
import org.entrementes.grappa.modelo.InstrucaoGrappa.Conexao;
import org.entrementes.grappa.modelo.InstrucaoGrappa.Resultado;
import org.entrementes.grappa.modelo.InstrucaoGrappa.TipoAcao;
import org.entrementes.smartJarro.modelo.Jarro;
import org.entrementes.smartJarro.modelo.Jarro.Estado;

@ObservadorGpio(endereco = 1)
public class ServicoFotoresitor implements ServicoGpio{

	@Override
	public void processarServico(Integer estadoPino){
		ContextoGrappa.getAplicacao().log("fotoresistor : " + estadoPino, NivelLog.INFO);
		InstrucaoGrappa resultado = ContextoGrappa.processarInstrucao(new InstrucaoGrappa(1, Conexao.REGISTRADOR, TipoAcao.LEITURA, null));
		if(Resultado.SUCESSO.equals(resultado.getResultado()) && resultado.getValor() != null){
			Jarro jarro = (Jarro) resultado.getCorpoValor();
			if(estadoPino == 1){
				jarro.setEstado(Estado.ABERTO);
				ContextoGrappa.getAplicacao().log("jarro Aberto.", NivelLog.INFO);
			}else{
				jarro.setEstado(Estado.FECHADO);
				ContextoGrappa.getAplicacao().log("jarro Fechado.", NivelLog.INFO);
			}
			ContextoGrappa.processarInstrucao(new InstrucaoGrappa(1, Conexao.REGISTRADOR, TipoAcao.ESCRITA, jarro));
		}else{
			ContextoGrappa.getAplicacao().log("Jarro n��o registrado.",NivelLog.INFO);
		}
	}
}
