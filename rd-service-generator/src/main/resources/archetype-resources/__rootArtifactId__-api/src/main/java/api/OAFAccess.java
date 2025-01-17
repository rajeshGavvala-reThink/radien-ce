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
package ${package}.api;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Rajesh Gavvala
 * @author Marco Weiland
 */
public interface OAFAccess extends Serializable {

	public String getVersion();

	public ResourceBundle getResourceBundle(String bundleName);

	public void fireEvent(Event event);

	public Locale getDefaultLocale();

	public Map<String, Locale> getSupportedLocales();

	public Locale findLocale(String language);
	
	public String getProperty(SystemProperties cfg);

}
