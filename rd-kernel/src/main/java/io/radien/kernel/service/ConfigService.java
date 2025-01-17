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

package io.radien.kernel.service;

import io.radien.api.Configurable;
import io.radien.api.SystemProperties;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import javax.ejb.Stateless;
import java.io.Serializable;

/**
 * Configuration service for the system property
 *
 * @author Nuno Santana
 */
@Stateless
public class ConfigService implements Configurable, Serializable {

	private static final long serialVersionUID = 224814422013232692L;

	/**
	 * by a given system property or endpoint configuration the method will return the correct property
	 * from the configuration provider
	 * @param cfg system property to be found and used
	 * @return a string value of a system property
	 */
	@Override
	public String getProperty(SystemProperties cfg) {
		Config config = ConfigProvider.getConfig();
		return config.getValue(cfg.propKey(),String.class);
	}

}
