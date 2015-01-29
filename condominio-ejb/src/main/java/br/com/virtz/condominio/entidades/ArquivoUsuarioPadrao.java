package br.com.virtz.condominio.entidades;

public class ArquivoUsuarioPadrao extends ArquivoUsuario {

	public ArquivoUsuarioPadrao() {
		super();
		this.nome = "foto.jpg";
		this.caminho = "/arquivos/usuario/";
		this.extensao = "jpg";
		this.nomeOriginal = "foto.jpg";
	}

	public String getCaminhoCompleto(){
		return "/arquivos/usuario/"+getNome();
	}
	
}
