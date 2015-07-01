package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.ArquivoIndicacao;
import br.com.virtz.condominio.entidades.CategoriaServicoProduto;
import br.com.virtz.condominio.entidades.Indicacao;
import br.com.virtz.condominio.exception.ErroAoSalvar;

@Local
public interface IIndicacaoService {
	
//	public Noticia criarNovaNoticia(Condominio condominio, String titulo, String conteudo);
	public ArquivoIndicacao criarNovoArquivo(String nome, String extensao, Indicacao indicacao);
	public void salvarIndicacao(Indicacao indicacao) throws ErroAoSalvar;
	public List<Indicacao> recuperarIndicacoes(Long idCondominio);
	public List<Indicacao> recuperarIndicacoesRecentes(Long idCondominio, Long quantidadeIndicacoes);
	public List<Indicacao> recuperarIndicacoesPorCategoria(Long idCondominio, Long idCategoria);
	public Indicacao recuperarIndicacao(Long idIndicacao);
	public void remover(Long idIndicacao);
	public void removerArquivo(Long idArquivo);
	
	// categorias
	public List<CategoriaServicoProduto> recuperarTodasCategorias();
	public List<CategoriaServicoProduto> recuperarTodasCategoriasComQuantidade();
	
}

