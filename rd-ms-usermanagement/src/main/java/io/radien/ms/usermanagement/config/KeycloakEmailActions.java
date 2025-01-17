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
package io.radien.ms.usermanagement.config;

/**
 * Keycloak type of emails enumeration
 *
 * @author Nuno Santana
 */
public enum KeycloakEmailActions  {

    TOTP("CONFIGURE_TOTP"),
    TERMS("TERMS_AND_CONDITIONS") ,
    UPDATE_PASSWORD("UPDATE_PASSWORD") ,
    UPDATE_PROFILE("UPDATE_PROFILE") ,
    VERIFY_EMAIL("VERIFY_EMAIL");

    private String propKey;

    /**
     * Keycloak email action constructor
     * @param propKey to be used
     */
    KeycloakEmailActions(String propKey){
        this.propKey = propKey;
    }

    /**
     * Selected email action property key getter
     * @return property key
     */
    public String propKey() {
        return propKey;
    }

}
