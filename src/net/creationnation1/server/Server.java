package net.creationnation1.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server {
    private ServerSocket serverSocket;
    private int port;
    private boolean listening = false;
    private RequestHandler requestHandler = new DefaultRequestHandler();

    public Server() {
        // Create a ServerSocket on port 80.
        // This is the default port for HTTP.
        port = 80;
    }

    public Server(int port) {
        // Create a ServerSocket on the specified port.
        this.port = port;
    }

    private void accept(Socket socket) {
        Multithreading.runTask(() -> {
            try {
                // Create a Client object from the socket.
                // The client object will break down the request.
                Client client = new Client(socket);
                InetAddress inetAddress = client.inetAddress;
                String requestLine = client.requestLine;
                String[] headers = client.headers;
                byte[] body = client.body;

                // Check to see if the requests passes the filter's validation.
                if (requestHandler.filter(inetAddress, requestLine, headers, body)) {
                    // If it does, send the appropriate response.
                    byte[] response = requestHandler.accept(inetAddress, requestLine, headers, body);
                    client.sendResponse(response);
                } else {
                    // If it doesn't, an alternate response is sent.
                    client.sendResponse(requestHandler.reject(inetAddress, requestLine, headers, body));
                }
            } catch (IOException e) {
                requestHandler.error(e);
            }
        });
    }

    public int getPort() {
        // Return the port that the server will be listening on.
        return port;
    }

    public void setPort(int port) {
        // Set the port that the server will be listening on.
        this.port = port;
    }

    public RequestHandler getRequestHandler() {
        // Return the RequestHandler that the server will use.
        return requestHandler;
    }

    public void setRequestHandler(RequestHandler requestHandler) {
        // Set the RequestHandler that the server will use.
        this.requestHandler = requestHandler;
    }

    public void listen() {
        if (!listening) {
            // Start listening for connections.
            listening = true;
            try {
                // Create a ServerSocket on the specified port.
                serverSocket = new ServerSocket(port);
                Multithreading.runTask(() -> {
                    try {
                        // Continuously accept connections until the server is closed.
                        while (listening && !serverSocket.isClosed()) {
                            Socket socket = serverSocket.accept();
                            accept(socket);
                        }
                    } catch (SocketException e) {
                        // Prevent the server from crashing if the server is closed at the same time as a connection is being accepted.
                        if (!serverSocket.isClosed()) {
                            requestHandler.error(e);
                        }
                    } catch (IOException e) {
                        requestHandler.error(e);
                    }
                });
            } catch (IOException e) {
                requestHandler.error(e);
            }
        }
    }

    public void close() {
        // Stop listening for connections.
        listening = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            requestHandler.error(e);
        }
    }

    public void restart() {
        // Restart the server with underlying calls to close() and listen().
        close();
        listen();
    }
}