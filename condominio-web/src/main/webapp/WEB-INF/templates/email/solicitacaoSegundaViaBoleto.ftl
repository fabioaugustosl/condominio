<html>

	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta charset="UTF-8">
	</head>

	<body>

		<h1>Prezados.</h1>

		<p>O morador ${nome_usuario} do condominio ${nome_condominio} solicita a segunda via dos boletos ainda não quitados.</p>
		<br/>
		<p>Por gentileza encaminhar os boletos para o morador. </p>
		<p>Os dados de contato são: </p>
		------------------------------------------------------------
		<br/>
		Nome: ${nome_completo} <br/>
		Email: ${email} <br/>
		Fone: ${fone} <br/>
		${nome_unidade}: ${nome_apto} <br/>
		${nome_bloco} <br/>
		${nome_agrupamento} <br/>

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