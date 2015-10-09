package br.com.virtz.condominio.constantes;

public enum EnumTipoNotificacao {
	CORRESPONDENCIA("Correspondência"),
	ENCOMENDA("Encomenda"),
	AVISO("Aviso"),
	LEMBRETE("Lembrete");
	
	private String descricao;

	private EnumTipoNotificacao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public static EnumTipoNotificacao recuperarPorDescricao(String descricao){
		for(EnumTipoNotificacao tv : EnumTipoNotificacao.values()){
			if(tv.getDescricao().equals(descricao)){
				return tv;
			}
		}
		return null;
	}
	
}
