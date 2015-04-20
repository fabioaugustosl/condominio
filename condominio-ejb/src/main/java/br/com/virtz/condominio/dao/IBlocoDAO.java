package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.Bloco;

@Local
public interface IBlocoDAO extends CrudDAO<Bloco> {
	public List<Bloco> recuperarComApartamentos(Long idCondominio);
	public Bloco recuperarBlocoCompleto(Long idBloco);
}
