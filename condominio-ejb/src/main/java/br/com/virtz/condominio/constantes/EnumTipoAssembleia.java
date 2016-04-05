package br.com.virtz.condominio.constantes;

public enum EnumTipoAssembleia {
	ORDINARIA("Ordinária"),
	EXTRAORDINARIA("Extraordinária");
	
	private String descricao;

	private EnumTipoAssembleia(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public static EnumTipoAssembleia recuperarPorDescricao(String descricao){
		for(EnumTipoAssembleia tv : EnumTipoAssembleia.values()){
			if(tv.getDescricao().equals(descricao)){
				return tv;
			}
		}
		return null;
	}
	
	
	@Override
	public String toString() {
		return this.descricao;
	}
	
}
