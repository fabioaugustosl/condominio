<html>

	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta charset="UTF-8">
	</head>

	<body>

		<h1>Olá ${nome_sindico}.</h1>

		<p>O morador ${nome_usuario} enviou uma sugestão de pauta para a assembleia do dia ${data_assembleia}.</p>

		<p>A pauta sugerida foi:</p>
		<br/>
		<p><b>${msg}</b></p>

		<br/>
		<br/>
		Essa sugestão de pauta está com status <b>Não Aprovada</b>. Para aprová-la favor logar no site e acessar o menu Condomínio > Assembleias e clicar em Editar na assembleia desejada.
		<br/>
		<br/>
		Agora se quiser aprová-la imediatamente clique no link abaixo: <br />
		<a href="${link}" target="_blank">${link}</a>
		<br />
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
	</body>

</html>