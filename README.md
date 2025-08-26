# Java Microframework
This is a basic Web Java Framework implementation in Java using ServerSocket.

## Getting Started
These instructions will help you get a copy of the project running on your local machine for development and testing purposes.

### Prerequisites
You will need the following installed:
- [Java 17+](https://jdk.java.net/)
- [Maven 3.6+](https://maven.apache.org/install.html)
- Git (optional, for cloning)

### Installing
Clone the repository:
```bash
git clone https://github.com/thesrcielos/JavaMicroframework
cd JavaMicroframework
```

Build the project using Maven:
```bash
mvn clean install
```

### Running the Application

#### HTTP Server
Start the HTTP server on port 35000:
```bash
java -cp target/HttpServer-1.0-SNAPSHOT.jar org.eci.arep.Main
```

The server will be available at `http://localhost:35000`

#### Usage and response Example of Microframework

Code example
````
public static void main(String[] args) throws IOException, URISyntaxException {
        staticfiles("public");
        get("/hello", (req, resp) -> "Hello " + req.getValues("name"));
        get("/pi", (req, resp) -> {
            return String.valueOf(Math.PI);
        });
        get("/app/hello", (req, res) -> "Hello");
        HttpServer.run();
    }
````
Go to postman and type localhost:35000/hello?name=<your-name>
![](/assets/img1.png)
Now check the Pi endpoint
![](/assets/img2.png)


## Features
- **HTTP Server**: Complete HTTP/1.1 server implementation supporting GET and POST methods
- **GET Method HTTP**: A simple way to create a get endpoint of HTTP
- **Static File Serving**: Serves HTML, CSS, JS, and image files from the directory you specify

## Diseño de la clase HttpServer

La clase HttpServer implementa un servidor web ligero utilizando la librería estándar de Java, específicamente el paquete java.net.ServerSocket. El objetivo de esta clase es brindar una infraestructura básica para:
Servir archivos estáticos desde un directorio definido por el usuario.
Atender solicitudes dinámicas mediante funciones registradas en tiempo de ejecución.
Proporcionar un mecanismo extensible para la construcción de aplicaciones web simples sin necesidad de frameworks adicionales.

### Responsabilidades de la clase

La clase HttpServer asume las siguientes responsabilidades:
Gestión de conexiones: escuchar en un puerto determinado (35000) y aceptar clientes entrantes.
Procesamiento de solicitudes HTTP: parsear la línea de petición (método, URI y versión HTTP) así como las cabeceras asociadas.
Resolución de recursos:
Buscar archivos estáticos en el directorio raíz definido.
Delegar en controladores dinámicos si no se encuentra un archivo.
Generación de respuestas: construir objetos HttpResponse con código de estado, cabeceras y cuerpo.
Extensibilidad: permitir el registro de servicios dinámicos asociados a rutas específicas.

### Métodos de configuración

get(String path, HttpService service)
Registra un servicio dinámico que será invocado cuando el servidor reciba una solicitud a la ruta especificada.

staticfiles(String path)
Establece el directorio raíz de archivos estáticos.

### Métodos de ejecución
run()
Inicia el servidor en el puerto 35000, acepta conexiones y procesa las solicitudes entrantes en un ciclo indefinido.

main(String[] args)
Método de entrada que invoca la ejecución del servidor.

## Usage Examples

### Basic HTTP Request
```bash
curl http://localhost:35000/
```

### Coding Style
The code follows the Google Java Style Guide and was formatted accordingly using Maven Checkstyle plugin.

## Running the tests

Run the automated unit tests with Maven:
```
mvn test
```
Unit Tests

Unit tests are written using JUnit and ensure the logic in **HttpServer** works as expected.
Some of the things that are being tested are:
* Http Server serves static files
* Http Response structure 
* Http Server API Endpoints

Example:
This test is testing that the required parameters are valid

```
@Test
    void testHandleDynamicRequest_Found() throws Exception {
        HttpServer.get("/hello", (req, resp) -> "Hello " + req.getValues("name"));

        HttpRequest request = new HttpRequest();
        request.setUri(new URI("/hello?name=Diego"));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Socket mockSocket = mock(Socket.class);
        when(mockSocket.getOutputStream()).thenReturn(outputStream);

        HttpServer.handleDynamicRequest(mockSocket, request);

        String response = outputStream.toString();
        assertTrue(response.contains("200 OK"));
        assertTrue(response.contains("Hello Diego"));

        verify(mockSocket, times(1)).getOutputStream();
    }
```

## Deployment
To package the application:
```bash
mvn package
```

Then run the packaged JAR:
```bash
java -jar target/HttpServer-1.0-SNAPSHOT.jar
```

## Javadoc
Code documentation is available in `target/site/apidocs/index.html` after running:
```bash
mvn javadoc:javadoc
```

## Built With
* [Java 17](https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html) - Programming Language
* [Maven](https://maven.apache.org/) - Dependency Management
* [JUnit 5](https://junit.org/junit5/) - Testing Framework
* [Mockito](https://mockito.org/) - Mocking Framework

## Contributing
Please read **CONTRIBUTING.md** for details on our code of conduct and the process for submitting pull requests.

## Authors
* **Diego Armando Macia Diaz** 

## License
This project is licensed under the MIT License – see the [LICENSE.md](LICENSE.md) file for details.
