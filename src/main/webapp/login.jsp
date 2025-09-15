<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Bestiario - Login</title>
<link rel="stylesheet" href="css/login.css">
</head>
<body class='logBody'>
        <form class="logForm" action="SvLogin" method="POST">
            <header class="logContainerHeader">
                <h1 class="logContainerHeaderText">Inicio de sesión</h1>
            </header>
            <section class="logInputs">
                <div class="logInput">
                    <input type="email" required placeholder=' ' id="correo" name="correo"/>
                    <label for='correo'>✉️ Correo electrónico</label>
                </div>
                <div class="logInput">
                    <input type="password" required placeholder=' ' id="contrasena" name='contrasena'/>
                    <label for="contrasena">🔐 Contraseña</label>
                </div>
              	
              	
                <div class='otherMsgs'>
                    <span class='otherMsg'>¿Has olvidado tu contraseña?</span>
                    <a class='otherMsg' href="SvRegister">¿Desea registrarse?</a>
                </div>
            </section>
            <aside class="logSubmitContainer">
                <input type="submit" class="logSubmit" value="Iniciar sesión" />
            </aside>
            <div class='errorBox'>
                    <ul>
                        <li class="errorMsg">${logMsg}</li>
                    </ul>
            </div>
        </form>
</body>
</html>