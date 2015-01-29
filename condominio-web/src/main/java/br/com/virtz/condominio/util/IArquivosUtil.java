package br.com.virtz.condominio.util;

import java.io.IOException;
import java.io.InputStream;

public interface IArquivosUtil {
	public String getCaminhaPastaTemplatesEmail();
	public String getCaminhoArquivosUpload();
	public String pegarExtensao(String caminho);
	public String gerarNomeArquivo(String extensao, String tipoArquivo);
	public void arquivar(InputStream arquivo,  String nomeArquivo) throws IOException;
	public String getMimetypeArquivo(String extensao);
	public boolean ehImagem(String extensao);
}
