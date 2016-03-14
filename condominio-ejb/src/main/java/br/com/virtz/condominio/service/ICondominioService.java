package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.Local;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import br.com.virtz.boleto.bean.EnumBanco;
import br.com.virtz.condominio.entidades.AcessoCFTV;
import br.com.virtz.condominio.entidades.AreaComum;
import br.com.virtz.condominio.entidades.Bloco;
import br.com.virtz.condominio.entidades.BoletoExterno;
import br.com.virtz.condominio.entidades.Cidade;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.ContaBancariaCondominio;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;

@Local
public interface ICondominioService {
	// condominio
	public Condominio salvar(Condominio condominio) throws Exception;
	public void remover(Long id);
	public List<Condominio> recuperarTodos();
	public Condominio recuperarCondominioCompleto(Usuario usuario);
	public List<Condominio> recuperarPorCidade(Long idCidade);
	public List<Cidade> cidadesQuePossuemCondominioCadastrado();
	
	// bloco
	public Bloco salvarBloco(Bloco bloco) throws Exception;
	public void removerBloco(Long id);
	public List<Bloco> recuperarTodosBlocos();
	public List<Bloco> recuperarTodosBlocosComApartamentos(Long idCondominio);
	public List<Bloco> sugerirBlocos(int quantidadeBlocos, Condominio condominio);
	public Bloco recuperarBloco(Long idBloco);
	
	// areas comuns
	public AreaComum salvarAreaComum(AreaComum area) throws Exception;
	public void removerAreaComum(Long id);
	
	// ContBancaria
	public ContaBancariaCondominio recuperarContaBancariaCondominioPrincipal(Long idCondominio);
	public ContaBancariaCondominio salvarContaBancariaCondominioPrincipal(ContaBancariaCondominio conta) throws AppException;
	public void salvarContaBancariaCondominioPrincipal(Condominio condominio, EnumBanco banco, String agencia, String digitoAgencia, String codigoCarteira) throws AppException;

	// Cftv
	public AcessoCFTV recuperarCFTV(Long idCondominio);
	public AcessoCFTV salvarAcessoCFTV(AcessoCFTV cftv) throws Exception;
	
	// BoletoExterno
	public BoletoExterno recuperarBoletoExterno(Long idCondominio);
	public BoletoExterno salvarBoletoExterno(BoletoExterno boleto) throws Exception;
	public void removerBoletoExterno(Long idBoletoExterno) throws Exception;
}
