package br.com.virtz.converter;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.virtz.condominio.entidades.Entidade;


@FacesConverter(value = "converterEntityById")
public class ConverterEntityById implements Converter {
	
	
	@Override
	public Object getAsObject(FacesContext ctx, UIComponent component, String value) {
		if (value != null) {  
            return this.getAttributesFrom(component).get(value);  
        }  
        return null;  
	}
	
	@Override
	public String getAsString(FacesContext ctx, UIComponent component, Object value) {
		if (value != null && !"".equals(value)) {  
			Long codigo = null;
			
			if(value instanceof Long){
				codigo =  (Long) value;
			} else if(value instanceof Entidade){
				Entidade entity = (Entidade) value;  
				
				// adiciona item como atributo do componente  
				this.addAttribute(component, entity);  
				
				codigo =  entity.getId();  
			} else {
				codigo = Long.parseLong((String) value);
			}
			
            if (codigo != null) {  
                return String.valueOf(codigo);  
            }  
        }  
  
        return (String) value;  
	}
	
	protected void addAttribute(UIComponent component, Entidade o) {
		if(o != null && o.getId()!= null){
			String key = o.getId().toString(); // codigo da empresa como chave neste caso  
			this.getAttributesFrom(component).put(key, o);  
		}
    }  
  
    protected Map<String, Object> getAttributesFrom(UIComponent component) {  
        return component.getAttributes();  
    } 
	
}
