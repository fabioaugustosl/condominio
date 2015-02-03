package br.com.virtz.condominio.entidades;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@NamedQueries({
	@NamedQuery(name="Token.recuperarPorToken",
				query=" SELECT distinct t FROM Token t WHERE t.token = :token ")
})
public class Token  extends Entidade implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "TOKEN")
	private String token;
	
	@Column(name = "CHAVE")
	private String chaveEntidade;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA")
	private Date data;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_EXPIRACAO")
	private Date dataExpiracao;

	@Column(name = "ATIVO")
	private boolean ativo;
	
	
	
	public Token() {
		super();
		ativo = Boolean.TRUE;
	}
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Date getDataExpiracao() {
		return dataExpiracao;
	}

	public void setDataExpiracao(Date dataExpiracao) {
		this.dataExpiracao = dataExpiracao;
	}
	
	public boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public String getChaveEntidade() {
		return chaveEntidade;
	}

	public void setChaveEntidade(String chaveEntidade) {
		this.chaveEntidade = chaveEntidade;
	}


}
