package servlet.noticia;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicNoticia;

import java.io.IOException;
import java.util.LinkedList;

import entities.Noticia;

/**
 * Servlet implementation class SvListarNoticias
 */
@WebServlet("/noticias/listar")
public class SvListarNoticias extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private LogicNoticia controlador = new LogicNoticia();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SvListarNoticias() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String flag = request.getParameter("flag");
		
		LinkedList<Noticia> noticias = null;
		
		RequestDispatcher rd = null;
		
		if("ultimasNoticias".equals(flag)) { 
			noticias = controlador.getUltimasNoticias();
			rd = request.getRequestDispatcher("../home.jsp");
		}else {
			noticias = controlador.findAll();
			rd = request.getRequestDispatcher("../noticias.jsp");
		}
		request.setAttribute("noticias", noticias);
		rd.forward(request, response);
	}

}
