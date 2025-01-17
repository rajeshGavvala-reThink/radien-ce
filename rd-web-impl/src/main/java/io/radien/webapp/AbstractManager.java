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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import java.io.IOException;
import java.io.Serializable;
import java.text.MessageFormat;

/**
 * @author Newton Carvalho
 * Base class for all JSF Manager Beans
 */
public abstract class AbstractManager implements Serializable {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    protected void handleError(Exception e, String pattern, Object ... params) {
        String msg = MessageFormat.format(pattern, params);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Flash flash = facesContext.getExternalContext().getFlash();
        flash.setKeepMessages(true);
        flash.setRedirect(true);
        facesContext.addMessage(null, new FacesMessage(
                FacesMessage.SEVERITY_ERROR, msg, extractErrorMessage(e)));
        log.error(msg, e);
    }

    protected void handleMessage(FacesMessage.Severity severity, String pattern, Object ... params) {
        String msg = MessageFormat.format(pattern, params);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Flash flash = facesContext.getExternalContext().getFlash();
        flash.setKeepMessages(true);
        flash.setRedirect(true);
        facesContext.addMessage(null, new FacesMessage(severity, msg, null));
        log.info(msg);  
    }

    protected String extractErrorMessage(Exception exception) {
        if(exception.getMessage() != null) {
            String errorMsg = (exception instanceof EJBException) ?
                    exception.getCause().getMessage() : exception.getMessage();
            // Bootsfaces growl has issues to handle special characters
            return errorMsg.replace("\n\t", "");
        }
        return "";
    }

    public Logger getLog() {
        return log;
    }


    /**
     * Redirects the user to the Radien home page can be appended with a message or not
     * @throws IOException in case of redirection error
     */
    protected void redirectToHomePage() throws IOException {
        redirectToPage(DataModelEnum.PUBLIC_INDEX_PATH.getValue());
    }


    /**
     * Redirect the navigation to a specified page
     * @param pagePath uri or (sub) path that designates the page (ex: /module/users, or /public/index)
     * @throws IOException in case of any i/o issue during the dispatch process
     */
    protected void redirectToPage(String pagePath) throws IOException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.redirect(context.getRequestContextPath() + pagePath);
    }
}
