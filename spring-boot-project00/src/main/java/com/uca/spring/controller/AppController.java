package com.uca.spring.controller;

import java.awt.PageAttributes.MediaType;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.uca.spring.model.Cliente;
import com.uca.spring.model.ClienteXml;
import com.uca.spring.util.Util;




@Controller
@RequestMapping("/")
public class AppController {
	
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
	  datosArrayTxtXml = new String[0];
	  clientes.clear();
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
	  modelMap.put("textoArchivo1", "No se ha cargado ningun archivo");
	  modelMap.put("textoResultado1", "No se ha convertido ningun archivo");  
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
	  
	  if(llave.length() < 8) {
		  modelMap.put("textoArchivo1", archivoCargadoTxtXmlStr);
		  modelMap.put("textoResultado1", "No se ha convertido ningun archivo");
		  modelMap.put("error1", "Escriba una clave valida (8 bits)");
		  return "txt_xml.jsp";
	  }
	  
	  //TODO: ARREGLAR PARA QUE ENCRIPTE BIEN:
	  
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
		  
	  
	  String xmlStr = "";
	  try {
          // Crear el contexto JAXB
          JAXBContext context = JAXBContext.newInstance(ClienteXml.class);

          // Crear el marshaller
          Marshaller marshaller = context.createMarshaller();
          marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
          marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);
 
          ClienteXml clientesXml = new ClienteXml();
          clientesXml.setClientes(clientes);

          // Guardar el objeto como archivo XML
          File file = new File("objeto.xml"); //Si es el mismo nombre, agrega los nuevos clientes
          marshaller.marshal(clientesXml, file);

          System.out.println("Archivo XML guardado correctamente.");
          
          
       // Obtener el contenido del archivo XML como String
          ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
          marshaller.marshal(clientesXml, outputStream);
          xmlStr = new String(outputStream.toByteArray(), "UTF-8");
          
      } catch (JAXBException e) {
          e.printStackTrace();
      }
	  
	  		
	  modelMap.put("error0", "Archivo convertido y gurdado correctamente");
	  modelMap.put("textoArchivo1", archivoCargadoTxtXmlStr);
	  modelMap.put("textoResultado1", xmlStr);	  	
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
	  
	  	  //TODO: DESENCRIPTAR (CACHA UNA EXCEPTION)
	  	  //Input length must be multiple of 16 when decrypting
		  clientes.forEach(c->{
			  
			String decryptedText = "";
			  
			//Desencriptando numero de tarjeta de credito:
			  try {
				decryptedText = Util.decrypt(c.getNumeroTarjeta(), llave);
			} catch (Exception e) {
				System.out.println("Hubo un error desencriptando la tarjeta");
				e.printStackTrace();  
			}
			  
			  
			//Seteando numero de tarjeta desencriptada:
			  c.setNumeroTarjeta(decryptedText);
		  }); 
		  
		  
		  
		  
		  FileWriter fichero = new FileWriter("objeto.txt");

		  //Imprimiendo resultado txt
		  clientes.forEach(cliente ->{
			  String linea = String.format("%s,%s,%s,%s,%s,%s,%s",
					  	cliente.getDocumento(),
					  	cliente.getNombres(),
                		cliente.getApellidos(),
                        cliente.getNumeroTarjeta(),
                        cliente.getTipoTarjeta(),
                        cliente.getTelefono(),
                        cliente.getPoligono());
                        
			  
			  			// Agrega un salto de línea después de cada cliente
			  			txtStr += linea + "<br>";
              			
              			try {
							fichero.write(linea+"\n");
						} catch (IOException e) {
							System.out.print("Error con escribir en archivo");
							e.printStackTrace();
						}
		  });
		  
		  fichero.close();
		  
//		  ClassPathResource resource = new ClassPathResource("objeto.txt");
//
//		  org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
//		  headers.add("Content-Disposition", "attachment; filename=\"objeto.txt\"");
//		  headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
//		  headers.add("Pragma", "no-cache");
//		  headers.add("Expires", "0");
//
//		  ResponseEntity<byte[]> responseEntity;
//		  try {
//		      byte[] fileBytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
//		      responseEntity = ResponseEntity.ok()
//		              .headers(headers)
//		              .contentLength(resource.contentLength())
//		              .contentType(MediaType.TEXT_PLAIN_VALUE)
//		              .body(fileBytes);
//		  } catch (IOException e) {
//		      // Handle the exception in case of error while reading the file
//		      responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//		  }
		  
//		  File file = new File("fileName");
//		  FileInputStream in = new FileInputStream(file);
//		  byte[] content = new byte[(int) file.length()];
//		  in.read(content);
//		  ServletContext sc = request.getSession().getServletContext();
//		  String mimetype = sc.getMimeType(file.getName());
//		  response.reset();
//		  response.setContentType(mimetype);
//		  response.setContentLength(content.length);
//		  response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
//		  org.springframework.util.FileCopyUtils.copy(content, response.getOutputStream());


	  		
	  modelMap.put("error0", "Archivo convertido y gurdado correctamente");
	  modelMap.put("textoArchivo1", archivoCargadoXmlTxtStr);
	  modelMap.put("textoResultado1", txtStr);
	  txtStr = "";
	  clientes.clear();
	  	
	  return "xml_txt.jsp";
  } 
  
  
}


    
	  
   
    
  
  

