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
package io.radien.ms.rolemanagement.services;

import io.radien.api.SystemVariables;
import io.radien.api.entity.Page;
import io.radien.api.model.role.SystemRole;
import io.radien.api.model.tenant.SystemTenant;
import io.radien.api.model.tenantrole.SystemTenantRole;
import io.radien.api.model.tenantrole.SystemTenantRolePermission;
import io.radien.api.model.tenantrole.SystemTenantRoleSearchFilter;
import io.radien.api.model.tenantrole.SystemTenantRoleUser;
import io.radien.api.service.role.RoleServiceAccess;
import io.radien.api.service.tenantrole.TenantRolePermissionServiceAccess;
import io.radien.api.service.tenantrole.TenantRoleServiceAccess;
import io.radien.api.service.tenantrole.TenantRoleUserServiceAccess;
import io.radien.exception.GenericErrorCodeMessage;
import io.radien.exception.TenantRoleException;
import io.radien.exception.UniquenessConstraintException;
import io.radien.ms.rolemanagement.client.entities.TenantRoleSearchFilter;
import io.radien.ms.rolemanagement.entities.RoleEntity;
import io.radien.ms.rolemanagement.entities.TenantRoleEntity;
import io.radien.ms.rolemanagement.entities.TenantRolePermissionEntity;
import io.radien.ms.rolemanagement.entities.TenantRoleUserEntity;
import java.util.ArrayList;
import org.junit.jupiter.api.*;

import javax.ejb.EJBException;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tenant Role Service test
 * {@link io.radien.ms.rolemanagement.services.TenantRoleService}
 *
 * @author Newton Carvalho
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TenantRoleServiceTest {

    static Properties p;
    static TenantRoleServiceAccess tenantRoleServiceAccess;
    static TenantRolePermissionServiceAccess tenantRolePermissionServiceAccess;
    static TenantRoleUserServiceAccess tenantRoleUserServiceAccess;
    static RoleServiceAccess roleServiceAccess;

    static SystemTenant rootTenant = null;
    static EJBContainer container;
    Long baseRoleId = 111L;
    Long baseTenantId = 222L;

    /**
     * Method before test preparation
     * @throws Exception in case of injection naming exception
     */
    @BeforeAll
    public static void start() throws Exception {
        p = new Properties();
        p.put("appframeDatabase", "new://Resource?type=DataSource");
        p.put("appframeDatabase.JdbcDriver", "org.hsqldb.jdbcDriver");
        p.put("appframeDatabase.JdbcUrl", "jdbc:hsqldb:mem:radienTest");
        p.put("appframeDatabase.userName", "sa");
        p.put("appframeDatabase.password", "");
        p.put("openejb.deployments.classpath.include",".*role.*");
        p.put("openejb.deployments.classpath.exclude",".*client.*");
        p.put("openejb.cdi.activated-on-ejb", "false");
        container = EJBContainer.createEJBContainer(p);
        final Context context = container.getContext();

        String lookupString = "java:global/rd-ms-rolemanagement//TenantRolePermissionService";
        tenantRolePermissionServiceAccess = (TenantRolePermissionServiceAccess) context.lookup(lookupString);

        lookupString = "java:global/rd-ms-rolemanagement//TenantRoleUserService";
        tenantRoleUserServiceAccess = (TenantRoleUserServiceAccess) context.lookup(lookupString);

        lookupString = "java:global/rd-ms-rolemanagement//RoleService";
        roleServiceAccess = (RoleServiceAccess) context.lookup(lookupString);
    }

    /**
     * Injection method
     * @throws NamingException in case of injection naming exception
     */
    @BeforeEach
    public void inject() throws NamingException {
        String lookupString = "java:global/rd-ms-rolemanagement//TenantRoleService";
        tenantRoleServiceAccess = (TenantRoleServiceAccess) container.getContext().lookup(lookupString);
        container.getContext().bind("inject", this);
    }

    /**
     * Method to stop the container after the testing classes have perform
     */
    @AfterAll
    public static void stop() {
        if (container != null) {
            container.close();
        }
    }

    /**
     * Add tenant role test.
     * Will create the new tenant role.
     * Expected result: Success.
     * Tested methods: void save(TenantRole tenantRole)     *
     * @throws UniquenessConstraintException in case of requested action is not well constructed
     */
    @Order(1)
    @Test
    public void testSave() throws UniquenessConstraintException {

        SystemTenantRole systemTenantRole = new TenantRoleEntity();
        systemTenantRole.setRoleId(baseRoleId);
        systemTenantRole.setTenantId(baseTenantId);

        this.tenantRoleServiceAccess.save(systemTenantRole);
        Assertions.assertNotNull(systemTenantRole.getId());
    }

    /**
     * Try to Add a tenant role with repeated information
     * Will not create the new tenant role.
     * Expected result: Fail. UniquenessConstraintException
     * Tested methods: void save(TenantRole tenantRole)     *
     */
    @Order(2)
    @Test
    public void testSaveDuplicatedWithError() {
        SystemTenantRole systemTenantRole = new TenantRoleEntity();
        systemTenantRole.setRoleId(baseRoleId);
        systemTenantRole.setTenantId(baseTenantId);
        Assertions.assertThrows(UniquenessConstraintException.class, () ->
                this.tenantRoleServiceAccess.save(systemTenantRole));
    }

    /**
     * Try to create a Tenant Role and then update it
     * Expected result: Success.
     * Tested methods: void save(TenantRole tenantRole)
     */
    @Test
    @Order(3)
    public void testUpdate() throws UniquenessConstraintException {
        Long originalRoleId = 22L, originalTenantId = 23L;

        SystemTenantRole systemTenantRole = new TenantRoleEntity();
        systemTenantRole.setRoleId(originalRoleId);
        systemTenantRole.setTenantId(originalTenantId);
        this.tenantRoleServiceAccess.save(systemTenantRole);

        Long generatedId = systemTenantRole.getId();
        Assertions.assertNotNull(generatedId);

        SystemTenantRole retrieved = this.tenantRoleServiceAccess.get(generatedId);
        Assertions.assertNotNull(retrieved);
        assertEquals(retrieved.getRoleId(), originalRoleId);
        assertEquals(retrieved.getTenantId(), originalTenantId);

        Long newRoleId = 44L, newTenantId = 45L;
        SystemTenantRole systemTenantRole1 = new TenantRoleEntity();
        systemTenantRole1.setId(generatedId);
        systemTenantRole1.setRoleId(newRoleId);
        systemTenantRole1.setTenantId(newTenantId);
        this.tenantRoleServiceAccess.save(systemTenantRole1);

        retrieved = this.tenantRoleServiceAccess.get(generatedId);
        Assertions.assertNotNull(retrieved.getId());
        assertEquals(retrieved.getRoleId(), newRoleId);
        assertEquals(retrieved.getTenantId(), newTenantId);
    }

    /**
     * Test updating a TenantRole with repeated information (The set Tenant + Role is unique)
     * Expected: Fail (Raising UniquenessConstraintException)
     * Tested methods: void save(TenantRole tenantRole)
     */
    @Test
    @Order(4)
    public void testUpdateWithError() {
        SystemTenantRole tenantRole = new TenantRoleEntity();
        tenantRole.setTenantId(101L);
        tenantRole.setRoleId(102L);
        Assertions.assertDoesNotThrow(() -> this.tenantRoleServiceAccess.save(tenantRole));

        SystemTenantRole updatableTenantRole = new TenantRoleEntity();
        updatableTenantRole.setId(tenantRole.getId());
        updatableTenantRole.setRoleId(baseRoleId);
        updatableTenantRole.setTenantId(baseTenantId);

        Assertions.assertThrows(UniquenessConstraintException.class,() -> this.tenantRoleServiceAccess.save(updatableTenantRole));
    }

    /**
     * Test retrieving a TenantRole by Id
     * Tested methods: SystemTenantRole get(Long id)
     */
    @Test
    @Order(5)
    public void testGetById() {

        SystemTenantRole systemTenantRole = new TenantRoleEntity();
        systemTenantRole.setTenantId(1L);
        systemTenantRole.setRoleId(1L);
        Assertions.assertDoesNotThrow(() -> tenantRoleServiceAccess.save(systemTenantRole));

        SystemTenantRole retrieved = tenantRoleServiceAccess.get(systemTenantRole.getId());
        Assertions.assertNotNull(retrieved);
    }

    /**
     * Test retrieving a TenantRole by an Id that does not exist
     * Expected: Success
     * Tested methods: SystemTenantRole get(Long id)
     */
    @Test
    @Order(6)
    public void testGetByIdNotExistent() {
        SystemTenantRole systemTenantRole = tenantRoleServiceAccess.get(999999L);
        Assertions.assertNull(systemTenantRole);
    }

    /**
     * Test checking if an association (Tenant + role) exists
     * Expected: SUCCESS
     */
    @Test
    @Order(7)
    public void testAssociationExists() {
        Assertions.assertTrue(this.tenantRoleServiceAccess.isAssociationAlreadyExistent(baseRoleId, baseTenantId));
    }

    /**
     * Test checking if an association (Tenant + role) exists
     * Expected: FAIL
     */
    @Test
    @Order(8)
    public void testAssociationNotExists() {
        Assertions.assertFalse(this.tenantRoleServiceAccess.isAssociationAlreadyExistent(baseRoleId, 88L));
        Assertions.assertFalse(this.tenantRoleServiceAccess.isAssociationAlreadyExistent(9L, 88L));
    }

    /**
     * Test checking if an association (Tenant + role) exists,
     * but omitting mandatory param Tenant Id
     * Expected: FAIL
     */
    @Test
    @Order(9)
    public void testAssociationWithoutInformingTenant() {
        EJBException ejbException = Assertions.assertThrows(EJBException.class,
                ()->tenantRoleServiceAccess.isAssociationAlreadyExistent(baseRoleId, null));
        Assertions.assertTrue(ejbException.getCausedByException() instanceof IllegalArgumentException);
        Assertions.assertTrue(ejbException.getCausedByException().getMessage().contains(GenericErrorCodeMessage.TENANT_ROLE_FIELD_MANDATORY.toString(SystemVariables.TENANT_ID.getLabel())));
    }

    /**
     * Test checking if an association (Tenant + role) exists,
     * but omitting mandatory param Role Id
     * Expected: FAIL
     */
    @Test
    @Order(10)
    public void testAssociationWithoutInformingRole() {
        EJBException ejbException = Assertions.assertThrows(EJBException.class,
                ()->tenantRoleServiceAccess.isAssociationAlreadyExistent(null, baseTenantId));
        Assertions.assertTrue(ejbException.getCausedByException() instanceof IllegalArgumentException);
        Assertions.assertTrue(ejbException.getCausedByException().getMessage().contains(GenericErrorCodeMessage.TENANT_ROLE_FIELD_MANDATORY.toString(SystemVariables.ROLE_ID.getLabel())));
    }

    /**
     * Try to delete an existent TenantRole
     * Expected: SUCCESS
     */
    @Test
    @Order(11)
    public void testDeleteTenantRole() {
        // Insert first
        SystemTenantRole tenantRole = new TenantRoleEntity();
        tenantRole.setRoleId(69L);
        tenantRole.setTenantId(70L);
        Assertions.assertDoesNotThrow(() -> tenantRoleServiceAccess.save(tenantRole));

        Long id = tenantRole.getId();

        // Try to delete
        Assertions.assertDoesNotThrow(() -> Assertions.assertTrue(tenantRoleServiceAccess.delete(id)));

        // Try to retrieve to confirm
        SystemTenantRole str = tenantRoleServiceAccess.get(id);
        Assertions.assertNull(str);
    }

    /**
     * Try to delete without informing Id
     * Expected: FAIL (Raise Exception)
     */
    @Test
    @Order(12)
    public void testDeleteWithoutInformingId() {
        EJBException e = Assertions.assertThrows(EJBException.class, () -> tenantRoleServiceAccess.delete(null));
        Assertions.assertTrue(e.getCausedByException() instanceof IllegalArgumentException);
    }

    /**
     * Try to delete an TenantRole that has an User (TenantRoleUser) associated
     * Expected: FAIL (Raising TenantRoleException)
     */
    @Test
    @Order(13)
    public void testDeleteWithUserAssociated() {
        // Step 1: Create a Tenant Role
        SystemTenantRole tenantRole = new TenantRoleEntity();
        tenantRole.setTenantId(120L);
        tenantRole.setRoleId(121L);
        Assertions.assertDoesNotThrow(() -> tenantRoleServiceAccess.save(tenantRole));

        // Step 2: Create a Tenant Role User
        SystemTenantRoleUser tenantRoleUser = new TenantRoleUserEntity();
        tenantRoleUser.setTenantRoleId(tenantRole.getId());
        tenantRoleUser.setUserId(11111L);
        Assertions.assertDoesNotThrow(() -> tenantRoleUserServiceAccess.create(tenantRoleUser));

        // Step 3: Try to delete the Tenant Role
        Assertions.assertThrows(TenantRoleException.class, () -> tenantRoleServiceAccess.delete(tenantRole.getId()));
    }

    /**
     * Try to delete an TenantRole that has a Permission (TenantRolePermission) associated
     * Expected: FAIL (Raising TenantRoleException)
     */
    @Test
    @Order(14)
    public void testDeleteWithPermissionAssociated() {
        // Step 1: Create a Tenant Role
        SystemTenantRole tenantRole = new TenantRoleEntity();
        tenantRole.setTenantId(120L);
        tenantRole.setRoleId(122L);
        Assertions.assertDoesNotThrow(() -> tenantRoleServiceAccess.save(tenantRole));

        // Step 2: Create a Tenant Role Permission
        SystemTenantRolePermission tenantRolePermission = new TenantRolePermissionEntity();
        tenantRolePermission.setTenantRoleId(tenantRole.getId());
        tenantRolePermission.setPermissionId(11111L);
        Assertions.assertDoesNotThrow(() -> tenantRolePermissionServiceAccess.create(tenantRolePermission));

        // Step 3: Try to delete the Tenant Role
        Assertions.assertThrows(TenantRoleException.class, () -> tenantRoleServiceAccess.delete(tenantRole.getId()));
    }

    /**
     * Retrieves all TenantRoles (inserted during this test) under a pagination approach
     * Expected: SUCCESS (A page not empty)
     */
    @Test
    @Order(15)
    public void testPagination() {
        Page<SystemTenantRole> p = tenantRoleServiceAccess.getAll(1, 100);
        Assertions.assertNotNull(p);
        Assertions.assertTrue(p.getTotalResults() > 0);
        assertEquals(1, p.getTotalPages());
        Assertions.assertNotNull(p.getResults());
        Assertions.assertFalse(p.getResults().isEmpty());
    }

    /**
     * Retrieve TenantRole association under filter approach.
     * For this case, applies (as parameters) the tenant id and the role id already used to
     * create the first TenantRole association
     * Expected: SUCCESS
     */
    @Test
    @Order(16)
    public void testRetrieveTheFirstAssociationUsingFilter() {
        SystemTenantRoleSearchFilter filter = new TenantRoleSearchFilter(baseTenantId, baseRoleId,
                true, true);
        List<? extends SystemTenantRole> list = tenantRoleServiceAccess.get(filter);
        Assertions.assertNotNull(list);
        assertEquals(1, list.size());
    }

    /**
     * Testing retrieval setting isLogicalConjunction to false (Performing OR instead AND)
     * Expected: Success
     */
    @Test
    @Order(17)
    public void testRetrieveSettingLogicConjunctionToFalse() {
        SystemTenantRole tenantRole = new TenantRoleEntity();
        tenantRole.setRoleId(404L);
        tenantRole.setTenantId(405L);
        Assertions.assertDoesNotThrow(() -> tenantRoleServiceAccess.save(tenantRole));

        SystemTenantRole tenantRole2 = new TenantRoleEntity();
        tenantRole2.setRoleId(406L);
        tenantRole2.setTenantId(407L);
        Assertions.assertDoesNotThrow(() -> tenantRoleServiceAccess.save(tenantRole2));

        // Using OR
        SystemTenantRoleSearchFilter filter = new TenantRoleSearchFilter(405L, 406L,
                true, false);
        List<? extends SystemTenantRole> list = tenantRoleServiceAccess.get(filter);
        Assertions.assertNotNull(list);
        assertEquals(2, list.size());
    }

    /**
     * Filtering by parameters that does not exists
     * Expected: Fail (Empty collection)
     */
    @Test
    @Order(18)
    public void testFilterUsingInvalidParameters() {
        SystemTenantRoleSearchFilter filter = new TenantRoleSearchFilter(1234L, 4321L,
                true, true);
        List<? extends SystemTenantRole> list = tenantRoleServiceAccess.get(filter);
        Assertions.assertNotNull(list);
        Assertions.assertTrue(list.isEmpty());

        filter = new TenantRoleSearchFilter(1234L, 4321L,
                true, false);
        list = tenantRoleServiceAccess.get(filter);
        Assertions.assertNotNull(list);
        Assertions.assertTrue(list.isEmpty());
    }

    /**
     * Tests method getTenantRoleId(Long tenant, Long role)
     */
    @Test
    @Order(19)
    public void testGetTenantRoleId() {
        SystemTenantRole str = new TenantRoleEntity();
        str.setTenantId(10000L);
        str.setRoleId(10001L);
        Assertions.assertDoesNotThrow(() -> this.tenantRoleServiceAccess.save(str));

        Long expectedId = str.getId();
        Assertions.assertNotNull(expectedId);

        Optional<Long> id = this.tenantRoleServiceAccess.getTenantRoleId(10000L, 10001L);
        Assertions.assertTrue(id.isPresent());
        assertEquals(expectedId, id.get());

        id = this.tenantRoleServiceAccess.getTenantRoleId(101010L, 202L);
        Assertions.assertFalse(id.isPresent());
    }

    /**
     * Tests for method hasAnyRole(Long userId, List<String> roles, tenantId)
     */
    @Test
    @Order(20)
    public void testHasAnyRole() {
        SystemRole roleA = new RoleEntity();
        roleA.setName("role-a");
        Assertions.assertDoesNotThrow(() -> this.roleServiceAccess.save(roleA));

        SystemRole roleB = new RoleEntity();
        roleB.setName("role-b");
        Assertions.assertDoesNotThrow(() -> this.roleServiceAccess.save(roleB));

        SystemRole roleC = new RoleEntity();
        roleC.setName("role-c");
        Assertions.assertDoesNotThrow(() -> this.roleServiceAccess.save(roleC));

        Long tenant1 = 444L;
        Long tenant2 = 445L;

        SystemTenantRole tenant1RoleA = new TenantRoleEntity();
        tenant1RoleA.setTenantId(tenant1); tenant1RoleA.setRoleId(roleA.getId());
        Assertions.assertDoesNotThrow(() -> tenantRoleServiceAccess.save(tenant1RoleA));

        SystemTenantRole tenant1RoleB = new TenantRoleEntity();
        tenant1RoleB.setTenantId(tenant1); tenant1RoleB.setRoleId(roleB.getId());
        Assertions.assertDoesNotThrow(() -> tenantRoleServiceAccess.save(tenant1RoleB));

        SystemTenantRole tenant2RoleC = new TenantRoleEntity();
        tenant2RoleC.setTenantId(tenant2); tenant2RoleC.setRoleId(roleC.getId());
        Assertions.assertDoesNotThrow(() -> tenantRoleServiceAccess.save(tenant2RoleC));

        Long user1 = 100000000L;
        Long user2 = 100000011L;

        SystemTenantRoleUser tenant1RoleAUser1 = new TenantRoleUserEntity();
        tenant1RoleAUser1.setUserId(user1); tenant1RoleAUser1.setTenantRoleId(tenant1RoleA.getId());
        Assertions.assertDoesNotThrow(() -> tenantRoleUserServiceAccess.create(tenant1RoleAUser1));

        SystemTenantRoleUser tenant1RoleBUser1 = new TenantRoleUserEntity();
        tenant1RoleBUser1.setUserId(user1); tenant1RoleBUser1.setTenantRoleId(tenant1RoleB.getId());
        Assertions.assertDoesNotThrow(() -> tenantRoleUserServiceAccess.create(tenant1RoleBUser1));

        SystemTenantRoleUser tenant2RoleCUser2 = new TenantRoleUserEntity();
        tenant2RoleCUser2.setUserId(user2); tenant2RoleCUser2.setTenantRoleId(tenant2RoleC.getId());
        Assertions.assertDoesNotThrow(() -> tenantRoleUserServiceAccess.create(tenant2RoleCUser2));

        // time for the truth
        Assertions.assertTrue(this.tenantRoleServiceAccess.
                hasAnyRole(user1, Arrays.asList("role-a", "role-b"), null));

        Assertions.assertTrue(this.tenantRoleServiceAccess.
                hasAnyRole(user1, Arrays.asList("role-a", "role-c"), null));

        Assertions.assertTrue(this.tenantRoleServiceAccess.
                hasAnyRole(user1, Arrays.asList("role-a", "role-c"), tenant1));

        Assertions.assertTrue(this.tenantRoleServiceAccess.
                hasAnyRole(user1, Arrays.asList("role-a", "role-b", "role-c"), tenant1));

        Assertions.assertFalse(this.tenantRoleServiceAccess.
                hasAnyRole(user1, Arrays.asList("role-a", "role-c"), tenant2));

        Assertions.assertFalse(this.tenantRoleServiceAccess.
                hasAnyRole(user2, Arrays.asList("role-a", "role-b", "role-c"), tenant1));

        Assertions.assertTrue(this.tenantRoleServiceAccess.
                hasAnyRole(user2, Arrays.asList("role-a", "role-b", "role-c"), tenant2));

        try {
            this.tenantRoleServiceAccess.
                    hasAnyRole(null, Arrays.asList("role-a", "role-b"), null);
        } catch(Exception e) {
            Assertions.assertTrue(e.getMessage().contains(GenericErrorCodeMessage.TENANT_ROLE_FIELD_MANDATORY.toString(SystemVariables.USER_ID.getLabel())));
        }
    }

    /**
     * Tests for method hasPermission(Long userId, Long permissionId, Long tenantId)
     */
    @Test
    @Order(21)
    public void testHasPermission() {

        /** Roles */
        SystemRole roleC = new RoleEntity();
        roleC.setName("role-c1");
        Assertions.assertDoesNotThrow(() -> this.roleServiceAccess.save(roleC));

        SystemRole roleD = new RoleEntity();
        roleD.setName("role-d1");
        Assertions.assertDoesNotThrow(() -> this.roleServiceAccess.save(roleD));

        Long tenant1 = 888L;
        Long tenant2 = 889L;

        /** Tenant Roles */
        SystemTenantRole tenant1RoleC = new TenantRoleEntity();
        tenant1RoleC.setTenantId(tenant1); tenant1RoleC.setRoleId(roleC.getId());
        Assertions.assertDoesNotThrow(() -> tenantRoleServiceAccess.save(tenant1RoleC));

        SystemTenantRole tenant2RoleD = new TenantRoleEntity();
        tenant2RoleD.setTenantId(tenant2); tenant2RoleD.setRoleId(roleD.getId());
        Assertions.assertDoesNotThrow(() -> tenantRoleServiceAccess.save(tenant2RoleD));

        Long permission1 = 100001L;
        Long permission2 = 100002L;
        Long permission3 = 100003L;

        /** Tenant Role Permission */
        SystemTenantRolePermission tenant1RoleCPermission1 = new TenantRolePermissionEntity();
        tenant1RoleCPermission1.setTenantRoleId(tenant1RoleC.getId());
        tenant1RoleCPermission1.setPermissionId(permission1);
        Assertions.assertDoesNotThrow(() -> tenantRolePermissionServiceAccess.create(tenant1RoleCPermission1));

        SystemTenantRolePermission tenant1RoleCPermission2 = new TenantRolePermissionEntity();
        tenant1RoleCPermission2.setTenantRoleId(tenant1RoleC.getId());
        tenant1RoleCPermission2.setPermissionId(permission2);
        Assertions.assertDoesNotThrow(() -> tenantRolePermissionServiceAccess.create(tenant1RoleCPermission2));

        SystemTenantRolePermission tenant2RoleDPermission2 = new TenantRolePermissionEntity();
        tenant2RoleDPermission2.setTenantRoleId(tenant2RoleD.getId());
        tenant2RoleDPermission2.setPermissionId(permission2);
        Assertions.assertDoesNotThrow(() -> tenantRolePermissionServiceAccess.create(tenant2RoleDPermission2));

        SystemTenantRolePermission tenant2RoleDPermission3 = new TenantRolePermissionEntity();
        tenant2RoleDPermission3.setTenantRoleId(tenant2RoleD.getId());
        tenant2RoleDPermission3.setPermissionId(permission3);
        Assertions.assertDoesNotThrow(() -> tenantRolePermissionServiceAccess.create(tenant2RoleDPermission3));

        /** Tenant Role Users */

        Long user1 = 10002222L;
        Long user2 = 10002223L;

        SystemTenantRoleUser tenant1RoleCUser1 = new TenantRoleUserEntity();
        tenant1RoleCUser1.setUserId(user1); tenant1RoleCUser1.setTenantRoleId(tenant1RoleC.getId());
        Assertions.assertDoesNotThrow(() -> tenantRoleUserServiceAccess.create(tenant1RoleCUser1));

        SystemTenantRoleUser tenant2RoleDUser2 = new TenantRoleUserEntity();
        tenant2RoleDUser2.setUserId(user2); tenant2RoleDUser2.setTenantRoleId(tenant2RoleD.getId());
        Assertions.assertDoesNotThrow(() -> tenantRoleUserServiceAccess.create(tenant2RoleDUser2));

        /** Checking permission */
        Assertions.assertTrue(tenantRoleServiceAccess.hasPermission(user1, permission1, null));
        Assertions.assertFalse(tenantRoleServiceAccess.hasPermission(user1, permission3, null));
        Assertions.assertTrue(tenantRoleServiceAccess.hasPermission(user1, permission1, tenant1));
        Assertions.assertFalse(tenantRoleServiceAccess.hasPermission(user1, permission1, tenant2));
        Assertions.assertTrue(tenantRoleServiceAccess.hasPermission(user1, permission2, null));
        Assertions.assertFalse(tenantRoleServiceAccess.hasPermission(user1, permission2, tenant2));
    }

    /**
     * Tests for method getTenants(Long userId, Long roleId)
     */
    @Test
    @Order(22)
    public void testGetTenants() {
        SystemRole roleD = new RoleEntity();
        roleD.setName("role-d");
        Assertions.assertDoesNotThrow(() -> this.roleServiceAccess.save(roleD));

        SystemRole roleE = new RoleEntity();
        roleE.setName("role-e");
        Assertions.assertDoesNotThrow(() -> this.roleServiceAccess.save(roleE));

        SystemRole roleF = new RoleEntity();
        roleF.setName("role-f");
        Assertions.assertDoesNotThrow(() -> this.roleServiceAccess.save(roleF));

        Long tenant1 = 444L;
        Long tenant2 = 445L;
        Long tenant3 = 446L;

        SystemTenantRole tenant1RoleD = new TenantRoleEntity();
        tenant1RoleD.setTenantId(tenant1); tenant1RoleD.setRoleId(roleD.getId());
        Assertions.assertDoesNotThrow(() -> tenantRoleServiceAccess.save(tenant1RoleD));

        SystemTenantRole tenant1RoleE = new TenantRoleEntity();
        tenant1RoleE.setTenantId(tenant1); tenant1RoleE.setRoleId(roleE.getId());
        Assertions.assertDoesNotThrow(() -> tenantRoleServiceAccess.save(tenant1RoleE));

        SystemTenantRole tenant2RoleD = new TenantRoleEntity();
        tenant2RoleD.setTenantId(tenant2); tenant2RoleD.setRoleId(roleD.getId());
        Assertions.assertDoesNotThrow(() -> tenantRoleServiceAccess.save(tenant2RoleD));

        SystemTenantRole tenant2RoleF = new TenantRoleEntity();
        tenant2RoleF.setTenantId(tenant2); tenant2RoleF.setRoleId(roleF.getId());
        Assertions.assertDoesNotThrow(() -> tenantRoleServiceAccess.save(tenant2RoleF));

        SystemTenantRole tenant3RoleF = new TenantRoleEntity();
        tenant3RoleF.setTenantId(tenant3); tenant3RoleF.setRoleId(roleF.getId());
        Assertions.assertDoesNotThrow(() -> tenantRoleServiceAccess.save(tenant3RoleF));

        Long user1 = 100000000L;
        Long user2 = 100000011L;

        SystemTenantRoleUser tenant1RoleDUser1 = new TenantRoleUserEntity();
        tenant1RoleDUser1.setUserId(user1); tenant1RoleDUser1.setTenantRoleId(tenant1RoleD.getId());
        Assertions.assertDoesNotThrow(() -> tenantRoleUserServiceAccess.create(tenant1RoleDUser1));

        SystemTenantRoleUser tenant2RoleDUser1 = new TenantRoleUserEntity();
        tenant2RoleDUser1.setUserId(user1); tenant2RoleDUser1.setTenantRoleId(tenant2RoleD.getId());
        Assertions.assertDoesNotThrow(() -> tenantRoleUserServiceAccess.create(tenant2RoleDUser1));

        SystemTenantRoleUser tenant3RoleFUser1 = new TenantRoleUserEntity();
        tenant3RoleFUser1.setUserId(user1); tenant3RoleFUser1.setTenantRoleId(tenant3RoleF.getId());
        Assertions.assertDoesNotThrow(() -> tenantRoleUserServiceAccess.create(tenant3RoleFUser1));

        SystemTenantRoleUser tenant1RoleEUser2 = new TenantRoleUserEntity();
        tenant1RoleEUser2.setUserId(user2); tenant1RoleEUser2.setTenantRoleId(tenant1RoleE.getId());
        Assertions.assertDoesNotThrow(() -> tenantRoleUserServiceAccess.create(tenant1RoleEUser2));

        SystemTenantRoleUser tenant2RoleFUser2 = new TenantRoleUserEntity();
        tenant2RoleFUser2.setUserId(user2); tenant2RoleFUser2.setTenantRoleId(tenant2RoleF.getId());
        Assertions.assertDoesNotThrow(() -> tenantRoleUserServiceAccess.create(tenant2RoleFUser2));

        List<Long> ids = tenantRoleServiceAccess.getTenants(user1, null);
        Assertions.assertTrue(ids.contains(tenant1) && ids.contains(tenant2) && ids.contains(tenant3));

        ids = tenantRoleServiceAccess.getTenants(user1, roleD.getId());
        Assertions.assertTrue(ids.contains(tenant1) && ids.contains(tenant2));

        ids = tenantRoleServiceAccess.getTenants(user1, roleE.getId());
        Assertions.assertTrue(ids.isEmpty());

        ids = tenantRoleServiceAccess.getTenants(user2, null);
        Assertions.assertTrue(ids.contains(tenant1) && ids.contains(tenant2));

        ids = tenantRoleServiceAccess.getTenants(user2, roleD.getId());
        Assertions.assertTrue(ids.isEmpty());

        ids = tenantRoleServiceAccess.getTenants(user2, roleE.getId());
        Assertions.assertTrue(ids.contains(tenant1));

        ids = tenantRoleServiceAccess.getTenants(user2, roleF.getId());
        Assertions.assertTrue(ids.contains(tenant2));

        ids = tenantRoleServiceAccess.getTenants(99999999L, null);
        Assertions.assertTrue(ids.isEmpty());
    }

    /**
     * Tests for method getPermissions(Long tenantId, Long roleId, Long userId)
     */
    @Test
    @Order(23)
    public void testGetPermissions() {
        /** Roles */
        SystemRole roleC = new RoleEntity();
        roleC.setName("role-cc1");
        Assertions.assertDoesNotThrow(() -> this.roleServiceAccess.save(roleC));

        SystemRole roleD = new RoleEntity();
        roleD.setName("role-dd1");
        Assertions.assertDoesNotThrow(() -> this.roleServiceAccess.save(roleD));

        Long tenant1 = 888L;
        Long tenant2 = 889L;

        /** Tenant Roles */
        SystemTenantRole tenant1RoleC = new TenantRoleEntity();
        tenant1RoleC.setTenantId(tenant1); tenant1RoleC.setRoleId(roleC.getId());
        Assertions.assertDoesNotThrow(() -> tenantRoleServiceAccess.save(tenant1RoleC));

        SystemTenantRole tenant2RoleD = new TenantRoleEntity();
        tenant2RoleD.setTenantId(tenant2); tenant2RoleD.setRoleId(roleD.getId());
        Assertions.assertDoesNotThrow(() -> tenantRoleServiceAccess.save(tenant2RoleD));

        Long permission1 = 100001L;
        Long permission2 = 100002L;
        Long permission3 = 100003L;

        /** Tenant Role Permission */
        SystemTenantRolePermission tenant1RoleCPermission1 = new TenantRolePermissionEntity();
        tenant1RoleCPermission1.setTenantRoleId(tenant1RoleC.getId());
        tenant1RoleCPermission1.setPermissionId(permission1);
        Assertions.assertDoesNotThrow(() -> tenantRolePermissionServiceAccess.create(tenant1RoleCPermission1));

        SystemTenantRolePermission tenant1RoleCPermission2 = new TenantRolePermissionEntity();
        tenant1RoleCPermission2.setTenantRoleId(tenant1RoleC.getId());
        tenant1RoleCPermission2.setPermissionId(permission2);
        Assertions.assertDoesNotThrow(() -> tenantRolePermissionServiceAccess.create(tenant1RoleCPermission2));

        SystemTenantRolePermission tenant2RoleDPermission2 = new TenantRolePermissionEntity();
        tenant2RoleDPermission2.setTenantRoleId(tenant2RoleD.getId());
        tenant2RoleDPermission2.setPermissionId(permission2);
        Assertions.assertDoesNotThrow(() -> tenantRolePermissionServiceAccess.create(tenant2RoleDPermission2));

        SystemTenantRolePermission tenant2RoleDPermission3 = new TenantRolePermissionEntity();
        tenant2RoleDPermission3.setTenantRoleId(tenant2RoleD.getId());
        tenant2RoleDPermission3.setPermissionId(permission3);
        Assertions.assertDoesNotThrow(() -> tenantRolePermissionServiceAccess.create(tenant2RoleDPermission3));

        /** Tenant Role Users */

        Long user1 = 10002222L;
        Long user2 = 10002223L;

        SystemTenantRoleUser tenant1RoleCUser1 = new TenantRoleUserEntity();
        tenant1RoleCUser1.setUserId(user1); tenant1RoleCUser1.setTenantRoleId(tenant1RoleC.getId());
        Assertions.assertDoesNotThrow(() -> tenantRoleUserServiceAccess.create(tenant1RoleCUser1));

        SystemTenantRoleUser tenant2RoleDUser2 = new TenantRoleUserEntity();
        tenant2RoleDUser2.setUserId(user2); tenant2RoleDUser2.setTenantRoleId(tenant2RoleD.getId());
        Assertions.assertDoesNotThrow(() -> tenantRoleUserServiceAccess.create(tenant2RoleDUser2));

        /** Retrieving permissions */
        List<Long> permissionIds = tenantRoleServiceAccess.getPermissions(tenant1, roleC.getId(), null);
        Assertions.assertTrue(permissionIds.contains(permission1) && permissionIds.contains(permission2));

        permissionIds = tenantRoleServiceAccess.getPermissions(tenant1, roleC.getId(), user1);
        Assertions.assertTrue(permissionIds.contains(permission1) && permissionIds.contains(permission2));

        permissionIds = tenantRoleServiceAccess.getPermissions(tenant2, roleC.getId(), null);
        Assertions.assertTrue(permissionIds.isEmpty());

        permissionIds = tenantRoleServiceAccess.getPermissions(tenant1, roleC.getId(), user2);
        Assertions.assertTrue(permissionIds.isEmpty());

        permissionIds = tenantRoleServiceAccess.getPermissions(tenant2, roleD.getId(), null);
        Assertions.assertTrue(permissionIds.contains(permission2) && permissionIds.contains(permission3));

        permissionIds = tenantRoleServiceAccess.getPermissions(tenant2, roleD.getId(), user2);
        Assertions.assertTrue(permissionIds.contains(permission2) && permissionIds.contains(permission3));
    }

    /**
     * Tests for method getPermissions(Long tenantId, Long roleId, Long userId)
     * without informing the mandatory field tenant
     */
    @Test
    @Order(24)
    public void testGetPermissionsWithoutMandatoryFieldTenant() {
        EJBException e = Assertions.assertThrows(EJBException.class, () ->
                tenantRoleServiceAccess.getPermissions(null, 1L, null));
        Assertions.assertTrue(e.getCausedByException() instanceof IllegalArgumentException);
    }


    /**
     * Tests for method getPermissions(Long tenantId, Long roleId, Long userId)
     * without informing the mandatory field Role
     */
    @Test
    @Order(25)
    public void testGetPermissionsWithoutMandatoryFieldRole() {
        EJBException e = Assertions.assertThrows(EJBException.class, () ->
                tenantRoleServiceAccess.getPermissions(1L, null, null));
        Assertions.assertTrue(e.getCausedByException() instanceof IllegalArgumentException);
    }

    /**
     * Tests for method getTenants(Long userId, Long roleId)
     * without informing the mandatory field Role
     */
    @Test
    @Order(26)
    public void testGetTenantsWithoutMandatoryFieldUser() {
        EJBException e = Assertions.assertThrows(EJBException.class, () ->
                tenantRoleServiceAccess.getTenants(null, null));
        Assertions.assertTrue(e.getCausedByException() instanceof IllegalArgumentException);
    }

    /**
     * Tests for method getTenantRoleId(Long tenant, Long role)
     * without informing the mandatory field Role
     */
    @Test
    @Order(27)
    public void testGetTenantRoleIdWithoutMandatoryFields() {
        EJBException e = Assertions.assertThrows(EJBException.class, () ->
                tenantRoleServiceAccess.getTenantRoleId(null, null));
        Assertions.assertTrue(e.getCausedByException() instanceof IllegalArgumentException);
    }

    /**
     * Tests for method hasAnyRole(Long userId, List<String> roles, tenantId)
     */
    @Test
    @Order(28)
    public void testHasAnyRoleNullArrayList() {
        SystemRole roleA = new RoleEntity();
        roleA.setName("role-xx");
        Assertions.assertDoesNotThrow(() -> this.roleServiceAccess.save(roleA));

        Long tenant1 = 544L;

        SystemTenantRole tenant1RoleA = new TenantRoleEntity();
        tenant1RoleA.setTenantId(tenant1); tenant1RoleA.setRoleId(roleA.getId());
        Assertions.assertDoesNotThrow(() -> tenantRoleServiceAccess.save(tenant1RoleA));

        Long user1 = 105000000L;

        SystemTenantRoleUser tenant1RoleAUser1 = new TenantRoleUserEntity();
        tenant1RoleAUser1.setUserId(user1); tenant1RoleAUser1.setTenantRoleId(tenant1RoleA.getId());
        Assertions.assertDoesNotThrow(() -> tenantRoleUserServiceAccess.create(tenant1RoleAUser1));

        try {
            this.tenantRoleServiceAccess.
                    hasAnyRole(user1, Arrays.asList(), null);
        } catch(Exception e) {
            Assertions.assertTrue(e.getMessage().contains(GenericErrorCodeMessage.TENANT_ROLE_FIELD_MANDATORY.toString(SystemVariables.ROLE_NAME.getLabel())));
        }
    }

    /**
     * Test method for getRoles(Long userId, Long tenantId)
     */
    @Test
    public void testGetRoles() {
        SystemRole role1 = new RoleEntity();
        roleObject(role1, "role1");

        SystemRole role2 = new RoleEntity();
        roleObject(role2, "role2");

        SystemRole role3 = new RoleEntity();
        roleObject(role3, "role3");


        Long tenant1=444L, tenant2=445L, tenant3 = 446L;
        SystemTenantRole tenant1Role1 = new TenantRoleEntity();
        tenantRoleObject(tenant1Role1, tenant1, role1);

        SystemTenantRole tenant1Role2 = new TenantRoleEntity();
        tenantRoleObject(tenant1Role2, tenant1, role2);

        SystemTenantRole tenant2Role1 = new TenantRoleEntity();
        tenantRoleObject(tenant2Role1, tenant2, role1);

        SystemTenantRole tenant2Role3 = new TenantRoleEntity();
        tenantRoleObject(tenant2Role3, tenant2, role3);

        SystemTenantRole tenant3Role3 = new TenantRoleEntity();
        tenantRoleObject(tenant3Role3, tenant3, role3);


        Long user1 = 100000000L, user2 = 100000011L;
        SystemTenantRoleUser tenant1Role1User1 = new TenantRoleUserEntity();
        userTenantRoleObject(tenant1Role1User1,user1,tenant1Role1);

        SystemTenantRoleUser tenant1Role2User1 = new TenantRoleUserEntity();
        userTenantRoleObject(tenant1Role2User1,user1,tenant1Role2);

        SystemTenantRoleUser tenant2Role1User1 = new TenantRoleUserEntity();
        userTenantRoleObject(tenant2Role1User1,user1,tenant2Role1);

        SystemTenantRoleUser tenant3Role3User1 = new TenantRoleUserEntity();
        userTenantRoleObject(tenant3Role3User1,user1,tenant3Role3);

        SystemTenantRoleUser tenant1Role2User2 = new TenantRoleUserEntity();
        userTenantRoleObject(tenant1Role2User2,user2,tenant1Role2);

        SystemTenantRoleUser tenant2Role3User2 = new TenantRoleUserEntity();
        userTenantRoleObject(tenant2Role3User2,user2,tenant2Role3);


        List<Long> ids = tenantRoleServiceAccess.getRoleIdsForUserTenant(user1, tenant1);
        Assertions.assertTrue(ids.contains(role1.getId()) && ids.contains(role2.getId()));
        Assertions.assertEquals(5, ids.size());

        ids = tenantRoleServiceAccess.getRoleIdsForUserTenant(user2, tenant2);
        Assertions.assertTrue(ids.contains(role3.getId()));
        Assertions.assertEquals(3, ids.size());

        ids = tenantRoleServiceAccess.getRoleIdsForUserTenant(99999999L, tenant3);
        Assertions.assertTrue(ids.isEmpty());
    }

    /**
     * Test method getRoles(Long userId, Long tenantId)
     * without informing the mandatory field Role
     */
    @Test
    public void testGetRolesWithoutMandatoryFieldUser() {
        EJBException e = Assertions.assertThrows(EJBException.class, () ->
                tenantRoleServiceAccess.getRoleIdsForUserTenant(null, null));
        Assertions.assertTrue(e.getCausedByException() instanceof IllegalArgumentException);
    }

    /**
     * Test method getTenantRoleIds()
     * asserts List TenantRoleIds
     * @throws TenantRoleException
     */
    @Test
    public void testGetTenantRoleIds() throws TenantRoleException {
        SystemTenantRole str = new TenantRoleEntity();
        str.setTenantId(10000L);
        str.setRoleId(10000L);
        Assertions.assertDoesNotThrow(() -> tenantRoleServiceAccess.save(str));

        ArrayList<Long> roleIds = new ArrayList<Long>();
        roleIds.add(10000L);

        Long expectedId = str.getId();
        Assertions.assertNotNull(expectedId);

        List<Long> ids = tenantRoleServiceAccess.getTenantRoleIds(10000L, roleIds);
        Assertions.assertFalse(ids.isEmpty());
        assertEquals(expectedId, ids.get(0));
    }

    /**
     * This method construct role object
     * @param role reference role object
     * @param roleName Role name
     */
    private void roleObject(SystemRole role, String roleName){
        role.setName(roleName);
        Assertions.assertDoesNotThrow(() -> this.roleServiceAccess.save(role));
    }

    /**
     * This method construct tenantRole object
     * @param tenantRole reference tenantRole object
     * @param tenantId Tenant Identifier
     * @param role reference role object
     */
    private void tenantRoleObject(SystemTenantRole tenantRole, Long tenantId, SystemRole role){
        tenantRole.setTenantId(tenantId); tenantRole.setRoleId(role.getId());
        Assertions.assertDoesNotThrow(() -> tenantRoleServiceAccess.save(tenantRole));
    }

    /**
     * This method construct systemTenantRoleUser object
     * @param systemTenantRoleUser reference systemTenantRoleUser object
     * @param userId User Identifier
     * @param tenantRole reference tenantRole object
     */
    private void userTenantRoleObject(SystemTenantRoleUser systemTenantRoleUser, Long userId, SystemTenantRole tenantRole){
        systemTenantRoleUser.setUserId(userId); systemTenantRoleUser.setTenantRoleId(tenantRole.getId());
        Assertions.assertDoesNotThrow(() -> tenantRoleUserServiceAccess.create(systemTenantRoleUser));
    }
}