package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.Recebido;
import br.com.virtz.condominio.exception.AppException;

@Local
public interface IRecebidoService {
	public List<Recebido> recuperarPorApartamento(Long idApartamento);
	public List<Recebido> recuperarPorCondominio(Long idCondominio);
	
	public void salvarNovaEncomenda(Long idApartamento, String comentario) throws AppException ;
	public void salvarNovaCorrespondencia(Long idApartamento, String comentario) throws AppException ;
	public void salvarRecebido(Recebido recebido) throws AppException ;
	public void remover(Long idRecebido) throws AppException;
}

