package org.entrementes.smartJarro;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.entrementes.smartJarro.modelo.Jarro;

public class SmartJarro {

	private static SmartJarro INSTANCIA;

	private Logger log;

	private Jarro dispositivo;

	private SmartJarro(Jarro dispositivo) {
		this.dispositivo = dispositivo;
		this.log = Logger.getLogger("smart-jarro");
		Properties configuracoes = new Properties();
		try {
			configuracoes.load(this.getClass().getClassLoader()
					.getResourceAsStream("grappa.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		configuracoes.put("log4j.appender.GRAPPA.File",
				"log/smart-jarro.log");
		PropertyConfigurator.configure(configuracoes);
		log.warn("");
		log.warn("=========================================================================");
		log.warn("GRAPPA >> DEPLOY EFETUADO: "
				+ new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(Calendar
						.getInstance().getTime()));
		log.warn("log criado em: " + "log/grappa.log");
		log.warn("=========================================================================");
		log.warn("");
	}

	public static void construir(Jarro dispositivo) {
		INSTANCIA = new SmartJarro(dispositivo);
	}

	public static Jarro carregarDispositivo() {
		return INSTANCIA.getDispositivo();
	}

	public Jarro getDispositivo() {
		return this.dispositivo;
	}
	
	public Logger getLog(){
		return this.log;
	}
	
	public static void erro(String mensagem){
		INSTANCIA.getLog().error(mensagem);
	}
	
	public static void aviso(String mensagem){
		INSTANCIA.getLog().warn(mensagem);
	}
	
	public static void info(String mensagem){
		INSTANCIA.getLog().info(mensagem);
	}
	
	public static void debug(String mensagem){
		INSTANCIA.getLog().debug(mensagem);
	}

	public static String getConteudoLog() {
		try {
			BufferedReader leitor = new BufferedReader(new FileReader(
					"log/smart-jarro.log"));
			StringBuilder resultado = new StringBuilder();
			String linha = leitor.readLine();

			while (linha != null) {
				resultado.append(linha);
				resultado.append("\n");

				linha = leitor.readLine();
			}

			leitor.close();
			return resultado.toString();

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}
