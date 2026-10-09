# Ejemplo sencillo de Login

## Tecnologías 
- Java 21
- Spring Boot
- Maven
- Spring Data JPA
- SQLite
- Argon2id

## Requisitos
Java 21 y Maven compatible

## Ejecución
Ejecutar "EjemploLoginApplication" y abrir en el navegador "http://localhost:8080/"

## Funcionamiento

La aplicación permite registrar usuarios e iniciar sesión mediante una interfaz web sencilla. Está desarrollada con Java y Spring Boot, utiliza SQLite para almacenar la información y emplea Argon2id para proteger las contraseñas.

### Registro de usuarios

1. El usuario introduce un nombre de usuario, un correo electrónico y una contraseña.
2. El frontend envía los datos al endpoint `POST /api/auth/register`.
3. El servicio valida los datos recibidos y comprueba que el nombre de usuario y el correo electrónico no estén registrados previamente.
4. La contraseña se transforma en un hash utilizando Argon2id antes de guardar el usuario en la base de datos.
5. La aplicación devuelve una respuesta indicando si el registro se realizó correctamente o si ocurrió algún error.

### Inicio de sesión

1. El usuario introduce su nombre de usuario o correo electrónico y su contraseña.
2. El frontend envía las credenciales al endpoint `POST /api/auth/login`.
3. El servicio busca al usuario correspondiente y compara la contraseña proporcionada con el hash almacenado.
4. Si las credenciales son correctas, la aplicación devuelve un mensaje de inicio de sesión exitoso. En caso contrario, devuelve un mensaje de error genérico.

### Almacenamiento y seguridad

- Base de datos: SQLite almacena los usuarios registrados en el archivo local `login.db`.
- Persistencia: Spring Data JPA y Hibernate gestionan las operaciones de lectura y escritura de los usuarios.
- Protección de contraseñas: las contraseñas no se almacenan en texto plano; se utiliza Argon2id para generar sus hashes.
- Validación: se comprueba que los datos requeridos estén presentes y que no existan nombres de usuario o correos electrónicos duplicados.
- Interfaz web: HTML y JavaScript permiten alternar entre los formularios de registro e inicio de sesión, enviar las solicitudes al backend y mostrar los mensajes correspondientes.
