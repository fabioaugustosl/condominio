package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.Cidade;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Usuario;

@Local
public interface ICidadeDAO  {
	public List<Cidade> recuperarCidadesComCondominiosCadastrados();
}
