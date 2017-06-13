package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.Recebido;
import br.com.virtz.condominio.exception.AppException;

@Local
public interface IRecebidoService extends PaginacaoServiceRecebidos<Recebido> {
	public List<Recebido> recuperarPorApartamento(Long idApartamento);
	public List<Recebido> recuperarPorCondominio(Long idCondominio);

	public void salvarNovaEncomenda(Long idCondominio, Long idApartamento, String comentario) throws AppException ;
	public void salvarNovaCorrespondencia(Long idCondominio, Long idApartamento, String comentario) throws AppException ;
	public void salvarRecebido(Recebido recebido) throws AppException ;
	public void remover(Long idRecebido) throws AppException;
	public void salvarRetirada(Long idRecebido, String nomePessoa) throws AppException ;

}

