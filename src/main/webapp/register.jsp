<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Bestiario-Register</title>
<link rel="stylesheet" href="css/login.css">
</head>
<body class='logBody'>
        <form class="logForm" action="SvRegister" method="POST">
            <header class="logContainerHeader">
                <h1 class="logContainerHeaderText">Registrarse</h1>
            </header>
            <section class="logInputs">
                <div class="logInput">
                    <input type="email" required placeholder=' ' id="correo" name="correo" required/>
                    <label for='correo'>✉️ Correo electrónico</label>
                </div>
                
                <div class="logInput">
                    <input type="password" required placeholder=' ' id="contrasena" name='contrasena' required/>
                    <label for="contrasena">🔐 Contraseña</label>
                </div>
                
				<div id="camposLector" class="extraFields" style="display:flex;">
				    <div class="logInput">
				        <input type="date" placeholder=' ' id="fechaNacimiento" name="fechaNacimiento" required/>
				        <label for="fechaNacimiento">Fecha de nacimiento</label>
				    </div>
				</div>         	
              	
                <div class='otherMsgs'>
                    <a class='otherMsg' href="#">¿Problemas para registrarse? Contáctanos</a>
                    <a class='otherMsg' href="login.jsp">¿Ya tienes una cuenta? Inicia sesion</a>
                </div>
            </section>
            <aside class="logSubmitContainer">
                <input type="submit" class="logSubmit" value="Registrarse" />
            </aside>
            <div class='errorBox'>
                    <ul>
                        <li class="errorMsg">${logMsg}</li>
                    </ul>
            </div>
        </form>
       
</body>
</html>