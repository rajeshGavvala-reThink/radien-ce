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
package io.radien.exception;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Test class to infer the behaviour of LinkedAuthorizationException
 * @author Newton Carvalho
 */
public class LinkedAuthorizationExceptionTest {

    /**
     * Test for the constructor and to infer the embedded message
     */
    @Test
    public void testBadRequestException(){
        LinkedAuthorizationException exception = new LinkedAuthorizationException();
        assertNotNull(exception);
        LinkedAuthorizationException exception2 = new LinkedAuthorizationException("message");
        assertEquals("message",exception2.getMessage());
    }

}
