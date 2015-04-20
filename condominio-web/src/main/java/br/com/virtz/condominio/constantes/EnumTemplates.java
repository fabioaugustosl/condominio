package br.com.virtz.condominio.constantes;

public enum EnumTemplates {
	PADRAO("padrao.ftl"),
	CONFIRMACAO_USUARIO("confirmacaoUsuario.ftl"),
	ESQUECI_MINHA_SENHA("esqueciMinhaSenha.ftl");

	private String nomeArquivo;

	private EnumTemplates(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

}
