package net.creationnation1.server;

import java.nio.charset.StandardCharsets;

public class ResponseBuilder {
    private String responseLine;
    private String[] headers;
    private byte[] body;

    public ResponseBuilder() {
        // Initialize the global variables.
        responseLine = "";
        headers = new String[0];
        body = new byte[0];
    }

    public ResponseBuilder setStatusCode(int code) {
        // Set the response line given the HTTP status code.
        responseLine = "HTTP/1.1 " + code + " " + StatusCodes.getDescription(code);
        return this;
    }

    public ResponseBuilder setResponseLine(String responseLine) {
        // Set the response line directly.
        this.responseLine = responseLine;
        return this;
    }

    public ResponseBuilder setHeaders(String[] headers) {
        // Set the headers directly.
        this.headers = headers;
        return this;
    }

    public ResponseBuilder addHeader(String header) {
        // Add a header to the headers array.
        String[] headers = new String[this.headers.length + 1];
        System.arraycopy(this.headers, 0, headers, 0, this.headers.length);
        headers[this.headers.length] = header;
        this.headers = headers;
        return this;
    }

    public ResponseBuilder addHeader(String name, String value) {
        return addHeader(name + ": " + value);
    }

    public ResponseBuilder setBody(byte[] body) {
        // Set the body directly.
        this.body = body;
        return this;
    }

    public byte[] build() {
        // Response is built using the internet protocol standard: CRLF (carriage return line feed).
        // The first line contains the response line.
        // The following lines contain the headers.
        // There is a blank line between the headers and the body.
        // The last line contains the body.
        final String separator = "\r\n";
        StringBuilder headers = new StringBuilder();
        for (String header : this.headers) {
            headers.append(header).append(separator);
        }
        return (responseLine + separator + headers + separator + new String(body)).getBytes(StandardCharsets.UTF_8);
    }
}
