package br.com.virtz.condominio.constantes;

public enum EnumPeriodicidadeLembrete {
	MENSAL("Mensal"),
	SEMESTRAL("Semestral"),
	ANUAL("Anual");
	
	private String descricao;

	private EnumPeriodicidadeLembrete(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public static EnumPeriodicidadeLembrete recuperarPorDescricao(String descricao){
		for(EnumPeriodicidadeLembrete tv : EnumPeriodicidadeLembrete.values()){
			if(tv.getDescricao().equals(descricao)){
				return tv;
			}
		}
		return null;
	}
	
}
