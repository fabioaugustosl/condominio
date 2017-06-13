<html>

	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta charset="UTF-8">
	</head>

	<body>

		<h1>Olá ${nome_usuario}.</h1>

		<p>Terminou a votação: </p>
		<p> <strong> ${assunto_votacao} </strong></p>
		<br/>
		<p>Confira o resultado final:</p>
		<#list resultados as resultado>
		  <p>${resultado}</p>
		</#list>
		<br/>
		<#if anexos??>
		  	<#list anexos as anexo>
		 	${anexo}
		 	</#list>
		</#if>
		<br/>

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
		<p style="width:100%; text-align:center; font-size:10px;">Email enviado pelo site www.condominiosobcontrole.com.br. Favor não responder.</p>
	</body>

</html>