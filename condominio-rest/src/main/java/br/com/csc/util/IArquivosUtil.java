package br.com.csc.util;

import javax.servlet.ServletContext;

public interface IArquivosUtil {
	public String getCaminhaPastaTemplatesEmail(ServletContext context);
	public String getCaminhoArquivosUpload();
	public String getCaminhoBaseArquivos();

}
