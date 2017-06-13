package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.constantes.EnumFuncionalidadeAtalho;
import br.com.virtz.condominio.entidades.Atalho;

@Local
public interface IAtalhoDAO extends CrudDAO<Atalho> {
	public List<Atalho> recuperar(Long idCondominio);
	public Atalho recuperar(Long idCondominio, EnumFuncionalidadeAtalho funcionalidade);
}
