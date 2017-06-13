package br.com.virtz.condominio.constantes;

public enum EnumParametroSistema {
	AVISAR_POR_EMAIL_QUANDO_AGENDAR_AREA_COMUM(1l),
	QUANTIDADE_DIAS_MAXIMO_PARA_AGENDAR_AREA_COMUM(2l),
	AVISAR_POR_EMAIL_QUANDO_AXEXAR_ATA(3l),
	QUANTIDADE_DIAS_MINIMO_PARA_AGENDAR_AREA_COMUM(4l);
	
	private Long id;
	
	private EnumParametroSistema(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
	
}
