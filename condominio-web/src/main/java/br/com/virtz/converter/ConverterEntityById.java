package br.com.virtz.converter;

import java.lang.reflect.Field;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.Id;

/**
 * Converter padrão para ser usado quando a entidade envolvida tiver a
 * propriedade id.
 * 
 * Exemplo de utilização:
 * 
 * <p
 * :selectOneMenu id="userOrganization" value="#{userController.organization}"
 * converter="converterEntityById" <f:selectItems
 * value="#{userController.organizations}" var="o" itemLabel="#{o.name}"
 * itemValue="#{o}" />
 * </p:selectOneMenu>
 * 
 */
@FacesConverter(value = "converterEntityById")
public class ConverterEntityById implements Converter {
	
	
	@Override
	public Object getAsObject(FacesContext ctx, UIComponent component, String value) {
	/*	if (StringUtils.isNotBlank(value)) {
			return component.getAttributes().get(value);
		}*/
		return null;
	}
	
	@Override
	public String getAsString(FacesContext ctx, UIComponent component, Object obj) {
		if (obj != null && !"".equals(obj)) {
			String id;
			try {
				id = this.getId(this.getClazz(ctx, component), obj);
				if (id == null) {
					id = "";
				}
				id = id.trim();
				component.getAttributes().put(id, this.getClazz(ctx, component).cast(obj));
				return id;
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private Class<?> getClazz(FacesContext facesContext, UIComponent component) {
	/*	if (component instanceof PickList) {
			Object dualList = ((PickList) component).getValue();
			DualListModel dl = (DualListModel) dualList;
			return dl.getSource().isEmpty() ? dl.getTarget().isEmpty() ? null : dl.getTarget().iterator().next().getClass() : dl.getSource().iterator().next().getClass();
		} else if (component instanceof SelectManyMenu) {
			try {
				List list = (List) ((SelectManyMenu) component).getChildren().get(0).getValueExpression("value").getValue(facesContext.getELContext());
				return list.get(0).getClass();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		} else {
			return component.getValueExpression("value").getType(facesContext.getELContext());
		}*/
		return null;
	}
	
	public String getId(Class<?> clazz, Object obj) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		if (clazz == null) {
			return null;
		}
		for (Field field : clazz.getDeclaredFields()) {
			if (field.getAnnotation(Id.class) != null) {
				Field privateField = clazz.getDeclaredField(field.getName());
				privateField.setAccessible(true);
				if (privateField.get(clazz.cast(obj)) != null) {
					return field.getType().cast(privateField.get(clazz.cast(obj))).toString();
				} else {
					return null;
				}
			}
		}
		return null;
	}
	
}
