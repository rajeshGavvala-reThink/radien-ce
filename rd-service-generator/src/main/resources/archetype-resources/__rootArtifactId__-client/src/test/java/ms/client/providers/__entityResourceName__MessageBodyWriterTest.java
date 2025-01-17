/*
 * Copyright (c) 2016-present radien.io & its legal owners. All rights reserved.
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
package ${package}.ms.client.providers;

import ${package}.ms.client.entities.${entityResourceName};
import ${package}.ms.client.services.${entityResourceName}Factory;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class
${entityResourceName}MessageBodyWriterTest {


    ${entityResourceName}MessageBodyWriter target;

    @Before
    public void before() {
        target = new
                ${entityResourceName}MessageBodyWriter();
    }

    @Test
    public void testIsWriteable() {
        assertTrue(target.isWriteable(
                ${entityResourceName}.class,null,null,null));
    }

    @Test
    public void testGetSize() {
        assertEquals(0L,target.getSize(null,null,null,null,null));
    }

    @Test
    public void testWriteTo() throws IOException {
        String result = "{\"" +
                "id\":null," +
                "\"name\":\"a\"" +
                "}";

        ${entityResourceName} ${entityResourceName.toLowerCase()} =
        ${entityResourceName}Factory.create("a");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        target.writeTo(${entityResourceName.toLowerCase()},null,null,null, null,null, baos);

        assertEquals(result,baos.toString());
    }
}
