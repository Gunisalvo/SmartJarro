package org.entrementes.smartJarro.http;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.entrementes.smartJarro.modelo.CredencialSeguranca;
import org.entrementes.smartJarro.modelo.Jarro;

@Path("/")
public interface InterfaceHttp {
	
	@GET
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Path("jarro")
	public Jarro lerEstadoJarro();
	
	@POST
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Path("jarro")
	public Jarro postarJarro(Jarro novo);
	
	@POST
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Path("jarro.xml")
	public Jarro postarJarroFormulario(	@FormParam("username") String username,
										@FormParam("email") String email, 
										@FormParam("senha") String senha);
	
	@GET
	@Produces({MediaType.TEXT_PLAIN})
	@Path("log")
	public Response lerLog();
	
	@PUT
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Path("jarro/protegerDesproteger")
	public Response protegerDesproteger(CredencialSeguranca cedencial);

	@POST
	@Path("jarro/protegerDesproteger.txt")
	public Response protegerDesprotegerFormulario(@FormParam("senha") String senha);

}
