package br.com.virtz.condominio.constantes;

public enum EnumTaxaCreditoDebito {
	C("Crédito"),
	D("Débito");
	
	private String descricao;

	private EnumTaxaCreditoDebito(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public static EnumTaxaCreditoDebito recuperarPorDescricao(String descricao){
		for(EnumTaxaCreditoDebito tv : EnumTaxaCreditoDebito.values()){
			if(tv.getDescricao().equals(descricao)){
				return tv;
			}
		}
		return null;
	}
	
}
