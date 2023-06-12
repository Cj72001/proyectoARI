package com.uca.spring.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


public class Util {
	
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
	//Para encriptar un String usando una API y una clave en Java, 
	//se utilizaron bibliotecas criptográficas como javax.crypto

	//javax.crypto es una API proporcionada por Java para realizar operaciones criptográficas,
	//como encriptación y desencriptación. Esta API forma parte del paquete javax.crypto y 
	//ofrece clases e interfaces para trabajar con algoritmos criptográficos, claves, cifradores y 
	//funciones hash.
	  
	  
  //Funcion para encriptar:
  public static String encrypt(String plaintext, String key) throws Exception {
      // Convertir la clave a una clave secreta
      SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

      // Obtener una instancia del cifrador AES
      Cipher cipher = Cipher.getInstance("AES");

      // Inicializar el cifrador en modo de encriptación con la clave secreta
      cipher.init(Cipher.ENCRYPT_MODE, secretKey);

      // Encriptar el texto plano
      byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

      // Codificar los bytes encriptados a una cadena Base64
      String encryptedText = Base64.getEncoder().encodeToString(encryptedBytes);

      return encryptedText;
  }

  
 
  //Funcion para desencriptar
  public static String decrypt(String encryptedText, String key) throws Exception {
      // Convertir la clave a una clave secreta
      SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

      // Obtener una instancia del cifrador AES
      Cipher cipher = Cipher.getInstance("AES");

      // Inicializar el cifrador en modo de desencriptación con la clave secreta
      cipher.init(Cipher.DECRYPT_MODE, secretKey);

      // Decodificar la cadena Base64 a bytes encriptados
      byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);

      // Desencriptar los bytes encriptados
      byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

      // Convertir los bytes desencriptados a texto plano
      String plaintext = new String(decryptedBytes, StandardCharsets.UTF_8);

      return plaintext;
  }


}
