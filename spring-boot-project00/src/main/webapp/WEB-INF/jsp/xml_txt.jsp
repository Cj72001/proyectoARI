<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <%@ include file="/WEB-INF/jsp/include-css.jsp" %>
    <title>TXT a XML</title>
</head>

<body>

    <!-- incluyendo header -->
    <%@ include file="/WEB-INF/jsp/header.jsp" %>
    
    
    <div class="container-wrapper">
    
   <div class="container-box box">
        <div class="box div-texto  custom-scrollbar">
            <p>
                Lorem, ipsum dolor sit amet consectetur adipisicing elit. Assumenda ad, a commodi libero animi iusto
                facere sint sed repellendus possimus voluptatibus tempora nostrum? Veritatis laboriosam ipsa dolore amet
                saepe aut. Lorem ipsum dolor sit amet consectetur adipisicing elit. Doloremque soluta obcaecati laborum
                accusamus dignissimos reiciendis voluptatibus fugiat, magnam odio accusantium quam quisquam temporibus
                expedita ut repellat cumque rerum eum itaque.Lorem, ipsum dolor sit amet consectetur adipisicing elit. Assumenda ad, a commodi libero animi iusto
                facere sint sed repellendus possimus voluptatibus tempora nostrum? Veritatis laboriosam ipsa dolore amet
                saepe aut. Lorem ipsum dolor sit amet consectetur adipisicing elit. Doloremque soluta obcaecati laborum
                accusamus dignissimos reiciendis voluptatibus fugiat, magnam odio accusantium quam quisquam temporibus
                expedita ut repellat cumque rerum eum itaque.Lorem, ipsum dolor sit amet consectetur adipisicing elit. Assumenda ad, a commodi libero animi iusto
                facere sint sed repellendus possimus voluptatibus tempora nostrum? Veritatis laboriosam ipsa dolore amet
                saepe aut. Lorem ipsum dolor sit amet consectetur adipisicing elit. Doloremque soluta obcaecati laborum
                accusamus dignissimos reiciendis voluptatibus fugiat, magnam odio accusantium quam quisquam temporibus
                expedita ut repellat cumque rerum eum itaque.Lorem, ipsum dolor sit amet consectetur adipisicing elit. Assumenda ad, a commodi libero animi iusto
                facere sint sed repellendus possimus voluptatibus tempora nostrum? Veritatis laboriosam ipsa dolore amet
                saepe aut. Lorem ipsum dolor sit amet consectetur adipisicing elit. Doloremque soluta obcaecati laborum
                accusamus dignissimos reiciendis voluptatibus fugiat, magnam odio accusantium quam quisquam temporibus
                expedita ut repellat cumque rerum eum itaque.Lorem, ipsum dolor sit amet consectetur adipisicing elit. Assumenda ad, a commodi libero animi iusto
                facere sint sed repellendus possimus voluptatibus tempora nostrum? Veritatis laboriosam ipsa dolore amet
                saepe aut. Lorem ipsum dolor sit amet consectetur adipisicing elit. Doloremque soluta obcaecati laborum
                accusamus dignissimos reiciendis voluptatibus fugiat, magnam odio accusantium quam quisquam temporibus
                expedita ut repellat cumque rerum eum itaque.
            </p>

        </div> <!-- container-box -->
        
  <div class="box separador2">

            <form method="post" action="abrirArchivo">
                <div class="box">

                    <label>Seleccione el archivo a convertir:</label>
                    <h3>${error1}</h3>

                    <br>
                    <input class="btn1" type="submit" value="Abrir Archivo">
                </div> <!-- End Box -->
            </form>

            <form method="post" action="convertirArchivo">
                <div class="box">

                    <label>Clave para encriptar:</label>

                    <input type="text" name="delimitador" placeholder=","
                           onFocus="field_focus(this, 'delimitador');"
                           onblur="field_blur(this, 'delimitador');" class="del" />

                    <label>Seleccione el archivo a convertir:</label>
                    <input type="text" name="llave" placeholder="Clave123"
                           onFocus="field_focus(this, 'llave');"
                           onblur="field_blur(this, 'llave');" class="clave" />

                    <h3>${error2}</h3>

                    <br>
                    <input class="btn2" type="submit" value="Guardar Archivo">
                </div> <!-- End Box -->
            </form>
        </div> <!-- End separador2 -->
        
        
</div><!-- container-wrapper -->

   

</body>

</html>
