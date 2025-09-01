package org.eci.arep;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.*;
import java.io.*;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HttpServerTest {

    @Test
    void testHandleDynamicRequest_NotFound() throws Exception {
        HttpRequest request = new HttpRequest();
        request.setUri(new URI("/no-exist"));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Socket mockSocket = mock(Socket.class);
        when(mockSocket.getOutputStream()).thenReturn(outputStream);

        HttpServer.handleDynamicRequest(mockSocket, request);

        String response = outputStream.toString();
        assertTrue(response.contains("404 Not Found"));
        assertTrue(response.contains("Not found: /no-exist"));
    }

    @Test
    void testStaticFileServingExistingFile() throws Exception {
        Socket mockSocket = mock(Socket.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter out = new PrintWriter(outputStream, true);
        
        when(mockSocket.getOutputStream()).thenReturn(outputStream);
        
        URI requestUri = new URI("/index.html");
        HttpRequest request = new HttpRequest();
        request.setUri(requestUri);
        HttpServer.handleHttpRequest(request, out, mockSocket);
        
        String response = outputStream.toString();
        assertTrue(response.contains("HTTP/1.1 200 OK"));
        assertTrue(response.contains("text/html"));
    }

    @Test
    void testStaticFileServingNonExistingFile() throws Exception {
        Socket mockSocket = mock(Socket.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter out = new PrintWriter(outputStream, true);
        
        when(mockSocket.getOutputStream()).thenReturn(outputStream);
        
        URI requestUri = new URI("/nonexistent.html");
        HttpRequest request = new HttpRequest();
        request.setUri(requestUri);
        HttpServer.handleHttpRequest(request, out, mockSocket);
        
        String response = outputStream.toString();
        assertTrue(response.contains("HTTP/1.1 404 Not Found"));
    }


    @Test
    void testContentTypeDetection() throws Exception {
        assertEquals("text/html; charset=utf-8", HttpServer.getContentType(Paths.get("test.html")));
        assertEquals("text/html; charset=utf-8", HttpServer.getContentType(Paths.get("test.htm")));
        assertEquals("text/css; charset=utf-8", HttpServer.getContentType(Paths.get("style.css")));
        assertEquals("application/javascript; charset=utf-8", HttpServer.getContentType(Paths.get("script.js")));
        assertEquals("application/json; charset=utf-8", HttpServer.getContentType(Paths.get("data.json")));
        assertEquals("image/png", HttpServer.getContentType(Paths.get("image.png")));
        assertEquals("image/jpeg", HttpServer.getContentType(Paths.get("photo.jpg")));
        assertEquals("image/jpeg", HttpServer.getContentType(Paths.get("photo.jpeg")));
        assertEquals("image/gif", HttpServer.getContentType(Paths.get("animation.gif")));
        assertEquals("image/svg+xml", HttpServer.getContentType(Paths.get("vector.svg")));
        assertEquals("image/x-icon", HttpServer.getContentType(Paths.get("favicon.ico")));
        assertEquals("application/octet-stream", HttpServer.getContentType(Paths.get("unknown.xyz")));
    }

    @Test
    void testContentTypeDetectionCaseInsensitive() throws Exception {
        assertEquals("text/html; charset=utf-8", HttpServer.getContentType(Paths.get("TEST.HTML")));
        assertEquals("text/css; charset=utf-8", HttpServer.getContentType(Paths.get("STYLE.CSS")));
        assertEquals("image/png", HttpServer.getContentType(Paths.get("IMAGE.PNG")));
    }

    @Test
    void testStaticFilesServesIndexHtml() throws Exception {
        Path tempDir = Files.createTempDirectory("webroot");
        Path indexFile = tempDir.resolve("index.html");

        String htmlContent = "<html><body><h1>Hello Static</h1></body></html>";
        Files.writeString(indexFile, htmlContent);

        HttpServer.staticfiles(tempDir.toString());


        HttpRequest request = new HttpRequest();
        request.setUri(new URI("/index.html"));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Socket mockSocket = mock(Socket.class);
        when(mockSocket.getOutputStream()).thenReturn(outputStream);

        PrintWriter out = new PrintWriter(outputStream, true);

        HttpServer.handleHttpRequest(request, out, mockSocket);

        String response = outputStream.toString();

        assertTrue(response.contains("200 OK"));
        assertTrue(response.contains("content-type"));
        assertTrue(response.contains("Hello Static"));

        verify(mockSocket, times(1)).getOutputStream();
    }
}