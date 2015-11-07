package br.com.virtz.boleto.bean;

public enum EnumBanco {
	BB("Banco do Brasil","001"),
	SANTANDER("Santander","033");
	
	private String descricao;
	private String codigo;

	private EnumBanco(String descricao, String codigo) {
		this.descricao = descricao;
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}
	

	public String getCodigo() {
		return codigo;
	}

	public static EnumBanco recuperarPorCodigo(String codigo){
		for(EnumBanco tv : EnumBanco.values()){
			if(tv.getCodigo().equals(codigo)){
				return tv;
			}
		}
		return null;
	}
	
}
