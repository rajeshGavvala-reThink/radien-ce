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

import javax.inject.Inject;

import io.radien.api.OAFAccess;

/**
 * Authorization filter object class
 *
 * @author Marco Weiland
 */
public class AuthorizationFilter extends AbstractAuthorizationFilter {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private OAFAccess baseApp;

	/**
	 * Gets the current OAF access
	 * @return the oaf object
	 */
	@Override
	public OAFAccess getOAF() {
		return baseApp;
	}

}
