<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="helpers.HttpRoutes"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Bestiario - Login</title>
<link rel="stylesheet" href="../css/login.css">
</head>
<body class='logBody'>
       <form class="logForm" action="<%=HttpRoutes.LOGIN(request.getContextPath())%>" method="POST">
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