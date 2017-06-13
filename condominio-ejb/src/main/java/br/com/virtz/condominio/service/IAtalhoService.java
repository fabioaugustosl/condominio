package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.constantes.EnumFuncionalidadeAtalho;
import br.com.virtz.condominio.entidades.Atalho;
import br.com.virtz.condominio.entidades.Condominio;

@Local
public interface IAtalhoService {
	public Atalho salvar(Atalho atalho) throws Exception;
	public void remover(Long id);
	public List<Atalho> recuperarTodos(Condominio condominio);
	public Atalho recuperarTodos(Condominio condominio, EnumFuncionalidadeAtalho funcionalidade);
}
