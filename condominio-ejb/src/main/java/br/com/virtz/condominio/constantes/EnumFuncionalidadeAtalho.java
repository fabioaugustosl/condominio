package br.com.virtz.condominio.constantes;

public enum EnumFuncionalidadeAtalho {
	NOTIFICAR_PORTARIA("Notificar portaria");
	
	private String descricao;

	private EnumFuncionalidadeAtalho(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public static EnumFuncionalidadeAtalho recuperarPorDescricao(String descricao){
		for(EnumFuncionalidadeAtalho tv : EnumFuncionalidadeAtalho.values()){
			if(tv.getDescricao().equals(descricao)){
				return tv;
			}
		}
		return null;
	}
	
}
