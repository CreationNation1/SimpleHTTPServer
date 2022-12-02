package net.creationnation1.server;

import java.net.InetAddress;

class DefaultRequestHandler implements RequestHandler {
    @Override
    public boolean filter(InetAddress inetAddress, String requestLine, String[] headers, byte[] body) {
        return inetAddress.isLoopbackAddress();
    }

    @Override
    public byte[] accept(InetAddress inetAddress, String requestLine, String[] headers, byte[] body) {
        ResponseBuilder responseBuilder = new ResponseBuilder();
        byte[] response;
        
        if (requestLine.startsWith("GET / HTTP/1.1")) {
            String content = "<!DOCTYPE html><html><head><title>Example</title></head><body><h1>Hello World!</h1></body></html>";
            response = responseBuilder.setResponseLine("HTTP/1.1 200 OK")
                    .addHeader("Content-Type: text/html")
                    .addHeader("Content-Length: 97")
                    .setBody(content.getBytes())
                    .build();
        } else {
            response = responseBuilder.setStatusCode(404).build();
        }
        return response;
    }

    @Override
    public byte[] reject(InetAddress inetAddress, String requestLine, String[] headers, byte[] body) {
        return new ResponseBuilder().setStatusCode(403).build();
    }

    @Override
    public void error(Exception e) {
        e.printStackTrace();
    }
}
