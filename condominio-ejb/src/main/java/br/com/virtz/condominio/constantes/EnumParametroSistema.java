package br.com.virtz.condominio.constantes;

public enum EnumParametroSistema {
	AVISAR_POR_EMAIL_QUANDO_AGENDAR_AREA_COMUM(1);
	
	private int id;
	
	private EnumParametroSistema(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
}
