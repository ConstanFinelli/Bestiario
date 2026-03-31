<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Bestiario - Reset Password</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/login.css">
</head>

<body class='logBody'>

<form class="logForm" action="<%=request.getContextPath()%>/reset-password" method="POST">

    <header class="logContainerHeader">
        <h1 class="logContainerHeaderText">Nueva contraseña</h1>
    </header>

    <section class="logInputs">

        <input type="hidden" name="token" value="${token}" />

        <div class="logInput">
            <input type="password" required placeholder=" " id="password" name="password" />
            <label for="password">🔐 Nueva contraseña</label>
        </div>
		
		<div class="logInput">
		    <input type="password" required placeholder=" " id="confirmPassword" name="confirmPassword" />
		    <label for="confirmPassword">🔐 Confirmar contraseña</label>
		</div>
		
        <div class='otherMsgs'>
            <span class='otherMsg'>Ingresa una nueva contraseña para tu cuenta</span>
        </div>

    </section>

    <aside class="logSubmitContainer">
        <input type="submit" class="logSubmit" value="Cambiar contraseña" />
    </aside>

    <div class='errorBox'>
        <ul>
            <li class="errorMsg">${logMsg}</li>
        </ul>
    </div>

</form>

</body>
</html>