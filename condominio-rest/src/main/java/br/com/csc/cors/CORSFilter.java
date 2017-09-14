package br.com.csc.cors;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class CORSFilter implements ContainerResponseFilter{

	@Override
	public void filter(ContainerRequestContext resquestCtx, ContainerResponseContext responseCtx) throws IOException {
		responseCtx.getHeaders().add( "Access-Control-Allow-Origin", "*" );
		responseCtx.getHeaders().add("Access-Control-Allow-Headers","origin, content-type, accept, authorization");
        responseCtx.getHeaders().add( "Access-Control-Allow-Credentials", "true" );
        responseCtx.getHeaders().add( "Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, PATCH" );

	}

}
