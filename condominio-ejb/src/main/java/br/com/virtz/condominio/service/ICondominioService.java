package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.AreaComum;
import br.com.virtz.condominio.entidades.Bloco;
import br.com.virtz.condominio.entidades.Cidade;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Usuario;

@Local
public interface ICondominioService {
	// condominio
	public Condominio salvar(Condominio condominio) throws Exception;
	public void remover(Long id);
	public List<Condominio> recuperarTodos();
	public Condominio recuperarCondominioCompleto(Usuario usuario);
	public List<Condominio> recuperarPorCidade(Long idCidade);
	public List<Cidade> cidadesQuePossuemCondominioCadastrado();
	
	// bloco
	public Bloco salvarBloco(Bloco bloco) throws Exception;
	public void removerBloco(Long id);
	public List<Bloco> recuperarTodosBlocos();
	public List<Bloco> recuperarTodosBlocosComApartamentos(Long idCondominio);
	public List<Bloco> sugerirBlocos(int quantidadeBlocos, Condominio condominio);
	public Bloco recuperarBloco(Long idBloco);
	
	// areas comuns
	public AreaComum salvarAreaComum(AreaComum area) throws Exception;
	public void removerAreaComum(Long id);
}
