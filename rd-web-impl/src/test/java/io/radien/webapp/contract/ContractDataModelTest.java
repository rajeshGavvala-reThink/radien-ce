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
package io.radien.webapp.contract;

import io.radien.api.model.permission.SystemAction;
import io.radien.api.model.tenant.SystemContract;
import io.radien.api.service.tenant.ContractRESTServiceAccess;

import io.radien.ms.permissionmanagement.client.entities.Action;
import io.radien.ms.tenantmanagement.client.entities.Contract;
import io.radien.webapp.JSFUtil;
import io.radien.webapp.JSFUtilAndFaceContextMessagesTest;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Class that aggregates UnitTest cases
 * for ContractDataModel
 *
 * @author Rajesh Gavvala
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({JSFUtil.class, FacesContext.class, ExternalContext.class})
public class ContractDataModelTest extends JSFUtilAndFaceContextMessagesTest {
    @InjectMocks
    private ContractDataModel contractDataModel;

    @Mock
    private ContractRESTServiceAccess service;

    @Mock
    private LazyDataModel<? extends SystemContract> lazyModel;

    private SystemContract systemContract;

    /**
     * Constructs mock object
     */
    @Before
    public void before(){
        MockitoAnnotations.initMocks(this);
        handleJSFUtilAndFaceContextMessages();

        systemContract = new Contract();
        systemContract.setId(1L);
        systemContract.setName("myContract1");
    }

    @Test
    public void testInit(){
        contractDataModel.init();
        contractDataModel.setService(service);
        assertEquals(service, contractDataModel.getService());

        contractDataModel.onload();

        contractDataModel.setLazyModel(lazyModel);
        assertEquals(lazyModel, contractDataModel.getLazyModel());

        SelectEvent<SystemContract> event = mock(SelectEvent.class);
        when(event.getObject()).thenReturn(systemContract);
        contractDataModel.onRowSelect(event);
    }

    @Test
    public void testGetActionAndSetAction(){
        contractDataModel.setSelectedContract(systemContract);
        assertEquals(systemContract, contractDataModel.getSelectedContract());
    }



}