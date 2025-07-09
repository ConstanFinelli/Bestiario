package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Servlet implementation class TipoEvidenciaServlet
 */
@WebServlet("/TipoEvidenciaServlet")
public class TipoEvidenciaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TipoEvidenciaServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ArrayList<String> tipos = new ArrayList<>();
		tipos.add("Video");
		tipos.add("Imagen");
		tipos.add("Relato");
		String text = "";
		for(String tipo : tipos) {
			text = text + tipo + "<br>";
		}
		String html = "<!DOCTYPE html>"
				+ "<head>"
				+ "<title>Tipo Evidencia</title>"
				+ "</head>"
				+ "<body>"
				+ text
				+ "</body>";
		PrintWriter out = response.getWriter();
		out.print(html);
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
