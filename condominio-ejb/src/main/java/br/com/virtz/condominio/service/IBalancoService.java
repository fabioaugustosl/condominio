package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.ArquivoBalanco;
import br.com.virtz.condominio.entidades.Balanco;
import br.com.virtz.condominio.entidades.ItemBalanco;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;

@Local
public interface IBalancoService {
	
	// Balanço
	public Balanco salvar(Balanco balanco) throws AppException;
	public List<Balanco> recuperarBalancoPorCondominio(Long idCondominio);
	public Balanco recuperarBalancoPorCondominio(Long idCondominio, Integer ano, Integer mes);
	public Balanco recuperarBalanco(Long idBalanco);
	public List<ItemBalanco> recuperarDespesas(Balanco balanco) throws AppException;
	public List<ItemBalanco> recuperarReceitas(Balanco balanco) throws AppException;

	
	// Despesas / Receitas
	public ItemBalanco salvarReceita(Long idBalanco, Double valor, String descricao, ArquivoBalanco arquivo, Usuario usuario)  throws AppException;
	public ItemBalanco salvarDespesa(Long idBalanco, Double valor, String descricao, ArquivoBalanco arquivo, Usuario usuario)  throws AppException;
	public void removerItemBalanco(Long idItemBalanco)  throws AppException;
	
	
}
