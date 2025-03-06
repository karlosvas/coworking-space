# Cómo usar la aplicación

## Requerimientos
Para el correcto funcionamiento de la aplicación necesitamos tener algunas cosas en cuenta. Antes que nada, debemos asegurarnos de que disponemos de todas las variables de entorno. En el repositorio encontrarán el archivo `.env.demo`, este contiene una plantilla de claves secretas que debes tener.
Estos datos deberán ser reflejados en el `.env`.

![Ejemplo de archivo .env](src/main/resources/img/ejemploenv.png)

Obviamente estas claves no son válidas y deberás añadir valores válidos.  
`PostgreSQL`: Si eres administrador, ponte en contacto con nosotros para ofrecerte el `.sql` de nuestra base de datos donde necesitarás poner los datos necesarios en la configuración del archivo `.env`.
En caso de no disponer de base de datos, debes ir al `application.properties` y desactivar todo lo relacionado con SQL y activar lo relacionado con H2:

![Ejemplo de archivo H2](src/main/resources/img/ejemploh2.png)

Mientras que con PostgreSQL deberás activar las siguientes funcionalidades:
![Ejemplo de archivo PGadmin](src/main/resources/img/ejemplopgadmin.png)

`Firma token` hace referencia a la firma de JWT que firma las contraseñas para más seguridad de extremo a extremo.  
`Cuenta Gmail` hace referencia al nombre de quien quieres que estén los correos que se van a enviar a los usuarios, junto con la contraseña de aplicación creada, que sirve como una especie de firma para asegurar que es tu cuenta. En el caso de ser administrador, contacta con nosotros y te proporcionaremos una contraseña de aplicación temporal.
Para más referencias: [https://support.google.com/accounts/answer/185833?hl=es](https://support.google.com/accounts/answer/185833?hl=es)

## Cómo levantar la aplicación
Nosotros recomendamos los siguientes comandos:

```bash
# Limpiar los archivos generados en compilaciones anteriores
mvn clean

# Compilar el código y empaquetar el proyecto
mvn package

# Instalar el paquete en el repositorio local de Maven
mvn install

# Ejecutar la aplicación
# O de forma alternativa darle al botón de Run o Play
mvn spring-boot:run
```

Si quieres la documentación Javadoc en el README.md comenta que se necesita exactamente para su creación.  
Una vez iniciada correctamente la aplicación, estos son los pasos a seguir para poder probar los endpoints:

Tenemos dos opciones: Acceder a los endpoints desde Postman o cualquier otro cliente HTTP, o utilizar la interfaz Swagger que viene integrada en la aplicación, con notificaciones incluidas. Esta es su interfaz:

![Ejemplo Swagger](src/main/resources/img/ejemploswagger.png)

Como podemos observar, arriba a la derecha se encuentra la opción de activar o desactivar las notificaciones de SweetAlert2 a tu gusto.

Para probar los endpoints deberemos tener en cuenta que existen tres tipos de usuarios distintos:

- Invitado: Persona que no puede hacer nada más allá que registrarse y loguearse con un usuario.
- Usuario: Persona que puede crear y ver las salas y reservas, que solo están disponibles para ese usuario.
- Administrador: Persona con control total que permite crear y ver salas y reservas para cualquier usuario, así como eliminar cualquier sala o reserva.

Al entrar en la aplicación por defecto eres Invitado.  
Deberás registrarte con un usuario, correo y contraseña válidos desde `users/register`. Una vez registrado, utiliza esos datos para loguearte con ese usuario. Se te creará un token de sesión como se ve en el JSON:

```java
{
  "data": {
    "id": 2,
    "username": "Jorge",
    "email": "jorge@gmail.com",
    "password": "$2a$10$CP.kgcLdLxBGzt4WN2odQue0oz06W3yYxUOaEdKlPYRe5lVY0mWmu",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUk9MRV9VU0VSIiwic3ViIjoiSm9yZ2UiLCJpYXQiOjE3NDEyNTc0MDAsImV4cCI6MTc0MTI5MzQwMH0.D4VofwnzFB_IpMkd7sSxKiK9Eg-ebyI9CEuN42cqDC0",
    "role": "ROLE_USER"
  },
  "hasError": false,
  "message": "User logged in successfully",
  "statusCode": 200,
  "timestamp": "2025-03-06T11:36:40.7725874"
}
```

Deberemos copiar el token y ponerlo en el botón de Autorize que se encuentra arriba junto a un candado. Una vez introducido, pulsamos autorizar.  
Esto nos dará rol de usuario o administrador según corresponda. Una vez hecho esto, ya podemos utilizar la aplicación sin ningún tipo de problema.