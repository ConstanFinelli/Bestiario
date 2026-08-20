<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="helpers.HttpRoutes" %>

<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>Bestiario - Recuperar contraseña</title>

    <link rel="stylesheet"
          href="<%= HttpRoutes.LOGIN_CSS(request.getContextPath()) %>">
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
                    <svg
                        xmlns="http://www.w3.org/2000/svg"
                        viewBox="0 0 640 640"
                    >
                        <path d="M73.4 297.4C60.9 309.9 60.9 330.2 73.4 342.7L233.4 502.7C245.9 515.2 266.2 515.2 278.7 502.7C291.2 490.2 291.2 469.9 278.7 457.4L173.3 352L544 352C561.7 352 576 337.7 576 320C576 302.3 561.7 288 544 288L173.3 288L278.7 182.6C291.2 170.1 291.2 149.8 278.7 137.3C266.2 124.8 245.9 124.8 233.4 137.3L73.4 297.3z"/>
                    </svg>

                    Volver
                </a>

                <input
                    type="submit"
                    class="logSubmit"
                    value="Enviar correo"
                />

            </aside>

            <%
                String errorMsg = (String) session.getAttribute("logMsg");

                if (errorMsg != null) {
            %>

                <div class="errorBox">
                    <ul>
                        <li class="errorMsg">
                            ⚠️ <%= errorMsg %>
                        </li>
                    </ul>
                </div>

            <%
                    session.removeAttribute("logMsg");
                }
            %>

        </form>

    </div>

</body>
</html>