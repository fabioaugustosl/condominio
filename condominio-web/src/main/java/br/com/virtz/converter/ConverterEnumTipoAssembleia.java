package br.com.virtz.converter;

import javax.faces.convert.EnumConverter;
import javax.faces.convert.FacesConverter;

import br.com.virtz.condominio.constantes.EnumTipoAssembleia;

@FacesConverter(value = "converterEnumTipoAssembleia")
public class ConverterEnumTipoAssembleia extends EnumConverter  {

	public ConverterEnumTipoAssembleia() {
		super(EnumTipoAssembleia.class);
	}


	

}
