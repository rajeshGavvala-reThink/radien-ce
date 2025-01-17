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
package ${package}.ms.client.services;

import ${package}.api.entity.Page;
import ${package}.ms.client.entities.${entityResourceName};

import org.junit.Test;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import static org.junit.Assert.assertEquals;

public class PageFactoryTest {
    @Test
    public void testConvert() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        builder.add("currentPage",-1);
        builder.add("results",jsonArrayBuilder.build());
        builder.add("totalPages",1);
        builder.add("totalResults",4);
        Page<${entityResourceName}> page = PageFactory.convert(builder.build());
        assertEquals(-1,page.getCurrentPage());
        assertEquals(0,page.getResults().size());
        assertEquals(1,page.getTotalPages());
        assertEquals(4,page.getTotalResults());
    }
}
