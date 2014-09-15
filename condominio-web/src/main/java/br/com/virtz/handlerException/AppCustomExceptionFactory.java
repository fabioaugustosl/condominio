package br.com.virtz.handlerException;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

public class AppCustomExceptionFactory extends ExceptionHandlerFactory {

	private ExceptionHandlerFactory parent;
	
	public AppCustomExceptionFactory(ExceptionHandlerFactory parent) {
		super();
		this.parent = parent;
	}

	@Override
	public ExceptionHandler getExceptionHandler() {
		return new CustomExceptionHandler(parent.getExceptionHandler());
	}

	
}
