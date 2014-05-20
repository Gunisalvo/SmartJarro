package org.gunisalvo.smartJarro.http;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.gunisalvo.grappa.modelo.MapaEletrico;
import org.gunisalvo.grappa.modelo.PacoteGrappa;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Conexao;
import org.gunisalvo.grappa.modelo.PacoteGrappa.TipoAcao;
import org.gunisalvo.grappa.modelo.RegistradoresGrappa;
import org.gunisalvo.smartJarro.modelo.Jarro;

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
	public PacoteGrappa postarPacote(PacoteGrappa comando);
	
	@POST
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Path("resultado-pacote.xml")
	public PacoteGrappa postarPacotePorFormulario(	@FormParam("endereco") Integer endereco,
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
