<html>

	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta charset="UTF-8">
	</head>
	<style>
		h1{width:100%; text-align:center; font-weight: bold;}
		h2{width:100%; text-align:center; color: #525A6E; }
	</style>
	<body>

		<h2 style="width:100%; text-align:center;">${nome_condominio}</h2>
		<h1 style="width:100%; text-align:center;">Convocação Assembleia Geral ${tipo_assembleia} </h1>

		<p>Ficam convocados os Srs. Proprietários do Edifício ${nome_condominio} situado na ${endereco}, ${numero}, ${bairro},
		para a Assembleia Geral ${tipo_assembleia} a realizar-se no dia ${data} às ${chamada_1} horas em primeira chamada
		e ${chamada_2} horas em segunda chamada, com qualquer número de presentes, para deliberar sobre os seguintes assuntos:</p>
		<br/>

		<#list pautas as pauta>
		  <p>* ${pauta}</p>
		</#list>

		<br />
		<p>Local: ${local}</p>
		<br/>
		<p>OBSERVAÇÕES: É lícito aos senhores proprietários se fazerem representar na Assembleia Geral ${tipo_assembleia} por procuradores munidos de procurações específicas, preferencialmente com Reconhecimento de Firma. A ausência dos senhores Proprietários/ Condôminos não os desobrigam de aceitarem como tácita concordância aos assuntos que foram tratados e deliberados. Os condôminos em atraso nos pagamentos de suas taxas condominiais não poderão votar nas deliberações. ( As respectivas representação deverão ser entregues ao conselho antes do inicio da assembleia e caso as mesmas não sejam entregues elas perderão o direito de voto).</p>
		<br/>
		<p>Atenciosamente, </p>
		<p>${nome_sindico}</p>
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