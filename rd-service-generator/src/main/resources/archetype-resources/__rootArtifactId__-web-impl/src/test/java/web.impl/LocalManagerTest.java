/*
 * Copyright (c) 2006-present openappframe.org & its legal owners. All rights reserved.
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
package ${package}.web.impl;

import com.sun.faces.config.InitFacesContext;
import ${package}.api.OAFAccess;
import ${package}.web.impl.i18n.LocaleManager;
import ${package}.webapp.AbstractLocaleManager;
import ${package}.webapp.JSFUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.ServletContext;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

/**
 *
 * @author Rajesh Gavvala
 */
@PowerMockIgnore("javax.management.*")
@PrepareForTest({FacesContext.class, JSFUtil.class})
public class LocalManagerTest {
    @InjectMocks
    LocaleManager localeManager;
    @Mock
    private AjaxBehaviorEvent ajaxBehaviorEvent;
    @Mock
    private FacesContext context;
    @Mock
    private ExternalContext externalContext;
    @Mock
    private ValueChangeEvent valueChangeEvent;
    @Mock
    private AbstractLocaleManager abstractLocaleManager;

    List<String> defaultLocalList = new ArrayList<>();
    Map<String, Locale> defaultLocalList1 = new HashMap<>();

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        defaultLocalList.add("en");
        defaultLocalList.add("new_language");

        defaultLocalList1.put("en", Locale.forLanguageTag("en"));
        defaultLocalList1.put("new_language", Locale.forLanguageTag("new_language"));

        ServletContext sc = mock(ServletContext.class);
        new FakeContext(sc);
        assertEquals(context, FacesContext.getCurrentInstance());
        doNothing().when(context).addMessage(isA(String.class), isA(FacesMessage.class));
        when(context.getExternalContext()).thenReturn(externalContext);

        UIViewRoot uiViewRoot = Mockito.mock(UIViewRoot.class);
        when(context.getViewRoot()).thenReturn(uiViewRoot);
        when(abstractLocaleManager.getClientTzOffset()).thenReturn(getOffset());
    }

    private class FakeContext extends InitFacesContext {
        FakeContext(ServletContext sc) {
            super(sc);
            setCurrentInstance(context);
        }
    }

    @Test(expected = NullPointerException.class)
    public void languageChanged_test() {
        when(valueChangeEvent.getNewValue()).thenReturn("new_language");
        when(abstractLocaleManager.getSupportedLanguages()).thenReturn(defaultLocalList);
        localeManager.languageChanged(valueChangeEvent);
    }

    @Test(expected = NullPointerException.class)
    public void timeZoneChangedListener_getClientOffset_exception_test() {
        when(JSFUtil.getExternalContext().getRequestLocale()).thenReturn(Locale.forLanguageTag("en-US"));
        localeManager.timezoneChangedListener(ajaxBehaviorEvent);
    }

    @Test
    public void timeZoneChangedListener_getTimeZone_test() {
        abstractLocaleManager.setClientTzOffset(anyString());
        String tzOffset = abstractLocaleManager.getClientTzOffset();
        when(localeManager.getClientTzOffset()).thenReturn(tzOffset);
        String expected = TimeZone.getTimeZone(tzOffset).getDisplayName();
        assertEquals(expected,TimeZone.getTimeZone(anyString()).getDisplayName());
    }

    @Test
    public void getUserLanguage_test() {
        String userLanguage = localeManager.getUserLanguage();
        assertNull(userLanguage);
    }

    private String getOffset() {
        Calendar instance = Calendar.getInstance(Locale.forLanguageTag("en-US"));
        String tzID = instance.getTimeZone().getID();
        return String.format("%s (%s)", tzID,
                LocalDateTime.now().atZone(ZoneId.of(tzID)).getOffset().getId().replace("Z", "+00:00"));
    }
}
