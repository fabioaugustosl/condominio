package br.com.virtz.condominio.util;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Random;

import javax.activation.MimetypesFileTypeMap;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.imageio.ImageIO;
import javax.inject.Named;
import javax.swing.ImageIcon;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.sun.mail.util.BASE64EncoderStream;

import br.com.virtz.condominio.exceptions.CondominioException;

@Named
@SessionScoped
public class ArquivosUtil implements IArquivosUtil, Serializable {
	
	private static final long serialVersionUID = -3351621178928638800L;
	
	public static final String TIPO_ARQUIVO_NOTICIA = "NOT";
	public static final String TIPO_ARQUIVO_ATA = "ATA";
	public static final String TIPO_ARQUIVO_DOCUMENTO = "DOC";
	public static final String TIPO_ARQUIVO_BALANCO = "BAL";
	public static final String TIPO_PORTARIA = "POR";
	public static final String TIPO_IMAGEM = "IMG";
	public static final String DIRETORIO_PADRAO_TEMPLATES = "WEB-INF\\templates\\email";
	public static final String THUMB_POS_FIXO = "_THUMB";
	
	private static final int MAX_THUMBNAIL_WIDTH = 100;
	
	private String[] extensoesTipoImagens = new String[]{"jpg" ,"JPG", "jpeg", "JPEG", "png", "PNG", "gif", "GIF"};

	
	public String getCaminhaPastaTemplatesEmail(){
		String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
	    if(path.contains("/")){
	    	if(!path.endsWith("/")){
	    		path += "/";
	    	}
	    	path +=DIRETORIO_PADRAO_TEMPLATES.replace("\\", "/");
	    } else {
	    	if(!path.endsWith("\\")){
	    		path += "\\";
	    	}
	    	path +=DIRETORIO_PADRAO_TEMPLATES;
	    }
	    
	    return path;
	}
	
	
	public String converterCaminhoImagemWindows(String caminho){
		return caminho.replace("/", "\\");
	}
	
	public String getCaminhoArquivosUpload(){
		return getCaminhoBaseArquivos()+"/arquivos/";
	}
	
	public String getCaminhoBaseArquivos(){
		return System.getProperty("jboss.server.base.dir");
	}
	
	public String getCaminhoUploadArquivosTemporario(){
		return FacesContext.getCurrentInstance().getExternalContext().getRealPath("/arquivos/"); 
	}
	
	public String getCaminhoBaseArquivosTemporario(){
		return FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"); 
	}
	
	public void copiarArquivos(String nomeArquivo) throws IOException{
		File arquivoDestino = new File(this.getCaminhoArquivosUpload());
		File arquivoOrigem = new File(this.getCaminhoBaseArquivosTemporario()+nomeArquivo);
		FileUtils.copyFile(arquivoOrigem, arquivoDestino);
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
		arquivar(arquivo, this.getCaminhoArquivosUpload(), nomeArquivo);
	}
	
	public void arquivar(InputStream arquivo, String pasta, String nomeArquivo) throws IOException{
		File targetFolder = new File(pasta);
			
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
	
	
	public StreamedContent exibir(String img){
		File arq = new File(getCaminhoBaseArquivos()+img);
		
		try {
			FacesContext context = FacesContext.getCurrentInstance();

		    if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
		        return new DefaultStreamedContent();
		    }
			StreamedContent stream  = new DefaultStreamedContent(new FileInputStream(arq), new MimetypesFileTypeMap().getContentType(arq));
			return stream;
			
        } catch (FileNotFoundException e) {
            return null;
        }
	}
	
	
	public void redimensionarImagem(InputStream arquivo, String pasta, String nomeArquivo, String extensao, int larguraMaxima, int alturaMaxima) throws IOException{
		redimensionarImagem(arquivo, pasta, nomeArquivo, extensao, larguraMaxima, alturaMaxima, 0, 0);
    } 
	
	public void redimensionarImagem(InputStream arquivo, String pasta, String nomeArquivo, String extensao, int larguraMaxima, int alturaMaxima, int larguraMinima, int alturaMinima) throws IOException{
        BufferedImage imagem = ImageIO.read(arquivo);
          
        Double novaImgLargura = (double) imagem.getWidth();  
        Double novaImgAltura = (double) imagem.getHeight();
        
        Double larguraTmp = 0d;  
        Double alturaTmp = 0d;  
  
        Double imgProporcao = null;  
        while (novaImgLargura > (double)larguraMaxima) {  
            imgProporcao = (novaImgAltura / novaImgLargura);  
            larguraTmp = novaImgLargura - (novaImgLargura * imgProporcao); 
            alturaTmp = novaImgAltura - (novaImgAltura * imgProporcao); 
            if((alturaTmp > 0 && larguraTmp > 0) && 
            		(alturaMinima == 0d && larguraMinima == 0d // eh pq o cara nao passou o minimo 
            		|| (alturaTmp > (double)alturaMinima && larguraTmp > (double)larguraMinima))){
            	novaImgAltura = alturaTmp;
            	novaImgLargura = larguraTmp;
            } else {
            	break;
            }
        } 

        while (novaImgAltura > (double)alturaMaxima) {  
            imgProporcao = (novaImgLargura / novaImgAltura);  
            larguraTmp = novaImgLargura - (novaImgLargura * imgProporcao); 
            alturaTmp = novaImgAltura - (novaImgAltura * imgProporcao); 
            if((alturaTmp > 0 && larguraTmp > 0) &&  
            		(alturaMinima == 0d && larguraMinima == 0d 
            			|| (alturaTmp > (double)alturaMinima && larguraTmp > (double)larguraMinima) )){
            	novaImgAltura = alturaTmp;
            	novaImgLargura = larguraTmp;
            } else {
            	break;
            }
        }  
        
        // se o cara não conseguiu achar uma proporção legal tenta reduzir pelo menos até o limite maximo passado
        if(novaImgLargura > larguraMaxima || novaImgAltura > alturaMaxima){
        	
        	larguraTmp = novaImgLargura;
            alturaTmp = novaImgAltura;
            
            int c = 0;
        	while((larguraTmp > (double) larguraMaxima || alturaTmp > (double) alturaMaxima) && c <= 8){
        		larguraTmp = ((double) larguraTmp * 0.80d);  
        		alturaTmp = ((double) alturaTmp * 0.80d);
        		c++;
        	}
        	
        	if(alturaMinima == 0d && larguraMinima == 0d 
            		|| (alturaTmp > (double)alturaMinima && larguraTmp > (double)larguraMinima) && alturaTmp > 0 && larguraTmp > 0){
            	novaImgAltura = alturaTmp;
            	novaImgLargura = larguraTmp;
            }
        }
  
        BufferedImage novaImagem = new BufferedImage(novaImgLargura.intValue(), novaImgAltura.intValue(), BufferedImage.TYPE_INT_RGB);  
        Graphics g = novaImagem.getGraphics();  
        g.drawImage(imagem.getScaledInstance(novaImgLargura.intValue(), novaImgAltura.intValue(), 10000), 0, 0, null);  
        g.dispose();  
        
        File targetFolder = new File(pasta);
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
	
	
	public byte[] converter(String arquivo){
		
		File f = new File(this.getCaminhoArquivosUpload()+arquivo);
		return converter(f);
	}
	
	public byte[] converter(File arquivo){
		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(arquivo);
			return IOUtils.toByteArray(fileInputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public byte[] converterParaBase64(File arquivo){
		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(arquivo);
			return Base64.encodeBase64(IOUtils.toByteArray(fileInputStream));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	public void gravarThumb(String nomeArquivo){
		
		File arq = new File(getCaminhoArquivosUpload()+nomeArquivo);
		
		ImageIcon imageIcon = new ImageIcon(converter(arq));  
		Image inImage = imageIcon.getImage();  
		double scale = (double) MAX_THUMBNAIL_WIDTH / (double) inImage.getWidth(null);  
		   
		int scaledW = (int) (scale * inImage.getWidth(null));  
		int scaledH = (int) (scale * inImage.getHeight(null));  
		   
		BufferedImage outImage = new BufferedImage(scaledW, scaledH, BufferedImage.TYPE_INT_RGB);  
		   
		AffineTransform tx = new AffineTransform();  
		   if(scale < 1.0d){  
		      tx.scale(scale, scale);  
		   }  
		   
		   Graphics2D g2d = outImage.createGraphics();  
		   g2d.drawImage(inImage, tx, null);  
		   g2d.dispose();  
		   
		   try {  
			   String ext = pegarExtensao(nomeArquivo);
		      ImageIO.write(outImage, ext,  new File(getCaminhoArquivosUpload(), getCaminhoCompletoThumb(nomeArquivo)));  
		   } catch (IOException e) {  
		      e.printStackTrace();  
		   }  
		} 
	
	public String getCaminhoCompletoThumb(String nomeArq){
		String ext = "."+pegarExtensao(nomeArq);
		String novoNome = nomeArq.replace(ext, "");
		return novoNome+ArquivosUtil.THUMB_POS_FIXO+ext;
	}


	@Override
	public String gerarNomeAleatorio(String prefixo) {
		int i = (int) (Math.random() * 10000000);
        if(StringUtils.isNotBlank(prefixo)){
        	return prefixo+"_"+String.valueOf(i);
        }
        	
        return String.valueOf(i);
	}
	
}
