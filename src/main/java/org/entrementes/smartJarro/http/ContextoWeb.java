package org.entrementes.smartJarro.http;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.entrementes.grappa.ContextoGrappa;

@WebListener
public class ContextoWeb implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent evento) {
		ContextoGrappa.getAplicacao().registrarDesligamento();
		System.gc();
	}

	@Override
	public void contextInitialized(ServletContextEvent evento) {
		ServletContext contexto = evento.getServletContext();
		ContextoGrappa.construir(contexto.getRealPath(""));
		
	}

}
