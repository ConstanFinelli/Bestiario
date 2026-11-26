<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="helpers.HttpRoutes"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Bestiario - Login</title>
<link rel="stylesheet" href="<%= HttpRoutes.MAIN_CSS(request.getContextPath()) %>">
<link rel="stylesheet" href="<%= HttpRoutes.LOGIN_CSS(request.getContextPath()) %>">
</head>
<body class='logBody'>
    <%@ include file="../components/error.jsp" %>
    <% 
        String urlAnterior = request.getParameter("urlAnterior"); 
        if (urlAnterior == null || urlAnterior.trim().isEmpty() || "null".equalsIgnoreCase(urlAnterior) || urlAnterior.contains("/auth/login") || urlAnterior.contains("/auth/logout")) {
            urlAnterior = HttpRoutes.HOME_JSP(request.getContextPath());
        }
    %>
		<div class="logDiv">
			<header class="logContainerHeader">
	          	<h1 class="logContainerHeaderText">Ingresar</h1>
	       </header>
	       <form class="logForm" action="<%=HttpRoutes.LOGIN(request.getContextPath())%>" method="POST">
	            <input type="hidden" name="urlAnterior" value="<%= urlAnterior %>">
	            <input type="submit" hidden="true" />
	            <section class="logInputs">
	                <div class="logInput">
	                    <input type="email" required placeholder='' id="correo" name="correo" value= 'test@a.com'/>
	                    <label for='correo'>✉️ Correo electrónico</label>
	                </div>
	                <div class="logInput">
	                    <input type="password" required placeholder='' id="contrasena" name='contrasena' value='123' />
	                    <label for="contrasena">🔐 Contraseña</label>
	                </div>
		            <div class='otherMsgs'>
	                 	<a
						    class="linkButton otherMsg"
						    href="<%= HttpRoutes.FORGOT_PASSWORD(request.getContextPath()) %>"
						>
						    ¿Has olvidado tu contraseña?
						</a>
			            <a class='otherMsg' href="<%= HttpRoutes.REGISTER(request.getContextPath()) %>">¿Aun no tienes una cuenta? Registrate	</a>
		           	</div>
	              	
	            </section>
	            <aside class="logSubmitContainer">
	            	<a href="<%= urlAnterior%>" class="backButton">
					<jsp:include page="/components/iconoVolver.jsp" />
					Volver
					</a>
	                <input type="submit" class="logSubmit" value="Iniciar sesión" />
	            </aside>
	            
				
				<% 
				    String successMsg = (String) session.getAttribute("successMsg");
				    if (successMsg != null) { 
				%>
				    <div class='successBox'>
				        <ul>
				            <li class="successMsg">✅ <%= successMsg %></li>
				        </ul>
				    </div>
				<% 
				        session.removeAttribute("successMsg"); // Lo borramos después de usarlo
				    } 
				%>
			</form>
		</div>
</body>
</html>