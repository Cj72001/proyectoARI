package com.uca.spring.controller;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.http.HttpHeaders;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.uca.spring.model.Cliente;
import com.uca.spring.model.ClienteJSON;
import com.uca.spring.model.ClienteXml;
import com.uca.spring.util.Util;





@Controller
@RequestMapping("/")
public class AppController { //Comentario de prueba
	private List<ClienteJSON> listaDeClientes;
		//Variables globales:
	  //Contenido del txt:
	  String fileContent = "";
	  
	  //contenido del txt exactamente con los saltos:
	  String archivoCargadoTxtXmlStr = "";
	  
	//Array para separar el contenido del documento segun el delimitador TXT a XML
	  String[] datosArrayTxtXml = new String[]{};
	  
	  //Array para separar los clientes
	  String[] clientesArrayTxtXml = new String[]{};
	  
	  //LinkedList para almacenar los clientes (objetos)
	  LinkedList<Cliente> clientes = new LinkedList<>();
	  
	  //XML a TXT
	  String archivoCargadoXmlTxtStr = "";

	//Action que se invoca al iniciar la app en la ruta del login (/)
  @GetMapping("/")
  public String getForm(ModelMap modelMap) {
	  
	  //Inicializando resultados
	  fileContent = "";
	  archivoCargadoTxtXmlStr = "";
	  datosArrayTxtXml = new String[]{};
	  modelMap.put("textoArchivo1", "No se ha cargado ningun archivo");
	  modelMap.put("textoResultado1", "No se ha convertido ningun archivo");
	  
    return "txt_xml.jsp";
  }
  
  //Actions para rutas (header):
  @GetMapping("/txtXml")
  public String txtXml(ModelMap modelMap) {
	
	  //Inicializando resultados
	  fileContent = "";
	  archivoCargadoTxtXmlStr = "";
	  archivoCargadoXmlTxtStr = "";
	  datosArrayTxtXml = new String[0];
	  clientes.clear();
	  txtStr = "";
	  modelMap.put("textoArchivo1", "No se ha cargado ningun archivo");
	  modelMap.put("textoResultado1", "No se ha convertido ningun archivo");
	  
    return "txt_xml.jsp";
  } 
  
  @GetMapping("/xmlTxt")
  public String xmlTxt(ModelMap modelMap) {
	  
	//Inicializando resultados
	  fileContent = "";
	  archivoCargadoTxtXmlStr = "";
	  archivoCargadoXmlTxtStr = "";
	  datosArrayTxtXml = new String[0];
	  clientes.clear();
	  txtStr = "";
	  modelMap.put("textoArchivo1", "No se ha cargado ningun archivo");
	  modelMap.put("textoResultado1", "No se ha convertido ningun archivo");  
	  return "xml_txt.jsp";
  } 
  
  @GetMapping("/descargarXML")
  public ResponseEntity<byte[]> descargarXML() throws JAXBException {
	  
	// Crear el contexto JAXB
      JAXBContext context = JAXBContext.newInstance(ClienteXml.class);

      // Crear el marshaller
      Marshaller marshaller = context.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);

      ClienteXml clientesXml = new ClienteXml();
      clientesXml.setClientes(clientes);

      // Crear un ByteArrayOutputStream para almacenar el contenido XML en memoria
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

      // Marshalizar el objeto a XML y escribirlo en el ByteArrayOutputStream
      marshaller.marshal(clientesXml, outputStream);

      // Obtener el contenido XML como un arreglo de bytes
      byte[] contenidoXML = outputStream.toByteArray();

      org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_XML);
      headers.setContentDispositionFormData("attachment", "objeto.xml");
      
      System.out.println("Archivo XML guardado correctamente.");
      //Limpiando variables
      txtStr = "";
      clientes.clear();

      return new ResponseEntity<>(contenidoXML, headers, org.springframework.http.HttpStatus.OK);
  }
  
  @GetMapping("/descargarTxt")
  public ResponseEntity<byte[]> descargarTxt() {
	  
		// Generar los datos del archivo
	      String contenidoArchivo = Util.generarContenidoArchivoTxt(clientes);
	      byte[] archivoBytes = contenidoArchivo.getBytes();

	      // Configurar los encabezados de la respuesta
	      org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
	      headers.add(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=objeto.txt");
	      headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

	      System.out.println("Archivo TXT guardado correctamente.");
	      //Limpiando variables
	      txtStr = "";
	      clientes.clear();
	      
	      // Devolver el archivo como una respuesta HTTP
	      return ResponseEntity
	              .ok()
	              .headers(headers)
	              .body(archivoBytes);
      
  }
  
  @GetMapping("/txtJson")
  public String txtJson() {
	  
    return "txt_json.jsp";
  } 
  
  @GetMapping("/jsonTxt")
  public String jsonTxt() {
	  
    return "json_txt.jsp";
  } 
  
  //____________________________________________________________________________________________________________
  //Actions para post:
  
  @PostMapping("/abrirArchivoTxtXml")   
  public String abrirArchivoTxtXml(ModelMap modelMap,
		  @RequestParam("delimitador") String delimitador,  
		  @RequestParam("archivo") MultipartFile file){ 
	  
	  
		  if (!file.isEmpty()) {
			  
		        // Verificar el tipo de archivo
		        if (file.getContentType().equals("text/plain")) {
		            try {
		            	fileContent = new String(file.getBytes(), "UTF-8");
		            } catch (IOException e) {
		                e.printStackTrace();
		                // Manejar el error de lectura del archivo
		                modelMap.put("error1","Error de lectura de archivo, vuelva a intentarlo");
		                
		                modelMap.put("textoArchivo1", "No se ha cargado ningun archivo");
		          	  	modelMap.put("textoResultado1", "No se ha convertido ningun archivo");
			  		  	return "txt_xml.jsp";
		            }
		        } else {
		            // El archivo seleccionado no es un archivo TXT
		            // Manejar el error de tipo de archivo no válido
		        	modelMap.put("error1","Seleccione un archivo valido (txt)");
		  		  	return "txt_xml.jsp";
		        }
		    } else {
		        // No se seleccionó ningún archivo
		        // Manejar el error de archivo no seleccionado
		    	modelMap.put("error1","No se ha seleccionado ningun archivo, seleccionelo");
		    	modelMap.put("textoArchivo1", "No se ha cargado ningun archivo");
		  	  	modelMap.put("textoResultado1", "No se ha convertido ningun archivo");
	  		  	return "txt_xml.jsp";
		    }
		  
		  	if(delimitador.isEmpty()) {
		  		modelMap.put("archivo",file);
		  		modelMap.put("error1","Escriba el delimitador antes de abrir el archivo");
		  		modelMap.put("error2","Vuelve a seleccionar el archivo");
		  		modelMap.put("textoArchivo1", "No se ha cargado ningun archivo");
		  		modelMap.put("textoResultado1", "No se ha convertido ningun archivo");
		  		
	  		  	return "txt_xml.jsp";
		  	  }
		    
		    // Realizar acciones con el contenido del archivo
		  	
		  	//se guardan los clientes en
		  	clientesArrayTxtXml = fileContent.split("\n");
		  	
		  	for(String cliente : clientesArrayTxtXml) {
		  		datosArrayTxtXml = cliente.split(delimitador);
		  		
		  		if(datosArrayTxtXml.length != 7) {
			  		//No se introducieron toda la informacion del cliente
			  		modelMap.put("error1","Introduzca un archivo txt con el numero de datos establecidos");
		  		  	return "txt_xml.jsp";
			  	}
		  		else {
		  			//Se introdujo la informacion correctamente
		  			//Se crea un cliente (objeto) y se guarda en la lista de clientes (objetos)
			  		Cliente clienteObjeto = new Cliente(datosArrayTxtXml[0], datosArrayTxtXml[1], 
			  				datosArrayTxtXml[2], datosArrayTxtXml[3], 
			  				datosArrayTxtXml[4], datosArrayTxtXml[5],
			  				datosArrayTxtXml[6]);
			  		clientes.addLast(clienteObjeto);
		  		}
		  	}
		  	
		  	//Para imprimir exactamente como esta en el txt (fileContent)
		  	for(String c : clientesArrayTxtXml) {
		  		archivoCargadoTxtXmlStr += c + "<br>";
		  	}
		  	
		  		modelMap.put("error0", "Archivo Cargado Correctamente");
		  		modelMap.put("textoArchivo1", archivoCargadoTxtXmlStr);
		  		modelMap.put("textoResultado1", "No se ha convertido ningun archivo");
			  	return "txt_xml.jsp";
  } 
  
  
  @PostMapping("/convertirArchivoTxtXml")   
  public String convertirArchivoTxtXml(ModelMap modelMap, 
		  @RequestParam("llave") String llave) throws Exception{ 
	  
	  if(clientes.isEmpty()) {
		  modelMap.put("textoArchivo1", "No se ha cargado ningun archivo");
		  modelMap.put("textoResultado1", "No se ha convertido ningun archivo");
		  modelMap.put("error1", "Cargue un archivo txt antes");
		  return "txt_xml.jsp";
	  }
	  
	  if(llave.isEmpty()) {
		  modelMap.put("textoArchivo1", archivoCargadoTxtXmlStr);
		  modelMap.put("textoResultado1", "No se ha convertido ningun archivo");
		  modelMap.put("error1", "Escriba la clave antes de guardar el archivo convertido");
		  return "txt_xml.jsp";
	  }
	  
	  if(llave.length() < 16) {
		  modelMap.put("textoArchivo1", archivoCargadoTxtXmlStr);
		  modelMap.put("textoResultado1", "No se ha convertido ningun archivo");
		  modelMap.put("error1", "Escriba una clave valida (16 caracteres)");
		  return "txt_xml.jsp";
	  }
	  
	  	  //Encriptando los numeros de tarjeta de cada cliente:
		  clientes.forEach(c->{
			  String encryptedText = "";
			  
			//Encriptando numero de tarjeta de credito:
			  try {
				encryptedText = Util.encrypt(c.getNumeroTarjeta(), llave);
				//Desencriptar:
				//String decryptedText = Util.decrypt(c.getNumeroTarjeta(), llave);
			} catch (Exception e) {
				System.out.println("Hubo un error encriptando la tarjeta");
				e.printStackTrace();  
			}
			  
			  
			//Seteando numero de tarjeta encriptada:
			  c.setNumeroTarjeta(encryptedText);
		  }); 
		  
	  //PARA MOSTRAR EL RESULTADO QUE SE DESCARGARA
	  String xmlStr = Util.xmlStr(clientes);
	  
	  		
	  modelMap.put("error0", "Archivo Convertido Correctamente");
	  modelMap.put("textoArchivo1", archivoCargadoTxtXmlStr);
	  modelMap.put("textoResultado1", xmlStr);	 
	  archivoCargadoTxtXmlStr = "";
	  return "txt_xml.jsp";
  } 
  
  
  
  
  //XML A TXT
  @PostMapping("/abrirArchivoXmlTxt")
  public String abrirArchivoXmlTxt(ModelMap modelMap, @RequestParam("archivo") MultipartFile file) {

      if (!file.isEmpty()) {
          // Verificar el tipo de archivo
          if (file.getContentType().equals("text/xml")) {
              try {
            	  
                  // Obtener el contenido del archivo XML como cadena
                  byte[] fileBytes = file.getBytes();
                  archivoCargadoXmlTxtStr = new String(fileBytes, StandardCharsets.UTF_8);
                  
                  
                  // Crear un contexto JAXB y unmarshaller
                  JAXBContext jaxbContext = JAXBContext.newInstance(ClienteXml.class);
                  Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

                  // Obtener el InputStream del archivo MultipartFile
                  InputStream inputStream = file.getInputStream();

                  // Leer el archivo XML y convertirlo en objeto ClienteXml
                  ClienteXml clienteXml = (ClienteXml) unmarshaller.unmarshal(inputStream);
                  
                  // Acceder a la lista de clientes
                  List<Cliente> listaClientes = clienteXml.getClientes();
                  
                  // Procesar la lista de clientes
                  for (Cliente cliente : listaClientes) {
                      clientes.addLast(cliente);
                  }

                  modelMap.put("textoArchivo1", archivoCargadoXmlTxtStr);
                  modelMap.put("textoResultado1", "No se ha convertido ningun archivo");
                  return "xml_txt.jsp";
                  
              } catch (Exception e) {
                  e.printStackTrace();
                  // Manejar el error de lectura o procesamiento del archivo
                  modelMap.put("error1", "Error al leer o procesar el archivo XML");
              }
          } else {
              // El archivo seleccionado no es un archivo XML
              // Manejar el error de tipo de archivo no válido
              modelMap.put("error1", "Seleccione un archivo válido (XML)");
          }
      } else {
          // No se seleccionó ningún archivo
          // Manejar el error de archivo no seleccionado
          modelMap.put("error1", "No se ha seleccionado ningún archivo");
      }

      // Si ocurre algún error, mostrarlo en la vista
      return "xml_txt.jsp";
  }
 
  String txtStr = "";
  boolean errorDesencriptado = false;
  
  @PostMapping("/convertirArchivoXmlTxt")   
  public String convertirArchivoXmlTxt(ModelMap modelMap, 
		  @RequestParam("delimitador") String delimitador,
		  @RequestParam("llave") String llave) throws Exception{ 
	  
	  if(clientes.isEmpty()) {
		  modelMap.put("textoArchivo1", "No se ha cargado ningun archivo");
		  modelMap.put("textoResultado1", "No se ha convertido ningun archivo");
		  modelMap.put("error1", "Cargue un archivo xml antes");
		  return "xml_txt.jsp";
	  }
	  
	  if(delimitador.isEmpty()) {
		  modelMap.put("textoArchivo1", archivoCargadoXmlTxtStr);
		  modelMap.put("textoResultado1", "No se ha convertido ningun archivo");
		  modelMap.put("error1", "Escriba el delimitador antes de guardar el archivo convertido");
		  return "xml_txt.jsp";
	  }
	  
	  if(llave.isEmpty()) {
		  modelMap.put("textoArchivo1", archivoCargadoXmlTxtStr);
		  modelMap.put("textoResultado1", "No se ha convertido ningun archivo");
		  modelMap.put("error1", "Escriba la clave antes de guardar el archivo convertido");
		  return "xml_txt.jsp";
	  }
	  
	  	  //Desencriptando los numeros de tarjeta de cada cliente:
	  	  //Input length must be multiple of 16 when decrypting
	  
		  clientes.forEach(c->{
			  
			String decryptedText = "";
			  
			//Desencriptando numero de tarjeta de credito:
			  try {
				decryptedText = Util.decrypt(c.getNumeroTarjeta(), llave);
			} catch (Exception e) {
				System.out.println("Hubo un error desencriptando la tarjeta");
				errorDesencriptado = true;
				e.printStackTrace(); 
				 
			}
			  
			  
			//Seteando numero de tarjeta desencriptada:
			  c.setNumeroTarjeta(decryptedText);
		  }); 
		  
		  
		  txtStr = Util.txtStr(clientes);
		  
		  if(errorDesencriptado) {
			  modelMap.put("error2", "Hubo un error con la clave");
			  modelMap.put("textoArchivo1", archivoCargadoXmlTxtStr);
			  modelMap.put("textoResultado1", txtStr);
			  return "xml_txt.jsp";
		}
		  
	  modelMap.put("error0", "Archivo convertido y gurdado correctamente");
	  modelMap.put("textoArchivo1", archivoCargadoXmlTxtStr);
	  modelMap.put("textoResultado1", txtStr);
	  	
	  return "xml_txt.jsp";
  } 
  
  
  @PostMapping("/convertToJson")
  public ResponseEntity<List<ClienteJSON>> convertToJSON(@RequestBody String requestBody) {
  	//Array para los obejtos clientes
  	listaDeClientes = new ArrayList<>();
  	
  	//Array para separar los clientes
	  String[] clientesArrayTxtJson = new String[]{};
  	
  	//Array para separar el contenido del documento segun el delimitador TXT a JSON
	  String[] datosArrayTxtJson = new String[]{};
  	
	  
  	// Descomponer el objeto JSON recibido en un mapa de campos
      ObjectMapper objectMapper = new ObjectMapper();
      Map<String, Object> jsonMap;
      try {
          jsonMap = objectMapper.readValue(requestBody, new TypeReference<Map<String, Object>>() {});
      } catch (IOException e) {
          return ResponseEntity.badRequest().build();
      }

      //Obtener los valores de los campos del objeto JSON
      String contenido = (String) jsonMap.get("content");
      String llave = (String) jsonMap.get("key");
      String delimitador = (String) jsonMap.get("delimit");
      
      
      clientesArrayTxtJson = contenido.split("\n");
      
      
      for (int i = 0; i < clientesArrayTxtJson.length; i++) {
    	  String elemento = clientesArrayTxtJson[i];
          datosArrayTxtJson = elemento.split("\\"+delimitador);
          
    	  String encryptedText = "";
    	  
    	  try {
			encryptedText = Util.encrypt(datosArrayTxtJson[3], llave);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      	 
      	listaDeClientes.add(new ClienteJSON(datosArrayTxtJson[0], datosArrayTxtJson[1], datosArrayTxtJson[2], 
      			encryptedText, datosArrayTxtJson[4], datosArrayTxtJson[5], datosArrayTxtJson[6]));
      }
      	        

      // Combinar los valores en un objeto Person
      //ParametrosDTO parametros = new ParametrosDTO(contenido, llave, delimitador);

      return ResponseEntity.ok(listaDeClientes);
  }
  

  @PostMapping("/convertToTxt")
  public ResponseEntity<List<ClienteJSON>> convertToTxt(@RequestBody String requestBody) {
  	//Array para los obejtos clientes
  	listaDeClientes = new ArrayList<>();
  	
	  
  	// Descomponer el objeto JSON recibido en un mapa de campos
      ObjectMapper objectMapper = new ObjectMapper();
      Map<String, Object> jsonMap;
      try {
          jsonMap = objectMapper.readValue(requestBody, new TypeReference<Map<String, Object>>() {});
      } catch (IOException e) {
          return ResponseEntity.badRequest().build();
      }

      //Obtener los valores de los campos del objeto JSON
      String jsonContent = (String) jsonMap.get("content");
      String llave = (String) jsonMap.get("key");
      String delimitador = (String) jsonMap.get("delimit");
      
      Gson gson = new Gson();
      List<ClienteJSON> listaDeClientes = gson.fromJson(jsonContent, new TypeToken<List<ClienteJSON>>() {}.getType());
      	        	        
      for (ClienteJSON cliente : listaDeClientes) {
    	  String decryptedText = "";
		try {
			decryptedText = Util.decrypt(cliente.getNumeroTarjeta(), llave);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  System.out.print(cliente.getNumeroTarjeta());
          cliente.setNumeroTarjeta(decryptedText); 
          System.out.print(cliente.getNumeroTarjeta());
      }

      return ResponseEntity.ok(listaDeClientes);
  }
  
  
}


    
	  
   
    
  
  

