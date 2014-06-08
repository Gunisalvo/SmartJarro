package org.entrementes.smartJarro.http;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.entrementes.grappa.modelo.MapaEletrico;
import org.entrementes.grappa.modelo.InstrucaoGrappa;
import org.entrementes.grappa.modelo.InstrucaoGrappa.Conexao;
import org.entrementes.grappa.modelo.InstrucaoGrappa.TipoAcao;
import org.entrementes.grappa.modelo.RegistradoresGrappa;
import org.entrementes.smartJarro.modelo.Jarro;

@Path("/")
public interface InterfaceHttp {
	
	@GET
	@Produces({MediaType.TEXT_PLAIN})
	@Path("log")
	public Response lerLog();
	
	@GET
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Path("registradores")
	public RegistradoresGrappa lerMapaRegistradores();
	
	@DELETE
	@Path("registradores")
	public Response limparMapaRegistradores();
	
	@GET
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Path("gpio")
	public MapaEletrico lerEstadoGpio();
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Path("resultado-pacote")
	public InstrucaoGrappa postarPacote(InstrucaoGrappa comando);
	
	@POST
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Path("resultado-pacote.xml")
	public InstrucaoGrappa postarPacotePorFormulario(	@FormParam("endereco") Integer endereco,
													@FormParam("conexao") Conexao conexao, 
													@FormParam("tipo") TipoAcao tipo,
													@FormParam("corpo") String corpo);
	
	@GET
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Path("jarro")
	public Jarro lerEstadoJarro();
	
	@POST
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Path("jarro")
	public Jarro postarJarro(Jarro novo);

}
