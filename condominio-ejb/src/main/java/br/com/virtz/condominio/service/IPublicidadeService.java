package br.com.virtz.condominio.service;

import javax.ejb.Local;

import br.com.virtz.condominio.constantes.EnumTipoPublicidade;
import br.com.virtz.condominio.entidades.Publicidade;

@Local
public interface IPublicidadeService {
	public Publicidade recuperar(Long idCondominio, EnumTipoPublicidade tipo);
}
