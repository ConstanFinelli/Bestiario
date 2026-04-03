<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="helpers.HttpRoutes"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Bestiario - Login</title>
<link rel="stylesheet" href="<%= HttpRoutes.LOGIN_CSS(request.getContextPath()) %>">
</head>
<body class='logBody'>
       <form class="logForm" action="<%=HttpRoutes.LOGIN(request.getContextPath())%>" method="POST">
       		<a href="<%= HttpRoutes.HOME_JSP(request.getContextPath())%>" class="backButton">
				<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 640"><path d="M73.4 297.4C60.9 309.9 60.9 330.2 73.4 342.7L233.4 502.7C245.9 515.2 266.2 515.2 278.7 502.7C291.2 490.2 291.2 469.9 278.7 457.4L173.3 352L544 352C561.7 352 576 337.7 576 320C576 302.3 561.7 288 544 288L173.3 288L278.7 182.6C291.2 170.1 291.2 149.8 278.7 137.3C266.2 124.8 245.9 124.8 233.4 137.3L73.4 297.3z"/></svg>
				Volver a inicio
			</a>
            <input type="submit" hidden="true" />
            
            <header class="logContainerHeader">
                <h1 class="logContainerHeaderText">Ingresar</h1>
            </header>
            <section class="logInputs">
                <div class="logInput">
                    <input type="email" required placeholder=' ' id="correo" name="correo" value='test@a.com' />
                    <label for='correo'>✉️ Correo electrónico</label>
                </div>
                <div class="logInput">
                    <input type="password" required placeholder=' ' id="contrasena" name='contrasena' value='123'/>
                    <label for="contrasena">🔐 Contraseña</label>
                </div>
	            <div class='otherMsgs'>
	                 	<button 
					    type="submit"
					    formaction="<%=HttpRoutes.FORGOT_PASSWORD(request.getContextPath())%>"
					    class="linkButton otherMsg"
					    onclick="if(document.getElementById('correo').value === '') { alert('Ingresa tu correo primero'); return false; }">
					    ¿Has olvidado tu contraseña?
					</button>
		            <a class='otherMsg' href="<%= HttpRoutes.REGISTER(request.getContextPath()) %>">¿Aun no tienes una cuenta? Registrate	</a>
	           	</div>
              	
            </section>
            <aside class="logSubmitContainer">
                <input type="submit" class="logSubmit" value="Iniciar sesión" />
            </aside>
            
			
			<% 
			    String errorMsg = (String) session.getAttribute("logMsg");
			    if (errorMsg != null) { 
			%>
			    <div class='errorBox'>
			        <ul>
			            <li class="errorMsg">⚠️<%= errorMsg %></li>
			        </ul>
			    </div>
			<% 
			        session.removeAttribute("logMsg"); // Lo borramos después de usarlo
			    } 
			%>
			
			
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
</body>
</html>