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
package ${package}.web.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.text.MessageFormat;

/**
 * @author Rajesh Gavvala
 * @author Newton Carvalho
 * Base class for all JSF Manager Beans
 */
public abstract class AbstractManager implements Serializable {
    protected static final long serialVersionUID = 6812608123262000037L;
    protected Logger log = LoggerFactory.getLogger(this.getClass());

    protected void handleError(Exception e, String pattern, Object ... params) {
        String msg = MessageFormat.format(pattern, params);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                FacesMessage.SEVERITY_ERROR, msg, e.getMessage()));
        log.error(msg, e);
    }

    protected void handleMessage(FacesMessage.Severity severity, String pattern, Object ... params) {
        String msg = MessageFormat.format(pattern, params);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, msg, null));
        log.info(msg);
    }

    public Logger getLog() {
        return log;
    }
}
