package br.com.virtz.condominio.service;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.ArquivoNoticia;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Noticia;

@Local
public interface INoticiaService {
	
	public Noticia criarNovaNoticia(Condominio condominio, String titulo, String conteudo);
	public ArquivoNoticia criarNovoArquivo(String nome, String extensao, Noticia noticia);
	public void salvarNoticia(Noticia noticia);
	public void salvarArquivo(ArquivoNoticia arquivo);
	
}

