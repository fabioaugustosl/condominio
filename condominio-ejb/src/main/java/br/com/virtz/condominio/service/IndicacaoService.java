package br.com.virtz.condominio.service;

import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.condominio.dao.IArquivoIndicacaoDAO;
import br.com.virtz.condominio.dao.ICategoriaServicoProdutoDAO;
import br.com.virtz.condominio.dao.IIndicacaoDAO;
import br.com.virtz.condominio.entidades.ArquivoIndicacao;
import br.com.virtz.condominio.entidades.CategoriaServicoProduto;
import br.com.virtz.condominio.entidades.Indicacao;
import br.com.virtz.condominio.exception.ErroAoSalvar;

@Stateless
public class IndicacaoService implements IIndicacaoService {

	@EJB
	private IIndicacaoDAO indicacaoDAO;
	
	@EJB
	private IArquivoIndicacaoDAO arquivoDAO;

	@EJB
	private ICategoriaServicoProdutoDAO categoriaDAO;
	


	@Override
	public void remover(Long idIndicacao) {
		indicacaoDAO.remover(idIndicacao);
	}


	@Override
	public void removerArquivo(Long idArquivo) {
		arquivoDAO.remover(idArquivo);
	}

	
	@Override
	public ArquivoIndicacao criarNovoArquivo(String nome, String extensao, Indicacao indicacao) {
		ArquivoIndicacao arquivo = new ArquivoIndicacao();
		arquivo.setExtensao(extensao);
		arquivo.setNome(nome);
		arquivo.setIndicacao(indicacao);
		return arquivo;
	}


	@Override
	public void salvarIndicacao(Indicacao indicacao) throws ErroAoSalvar {
		try {
			indicacaoDAO.salvar(indicacao);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ErroAoSalvar("Ocorreu uma falha ao salvar a indicação. Favor tente novamente.", indicacao);
		}
	}


	@Override
	public List<Indicacao> recuperarIndicacoes(Long idCondominio) {
		return indicacaoDAO.recuperar(idCondominio);
	}


	@Override
	public Indicacao recuperarIndicacao(Long idIndicacao) {
		return indicacaoDAO.recuperarPorId(idIndicacao);
	}


	@Override
	public List<CategoriaServicoProduto> recuperarTodasCategorias() {
		List<CategoriaServicoProduto> categorias =  categoriaDAO.recuperarTodos();
		Collections.sort(categorias);
		return categorias;
	}


	@Override
	public List<CategoriaServicoProduto> recuperarTodasCategoriasComQuantidade() {
		return categoriaDAO.recuperarComQuantidade();
	}


	@Override
	public List<Indicacao> recuperarIndicacoesPorCategoria(Long idCondominio, Long idCategoria) {
		return indicacaoDAO.recuperarPorCategoria(idCondominio, idCategoria);
	}


	@Override
	public List<Indicacao> recuperarIndicacoesRecentes(Long idCondominio,Long quantidadeIndicacoes) {
		int quantidade = 9;
		if(quantidadeIndicacoes != null){
			quantidade = quantidadeIndicacoes.intValue();
		}
		return indicacaoDAO.recuperar(idCondominio, quantidade);
	}


}
