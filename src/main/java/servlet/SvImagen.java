package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import helpers.FilesHelper;
import helpers.ImagesHelper;

/**
 * Servlet implementation class SvImagenes
 */
@WebServlet("/SvImagen")
public class SvImagen extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SvImagen() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String fileName = request.getParameter("file");
        if(fileName == null || fileName.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No file specified");
            return;
        }
        fileName = new File(fileName).getName(); //Evita el acceso indebido ingresando una ruta en file

        File imageFile = new File(ImagesHelper.getImagesFolder(), fileName);
        if(!imageFile.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Image not found");
            return;
        }

     
        String mimeType = getServletContext().getMimeType(imageFile.getName());
        if(mimeType == null) {
            mimeType = "application/octet-stream";
        }

        response.setContentType(mimeType);
        response.setContentLengthLong(imageFile.length());

        try(FileInputStream in = new FileInputStream(imageFile)){
        	FilesHelper.transcriptFile(in, response.getOutputStream());	
        }

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */

}
