<html>

	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	</head>

	<body>

		<h1>Olá ${nomeUsuario}!</h1>
		<br />
		<br />
		<h3>Um novo morador se cadatrou no site www.condominiosobcontrole.com.br para acessar o condomínio ${nomeCondominio}.</h3>

		<br/>
		<br/>
		<p>Seus dados para conferência são: </p>
		<br/>
		<p>Nome: ${nomeNovoUsuario}</p>
		<p>Apto: ${numeroApto}  - Bloco ${numeroBloco}</p>
		<br  />
		<br  />
		<br  />
		<p>Caso essa pessoa não seja morador do seu condomínio favor acessar o site e excluí-lo. No site você pode acessar mais informações de contato com essa pessoa.</p>
		<br  />
		<p>Obrigado. </p>

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