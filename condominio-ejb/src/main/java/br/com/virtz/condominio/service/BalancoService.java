package br.com.virtz.condominio.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.condominio.constantes.EnumTipoBalanco;
import br.com.virtz.condominio.dao.IBalancoDAO;
import br.com.virtz.condominio.dao.ICategoriaItemBalancoDAO;
import br.com.virtz.condominio.dao.ICondominioDAO;
import br.com.virtz.condominio.dao.IItemBalancoDAO;
import br.com.virtz.condominio.entidades.ArquivoBalanco;
import br.com.virtz.condominio.entidades.Balanco;
import br.com.virtz.condominio.entidades.CategoriaItemBalanco;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.ItemBalanco;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;

@Stateless
public class BalancoService implements IBalancoService {

	@EJB
	private ICondominioDAO condominioDAO;
	
	@EJB
	private IBalancoDAO balancoDAO;
	
	@EJB
	private IItemBalancoDAO itemBalancoDAO;
	
	@EJB
	private ICategoriaItemBalancoDAO categoriaDAO;
	
	

	@Override
	public Balanco salvar(Balanco balanco) throws AppException {
		if(balanco == null){
			throw new AppException("Balanço vazia");
		}
		if(balanco.getCondominio() == null || balanco.getAno() == null || balanco.getMes() == null){
			throw new AppException("Campos obrigatórios não preenchidos.");
		}
		
		try {
			Balanco b1 = recuperarBalancoPorCondominio(balanco.getCondominio().getId(), balanco.getAno(), balanco.getMes());
			if(b1 != null && !b1.getId().equals(balanco.getId())){
				throw new AppException("Já existe um balanço salvo para a competência "+balanco.getAno()+"/"+balanco.getMes()+".");
			}
			
			if(balanco.getData() == null){
				balanco.setData(new Date());
			}
			
			return balancoDAO.salvar(balanco);
		} catch (Exception e) {
			throw new AppException("Erro ao salvar");
		}
	}


	@Override
	public List<Balanco> recuperarBalancoPorCondominio(Long idCondominio) {
		return balancoDAO.recuperarPorCondominio(idCondominio);
	}


	@Override
	public Balanco recuperarBalancoPorCondominio(Long idCondominio, Integer ano, Integer mes) {
		return balancoDAO.recuperarPorCondominio(idCondominio, ano, mes);
	}


	@Override
	public Balanco recuperarBalanco(Long idBalanco) {
		return balancoDAO.recuperarPorId(idBalanco);
	}


	@Override
	public ItemBalanco salvarReceita(Long idBalanco, Double valor,
								String descricao, ArquivoBalanco arquivo, Usuario usuario, CategoriaItemBalanco categoria) throws AppException {
		if(idBalanco == null || valor == null || descricao == null){
			throw new AppException("Campos obrigatórios não preenchidos.");
		}
		
		ItemBalanco i = new ItemBalanco();
		i.setData(new Date());
		i.setArquivo(arquivo);
		i.setDescricao(descricao);
		i.setTipoBalanco(EnumTipoBalanco.RECEITA);
		i.setUsuario(usuario);
		i.setValor(valor);
		i.setCategoria(categoria);
		
		Balanco b = balancoDAO.recuperarPorId(idBalanco);
		i.setBalanco(b);
		
		try {
			return itemBalancoDAO.salvar(i);
		} catch (Exception e) {
			throw new AppException("Ocorreu um erro ao salvar a receita.");
		}
	}


	@Override
	public ItemBalanco salvarDespesa(Long idBalanco, Double valor,
								String descricao, ArquivoBalanco arquivo, Usuario usuario, CategoriaItemBalanco categoria) throws AppException {
		if(idBalanco == null || valor == null || descricao == null){
			throw new AppException("Campos obrigatórios não preenchidos.");
		}
		ItemBalanco i = new ItemBalanco();
		i.setData(new Date());
		i.setArquivo(arquivo);
		i.setDescricao(descricao);
		i.setTipoBalanco(EnumTipoBalanco.DESPESA);
		i.setUsuario(usuario);
		i.setValor(valor);
		i.setCategoria(categoria);
		
		Balanco b = balancoDAO.recuperarPorId(idBalanco);
		i.setBalanco(b);
		
		try {
			return itemBalancoDAO.salvar(i);
		} catch (Exception e) {
			throw new AppException("Ocorreu um erro ao salvar a despesa.");
		}
	}


	@Override
	public void removerItemBalanco(Long idItemBalanco) throws AppException {
		itemBalancoDAO.remover(idItemBalanco);
	}


	@Override
	public List<ItemBalanco> recuperarDespesas(Balanco balanco) throws AppException {
		List<ItemBalanco> balancos = new ArrayList<ItemBalanco>();
	
		if(balanco !=null){
			try {
				for(ItemBalanco i : balanco.getItens()){
					if(EnumTipoBalanco.DESPESA.equals(i.getTipoBalanco())){
						balancos.add(i);
					}
				}
			}catch(Exception e){
				return balancos;
			}
		}
		
		return balancos;
	}


	@Override
	public List<ItemBalanco> recuperarReceitas(Balanco balanco) throws AppException {
		List<ItemBalanco> receitas = new ArrayList<ItemBalanco>();
		
		if(balanco !=null){
			try {
				for(ItemBalanco i : balanco.getItens()){
					if(EnumTipoBalanco.RECEITA.equals(i.getTipoBalanco())){
						receitas.add(i);
					}
				}
			}catch(Exception e){
				return receitas;
			}
		}
		
		return receitas;
	}


	@Override
	public Double somarItens(List<ItemBalanco> itens) throws AppException {
		if(itens == null || itens.isEmpty()){
			return 0d;
		}
		BigDecimal v = new BigDecimal(0d);
		
		for(ItemBalanco i : itens){
			v = v.add(new BigDecimal(i.getValor()));
		}
		
		return v.setScale(2, RoundingMode.HALF_EVEN).doubleValue();
	}


	@Override
	public List<Balanco> recuperarPorCondominioComSomatorio(Long idCondominio) {
		return balancoDAO.recuperarPorCondominioComSomatorio(idCondominio);
	}


	@Override
	public ItemBalanco salvarItem(ItemBalanco item) throws AppException {
		try {
			return itemBalancoDAO.salvar(item);
		} catch (Exception e) {
			throw new AppException("Ocorreu um erro ao salvar o item");
		}
	}


	@Override
	public List<String> recuperarUltimasDescricoes(Long idCondominio, Integer ano, EnumTipoBalanco tipo) throws AppException {
		return itemBalancoDAO.recuperarUltimasDescricoes(idCondominio, ano, tipo, 50);
	}


	@Override
	public CategoriaItemBalanco salvarCategoriaItem(Long idCondominio, String nome, EnumTipoBalanco tipoBalanco) throws AppException {
		CategoriaItemBalanco categoria = categoriaDAO.recuperarPorCondominio(idCondominio, nome);
		if(categoria == null){
			categoria = new CategoriaItemBalanco();
			categoria.setAtiva(Boolean.TRUE);
			Condominio cond = new Condominio();
			cond.setId(idCondominio);
			categoria.setCondominio(cond);
			categoria.setNome(nome);
			categoria.setTipoBalanco(tipoBalanco);
			
			try {
				categoriaDAO.salvar(categoria);
			} catch (Exception e) {
				throw new AppException("Ocorreu um erro ao salvar a categoria.");
			}
		}
		return categoria;
	}


	@Override
	public CategoriaItemBalanco salvarCategoriaItem(CategoriaItemBalanco categoria) throws AppException {
		try {
			categoria = categoriaDAO.salvar(categoria);
		} catch (Exception e) {
			throw new AppException("Ocorreu um erro ao salvar a categoria.");
		}
		return categoria;
	}


	@Override
	public void inativarCategoriaItem(Long idCategoria) throws AppException {
		CategoriaItemBalanco categoria = categoriaDAO.recuperarPorId(idCategoria);
		if(categoria == null){
			throw new AppException("Não foi possível recuperar a categoria para inativá-la.");
		}
		categoria.setAtiva(Boolean.FALSE);
		try {
			categoria = categoriaDAO.salvar(categoria);
		} catch (Exception e) {
			throw new AppException("Ocorreu um erro ao inativar a categoria.");
		}
	}


	@Override
	public void ativarCategoriaItem(Long idCategoria) throws AppException {
		CategoriaItemBalanco categoria = categoriaDAO.recuperarPorId(idCategoria);
		if(categoria == null){
			throw new AppException("Não foi possível recuperar a categoria para ativá-la.");
		}
		categoria.setAtiva(Boolean.TRUE);
		try {
			categoria = categoriaDAO.salvar(categoria);
		} catch (Exception e) {
			throw new AppException("Ocorreu um erro ao ativar a categoria.");
		}
	}


	@Override
	public void removerCategoriaItem(Long idCategoria) throws AppException {
		categoriaDAO.remover(idCategoria);
	}


	@Override
	public List<CategoriaItemBalanco> recuperarCategoriaItemPorCondominio(Long idCondominio, EnumTipoBalanco tipoBalanco) {
		return categoriaDAO.recuperarPorCondominio(idCondominio, tipoBalanco);
	}


	@Override
	public List<CategoriaItemBalanco> recuperarCategoriaItemPorCondominio(Long idCondominio) {
		return categoriaDAO.recuperarPorCondominio(idCondominio);
	}

}
