package br.com.virtz.condominio;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ImagemServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	
		String arquivo = req.getParameter("arquivo");
		
		 // Image bytes
        byte[] imageBytes = null;
        BufferedInputStream in;
        try {
			in = new BufferedInputStream(new FileInputStream(System.getProperty("jboss.server.base.dir")+arquivo));
			imageBytes=new byte[in.available()];
			in.read(imageBytes);
			in.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("[ERRO] File Not Found ao exibir a imagem "+arquivo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("[ERRO] IOException ao exibir a imagem "+arquivo);
		}

         
        resp.getOutputStream().write(imageBytes);
        resp.getOutputStream().close();

	}
}
