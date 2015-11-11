package br.com.virtz.boleto.util;

import java.util.ArrayList;
import java.util.List;

public class FormatadorTexto {

	public List<String> quebrarStrings(String texto, int tamanhoMaxLinha){
		if(tamanhoMaxLinha<1){
			tamanhoMaxLinha = 999;
		}
		List<String> linhas = new ArrayList<String>();
		
		String quebraLinha = "<br/>|<br>|<br />|<BR/>|<BR>|<BR />|\r\n|\n";
		//StringEscapeUtils.escapeHtml(quebraLinha)
		String[] quebrado = texto.split(quebraLinha);
		
		for(String t : quebrado){
			if(t.length() <= tamanhoMaxLinha){
				linhas.add(t);
			}else{
				String txtLongo = t;
				while(txtLongo.length() > tamanhoMaxLinha){
					String subsTxt = txtLongo.substring(0, (tamanhoMaxLinha));
					int ultimoEspaco = subsTxt.lastIndexOf(" ");
					String parte1Txt = txtLongo.substring(0,ultimoEspaco);
					linhas.add(parte1Txt);
					txtLongo = txtLongo.substring(ultimoEspaco).trim();
				}
				linhas.add(txtLongo);
			}
		}
		
		return linhas;
	}
	
}
