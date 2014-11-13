package br.com.virtz.condominio.bean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Email {
	
	private String de;
	private List<String> para;
	private String assunto;
	private String mensagem;
	
	
	public Email(String de, List<String> para, String assunto, String mensagem) {
		super();
		this.de = de;
		this.para = para;
		this.assunto = assunto;
		this.mensagem = mensagem;
	}
	
	public Email(String de, String para, String assunto, String mensagem) {
		super();
		this.de = de;
		this.para = new ArrayList<String>();
		this.para.add(para);
		this.assunto = assunto;
		this.mensagem = mensagem;
	}

	public String getDe() {
		return de;
	}

	public List<String> getPara() {
		return para;
	}

	public String getAssunto() {
		return assunto;
	}

	public String getMensagem() {
		return mensagem;
	}

	public String getParaToString(){
		StringBuilder paras = new StringBuilder();
		if(para != null && !para.isEmpty()){
			Iterator<String> it = para.iterator();
			while(it.hasNext()){
				String p = it.next();
				paras.append(p);
				if(it.hasNext()){
					paras.append(", ");
				}
			}
		}
		return paras.toString();
	}

}
