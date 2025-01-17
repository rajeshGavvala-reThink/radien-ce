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
package io.radien.ms.permissionmanagement.client.providers;

import io.radien.ms.permissionmanagement.client.entities.Action;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ActionMessageBodyWriterTest {

    @InjectMocks
    ActionMessageBodyWriter target;

    @Test
    public void testIsWriteable() {
        assertTrue(target.isWriteable(Action.class,null,null,null));
    }

    @Test
    public void testGetSize() {
        assertEquals(0L,target.getSize(null,null,null,null,null));
    }

    @Test
    public void testWriteTo() throws IOException {
        String result = "{\"" +
                "id\":null," +
                "\"name\":\"a\"," +
                "\"createUser\":null," +
                "\"lastUpdateUser\":null" +
                "}";

        Action action = new Action();
        action.setName("a");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        target.writeTo(action,null,null,
                null, null,null, out);

        assertEquals(result, out.toString());
    }

    @Test
    public void test2WriteTo() throws IOException {
        String result = "{\"" +
                "id\":1," +
                "\"name\":\"action-a\"," +
                "\"createUser\":28," +
                "\"lastUpdateUser\":100" +
                "}";

        Action action = new Action();
        action.setName("action-a");
        action.setId(1L);
        action.setCreateUser(28L);
        action.setLastUpdateUser(100L);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        target.writeTo(action,null,null,
                null, null,null, out);

        assertEquals(result, out.toString());
    }
}