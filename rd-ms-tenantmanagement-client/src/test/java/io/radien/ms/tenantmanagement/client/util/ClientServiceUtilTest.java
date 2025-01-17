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
package io.radien.ms.tenantmanagement.client.util;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.net.MalformedURLException;

import static org.junit.Assert.*;

/**
 * @author Bruno Gama
 */
public class ClientServiceUtilTest {

    @InjectMocks
    private ClientServiceUtil clientServiceUtil;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetContractResourceClient() {
        boolean valid = true;
        try {
            clientServiceUtil.getContractResourceClient("http://url.test.pt") ;
        } catch (MalformedURLException e) {
            valid = false;
        }
        assertTrue(valid);
    }

    @Test
    public void testGetTenantResourceClient() {
        boolean valid = true;
        try {
            clientServiceUtil.getTenantResourceClient("http://url.test.pt") ;
        } catch (MalformedURLException e) {
            valid = false;
        }
        assertTrue(valid);
    }

    @Test
    public void testGetActiveTenantResourceClient() {
        boolean valid = true;
        try {
            clientServiceUtil.getActiveTenantResourceClient("http://url.test.pt") ;
        } catch (MalformedURLException e) {
            valid = false;
        }
        assertTrue(valid);
    }
}