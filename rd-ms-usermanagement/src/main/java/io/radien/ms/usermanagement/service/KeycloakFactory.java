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
package io.radien.ms.usermanagement.service;

import io.radien.api.model.user.SystemUser;

import org.keycloak.representations.idm.UserRepresentation;
/**
 * Keycloak mapping and conversion from user information into user representation to be processed
 * by keycloak
 *
 * @author Nuno Santana
 */
public class KeycloakFactory {

    /**
     * Empty constructor
     */
    private KeycloakFactory(){}

    /**
     * Converter method that will translate/convert a system user information
     * into a user representation
     * @param user to be converted
     * @return the user representation for keycloak understanding
     */
    public static UserRepresentation convertToUserRepresentation(SystemUser user) {
        UserRepresentation result = new UserRepresentation();
        result.setFirstName(user.getFirstname());
        result.setLastName(user.getLastname());
        result.setUsername(user.getLogon());
        result.setEmail(user.getUserEmail());
        result.setEnabled(user.isEnabled());
        return result;
    }
}
