package br.com.virtz.condominio.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.StringUtils;

import br.com.virtz.condominio.constantes.EnumPlanoContratado;

/**
 * @author Fabio
 *
 */
@Entity(name="condominio")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Condominio.recuperarPorCidade",
			query = "Select c FROM condominio c WHERE c.cidade.id = :idCidade AND ehTeste = 0 "),
	@NamedQuery(name = "Condominio.recuperarPorId",
			query = "Select c  FROM condominio c LEFT JOIN FETCH c.areasComuns areas  WHERE c.id = :id")
})
public class Condominio extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String TIPO_VERTICAL = "VERTICAL";
	public static final String TIPO_HORIZONTAL = "HORIZONTAL";


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@XmlTransient
	@OneToMany(mappedBy = "condominio")
	private List<Bloco> blocos;

	@OneToMany(mappedBy = "condominio")
	private Set<AreaComum> areasComuns;

	@OneToMany(mappedBy = "condominio")
	private Set<Usuario> usuarios;

	@NotNull
	@Column(name = "nome", length = 100, nullable = false)
	private String nome;

	@Column(name = "qtdAptos")
	private Integer quantidadeApartamentos;

	@Column(name = "endereco", length = 100)
	private String endereco;

	@Column(name = "numero")
	private Integer numero;

	@Column(name = "complemento", length = 100)
	private String complemento;

	@Column(name = "bairro", length = 100)
	private String bairro;

	@Column(name = "cep", length = 20)
	private String cep;

	@Column(name = "telefone", length = 20)
	private String telefone;

	@Column(name = "cnpj", length = 20)
	private String cnpj;

	@ManyToOne
	@JoinColumn(name = "idCidade")
	private Cidade cidade;

	@Column(name = "email", length = 100)
	private String email;

	@OneToOne(mappedBy = "condominio")
	private AcessoCFTV cftv;

	@Column(name = "nomeUnidade", length = 20)
	private String nomeUnidade;

	@Column(name = "tipoCondominio", length = 50)
	private String tipoCondominio;

	@Column(name = "nomeNivelAgrupamento1", length = 50)
	private String nomeNivelAgrupamento1;

	@Column(name = "nomeNivelAgrupamento2", length = 50)
	private String nomeNivelAgrupamento2;

	@Column(name = "bloqueado")
	private Boolean bloqueado;

	@Temporal(TemporalType.DATE)
	@Column(name = "dataAceite")
	private Date dataAceiteTermo;

	@Column(name = "termoAceite", length=40000)
	private String termoAceite;

	@Enumerated(EnumType.STRING)
	@Column(name = "planoContratado", length=50)
	private EnumPlanoContratado planoContratado;

	@Column(name = "usuarioTermoAceite", length=100)
	private String usuarioTermoAceite;



	// parte financeira
	@Column(name = "valorCondominioMes")
	private Double valorCondominioMes;

	@Column(name = "multaAposVencimento")
	private boolean multaAposVencimento;

	@Column(name = "jurosAposVencimento")
	private boolean jurosAposVencimento;

	@Column(name = "indTeste")
	private Boolean ehTeste;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Bloco> getBlocos() {
		return blocos;
	}

	public void setBlocos(List<Bloco> blocos) {
		this.blocos = blocos;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getQuantidadeApartamentos() {
		return quantidadeApartamentos;
	}

	public void setQuantidadeApartamentos(Integer quantidadeApartamentos) {
		this.quantidadeApartamentos = quantidadeApartamentos;
	}

	public Set<AreaComum> getAreasComuns() {
		return areasComuns;
	}

	public void setAreasComuns(Set<AreaComum> areasComuns) {
		this.areasComuns = areasComuns;
	}

	public Set<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(Set<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Double getValorCondominioMes() {
		return valorCondominioMes;
	}

	public void setValorCondominioMes(Double valorCondominioMes) {
		this.valorCondominioMes = valorCondominioMes;
	}

	public boolean isMultaAposVencimento() {
		return multaAposVencimento;
	}

	public void setMultaAposVencimento(boolean multaAposVencimento) {
		this.multaAposVencimento = multaAposVencimento;
	}

	public boolean isJurosAposVencimento() {
		return jurosAposVencimento;
	}

	public void setJurosAposVencimento(boolean jurosAposVencimento) {
		this.jurosAposVencimento = jurosAposVencimento;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public Boolean getEhTeste() {
		return ehTeste;
	}

	public void setEhTeste(Boolean ehTeste) {
		this.ehTeste = ehTeste;
	}

	public AcessoCFTV getCftv() {
		return cftv;
	}

	public void setCftv(AcessoCFTV cftv) {
		this.cftv = cftv;
	}

	public String getTipoCondominio() {
		return tipoCondominio;
	}

	public void setTipoCondominio(String tipoCondominio) {
		this.tipoCondominio = tipoCondominio;
	}

	public boolean condominioEhVertical(){
		if(StringUtils.isBlank(this.tipoCondominio) || TIPO_VERTICAL.equals(this.tipoCondominio)){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public boolean condominioEhHorizontal(){
		if(TIPO_HORIZONTAL.equals(this.tipoCondominio)){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public String getNomeUnidade() {
		return nomeUnidade;
	}

	public void setNomeUnidade(String nomeUnidade) {
		this.nomeUnidade = nomeUnidade;
	}

	public String getNomeNivelAgrupamento1() {
		return nomeNivelAgrupamento1;
	}

	public void setNomeNivelAgrupamento1(String nomeNivelAgrupamento1) {
		this.nomeNivelAgrupamento1 = nomeNivelAgrupamento1;
	}

	public String getNomeNivelAgrupamento2() {
		return nomeNivelAgrupamento2;
	}

	public void setNomeNivelAgrupamento2(String nomeNivelAgrupamento2) {
		this.nomeNivelAgrupamento2 = nomeNivelAgrupamento2;
	}

	public Boolean getBloqueado() {
		return bloqueado;
	}

	public void setBloqueado(Boolean bloqueado) {
		this.bloqueado = bloqueado;
	}

	public Date getDataAceiteTermo() {
		return dataAceiteTermo;
	}

	public void setDataAceiteTermo(Date dataAceiteTermo) {
		this.dataAceiteTermo = dataAceiteTermo;
	}

	public String getTermoAceite() {
		return termoAceite;
	}

	public void setTermoAceite(String termoAceite) {
		this.termoAceite = termoAceite;
	}

	public EnumPlanoContratado getPlanoContratado() {
		return planoContratado;
	}

	public void setPlanoContratado(EnumPlanoContratado planoContratado) {
		this.planoContratado = planoContratado;
	}

	public String getUsuarioTermoAceite() {
		return usuarioTermoAceite;
	}

	public void setUsuarioTermoAceite(String usuarioTermoAceite) {
		this.usuarioTermoAceite = usuarioTermoAceite;
	}

}