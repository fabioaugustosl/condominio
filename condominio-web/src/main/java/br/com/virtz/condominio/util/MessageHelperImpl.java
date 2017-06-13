package br.com.virtz.condominio.util;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

@ApplicationScoped
public class MessageHelperImpl implements MessageHelper{

	private static final long serialVersionUID = -1920177843301278214L;

	
    public void addInfo(String msg) {
        createMsgFacesContext(FacesMessage.SEVERITY_INFO, msg);
    }

    public void addWarn(String msg) {
    	createMsgFacesContext(FacesMessage.SEVERITY_WARN, msg);
    }

  
    public void addError(String msg) {
    	createMsgFacesContext(FacesMessage.SEVERITY_ERROR, msg);
    }

    public void addFatal(String msg) {
    	createMsgFacesContext(FacesMessage.SEVERITY_ERROR, msg);
    }

    private void createMsgFacesContext(FacesMessage.Severity severity, String msg){
    	setMsgFacesContext( severity, msg);
    }
    

    private void setMsgFacesContext(FacesMessage.Severity severity, String msg){
    	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, msg, null));
    }

	public void addError(Exception exception) {
		addError(exception.getMessage());
	}

}
