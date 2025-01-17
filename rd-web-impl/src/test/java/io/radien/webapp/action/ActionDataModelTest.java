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
package io.radien.webapp.action;

import io.radien.api.model.permission.SystemAction;
import io.radien.api.service.permission.ActionRESTServiceAccess;

import io.radien.exception.SystemException;
import io.radien.ms.permissionmanagement.client.entities.Action;
import io.radien.webapp.DataModelEnum;
import io.radien.webapp.JSFUtil;
import io.radien.webapp.JSFUtilAndFaceContextMessagesTest;
import io.radien.webapp.activeTenant.ActiveTenantDataModelManager;

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

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
/**
 * Class that aggregates UnitTest cases for ActionDataModel
 *
 * @author Rajesh Gavvala
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({JSFUtil.class, FacesContext.class, ExternalContext.class})
public class ActionDataModelTest extends JSFUtilAndFaceContextMessagesTest {
    @InjectMocks
    private ActionDataModel actionDataModel;

    @Mock
    private ActionRESTServiceAccess service;

    @Mock
    private ActiveTenantDataModelManager activeTenantDataModelManager;

    SystemAction systemAction;

    /**
     * Constructs mock object
     */
    @Before
    public void before(){
        MockitoAnnotations.initMocks(this);
        handleJSFUtilAndFaceContextMessages();

        systemAction = new Action();
        systemAction.setId(1L);
        systemAction.setName("testAction");
    }

    @Test
    public void testInit(){
        doReturn(true).when(activeTenantDataModelManager).isTenantActive();
        actionDataModel.init();
        assertEquals(service, actionDataModel.getService());
        assertNotNull(actionDataModel.getLazyModel());

        doReturn(false).when(activeTenantDataModelManager).isTenantActive();
        actionDataModel.onload();
    }

    @Test
    public void testSave() throws SystemException {
        doReturn(true).when(service).create(systemAction);
        String expected = actionDataModel.save(systemAction);
        assertEquals(expected, DataModelEnum.ACTION_MAIN_PAGE.getValue());
    }

    @Test
    public void testSaveException() throws SystemException {
        doThrow(NullPointerException.class).when(service).create(systemAction);
        String expected = actionDataModel.save(systemAction);
        assertEquals(expected, DataModelEnum.ACTION_CREATION_PAGE.getValue());
    }

    @Test
    public void testEditRecords() {
        actionDataModel.setSelectedAction(null);
        String expectedMainPage = actionDataModel.editRecords();
        assertEquals(expectedMainPage, DataModelEnum.ACTION_MAIN_PAGE.getValue());

        actionDataModel.setSelectedAction(systemAction);
        String expectedDetailPage = actionDataModel.editRecords();
        assertEquals(expectedDetailPage, DataModelEnum.ACTION_DETAIL_PAGE.getValue());
    }

    @Test
    public void testDelete() throws SystemException {
        actionDataModel.setSelectedAction(systemAction);
        doReturn(true).when(service).delete(systemAction.getId());
        actionDataModel.delete();
        assertNull(actionDataModel.getSelectedAction());

        actionDataModel.delete();
    }

    @Test(expected = Exception.class)
    public void testDeleteException() throws SystemException {
        actionDataModel.setSelectedAction(systemAction);
        doThrow(Exception.class).when(service).delete(systemAction.getId());
        actionDataModel.delete();
    }

    @Test
    public void testEdit() throws SystemException {
        String expectedMainPage = actionDataModel.edit(null);
        assertEquals(expectedMainPage, DataModelEnum.ACTION_DETAIL_PAGE.getValue());

        systemAction.setName("testUpdate");
        actionDataModel.setSelectedAction(systemAction);
        doReturn(true).when(service).create(systemAction);
        String expected = actionDataModel.edit(systemAction);
        assertEquals(expected, DataModelEnum.ACTION_MAIN_PAGE.getValue());
    }

    @Test
    public void testEditException() throws SystemException {
        actionDataModel.setSelectedAction(systemAction);
        doThrow(NullPointerException.class).when(service).create(systemAction);
        String expected = actionDataModel.edit(systemAction);
        assertEquals(expected, DataModelEnum.ACTION_DETAIL_PAGE.getValue());
    }

    @Test
    public void testGetActionAndSetAction(){
        actionDataModel.setAction(systemAction);
        assertEquals(systemAction, actionDataModel.getAction());
    }

    @Test
    public void testReturnToDataTableRecords(){
        String expected = actionDataModel.returnToDataTableRecords();
        assertEquals(expected, DataModelEnum.ACTION_MAIN_PAGE.getValue());
    }

    @Test
    public void testOnRowSelect() {
        SelectEvent<SystemAction> event = mock(SelectEvent.class);
        when(event.getObject()).thenReturn(systemAction);
        actionDataModel.onRowSelect(event);
        assertEquals(systemAction, actionDataModel.getSelectedAction());
    }

}