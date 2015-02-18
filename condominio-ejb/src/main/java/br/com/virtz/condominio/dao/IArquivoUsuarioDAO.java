package br.com.virtz.condominio.dao;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.ArquivoUsuario;

@Local
public interface IArquivoUsuarioDAO extends CrudDAO<ArquivoUsuario> {
}
