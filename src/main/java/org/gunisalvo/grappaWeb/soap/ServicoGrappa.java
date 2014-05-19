package org.gunisalvo.grappaWeb.soap;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import org.gunisalvo.grappa.Barramento;
import org.gunisalvo.grappa.gpio.BarramentoGpio;
import org.gunisalvo.grappa.modelo.MapaEletrico;
import org.gunisalvo.grappa.modelo.PacoteGrappa;
import org.gunisalvo.grappa.modelo.RegistradoresGrappa;
import org.gunisalvo.grappa.registradores.BarramentoRegistradores;

@WebService(targetNamespace="http://grappa.gunisalvo.org/",serviceName="servico-grappa")
@SOAPBinding(style=Style.DOCUMENT)
public class ServicoGrappa {
	
	@WebMethod(operationName="mapaRegistradores")
	@WebResult(name="registradores",targetNamespace="http://grappa.gunisalvo.org/")
	public RegistradoresGrappa lerMapaRegistradores(){
		return BarramentoRegistradores.getBarramento().getRegistradores();
	}
	
	@WebMethod(operationName="mapaGPIO")
	@WebResult(name="gpio",targetNamespace="http://grappa.gunisalvo.org/")
	public MapaEletrico lerEstadoGpio(){
		return BarramentoGpio.getBarramento().getEstado();
	}
	
	@WebMethod(operationName="processarPacote")
	@WebResult(name="grappa", targetNamespace="http://grappa.gunisalvo.org/")
	public PacoteGrappa postarPacote(@WebParam(name="grappa", targetNamespace="http://grappa.gunisalvo.org/") PacoteGrappa comando){
		return Barramento.processarPacote(comando);
	}

}
