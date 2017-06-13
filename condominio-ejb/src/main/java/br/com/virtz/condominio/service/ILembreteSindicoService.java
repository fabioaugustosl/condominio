package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.LembreteSindico;

@Local
public interface ILembreteSindicoService {
	public LembreteSindico salvar(LembreteSindico lembreteSindico) throws Exception;
	public void remover(Long id);
	public List<LembreteSindico> recuperarTodos(Condominio condominio);
}
