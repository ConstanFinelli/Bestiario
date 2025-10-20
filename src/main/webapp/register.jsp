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
                    <input type="email" required placeholder=' ' id="correo" name="correo"/>
                    <label for='correo'>✉️ Correo electrónico</label>
                </div>
                <div class="logInput">
                    <input type="password" required placeholder=' ' id="contrasena" name='contrasena'/>
                    <label for="contrasena">🔐 Contraseña</label>
                </div>
                <div class="logInputCheckbox">
              		<input type="checkbox" id="esInvestigador" name='esInvestigador'>
              		<label for="esInvestigador">¿Ud. es un investigador?</label>
              	</div>
              	
				<div id="camposInvestigador" class="extraFields" style="display:none;">
	    			<div class="logInput">
				        <input type="text" placeholder=' ' id="nombre" name="nombre"/>
				        <label for="nombre">Nombre</label>
				    </div>
				    <div class="logInput">
				        <input type="text" placeholder=' ' id="apellido" name="apellido"/>
				        <label for="apellido">Apellido</label>
				    </div>
				    <div class="logInput">
				        <input type="text" placeholder=' ' id="dni" name="dni"/>
				        <label for="dni">Dni</label>
				    </div>
				</div>


				<div id="camposLector" class="extraFields" style="display:flex;">
				    <div class="logInput">
				        <input type="date" required placeholder=' ' id="fechaNacimiento" name="fechaNacimiento"/>
				        <label for="fechaNacimiento">Fecha de nacimiento</label>
				    </div>
				</div>         	
              	
                <div class='otherMsgs'>
                    <a class='otherMsg' href="enConstruccion">¿Problemas para registrarse? Contáctanos</a>
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
        
        <script>
		    const checkbox = document.getElementById("esInvestigador");
		    const camposInvestigador = document.getElementById("camposInvestigador");
		    const camposLector = document.getElementById("camposLector");
		
		    checkbox.addEventListener("change", function() {
		        if (this.checked) {
		            camposInvestigador.style.display = "flex"; 
		            camposLector.style.display = "none";       
		        } else {
		            camposInvestigador.style.display = "none";
		            camposLector.style.display = "flex";        
		        }
		    });
		</script>
</body>
</html>