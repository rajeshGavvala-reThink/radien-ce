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

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import io.radien.ms.permissionmanagement.client.util.ClientServiceUtil;

import java.net.MalformedURLException;

import static org.junit.Assert.*;

public class ClientServiceUtilTest {

    @InjectMocks
    private ClientServiceUtil clientServiceUtil;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetPermissionResourceClient() {
        boolean valid = true;
        try {
            clientServiceUtil.getPermissionResourceClient("http://url.test.pt") ;
        } catch (MalformedURLException e) {
            valid = false;
        }
        assertTrue(valid);
    }

    @Test
    public void testGetActionResourceClient() {
        boolean valid = true;
        try {
            clientServiceUtil.getActionResourceClient("http://url.test.pt") ;
        } catch (MalformedURLException e) {
            valid = false;
        }
        assertTrue(valid);
    }

    @Test
    public void testGetResourceResourceClient() {
        boolean valid = true;
        try {
            clientServiceUtil.getResourceResourceClient("http://url.test.pt") ;
        } catch (MalformedURLException e) {
            valid = false;
        }
        assertTrue(valid);
    }
}