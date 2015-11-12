package br.com.virtz.condominio.constantes;

public enum EnumTipoBalanco {
	RECEITA("Receita"),
	DESPESA("Despesa");
	
	private String descricao;

	private EnumTipoBalanco(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public static EnumTipoBalanco recuperarPorDescricao(String descricao){
		for(EnumTipoBalanco tv : EnumTipoBalanco.values()){
			if(tv.getDescricao().equals(descricao)){
				return tv;
			}
		}
		return null;
	}
	
}
