package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Documento;

@Local
public interface IDocumentoService {
	public Documento salvar(Documento documento) throws Exception;
	public Documento recuperar(Long id);
	public void remover(Long id);
	public List<Documento> recuperarTodos(Condominio condominio);
}
