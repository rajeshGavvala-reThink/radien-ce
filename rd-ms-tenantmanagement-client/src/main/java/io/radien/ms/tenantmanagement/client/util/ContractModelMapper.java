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
package io.radien.ms.tenantmanagement.client.util;

import io.radien.api.entity.Page;
import io.radien.ms.tenantmanagement.client.services.ContractFactory;
import io.radien.ms.tenantmanagement.client.entities.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.InputStream;
import java.text.ParseException;
import java.util.List;

/**
 * Mapper from a given information into a JSON or a Contract
 * @author Santana
 */
public class ContractModelMapper {
    protected static final Logger log = LoggerFactory.getLogger(ContractModelMapper.class);

    /**
     * Maps into a Json Object a Contract
     * @param model role that has the information to be converted
     * @return a json object created based the contract
     */
    public static JsonObject map(Contract model) {
        return ContractFactory.convertToJsonObject(model);
    }

    public static JsonArray map(List<? extends Contract> models) {
        final JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        models.forEach(model -> {
            JsonObject jsonObject = map(model);
            arrayBuilder.add(jsonObject);
        });
        return arrayBuilder.build();
    }

    /**
     * Creates a contract based a received inputted information
     * @param is inputted information to be converted into the object
     * @return a contract object based in the received information
     */
    public static Contract map(InputStream is) throws ParseException {
        try(JsonReader jsonReader = Json.createReader(is)) {
            JsonObject jsonObject = jsonReader.readObject();

            return ContractFactory.convert(jsonObject);
        }
    }

    /**
     * Creates a contract based a received inputted information
     * @param is inputted information to be converted into the object
     * @return a page with contract based in the received information
     */
    public static Page<Contract> mapToPage(InputStream is) {
        Page<Contract> page = null;
        try(JsonReader jsonReader = Json.createReader(is)) {
            JsonObject jsonObject = jsonReader.readObject();

            page = ContractFactory.convertJsonToPage(jsonObject);
        } catch (ParseException e) {
            log.error(e.getMessage(),e);
        }

        return page;
    }
}
