package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.constantes.EnumTipoBalanco;
import br.com.virtz.condominio.entidades.ItemBalanco;

@Local
public interface IItemBalancoDAO extends CrudDAO<ItemBalanco> {
	public List<String> recuperarUltimasDescricoes(Long idCondominio, Integer ano, EnumTipoBalanco tipo, Integer limite);
}
