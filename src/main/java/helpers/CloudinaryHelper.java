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

				String contentType = archivo.getContentType();

			fileBytes = ImageHelper.comprimir(fileBytes, contentType);

			
			Transformation<?> uploadTransformation = new Transformation<>()
				.width(1920)
				.height(1080)
				.crop("limit")
				.quality("auto");

			return getInstancia().uploader().upload(fileBytes, ObjectUtils.asMap(
				"resource_type", "auto",
				"transformation", uploadTransformation
			)).get("public_id").toString();
		}catch(IOException e) {
			throw new RuntimeException("Error al subir archivo", e); 
		}
	}
	
	private static String getDefaultImageParam() {
		return EnvHelper.get("DEFAULT_PICTURE_ID") + ".png";
	}

	private static String resolvePublicId(String publicId) {
		return (publicId == null || publicId.trim().isEmpty()) ? EnvHelper.get("DEFAULT_PICTURE_ID") : publicId;
	}

	public static String getImagenRegistro(String publicId) {
		return getInstancia().url().transformation(new Transformation<>().width(1920).height(1080).crop("limit").quality("auto").fetchFormat("auto").defaultImage(getDefaultImageParam())).generate(resolvePublicId(publicId));
	}
	
	private static String getImagenCuadrada(String publicId, int size) {
		return getInstancia().url().transformation(
				new Transformation<>().width(size).height(size).crop("fill").quality("auto").fetchFormat("auto").defaultImage(getDefaultImageParam()))
				.generate(resolvePublicId(publicId));
	}

	public static String getImagenMapa(String publicId) {
		return getImagenCuadrada(publicId, 200);
	}

	public static String getImagenListadoBestia(String publicId) {
		return getImagenCuadrada(publicId, 400);
	}
	
	public static String getVideoEvidencia(String publicId) {
		return getInstancia().url().resourceType("video").transformation(new Transformation<>().width(1920).height(1080).crop("limit").quality("auto").fetchFormat("auto")).generate(publicId);
	}
	
	public static String getImagenEvidencia(String publicId) {
		return getInstancia().url().transformation(new Transformation<>().width(1920).height(1080).crop("limit").quality("auto").fetchFormat("auto").defaultImage(getDefaultImageParam())).generate(resolvePublicId(publicId));
	}
	
	public static String getArchivoEvidencia(String publicId) {
		return getInstancia().url().resourceType("raw").transformation(new Transformation<>().flags("attachment")).generate(publicId);
	}

	public static String getUrlEvidencia(String resourceType, String fileId) {
		if (resourceType == null) {
			return getArchivoEvidencia(fileId);
		}
		return switch (resourceType) {
			case "video" -> getVideoEvidencia(fileId);
			case "image" -> getImagenEvidencia(fileId);
			default -> getArchivoEvidencia(fileId);
		};
	}
	
	public static void deleteResource(String publicId, String resourceType) {
	    if (publicId == null || publicId.isBlank()) {
	        return;
	    }
	    String resolvedType = switch (resourceType == null ? "" : resourceType) {
	        case "image", "video" -> resourceType;
	        default -> "raw";
	    };
	    try {
	        getInstancia().uploader().destroy(publicId, ObjectUtils.asMap("resource_type", resolvedType));
	    } catch (IOException e) {
	        throw new RuntimeException("Error eliminando archivo", e);
	    }
	}
	
}
