# coworking-space

## Descripción del Proyecto 📋

Coworking Space es un sistema de reservas diseñado para gestionar las salas de trabajo en un espacio de coworking. La API permitirá a los usuarios realizar y consultar reservas en tiempo real, facilitando la administración de los espacios disponibles.

## Metodología de Trabajo 🛠️

Este proyecto sigue la metodología ágil **Scrum** para la gestión y desarrollo de las tareas. Utilizamos **Trello** para organizar y hacer seguimiento de las tareas, asegurando una colaboración eficiente y una entrega continua de valor.

## Equipo de Desarrollo 👥

@karlosvas  
@puriihuaman  
@Lsterpino  
@Rs-845  
Creadores de Bytes Colaborativos: @devch-tech y @Jorexbp  
Canal de Twitch: [Bytes Colaborativos](https://www.twitch.tv/api/bytescolaborativos)

## Funcionalidades Principales ✨

- **CRUD de Servicios**: Gestión de los diferentes servicios que ofrece el coworking.
- **CRUD de Reservas**: Creación, consulta, modificación y eliminación de reservas.
- **Autenticación de Usuarios**: Registro e inicio de sesión mediante email y contraseña.
- **Uso de Tokens con Spring Security** (sin encriptación por el momento).
- **Notificaciones por correo electrónico** a los usuarios incluidos en una reserva.

## Tecnologías Utilizadas ✨

- **Backend**: Java con Spring Boot
- **Seguridad**: Spring Security (uso de tokens JWT)
- **Base de Datos**: PostgreSQL / MySQL
- **Correo Electrónico**: JavaMailSender
- **API Docs**: Swagger / OpenAPI

## Instalación y Configuración 🐧

```bash
# Clona el repositorio
git clone https://github.com/karlosvas/coworking-space.git
cd coworking-space

# Configura la base de datos en application.properties o application.yml, y crea la base de datos en tu servidor local o hazlo con h2 en memoria.
# Crea el .env con las variables de entorno necesarias para la configuración del correo electrónico.

# Ejecuta la aplicación
mvn spring-boot:run
```

## Contribución 🟢

Este es un proyecto colaborativo de la comunidad **Bytes Colaborativos**. Si deseas contribuir, abre un issue o realiza un pull request con tus mejoras.  

> [!NOTE]  
> Para mas informacion de como usarlo ver el archivo USAGE.md  

## Documentación

Encontrarás toda la documentación generada por Swagger en la siguiente URL: [http://localhost:8080/swagger-ui.html](http://localhost:8080/api/swagger-ui.html) o en el javadoc generado en la carpeta target/site/apidocs/index.html, para generarlo ejectuar el comand `mvn javadoc:javadoc`
Para obtener el .jar ejecutable, ejecutar el comando `mvn clean package`
Para obtener todas las variables de entorno contacte con el equipo de desarrollo, o cree un archivo .env en la raíz del proyecto con las variables especificadas en el archivo .env.example
El .jar se puede crear utilizando el comando `mvn clean package` y se ejecuta con el comando `java -jar coworking-space-0.0.1-SNAPSHOT.jar`

### Endpoints 🔗

- `/api/users`
  Registrar usuario /user/register  
  Iniciar sesión /user/login  
  Registrar administrador /user/admin/register  
  Obtener recursos del usuario /user/resources  
  Obtener todos los usuarios /user/list  
  Eliminar usuario /user/delete/{id}  

- `/api/reservations`
  Obtener todas las reservas /reservations  
  Obtener reservas por usuario /reservations/user/{id}  
  Obtener reservas por sala /reservations/room/{id}  
  Obtener reservas por estado /reservations/status/{status}  
  Obtener reservas por fecha /reservations/date/{date}  
  Crear reserva /reservations/create  
  Actualizar reserva /reservations/update/{id}  
  Eliminar reserva /reservations/delete/{id}  

- `/api/rooms`
  Obtener todas las salas /rooms  
  Obtener sala por ID /rooms/{id}  
  Crear sala /rooms/create  
  Actualizar sala /rooms/update/{id}  
  Eliminar sala /rooms/delete/{id}  

### Tablas y Estructura 📊

#### Tabla `user`

| Campo    | Tipo   | Descripción            |
| -------- | ------ | ---------------------- |
| user_id  | UUID   | Identificador único    |
| name     | String | Nombre del usuario     |
| email    | String | Correo electrónico     |
| password | String | Contraseña del usuario |

#### Tabla `reservation`

| Campo          | Tipo                    | Descripción                                |
| -------------- | ----------------------- | ------------------------------------------ |
| reservation_id | UUID (PK)               | Identificador único de la reserva          |
| user_id        | UUID (FK)               | Identificador del usuario que reserva      |
| room_id        | UUID (FK)               | Identificador de la sala reservada         |
| start_date     | Timestamp               | Fecha y hora de inicio de la reserva       |
| end_date       | Timestamp               | Fecha y hora de finalización de la reserva |
| reserve_status | Enum(ReservationStatus) | Estado de la reserva                       |
| description    | String                  | Descripción de la reserva                  |

#### Tabla `room`

| Campo      | Tipo             | Descripción                    |
| ---------- | ---------------- | ------------------------------ |
| room_id    | UUID (PK)        | Identificador único de la sala |
| name       | String           | Nombre de la sala              |
| num_status | Enum(RoomStatus) | Estado de la sala              |
| capacity   | int              | Capacidad de la sala           |
