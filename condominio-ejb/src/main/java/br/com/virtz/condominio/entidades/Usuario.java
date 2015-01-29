package br.com.virtz.condominio.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.virtz.condominio.constantes.EnumTipoUsuario;

@Entity
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Usuario.recuperarPorCondominio", 
			query = "Select u FROM Usuario u WHERE u.condominio.id = :idCondominio") })
public class Usuario extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Column(name = "nome", length = 100, nullable = false)
	private String nome;
	
	@NotNull
	@Column(name = "email", length = 100, nullable = false)
	private String email;
	
	@Column(name = "telefone", length = 20)
	private String telefone;
	
	@Column(name = "celular", length = 20)
	private String celular;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "tipoUsuario", nullable = false, length=30)
	private EnumTipoUsuario tipoUsuario;
	
	@ManyToOne
	@JoinColumn(name="idCondominio", nullable=false)
	private Condominio condominio;
	
	@Deprecated
	@Column(name = "fotoPerfil", length = 300)
	private String fotoPerfil;
	
	@ManyToOne
	@JoinColumn(name="idApartamento")
	private Apartamento apartamento;
	
	@ManyToOne
	@JoinColumn(name="idImagem")
	private ArquivoUsuario arquivo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Condominio getCondominio() {
		return condominio;
	}

	public void setCondominio(Condominio condominio) {
		this.condominio = condominio;
	}

	public EnumTipoUsuario getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(EnumTipoUsuario tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	public String getFotoPerfil() {
		return fotoPerfil;
	}

	public void setFotoPerfil(String fotoPerfil) {
		this.fotoPerfil = fotoPerfil;
	}
	
	public ArquivoUsuario getArquivo() {
		if(arquivo == null){
			return new ArquivoUsuarioPadrao();
		}
		return arquivo;
	}

	public void setArquivo(ArquivoUsuario arquivo) {
		this.arquivo = arquivo;
	}

	public Apartamento getApartamento() {
		return apartamento;
	}

	public void setApartamento(Apartamento apartamento) {
		this.apartamento = apartamento;
	}
	
	public boolean isSindico(){
		if(EnumTipoUsuario.SINDICO.equals(this.tipoUsuario)){
			return Boolean.TRUE;	
		}
		return Boolean.FALSE;
	}
	
	public boolean isMorador(){
		if(EnumTipoUsuario.MORADOR.equals(this.tipoUsuario)){
			return Boolean.TRUE;	
		}
		return Boolean.FALSE;
	}
	
	public boolean isAdministrativo(){
		if(EnumTipoUsuario.ADMINISTRATIVO.equals(this.tipoUsuario)){
			return Boolean.TRUE;	
		}
		return Boolean.FALSE;
	}
	
}