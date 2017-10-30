<html>

	<head>
	<meta http-equiv="Content-Type" content="text/html;" />
	<meta charset="UTF-8">
	</head>

	<body>

		<h1>Ol\u00e1 ${nome_usuario}.</h1>
		<br />
		<p>Uma nova assembl\u00e9ia acabou de ser lan\u00e7ada no site. Acesse <a href="http://www.condominiosobcontrole.com.br" target="_blank">www.condominiosobcontrole.com.br</a> e veja mais informa\u00e7\u00f5es.</p>
		<br/>
		<p>Dados da assembleia:</p>
		------------------------------------------------------------
		<div>
			Data:<b> ${data} </b><br />
			Hor\u00e1rio 1\u00aa chamada: <b> ${chamada_1} </b><br />
			Hor\u00e1rio 2\u00aa chamada: <b> ${chamada_2} </b><br />
			Contamos com sua presen\u00e7a.
		</div>
		<br />
		<br />
		<table>
		<tr>
			<td style="width:50%">
				<a href="http://www.condominiosobcontrole.com.br">
				<img src="http://www.condominiosobcontrole.com.br/condominio/img/logo_pequena_azul.png?pfdrid_c=true" title="Condominio SOBControle"/><br/>
					www.condominiosobcontrole.com.br
				</a>
			</td>
			<td style="text-align:center; width:50%">
				<#if parceiro??>
					<img src="${parceiro}" style="max-width:300px; max-height:180px;" />
					<p style=" font-size:10px;">Email patrocinado pelo nosso parceiro</p>
				</#if>
			</td>
		</tr>
		</table>
		<p style="width:100%; text-align:center; font-size:10px;">Email enviado pelo site www.condominiosobcontrole.com.br. Favor n\u00e3o responder.</p>
	</body>

</html>