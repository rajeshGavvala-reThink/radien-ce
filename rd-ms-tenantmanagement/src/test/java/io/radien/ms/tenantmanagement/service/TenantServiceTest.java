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
package io.radien.ms.tenantmanagement.service;

import io.radien.api.entity.Page;
import io.radien.api.model.tenant.SystemTenant;
import io.radien.api.service.tenant.TenantServiceAccess;
import io.radien.exception.GenericErrorCodeMessage;
import io.radien.exception.NotFoundException;
import io.radien.exception.TenantException;
import io.radien.exception.UniquenessConstraintException;
import io.radien.ms.tenantmanagement.client.entities.TenantSearchFilter;
import io.radien.ms.tenantmanagement.client.entities.TenantType;
import io.radien.ms.tenantmanagement.entities.TenantEntity;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

/**
 * @author Nuno Santana
 * @author Bruno Gama
 */
public class TenantServiceTest {

    Properties p;
    TenantServiceAccess tenantServiceAccess;
    SystemTenant rootTenant;

    public TenantServiceTest() throws Exception {
        p = new Properties();
        p.put("appframeDatabase", "new://Resource?type=DataSource");
        p.put("appframeDatabase.JdbcDriver", "org.hsqldb.jdbcDriver");
        p.put("appframeDatabase.JdbcUrl", "jdbc:hsqldb:mem:radienTest");
        p.put("appframeDatabase.userName", "sa");
        p.put("appframeDatabase.password", "");
        p.put("openejb.deployments.classpath.include",".*tenant.*");
        p.put("openejb.deployments.classpath.exclude",".*client.*");


        final Context context = EJBContainer.createEJBContainer(p).getContext();

        tenantServiceAccess = (TenantServiceAccess) context.lookup("java:global/rd-ms-tenantmanagement//TenantService");

        TenantSearchFilter filter = new TenantSearchFilter("rootName", null, null,false, false);
        List<? extends SystemTenant> roots = tenantServiceAccess.get(filter);
        if (roots.isEmpty()) {
            rootTenant = new TenantEntity();
            rootTenant.setName("rootName");
            rootTenant.setTenantKey("rd");
            rootTenant.setTenantType(TenantType.ROOT_TENANT);
            tenantServiceAccess.create(rootTenant);
        }
        else {
            rootTenant = roots.get(0);
        }
    }

    /**
     * Add tenant test.
     * Will create and save the tenant.
     * Expected result: Success.
     * Tested methods: void save(Tenant tenant)
     *
     * @throws UniquenessConstraintException in case of requested action is not well constructed
     */
    @Test
    public void testCreate() throws UniquenessConstraintException, TenantException {
        SystemTenant result = createTenant("testCreate");
        assertNotNull(result);
    }


    /**
     * Add second root tenant. Should throw exception.
     */
    @Test
    public void testCreateDoubleRootException() {
        SystemTenant tenant = new TenantEntity();
        tenant.setName("nameCreation");
        tenant.setTenantType(TenantType.ROOT_TENANT);
        tenant.setTenantStart(LocalDate.now());
        tenant.setTenantKey(RandomStringUtils.randomAlphabetic(4));
        Exception exception = assertThrows(TenantException.class, () -> tenantServiceAccess.create(tenant));
        String expectedMessage = GenericErrorCodeMessage.TENANT_ROOT_ALREADY_INSERTED.toString();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Add tenant test with duplicated name.
     * Will create and save the tenant, with an already existent name.
     * Expected result: Throw treated exception Error 101 Message There is more than one tenant with the same name.
     * Tested methods: void create(Tenant tenant)
     */
    @Test
    public void testAddDuplicatedName() throws UniquenessConstraintException, TenantException {
        createTenant("testAddDuplicatedName");
        Exception exception = assertThrows(UniquenessConstraintException.class, () -> createTenant("testAddDuplicatedName"));
        String expectedMessage = GenericErrorCodeMessage.DUPLICATED_FIELD.toString("Name");
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Update tenant test with duplicated name.
     * Will create and save the tenant, with an already existent name.
     * Expected result: Throw treated exception Error 101 Message There is more than one tenant with the same name.
     * Tested methods: void update(Tenant tenant)
     */
    @Test
    public void testUpdateDuplicated() throws UniquenessConstraintException, TenantException {
        SystemTenant c = createTenant("testUpdateDuplicated");
        Exception exception = assertThrows(UniquenessConstraintException.class, () ->
                tenantServiceAccess.update(new TenantEntity(new io.radien.ms.tenantmanagement.client.entities.Tenant(
                        null,"testUpdateDuplicated", "key-x", TenantType.CLIENT_TENANT, null, null,
                        null, null, null,null, null,
                        null, rootTenant.getId(), null))));
        String expectedMessage = GenericErrorCodeMessage.DUPLICATED_FIELD.toString("Name");
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    /**
     * Gets tenant using the PK (id).
     * Will create a new tenant, save it into the DB and retrieve the specific tenant using the ID.
     * Expected result: will return the correct inserted tenant.
     * Tested methods: SystemTenant get(Long tenantId)
     *
     * @throws UniquenessConstraintException in case of requested action is not well constructed
     */
    @Test
    public void testGetById() throws UniquenessConstraintException, TenantException {
        String name = "testGetById";
        SystemTenant c = new TenantEntity(new io.radien.ms.tenantmanagement.client.entities.Tenant(
                11111L,name, RandomStringUtils.randomAlphabetic(4), TenantType.CLIENT_TENANT, null, null,
                null, null, null,null, null,
                null, rootTenant.getId(), null));
        tenantServiceAccess.create(c);
        SystemTenant result = tenantServiceAccess.get(c.getId());
        assertNotNull(result);
        assertEquals(c.getName(), result.getName());
    }

    /**
     * Deletes inserted tenant using the PK (id).
     * Will create a new tenant, save it into the DB and delete it after using the specific ID.
     * Expected result: will return null when retrieving the tenant.
     * Tested methods: void delete(Long tenantId)
     *
     * @throws UniquenessConstraintException in case of requested action is not well constructed
     */
    @Test
    public void testDeleteById() throws UniquenessConstraintException, TenantException {
        SystemTenant tenant = createTenant("testDeleteById");
        SystemTenant result = tenantServiceAccess.get(tenant.getId());
        assertNotNull(result);
        assertEquals(tenant.getName(), result.getName());
        tenantServiceAccess.delete(tenant.getId());
        result = tenantServiceAccess.get(tenant.getId());
        assertNull(result);
    }

    /**
     * Deletes inserted tenant using the PK (id).
     * Will create a new tenant, save it into the DB and delete it after using the specific ID.
     * Expected result: will return null when retrieving the tenant.
     * Tested methods: void delete(Long tenantId)
     *
     * @throws UniquenessConstraintException in case of requested action is not well constructed
     */
    @Test
    public void testDeleteByListId() throws UniquenessConstraintException, TenantException {
        SystemTenant tenant = createTenant("testDeleteByListId");
        SystemTenant result = tenantServiceAccess.get(tenant.getId());
        assertNotNull(result);
        assertEquals(tenant.getName(), result.getName());
        tenantServiceAccess.delete(Collections.singletonList(tenant.getId()));
        result = tenantServiceAccess.get(tenant.getId());
        assertNull(result);
    }

    /**
     * Deletes inserted tenant using the PK (id) and if exists the under the parent of tenants.
     * Will create a new tenant, save it into the DB and delete it after using the specific ID.
     * Expected result: will return null when retrieving the tenant and under sub tenants.
     * Tested methods: void deleteTenantHierarchy(Long tenantId)
     *
     * @throws UniquenessConstraintException in case of requested action is not well constructed
     */
    @Test
    public void testDeleteTenantHierarchyById() throws UniquenessConstraintException, TenantException {
        SystemTenant tenant = createTenant("testDeleteById");
        SystemTenant result = tenantServiceAccess.get(tenant.getId());
        assertNotNull(result);
        assertEquals(tenant.getName(), result.getName());
        tenantServiceAccess.deleteTenantHierarchy(tenant.getId());
        result = tenantServiceAccess.get(tenant.getId());
        assertNull(result);
    }

    /**
     * Test updates the tenant information.
     * @throws Exception in case of tenant to be updated not found
     */
    @Test
    public void testUpdateSuccess() throws Exception {
        SystemTenant c1 = createTenant("a<a>2355");
        String name3 = "a<a>99zz";
        c1.setName(name3);
        tenantServiceAccess.update(c1);
        c1 = tenantServiceAccess.get(c1.getId());

        assertEquals(name3, c1.getName());
    }

    /**
     * Test of creation of tenants
     * @param name of the tenant to create
     * @return system tenant
     * @throws UniquenessConstraintException in case of duplicates
     * @throws TenantException in case of any issue in the data
     */
    private SystemTenant createTenant(String name) throws UniquenessConstraintException, TenantException {
        SystemTenant tenant = new TenantEntity();
        tenant.setName(name);
        tenant.setTenantType(TenantType.CLIENT_TENANT);
        tenant.setParentId(rootTenant.getId());
        tenant.setTenantKey(RandomStringUtils.randomAlphabetic(4));
        tenantServiceAccess.create(tenant);
        return tenant;
    }

    /**
     * Test of get all the tenants
     * @throws UniquenessConstraintException in case of duplicates
     * @throws TenantException in case of any issue in the data
     */
    @Test
    public void testGetAll() throws UniquenessConstraintException, TenantException {
        String name = "testGetAll";
        SystemTenant c = new TenantEntity();
        c.setId(100L);
        c.setName(name);
        c.setTenantType(TenantType.CLIENT_TENANT);
        c.setParentId(rootTenant.getId());
        c.setTenantKey(RandomStringUtils.randomAlphabetic(4));
        tenantServiceAccess.create(c);
        Page<SystemTenant> result = tenantServiceAccess.getAll(null,1,10,null,false);
        assertNotNull(result);
    }

    @Test
    public void testGetAllSearchNotNullSort() throws UniquenessConstraintException, TenantException {
        String name = "testGetAll2";
        SystemTenant c = new TenantEntity();
        c.setId(102L);
        c.setName(name);
        c.setTenantType(TenantType.CLIENT_TENANT);
        c.setParentId(rootTenant.getId());
        c.setTenantKey(RandomStringUtils.randomAlphabetic(4));
        tenantServiceAccess.create(c);

        List<String> sortBy = new ArrayList<>();
        sortBy.add("name");

        Page<SystemTenant> result = tenantServiceAccess.getAll("testGetAll2",1,10,sortBy,false);
        assertNotNull(result);

        Page<SystemTenant> result2 = tenantServiceAccess.getAll("testGetAll2",1,10,sortBy,true);
        assertNotNull(result2);
    }

    /**
     * Test of get specific tenant method
     * @throws UniquenessConstraintException in case of duplicates
     * @throws TenantException in case of any issue in the data
     */
    @Test
    public void testGet() throws UniquenessConstraintException, TenantException {
        String name = "testGet";
        SystemTenant c = new TenantEntity(new io.radien.ms.tenantmanagement.client.entities.Tenant(
                200L,name, RandomStringUtils.randomAlphabetic(4), TenantType.CLIENT_TENANT, null, null,
                null, null, null,null, null,
                null, rootTenant.getId(), null));
        tenantServiceAccess.create(c);
        TenantSearchFilter filter = new TenantSearchFilter(name, null, null,false, false);
        List<? extends SystemTenant> result = tenantServiceAccess.get(filter);
        assertNotNull(result);
        assertEquals((Long) 200L, result.get(0).getId());
    }

    /**
     * Test of get specific tenant method with logical conjunction
     * @throws UniquenessConstraintException in case of duplicates
     * @throws TenantException in case of any issue in the data
     */
    @Test
    public void testGetIsLogicConjunction() throws UniquenessConstraintException, TenantException {
        String name = "testGetIsLogicalConjunction";
        SystemTenant c = new TenantEntity(new io.radien.ms.tenantmanagement.client.entities.Tenant(
                923L,name, RandomStringUtils.randomAlphabetic(4), TenantType.CLIENT_TENANT, null, null,
                null, null, null,null, null,
                null, rootTenant.getId(), null));
        tenantServiceAccess.create(c);
        TenantSearchFilter filter = new TenantSearchFilter(name, null, null,false, true);
        List<? extends SystemTenant> result = tenantServiceAccess.get(filter);
        assertNotNull(result);
        assertEquals((Long) 923L, result.get(0).getId());
    }

    /**
     * Will test the validation of a specific tenant exists
     * @throws UniquenessConstraintException in case of duplicates
     * @throws NotFoundException in case of tenant not existing
     * @throws TenantException in case of any issue in the data
     */
    @Test
    public void testExists() throws UniquenessConstraintException, NotFoundException, TenantException {
        String name = "testExists";
        SystemTenant c = new TenantEntity(new io.radien.ms.tenantmanagement.client.entities.Tenant(
                300L,name, RandomStringUtils.randomAlphabetic(4), TenantType.CLIENT_TENANT, null, null,
                null, null, null,null, null,
                null, rootTenant.getId(), null));
        tenantServiceAccess.create(c);
        assertTrue(tenantServiceAccess.exists(300L));
    }

    /**
     * Tests adding a root tenant
     */
    @Test
    public void testAddRootTenant() {
        TenantEntity tenant = new TenantEntity();
        tenant.setTenantType(TenantType.ROOT_TENANT);
        tenant.setTenantKey("key1");
        tenant.setName("radien-default");
        tenant.setTenantStart(LocalDate.now());
        tenant.setParentId(2L);
        tenant.setTenantEnd(LocalDate.now().plus(3, ChronoUnit.YEARS));
        assertThrows(TenantException.class, ()->tenantServiceAccess.create(tenant));
    }

    /**
     * Test to add a sub tenant
     * @throws UniquenessConstraintException in case of duplicates
     * @throws TenantException in case of any issue in the data
     */
    @Test
    public void testAddSubTenant() throws UniquenessConstraintException, TenantException {
        TenantEntity tenant = new TenantEntity();
        tenant.setTenantType(TenantType.CLIENT_TENANT);
        tenant.setTenantKey("keyClient");
        tenant.setParentId(rootTenant.getId());
        tenant.setName("volkswagen-accountancy-client");
        tenant.setTenantStart(LocalDate.now());
        tenant.setTenantEnd(LocalDate.now().plus(3, ChronoUnit.YEARS));

        tenantServiceAccess.create(tenant);

        TenantEntity tenantSub = new TenantEntity();
        tenantSub.setTenantType(TenantType.SUB_TENANT);
        tenantSub.setTenantKey("key111");
        tenantSub.setParentId(tenant.getId());
        tenantSub.setClientId(tenant.getId());
        tenantSub.setName("volkswagen-accountancy");
        tenantSub.setTenantStart(LocalDate.now());
        tenantSub.setTenantEnd(LocalDate.now().plus(3, ChronoUnit.YEARS));

        tenantServiceAccess.create(tenantSub);

        SystemTenant systemTenant = tenantServiceAccess.get(tenantSub.getId());
        assertNotNull(systemTenant);
        assertEquals(TenantType.SUB_TENANT, systemTenant.getTenantType());

        TenantSearchFilter filter = new TenantSearchFilter("volkswagen-accountancy", null, null,false, false);
        List<? extends SystemTenant> list =
                tenantServiceAccess.get(filter);
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

    /**
     * Test to retrieve (filter) tenants by a list of ids
     * @throws UniquenessConstraintException in case of duplicates
     * @throws TenantException in case of any issue in the data
     */
    @Test
    public void testGetTenantsByIds() throws UniquenessConstraintException, TenantException {
        TenantEntity tenant = new TenantEntity();
        tenant.setTenantType(TenantType.CLIENT_TENANT);
        tenant.setTenantKey("keyClient");
        tenant.setParentId(rootTenant.getId());
        tenant.setName("bmw");
        tenant.setTenantStart(LocalDate.now());
        tenantServiceAccess.create(tenant);

        TenantEntity tenantSub = new TenantEntity();
        tenantSub.setTenantType(TenantType.SUB_TENANT);
        tenantSub.setTenantKey("key111");
        tenantSub.setParentId(tenant.getId());
        tenantSub.setClientId(tenant.getId());
        tenantSub.setName("bmw-hr");
        tenantSub.setTenantStart(LocalDate.now());
        tenantServiceAccess.create(tenantSub);

        TenantEntity tenantSub2 = new TenantEntity();
        tenantSub2.setTenantType(TenantType.SUB_TENANT);
        tenantSub2.setTenantKey("key111");
        tenantSub2.setParentId(tenant.getId());
        tenantSub2.setClientId(tenant.getId());
        tenantSub2.setName("bmw-it");
        tenantSub2.setTenantStart(LocalDate.now());
        tenantServiceAccess.create(tenantSub2);

        List<Long> ids = Arrays.asList(tenant.getId(), tenantSub.getId(), tenantSub2.getId());

        TenantSearchFilter filter = new TenantSearchFilter(null, null, ids,false, false);
        List<? extends SystemTenant> list = tenantServiceAccess.get(filter);
        assertEquals(3, list.size());

        filter.setLogicConjunction(true);
        list = tenantServiceAccess.get(filter);
        assertEquals(3, list.size());
    }

    /**
     * Test to validate the creation of a root tenant under a client tenant
     * @throws UniquenessConstraintException in case of duplicates
     * @throws TenantException in case of any issue in the data
     */
    @Test
    public void rootUnderClientException() throws UniquenessConstraintException, TenantException {
        TenantEntity tenant = new TenantEntity();
        tenant.setTenantType(TenantType.CLIENT_TENANT);
        tenant.setTenantKey("keyClient1");
        tenant.setParentId(rootTenant.getId());
        tenant.setName("volkswagen-accountancy-client1");
        tenant.setTenantStart(LocalDate.now());
        tenant.setTenantEnd(LocalDate.now().plus(3, ChronoUnit.YEARS));

        tenantServiceAccess.create(tenant);

        TenantEntity tenantRoot = new TenantEntity();
        tenantRoot.setTenantType(TenantType.ROOT_TENANT);
        tenantRoot.setTenantKey("keyRoot1");
        tenantRoot.setParentId(tenant.getId());
        tenantRoot.setName("volkswagen-accountancy-root1");
        tenantRoot.setTenantStart(LocalDate.now());
        tenantRoot.setTenantEnd(LocalDate.now().plus(3, ChronoUnit.YEARS));

        TenantException e = assertThrows(TenantException.class, ()->tenantServiceAccess.create(tenantRoot));
        assertEquals(GenericErrorCodeMessage.TENANT_ROOT_WITH_PARENT.toString(), e.getMessage());

        TenantEntity tenantRoot2 = new TenantEntity();
        tenantRoot2.setTenantType(TenantType.ROOT_TENANT);
        tenantRoot2.setTenantKey("keyRoot1");
        tenantRoot2.setClientId(tenant.getId());
        tenantRoot2.setName("volkswagen-accountancy-root1");
        tenantRoot2.setTenantStart(LocalDate.now());
        tenantRoot2.setTenantEnd(LocalDate.now().plus(3, ChronoUnit.YEARS));

        TenantException e2 = assertThrows(TenantException.class, ()->tenantServiceAccess.create(tenantRoot2));
        assertEquals(GenericErrorCodeMessage.TENANT_ROOT_WITH_CLIENT.toString(), e2.getMessage());
    }

    /**
     * Tests the creation of a client tenant under a sub tenant
     * @throws UniquenessConstraintException in case of duplicates
     * @throws TenantException in case of any issue in the data
     */
    @Test
    public void clientUnderSubException() throws UniquenessConstraintException, TenantException {
        TenantEntity tenant = new TenantEntity();
        tenant.setTenantType(TenantType.SUB_TENANT);
        tenant.setTenantKey("keySub1");
        tenant.setParentId(rootTenant.getId());
        tenant.setClientId(rootTenant.getId());
        tenant.setName("clientUnderSubException1");
        tenant.setTenantStart(LocalDate.now());
        tenant.setTenantEnd(LocalDate.now().plus(3, ChronoUnit.YEARS));

        tenantServiceAccess.create(tenant);

        TenantEntity tenantRoot = new TenantEntity();
        tenantRoot.setTenantType(TenantType.CLIENT_TENANT);
        tenantRoot.setTenantKey("keyRoot1");
        tenantRoot.setParentId(tenant.getId());
        tenantRoot.setName("volkswagen-accountancy-root1");
        tenantRoot.setTenantStart(LocalDate.now());
        tenantRoot.setTenantEnd(LocalDate.now().plus(3, ChronoUnit.YEARS));

        TenantException e = assertThrows(TenantException.class, ()->tenantServiceAccess.create(tenantRoot));
        assertEquals(GenericErrorCodeMessage.TENANT_PARENT_TYPE_IS_INVALID.toString(), e.getMessage());
    }

    /**
     * Tests the sub tenant rule of non parent
     */
    @Test
    public void subTenantRuleValidationNoParent() {
        TenantEntity tenant = new TenantEntity();
        tenant.setTenantType(TenantType.SUB_TENANT);
        tenant.setTenantKey("keySub1");
        tenant.setClientId(rootTenant.getId());
        tenant.setName("volkswagen-accountancy-Sub1");
        tenant.setTenantStart(LocalDate.now());
        tenant.setTenantEnd(LocalDate.now().plus(3, ChronoUnit.YEARS));

        TenantException e = assertThrows(TenantException.class, ()->tenantServiceAccess.create(tenant));
        assertEquals(GenericErrorCodeMessage.TENANT_PARENT_NOT_INFORMED.toString(), e.getMessage());
    }

    /**
     * Tests the sub tenant rule of non client
     */
    @Test
    public void subTenantRuleValidationNoClient() {
        TenantEntity tenant = new TenantEntity();
        tenant.setTenantType(TenantType.SUB_TENANT);
        tenant.setTenantKey("keySub1");
        tenant.setParentId(rootTenant.getId());
        tenant.setName("volkswagen-accountancy-Sub1");
        tenant.setTenantStart(LocalDate.now());
        tenant.setTenantEnd(LocalDate.now().plus(3, ChronoUnit.YEARS));

        TenantException e = assertThrows(TenantException.class, ()->tenantServiceAccess.create(tenant));
        assertEquals(GenericErrorCodeMessage.TENANT_CLIENT_NOT_INFORMED.toString(), e.getMessage());
    }

    /**
     * Tests the sub tenant rule of not found parent
     */
    @Test
    public void subTenantRuleValidationNotFoundParent() {
        TenantEntity tenant = new TenantEntity();
        tenant.setTenantType(TenantType.SUB_TENANT);
        tenant.setTenantKey("keySub1");
        tenant.setClientId(rootTenant.getId());
        tenant.setParentId(555L);
        tenant.setName("volkswagen-accountancy-Sub1");
        tenant.setTenantStart(LocalDate.now());
        tenant.setTenantEnd(LocalDate.now().plus(3, ChronoUnit.YEARS));

        TenantException e = assertThrows(TenantException.class, ()->tenantServiceAccess.create(tenant));
        assertEquals(GenericErrorCodeMessage.TENANT_PARENT_NOT_FOUND.toString(), e.getMessage());
    }

    /**
     * Tests the sub tenant rule of not found client
     */
    @Test
    public void subTenantRuleValidationNotFountClient() {
        TenantEntity tenant = new TenantEntity();
        tenant.setTenantType(TenantType.SUB_TENANT);
        tenant.setTenantKey("keySub1");
        tenant.setParentId(rootTenant.getId());
        tenant.setClientId(200L);
        tenant.setName("volkswagen-accountancy-Sub1");
        tenant.setTenantStart(LocalDate.now());
        tenant.setTenantEnd(LocalDate.now().plus(3, ChronoUnit.YEARS));

        TenantException e = assertThrows(TenantException.class, ()->tenantServiceAccess.create(tenant));
        assertEquals(GenericErrorCodeMessage.TENANT_CLIENT_NOT_FOUND.toString(), e.getMessage());
    }

    /**
     * Test to retrieve all the tenants
     * @throws UniquenessConstraintException in case of duplicates
     * @throws TenantException in case of any issue in the data
     */
    @Test
    public void testRetrieveAllPossibleTenants() throws UniquenessConstraintException, TenantException {

        SystemTenant tenant = new TenantEntity();
        tenant.setTenantType(TenantType.CLIENT_TENANT);
        tenant.setTenantKey(RandomStringUtils.randomAlphabetic(4));
        tenant.setName("volkswagen-marketing");
        tenant.setTenantStart(LocalDate.now());
        tenant.setParentId(rootTenant.getId());
        tenantServiceAccess.create(tenant);

        tenant = new TenantEntity();
        tenant.setTenantType(TenantType.CLIENT_TENANT);
        tenant.setTenantKey(RandomStringUtils.randomAlphabetic(4));
        tenant.setName("volkswagen-human-resources");
        tenant.setTenantStart(LocalDate.now());
        tenant.setTenantEnd(LocalDate.now().plus(10, ChronoUnit.YEARS));
        tenant.setParentId(rootTenant.getId());

        tenantServiceAccess.create(tenant);

        // Page size = 100 -> Overkill!!
        Page<SystemTenant> page = tenantServiceAccess.getAll(null,1,100,null,false);
        assertTrue(page.getTotalResults()>2);
    }

    /**
     * Tests to validate tenant rules
     */
    @Test
    public void creatingInvalidTenant() {
        SystemTenant tenant = new TenantEntity();
        TenantException e = assertThrows(TenantException.class, ()->tenantServiceAccess.create(tenant));
        assertEquals(GenericErrorCodeMessage.TENANT_FIELD_NOT_INFORMED.toString("name"), e.getMessage());

        tenant.setName("test");
        e = assertThrows(TenantException.class, ()->tenantServiceAccess.create(tenant));
        assertEquals(GenericErrorCodeMessage.TENANT_FIELD_NOT_INFORMED.toString("tenantKey"), e.getMessage());

        tenant.setTenantKey("key-1");
        e = assertThrows(TenantException.class, ()->tenantServiceAccess.create(tenant));
        assertEquals(GenericErrorCodeMessage.TENANT_FIELD_NOT_INFORMED.toString("tenantType"), e.getMessage());

        tenant.setTenantType(TenantType.CLIENT_TENANT);
        tenant.setTenantEnd(LocalDate.now().minus(3, ChronoUnit.MONTHS));
        tenant.setTenantStart(LocalDate.now());

        e = assertThrows(TenantException.class, ()->tenantServiceAccess.create(tenant));
        assertEquals(GenericErrorCodeMessage.TENANT_END_DATE_IS_IS_INVALID.toString(), e.getMessage());

        tenant.setTenantEnd(LocalDate.now().plus(3, ChronoUnit.MONTHS));

        e = assertThrows(TenantException.class, ()->tenantServiceAccess.create(tenant));
        assertEquals(GenericErrorCodeMessage.TENANT_PARENT_NOT_INFORMED.toString(), e.getMessage());

        tenant.setParentId(111L);
        e = assertThrows(TenantException.class, ()->tenantServiceAccess.create(tenant));
        assertEquals(GenericErrorCodeMessage.TENANT_PARENT_NOT_FOUND.toString(), e.getMessage());

        SystemTenant newTenantRoot = new TenantEntity();
        newTenantRoot.setTenantType(TenantType.ROOT_TENANT);
        newTenantRoot.setName("root");
        newTenantRoot.setTenantKey("root-1");
        newTenantRoot.setTenantStart(LocalDate.now());
        newTenantRoot.setTenantEnd(LocalDate.now().plus(5, ChronoUnit.MONTHS));
        newTenantRoot.setParentId(111L);

        e = assertThrows(TenantException.class, ()->tenantServiceAccess.create(newTenantRoot));
        assertEquals(GenericErrorCodeMessage.TENANT_ROOT_WITH_PARENT.toString(), e.getMessage());

        newTenantRoot.setParentId(null);
    }
}