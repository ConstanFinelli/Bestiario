<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="helpers.HttpRoutes" %>

<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>Bestiario - Recuperar contraseña</title>

    <link rel="stylesheet" href="<%= HttpRoutes.MAIN_CSS(request.getContextPath()) %>">
    <link rel="stylesheet"
          href="<%= HttpRoutes.LOGIN_CSS(request.getContextPath()) %>">
    <%@ include file="../components/error.jsp" %>
</head>

<body class="logBody">

    <div class="logDiv">

        <header class="logContainerHeader">
            <h1 class="logContainerHeaderText">
                Recuperar contraseña
            </h1>
        </header>

        <form class="logForm"
              action="<%= HttpRoutes.FORGOT_PASSWORD(request.getContextPath()) %>"
              method="POST">

            <section class="logInputs">

                <div class="otherMsgs">
                    <span class="otherMsg">
                        Ingresá el correo electrónico asociado a tu cuenta.
                    </span>

                    <span class="otherMsg">
                        Te enviaremos un enlace para cambiar tu contraseña.
                    </span>
                </div>

                <div class="logInput">

                    <input
                        type="email"
                        required
                        placeholder=" "
                        id="correo"
                        name="correo"
                        autocomplete="email"
                    />

                    <label for="correo">
                        ✉️ Correo electrónico
                    </label>

                </div>

            </section>

            <aside class="logSubmitContainer">

                <a
                    href="<%= HttpRoutes.LOGIN(request.getContextPath()) %>"
                    class="backButton"
                >
                    <jsp:include page="/components/iconoVolver.jsp" />

                    Volver
                </a>

                <input
                    type="submit"
                    class="logSubmit"
                    value="Enviar correo"
                />

            </aside>

        </form>

    </div>

</body>
</html>