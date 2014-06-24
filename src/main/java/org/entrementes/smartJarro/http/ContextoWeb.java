package org.entrementes.smartJarro.http;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.entrementes.smartJarro.SmartJarro;
import org.entrementes.smartJarro.modelo.Jarro;

import br.com.caelum.grappa.context.GrappaContext;
import br.com.caelum.grappa.context.ServletBoundContext;

@WebListener
public class ContextoWeb implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent evento) {
		ServletContext contexto = evento.getServletContext();
		GrappaContext grappa = (GrappaContext) contexto.getAttribute("grappa");
		grappa.getPhysicalDevice().shutdown();
	}

	@Override
	public void contextInitialized(ServletContextEvent evento) {
		ServletContext contexto = evento.getServletContext();
		GrappaContext grappa = new ServletBoundContext(contexto);
		SmartJarro.construir((Jarro)grappa.getDevices().get("smart-jarro"));
		
	}

}
