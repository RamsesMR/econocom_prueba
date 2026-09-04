# prueba tecnica angular + spring boot

prueba tecnica de autenticacion desarrollada con spring boot y angular.

el proyecto tiene login con email y contraseña, autenticacion mediante jwt, validacion y renovacion del token, proteccion de rutas y un flujo sso simulado.

## estructura

el repositorio esta dividido en:

```text
backend/
frontend/
database/
README.md
```

- `backend` contiene la api de spring boot
- `frontend` contiene la aplicacion angular
- `database` contiene el script de la base de datos

## versiones utilizadas

backend:

- java 8
- spring boot 2.7.18
- maven
- spring security
- spring data jpa
- jjwt 0.9.1
- mysql 5

frontend:

- node 18
- angular cli 16.2.16
- angular material 16.2.14

durante el desarrollo se ha utilizado mysql 5.

## base de datos

la base de datos utilizada es:

```text
adrian_login
```

antes de arrancar el backend hay que importar el script sql que se encuentra dentro de la carpeta:

```text
database/
```

la configuracion utilizada en local es:

```properties
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/adrian_login
spring.datasource.username=root
spring.datasource.password=
```

si la configuracion de mysql es diferente hay que modificar estos valores en:

```text
backend/src/main/resources/application.properties
```

## usuario de prueba

para probar el login:

```text
email: usuario@prueba.com
password: 654321
```

este mismo usuario se utiliza para el flujo sso simulado.

## arrancar backend

desde la carpeta raiz del proyecto:

```bash
cd backend
```

ejecutar:

```bash
mvn spring-boot:run
```

el backend queda levantado en:

```text
http://localhost:8080
```

## arrancar frontend

en otra terminal:

```bash
cd frontend
```

instalar las dependencias:

```bash
npm install
```

arrancar angular:

```bash
ng serve
```

la aplicacion queda disponible en:

```text
http://localhost:4200
```

## endpoints

ruta base:

```text
/api/auth
```

### login

```text
POST /api/auth/login
```

recibe:

```json
{
  "email": "usuario@prueba.com",
  "password": "654321"
}
```

si las credenciales son correctas devuelve el token junto al nombre y email del usuario.

ejemplo de respuesta:

```json
{
  "token": "jwt",
  "nombre": "Usuario Prueba",
  "email": "usuario@prueba.com"
}
```

si las credenciales no son correctas devuelve un `401`.

### validar token

```text
POST /api/auth/validate
```

el jwt se envia en la cabecera:

```text
Authorization: Bearer TOKEN
```

este endpoint comprueba que el token sea valido, que la firma sea correcta y que no haya caducado.

### renovar token

```text
POST /api/auth/refresh
```

tambien recibe el jwt mediante:

```text
Authorization: Bearer TOKEN
```

si el token actual sigue siendo valido se recupera el usuario y se genera un token nuevo.

en esta implementacion el refresh trabaja con un token que todavia no haya caducado.

### iniciar sso

```text
GET /api/auth/sso
```

inicia el flujo sso simulado.

como no se utiliza un proveedor real, el backend simula un codigo y un usuario autenticado:

```text
codigo-sso-prueba
usuario@prueba.com
```

despues devuelve una respuesta http `302` hacia el callback del frontend.

### callback sso

```text
GET /api/auth/sso/callback
```

recibe:

```text
code
email
```

por ejemplo:

```text
/api/auth/sso/callback?code=codigo-sso-prueba&email=usuario@prueba.com
```

el backend valida el codigo, busca el usuario por email y genera el jwt de la aplicacion.


las contraseñas almacenadas en base de datos estan cifradas utilizando bcrypt.

## jwt

el jwt utiliza el email del usuario como `subject`.

tambien guarda:

- fecha de creacion
- fecha de expiracion
- firma hs256

la duracion configurada para el token es de 15 minutos.

el backend tiene metodos para:

- generar el token
- obtener el email almacenado en el token
- validar el token
- renovar el token

## proteccion de rutas

la ruta:

```text
/inicio
```

esta protegida mediante un `AuthGuard`.

cuando se intenta entrar a inicio, angular comprueba primero si existe un jwt.

si existe, se llama al backend:

```text
POST /api/auth/validate
```

si el backend indica que el token es correcto se permite el acceso.

si no existe token, esta caducado o no es valido, se elimina el token y el usuario vuelve a:

```text
/login
```

tambien hay una ruta comodin para evitar que una url inexistente deje la aplicacion en una pagina en blanco.

## cerrar sesion

al cerrar sesion se elimina el jwt guardado en `localStorage` y se vuelve al login.

## flujo sso

el sso de esta prueba esta simulado.

el flujo es:

```text
usuario pulsa entrar con sso
        |
        v
GET /api/auth/sso
        |
        v
spring genera codigo y usuario simulado
        |
        v
respuesta http 302
        |
        v
/sso/callback de angular
        |
        v
angular obtiene code y email
        |
        v
GET /api/auth/sso/callback
        |
        v
spring valida el codigo
        |
        v
busca el usuario en mysql
        |
        v
genera el jwt
        |
        v
angular guarda el token
        |
        v
muestra el resultado del sso
        |
        v
3 segundos despues redirige a /inicio
```

en una integracion real el endpoint `/sso` redirigiria a un proveedor como google o microsoft.

el proveedor autenticaria al usuario y devolveria un codigo.

despues el backend validaria o intercambiaria ese codigo directamente con el proveedor para obtener la identidad real del usuario y generar el jwt de la aplicacion.

en esta prueba esta parte se ha simulado completamente.

## spring security

se utiliza `SecurityFilterChain` para configurar la seguridad del backend.

las rutas de autenticacion estan permitidas publicamente:

```text
/api/auth/login
/api/auth/validate
/api/auth/refresh
/api/auth/sso
/api/auth/sso/callback
```

el resto de peticiones necesitan autenticacion.

tambien esta configurado cors para permitir la comunicacion durante desarrollo entre:

```text
frontend: http://localhost:4200
backend:  http://localhost:8080
```

csrf esta desactivado porque la api trabaja con jwt y no con una sesion tradicional basada en cookies.

## idiomas

la aplicacion tiene soporte para:

- español
- ingles
- frances
- portugues

los textos se cargan desde los json:

```text
src/assets/i18n/es.json
src/assets/i18n/en.json
src/assets/i18n/fr.json
src/assets/i18n/pt.json
```

`IdiomaService` se encarga de cargar el idioma seleccionado.

el idioma se mantiene en:

- login
- callback sso
- inicio

## rutas frontend

```text
/login
/inicio
/sso/callback
```

`/inicio` necesita una autenticacion valida.

## puertos utilizados

```text
angular:     4200
spring boot: 8080
mysql:       3306
```
