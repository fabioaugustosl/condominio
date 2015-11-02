package br.com.virtz.condominio.constantes;

public enum EnumTaxaPorcentagemValor {
	P("Porcentagem"),
	V("Valor");
	
	private String descricao;

	private EnumTaxaPorcentagemValor(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public static EnumTaxaPorcentagemValor recuperarPorDescricao(String descricao){
		for(EnumTaxaPorcentagemValor tv : EnumTaxaPorcentagemValor.values()){
			if(tv.getDescricao().equals(descricao)){
				return tv;
			}
		}
		return null;
	}
	
}
