<html>

	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta charset="UTF-8">
	</head>

	<body>

		<h1>Prezado(a) ${nome_sindico}</h1>
		<br />
		<p>O morador ${nome_usuario} acabou de registrar uma nova ocorrência.</p>
		<p>Segue o relato:</p>

		<br/>

		<p>${msg}</p>

		<br/>
		<span style="color:blue"><p>Acesse o site www.condominiosobcontrole.com.br e veja mais detalhes dessa e de outras ocorrências.</p></span>
		<br/>
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