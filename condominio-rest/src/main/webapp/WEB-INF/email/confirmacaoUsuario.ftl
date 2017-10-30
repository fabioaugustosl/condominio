<html>

	<head>
	<meta http-equiv="Content-Type" content="text/html;" />
	<meta charset="ISO-8859-1">
	</head>

	<body>

		<h1>Ol\u00e1 ${nomeUsuario}!</h1>
		<br />
		<br />
		<h3>Bem vindo ao sistema de condom\u00ednio.</h3>

		<br/>
		<p>Seu cadastro foi realizado com sucesso!</p>
		<br  />
		<p>Para finalizar basta clicar no link abaixo e confirmar seu cadastro.</p>
		<a href="${link}" target="_blank">${link}</a>
		<br  />
		<br  />
		<p>Obrigado. </p>

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