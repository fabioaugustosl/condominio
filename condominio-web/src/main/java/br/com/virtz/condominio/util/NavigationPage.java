package br.com.virtz.condominio.util;

import java.io.Serializable;

import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.context.FacesContext;

public class NavigationPage implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private NavigationPage() {
		super();
	}
	
	public static void redirectToPage(String pathPage) {
		ConfigurableNavigationHandler nav = retrieveNavigationHandle();
		
		String pathParams = "faces-redirect=true&includeViewParams=true";
		
		if (!pathPage.endsWith("?faces-redirect=true")) {
			if (pathPage.contains("?")) {
				pathPage += "&" + pathParams;
			} else {
				pathPage += "?" + pathParams;
			}
		}
		
		nav.performNavigation(pathPage);
	}
	
	public static void forwardToPage(String pathPage) {
		ConfigurableNavigationHandler nav = retrieveNavigationHandle();
		nav.performNavigation(pathPage);
	}
	
	private static ConfigurableNavigationHandler retrieveNavigationHandle() {
		FacesContext context = FacesContext.getCurrentInstance();
		
		ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler) context.getApplication().getNavigationHandler();
		return nav;
	}
	
}