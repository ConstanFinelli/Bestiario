package helpers;

import java.io.IOException;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;

import jakarta.servlet.http.Part;

public class CloudinaryHelper {
	private static Cloudinary instancia;
	
	public static Cloudinary getInstancia() {
		if(instancia == null) {
		instancia = new Cloudinary(ObjectUtils.asMap(
					  "cloud_name", EnvHelper.get("CLOUD_NAME"),
					  "api_key", EnvHelper.get("API_KEY"),
					  "api_secret", EnvHelper.get("API_SECRET")));
		}
		return instancia;
	};
	
	public static String upload(Part archivo){		
		try {
		byte[] fileBytes = archivo.getInputStream().readAllBytes();
		return getInstancia().uploader().upload(fileBytes, ObjectUtils.emptyMap()).get("public_id").toString();
		}catch(IOException e) {
			throw new RuntimeException("Error al subir imagen", e); 
		}
	}
	
	public static String isImagenDefault(String publicId) {
		if(publicId == null) {
			publicId = EnvHelper.get("DEFAULT_ID");
		}
		return publicId;
	}
	
	public static String getImagenRegistro(String publicId) {
		publicId = isImagenDefault(publicId);
		return getInstancia().url().transformation(new Transformation<>().width(200).height(200).crop("fill").quality("auto").fetchFormat("auto")).generate(publicId);
	}
	
	public static String getImagenListadoBestia(String publicId) {
		publicId = isImagenDefault(publicId);
		return getInstancia().url().transformation(new Transformation<>().width(200).height(200).crop("fill").quality("auto").fetchFormat("auto")).generate(publicId);
	}
	
	public static void delete(String publicId) {
	    try {
	        getInstancia().uploader().destroy(publicId, ObjectUtils.emptyMap());
	    } catch (IOException e) {
	        throw new RuntimeException("Error eliminando imagen", e);
	    }
	}
	
}
