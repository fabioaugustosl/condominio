package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.constantes.EnumTipoBalanco;
import br.com.virtz.condominio.entidades.ArquivoBalanco;
import br.com.virtz.condominio.entidades.Balanco;
import br.com.virtz.condominio.entidades.CategoriaItemBalanco;
import br.com.virtz.condominio.entidades.ItemBalanco;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;

@Local
public interface IBalancoService {
	
	// Balanço
	public Balanco salvar(Balanco balanco) throws AppException;
	public List<Balanco> recuperarBalancoPorCondominio(Long idCondominio);
	public List<Balanco> recuperarPorCondominioComSomatorio(Long idCondominio);
	public Balanco recuperarBalancoPorCondominio(Long idCondominio, Integer ano, Integer mes);
	public Balanco recuperarBalanco(Long idBalanco);
	public List<ItemBalanco> recuperarDespesas(Balanco balanco) throws AppException;
	public List<ItemBalanco> recuperarReceitas(Balanco balanco) throws AppException;
	
	
	// Categoria itemBalanco
	public CategoriaItemBalanco salvarCategoriaItem(Long idCondominio, String nome, EnumTipoBalanco tipoBalanco) throws AppException;
	public CategoriaItemBalanco salvarCategoriaItem(CategoriaItemBalanco categoria) throws AppException;
	public void inativarCategoriaItem(Long idCategoria) throws AppException;
	public void ativarCategoriaItem(Long idCategoria) throws AppException;
	public void removerCategoriaItem(Long idCategoria) throws AppException;
	public List<CategoriaItemBalanco> recuperarCategoriaItemPorCondominio(Long idCondominio, EnumTipoBalanco tipoBalanco);
	public List<CategoriaItemBalanco> recuperarCategoriaItemPorCondominio(Long idCondominio);

	
	// Despesas / Receitas
	public ItemBalanco salvarReceita(Long idBalanco, Double valor, String descricao, ArquivoBalanco arquivo, Usuario usuario, CategoriaItemBalanco categoria)  throws AppException;
	public ItemBalanco salvarDespesa(Long idBalanco, Double valor, String descricao, ArquivoBalanco arquivo, Usuario usuario, CategoriaItemBalanco categoria)  throws AppException;
	public ItemBalanco salvarItem(ItemBalanco item) throws AppException;
	public void removerItemBalanco(Long idItemBalanco) throws AppException;
	public Double somarItens(List<ItemBalanco> itens) throws AppException;
	public List<String> recuperarUltimasDescricoes(Long idCondominio, Integer ano, EnumTipoBalanco tipo) throws AppException;
	
	
}
