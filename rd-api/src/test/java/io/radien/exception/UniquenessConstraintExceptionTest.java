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
import static org.junit.Assert.assertNotNull;

/**
 * Class that aggregates UnitTest UniquenessConstraintException
 *
 * @author Rajesh Gavvala
 */
public class UniquenessConstraintExceptionTest {
    /**
     * Asserts UniquenessConstraintException
     */
    @Test
    public void testUniquenessConstraintException(){
        UniquenessConstraintException exception = new UniquenessConstraintException();
        assertNotNull(exception);

        UniquenessConstraintException exception1 = new UniquenessConstraintException("uniquenessConstraint_exception");
        assertEquals("uniquenessConstraint_exception",exception1.getMessage());
    }
}