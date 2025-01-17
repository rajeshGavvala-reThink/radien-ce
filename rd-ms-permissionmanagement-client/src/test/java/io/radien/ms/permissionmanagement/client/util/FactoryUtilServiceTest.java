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
package io.radien.ms.permissionmanagement.client.util;

import io.radien.api.util.FactoryUtilService;
import junit.framework.TestCase;
import org.junit.Test;
import javax.json.*;

public class FactoryUtilServiceTest extends TestCase {

    JsonObject json;

    public FactoryUtilServiceTest() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        JsonObjectBuilder builder2 = Json.createObjectBuilder();
        builder2.add("testeArray", "testeArray");
        builder2.add("testeArray2", "testeArray2");

        arrayBuilder.add(builder2.build());

        builder.addNull("id");
        builder.add("name", "permissionNameTest");
        builder.add("createUser", 2L);
        builder.addNull("lastUpdateUser");
        builder.add("integerTest", 123);
        builder.add("arrayTest", arrayBuilder.build());
        builder.add("booleanTest", false);
        builder.add("doubleTest", 2.0);
        json = builder.build();
    }

    /**
     * Test to retrieve correctly a String from a JSON
     */
    @Test
    public void testGetStringFromJson() {
        String name = FactoryUtilService.getStringFromJson("name", json);
        assertEquals("permissionNameTest", name);

    }

    @Test
    public void testGetIntFromJson() {
        int integerTest = FactoryUtilService.getIntFromJson("integerTest", json);
        assertEquals(123, integerTest);
    }


    @Test
    public void testGetLongFromJson() {
        Long createUser = FactoryUtilService.getLongFromJson("createUser", json);
        assertEquals((Long) 2L, createUser);
    }

    @Test
    public void testGetArrayFromJson() {
        JsonArray array = FactoryUtilService.getArrayFromJson("arrayTest", json);

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        JsonObjectBuilder builder2 = Json.createObjectBuilder();
        builder2.add("testeArray", "testeArray");
        builder2.add("testeArray2", "testeArray2");

        arrayBuilder.add(builder2.build());

        assertEquals(arrayBuilder.build(), array);
    }

    @Test
    public void testGetArrayFromJsonNonExistingKey() {
        JsonArray array = FactoryUtilService.getArrayFromJson("wolverine", json);
        assertNull(array);
    }

    @Test
    public void testGetBooleanFromJson() {
        boolean booleanValue = FactoryUtilService.getBooleanFromJson("booleanTest", json);
        assertFalse(booleanValue);
    }

    @Test
    public void testGetDoubleFromJson() {
        double doubleValue = FactoryUtilService.getDoubleFromJson("doubleTest", json);
        assertEquals(2.0, doubleValue);
    }

    @Test
    public void testAddValue() {
        JsonObjectBuilder builder = Json.createObjectBuilder();

        FactoryUtilService.addValue(builder, "id", "permissionID");
        FactoryUtilService.addValue(builder, "contract", "permissionContract");

        JsonObject json = builder.build();

        String id = FactoryUtilService.getStringFromJson("id", json);
        String contract = FactoryUtilService.getStringFromJson("contract", json);

        assertEquals("permissionID", id);
        assertEquals("permissionContract", contract);
    }

    @Test
    public void testAddValueLong() {
        JsonObjectBuilder builder = Json.createObjectBuilder();

        FactoryUtilService.addValueLong(builder, "createdUser", 2L);
        FactoryUtilService.addValueLong(builder, "updatedUser", 3L);

        JsonObject json = builder.build();

        Long createdUser = FactoryUtilService.getLongFromJson("createdUser", json);
        Long updatedUser = FactoryUtilService.getLongFromJson("updatedUser", json);

        assertEquals((Long) 2L, createdUser);
        assertEquals((Long) 3L, updatedUser);
    }

    @Test
    public void testAddValueNull(){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        FactoryUtilService.addValue(builder, "id",null);
        JsonObject json = builder.build();
        assertNull(FactoryUtilService.getStringFromJson("id" ,json));
    }

    @Test
    public void testAddValueBoolean(){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        FactoryUtilService.addValueBoolean(builder, "newBooleanTest",false);
        JsonObject json = builder.build();
        assertFalse(FactoryUtilService.getBooleanFromJson("newBooleanTest" ,json));
    }

    @Test
    public void testAddValueDouble(){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        FactoryUtilService.addValueDouble(builder, "newDoubleTest",3.0);
        JsonObject json = builder.build();
        assertEquals(3.0, FactoryUtilService.getDoubleFromJson("newDoubleTest" ,json));
    }

    @Test
    public void testAddValueIntNull(){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        FactoryUtilService.addValueInt(builder, "id",null);
        JsonObject json = builder.build();
        assertNull(FactoryUtilService.getIntFromJson("id" ,json));
    }

    @Test
    public void testAddValueArrayNull(){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        FactoryUtilService.addValueArray(builder, "elements",null);
        JsonObject json = builder.build();
        assertNull(FactoryUtilService.getIntFromJson("elements" ,json));
    }
}