/*
 * Copyright (c) 2006-present openappframe.org & its legal owners. All rights reserved.
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
package io.radien.ms.ecm.client.providers;

import io.radien.ms.ecm.client.entities.I18NProperty;
import io.radien.ms.ecm.client.util.I18NPropertyMapper;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * I18N Property Message reader constructor class validator and converter for received messages
 *
 * @author Marco Weiland
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class I18NPropertyMessageBodyReader implements MessageBodyReader<I18NProperty> {

	/**
	 * Validator of the I18N property message body is readable
	 * @param type of the object
	 * @param genericType generic type of the object
	 * @param annotations annotation
	 * @param mediaType type of the given readable field
	 * @return true if the given object is a I18N property object
	 */
	@Override
	 public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
	 	return type.equals(I18NProperty.class);
	 }

	/**
	 * Will call the I18N Property mapper to convert the given object
	 * @param type of the object
	 * @param genericType generic type of the received object
	 * @param annotations annotation
	 * @param mediaType type of the given readable field
	 * @param httpHeaders header of the http received
	 * @param entityStream received object
	 * @return a I18N that has been converted from the entity stream
	 * @throws WebApplicationException This exception may be thrown by a resource method, provider or StreamingOutput
	 * implementation if a specific HTTP error response needs to be produced.
	 */
	 @Override
	 public I18NProperty readFrom(Class<I18NProperty> type, Type genericType, Annotation[] annotations,
								  MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			 throws WebApplicationException {
	 	return I18NPropertyMapper.map(entityStream);
	 }
}
