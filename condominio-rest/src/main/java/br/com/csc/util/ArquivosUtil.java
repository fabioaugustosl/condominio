package br.com.csc.util;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.servlet.ServletContext;

@Named
@SessionScoped
public class ArquivosUtil implements IArquivosUtil, Serializable {

	private static final long serialVersionUID = -3351621178928638800L;

	public static final String TIPO_ARQUIVO_NOTICIA = "NOT";
	public static final String TIPO_ARQUIVO_ATA = "ATA";
	public static final String TIPO_ARQUIVO_DOCUMENTO = "DOC";
	public static final String TIPO_ARQUIVO_BALANCO = "BAL";
	public static final String TIPO_PORTARIA = "POR";
	public static final String TIPO_IMAGEM = "IMG";
	public static final String DIRETORIO_PADRAO_TEMPLATES = "WEB-INF\\email";
	public static final String THUMB_POS_FIXO = "_THUMB";

	private static final int MAX_THUMBNAIL_WIDTH = 100;

	private String[] extensoesTipoImagens = new String[]{"jpg" ,"JPG", "jpeg", "JPEG", "png", "PNG", "gif", "GIF"};


	public String getCaminhaPastaTemplatesEmail(ServletContext context){
		String path = context.getRealPath("/");
	    if(path.contains("/")){
	    	if(!path.endsWith("/")){
	    		path += "/";
	    	}
	    	path +=DIRETORIO_PADRAO_TEMPLATES.replace("\\", "/");
	    } else {
	    	if(!path.endsWith("\\")){
	    		path += "\\";
	    	}
	    	path +=DIRETORIO_PADRAO_TEMPLATES;
	    }

	    return path;
	}


	public String getCaminhoArquivosUpload(){
		return getCaminhoBaseArquivos()+"/arquivos/";
	}

	public String getCaminhoBaseArquivos(){
		return System.getProperty("jboss.server.base.dir");
	}


}
