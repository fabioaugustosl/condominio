package br.com.virtz.condominio.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.condominio.dao.IArquivoNoticiaDAO;
import br.com.virtz.condominio.dao.INoticiaDAO;
import br.com.virtz.condominio.entidades.ArquivoNoticia;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Noticia;

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
	public void salvarNoticia(Noticia noticia) {
		try {
			noticiaDAO.salvar(noticia);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void salvarArquivo(ArquivoNoticia arquivo) {
		try {
			arquivoDAO.salvar(arquivo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
