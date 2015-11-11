package br.com.virtz.condominio.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.primefaces.model.StreamedContent;

import br.com.virtz.condominio.entidades.CobrancaUsuario;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.service.IFinanceiroService;
import br.com.virtz.condominio.session.SessaoUsuario;

@ManagedBean
@ViewScoped
public class GeracaoBoletoController {

	@EJB
	private IFinanceiroService financeiroService;	
	
	@Inject
	private DownloadBoletoController downloadController;
	
	
	@Inject
	private SessaoUsuario sessao;
	
	
	private List<CobrancaUsuario> cobrancas = null;
	private List<CobrancaUsuario> cobrancasSelecionadas = null;
	private Usuario usuario = null;
	
	private List<String> anosMeses = null;
	private String anoMes= null;

	
		
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		anosMeses  = financeiroService.recuperarAnosMesesDispiniveis(usuario.getCondominio().getId());
		if(anosMeses != null && !anosMeses.isEmpty()){
			anoMes = anosMeses.get(0);
		}
		cobrancasSelecionadas= new ArrayList<CobrancaUsuario>();
	}
	

	public void listarUsuarios(){
		cobrancas = new ArrayList<CobrancaUsuario>();
		if(StringUtils.isNotBlank(anoMes)){
			String[] anoMesSelecionado = anoMes.split("/");
			cobrancas =  financeiroService.recuperarCobrancas(usuario.getCondominio().getId(), Integer.parseInt(anoMesSelecionado[0]), Integer.parseInt(anoMesSelecionado[1]));
		}
	}
	
	
	public StreamedContent download() {
		if(cobrancasSelecionadas == null || cobrancasSelecionadas.isEmpty()){
			return null;
		}
		return downloadController.download(cobrancasSelecionadas);
    }


	
	
	public List<CobrancaUsuario> getCobrancas() {
		return cobrancas;
	}

	public void setCobrancas(List<CobrancaUsuario> cobrancas) {
		this.cobrancas = cobrancas;
	}

	public List<String> getAnosMeses() {
		return anosMeses;
	}

	public void setAnosMeses(List<String> anosMeses) {
		this.anosMeses = anosMeses;
	}

	public String getAnoMes() {
		return anoMes;
	}

	public void setAnoMes(String anoMes) {
		this.anoMes = anoMes;
	}

	public List<CobrancaUsuario> getCobrancasSelecionadas() {
		return cobrancasSelecionadas;
	}

	public void setCobrancasSelecionadas(List<CobrancaUsuario> cobrancasSelecionadas) {
		this.cobrancasSelecionadas = cobrancasSelecionadas;
	}
		
		
}
