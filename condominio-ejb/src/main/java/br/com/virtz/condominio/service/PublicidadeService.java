package br.com.virtz.condominio.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.condominio.constantes.EnumTipoPublicidade;
import br.com.virtz.condominio.dao.IPublicidadeDAO;
import br.com.virtz.condominio.entidades.Publicidade;

@Stateless
public class PublicidadeService implements IPublicidadeService {

	@EJB
	private IPublicidadeDAO publicidadeDAO;

	
	@Override
	public Publicidade recuperar(Long idCondominio, EnumTipoPublicidade tipo) {
		return publicidadeDAO.recuperar(idCondominio, tipo);
	}
	

}
