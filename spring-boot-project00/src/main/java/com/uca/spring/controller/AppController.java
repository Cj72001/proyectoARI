package com.uca.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.uca.spring.model.Cliente;
import com.uca.spring.model.ClienteXml;
import com.uca.spring.util.Util;

import javassist.expr.NewArray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;



@Controller
@RequestMapping("/")
public class AppController {
	
		//Variables globales:
	  //Contenido del txt:
	  String fileContent = "";
	  
	  //Cliente con sus datos 
	  Cliente cliente = new Cliente("", "", "", "", "", "", "");
	  
	//Array para separar el contenido del documento segun el delimitador TXT a XML
	  String[] datosArrayTxtXml = new String[]{};

	//Action que se invoca al iniciar la app en la ruta del login (/)
  @GetMapping("/")
  public String getForm(ModelMap modelMap) {
	  
	  //Inicializando resultados
	  fileContent = "";
	  cliente = new Cliente("", "", "", "", "", "", "");
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
	  cliente = new Cliente("", "", "", "", "", "", "");
	  datosArrayTxtXml = new String[0];
	  modelMap.put("textoArchivo1", "No se ha cargado ningun archivo");
	  modelMap.put("textoResultado1", "No se ha convertido ningun archivo");
	  
    return "txt_xml.jsp";
  } 
  
  @GetMapping("/xmlTxt")
  public String xmlTxt() {
	  
    return "xml_txt.jsp";
  } 
  
  @GetMapping("/txtJson")
  public String txtJson() {
	  
    return "json_txt.jsp";
  } 
  
  @GetMapping("/jsonTxt")
  public String jsonTxt() {
	  
    return "txt_json.jsp";
  } 
  
  //Actions para post:
  
  @PostMapping("/abrirArchivoTxtXml")   
  public String abrirArchivoTxtXml(ModelMap modelMap,
		  @RequestParam("delimitador") String delimitador,  
		  @RequestParam("archivo") MultipartFile file){ 
	  
	  
		  if (!file.isEmpty()) {
			  
			// Verificar el tamaño del archivo (en bytes)
//		        long maxSize = 10 * 1024 * 1024; // 10 MB 
//		        if (file.getSize() <= maxSize) {
//		        	// El archivo seleccionado es demasiado grande
//		            // Manejar el error de tamaño de archivo excedido
//		        	modelMap.put("error1","Ha seleccionado un archivo demasiado grande");
//		  		  	return "txt_xml.jsp";
//		        }
			  
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
		  	//Se guardan los datos del cliente en 
		  	datosArrayTxtXml = fileContent.split(",");
		  	if(datosArrayTxtXml.length != 7) {
		  		//No se introducieron toda la informacion del cliente
		  		modelMap.put("error1","Introduzca un archivo txt con el numero de datos establecidos");
	  		  	return "txt_xml.jsp";
		  	}
		  	else {
		  		//Se introdujo la informacion correctamente
		  		
		  		cliente.setDocumento(datosArrayTxtXml[0]);
		  		cliente.setNombres(datosArrayTxtXml[1]);
		  		cliente.setApellidos(datosArrayTxtXml[2]);
		  		cliente.setNumeroTarjeta(datosArrayTxtXml[3]);
		  		cliente.setTipoTarjeta(datosArrayTxtXml[4]);
		  		cliente.setTelefono(datosArrayTxtXml[5]);
		  		cliente.setPoligono(datosArrayTxtXml[6]);
		  		
		  		modelMap.put("error0", "Archivo Cargado Correctamente");
		  		modelMap.put("textoResultado1", "No se ha convertido ningun archivo");
		  		modelMap.put("textoArchivo1", fileContent);
			  	return "txt_xml.jsp";
		  	}
		  	
		  
  } 
  
  
  @PostMapping("/convertirArchivoTxtXml")   
  public String convertirArchivoTxtXml(ModelMap modelMap, 
		  @RequestParam("llave") String llave) throws Exception{ 
	  
	  if(datosArrayTxtXml.length==0) {
		  modelMap.put("textoArchivo1", "No se ha cargado ningun archivo");
		  modelMap.put("textoResultado1", "No se ha convertido ningun archivo");
		  modelMap.put("error1", "Cargue un archivo txt antes");
		  return "txt_xml.jsp";
	  }
	  
	  if(llave.isEmpty()) {
		  modelMap.put("textoArchivo1", fileContent);
		  modelMap.put("textoResultado1", "No se ha convertido ningun archivo");
		  modelMap.put("error1", "Escriba la clave antes de guardar el archivo convertido");
		  return "txt_xml.jsp";
	  }
	  
	  
	  
	  String encryptedText = "";
	  String xmlString = "";
	  
	  try {
		  //Encriptando numero de tarjeta de credito:
		  encryptedText = Util.encrypt(datosArrayTxtXml[3], llave);
		  
		  //Desencriptar:
		  //String decryptedText = Util.decrypt(datosArray[3], llave);
		  
		  //Seteando numero de tarjeta encriptada:
		  cliente.setNumeroTarjeta(encryptedText);
	  }catch (Exception e) {
		// TODO: handle exception
	}
	  
	  ClienteXml clienteXml = new ClienteXml();
	  
	  clienteXml.setDocumento(datosArrayTxtXml[0]);
	  clienteXml.setNombres(datosArrayTxtXml[1]);
	  clienteXml.setApellidos(datosArrayTxtXml[2]);
	  clienteXml.setNumeroTarjeta(encryptedText);
	  clienteXml.setTipoTarjeta(datosArrayTxtXml[4]);
	  clienteXml.setTelefono(datosArrayTxtXml[5]);
	  clienteXml.setPoligono(datosArrayTxtXml[6]);
	  
	  try {
          // Crear el contexto JAXB
          JAXBContext context = JAXBContext.newInstance(ClienteXml.class);

          // Crear el marshaller
          Marshaller marshaller = context.createMarshaller();
          marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
          marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);
 

          // Guardar el objeto como archivo XML
          File file = new File("objeto.xml");
          marshaller.marshal(clienteXml, file);

          System.out.println("Archivo XML guardado correctamente.");
          
          
       // Obtener el contenido del archivo XML como String
          ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
          marshaller.marshal(clienteXml, outputStream);
          xmlString = new String(outputStream.toByteArray(), "UTF-8");
          
      } catch (JAXBException e) {
          e.printStackTrace();
      }
	  
	  		
	  modelMap.put("error0", "Archivo convertido y gurdado correctamente");
	  modelMap.put("textoArchivo1", fileContent);
	  modelMap.put("textoResultado1", xmlString);	  	
	  return "txt_xml.jsp";
  } 
  
  
}


    
	  
   
    
  
  

