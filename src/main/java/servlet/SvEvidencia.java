package servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;

import entities.TipoEvidencia;
import entities.Evidencia;
import logic.LogicEvidencia;
import logic.LogicTipoEvidencia;
/**
 * Servlet implementation class SvEvidencia
 */
@WebServlet("/SvEvidencia")
public class SvEvidencia extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	public LogicEvidencia controladorEvidencia = new LogicEvidencia();
	public LogicTipoEvidencia controladorTipoEvidencia = new LogicTipoEvidencia();
    
	/**
     * @see HttpServlet#HttpServlet()
     */
    public SvEvidencia() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		RequestDispatcher rd = request.getRequestDispatcher("evidenciaForms.jsp");
		String msj = "";
		String idTipo = request.getParameter("IdTipo");
		String nroEvidencia = request.getParameter("nroEvidencia");
		String flag = request.getParameter("flag");
		if("getOne".equals(flag)) {
			TipoEvidencia t = new TipoEvidencia(Integer.parseInt(idTipo), null);
			t = controladorTipoEvidencia.getOne(t);
			Evidencia e = new Evidencia(Integer.parseInt(nroEvidencia), null, null, null, t);
			e = controladorEvidencia.getOne(e);
			if(e != null) {
				msj = msj + "Evidencia encontrada: <br>" + e;
			}else {
				msj = "Evidencia no encontrada";
			}
			request.setAttribute("msjOne", msj);
		} else if("findAll".equals(flag)) {
			LinkedList<Evidencia> evidencias = controladorEvidencia.findAll();
			for(Evidencia e : evidencias) {
				msj = msj + "<br><br>Tipo de Evidencia: " + e.getTipo().getId() + "<br>"+ e + "<br><br>";
			}
			request.setAttribute("msjAll", msj);
		} else if ("findAllType".equals(flag)) {
			TipoEvidencia tipo = controladorTipoEvidencia.getOne(new TipoEvidencia(Integer.parseInt(idTipo), null));
			if(tipo != null) {
				LinkedList<Evidencia> evidencias = controladorEvidencia.findAllType(tipo);
				for(Evidencia e : evidencias) {
					msj = msj + e + "<br><br>";
				}
			} else {
				msj = "Tipo no valido";
			}
			request.setAttribute("msjFindAllType", msj);
		}
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		RequestDispatcher rd = request.getRequestDispatcher("evidenciaForms.jsp");
		String msj = null;
		String flag = request.getParameter("flag");
		if("create".equals(flag)) {
			String link = request.getParameter("link");
			String estado = request.getParameter("estado");
			TipoEvidencia tipo = controladorTipoEvidencia.getOne(new TipoEvidencia(Integer.parseInt(request.getParameter("idTipoEvidencia")), null)); 
			LocalDateTime fechaO = LocalDateTime.parse(request.getParameter("fechaObtencion"));
			Evidencia evidencia = controladorEvidencia.save(new Evidencia(0, fechaO, estado, link, tipo));
			msj = "Evidencia guardada: " + evidencia; 
			request.setAttribute("msjCreate", msj);
		}else if("update".equals(flag)) {
			doPut(request, response);
		} else if("delete".equals(flag)) {
			doDelete(request, response);
		}
		rd.forward(request, response);
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String msj = null;
		int idTipo = Integer.parseInt(request.getParameter("idTipoEvidencia"));
		int nroEvidencia = Integer.parseInt(request.getParameter("nroEvidencia"));
		Evidencia evidencia = controladorEvidencia.getOne(new Evidencia(nroEvidencia, null, null, null, new TipoEvidencia(idTipo, null)));
		if(evidencia != null) {
			msj = "Evidencia eliminada: " + evidencia;
		} else {
			msj = "Evidencia no encontrada";
		}
		request.setAttribute("msjDelete", msj);
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String msj = null;
		TipoEvidencia tipo = controladorTipoEvidencia.getOne(new TipoEvidencia(Integer.parseInt(request.getParameter("idTipo")), null));
		if(tipo == null) {
		msj = "Tipo de Evidencia invalido<br><br>";	
		}else {
		Evidencia evidencia = controladorEvidencia.update(new Evidencia(Integer.parseInt(request.getParameter("nroEvidencia")), LocalDateTime.parse(request.getParameter("fechaObtencion")), request.getParameter("estado"), request.getParameter("link"), tipo));
		if(evidencia != null) {
			msj = "Evidencia guardada: <br> " + evidencia;
		} else {
			msj = "Evidencia no encontrada: ";
		}
			}
		request.setAttribute("msjUpdate", msj);
	}
	
}
