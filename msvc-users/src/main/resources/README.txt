***** README ******


Paso 1: Requisitos
-----------

* Antes de levantar el proyecto, asegúrese de tener instalado:

> Java 17+
> Maven 3.8+
> Git
> IDE recomendado: IntelliJ IDEA o VS Code

Pasos para levantar la aplicación:
----------------------------------
> git init
> git clone https://github.com/diegozegar/evaluacion-ntt.git
> cd evaluacion-ntt
> cd msvc-users

Paso 2: Base de Datos:
--------------
*****************************************************************************
NOTA:
Se estableció una configuración para que se creen automáticamente las tablas,
por lo que no será necesario ejecutar los scripts de creación, aun asi, se adjuntaron
los mismos:

> puede verlo adjunto en esta ruta dentro del proyecto: 
- msvc-users\src\main\resources\schema.text

> se puede encontrar los scripts necesarios debajo:
*****************************************************************************

DROP TABLE IF EXISTS phones;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id VARCHAR(36) PRIMARY KEY,
    nombre VARCHAR(255),
    correo VARCHAR(255) UNIQUE NOT NULL,
    contraseña VARCHAR(255),
    active BOOLEAN,
    token VARCHAR(500),

    creado TIMESTAMP,
    modificado TIMESTAMP,
    ultimo_login TIMESTAMP
);

CREATE TABLE phones (
    id IDENTITY PRIMARY KEY,
    numero VARCHAR(50),
    codigo_ciudad VARCHAR(10),
    codigo_pais VARCHAR(10),
    
    user_id VARCHAR(36),
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

Paso 3: Build del proyecto:

> abrir el proyecto
> Ejecutar:

mvn clean package
mvn spring-boot:run


> La aplicación arrancara en el puerto 8081.

Paso 4: Pruebas de la aplicación:
**************************************************************************************************************************************************************

> Crear usuario (POST):
no necesita JWT Auth:
 - Usar la siguiente URL en postman: http://localhost:8081/api/users/create
 - Podemos usar este Json para crear un nuevo usuario:

 {
  "nombre": "User Test 1",
  "correo": "user@test.org",
  "contraseña": "hunter4",
  "telefonos": [
    {
      "numero": "11111",
      "codigoCiudad": "1",
      "codigoPais": "11"
    }, 
    {
      "numero": "22222",
      "codigoCiudad": "2",
      "codigoPais": "2222"
    }
  ]
}

 - Se recibirá como respuesta el siguiente formato de Json:
{
    "id": "f3353f34-9acf-47ee-985f-23dab0624290",
    "creado": "2025-11-21T15:16:53",
    "modificado": "2025-11-21T15:16:53",
    "ultimoLogin": "2025-11-21T15:16:53",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQHRlc3Qub3JnIiwiaWF0IjoxNzYzNzU2MjEzLCJleHAiOjE3NjM3NTk4MTN9.StW3lWF40Z7buRsRm6HUaprAkHdtNb0nFsAmOk2wUFo",
    "active": true
}

**************************************************************************************************************************************************************

> Consultar todos los usuarios (GET):
Se necesita JWT Auth:
 - Usar la siguiente URL en postman: http://localhost:8081/api/users/all_users
 - IMPORTANTE: 
    en la pestaña de Authorization, debemos especificar AuthType el tipo Bearer Token, colocar el token generado:        eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQHRlc3Qub3JnIiwiaWF0IjoxNzYzNzU2MjEzLCJleHAiOjE3NjM3NTk4MTN9.StW3lWF40Z7buRsRm6HUaprAkHdtNb0nFsAmOk2wUFo  
 - Después de ejecutar, recibiremos el siguiente Json como respuesta:

[
    {
        "id": "665b045a-6d45-4675-918d-be7338c2cd00",
        "creado": "2025-11-21T14:21:24",
        "modificado": "2025-11-21T14:21:43",
        "ultimoLogin": "2025-11-21T14:21:43",
        "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQHRlc3Qub3JnIiwiaWF0IjoxNzYzNzUyOTAzLCJleHAiOjE3NjM3NTY1MDN9.6vuPdCwgvHdjDPy05HBfuqoLbxkToLmN08N_4ogChWQ",
        "active": true
    }
]

**************************************************************************************************************************************************************

> Consultar usuario by Id (GET):
Se necesita JWT Auth:
 - Usar la siguiente URL en postman: http://localhost:8081/api/users/find_user_by_id/665b045a-6d45-4675-918d-be7338c2cd00
 - IMPORTANTE:
    * Especificar el Id, al final de la Url: 665b045a-6d45-4675-918d-be7338c2cd00
    * En la pestaña de Authorization, debemos especificar AuthType el tipo Bearer Token, colocar el token generado:        eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQHRlc3Qub3JnIiwiaWF0IjoxNzYzNzU2MjEzLCJleHAiOjE3NjM3NTk4MTN9.StW3lWF40Z7buRsRm6HUaprAkHdtNb0nFsAmOk2wUFo
 - Después de ejecutar, recibiremos el siguiente Json como respuesta:

[
    {
        "id": "665b045a-6d45-4675-918d-be7338c2cd00",
        "creado": "2025-11-21T14:21:24",
        "modificado": "2025-11-21T14:21:43",
        "ultimoLogin": "2025-11-21T14:21:43",
        "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQHRlc3Qub3JnIiwiaWF0IjoxNzYzNzUyOTAzLCJleHAiOjE3NjM3NTY1MDN9.6vuPdCwgvHdjDPy05HBfuqoLbxkToLmN08N_4ogChWQ",
        "active": true
    }
]

**************************************************************************************************************************************************************

> Consultar usuario by Email (GET):
Se necesita JWT Auth:
 - Usar la siguiente URL en postman: http://localhost:8081/api/users/find_user_by_email/user@test.org
 - IMPORTANTE:
    * Especificar el Email, al final de la Url: user@test.org
    * En la pestaña de Authorization, debemos especificar AuthType el tipo Bearer Token, colocar el token generado:        eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQHRlc3Qub3JnIiwiaWF0IjoxNzYzNzU2MjEzLCJleHAiOjE3NjM3NTk4MTN9.StW3lWF40Z7buRsRm6HUaprAkHdtNb0nFsAmOk2wUFo
 - Después de ejecutar, recibiremos el siguiente Json como respuesta:

{
    "id": "665b045a-6d45-4675-918d-be7338c2cd00",
    "creado": "2025-11-21T14:21:24",
    "modificado": "2025-11-21T14:21:43",
    "ultimoLogin": "2025-11-21T14:21:43",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQHRlc3Qub3JnIiwiaWF0IjoxNzYzNzUyOTAzLCJleHAiOjE3NjM3NTY1MDN9.6vuPdCwgvHdjDPy05HBfuqoLbxkToLmN08N_4ogChWQ",
    "active": true
}

**************************************************************************************************************************************************************

> Consultar usuario by Id (PATCH):
Se necesita JWT Auth:
 - Usar la siguiente URL en postman: http://localhost:8081/api/users/partial_update_by_id/665b045a-6d45-4675-918d-be7338c2cd00
 - IMPORTANTE:
    * Especificar el Id, al final de la Url: 665b045a-6d45-4675-918d-be7338c2cd00
    * En la pestaña de Authorization, debemos especificar AuthType el tipo Bearer Token, colocar el token generado:        eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQHRlc3Qub3JnIiwiaWF0IjoxNzYzNzU2MjEzLCJleHAiOjE3NjM3NTk4MTN9.StW3lWF40Z7buRsRm6HUaprAkHdtNb0nFsAmOk2wUFo

 - Podemos utilizar el Json para actualizar los datos:

{
  "nombre": "User Test 1 PATCH",
  "telefonos": [
    {
      "numero": "111111",
      "codigoCiudad": "1",
      "codigoPais": "11"
    }, 
    {
      "numero": "22222",
      "codigoCiudad": "2",
      "codigoPais": "22"
    }, 
    {
      "numero": "3333",
      "codigoCiudad": "33",
      "codigoPais": "3333"
    }
  ]
}

 - Después de ejecutar, recibiremos el siguiente Json como respuesta:

{
    "id": "665b045a-6d45-4675-918d-be7338c2cd00",
    "creado": "2025-11-21T14:21:24",
    "modificado": "2025-11-21T14:25:01",
    "ultimoLogin": "2025-11-21T14:21:43",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQHRlc3Qub3JnIiwiaWF0IjoxNzYzNzUyOTAzLCJleHAiOjE3NjM3NTY1MDN9.6vuPdCwgvHdjDPy05HBfuqoLbxkToLmN08N_4ogChWQ",
    "active": true
}

notemos que la fecha de modificación se actualizo!

**************************************************************************************************************************************************************

> Consultar usuario by Email (PATCH):
Se necesita JWT Auth:
 - Usar la siguiente URL en postman: http://localhost:8081/api/users/partial_update_by_email/user@test.org
 - IMPORTANTE:
    * Especificar el Email, al final de la Url: user@test.org
    * En la pestaña de Authorization, debemos especificar AuthType el tipo Bearer Token, colocar el token generado:        eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQHRlc3Qub3JnIiwiaWF0IjoxNzYzNzU2MjEzLCJleHAiOjE3NjM3NTk4MTN9.StW3lWF40Z7buRsRm6HUaprAkHdtNb0nFsAmOk2wUFo

 - Podemos utilizar el Json para actualizar los datos:

{
  "nombre": "User Test 1 PATCH by Email",
  "telefonos": [
    {
      "numero": "111111",
      "codigoCiudad": "1",
      "codigoPais": "11"
    }
  ]
}

 - Después de ejecutar, recibiremos el siguiente Json como respuesta:

{
    "id": "665b045a-6d45-4675-918d-be7338c2cd00",
    "creado": "2025-11-21T14:21:24",
    "modificado": "2025-11-21T14:25:01",
    "ultimoLogin": "2025-11-21T14:21:43",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQHRlc3Qub3JnIiwiaWF0IjoxNzYzNzUyOTAzLCJleHAiOjE3NjM3NTY1MDN9.6vuPdCwgvHdjDPy05HBfuqoLbxkToLmN08N_4ogChWQ",
    "active": true
}

notemos que la fecha de modificación se actualizo!

**************************************************************************************************************************************************************

> Consultar usuario by Id (PUT):
Se necesita JWT Auth:
 - Usar la siguiente URL en postman: http://localhost:8081/api/users/update_user_by_id/665b045a-6d45-4675-918d-be7338c2cd00
 - IMPORTANTE:
    * Especificar el Id, al final de la Url: 665b045a-6d45-4675-918d-be7338c2cd00
    * En la pestaña de Authorization, debemos especificar AuthType el tipo Bearer Token, colocar el token generado:        eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQHRlc3Qub3JnIiwiaWF0IjoxNzYzNzU2MjEzLCJleHAiOjE3NjM3NTk4MTN9.StW3lWF40Z7buRsRm6HUaprAkHdtNb0nFsAmOk2wUFo

 - Podemos utilizar el Json para actualizar los datos:

{
  "nombre": "User Test 1 PUT",
  "correo": "user@test.org",
  "contraseña": "hunter4",
  "telefonos": [
    {
      "numero": "11111",
      "codigoCiudad": "1",
      "codigoPais": "11"
    }, 
    {
      "numero": "22222",
      "codigoCiudad": "2",
      "codigoPais": "2222"
    }
  ]
}

 - Después de ejecutar, recibiremos el siguiente Json como respuesta:

{
    "id": "665b045a-6d45-4675-918d-be7338c2cd00",
    "creado": "2025-11-21T14:21:24",
    "modificado": "2025-11-21T14:35:13",
    "ultimoLogin": "2025-11-21T14:21:43",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQHRlc3Qub3JnIiwiaWF0IjoxNzYzNzUyOTAzLCJleHAiOjE3NjM3NTY1MDN9.6vuPdCwgvHdjDPy05HBfuqoLbxkToLmN08N_4ogChWQ",
    "active": true
}

notemos que la fecha de modificación se actualizo!

**************************************************************************************************************************************************************

> Consultar usuario by Email (PUT):
Se necesita JWT Auth:
 - Usar la siguiente URL en postman: http://localhost:8081/api/users/partial_update_by_email/user@test.org
 - IMPORTANTE:
    * Especificar el Email, al final de la Url: user@test.org
    * En la pestaña de Authorization, debemos especificar AuthType el tipo Bearer Token, colocar el token generado:        eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQHRlc3Qub3JnIiwiaWF0IjoxNzYzNzU2MjEzLCJleHAiOjE3NjM3NTk4MTN9.StW3lWF40Z7buRsRm6HUaprAkHdtNb0nFsAmOk2wUFo

 - Podemos utilizar el Json para actualizar los datos:

{
  "nombre": "User Test 1 PUT BY EMAIL",
  "correo": "user@test.org",
  "contraseña": "hunter4",
  "telefonos": [
    {
      "numero": "11111",
      "codigoCiudad": "1",
      "codigoPais": "11"
    }, 
    {
      "numero": "22222",
      "codigoCiudad": "2",
      "codigoPais": "2222"
    }
  ]
}

 - Después de ejecutar, recibiremos el siguiente Json como respuesta:

{
    "id": "665b045a-6d45-4675-918d-be7338c2cd00",
    "creado": "2025-11-21T14:21:24",
    "modificado": "2025-11-21T14:25:01",
    "ultimoLogin": "2025-11-21T14:21:43",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQHRlc3Qub3JnIiwiaWF0IjoxNzYzNzUyOTAzLCJleHAiOjE3NjM3NTY1MDN9.6vuPdCwgvHdjDPy05HBfuqoLbxkToLmN08N_4ogChWQ",
    "active": true
}

notemos que la fecha de modificación se actualizo!

**************************************************************************************************************************************************************

> Eliminar usuario by Id (DELETE):
Se necesita JWT Auth:
 - Usar la siguiente URL en postman: http://localhost:8081/api/users/delete_by_user_id/665b045a-6d45-4675-918d-be7338c2cd00
 - IMPORTANTE:
    * Especificar el Id, al final de la Url: 665b045a-6d45-4675-918d-be7338c2cd00
    * En la pestaña de Authorization, debemos especificar AuthType el tipo Bearer Token, colocar el token generado:        eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQHRlc3Qub3JnIiwiaWF0IjoxNzYzNzU2MjEzLCJleHAiOjE3NjM3NTk4MTN9.StW3lWF40Z7buRsRm6HUaprAkHdtNb0nFsAmOk2wUFo
 - Después de ejecutar, recibiremos Forbidden como respuesta, esto esta bien, ya que el usuario no existe mas

**************************************************************************************************************************************************************

> Eliminar usuario by Email (DELETE):
Se necesita JWT Auth:
 - Usar la siguiente URL en postman: http://localhost:8081/api/users/delete_by_user_email/user@test.org
 - IMPORTANTE:
    * Especificar el Email, al final de la Url: user@test.org
    * En la pestaña de Authorization, debemos especificar AuthType el tipo Bearer Token, colocar el token generado:        eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQHRlc3Qub3JnIiwiaWF0IjoxNzYzNzU2MjEzLCJleHAiOjE3NjM3NTk4MTN9.StW3lWF40Z7buRsRm6HUaprAkHdtNb0nFsAmOk2wUFo
 - Después de ejecutar, recibiremos Forbidden como respuesta, esto esta bien, ya que el usuario no existe mas

**************************************************************************************************************************************************************

Adicionalmente se considero una petición para login:

 - Usar la siguiente URL en postman: http://localhost:8081/auth/login
 - Se debe especificar el correo y la clave correcta:
{
  "correo": "user@test.org",
  "contraseña": "hunter4"
}

 - tendremos como respuesta algo como:

{
    "correo": "user@test.org",
    "nombre": "User Test 1",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQHRlc3Qub3JnIiwiaWF0IjoxNzYzNzUyOTAzLCJleHAiOjE3NjM3NTY1MDN9.6vuPdCwgvHdjDPy05HBfuqoLbxkToLmN08N_4ogChWQ",
    "id": "665b045a-6d45-4675-918d-be7338c2cd00"
}