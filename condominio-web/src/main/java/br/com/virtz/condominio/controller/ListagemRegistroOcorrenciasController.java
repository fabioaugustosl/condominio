package br.com.virtz.condominio.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.model.LazyDataModel;

import br.com.virtz.condominio.controller.lazy.RegistroOcorrenciaLazyModel;
import br.com.virtz.condominio.entidades.RegistroOcorrencia;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.service.IRegistroOcorrenciaService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class ListagemRegistroOcorrenciasController {

	@EJB
	private IRegistroOcorrenciaService registroService;

	@Inject
	private MessageHelper messageHelper;

	@Inject
	private SessaoUsuario sessao;

	@Inject
	private NavigationPage navegacao;

	private LazyDataModel<RegistroOcorrencia> ocorrencias = null;
	private Usuario usuario = null;

	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
	}



	public LazyDataModel<RegistroOcorrencia> getOcorrencias(){
		if(ocorrencias == null){
			ocorrencias = new RegistroOcorrenciaLazyModel( registroService.recuperarPorCondominioPaginado(usuario.getCondominio().getId(), 0, 50), usuario.getCondominio().getId(),registroService);
		}

		return ocorrencias;
	}

	public List<RegistroOcorrencia> listarTodos(){
		List<RegistroOcorrencia> lista = registroService.recuperarTodos(usuario.getCondominio().getId());
		return lista;
	}


	public void irParaCadastro(){
		navegacao.redirectToPage("/ocorrencia/cadastrarRegistroOcorrencia.faces");
	}


	// GETTERS E SETTERS

}
