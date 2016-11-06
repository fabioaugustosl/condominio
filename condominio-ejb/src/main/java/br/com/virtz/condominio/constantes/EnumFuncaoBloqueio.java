package br.com.virtz.condominio.constantes;

public enum EnumFuncaoBloqueio {
	RESERVA("Seu usuário foi bloqueado para efetuar reserva de área comum. Favor contactar o síndico.");

	private String msgParaUsuario;

	private EnumFuncaoBloqueio(String msgParaUsuario) {
		this.msgParaUsuario = msgParaUsuario;
	}

	public String getMsgParaUsuario() {
		return msgParaUsuario;
	}

}
