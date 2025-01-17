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
package ${package}.ms.service.config;

import ${package}.api.Event;
import ${package}.api.OAFAccess;
import ${package}.api.OAFProperties;
import ${package}.api.SystemProperties;
import org.eclipse.microprofile.config.Config;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.ws.rs.core.Application;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class ${entityResourceName}msOAFTest{

    @InjectMocks
    ${entityResourceName}msOAF ${entityResourceName.toLowerCase()}msOAF;
    @Mock
    OAFAccess oafAccess;
    @Mock
    Config config;
    @Mock
    Event event;
    @Mock
    SystemProperties cfg;
    @Mock
    private Application app;


    private Map<String, Locale> defaultLocalList = new HashMap<>();
    String defaultLocaleProperty;

    @Before
    public void before(){
        MockitoAnnotations.initMocks(this);
        defaultLocalList.put("en", Locale.forLanguageTag("en"));
        defaultLocaleProperty = defaultLocalList.get("en").toString();
    }

    @Test
    public void getVersion_test() {
        assertNull(${entityResourceName.toLowerCase()}msOAF.getVersion());
    }

    @Test
    public void fireEvent_test(){
        ${entityResourceName.toLowerCase()}msOAF.fireEvent(event);
    }

    @Test
    public void findLocale_test(){
        String actual = ${entityResourceName.toLowerCase()}msOAF.findLocale("en").toLanguageTag();
        assertEquals(defaultLocaleProperty, actual);
    }

    @Test
    public void getProperty_test() {
        when(config.getValue(getDefaultLocaleProperty(), String.class)).thenReturn("");
        String expected = ${entityResourceName.toLowerCase()}msOAF.getProperty(OAFProperties.SYS_DEFAULT_LOCALE);
        assertNotNull(expected);
    }

    @Test
    public void getProperty_null_test() {
        String expected = config.getValue(getDefaultLocaleProperty(), String.class);
        assertNull(expected);
    }

    @Test
    public void getDefaultLocale_test() {
        assertEquals(defaultLocaleProperty, ${entityResourceName.toLowerCase()}msOAF.getDefaultLocale().toLanguageTag());
    }

    @Test(expected = MissingResourceException.class)
    public void getResourceBundle_test() {
        ${entityResourceName.toLowerCase()}msOAF.getResourceBundle("");
    }

    private String getDefaultLocaleProperty(){
        String property = "";
        when(oafAccess.getProperty(OAFProperties.SYS_DEFAULT_LOCALE)).thenReturn(property);
        return property;
    }

}
