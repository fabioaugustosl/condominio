package br.com.virtz.condominio.entidades;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name="taxaconfiguracaoboleto")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "TaxaConfiguracaoBoleto.recuperarPorConfiguracao", 
		query = "Select u FROM taxaconfiguracaoboleto u "
		+ " WHERE u.configuracaoBoleto.id = :idConfiguracaoBoleto ") 
})
public class TaxaConfiguracaoBoleto extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "idConfiguracaoBoleto", nullable = false)
	private ConfiguracaoBoleto configuracaoBoleto;

	@ManyToOne
	@JoinColumn(name = "idTaxa", nullable = false)
	private Taxa taxa;

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ConfiguracaoBoleto getConfiguracaoBoleto() {
		return configuracaoBoleto;
	}

	public void setConfiguracaoBoleto(ConfiguracaoBoleto configuracaoBoleto) {
		this.configuracaoBoleto = configuracaoBoleto;
	}

	public Taxa getTaxa() {
		return taxa;
	}

	public void setTaxa(Taxa taxa) {
		this.taxa = taxa;
	}
	
	
}