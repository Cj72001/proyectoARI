<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <%@include file="/WEB-INF/jsp/include-css.jsp" %>
    <%@include file="/WEB-INF/jsp/include-css2.jsp" %>
    <title>JSON a TXT</title>
</head>

<body>
    <!-- inluyendo header -->
    <%@include file="/WEB-INF/jsp/header.jsp" %>
    <div class="tema">
	  <h1>CONVERTIR JSON a TXT</h1>
	</div>
    
    <div class="container-box box">
      <div class=" box div-texto  custom-scrollbar">
        <div id="fileContent">
			
        </div>

      </div>
      <div class="box separador2">

        
        <div>
          <div class="button-container">
            <div class="input-container">
              <input id="fileInput" type="file">
              <button id="fileButton" class="button-convertir">Abrir Archivo</button>
            </div>
          </div>
          
          <div class="button-container">
            <div class="input-container">
              <input id="inputLlave" type="text" placeholder="Ingrese la llave" />
            </div>
          </div>
          <div class="button-container">
            <div class="input-container">
              <input id="inputDelimitador" type="text" placeholder="Ingrese delimitador" />
            </div>
          </div>
          <div class="button-container">
            <div class="input-container">
              <input id="fileInput" type="file">
              <button class="button-convertir" onclick="convertToJson()">Convertir</button>
            </div>
          </div>
          <div class="button-container">
            <div class="input-container separador">
              <button class="button-convertir" onclick="saveJson()">Guardar Archivo</button>
            </div>
          </div>
          
        </div>
      </div>
    </div>
<script>
let fileContent = null;
const jsonContentDiv = document.getElementById('jsonContent');

const fileButton = document.getElementById('fileButton');
const fileInput = document.getElementById('fileInput'); 
const fileContentDiv = document.getElementById('fileContent');
const inputLlave = document.getElementById('inputLlave');
const inputDelimitador = document.getElementById('inputDelimitador');

//Cuando se le da click al botón de buscar archivo
fileButton.addEventListener('click', function() {
  fileInput.click();
});

//Evento que se encarga de abrir arhivo
fileInput.addEventListener('change', function(){
  const file = fileInput.files[0];
  const reader = new FileReader();

  reader.onload = function(e) {
    fileContent = e.target.result;
    
    fileContentDiv.textContent = fileContent;
  };

  reader.readAsText(file);
});
//Al darle click al botón de convertir a JSON
function convertToJson() {
  if (!fileContent) {
    console.error('No se ha cargado ningún archivo.');
    return;
  }
  const llave = inputLlave.value; 
  const delimitador = inputDelimitador.value;
  
  const contenido = {
		  content: fileContent,
		  key: llave,
		  delimit: delimitador
		};

fetch('http://localhost:8080/convertToTxt', {
	  method: 'POST',
	  headers: {
	    'Content-Type': 'application/json'
	  },
	  body: JSON.stringify(contenido)
	})
	  .then(response => response.json())
	  .then(data => {
		  fileContentDiv.innerHTML = '';
		 
		  // Recorre la lista de objetos y crea elementos para mostrarlos en el div
		    data.forEach(object => {
		    	 const pElement = document.createElement('p');
		    	 pElement.textContent = `${object.documento}${delimitador}${object.nombres}${delimitador}${object.apellidos}${delimitador}${object.numeroTarjeta}${delimitador}${object.tipoTarjeta}${delimitador}${object.telefono}${delimitador}${object.poligono}`;
		    	fileContentDiv.appendChild(pElement);
		    });
		  
	  })
	  .catch(error => {
		  fileContentDiv.innerHTML = error.message;
	  });

}

//Función para guardar en el sistema de archivos
  async function saveJson() {
            if (fileContentDiv.textContent) {
                var filename = 'conversion.txt';
                await saveAsFile(filename, fileContentDiv.textContent);
            }
        }

        async function saveAsFile(filename, content) {
            try {
                const handle = await window.showSaveFilePicker({
                    suggestedName: filename,
                    types: [
                        {
                            description: 'Archivos de texto',
                           accept: {
                        	   'text/plain': ['.txt'],
                            },
                        },
                    ],
                });

                const writable = await handle.createWritable();
                await writable.write(content);
                await writable.close();

                console.log('Archivo de texto guardado exitosamente.');
            } catch (error) {
                console.error('Error al guardar el archivo:', error);
            }
        }
</script>
</body>

</html>