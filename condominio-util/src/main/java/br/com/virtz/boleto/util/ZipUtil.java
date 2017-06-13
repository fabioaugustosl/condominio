package br.com.virtz.boleto.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

	 public static ZipOutputStream zipFile(List<File> arquivos, File arquivoRetorno) throws IOException {
		 	if(arquivos == null || arquivos.isEmpty()){
		 		return null;
		 	}
	        	
	            // Wrap a FileOutputStream around a ZipOutputStream
	            // to store the zip stream to a file. Note that this is
	            // not absolutely necessary
	            FileOutputStream fileOutputStream = new FileOutputStream(arquivoRetorno);
	            ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);

	            // a ZipEntry represents a file entry in the zip archive
	            // We name the ZipEntry after the original file's name
	           Integer cont = 1;
	            for(File arq : arquivos){
		            ZipEntry zipEntry = new ZipEntry(cont+++"_"+arq.getName());
		            zipOutputStream.putNextEntry(zipEntry);
	
		            FileInputStream fileInputStream = new FileInputStream(arq);
		            byte[] buf = new byte[1024];
		            int bytesRead;
	
		            // Read the input file by chucks of 1024 bytes
		            // and write the read bytes to the zip stream
		            while ((bytesRead = fileInputStream.read(buf)) > 0) {
		                zipOutputStream.write(buf, 0, bytesRead);
		            }
	
		            // close ZipEntry to store the stream to the file
		            zipOutputStream.closeEntry();
	            }
	            zipOutputStream.close();
	            fileOutputStream.close();

	      
			return null;

	    }
	
}
