package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.Assembleia;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.ErroAoSalvar;

@Local
public interface IAssembleiaService {
	public Assembleia salvar(Assembleia assembleia) throws ErroAoSalvar;
	public void removerAssembleia(Long id);
	public List<Assembleia> recuperarTodos(Long idCondominio);
	public void removerPauta(Long idPauta);
	public void novaPauta(Long idAssembleia,String textoPauta, Usuario usuario) throws ErroAoSalvar;
	public Assembleia recuperar(Long idAssembleia);
	public List<Assembleia> recuperarAssembleiasNaoRealizadas(Long idCondominio);
}
