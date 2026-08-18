package helpers;

public class EmailTemplates {

    public static String nuevaNoticia(String tituloNoticia) {

        return """
            <!DOCTYPE html>
            <html lang="es">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Bestiario - Nueva noticia</title>
            </head>

            <body style="
                margin: 0;
                padding: 0;
                background-color: #8E6E53;
                font-family: Arial, Helvetica, sans-serif;
            ">

                <table
                    width="100%%"
                    cellpadding="0"
                    cellspacing="0"
                    border="0"
                    style="padding: 35px 15px;"
                >
                    <tr>
                        <td align="center">

                            <!-- Contenedor principal -->
                            <table
                                width="600"
                                cellpadding="0"
                                cellspacing="0"
                                border="0"
                                style="
                                    width: 100%%;
                                    max-width: 600px;
                                    background-color: #F4F1EA;
                                    border-radius: 16px;
                                    overflow: hidden;
                                "
                            >

                                <!-- Encabezado -->
                                <tr>
                                    <td align="center" style="
                                        padding: 30px 25px;
                                        border-bottom: 1px solid #b0aaa0;
                                    ">

                                        <h1 style="
                                            margin: 0;
                                            color: #2C3E50;
                                            font-size: 30px;
                                            font-weight: 500;
                                            letter-spacing: 1px;
                                        ">
                                            BESTIARIO
                                        </h1>

                                    </td>
                                </tr>

                                <!-- Área de noticias -->
                                <tr>
                                    <td style="
                                        padding: 30px;
                                        background-color: rgb(203, 187, 147);
                                    ">

                                        <!-- Tarjeta de noticia -->
                                        <table
                                            width="100%%"
                                            cellpadding="0"
                                            cellspacing="0"
                                            border="0"
                                            style="
                                                border-radius: 16px;
                                                overflow: hidden;
                                                background-color: #E8DCB9;
                                            "
                                        >

                                            <!-- Título -->
                                            <tr>
                                                <td style="
                                                    padding: 25px 25px 10px 25px;
                                                ">

                                                    <h2 style="
                                                        margin: 0;
                                                        color: #2C3E50;
                                                        font-size: 23px;
                                                        font-weight: 600;
                                                        line-height: 1.3;
                                                    ">
                                                        📰 Nueva noticia
                                                    </h2>

                                                </td>
                                            </tr>

                                            <!-- Información -->
                                            <tr>
                                                <td style="
                                                    padding: 0 25px 15px 25px;
                                                ">

                                                    <span style="
                                                        color: #777777;
                                                        font-size: 14px;
                                                    ">
                                                        Nueva publicación en Bestiario
                                                    </span>

                                                </td>
                                            </tr>

                                            <!-- Contenido de noticia -->
                                            <tr>
                                                <td style="
                                                    padding: 10px 25px 25px 25px;
                                                ">

                                                    <h3 style="
                                                        margin: 0 0 15px 0;
                                                        color: #2C3E50;
                                                        font-size: 20px;
                                                        font-weight: 600;
                                                        line-height: 1.4;
                                                    ">
                                                        %s
                                                    </h3>

                                                    <p style="
                                                        margin: 0;
                                                        color: #3f3f3f;
                                                        font-size: 15px;
                                                        line-height: 1.6;
                                                    ">
                                                        Se ha publicado una nueva noticia
                                                        en el Bestiario.
                                                        Ingresá a la aplicación para
                                                        conocer todos los detalles.
                                                    </p>

                                                </td>
                                            </tr>

                                            <!-- Botón -->
                                            <tr>
                                                <td align="center" style="
                                                    padding: 0 25px 30px 25px;
                                                ">

                                                    <a
                                                        href="http://localhost:8080/Bestiario/noticias/listar"
                                                        style="
                                                            display: inline-block;
                                                            padding: 12px 25px;
                                                            background-color: #8E6E53;
                                                            color: #FFFFFF;
                                                            text-decoration: none;
                                                            font-size: 15px;
                                                            font-weight: 600;
                                                            border-radius: 10px;
                                                        "
                                                    >
                                                        Ver noticia
                                                    </a>

                                                </td>
                                            </tr>

                                        </table>

                                    </td>
                                </tr>

                                <!-- Footer -->
                                <tr>
                                    <td align="center" style="
                                        padding: 20px;
                                        background-color: #E9E4DA;
                                    ">

                                        <p style="
                                            margin: 0;
                                            color: #777777;
                                            font-size: 12px;
                                        ">
                                            © 2026 Bestiario
                                        </p>

                                    </td>
                                </tr>

                            </table>

                        </td>
                    </tr>
                </table>

            </body>
            </html>
            """.formatted(tituloNoticia);
    }
}