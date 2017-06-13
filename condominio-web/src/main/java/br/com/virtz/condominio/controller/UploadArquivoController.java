package br.com.virtz.condominio.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.FileUploadEvent;

import br.com.virtz.condominio.util.ArquivosUtil;
import br.com.virtz.condominio.util.IArquivosUtil;

@ManagedBean
@ViewScoped
public class UploadArquivoController {
	
	
	@Inject
	private IArquivosUtil arquivoUtil;
	

	
	
	@PostConstruct
	public void init(){
		
	}
		
	public String uploadArquivo(FileUploadEvent event, String prefixo) {
        try {
			InputStream inputStream = event.getFile().getInputstream();
			
			String caminho = arquivoUtil.getCaminhoArquivosUpload();
			String nomeAntigo = event.getFile().getFileName();
			String extensao = arquivoUtil.pegarExtensao(nomeAntigo);
			String nomeNovo = arquivoUtil.gerarNomeArquivo(extensao,prefixo);
			
			arquivoUtil.redimensionarImagem(inputStream, arquivoUtil.getCaminhoArquivosUpload(), nomeNovo, extensao, 1000, 1000);
			return nomeNovo;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
	

	
}

