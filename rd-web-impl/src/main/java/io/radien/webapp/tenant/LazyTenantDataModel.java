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
package io.radien.webapp.tenant;

import io.radien.api.entity.Page;
import io.radien.api.model.tenant.SystemTenant;
import io.radien.api.service.tenant.TenantRESTServiceAccess;
import io.radien.exception.SystemException;
import io.radien.webapp.LazyAbstractDataModel;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author Bruno Gama
 */
public class LazyTenantDataModel extends LazyAbstractDataModel<SystemTenant> {

    private static final long serialVersionUID = -2487575100973554400L;
    private TenantRESTServiceAccess service;

    public LazyTenantDataModel(TenantRESTServiceAccess service) {
        this.service=service;
        this.datasource = new ArrayList<>();
    }

    @Override
    public Page<? extends SystemTenant> getData(int offset, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) throws SystemException {
        return service.getAll(null,(offset/pageSize) + 1, pageSize, null, false);
    }

}
