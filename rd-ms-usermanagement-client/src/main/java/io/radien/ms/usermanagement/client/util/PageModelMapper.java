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

import io.radien.api.entity.Page;
import io.radien.ms.usermanagement.client.entities.User;
import io.radien.api.util.PageFactory;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.InputStream;

/**
 * User Management page mapper
 *
 * @author Marco Weiland 
 */
public class PageModelMapper {

    /**
     * Empty constructor
     */
    private PageModelMapper(){}

    /**
     * Mapper for a received input stream into a page of possible users
     * @param is to be mapped
     * @return a page of user information
     */
    public static Page<?> map(InputStream is) {
        try(JsonReader jsonReader = Json.createReader(is)) {
            JsonObject jsonObject = jsonReader.readObject();

            return PageFactory.convertToPageObject(jsonObject);
        }
    }
}
