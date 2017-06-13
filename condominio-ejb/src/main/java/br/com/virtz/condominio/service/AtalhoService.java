package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.condominio.constantes.EnumFuncionalidadeAtalho;
import br.com.virtz.condominio.dao.IAtalhoDAO;
import br.com.virtz.condominio.entidades.Atalho;
import br.com.virtz.condominio.entidades.Condominio;

@Stateless
public class AtalhoService implements IAtalhoService {

	@EJB
	private IAtalhoDAO atalhoDAO;


	@Override
	public Atalho salvar(Atalho atalho) throws Exception {
		
		if(atalho == null){
			return null;
		}
		
		if(atalho.getId() == null){
			Atalho a = atalhoDAO.recuperar(atalho.getCondominio().getId(), atalho.getFuncionalidade());
			if(a != null){
				throw new Exception("Já exite um atalho para essa funcionalidade");
			}
		}
		
		return atalhoDAO.salvar(atalho);
	}


	@Override
	public List<Atalho> recuperarTodos(Condominio condominio) {
		if(condominio != null){
			return atalhoDAO.recuperar(condominio.getId());
		}
		return null;
	}


	@Override
	public Atalho recuperarTodos(Condominio condominio, EnumFuncionalidadeAtalho funcionalidade) {
		if(condominio != null && funcionalidade != null){
			return atalhoDAO.recuperar(condominio.getId(), funcionalidade);
		}
		return null;
	}


	@Override
	public void remover(Long id) {
		atalhoDAO.remover(id);
	}


}
