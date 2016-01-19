package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.condominio.dao.IArquivoNoticiaDAO;
import br.com.virtz.condominio.dao.INoticiaDAO;
import br.com.virtz.condominio.entidades.ArquivoNoticia;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Noticia;
import br.com.virtz.condominio.exception.ErroAoSalvar;

@Stateless
public class NoticiaService implements INoticiaService {

	@EJB
	private INoticiaDAO noticiaDAO;
	
	@EJB
	private IArquivoNoticiaDAO arquivoDAO;

	
	
	@Override
	public Noticia criarNovaNoticia(Condominio condominio, String titulo, String conteudo) {
		Noticia noticia = new Noticia();
		noticia.setCondominio(condominio);
		noticia.setTitulo(titulo);
		noticia.setConteudo(conteudo);
		return noticia;
	}
	

	@Override
	public ArquivoNoticia criarNovoArquivo(String nome, String extensao,Noticia noticia) {
		ArquivoNoticia arquivo = new ArquivoNoticia();
		arquivo.setExtensao(extensao);
		arquivo.setNome(nome);
		arquivo.setNoticia(noticia);
		return arquivo;
	}

	
	@Override
	public void salvarNoticia(Noticia noticia) throws ErroAoSalvar {
		try {
			noticiaDAO.salvar(noticia);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ErroAoSalvar("Ocorreu uma falha ao salvar a notícia. Favor tente novamente.", noticia);
		}
	}

	
	@Override
	public List<Noticia> recuperarNoticias(Long idCondominio) {
		return noticiaDAO.recuperar(idCondominio);
	}
	
	@Override
	public List<Noticia> recuperarNoticiasAtivas(Long idCondominio) {
		return noticiaDAO.recuperarAtivas(idCondominio, null);
	}
	
	@Override
	public List<Noticia> recuperarNoticiasAtivas(Long idCondominio, Integer totalRegistros) {
		return noticiaDAO.recuperarAtivas(idCondominio, totalRegistros);
	}

	@Override
	public Noticia recuperarNoticia(Long idNoticia) {
		return noticiaDAO.recuperarPorId(idNoticia);
	}
	

	@Override
	public void remover(Long idNoticia) {
		noticiaDAO.remover(idNoticia);
	}


	@Override
	public void removerArquivo(Long idArquivo) {
		arquivoDAO.remover(idArquivo);
		
	}


}
