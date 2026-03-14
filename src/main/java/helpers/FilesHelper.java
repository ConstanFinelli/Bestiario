package helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import jakarta.servlet.http.Part;

public class FilesHelper {

	public static String getFilesFolder() {
		File projectDir = new File("").getAbsoluteFile();
		return (projectDir.toString() + File.separator + "docs"); 
	}
	
	public static void transcriptFile(FileInputStream in, OutputStream out) {
		try (FileInputStream fis = in;
	             OutputStream os = out) {
	
	            byte[] buffer = new byte[4096];
	            int bytesRead;
	            while((bytesRead = fis.read(buffer)) != -1) {
	                os.write(buffer, 0, bytesRead);
	            }
	        } catch(IOException e) {
	        	System.out.println("Error de entrada o salida en transcriptFile");
	        }
	}
	
	public static String saveFile(Part filePart, String nombreImagen, String folder) throws IOException{
		String submittedFileName = filePart.getSubmittedFileName();
		String extension = submittedFileName.substring(submittedFileName.lastIndexOf("."));
    	String fileName = nombreImagen + extension;
    	
    	File uploadDir = new File(folder);
    	if(!uploadDir.exists()) {
    		uploadDir.mkdirs();
    	}
    	
    	File file = new File(uploadDir, fileName);
    	filePart.write(file.getAbsolutePath());
    	return fileName;
	}
}
