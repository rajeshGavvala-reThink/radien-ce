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

package io.radien.ms.usermanagement.client.util;

import io.radien.api.model.user.SystemUser;
import io.radien.ms.usermanagement.client.services.UserFactory;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;
import java.io.InputStream;
import java.util.List;

/**
 * User management mapper from a input stream into a list
 *
 * @author Bruno Gama
 */
public class ListUserModelMapper {

    /**
     * Mapper for a received input stream into a list of possible system users
     * @param is to be mapped
     * @return a list of system users information
     */
    public static List<? extends SystemUser> map(InputStream is) {
        try(JsonReader jsonReader = Json.createReader(is)) {
            JsonArray jsonArray = jsonReader.readArray();
            return UserFactory.convert(jsonArray);
        }
    }
}
