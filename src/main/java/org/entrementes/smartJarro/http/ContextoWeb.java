package org.entrementes.smartJarro.http;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.entrementes.grappa.contexto.ContextoGrappa;
import org.entrementes.grappa.contexto.ContextoServlet;
import org.entrementes.smartJarro.SmartJarro;
import org.entrementes.smartJarro.modelo.Jarro;

@WebListener
public class ContextoWeb implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent evento) {
		ServletContext contexto = evento.getServletContext();
		ContextoGrappa grappa = (ContextoGrappa) contexto.getAttribute("grappa");
		grappa.getImplementacao().desativar();
	}

	@Override
	public void contextInitialized(ServletContextEvent evento) {
		ServletContext contexto = evento.getServletContext();
		ContextoGrappa grappa = new ContextoServlet(contexto);
		SmartJarro.construir((Jarro)grappa.getDispositivos().get("smart-jarro"));
		
	}

}
