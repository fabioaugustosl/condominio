package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.condominio.dao.IDocumentoDAO;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Documento;

@Stateless
public class DocumentoService implements IDocumentoService {

	@EJB
	private IDocumentoDAO documentoDAO;

	@Override
	public Documento salvar(Documento documento) throws Exception {
		return documentoDAO.salvar(documento);
	}

	@Override
	public void remover(Long id) {
		documentoDAO.remover(id);
	}

	@Override
	public List<Documento> recuperarTodos(Condominio condominio) {
		return documentoDAO.recuperar(condominio);
	}

	@Override
	public Documento recuperar(Long id) {
		Documento doc = documentoDAO.recuperarPorId(id);
		return doc;
	}
	

}
