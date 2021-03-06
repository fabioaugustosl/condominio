package br.com.virtz.condominio.util;

import java.io.IOException;
import java.io.InputStream;

import org.primefaces.model.StreamedContent;

import br.com.virtz.condominio.exceptions.CondominioException;

public interface IArquivosUtil {
	public StreamedContent exibir(String img);
	public String getCaminhaPastaTemplatesEmail();
	public String getCaminhoArquivosUpload();
	public String getCaminhoBaseArquivos();
	public String pegarExtensao(String caminho);
	public String converterCaminhoImagemWindows(String caminho);
	public String gerarNomeArquivo(String extensao, String tipoArquivo);
	public void arquivar(InputStream arquivo,  String nomeArquivo) throws IOException;
	public void arquivar(InputStream arquivo, String pasta, String nomeArquivo) throws IOException;
	public String getMimetypeArquivo(String extensao);
	public boolean ehImagem(String extensao);
	public boolean tamanhoImagemEhValido(InputStream arquivo, int larguraMinima, int alturaMinima) throws CondominioException;
	public void redimensionarImagem(InputStream arquivo, String pasta, String nomeArquivo, String extensao, int larguraMaxima, int alturaMaxima) throws IOException;
	public void redimensionarImagem(InputStream arquivo, String pasta, String nomeArquivo, String extensao, int larguraMaxima, int alturaMaxima, int larguraMinima, int alturaMinima) throws IOException;
	public void copiarArquivos(String nomeArquivo) throws IOException;
	public String getCaminhoUploadArquivosTemporario();
	public String getCaminhoBaseArquivosTemporario();
	public void gravarThumb(String nomeArquivo);
	public String getCaminhoCompletoThumb(String nomeArq);
	public String gerarNomeAleatorio(String prefixo);
	public byte[] converter(String arquivo);
     
}
