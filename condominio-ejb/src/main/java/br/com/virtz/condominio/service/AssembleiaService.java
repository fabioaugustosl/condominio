package br.com.virtz.condominio.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang.StringUtils;

import br.com.virtz.boleto.util.DataUtil;
import br.com.virtz.condominio.dao.IAssembleiaDAO;
import br.com.virtz.condominio.dao.IPautaDAO;
import br.com.virtz.condominio.entidades.Assembleia;
import br.com.virtz.condominio.entidades.PautaAssembleia;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.ErroAoSalvar;

@Stateless
public class AssembleiaService implements IAssembleiaService {

	@EJB
	private IAssembleiaDAO assembleiaDAO;
	
	@EJB
	private IPautaDAO pautaDAO;
	
	
	

	@Override
	public Assembleia salvar(Assembleia assembleia) throws ErroAoSalvar {
		try {
			if(assembleia.getArquivoAta() != null 
					&& StringUtils.isBlank(assembleia.getArquivoAta().getNome())){
				assembleia.setArquivoAta(null);
			}
			return assembleiaDAO.salvar(assembleia);
		} catch (Exception e) {
			throw new ErroAoSalvar("Ocorreu um erro ao salvar a assembleia.", assembleia);
		}
	}

	@Override
	public List<Assembleia> recuperarTodos(Long idCondominio) {
		return assembleiaDAO.recuperar(idCondominio);
	}
	
	@Override
	public List<Assembleia> recuperarAssembleiasNaoRealizadas(Long idCondominio) {
		return assembleiaDAO.recuperarNaoRealizadas(idCondominio);
	}

	@Override
	public void removerPauta(Long idPauta) {
		pautaDAO.remover(idPauta);		
	}

	@Override
	public void removerAssembleia(Long id) {
		assembleiaDAO.remover(id);
	}

	@Override
	public Assembleia recuperar(Long idAssembleia) {
		return assembleiaDAO.recuperarPorId(idAssembleia);
	}

	@Override
	public PautaAssembleia novaPauta(Long idAssembleia, String textoPauta, Usuario usuario) throws ErroAoSalvar {
		Assembleia a = assembleiaDAO.recuperarPorId(idAssembleia);
		PautaAssembleia pauta = new PautaAssembleia();
		pauta.setAprovada(false);
		pauta.setAssembleia(a);
		pauta.setMensagem(textoPauta);
		pauta.setUsuario(usuario);
		
		if(a.getPautas() == null){
			a.setPautas(new ArrayList<PautaAssembleia>());
		}
		a.getPautas().add(pauta);
		try {
			assembleiaDAO.salvar(a);
			return a.getPautas().get(a.getPautas().size()-1);
		} catch (Exception e) {
			throw new ErroAoSalvar("Aconteceu um erro ao salvar nova pauta.", pauta);
		}
	}

	@Override
	public Assembleia recuperarUltimaAssembleiasRealizadas(Long idCondominio) {
		return assembleiaDAO.recuperarUltimaAssembleia(idCondominio);
	}

	@Override
	public PautaAssembleia recuperarPautaPorId(Long idPauta) {
		return pautaDAO.recuperarPorId(idPauta);
	}

	@Override
	public PautaAssembleia salvarPauta(PautaAssembleia pauta) throws ErroAoSalvar {
		try {
			return pautaDAO.salvar(pauta);
		} catch (Exception e) {
			throw new ErroAoSalvar("Ocorreu um erro ao salvar a pauta.", pauta);
		}
	}

	
	/**
	 * Recupera assembleias sem ata a mais de 48 horas.
	 */
	@Override
	public List<Assembleia> recuperarAssembleiasRealizadasSemAta() {
		List<Assembleia> assembleias = assembleiaDAO.recuperarRealizadasSemAta();
		List<Assembleia> assembleiasRetorno = new ArrayList<Assembleia>();
		
		if(assembleias != null && !assembleias.isEmpty()){
			DataUtil util = new DataUtil();
			for(Assembleia a : assembleias){
				int dias = util.diasEntreDatas(a.getData(), new Date());
				if(Math.abs(dias) > 2){
					assembleiasRetorno.add(a);
				}
			}
			
		}
		return assembleiasRetorno;
	}

	
	@Override
	public List<Assembleia> recuperarAssembleiasQueSeraoRealizadasDaqui3dias() {
		List<Assembleia> assembleias = assembleiaDAO.recuperarNaoRealizadas();
		List<Assembleia> assembleiasRetorno = new ArrayList<Assembleia>();
		
		if(assembleias != null && !assembleias.isEmpty()){
			DataUtil util = new DataUtil();
			for(Assembleia a : assembleias){
				int dias = util.diasEntreDatas( new Date(), a.getData());
				if(Math.abs(dias) <= 3 && !a.isAvisoDeAssembleiaAutomatico()){
					assembleiasRetorno.add(a);
				}
			}
			
		}
		return assembleiasRetorno;
	}
	

}
