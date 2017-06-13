package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.ArquivoNoticia;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Noticia;
import br.com.virtz.condominio.exception.ErroAoSalvar;

@Local
public interface INoticiaService {
	
	public Noticia criarNovaNoticia(Condominio condominio, String titulo, String conteudo);
	public ArquivoNoticia criarNovoArquivo(String nome, String extensao, Noticia noticia);
	public void salvarNoticia(Noticia noticia) throws ErroAoSalvar;
	public List<Noticia> recuperarNoticias(Long idCondominio);
	public Noticia recuperarNoticia(Long idNoticia);
	public void remover(Long idNoticia);
	public void removerArquivo(Long idArquivo);
	public List<Noticia> recuperarNoticiasAtivas(Long idCondominio);
	public List<Noticia> recuperarNoticiasAtivas(Long idCondominio, Integer totalRegistros);
	
}

