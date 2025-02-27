/*
 * Copyright 2015 Kantega AS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kantega.respiro.collector.jaxrs;

import org.glassfish.jersey.message.MessageUtils;
import org.kantega.respiro.collector.Collector;
import org.kantega.respiro.collector.ExchangeMessage;

import javax.annotation.Priority;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 */
@Priority(Integer.MIN_VALUE+1)
public class ClientCollectingFilter extends CollectingFilter implements ClientRequestFilter, ClientResponseFilter {

    private static final String ENTITY_LOGGER_PROPERTY = ClientCollectingFilter.class.getName() + ".entityLogger";

    private static final int DEFAULT_MAX_ENTITY_SIZE = 8 * 1024;

    public ClientCollectingFilter() {
        super(DEFAULT_MAX_ENTITY_SIZE);
    }

    @Override
    public void filter(ClientRequestContext context) throws IOException {

        Collector.getCurrent().ifPresent((exchangeInfo -> {


            JaxRsExchangeMessage msg = new JaxRsExchangeMessage(ExchangeMessage.Type.REQUEST);

            msg.setMethod(context.getMethod());
            msg.setAddress( context.getUri().toString());
            msg.setHeaders( context.getStringHeaders());


            if (context.hasEntity()) {
                final OutputStream stream = new LoggingStream(msg, context.getEntityStream()) {
                    @Override
                    public void collect() {
                        exchangeInfo.addBackendMessage(msg);
                    }
                };
                context.setEntityStream(stream);
                context.setProperty(ENTITY_LOGGER_PROPERTY, stream);
                // not calling log(b) here - it will be called by the interceptor
            } else {
                exchangeInfo.addBackendMessage(msg);
            }


        }));

    }

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        Collector.getCurrent().ifPresent(exchangeInfo -> {

            try {
                final StringBuilder b = new StringBuilder();

                JaxRsExchangeMessage msg = new JaxRsExchangeMessage(ExchangeMessage.Type.RESPONSE);


                msg.setResponseCode(responseContext.getStatus());
                msg.setHeaders( responseContext.getHeaders());

                if (responseContext.hasEntity()) {
                    responseContext.setEntityStream(logInboundEntity(b, responseContext.getEntityStream(),
                            MessageUtils.getCharset(responseContext.getMediaType())));
                }

                msg.setPayload(b.toString());

                exchangeInfo.addBackendMessage(msg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}


