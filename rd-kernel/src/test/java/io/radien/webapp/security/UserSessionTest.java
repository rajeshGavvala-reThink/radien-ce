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
package io.radien.webapp.security;

import io.radien.api.OAFAccess;
import io.radien.api.OAFProperties;
import io.radien.api.model.user.SystemUser;
import io.radien.webapp.JSFUtil;
import java.io.IOException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(PowerMockRunner.class)
@PrepareForTest({JSFUtil.class, FacesContext.class, ExternalContext.class})
public class UserSessionTest {

    @InjectMocks
    private UserSession userSession;

    @Mock
    private OAFAccess oaf;

    private FacesContext facesContext;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        PowerMockito.mockStatic(FacesContext.class);
        PowerMockito.mockStatic(JSFUtil.class);

        facesContext = mock(FacesContext.class);
        when(FacesContext.getCurrentInstance()).thenReturn(facesContext);

        ExternalContext externalContext = mock(ExternalContext.class);
        when(facesContext.getExternalContext()).thenReturn(externalContext);

        Flash flash = mock(Flash.class);
        when(externalContext.getFlash()).thenReturn(flash);

        when(JSFUtil.getFacesContext()).thenReturn(facesContext);
        when(JSFUtil.getExternalContext()).thenReturn(externalContext);
        when(JSFUtil.getMessage(anyString())).thenAnswer(i -> i.getArguments()[0]);
    }

    /**
     * Test for method {@link UserSession#logout()}
     * @throws ServletException in case of i/o error during request.logout
     * @throws IOException in case of i/o error when performing sendRedirect
     */
    @Test
    public void testLogout() throws ServletException, IOException {
        SystemUser user = mock(SystemUser.class);
        when(user.isEnabled()).thenReturn(Boolean.TRUE);
        userSession.setUser(user);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(JSFUtil.getExternalContext().getRequest()).thenReturn(request);
        when(JSFUtil.getExternalContext().getResponse()).thenReturn(response);
        doNothing().when(request).logout();

        String mockedUrl = "https://domain.io/auth/realms/realmtest/protocol/openid-connect/logout";
        when(oaf.getProperty(OAFProperties.AUTH_LOGOUT_URI)).thenReturn(mockedUrl);

        when(request.toString()).thenReturn("https://localhost:8443/web/public/index.oaf");
        when(request.getServletPath()).thenReturn("/public/index.oaf");
        doNothing().when(response).sendRedirect(anyString());

        Assert.assertTrue(userSession.logout());
    }

    /**
     * Test for method {@link UserSession#logout()} when there is no user active
     * @throws ServletException in case of i/o error during request.logout
     * @throws IOException in case of i/o error when performing sendRedirect
     */
    @Test
    public void testLogoutWhenNoUserActive() throws ServletException, IOException {
        SystemUser user = mock(SystemUser.class);
        when(user.isEnabled()).thenReturn(Boolean.FALSE);
        userSession.setUser(user);
        Assert.assertFalse(userSession.logout());
    }

    /**
     * Test for method {@link UserSession#logout()}
     * @throws ServletException in case of i/o error during request.logout
     * @throws IOException in case of i/o error when performing sendRedirect
     */
    @Test
    public void testLogoutWhenExceptionOccurs() throws ServletException, IOException {
        SystemUser user = mock(SystemUser.class);
        when(user.isEnabled()).thenReturn(Boolean.TRUE);
        userSession.setUser(user);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(JSFUtil.getExternalContext().getRequest()).thenReturn(request);
        when(JSFUtil.getExternalContext().getResponse()).thenReturn(response);
        doThrow(new ServletException("error")).when(request).logout();
        Assert.assertFalse(userSession.logout());
    }

}
