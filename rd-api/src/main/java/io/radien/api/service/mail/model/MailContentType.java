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
package io.radien.api.service.mail.model;

/**
 * Type of the content email, if the email is going to be written in HTML format or plain text format
 *
 * @author Marco Weiland
 */
public enum MailContentType {
	HTML("html"), TEXT("plain");

	private String type;

	/**
	 * Mail content type constructor
	 * @param type of the email
	 */
	MailContentType(String type) {
		this.type = type;
	}

	/**
	 * Email type getter
	 * @return the email type
	 */
	public String type() {
		return type;
	}
}
