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
		return getInstancia().uploader().upload(fileBytes, ObjectUtils.asMap("resource_type", "auto")).get("public_id").toString();
		}catch(IOException e) {
			throw new RuntimeException("Error al subir archivo", e); 
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
	
	public static String getImagenMapa(String publicId) {
		publicId = isImagenDefault(publicId);
		return getInstancia().url().transformation(new Transformation<>().width(200).height(200).crop("fill").quality("auto").fetchFormat("auto")).generate(publicId);
	}
	
	public static String getImagenMapaButton(String publicId) {
		publicId = isImagenDefault(publicId);
		return getInstancia().url().transformation(new Transformation<>().width(200).height(200).crop("fill").quality("auto").fetchFormat("auto")).generate(publicId);
	}
	
	public static String getImagenListadoBestia(String publicId) {
		publicId = isImagenDefault(publicId);
		return getInstancia().url().transformation(new Transformation<>().width(200).height(200).crop("fill").quality("auto").fetchFormat("auto")).generate(publicId);
	}
	
	public static String getImagenEditarBestia(String publicId) {
		publicId = isImagenDefault(publicId);
		return getInstancia().url().transformation(new Transformation<>().width(200).height(200).crop("fill").quality("auto").fetchFormat("auto")).generate(publicId);
	}
	
	public static String getVideoEvidencia(String publicId) {
		return getInstancia().url().resourceType("video").transformation(new Transformation<>().width(200).height(200).crop("fill").quality("auto").fetchFormat("auto")).generate(publicId);
	}
	
	public static String getImagenEvidencia(String publicId) {
		return getInstancia().url().transformation(new Transformation<>().width(200).height(200).crop("fill").quality("auto").fetchFormat("auto")).generate(publicId);
	}
	
	public static String getArchivoEvidencia(String publicId) {
		return getInstancia().url().resourceType("raw").transformation(new Transformation<>().flags("attachment")).generate(publicId);
	}
	
	public static void deleteImage(String publicId) {
	    try {
	        getInstancia().uploader().destroy(publicId, ObjectUtils.asMap("resource_type", "image"));
	    } catch (IOException e) {
	        throw new RuntimeException("Error eliminando archivo", e);
	    }
	}
	
}
