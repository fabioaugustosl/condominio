package br.com.virtz.handlerException;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import org.apache.commons.lang.StringUtils;


public class CustomExceptionHandler extends ExceptionHandlerWrapper {

	private ExceptionHandler wrapped;


	public CustomExceptionHandler(ExceptionHandler wrapped) {
		super();
		this.wrapped = wrapped;
	}

	@Override
	public ExceptionHandler getWrapped() {
		return this.wrapped;
	}

	/**
	 *  Sobrescreve o método responsável por manipular as exceções do JSF.
	 */
	@Override
	public void handle() throws FacesException {

		final Iterator<?> iterator = this.getUnhandledExceptionQueuedEvents().iterator();

		while (iterator.hasNext()) {
			ExceptionQueuedEvent event = (ExceptionQueuedEvent) iterator.next();
			ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();

			Throwable exception = context.getException();
			
			exception.printStackTrace();
			
			String msg = "";
			try {
				FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("exceptionMessage", exception.getMessage());

				msg = this.retrieveTranslatedMessage(exception);
				
				FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage( FacesMessage.SEVERITY_ERROR, msg, null));
				//this.handleRedirect(FacesContext.getCurrentInstance());

				FacesContext.getCurrentInstance().renderResponse();
			} finally {
				FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
				if(StringUtils.isBlank(msg)){
					FacesContext.getCurrentInstance().addMessage(null,	new FacesMessage( FacesMessage.SEVERITY_ERROR, exception.getLocalizedMessage(), ""));
				}

				iterator.remove();
			}
		}
		this.getWrapped().handle();
	}

	protected void handleRedirect(FacesContext context) {
		String errorPage = context.getExternalContext().getInitParameter("error-page");
		try {
			context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + errorPage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void buildDetailError(Throwable exception, StringBuilder detail) {

		detail.append("\n" + exception.getMessage());

		for (int i = 0; i < exception.getStackTrace().length; i++) {
			detail.append(exception.getStackTrace()[i]).append("\n");
		}
	}

	private String retrieveTranslatedMessage(Throwable exception) {
		if(exception.getCause() != null){
			// tenta até o terceiro nível a tradução da mensagem
			Throwable cause = exception.getCause();
			for(int i = 0; i < 20; i++){
				if(cause.getLocalizedMessage() == null){
					return cause.toString();
				}
				if(!cause.getLocalizedMessage().startsWith("/")){
					return cause.getLocalizedMessage();
				}
				if(cause.getCause() != null){
					cause = cause.getCause();
				} else {
					return cause.toString();
				}
			}
		} else {
			return exception.getMessage();
		}
		return "";
	}

/*	private AppException getAppException(Throwable exception) {
		Throwable cause = exception.getCause();
		// tenta até o terceiro nível a tradução da mensagem
		for(int i = 0; i < 5; i++){
			if(cause == null){
				return null;
			}
			if(cause instanceof AppException) {
				return (AppException) cause;
			}
			cause = cause.getCause();
		}
		return null;
	}*/


}
