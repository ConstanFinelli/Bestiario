package helpers;

import java.io.File;
import java.io.IOException;
import jakarta.servlet.http.Part;


public class ImagesHelper {
	
	public static String getImagesFolder() {
		File folder = new File(FilesHelper.getFilesFolder(), "imagenes");
		return (folder.getAbsolutePath()); 
	}
	
	public static String saveImage(Part filePart, String nombreImagen) throws IOException{
		return FilesHelper.saveFile(filePart, nombreImagen, getImagesFolder());
	}
	
}	
