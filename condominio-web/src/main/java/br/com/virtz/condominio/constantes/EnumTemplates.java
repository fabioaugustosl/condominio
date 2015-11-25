package br.com.virtz.condominio.constantes;

public enum EnumTemplates {
	PADRAO("padrao.ftl", "Informativo CondominioSOBCOntrole.com.br", "fabioaugustosl@yahoo.com.br"),
	CONFIRMACAO_USUARIO("confirmacaoUsuario.ftl", "Confirmação de usuário", "fabioaugustosl@yahoo.com.br"),
	LEMBRETE_ASSEMBLEIA("lembreteAssembleia.ftl", "Lembrete de assembléia", "fabioaugustosl@yahoo.com.br"),
	MENSAGEM_SINDICO("mensagemSindico.ftl", "Fale com o Síndico - Contato enviado através do site", "fabioaugustosl@yahoo.com.br"),
	BOLETO("boletoPorEmail.ftl", "Boleto", "fabioaugustosl@yahoo.com.br"),
	NOVO_MORADOR("novoMorador.ftl", "Novo Morador Cadastrado no Site", "fabioaugustosl@yahoo.com.br"),
	ESQUECI_MINHA_SENHA("esqueciMinhaSenha.ftl", "Esqueci minha senha", "fabioaugustosl@yahoo.com.br");

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
