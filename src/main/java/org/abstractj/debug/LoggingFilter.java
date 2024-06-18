/*
 * Copyright (C) 2024 Bruno Oliveira<bruno@abstractj.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

// Uncomment the @Provider annotation to enable this filter - used for debug purposes
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
