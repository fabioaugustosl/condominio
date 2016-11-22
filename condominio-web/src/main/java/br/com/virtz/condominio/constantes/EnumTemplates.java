package br.com.virtz.condominio.constantes;

public enum EnumTemplates {
	PADRAO("padrao.ftl", "Informativo CondominioSOBCOntrole.com.br", "contato@condominiosobcontrole.com.br"),
	CONFIRMACAO_USUARIO("confirmacaoUsuario.ftl", "Confirmação de usuário", "contato@condominiosobcontrole.com.br"),
	CONFIRMACAO_USUARIO_CONDOMINIO("confirmacaoUsuarioCondominio.ftl", "Bem vindo! Acesse seu condomínio online. ", "contato@condominiosobcontrole.com.br"),
	LEMBRETE_ASSEMBLEIA("lembreteAssembleia.ftl", "Lembrete de assembleia", "contato@condominiosobcontrole.com.br"),
	MENSAGEM_SINDICO("mensagemSindico.ftl", "Fale com o Síndico - Contato enviado através do site", "contato@condominiosobcontrole.com.br"),
	RESPOSTA_MENSAGEM_SINDICO("respostaMensagemSindico.ftl", "Resposta do S\u00edndico - reposta registrada no site", "contato@condominiosobcontrole.com.br"),
	BOLETO("boletoPorEmail.ftl", "Boleto", "contato@condominiosobcontrole.com.br"),
	NOVO_MORADOR("novoMorador.ftl", "Novo Morador Cadastrado no Site", "contato@condominiosobcontrole.com.br"),
	PAUTA_ENVIADA("pautaEnviada.ftl", "Pauta para assembleia enviada pelo site.", "contato@condominiosobcontrole.com.br"),
	ESQUECI_MINHA_SENHA("esqueciMinhaSenha.ftl", "Esqueci minha senha", "contato@condominiosobcontrole.com.br"),
	CONFIRMACAO_RESERVA_AREA("confirmacaoReservaArea.ftl", "Confirmação de reserva de área comum", "contato@condominiosobcontrole.com.br"),
	RESULTADO_FINAL_VOTACAO("resultadoVotacao.ftl", "Resultado final de votação", "contato@condominiosobcontrole.com.br"),
	ATA_ANEXADA("emailAta.ftl", "Ata disponibilizada no site", "contato@condominiosobcontrole.com.br"),
	NOVA_NOTICIA("notificacaoNoticia.ftl", "Nova notícia!", "contato@condominiosobcontrole.com.br"),
	CONVOCACAO_ASSEMBLEIA("convocacaoAssembleia.ftl", "Convocação para assembleia", "contato@condominiosobcontrole.com.br"),
	NOVA_ASSEMBLEIA("avisoCriacaoAssembleia.ftl", "Aviso de nova assembleia", "contato@condominiosobcontrole.com.br"),
	REGISTRO_OCORRENCIA("novoRegistroOcorrencia.ftl", "Novo registro de ocorrência", "contato@condominiosobcontrole.com.br");

	private String nomeArquivo;
	private String assunto;
	private String de;



	private EnumTemplates(String nomeArquivo, String assunto, String de) {
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
