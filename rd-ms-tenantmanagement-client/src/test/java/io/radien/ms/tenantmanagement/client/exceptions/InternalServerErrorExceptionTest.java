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
package io.radien.ms.tenantmanagement.client.exceptions;

import junit.framework.TestCase;
import org.junit.Test;

public class InternalServerErrorExceptionTest extends TestCase {

    @Test
    public void testInternalServerErrorException(){
        InternalServerErrorException exception = new InternalServerErrorException();
        assertNotNull(exception);
        InternalServerErrorException exception2 = new InternalServerErrorException("message");
        assertEquals("message",exception2.getMessage());
    }

}