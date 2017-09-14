package br.com.csc.util;

public enum EnumTemplatesRestRefatorar {
	PADRAO("padrao.ftl", "Informativo CondominioSOBCOntrole.com.br", "nao_responder@condominiosobcontrole.com.br"),
	CONFIRMACAO_USUARIO("confirmacaoUsuario.ftl", "Confirmação de usuário", "nao_responder@condominiosobcontrole.com.br"),
	CONFIRMACAO_USUARIO_CONDOMINIO("confirmacaoUsuarioCondominio.ftl", "Bem vindo! Acesse seu condomínio online. ", "nao_responder@condominiosobcontrole.com.br"),
	LEMBRETE_ASSEMBLEIA("lembreteAssembleia.ftl", "Lembrete de assembleia", "nao_responder@condominiosobcontrole.com.br"),
	MENSAGEM_SINDICO("mensagemSindico.ftl", "Fale com o Síndico - Contato enviado através do site", "nao_responder@condominiosobcontrole.com.br"),
	RESPOSTA_MENSAGEM_SINDICO("respostaMensagemSindico.ftl", "Resposta do S\u00edndico - reposta registrada no site", "nao_responder@condominiosobcontrole.com.br"),
	BOLETO("boletoPorEmail.ftl", "Boleto", "nao_responder@condominiosobcontrole.com.br"),
	NOVO_MORADOR("novoMorador.ftl", "Novo Morador Cadastrado no Site", "nao_responder@condominiosobcontrole.com.br"),
	PAUTA_ENVIADA("pautaEnviada.ftl", "Pauta para assembleia enviada pelo site.", "nao_responder@condominiosobcontrole.com.br"),
	ESQUECI_MINHA_SENHA("esqueciMinhaSenha.ftl", "Esqueci minha senha", "nao_responder@condominiosobcontrole.com.br"),
	CONFIRMACAO_RESERVA_AREA("confirmacaoReservaArea.ftl", "Confirmação de reserva de área comum", "nao_responder@condominiosobcontrole.com.br"),
	RESULTADO_FINAL_VOTACAO("resultadoVotacao.ftl", "Resultado final de votação", "nao_responder@condominiosobcontrole.com.br"),
	ATA_ANEXADA("emailAta.ftl", "Ata disponibilizada no site", "nao_responder@condominiosobcontrole.com.br"),
	NOVA_NOTICIA("notificacaoNoticia.ftl", "Nova notícia!", "nao_responder@condominiosobcontrole.com.br"),
	CONVOCACAO_ASSEMBLEIA("convocacaoAssembleia.ftl", "Convocação para assembleia", "nao_responder@condominiosobcontrole.com.br"),
	NOVA_ASSEMBLEIA("avisoCriacaoAssembleia.ftl", "Aviso de nova assembleia", "nao_responder@condominiosobcontrole.com.br"),
	REGISTRO_OCORRENCIA("novoRegistroOcorrencia.ftl", "Novo registro de ocorrência", "nao_responder@condominiosobcontrole.com.br"),
	SOLICITACAO_SEGUNDA_VIA_BOLETO("solicitacaoSegundaViaBoleto.ftl", "Solicitação de segunda via de boleto", "nao_responder@condominiosobcontrole.com.br");

	private String nomeArquivo;
	private String assunto;
	private String de;



	private EnumTemplatesRestRefatorar(String nomeArquivo, String assunto, String de) {
		this.nomeArquivo = nomeArquivo;
		this.assunto = assunto;
		this.de = de;
	}



	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public String getAssunto() {
		return assunto;
	}

	public String getDe() {
		return de;
	}


}
