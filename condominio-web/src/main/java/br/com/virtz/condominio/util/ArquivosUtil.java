package br.com.virtz.condominio.util;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Random;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;

import br.com.virtz.condominio.exceptions.CondominioException;

@Named
@SessionScoped
public class ArquivosUtil implements IArquivosUtil, Serializable {
	
	private static final long serialVersionUID = -3351621178928638800L;
	
	public static final String TIPO_ARQUIVO_NOTICIA = "NOT";
	public static final String TIPO_ARQUIVO_ATA = "ATA";
	public static final String TIPO_ARQUIVO_DOCUMENTO = "DOC";
	public static final String TIPO_IMAGEM = "IMG";
	public static final String DIRETORIO_PADRAO_TEMPLATES = "WEB-INF\\templates\\email";
	
	private String[] extensoesTipoImagens = new String[]{"jpg" ,"JPG", "jpeg", "JPEG", "png", "PNG", "gif", "GIF"};

	
	public String getCaminhaPastaTemplatesEmail(){
		String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
	    if(path.contains("/")){
	    	path +=DIRETORIO_PADRAO_TEMPLATES.replace("\\", "/");
	    } else {
	    	path +=DIRETORIO_PADRAO_TEMPLATES;
	    }
	    
	    return path;
	}
	
	
	public String getCaminhoArquivosUpload(){
		return System.getProperty("jboss.server.base.dir")+"/arquivos/";  //FacesContext.getCurrentInstance().getExternalContext().getRealPath("/arquivos/");
	}
	
	
	public String pegarExtensao(String caminho){
		String extensao = caminho.substring(caminho.lastIndexOf(".") + 1);
		return extensao;
	}
	
	
	public String gerarNomeArquivo(String extensao, String tipoArquivo){
		Random gerador = new Random();
		Integer numero = gerador.nextInt(999999);
		Long numero2 = Calendar.getInstance().getTimeInMillis();
		return tipoArquivo+"_"+numero+numero2+"."+extensao;
	}
	
	public boolean tamanhoImagemEhValido(InputStream arquivo, int larguraMinima, int alturaMinima) throws CondominioException{
		Image image;
		try {
			image = ImageIO.read(arquivo);
		} catch (IOException e) {
			throw new CondominioException("Erro ao ler o arquivo.");
		}
	    int w = image.getWidth(null);
	    int h = image.getHeight(null);
	    
	    if(w < larguraMinima || h < alturaMinima){
	    	return Boolean.FALSE;
	    }
	    
	    return Boolean.TRUE;
	}
	
	public void arquivar(InputStream arquivo,  String nomeArquivo) throws IOException{
		File targetFolder = new File(this.getCaminhoArquivosUpload());
			
		OutputStream out = new FileOutputStream(new File(targetFolder, nomeArquivo));
			
        int read = 0;
        byte[] bytes = new byte[1024];

        while ((read = arquivo.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }
        arquivo.close();
        out.flush();
        out.close();
	}
	
	
	public void redimensionarImagem(InputStream arquivo, String nomeArquivo, String extensao, int larguraMaxima, int alturaMaxima) throws IOException{
        BufferedImage imagem = ImageIO.read(arquivo);
          
        Double novaImgLargura = (double) imagem.getWidth();  
        Double novaImgAltura = (double) imagem.getHeight();  
  
        Double imgProporcao = null;  
        if (novaImgLargura >= larguraMaxima) {  
            imgProporcao = (novaImgAltura / novaImgLargura);  
            novaImgLargura = (double) larguraMaxima;  
            novaImgAltura = (novaImgLargura * imgProporcao);  
        } 
        if (novaImgAltura >= alturaMaxima) {  
            imgProporcao = (novaImgLargura / novaImgAltura);  
            novaImgAltura = (double) alturaMaxima;  
            novaImgLargura = (novaImgAltura * imgProporcao);  
        }  
  
        BufferedImage novaImagem = new BufferedImage(novaImgLargura.intValue(), novaImgAltura.intValue(), BufferedImage.TYPE_INT_RGB);  
        Graphics g = novaImagem.getGraphics();  
        g.drawImage(imagem.getScaledInstance(novaImgLargura.intValue(), novaImgAltura.intValue(), 10000), 0, 0, null);  
        g.dispose();  
        
        File targetFolder = new File(this.getCaminhoArquivosUpload());
        ImageIO.write(novaImagem, extensao, new File(targetFolder, nomeArquivo));  
    } 
	
	
	
	public String getMimetypeArquivo(String extensao){
		if(extensao.equalsIgnoreCase("jpg") || extensao.equalsIgnoreCase("jpeg")){
			return "image/jpg";
		} else if(extensao.equalsIgnoreCase("pdf")){
			return "application/pdf";
		} else if(extensao.equalsIgnoreCase("doc")){
			return "application/msword";
		} else if(extensao.equalsIgnoreCase("gif")){
			return "image/jpg";
		} else if(extensao.equalsIgnoreCase("xls")){
			return "application/excel";
		}
		
		return null;
	}
	
	
	public boolean ehImagem(String extensao) {
		if(StringUtils.isNotBlank(extensao)){
			for(String tipo : extensoesTipoImagens){
				if(tipo.equals(extensao)){
					return Boolean.TRUE;
				}
			}
		}
		
		return Boolean.FALSE;
	}
	
}
