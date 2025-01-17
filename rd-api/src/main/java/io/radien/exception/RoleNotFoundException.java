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
package io.radien.exception;

/**
 * To be describe situations whereas an expected Role could not be found
 *
 * @author Bruno Gama
 */
public class RoleNotFoundException extends Exception {
	private static final long serialVersionUID = -7722810065917141193L;

	/**
	 * Role Not Found exception constructor by a given message
	 * @param message to create the role not found exception with
	 */
	public RoleNotFoundException(String message) {
		super(message);
	}
}
