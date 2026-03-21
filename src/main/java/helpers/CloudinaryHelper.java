package helpers;

import java.io.File;
import java.io.IOException;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;

public class CloudinaryHelper {
	private Cloudinary instancia;
	
	public Cloudinary getInstancia() {
		if(instancia == null) {
		instancia = new Cloudinary(ObjectUtils.asMap(
					  "cloud_name", EnvHelper.get("CLOUD_NAME"),
					  "api_key", EnvHelper.get("API_KEY"),
					  "api_secret", EnvHelper.get("API_SECRET")));
		}
		return instancia;
	};
	
	public String upload(File archivo){		
		try {
		return getInstancia().uploader().upload(archivo, ObjectUtils.emptyMap()).get("public_id").toString();
		}catch(IOException e) {
			throw new RuntimeException("Error al subir imagen", e); 
		}
	}
	
	public String getImagenRegistro(String publicId) {
		return getInstancia().url().transformation(new Transformation<>().width(200).height(200).crop("fill").quality("auto").fetchFormat("auto")).generate(publicId);
	}
	
	public String getImagenListadoBestia(String publicId) {
		return getInstancia().url().transformation(new Transformation<>().width(200).height(200).crop("fill").quality("auto").fetchFormat("auto")).generate(publicId);
	}
	
	public void delete(String publicId) {
	    try {
	        getInstancia().uploader().destroy(publicId, ObjectUtils.emptyMap());
	    } catch (IOException e) {
	        throw new RuntimeException("Error eliminando imagen", e);
	    }
	}
	
	
}
