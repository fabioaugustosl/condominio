package br.com.virtz.condominio.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.condominio.constantes.EnumTaxaCreditoDebito;
import br.com.virtz.condominio.constantes.EnumTaxaPorcentagemValor;
import br.com.virtz.condominio.dao.ICobrancaUsuarioDAO;
import br.com.virtz.condominio.dao.ICondominioDAO;
import br.com.virtz.condominio.dao.IConfiguracaoBoletoDAO;
import br.com.virtz.condominio.dao.ITaxaDAO;
import br.com.virtz.condominio.dao.IUsuarioDAO;
import br.com.virtz.condominio.entidades.CobrancaUsuario;
import br.com.virtz.condominio.entidades.ConfiguracaoBoleto;
import br.com.virtz.condominio.entidades.Taxa;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;

@Stateless
public class FinanceiroService implements IFinanceiroService {

	@EJB
	private ICondominioDAO condominioDAO;
	
	@EJB
	private ITaxaDAO taxaDAO;

	@EJB
	private ICobrancaUsuarioDAO cobrancaDAO;
	
	@EJB
	private IConfiguracaoBoletoDAO configuracaoDAO;
	
	@EJB
	private IUsuarioDAO usuarioDAO;
	
	
	@Override
	public Taxa salvar(Taxa taxa) throws AppException {
		if(taxa == null){
			throw new AppException("Taxa vazia");
		}
		if(taxa.getCondominio() == null || taxa.getValor() == null){
			throw new AppException("Campos obrigatórios não preenchidos.");
		}
		if(taxa.getCreditoOuDebito() == null){
			taxa.setCreditoOuDebito(EnumTaxaCreditoDebito.C);
		}
		if(taxa.getPorcentagemOuValor() == null){
			taxa.setPorcentagemOuValor(EnumTaxaPorcentagemValor.V);
		}
		try {
			return taxaDAO.salvar(taxa);
		} catch (Exception e) {
			throw new AppException("Erro ao salvar");
		}
	}

	
	@Override
	public List<Taxa> recuperarTaxaPorCondominio(Long idCondominio) {
		return taxaDAO.recuperarPorCondominio(idCondominio);
	}

	
	@Override
	public Taxa recuperarTaxa(Long idTaxa) {
		return taxaDAO.recuperarPorId(idTaxa);
	}


	@Override
	public void removerTaxa(Long idTaxa) throws AppException{
		taxaDAO.remover(idTaxa);
	}


	@Override
	public ConfiguracaoBoleto salvar(ConfiguracaoBoleto configuracao) throws AppException {
		if(configuracao == null){
			throw new AppException("Configuração vazia");
		}
		if(configuracao.getCondominio() == null || configuracao.getAno() == null || configuracao.getMes() == null){
			throw new AppException("Campos obrigatórios não preenchidos.");
		}
		if(configuracao.getBloquearEdicao() != null && configuracao.getBloquearEdicao()){
			throw new AppException("A configuração está bloqueada para edição. ");
		}
		
		try {
			return configuracaoDAO.salvar(configuracao);
		} catch (Exception e) {
			throw new AppException("Erro ao salvar");
		}
	}


	@Override
	public List<ConfiguracaoBoleto> recuperarConfiguracaoBoleto(Long idConfiguracaoBoleto) {
		return configuracaoDAO.recuperarPorCondominio(idConfiguracaoBoleto);
	}


	@Override
	public ConfiguracaoBoleto recuperarConfiguracaoPorCondominio(Long idCondominio, Integer ano, Integer mes) {
		return configuracaoDAO.recuperarPorCondominio(idCondominio, ano, mes);
	}


	@Override
	public CobrancaUsuario salvar(CobrancaUsuario cobranca) throws AppException {
		if(cobranca == null){
			throw new AppException("Cobrança vazia");
		}
		if(cobranca.getValor() == null || cobranca.getAno() == null || cobranca.getMes() == null){
			throw new AppException("Campos obrigatórios não preenchidos.");
		}
		
		try {
			return cobrancaDAO.salvar(cobranca);
		} catch (Exception e) {
			throw new AppException("Erro ao salvar");
		}
	}


	@Override
	public List<CobrancaUsuario> recuperarCobrancas(Long idCondominio, Integer ano, Integer mes) {
		return cobrancaDAO.recuperarPorCondominio(idCondominio, ano, mes);
	}
	
	
	@Override
	public List<CobrancaUsuario> recuperarCobrancasDeAvulsos(Long idCondominio, Integer ano, Integer mes) {
		return cobrancaDAO.recuperarCobrancasDeAvulsosPorCondominio(idCondominio, ano, mes);
	}


	@Override
	public CobrancaUsuario recuperarCobranca(Long idCondominio, Long idUsuario, Integer ano, Integer mes) {
		return cobrancaDAO.recuperar(idCondominio, idUsuario, ano, mes);
	}


	@Override
	public List<CobrancaUsuario> gerarCobrancas(ConfiguracaoBoleto configuracao) throws AppException {
		if(configuracao == null || configuracao.getCondominio() == null){
			throw new AppException("Configuração inválida");
		}
		List<Usuario> usuarios = usuarioDAO.recuperarTodos(configuracao.getCondominio().getId());
		if(usuarios == null || usuarios.isEmpty()){
			return null;
		}
		
		List<CobrancaUsuario> cobrancas = cobrancaDAO.recuperarPorCondominio(configuracao.getCondominio().getId(), configuracao.getAno(), configuracao.getMes());
		
		if(cobrancas == null){
			cobrancas = new ArrayList<CobrancaUsuario>();
		}
		
		for(Usuario u : usuarios){
			CobrancaUsuario c = recuperarCobrancaUsuario(cobrancas, u);
			if(c == null){
				c = new CobrancaUsuario();
				c.setAno(configuracao.getAno());
				c.setMes(configuracao.getMes());
				c.setUsuario(u);
			}
			c.setConfiguracaoBoleto(configuracao);
			c.setValor(configuracao.getValorBase());
			
			try {
				cobrancaDAO.salvar(c);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}


	private CobrancaUsuario recuperarCobrancaUsuario(List<CobrancaUsuario> cobrancas, Usuario usuario) {
		for(CobrancaUsuario cobranca : cobrancas){
			if(cobranca.getUsuario().equals(usuario)){
				return cobranca;
			}
		}
		return null;
	}
	
	

	@Override
	public List<String> recuperarAnosMesesDispiniveis(Long idCondominio) {
		List<String> anosMeses = new ArrayList<String>();
		
		List<Object[]> ret = cobrancaDAO.recuperarAnosMeses(idCondominio);
		if(ret != null){
			for(Object[] o : ret){
				anosMeses.add(o[0]+"/"+o[1]);
			}
		}
		
		return anosMeses;
	}


	@Override
	public List<CobrancaUsuario> recuperarCobrancasUsuario(Long idCondominio, Long idUsuario) {
		return cobrancaDAO.recuperar(idCondominio, idUsuario);
	}


	@Override
	public CobrancaUsuario atualizarCobranca(Long idCobranca,
			String nossoNumero,String digitoNossoNumero, String codigoBarras) throws AppException {
		CobrancaUsuario c = cobrancaDAO.recuperarPorId(idCobranca);
		c.setNossoNumero(nossoNumero);
		c.setCodigoBarras(codigoBarras);
		c.setDigitoNossoNumero(digitoNossoNumero);
		try {
			return cobrancaDAO.salvar(c);
		} catch (Exception e) {
			throw new AppException("Erro ao atualizar cobrança");
		}
	}


	
}
