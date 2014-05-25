package org.gunisalvo.smartJarro.servico.gpio;

import org.gunisalvo.grappa.Barramento;
import org.gunisalvo.grappa.Grappa;
import org.gunisalvo.grappa.Grappa.NivelLog;
import org.gunisalvo.grappa.gpio.GPIOListener;
import org.gunisalvo.grappa.gpio.ServicoBarramentoGpio;
import org.gunisalvo.grappa.modelo.PacoteGrappa;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Conexao;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Resultado;
import org.gunisalvo.grappa.modelo.PacoteGrappa.TipoAcao;
import org.gunisalvo.smartJarro.modelo.Jarro;
import org.gunisalvo.smartJarro.modelo.Jarro.Estado;

@GPIOListener(pino = 1)
public class ServicoFotoresitor implements ServicoBarramentoGpio{

	@Override
	public void processarServico(Integer estadoPino){
		Grappa.getAplicacao().log("fotoresistor : " + estadoPino, NivelLog.INFO);
		PacoteGrappa resultado = Barramento.processarPacote(new PacoteGrappa(1, Conexao.REGISTRADOR, TipoAcao.LEITURA, null));
		if(Resultado.SUCESSO.equals(resultado.getResultado()) && resultado.getCorpo() != null){
			Jarro jarro = (Jarro) resultado.getCorpo();
			if(estadoPino == 1){
				jarro.setEstado(Estado.ABERTO);
				Grappa.getAplicacao().log("jarro Aberto.", NivelLog.INFO);
			}else{
				jarro.setEstado(Estado.FECHADO);
				Grappa.getAplicacao().log("jarro Fechado.", NivelLog.INFO);
			}
			Barramento.processarPacote(new PacoteGrappa(1, Conexao.REGISTRADOR, TipoAcao.ESCRITA, jarro));
		}else{
			Grappa.getAplicacao().log("Jarro não registrado.",NivelLog.INFO);
		}
	}
}
