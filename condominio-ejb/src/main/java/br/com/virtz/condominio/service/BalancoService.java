package br.com.virtz.condominio.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.condominio.constantes.EnumTipoBalanco;
import br.com.virtz.condominio.dao.IBalancoDAO;
import br.com.virtz.condominio.dao.ICondominioDAO;
import br.com.virtz.condominio.dao.IItemBalancoDAO;
import br.com.virtz.condominio.entidades.ArquivoBalanco;
import br.com.virtz.condominio.entidades.Balanco;
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
								String descricao, ArquivoBalanco arquivo, Usuario usuario) throws AppException {
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
								String descricao, ArquivoBalanco arquivo, Usuario usuario) throws AppException {
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


	
}
