<html>

	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta charset="ISO-8859-1">
	</head>

	<body>

		<h1>Olá ${nomeUsuario}!</h1>
		<br />
		<br />
		<h3>Você recebeu um convite para fazer parte do seu condomínio online. </h3>

		<br/>
		<p>O responsável pelo seu condomínio (${nomeCondominio}), está te convidando a acessar o site www.condominiosobcontrole.com.br e ficar por dentro de tudo que acontece no seu condomínio. Lá você terá um canal direto com o sindico, poderá fazer reservas online, registrar ocorrências, acompanhar as assembleias e atas, entre outros.</p>
		<br  />
		<p>Para finalizar, clique no link abaixo. Assim você confirma seu cadastro e cria uma senha para acessar o site. Não perca tempo!</p>
		<a href="${link}" target="_blank">${link}</a>
		<br  />
		<br  />
		<p>Agrademos sua atenção e desejamos boas vindas. </p>

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