package org.abstractj.debug;

import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.client.ClientResponseContext;
import jakarta.ws.rs.client.ClientResponseFilter;
import org.jboss.logging.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

//@Provider
public class LoggingFilter implements ClientRequestFilter, ClientResponseFilter {

    private static final Logger LOGGER = Logger.getLogger(LoggingFilter.class);

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        LOGGER.infof("Request URI: %s", requestContext.getUri());
        LOGGER.infof("Request Method: %s", requestContext.getMethod());
        LOGGER.infof("Request Headers: %s", requestContext.getHeaders());
    }

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        LOGGER.infof("Response Status: %d", responseContext.getStatus());
        LOGGER.infof("Response Headers: %s", responseContext.getHeaders());
        if (responseContext.hasEntity()) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            InputStream entityStream = responseContext.getEntityStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = entityStream.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            byte[] entityBytes = baos.toByteArray();
            LOGGER.infof("Response Entity: %s", new String(entityBytes, StandardCharsets.UTF_8));

            // Reset the entity stream with the captured data
            responseContext.setEntityStream(new ByteArrayInputStream(entityBytes));
        }
    }
}
