package br.com.csc.rest.bean;

import javax.xml.bind.annotation.XmlRootElement;

import br.com.virtz.condominio.entidades.Reserva;

@XmlRootElement
public class ReservaRest extends Reserva {

	private String nomeApartamento;
	private String nomeUsuario;


	public ReservaRest(Reserva r) {
		super();
		this.setApartamento(r.getApartamento());
		this.setAreaComum(r.getAreaComum());
		this.setData(r.getData());
		this.setHoraFim(r.getHoraFim());
		this.setHoraInicio(r.getHoraInicio());
		this.setId(r.getId());
		this.setUsuario(r.getUsuario());
	}
	public String getNomeApartamento() {
		return nomeApartamento;
	}
	public void setNomeApartamento(String nomeApartamento) {
		this.nomeApartamento = nomeApartamento;
	}
	public String getNomeUsuario() {
		return nomeUsuario;
	}
	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}




}
