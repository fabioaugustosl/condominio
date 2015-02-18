package br.com.virtz.condominio.util;

import java.io.IOException;
import java.io.InputStream;

import br.com.virtz.condominio.exceptions.CondominioException;

public interface IArquivosUtil {
	public String getCaminhaPastaTemplatesEmail();
	public String getCaminhoArquivosUpload();
	public String pegarExtensao(String caminho);
	public String gerarNomeArquivo(String extensao, String tipoArquivo);
	public void arquivar(InputStream arquivo,  String nomeArquivo) throws IOException;
	public String getMimetypeArquivo(String extensao);
	public boolean ehImagem(String extensao);
	public boolean tamanhoImagemEhValido(InputStream arquivo, int larguraMinima, int alturaMinima) throws CondominioException;
	public void redimensionarImagem(InputStream arquivo, String nomeArquivo, String extensao, int larguraMaxima, int alturaMaxima) throws IOException;
}
