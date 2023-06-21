<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/WEB-INF/jsp/include-css.jsp"%>

    <!-- para file selector: -->
    <!-- Otras etiquetas de encabezado -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<title>TXT a XML</title>
</head>

<body>

	<!-- incluyendo header -->
	<%@ include file="/WEB-INF/jsp/header.jsp"%>


	<div class="tema">
		<h1>CONVERTIR TXT A XML</h1>
		<h2 class=error0>${error0}</h2>
		<h2>${error1}</h2>
		<h3>${error2}</h3>


	</div>




	<div class="container-wrapper">

		<div class="box separador2">
			<form method="post" action="abrirArchivoTxtXml"
				enctype="multipart/form-data">
				<div class="box">

					<label>Seleccione el archivo a convertir: </label> <input
						type="file" name="archivo" accept=".txt"> <br> <br>
					<label>Escriba el delimitador de los datos:</label> <br> <input
						type="text" name="delimitador" placeholder=","
						onFocus="field_focus(this, 'delimitador');"
						onblur="field_blur(this, 'delimitador');" class="del" /> <br>
					<input class="btn1" type="submit" value="Abrir Archivo">
				</div>
				<!-- End Box -->
			</form>

		</div>

		<div class="container-box box">

			<div class="box div-texto  custom-scrollbar">

				<br> <label>Archivo Cargado:</label> <br>
				<p>${textoArchivo1}</p>

			</div>
			<!-- box div-texto -->
		</div>
		<!-- container-box -->

		<div class="box separador2">

			<form method="post" action="convertirArchivoTxtXml">
				<div class="box">

					<label>Clave para encriptar tarjeta (16 caracteres):</label> <input type="text"
						name="llave" placeholder="abcdefgh"
						onFocus="field_focus(this, 'llave');"
						onblur="field_blur(this, 'llave');" class="clave" /> <br> <input
						class="btn2" type="submit" value="Generar">
				</div>
				<!-- End Box -->
			</form>
		</div>
		<!-- End separador2 -->



		<div class="container-box box">

			<div class="box div-texto  custom-scrollbar">

				<br> <label>Resultado:</label> <br>
				<xmp>${textoResultado1}</xmp>

			</div>
			<!-- box div-texto -->
		</div>
		<!-- container-box -->
		
		
		<div class="box separador2">
			<form method="get" action="descargarXML"
				enctype="multipart/form-data">
				<div class="box"> 
					<input class="btn1" type="submit" value="Descargar">
				</div>
				<!-- End Box -->
			</form>

		</div>
		
		

	</div>
	<!-- container-wrapper -->



</body>

</html>
