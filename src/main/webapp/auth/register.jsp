<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="helpers.HttpRoutes"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Bestiario-Register</title>
<link rel="stylesheet" href="../css/login.css">
</head>
<body class='logBody'>
        <form class="logForm" action="<%= HttpRoutes.REGISTER(request.getContextPath()) %>" method="POST">
        	<a href="<%= HttpRoutes.LOGIN_JSP(request.getContextPath())%>" class="backButton">
				<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 640"><path d="M73.4 297.4C60.9 309.9 60.9 330.2 73.4 342.7L233.4 502.7C245.9 515.2 266.2 515.2 278.7 502.7C291.2 490.2 291.2 469.9 278.7 457.4L173.3 352L544 352C561.7 352 576 337.7 576 320C576 302.3 561.7 288 544 288L173.3 288L278.7 182.6C291.2 170.1 291.2 149.8 278.7 137.3C266.2 124.8 245.9 124.8 233.4 137.3L73.4 297.3z"/></svg>
				Volver a inicio de sesión
			</a>
            <header class="logContainerHeader">
                <h1 class="logContainerHeaderText">Registrarse</h1>
            </header>
            <section class="logInputs">
                <div class="logInput">
                    <input type="email" required placeholder=' ' id="correo" name="correo" required/>
                    <label for='correo'>✉️ Correo electrónico</label>
                </div>
                
                <div class="logInput">
                    <input type="password" required placeholder=' ' id="password" name='password' required/>
                    <label for="password">🔐 Contraseña</label>
                </div>
                
				<div id="camposLector" class="extraFields" style="display:flex;">
				    <div class="logInput">
				        <input type="date" placeholder=' ' id="fechaNacimiento" name="fechaNacimiento" min="1000-01-01" max="9999-12-31" required/>
				        <label for="fechaNacimiento">Fecha de nacimiento</label>
				    </div>
				</div>     
				
				<div class="logInputCheckbox">
                    <input type="checkbox" id="recibirNotificaciones" name='recibirNotificaciones'/>
                    <label for="recibirNotificaciones">¿Desea recibir notificaciones?</label>
                </div>    
            </section>
            <aside class="logSubmitContainer">
                <input type="submit" class="logSubmit" value="Registrarse" />
            </aside>
            
            <% 
			    String logMsg = (String) request.getAttribute("logMsg");
			    if (logMsg != null && !logMsg.isEmpty()) { 
			%>
			    <div class='errorBox'>
			        <ul>
			            <li class="errorMsg"><%= logMsg %></li>
			        </ul>
			    </div>
			<% } %>
        </form>
       
</body>
</html>