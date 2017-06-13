package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.Cidade;

@Local
public interface ICidadeDAO  {
	public List<Cidade> recuperarCidadesComCondominiosCadastrados();
	public List<Cidade> recuperarPorEstado(Long idEstado);
}
