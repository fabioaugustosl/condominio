package br.com.virtz.email.template;

import java.io.File;
import java.util.Map;

public interface IGerarTemplate {
	public String gerar(Map<Object, Object> map, File directoryForTemplateLoading, String templateName) throws Exception;
}
