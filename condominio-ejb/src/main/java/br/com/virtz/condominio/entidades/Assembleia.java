package br.com.virtz.condominio.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.virtz.condominio.constantes.EnumTipoAssembleia;

@Entity(name="assembleia")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Assembleia.recuperarPorCondominio", 
				query = "Select n FROM assembleia n WHERE n.condominio.id = :idCondominio ORDER BY n.data DESC "),
		@NamedQuery(name = "Assembleia.recuperarNaoRealizadasPorCondominio", 
				query = "Select n FROM assembleia n "
						+ "	WHERE n.condominio.id = :idCondominio AND n.data >= CURRENT_DATE "
						+ " ORDER BY n.data ASC "),
		@NamedQuery(name = "Assembleia.recuperarIdUltimaAssembleiaDoCondominio", 
						query = "Select max(n.id) FROM assembleia n "
								+ "	WHERE n.condominio.id = :idCondominio AND n.data < CURRENT_DATE "
								+ " ORDER BY n.data DESC ")
})
public class Assembleia extends Entidade implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dataCadastro")
	private Date dataCadastro;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "data")
	private Date data;
	
	@Temporal(TemporalType.TIME)
	@Column(name = "horario1")
	private Date horario1;
	
	@Temporal(TemporalType.TIME)
	@Column(name = "horario2")
	private Date horario2;

	@ManyToOne
	@JoinColumn(name = "idCondominio")
	private Condominio condominio;
	
	@Column(name = "conteudo", length=10000)
	private String conteudo;
	
	@Column(name = "local", length=100)
	private String local;
	
	@OneToMany(mappedBy="assembleia", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	private List<PautaAssembleia> pautas;

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "idAta")
	private ArquivoAtaAssembleia arquivoAta;
	
	@Column(name = "ata", length=99999)
	private String ata;
	
	@Column(name = "permitirPautas")
	private boolean permitirPautas;
	
	@Column(name = "convocacaoFoiEnviada")
	private boolean convocacaoFoiEnviada;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tipoAssembleia")
	private EnumTipoAssembleia tipoAssembleia;
	
	
	
	public Assembleia() {
		super();
	}
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Condominio getCondominio() {
		return condominio;
	}

	public void setCondominio(Condominio condominio) {
		this.condominio = condominio;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public List<PautaAssembleia> getPautas() {
		return pautas;
	}

	public void setPautas(List<PautaAssembleia> pautas) {
		this.pautas = pautas;
	}

	public ArquivoAtaAssembleia getArquivoAta() {
		return arquivoAta;
	}

	public void setArquivoAta(ArquivoAtaAssembleia arquivoAta) {
		this.arquivoAta = arquivoAta;
	}

	public String getAta() {
		return ata;
	}

	public void setAta(String ata) {
		this.ata = ata;
	}

	public Date getHorario1() {
		return horario1;
	}

	public void setHorario1(Date horario1) {
		this.horario1 = horario1;
	}

	public Date getHorario2() {
		return horario2;
	}

	public void setHorario2(Date horario2) {
		this.horario2 = horario2;
	}
	
	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	
	public boolean getPermitirPautas() {
		return permitirPautas;
	}

	public void setPermitirPautas(boolean permitirPautas) {
		this.permitirPautas = permitirPautas;
	}
	
	public boolean isConvocacaoFoiEnviada() {
		return convocacaoFoiEnviada;
	}

	public void setConvocacaoFoiEnviada(boolean convocacaoFoiEnviada) {
		this.convocacaoFoiEnviada = convocacaoFoiEnviada;
	}
	
	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}
	
	public EnumTipoAssembleia getTipoAssembleia() {
		return tipoAssembleia;
	}

	public void setTipoAssembleia(EnumTipoAssembleia tipoAssembleia) {
		this.tipoAssembleia = tipoAssembleia;
	}



	/**
	 * De acordo com a data da assembleia exibe um status.
	 * @return
	 */
	public String getSituacao(){
		Calendar data = Calendar.getInstance();
		setTimeZero(data);
		
		Calendar dataAssembleia = Calendar.getInstance();
		dataAssembleia.setTime(getData());
		setTimeZero(dataAssembleia);
		
		if(data.equals(dataAssembleia)){
			return "É hoje";
		} else if(data.before(dataAssembleia)){
			return "Agendada";
		} else if(data.after(dataAssembleia)){
			return "Realizada";
		}
		return " - ";
	}



	private void setTimeZero(Calendar data) {
		data.set(Calendar.MILLISECOND, 0);
		data.set(Calendar.SECOND, 0);
		data.set(Calendar.MINUTE, 0);
		data.set(Calendar.HOUR, 0);
	}
	
	
	public List<PautaAssembleia> getPautasAprovadas() {
		List<PautaAssembleia> aprovadas = new ArrayList<PautaAssembleia>();
		if(pautas != null && !pautas.isEmpty()){
			for(PautaAssembleia p : pautas){
				if(p.isAprovada()){
					aprovadas.add(p);
				}
			}
		}
		return aprovadas;
	}
	
}