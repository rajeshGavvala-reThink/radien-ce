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
package io.radien.kernel.eventbus.event;

import io.radien.api.Event;

/**
 * Initialization Application event constructor class
 *
 * @author Marco Weiland
 */
public class InitApplicationEvent extends AbstractSystemEvent implements Event {

	private static final long serialVersionUID = -994860112323284786L;

	/**
	 * Initializer Application empty constructor
	 */
	public InitApplicationEvent() {
		super();
	}

}
