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
package io.radien.api.model.permission;

import io.radien.api.Model;

/**
 * Contract description for Permission
 *
 * @author Newton Carvalho
 */
public interface SystemPermission extends Model {

    /**
     * System Permission name getter
     * @return the system permission name
     */
    String getName();

    /**
     * System Permission name setter
     * @param name to be set
     */
    void setName(String name);

    /**
     * System Permission action id getter
     * @return the system permission action id
     */
    Long getActionId();

    /**
     * System Permission action id setter
     * @param actionId to be set
     */
    void setActionId(Long actionId);

    /**
     * System Permission resource id getter
     * @return the system permission resource id
     */
    Long getResourceId();

    /**
     * System Permission resource id setter
     * @param resourceId to be set
     */
    void setResourceId(Long resourceId);
}
