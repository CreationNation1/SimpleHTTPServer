package net.creationnation1.server;

import java.net.InetAddress;

public interface RequestHandler {
    // The filter determines whether the request should be accepted or not.
    // If the filter returns true, the request will be accepted.
    // The DefaultRequestHandler returns true if the request is a loopback request.
    boolean filter(InetAddress inetAddress, String requestLine, String[] headers, byte[] body);

    // The accept method is called when the request passes through the filter.
    // The DefaultRequestHandler returns the response for an example website.
    byte[] accept(InetAddress inetAddress, String requestLine, String[] headers, byte[] body);

    // The reject method is called when the request fails the filter.
    // The DefaultRequestHandler returns the response for a 403 Forbidden error.
    byte[] reject(InetAddress inetAddress, String requestLine, String[] headers, byte[] body);

    // The error method is called when an error occurs.
    void error(Exception e);
}
