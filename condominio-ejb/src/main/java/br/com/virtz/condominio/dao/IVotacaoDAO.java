package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.AreaComum;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Reserva;
import br.com.virtz.condominio.entidades.Votacao;

@Local
public interface IVotacaoDAO extends CrudDAO<Votacao> {
	public List<Votacao> recuperar(Condominio condominio);
}
