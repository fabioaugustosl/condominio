package br.com.virtz.email.template;

import java.io.File;
import java.io.StringWriter;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;


public class GerarTemplate implements IGerarTemplate {
	private Configuration cfg;

	public GerarTemplate() {
		super();
		cfg = new Configuration();
	}

	public String gerar(Map<Object, Object> map, File directoryForTemplateLoading, String templateName) throws Exception  {
		// diretório onde estão templates

		if(directoryForTemplateLoading == null || templateName == null){
			throw new Exception("Todos os atributos são obrigatórios.");
		}

		cfg.setDirectoryForTemplateLoading(directoryForTemplateLoading);

		cfg.setObjectWrapper(new DefaultObjectWrapper());

		// recupera o template

		//Template t = cfg.getTemplate(templateName, "UTF-8");
		Template t = cfg.getTemplate(templateName, "ISO-8859-1");
		if(t.toString().contains("UTF-8")){
			t = cfg.getTemplate(templateName, "UTF-8");
		}

		StringWriter writer = new StringWriter();

		/** Freemarker **/

		try {
			t.process(map, writer);
		} catch (TemplateException e) {
			e.printStackTrace();
			return null;
		}

		writer.flush();

		writer.close();

		return writer.toString();
	}

}
