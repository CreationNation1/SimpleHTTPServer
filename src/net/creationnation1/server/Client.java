package net.creationnation1.server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

class Client {
    private final Socket socket;
    private final InputStream incoming;
    private final OutputStream outgoing;
    InetAddress inetAddress;
    String requestLine;
    String[] headers;
    byte[] body;

    private void addHeader(String header) {
        // Add a header to the headers array.
        String[] headers = new String[this.headers.length + 1];
        System.arraycopy(this.headers, 0, headers, 0, this.headers.length);
        headers[this.headers.length] = header;
        this.headers = headers;
    }

    Client(Socket socket) throws IOException {
        // Initialize the global variables.
        this.socket = socket;
        inetAddress = socket.getInetAddress();
        requestLine = "";
        headers = new String[0];
        body = new byte[0];
        incoming = socket.getInputStream();
        outgoing = socket.getOutputStream();

        // Create a BufferedReader to read the request.
        BufferedReader requestReader = new BufferedReader(new InputStreamReader(incoming));

        // Read the request line.
        String line;
        if ((line = requestReader.readLine()) != null) {
            requestLine = line;
        }

        // Read the headers.
        while ((line = requestReader.readLine()) != null) {
            if (line.equals("")) {
                break;
            }
            addHeader(line);
        }

        // Read the body.
        while (requestReader.ready()) {
            byte[] body = new byte[this.body.length + 1];
            System.arraycopy(this.body, 0, body, 0, this.body.length);
            body[this.body.length] = (byte) requestReader.read();
            this.body = body;
        }
    }

    void sendResponse(byte[] response) throws IOException {
        // Send the response.
        outgoing.write(response);
        outgoing.flush();

        // Close the socket and byte streams.
        socket.close();
        outgoing.close();
        incoming.close();
    }
}
