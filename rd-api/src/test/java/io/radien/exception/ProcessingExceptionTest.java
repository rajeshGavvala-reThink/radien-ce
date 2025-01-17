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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
/**
 * Class that aggregates UnitTest ProcessingException
 *
 * @author Rajesh Gavvala
 */
public class ProcessingExceptionTest {
    /**
     * Asserts ProcessingException
     */
    @Test
    public void testProcessingException(){
        ProcessingException exception = new ProcessingException();
        assertNull(exception.getMessage());

        ProcessingException exception_processing = new ProcessingException("permission_exception");
        assertEquals("permission_exception",exception_processing.getMessage());
    }

}