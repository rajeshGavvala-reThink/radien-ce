/*
 * Copyright (c) 2021-present radien GmbH. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ${package}.ms.client;

import ${package}.exception.NotFoundException;

import ${package}.ms.client.exceptions.BadRequestException;
import ${package}.ms.client.exceptions.InternalServerErrorException;

import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

import javax.el.MethodNotFoundException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class ${entityResourceName}ResponseExceptionMapper implements
        ResponseExceptionMapper<Exception> {
    private static final long serialVersionUID = 6812608123262000052L;

    @Override
    public boolean handles(int statusCode, MultivaluedMap<String, Object> headers) {
        return statusCode == 400        // Bad Request
                || statusCode == 401
                || statusCode == 404    // Not Found
                || statusCode == 500    // Internal Server Error
                ;
    }

    @Override
    public Exception toThrowable(Response response) {
        switch(response.getStatus()) {
            case 400: return new BadRequestException(response.readEntity(String.class));
            case 401: return new MethodNotFoundException(response.readEntity(String.class));
            case 404: return new NotFoundException(response.readEntity(String.class));
            case 500: return new InternalServerErrorException(response.readEntity(String.class));
        }
        return null;
    }

}