package br.com.virtz.condominio.constantes;

public enum EnumTemplates {
	PADRAO("padrao.ftl", "Informativo CondominioSOBCOntrole.com.br", "contatovirtz@gmail.com"),
	CONFIRMACAO_USUARIO("confirmacaoUsuario.ftl", "Confirmação de usuário", "contatovirtz@gmail.com"),
	LEMBRETE_ASSEMBLEIA("lembreteAssembleia.ftl", "Lembrete de assembléia", "contatovirtz@gmail.com"),
	MENSAGEM_SINDICO("mensagemSindico.ftl", "Fale com o Síndico - Contato enviado através do site", "contatovirtz@gmail.com"),
	BOLETO("boletoPorEmail.ftl", "Boleto", "contatovirtz@gmail.com"),
	NOVO_MORADOR("novoMorador.ftl", "Novo Morador Cadastrado no Site", "contatovirtz@gmail.com"),
	PAUTA_ENVIADA("pautaEnviada.ftl", "Pauta para assembléia enviada pelo site.", "contatovirtz@gmail.com"),
	ESQUECI_MINHA_SENHA("esqueciMinhaSenha.ftl", "Esqueci minha senha", "contatovirtz@gmail.com");

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
