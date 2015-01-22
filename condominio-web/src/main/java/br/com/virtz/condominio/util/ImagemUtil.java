package br.com.virtz.condominio.util;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Random;

import javax.enterprise.context.SessionScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;

@SessionScoped
public class ImagemUtil implements Serializable {

	private static final long serialVersionUID = 1L;

//	@Inject
	private IArquivosUtil arquivoUtil;
	
	public BufferedImage carregarImagem(String caminho){
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(new File(caminho));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bi;
	}
	
	public void salvarNovaImagem(String caminho){
		BufferedImage img = carregarImagem(caminho);
		String extensao = arquivoUtil.pegarExtensao(caminho);
		BufferedImage bi = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D grph = (Graphics2D) bi.getGraphics();
		grph.drawImage(img, 0, 0, null);
		grph.dispose();
		try {
			ImageIO.write(bi, extensao, new File("/home/crisaltmann/Imagens/new_foto."+extensao));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public byte[] redimensionarImagem(String arquivo, double requiredWidth, double requiredHeight) {
 
        BufferedImage imagem = carregarImagem(arquivo);
 
        double originalWidth = imagem.getWidth();
        double originalHeight = imagem.getHeight();
        double newWidth = 0;
        double newHeight = 0;
        double diff = 0;
 
        if (requiredHeight == 0) {
            requiredHeight = requiredWidth;
        }
 
        if (requiredWidth == 0) {
            requiredWidth = requiredHeight;
        }
 
        if(originalWidth < requiredWidth && originalHeight < requiredHeight){
            return null; //read(arquivo);
        }
 
        if(requiredWidth == 0 && requiredHeight == 0){
            return null; //read(arquivo);
        }
 
        if (originalWidth > originalHeight) {
            diff = originalWidth - originalHeight;
            newWidth = requiredWidth;            
            diff = diff / originalWidth;
            newHeight = newWidth - (newWidth * diff);
        } else if (originalWidth < originalHeight) {
            diff = originalHeight - originalWidth;
            newHeight = requiredHeight;            
            diff = diff / originalHeight;
            newWidth = newHeight - (newHeight * diff);
        } else {
 
            if (requiredHeight > requiredWidth) {
                requiredHeight = requiredWidth;
            } else if (requiredHeight < requiredWidth) {
                requiredWidth = requiredHeight;
            }
 
            newHeight = requiredHeight;
            newWidth = requiredWidth;
        }
 
 
        int type = BufferedImage.TYPE_INT_RGB;
        boolean isPng = arquivo.toUpperCase().endsWith("PNG");
 
        if (isPng) {
            type = BufferedImage.BITMASK;
        }
 
 
        BufferedImage new_img = new BufferedImage((int) newWidth, (int) newHeight, type);
        Graphics2D g = new_img.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.drawImage(imagem, 0, 0, (int) newWidth, (int) newHeight, null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
 
        try {
	        if (isPng) {
	        	ImageIO.write(new_img, "PNG", out);
	        }else{
	            ImageIO.write(new_img, "JPG", out);
	        }
        } catch (IOException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
        }
 
        try {
            return out.toByteArray();
        } finally {
            try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

	public byte[] redimensionarImagemProporsionalmente(String caminho, double scale){
		BufferedImage img = carregarImagem(caminho);
		String extensao = arquivoUtil.pegarExtensao(caminho);
		BufferedImage bi = new BufferedImage((int) (scale * img.getWidth()), (int) (scale * img.getHeight()), BufferedImage.TYPE_INT_RGB); 
		Graphics2D grph = (Graphics2D) bi.getGraphics();
		grph.scale(scale, scale);
		grph.drawImage(img, 0, 0, null);
		grph.dispose();
		try {
			ImageIO.write(bi, extensao, new File("/home/crisaltmann/Imagens/new_foto.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public byte[] cortarImagem(String caminho, double scale){
		BufferedImage img = carregarImagem(caminho);
		String extensao = arquivoUtil.pegarExtensao(caminho);
		img = img.getSubimage(50, 50, 100, 200);
		BufferedImage bi = new BufferedImage((int) (scale * img.getWidth()), 
			(int) (scale * img.getHeight()), BufferedImage.TYPE_INT_RGB); 
		Graphics2D grph = (Graphics2D) bi.getGraphics();
		grph.scale(scale, scale);
		grph.drawImage(img, 0, 0, null);
		//return grph.dispose();
		return null;
		//O método getSubimage nos retorna um novo BufferedImage, descartando o restante. Basta você substituir os valores fixos por parâmetros recuperados do front-end.
	}
}
