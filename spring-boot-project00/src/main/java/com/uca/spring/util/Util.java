package com.uca.spring.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;


public class Util {
	
	//TODO: IMPLEMENTAR FUNCION (AUN NO SIRVE EL FILE CHOOSER PARA DESCARGAR)
	//Para seleccionar la carpeta:
	public static String seleccionarCarpeta() {
		JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
	    fileChooser.setDialogTitle("Seleccionar carpeta de destino");
	    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    
	    String carpetaDestino = "";

	    int returnValue = fileChooser.showOpenDialog(null);
	    if (returnValue == JFileChooser.APPROVE_OPTION) {
	        File selectedFile = fileChooser.getSelectedFile();
	        carpetaDestino = selectedFile.getAbsolutePath();
	    }

		    return carpetaDestino;
		}
	  
	  
	  
	//______________________________________________________________________________
	/*documentación de los paquetes y API utilizados en la función cifrarNumero para cifrar un número con una clave en Java:

		Paquetes utilizados:
		-javax.crypto: Este paquete proporciona las clases y interfaces necesarias para realizar operaciones de criptografía, como cifrado y descifrado.
		-java.nio.charset: Este paquete proporciona clases para trabajar con conjuntos de caracteres y codificaciones de caracteres.
		-java.util: Este paquete proporciona clases de utilidad, como Base64, que se utiliza para codificar y decodificar datos en formato Base64.

		API utilizadas:
		-Cipher: La clase Cipher proporciona la funcionalidad principal para realizar operaciones de cifrado y descifrado. Se utiliza para crear una instancia de cifrador AES y para cifrar el número en este caso.
		-SecretKeySpec: La clase SecretKeySpec se utiliza para representar una clave secreta en forma de un objeto SecretKey que puede ser utilizado por los algoritmos de cifrado.
		-SecretKeyFactory: La clase SecretKeyFactory se utiliza para generar objetos SecretKey a partir de especificaciones de clave, como en este caso se utiliza para generar la clave secreta utilizando PBKDF2.
		-PBEKeySpec: La clase PBEKeySpec representa una especificación de clave basada en una contraseña (en este caso, la clave proporcionada). Se utiliza junto con SecretKeyFactory para generar la clave secreta utilizando PBKDF2.
		-Base64: La clase Base64 proporciona métodos estáticos para codificar y decodificar datos en formato Base64. Se utiliza para codificar el número cifrado en Base64 antes de retornarlo como una cadena de texto. */
	
		/*Al utilizar el algoritmo de derivación de claves PBKDF2 en la función cifrarNumero, 
		se puede proporcionar una clave de cualquier longitud, incluso si no es de 8 bits. 
		PBKDF2 se encarga de generar una clave de longitud adecuada para el algoritmo AES,
		que puede ser de 128, 192 o 256 bits */
	  
	  
  //Funcion para encriptar:
  public static String encrypt(String numeroTexto, String clave) throws Exception {
	  

          // Generar una clave de 128 bits (16 bytes) utilizando PBKDF2
          SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
          KeySpec spec = new PBEKeySpec(clave.toCharArray(), clave.getBytes(), 65536, 128);
          SecretKey secretKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

          // Obtener una instancia del cifrador AES
          Cipher cipher = Cipher.getInstance("AES");
          cipher.init(Cipher.ENCRYPT_MODE, secretKey);

          // Cifrar el número como una cadena de texto
          byte[] numeroCifrado = cipher.doFinal(numeroTexto.getBytes(StandardCharsets.UTF_8));

          // Codificar el número cifrado en Base64 para poder imprimirlo o transmitirlo
          return Base64.getEncoder().encodeToString(numeroCifrado);
     

  }

  
  //Funcion para desencriptar
  public static String decrypt(String textoCifrado, String clave) throws Exception {
	  
          // Decodificar el texto cifrado de Base64
          byte[] textoCifradoBytes = Base64.getDecoder().decode(textoCifrado);

          // Generar una clave de 128 bits (16 bytes) utilizando PBKDF2
          SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
          KeySpec spec = new PBEKeySpec(clave.toCharArray(), clave.getBytes(), 65536, 128);
          SecretKey secretKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

          // Obtener una instancia del cifrador AES
          Cipher cipher = Cipher.getInstance("AES");
          cipher.init(Cipher.DECRYPT_MODE, secretKey);

          // Descifrar el texto cifrado y obtener los bytes del número original
          byte[] numeroBytes = cipher.doFinal(textoCifradoBytes);

          // Convertir los bytes del número a una cadena de texto
          return new String(numeroBytes, StandardCharsets.UTF_8);
  } 


}
