package br.com.virtz.condominio.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.condominio.constantes.EnumTipoRecebido;
import br.com.virtz.condominio.dao.IApartamentoDAO;
import br.com.virtz.condominio.dao.ICondominioDAO;
import br.com.virtz.condominio.dao.IRecebidoDAO;
import br.com.virtz.condominio.entidades.Apartamento;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Recebido;
import br.com.virtz.condominio.exception.AppException;

@Stateless
public class RecebidoService implements IRecebidoService {

	@EJB
	private IRecebidoDAO recebidoDAO;

	@EJB
	private IApartamentoDAO apartamentoDAO;

	@EJB
	private ICondominioDAO condominioDAO;



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
	public void salvarNovaEncomenda(Long idCondominio, Long idApartamento, String comentario) throws AppException {
		try {
			if(idApartamento == null){
				throw new AppException("É necessário informar o apartamento que recebeu a encomenda.");
			}

			if(idCondominio == null){
				throw new AppException("É necessário informar o condomínio que recebeu a correspondência.");
			}

			Apartamento apto = apartamentoDAO.recuperarPorId(idApartamento);
			Condominio cond = condominioDAO.recuperarPorId(idCondominio);
			Recebido recebido = new Recebido();
			recebido.setCondominio(cond);
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
	public void salvarNovaCorrespondencia(Long idCondominio, Long idApartamento, String comentario) throws AppException {
		try {
			if(idApartamento == null){
				throw new AppException("É necessário informar o apartamento que recebeu a correspondência.");
			}

			if(idCondominio == null){
				throw new AppException("É necessário informar o condomínio que recebeu a correspondência.");
			}

			Apartamento apto = apartamentoDAO.recuperarPorId(idApartamento);
			Condominio cond = condominioDAO.recuperarPorId(idCondominio);
			Recebido recebido = new Recebido();
			recebido.setCondominio(cond);
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


	@Override
	public void salvarRetirada(Long idRecebido, String nomePessoa) throws AppException {
		if(idRecebido == null){
			throw new AppException("O recebido é obrigatório.");
		}

		Recebido r = recebidoDAO.recuperarPorId(idRecebido);

		r.setDataRetirada(new Date());
		r.setPessoaRetirada(nomePessoa);

		try {
			recebidoDAO.salvar(r);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException("Ocorreu um erro ao salvar a retirado da encomenda/correspondência.");
		}

	}


	@Override
	public List<Recebido> recuperarPorCondominioPaginado(Long idCondominio, int inicio, int qtdRegistros) {
		return recebidoDAO.recuperarPaginado(idCondominio, inicio, qtdRegistros);
	}

	@Override
	public List<Recebido> recuperarPorApartamentoPaginado(Long idApartamento, int inicio, int qtdRegistros) {
		return recebidoDAO.recuperarPaginadoApto(idApartamento, inicio, qtdRegistros);
	}


	@Override
	public int total(Long idCondominio) {
		return recebidoDAO.totalRecebidos(idCondominio);
	}

	@Override
	public int totalVisitantesApto(Long idApartamento) {
		return recebidoDAO.totalRecebidosApto(idApartamento);
	}


}
