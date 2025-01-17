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
package io.radien.webapp;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

import org.powermock.api.mockito.PowerMockito;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
/**
 * Abstract virtual test class that contracts the method
 * handleJSFAndFaceContextMessages() to be invoked when
 * its applicable
 *
 * @author Rajesh Gavvala
 */
public abstract class JSFUtilAndFaceContextMessagesTest {
    FacesContext facesContext;

    /**
     * This method constructs and handles JSF util and
     * FaceContext messages
     */
    public final void handleJSFUtilAndFaceContextMessages(){
        FacesContext facesContext;

        PowerMockito.mockStatic(FacesContext.class);
        PowerMockito.mockStatic(JSFUtil.class);

        facesContext = mock(FacesContext.class);
        when(FacesContext.getCurrentInstance()).thenReturn(facesContext);

        ExternalContext externalContext = mock(ExternalContext.class);
        when(facesContext.getExternalContext())
                .thenReturn(externalContext);

        Flash flash = mock(Flash.class);
        when(externalContext.getFlash()).thenReturn(flash);

        when(JSFUtil.getFacesContext()).thenReturn(facesContext);
        when(JSFUtil.getExternalContext()).thenReturn(externalContext);
        when(JSFUtil.getMessage(anyString())).thenAnswer(i -> i.getArguments()[0]);
    }

    /**
     * This method constructs and handles JSF util and
     * FaceContext messages
     * Returns mock FacesContext object
     */
    public final FacesContext getFacesContext(){
        PowerMockito.mockStatic(FacesContext.class);
        PowerMockito.mockStatic(JSFUtil.class);

        facesContext = mock(FacesContext.class);
        when(FacesContext.getCurrentInstance()).thenReturn(facesContext);

        ExternalContext externalContext = mock(ExternalContext.class);
        when(facesContext.getExternalContext())
                .thenReturn(externalContext);

        Flash flash = mock(Flash.class);
        when(externalContext.getFlash()).thenReturn(flash);

        when(JSFUtil.getFacesContext()).thenReturn(facesContext);
        when(JSFUtil.getExternalContext()).thenReturn(externalContext);
        when(JSFUtil.getMessage(anyString())).thenAnswer(i -> i.getArguments()[0]);

        return facesContext;
    }
}
