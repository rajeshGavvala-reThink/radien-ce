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
package io.radien.api.service.tenantrole;

import io.radien.api.entity.Page;
import io.radien.api.model.tenantrole.SystemTenantRolePermission;
import io.radien.exception.SystemException;

/**
 * Rest service client responsible to Deal with TenantRole Permission endpoint
 *
 * @author Newton Carvalho
 */
public interface TenantRolePermissionRESTServiceAccess {

    /**
     * Under a pagination approach, retrieves the Permissions associations that currently exist
     * @param tenantId tenant identifier for a TenantRole (Acting as filter)
     * @param roleId role identifier for a TenantRole (Acting as filter)
     * @param pageNo page number
     * @param pageSize page size
     * @return Page containing TenantRoleUser instances
     * @throws SystemException in case of any error
     */
    Page<? extends SystemTenantRolePermission> getPermissions(Long tenantId, Long roleId, int pageNo, int pageSize) throws SystemException;

    /**
     * Assign/associate/add permission to a TenantRole domain
     * The association will always be under a specific role
     * @param tenantRolePermission association between Tenant, Role and Permission
     * @return Boolean indicating if operation was concluded successfully
     * @throws SystemException in case of any error
     */
    Boolean assignPermission(SystemTenantRolePermission tenantRolePermission) throws SystemException;

    /**
     * (Un)Assign/Dissociate/remove permission from a TenantRole domain
     * @param tenantId Tenant identifier (Mandatory)
     * @param roleId Role identifier (Mandatory)
     * @param permissionId Permission identifier (Mandatory)
     * @return Boolean indicating if operation was concluded successfully
     * @throws SystemException in case of any error
     */
    Boolean unAssignPermission(Long tenantId, Long roleId, Long permissionId) throws SystemException;

    /**
     * (Un)Assign/Dissociate/remove permission from a TenantRole domain
     * Simply deletes a TenantRolePermission that eventually exists
     * @param tenantRolePermissionId identifier that maps a TenantRolePermission entity
     * @throws SystemException in case of any error
     */
    Boolean unAssignPermission(Long tenantRolePermissionId) throws SystemException;
}
