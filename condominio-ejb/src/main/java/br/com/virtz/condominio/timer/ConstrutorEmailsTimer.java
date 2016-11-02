package br.com.virtz.condominio.timer;

import java.util.List;

import br.com.virtz.condominio.entidades.Usuario;

public class ConstrutorEmailsTimer {


	public ConstrutorEmailsTimer() {
	}

	/**
	 * Monta o template do email a ser enviado.
	 * Copiei e colei o resultadoVotacao.ftl.. tentar manter compatibilidade.
	 * @param u
	 * @return
	 */
	public String montarTextoEmail(Usuario u, String tituloVotacao, List<String> resultado){
		StringBuilder sb = new StringBuilder();

		sb.append("<html>");
		sb.append("<head>");
		sb.append("<head>");
		sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\" />");
		sb.append("<meta charset=\"ISO-8859-1\">");
		sb.append("</head>");

		sb.append("<body>");
		sb.append("<h1>Olá ").append(u.getNomeExibicao()).append(".</h1>");
		sb.append("<p>Terminou a vota&ccedil;&atilde;o: </p>");
		sb.append("<p> <strong>").append(tituloVotacao).append("</strong></p> <br />");
		sb.append("<p>Confira o resultado final:</p>");
		for(String r : resultado){
			sb.append("<p> ").append(r).append("</p>");
		}
		sb.append("<br /><br />");
		sb.append("<a href=\"http://www.condominiosobcontrole.com.br\">");
		sb.append("<img src=\"http://www.condominiosobcontrole.com.br/condominio/img/logo_pequena_azul.png?pfdrid_c=true\" title=\"Condominio SOBControle\"/><br/>www.condominiosobcontrole.com.br</a>");


		sb.append("</body>");
		sb.append("</html>");
		return sb.toString();
	}



	/**
	 * Monta o template do email a ser enviado para lembrar o sindico de anexar a ata da assembleia realizada.
	 * @param u
	 * @return
	 */
	public String montarTextoEmailLembreAta(Usuario u, String data){
		StringBuilder sb = new StringBuilder();

		sb.append("<html>");
		sb.append("<head>");
		sb.append("<head>");
		sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\" />");
		sb.append("<meta charset=\"ISO-8859-1\">");
		sb.append("</head>");

		sb.append("<body>");
		sb.append("<h1>Olá ").append(u.getNomeExibicao()).append(".</h1>");
		sb.append("<p>Gostar&iacute;amos de lembr&aacute;-lo que ainda n&atilde;o foi anexada nenhuma ata referente a assembleia realizada dia ").append(data).append(".");
		sb.append("<br /><br />");
		sb.append("<br /><br />");
		sb.append("<a href=\"http://www.condominiosobcontrole.com.br\">");
		sb.append("<img src=\"http://www.condominiosobcontrole.com.br/condominio/img/logo_pequena_azul.png?pfdrid_c=true\" title=\"Condominio SOBControle\"/><br/>www.condominiosobcontrole.com.br</a>");


		sb.append("</body>");
		sb.append("</html>");
		return sb.toString();
	}


		/**
		 * Monta o template do email a ser enviado para lembrar o sindico de anexar a ata da assembleia realizada.
		 * @param u
		 * @return
		 */
		public String montarTextoEmailLembreteAssembleia(Usuario u, String data, String horario1, String horario2){
			StringBuilder sb = new StringBuilder();

			sb.append("<html>");
			sb.append("<head>");
			sb.append("<head>");
			sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\" />");
			sb.append("<meta charset=\"ISO-8859-1\">");
			sb.append("</head>");

			sb.append("<body>");
			sb.append("<h1>Ol&aacute; ").append(u.getNomeExibicao()).append(".</h1>");
			sb.append("<p>Gostar&iacute;amos de lembr&aacute;-lo que a assembleia do condominio est&aacute; chegando! Programe-se.</p> ");
			sb.append("<br />");
			sb.append("<p>Dados da assembleia: </p>");
			sb.append("<p>Data: ").append(data).append("</p>");
			sb.append("<p>Primeira chamada: ").append(horario1).append("</p>");
			sb.append("<p>Segunda chamada: ").append(horario2).append("</p>");
			sb.append("<br /><br />");
			sb.append("<a href=\"http://www.condominiosobcontrole.com.br\">");
			sb.append("<img src=\"http://www.condominiosobcontrole.com.br/condominio/img/logo_pequena_azul.png?pfdrid_c=true\" title=\"Condominio SOBControle\"/><br/>www.condominiosobcontrole.com.br</a>");


			sb.append("</body>");
			sb.append("</html>");
			return sb.toString();
		}



		/**
		 * Monta o template do email a ser enviado avisando de nova votacao.
		 * @param u
		 * @return
		 */
		public String montarTextoEmailNovaVotacao(Usuario u, String tituloVotacao){
			StringBuilder sb = new StringBuilder();

			sb.append("<html>");
			sb.append("<head>");
			sb.append("<head>");
			sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\" />");
			sb.append("<meta charset=\"ISO-8859-1\">");
			sb.append("</head>");

			sb.append("<body>");
			sb.append("<h1>Ol&aacute; ").append(u.getNomeExibicao()).append(".</h1>");
			sb.append("<p>Uma nova vota&ccedil;&atilde;o est&aacute; acontecendo no site! </p>");
			sb.append("<br /><p>A vota&ccedil;&atilde;o &eacute;: </p>");
			sb.append("<p> <strong>").append(tituloVotacao).append("</strong></p> <br />");
			sb.append("<br /><p>Acesse o site registre seu voto. &Eacute; muito importante a participa&ccedil;&atilde;o de todos os moradores. </p>");

			sb.append("<br /><br />");
			sb.append("<a href=\"http://www.condominiosobcontrole.com.br\">");
			sb.append("<img src=\"http://www.condominiosobcontrole.com.br/condominio/img/logo_pequena_azul.png?pfdrid_c=true\" title=\"Condominio SOBControle\"/><br/>www.condominiosobcontrole.com.br</a>");


			sb.append("</body>");
			sb.append("</html>");
			return sb.toString();
		}

}