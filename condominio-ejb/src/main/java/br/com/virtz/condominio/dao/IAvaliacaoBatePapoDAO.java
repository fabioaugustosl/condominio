package br.com.virtz.condominio.dao;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.Avaliacao;
import br.com.virtz.condominio.entidades.BatePapo;
import br.com.virtz.condominio.entidades.Usuario;

@Local
public interface IAvaliacaoBatePapoDAO {
	public Avaliacao recuperar(BatePapo batePapo, Usuario usuario);
	public Avaliacao salvar(Avaliacao t) throws Exception;
}
