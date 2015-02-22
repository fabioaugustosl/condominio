package br.com.virtz.condominio.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.condominio.dao.IAreaComumDAO;
import br.com.virtz.condominio.dao.IBlocoDAO;
import br.com.virtz.condominio.dao.ICidadeDAO;
import br.com.virtz.condominio.dao.ICondominioDAO;
import br.com.virtz.condominio.entidades.AreaComum;
import br.com.virtz.condominio.entidades.Bloco;
import br.com.virtz.condominio.entidades.Cidade;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Usuario;

@Stateless
public class CondominioService implements ICondominioService {

	@EJB
	private ICondominioDAO condominioDAO;
	
	@EJB
	private ICidadeDAO cidadeDAO;
	
	@EJB
	private IBlocoDAO blocoDAO;

	
	@EJB
	private IAreaComumDAO areaComumDAO;

	@Override
	public List<Condominio> recuperarTodos() {
		return condominioDAO.recuperarTodos();
	}

	@Override
	public void remover(Long id) {
		condominioDAO.remover(id);
	}

	@Override
	public Condominio salvar(Condominio condominio) throws Exception {
		return condominioDAO.salvar(condominio);
	}

	@Override
	public Bloco salvarBloco(Bloco bloco) throws Exception {
		return blocoDAO.salvar(bloco);
	}

	@Override
	public void removerBloco(Long id) {
		blocoDAO.remover(id);
	}

	@Override
	public List<Bloco> recuperarTodosBlocos() {
		return blocoDAO.recuperarTodos();
	}

	@Override
	public Condominio recuperarCondominioCompleto(Usuario usuario) {
		return condominioDAO.recuperarCondominioCompleto(usuario);
	}

	@Override
	public AreaComum salvarAreaComum(AreaComum area) throws Exception {
		return areaComumDAO.salvar(area);
	}

	@Override
	public void removerAreaComum(Long id) {
		areaComumDAO.remover(id);
	}

	@Override
	public List<Condominio> recuperarPorCidade(Long idCidade) {
		return condominioDAO.recuperarPorCidade(idCidade);
	}

	@Override
	public List<Cidade> cidadesQuePossuemCondominioCadastrado() {
		return cidadeDAO.recuperarCidadesComCondominiosCadastrados();
	}

	@Override
	public List<Bloco> recuperarTodosBlocosComApartamentos(Long idCondominio) {
		return blocoDAO.recuperarComApartamentos(idCondominio);
	}
	
	@Override
	public List<Bloco> sugerirBlocos(int quantidadeBlocos, Condominio condominio){
		List<Bloco> blocos = new ArrayList<Bloco>();
		for(int i=0; i<quantidadeBlocos; i++){
			Bloco b = new Bloco();
			b.setNome("Bloco "+(i+1));
			b.setQuantidadeAndares(4);
			b.setCondominio(condominio);
			blocos.add(b);
		}
		return blocos;
	}


}
