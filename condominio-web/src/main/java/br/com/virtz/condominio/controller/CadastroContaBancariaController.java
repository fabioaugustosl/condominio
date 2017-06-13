package br.com.virtz.condominio.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import br.com.virtz.boleto.bean.EnumBanco;
import br.com.virtz.boleto.validador.FabricaValidadorDadosBancarios;
import br.com.virtz.boleto.validador.ValidadorDadosBancarios;
import br.com.virtz.condominio.boleto.conversor.ConversorDadosBoleto;
import br.com.virtz.condominio.entidades.ContaBancariaCondominio;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class CadastroContaBancariaController {
	
	@EJB
	private ICondominioService condominioService;

	
	@Inject
	private ConversorDadosBoleto conversorBoleto;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private MessageHelper message;
	
	@Inject
	private NavigationPage navigation;
	
	
	
	private Usuario usuario = null;
	
	private String banco;  
	private Map<String,String> bancos = new HashMap<String, String>();
	
	private ContaBancariaCondominio conta;
	  
	
	
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
				
		for(EnumBanco b : EnumBanco.values()){
			bancos.put( b.getDescricao(), b.getCodigo());
		}
		
		conta = condominioService.recuperarContaBancariaCondominioPrincipal(usuario.getCondominio().getId());
		if(conta == null){
			conta = new ContaBancariaCondominio();
			conta.setCondominio(usuario.getCondominio());
		} else {
			banco = conta.getBanco().getCodigo();
		}
		 
	}
	
	
	public void salvar(ActionEvent event) throws CondominioException {
		try{
			EnumBanco enumBanco = EnumBanco.recuperarPorCodigo(banco);
			conta.setBanco(enumBanco);
			
			ValidadorDadosBancarios validador = FabricaValidadorDadosBancarios.recuperarFormatador(conta.getBanco());
			
			List<String> erros = validador.validar(conversorBoleto.criarConta(conta));
			
			if(erros != null && !erros.isEmpty()){
				for(String e : erros){
					message.addError(e);
				}
				return;
			}
			
			conta = condominioService.salvarContaBancariaCondominioPrincipal(conta);
			
			message.addInfo("A conta bancária foi salva com sucesso.");

		}catch(Exception e){
			e.printStackTrace();
			throw new CondominioException("Ocorreu um erro inesperado ao salvar a conta bancaria.");
		}
	}
	
	public void voltar(){
		navigation.redirectToPage("/financeiro/gerenciarFinanceiro.faces");
	}
	
	
	/* GETTERS e SETTERS*/
	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public Map<String, String> getBancos() {
		return bancos;
	}

	public void setBancos(Map<String, String> bancos) {
		this.bancos = bancos;
	}

	public ContaBancariaCondominio getConta() {
		return conta;
	}

	public void setConta(ContaBancariaCondominio conta) {
		this.conta = conta;
	}
	
	
}
