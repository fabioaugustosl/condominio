package br.com.virtz.condominio.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.condominio.constantes.EnumTipoRecebido;
import br.com.virtz.condominio.dao.IApartamentoDAO;
import br.com.virtz.condominio.dao.IRecebidoDAO;
import br.com.virtz.condominio.entidades.Apartamento;
import br.com.virtz.condominio.entidades.Recebido;
import br.com.virtz.condominio.exception.AppException;

@Stateless
public class RecebidoService implements IRecebidoService {

	@EJB
	private IRecebidoDAO recebidoDAO;
	
	@EJB
	private IApartamentoDAO apartamentoDAO;
	
	
	
	@Override
	public List<Recebido> recuperarPorApartamento(Long idApartamento) {
		if(idApartamento == null){
			return new ArrayList<Recebido>();
		}
		return recebidoDAO.recuperarPorApartamento(idApartamento);
	}


	@Override
	public List<Recebido> recuperarPorCondominio(Long idCondominio) {
		if(idCondominio == null){
			return new ArrayList<Recebido>();
		}
		return recebidoDAO.recuperarPorCondominio(idCondominio);
	}


	@Override
	public void salvarNovaEncomenda(Long idApartamento, String comentario) throws AppException {
		try {
			if(idApartamento == null){
				throw new AppException("É necessário informar o apartamento que recebeu a encomenda.");
			}
			
			Apartamento apto = apartamentoDAO.recuperarPorId(idApartamento);
			Recebido recebido = new Recebido();
			recebido.setApartamento(apto);
			recebido.setComentario(comentario);
			recebido.setData(new Date());
			recebido.setTipoRecebido(EnumTipoRecebido.ENCOMENDA);
			
			recebidoDAO.salvar(recebido);
		} catch (Exception e) {
			throw new AppException("Ocorreu um erro ao salvar a encomenda.");
		}
		
	}


	@Override
	public void salvarNovaCorrespondencia(Long idApartamento, String comentario) throws AppException {
		try {
			if(idApartamento == null){
				throw new AppException("É necessário informar o apartamento que recebeu a correspondência.");
			}
			
			Apartamento apto = apartamentoDAO.recuperarPorId(idApartamento);
			Recebido recebido = new Recebido();
			recebido.setApartamento(apto);
			recebido.setComentario(comentario);
			recebido.setData(new Date());
			recebido.setTipoRecebido(EnumTipoRecebido.CORRESPONDENCIA);
			
			recebidoDAO.salvar(recebido);
		} catch (Exception e) {
			throw new AppException("Ocorreu um erro ao salvar a correspondência.");
		}
	}


	@Override
	public void salvarRecebido(Recebido recebido) throws AppException {
		try{
			if(recebido == null){
				throw new AppException("Informação incompletas para salvar a correspondência/encomenda.");
			}
			if(recebido.getApartamento() == null){
				throw new AppException("É necessário informar o apartamento que recebeu a correspondência/encomenda.");
			}
			recebidoDAO.salvar(recebido);
		} catch (Exception e) {
			throw new AppException("Ocorreu um erro ao salvar a correspondência/encomenda.");
		}
	}


	@Override
	public void remover(Long idRecebido) throws AppException {
		recebidoDAO.remover(idRecebido);
	}
	
	
}
