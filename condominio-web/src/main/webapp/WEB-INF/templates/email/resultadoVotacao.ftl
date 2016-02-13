<html>

	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
	<meta charset="ISO-8859-1">
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
		<br/>
		
		<a href="http://www.condominiosobcontrole.com.br">
			<img src="http://www.condominiosobcontrole.com.br/condominio/img/logo_pequena_azul.png?pfdrid_c=true" title="Condominio SOBControle"/><br/>
			www.condominiosobcontrole.com.br
		</a>
	</body>

</html>