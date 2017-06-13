package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.constantes.EnumFuncaoBloqueio;
import br.com.virtz.condominio.entidades.BloqueioFuncaoUsuario;

@Local
public interface IBloqueioFuncaoUsuarioDAO extends CrudDAO<BloqueioFuncaoUsuario> {

	public List<BloqueioFuncaoUsuario> recuperarPorUsuarioEFuncao(Long idUsuario, EnumFuncaoBloqueio funcao);
	public List<BloqueioFuncaoUsuario> recuperarPorCondominioEFuncao(Long idCondominio, EnumFuncaoBloqueio funcao);
	public List<BloqueioFuncaoUsuario> recuperarPorUsuario(Long idUsuario);

}
