package br.com.virtz.condominio.constantes;

public enum EnumTipoRecebido {
	CORRESPONDENCIA("Correspondência"),
	ENCOMENDA("Encomenda");
	
	private String descricao;

	private EnumTipoRecebido(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public static EnumTipoRecebido recuperarPorDescricao(String descricao){
		for(EnumTipoRecebido tv : EnumTipoRecebido.values()){
			if(tv.getDescricao().equals(descricao)){
				return tv;
			}
		}
		return null;
	}
	
}
