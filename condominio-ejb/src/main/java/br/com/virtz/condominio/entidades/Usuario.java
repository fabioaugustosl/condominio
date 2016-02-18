package br.com.virtz.condominio.entidades;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.virtz.condominio.constantes.EnumTipoUsuario;

@Entity(name="usuario")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Usuario.recuperarPorCondominio", 
			query = "Select u FROM usuario u "
					+ "	LEFT JOIN u.apartamento ap "
					+ "	LEFT JOIN u.arquivo arq "
					+ " WHERE u.condominio.id = :idCondominio AND u.deletado = 0 "),
	@NamedQuery(name = "Usuario.recuperarSindicosPorCondominio", 
			query = "Select u FROM usuario u WHERE u.condominio.id = :idCondominio AND u.tipoUsuario = 'SINDICO' AND u.deletado = 0"),
	@NamedQuery(name = "Usuario.recuperarPorEmail", 
			query = "Select u FROM usuario u "
					+ " LEFT JOIN FETCH u.condominio c "
					+ " LEFT JOIN FETCH c.areasComuns areas "
					+ " WHERE u.email = :email and u.cadastroConfirmado = 1 AND u.deletado = 0"),
	@NamedQuery(name = "Usuario.recuperarPorEmailAutenticacao", 
			query = "Select u FROM usuario u "
							+ " LEFT JOIN FETCH u.condominio c "
							+ " LEFT JOIN FETCH c.areasComuns areas "
							+ " WHERE u.email = :email AND u.deletado = 0 ORDER BY c.id DESC"),
	@NamedQuery(name = "Usuario.recuperarPorCondominioETipoUsuario", 
			query = "Select u FROM usuario u "
					+ " JOIN u.condominio c "
					+ " WHERE c.id = :idCondominio AND u.tipoUsuario = :tipoUsuario AND u.deletado = 0"),
	@NamedQuery(name = "Usuario.recuperarUsuariosPorEmail", 
				query = "Select u FROM usuario u "
							+ " WHERE u.email = :email AND u.deletado = 0"),
	@NamedQuery(name = "Usuario.recuperarUsuariosPorApto", 
				query = "Select u FROM usuario u "
							+ " WHERE u.apartamento.id = :idApartamento AND u.deletado = 0")
})
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
	
	@NotNull
	@Column(name = "senha", length = 100, nullable = false)
	private String senha;
	
	@Column(name = "cpf", length = 14)
	private String cpf;
	
	@Column(name = "telefone", length = 20)
	private String telefone;
	
	@Column(name = "celular", length = 20)
	private String celular;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dataCadastro")
	private Date dataCadastro;
	
	@Column(name = "cadastroConfirmado")
	private Boolean cadastroConfirmado;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "tipoUsuario", nullable = false, length=30)
	private EnumTipoUsuario tipoUsuario;
	
	@ManyToOne
	@JoinColumn(name="idCondominio")
	private Condominio condominio;
	
	@Deprecated
	@Column(name = "fotoPerfil", length = 300)
	private String fotoPerfil;
	
	@ManyToOne
	@JoinColumn(name="idApartamento")
	private Apartamento apartamento;
	
	@ManyToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(name="idImagem")
	private ArquivoUsuario arquivo;
	
	@Column(name = "deletado")
	private Boolean deletado;
	
	@Transient
	private String senhaDigitada;
	
	@Transient
	private String senhaDigitadaConfirmacao;

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
	

	public boolean existeFotoParaUsuario() {
		if(arquivo == null){
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
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
	
	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}
	
	public String getSenhaDigitada() {
		return senhaDigitada;
	}

	public void setSenhaDigitada(String senhaDigitada) {
		this.senhaDigitada = senhaDigitada;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Boolean getCadastroConfirmado() {
		return cadastroConfirmado;
	}

	public void setCadastroConfirmado(Boolean cadastroConfirmado) {
		this.cadastroConfirmado = cadastroConfirmado;
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

	public String getSenhaDigitadaConfirmacao() {
		return senhaDigitadaConfirmacao;
	}

	public void setSenhaDigitadaConfirmacao(String senhaDigitadaConfirmacao) {
		this.senhaDigitadaConfirmacao = senhaDigitadaConfirmacao;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Boolean getDeletado() {
		return deletado;
	}

	public void setDeletado(Boolean deletado) {
		this.deletado = deletado;
	}
	
	public String getNomeExibicao(){
		if(this.nome == null || this.nome.length() < 12){
			return this.nome;
		}
		String[] nomeQuebrado = this.nome.split(" ");
		if(nomeQuebrado.length > 1){
			String n = nomeQuebrado[0];
			return n;
		}
		return this.nome;
	}
	
	
	
}