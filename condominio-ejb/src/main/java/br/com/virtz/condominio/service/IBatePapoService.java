package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.Avaliacao;
import br.com.virtz.condominio.entidades.BatePapo;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.ErroAoSalvar;

@Local
public interface IBatePapoService {
	public BatePapo salvar(BatePapo batePapo) throws Exception;
	public void remover(Long id);
	public List<BatePapo> recuperarTodos(Condominio condominio);
	public Avaliacao avaliarPositivamente(BatePapo papo, Usuario usuario, String comentario) throws ErroAoSalvar;
	public Avaliacao avaliarNegativamente(BatePapo papo, Usuario usuario, String comentario) throws ErroAoSalvar;
	public boolean usuarioJaAvaliou(BatePapo batePapo, Usuario usuario);
}
