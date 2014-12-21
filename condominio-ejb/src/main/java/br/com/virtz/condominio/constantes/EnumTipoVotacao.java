package br.com.virtz.condominio.constantes;

public enum EnumTipoVotacao {
	SIM_NAO("Sim/Não"),
	DATA("Data"),
	MOEDA("Monetário"),
	NUMERICA("Numérica"),
	OPCOES("Opções cadastradas");
	
	private String descricao;

	private EnumTipoVotacao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public static EnumTipoVotacao recuperarPorDescricao(String descricao){
		for(EnumTipoVotacao tv : EnumTipoVotacao.values()){
			if(tv.getDescricao().equals(descricao)){
				return tv;
			}
		}
		return null;
	}
	
}
