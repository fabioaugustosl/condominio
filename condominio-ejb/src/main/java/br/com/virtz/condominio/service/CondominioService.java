package br.com.virtz.condominio.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.condominio.constantes.EnumBanco;
import br.com.virtz.condominio.dao.IAreaComumDAO;
import br.com.virtz.condominio.dao.IBlocoDAO;
import br.com.virtz.condominio.dao.ICidadeDAO;
import br.com.virtz.condominio.dao.ICondominioDAO;
import br.com.virtz.condominio.dao.IContaBancariaDAO;
import br.com.virtz.condominio.entidades.AreaComum;
import br.com.virtz.condominio.entidades.Bloco;
import br.com.virtz.condominio.entidades.Cidade;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.ContaBancariaCondominio;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;

@Stateless
public class CondominioService implements ICondominioService {

	@EJB
	private ICondominioDAO condominioDAO;
	
	@EJB
	private ICidadeDAO cidadeDAO;
	
	@EJB
	private IBlocoDAO blocoDAO;
	
	@EJB
	private IContaBancariaDAO contaBancariaDAO;
	
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
		//TODO : validar se a area possui alguma reserva para ela.
		
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

	@Override
	public Bloco recuperarBloco(Long idBloco) {
		return blocoDAO.recuperarBlocoCompleto(idBloco);
	}
	
	
	@Override
	public ContaBancariaCondominio recuperarContaBancariaCondominioPrincipal(Long idCondominio) {
		List<ContaBancariaCondominio> contas =  contaBancariaDAO.recuperarPorCondominio(idCondominio);
		if(contas == null || contas.isEmpty()){
			return null;
		}
		return contas.get(0);
	}

	@Override
	public void salvarContaBancariaCondominioPrincipal(
			Condominio condominio, EnumBanco banco, String agencia,
			String digitoAgencia, String codigoCarteira) throws AppException {
		ContaBancariaCondominio conta = this.recuperarContaBancariaCondominioPrincipal(condominio.getId());
		if(conta == null){
			conta = new ContaBancariaCondominio();
			conta.setCondominio(condominio);
		}
		conta.setAgencia(agencia);
		conta.setBanco(banco);
		conta.setDigitoAgencia(digitoAgencia);
		conta.setCodigoCarteira(codigoCarteira);
		
		try {
			contaBancariaDAO.salvar(conta);
		} catch (Exception e) {
			throw new AppException("Erro ao salvar");
		}
	}
	
	@Override
	public void salvarContaBancariaCondominioPrincipal(ContaBancariaCondominio conta) throws AppException {
		if(conta == null){
			return;
		}
		
		try {
			contaBancariaDAO.salvar(conta);
		} catch (Exception e) {
			throw new AppException("Erro ao salvar");
		}
	}


}
