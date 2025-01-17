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
 * See the License for the specific language governing actions and
 * limitations under the License.
 */
package io.radien.ms.permissionmanagement.client.services;

import io.radien.api.OAFAccess;
import io.radien.api.OAFProperties;
import io.radien.api.entity.Page;
import io.radien.api.model.permission.SystemResource;
import io.radien.api.service.permission.ResourceRESTServiceAccess;
import io.radien.exception.GenericErrorCodeMessage;
import io.radien.exception.SystemException;
import io.radien.exception.TokenExpiredException;
import io.radien.ms.authz.security.AuthorizationChecker;
import io.radien.ms.permissionmanagement.client.entities.Resource;
import io.radien.ms.permissionmanagement.client.util.ClientServiceUtil;
import io.radien.ms.permissionmanagement.client.util.ResourceModelMapper;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;
import org.apache.cxf.bus.extension.ExtensionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation for Rest Service Client regarding Resource domain object
 * @author Newton Carvalho
 */
@RequestScoped
public class ResourceRESTServiceClient extends AuthorizationChecker implements ResourceRESTServiceAccess {

	private static final long serialVersionUID = 9067873232102652213L;

	private static final Logger log = LoggerFactory.getLogger(ResourceRESTServiceClient.class);

    private OAFAccess oaf;

    @Inject
    private ClientServiceUtil clientServiceUtil;

    /**
     * Calls the requester to fetch all resources if not possible will reload the access token and retry
     * @param search value to be filtered
     * @param pageNo of the information to be checked
     * @param pageSize max page numbers for the necessary requested data
     * @param sortBy list of values to sort request
     * @param isAscending in case of true data will come ascending mode if false descending
     * @return list of resources
     * @throws SystemException in case of any communication error
     */
    @Override
    public Page<? extends SystemResource> getAll(String search, int pageNo, int pageSize,
                                                 List<String> sortBy, boolean isAscending) throws SystemException {
        try {
            return getAllRequester(search, pageNo, pageSize, sortBy, isAscending);
        } catch (TokenExpiredException expiredException) {
            refreshToken();
            try{
                return getAllRequester(search, pageNo, pageSize, sortBy, isAscending);
            } catch (TokenExpiredException expiredException1){
                throw new SystemException(GenericErrorCodeMessage.EXPIRED_ACCESS_TOKEN.toString());
            }
        }
    }

    /**
     * Fetches all resources
     * @param search value to be filtered
     * @param pageNo of the information to be checked
     * @param pageSize max page numbers for the necessary requested data
     * @param sortBy list of values to sort request
     * @param isAscending in case of true data will come ascending mode if false descending
     * @return list of resources
     * @throws SystemException in case of any communication error
     */
    private Page<? extends SystemResource> getAllRequester(String search, int pageNo, int pageSize,
                                                          List<String> sortBy, boolean isAscending) throws SystemException {
        try {
            ResourceResourceClient client = clientServiceUtil.getResourceResourceClient(getOAF().getProperty(OAFProperties.SYSTEM_MS_ENDPOINT_PERMISSIONMANAGEMENT));
            Response response = client.getAll(search, pageNo, pageSize, sortBy, isAscending);
            return ResourceModelMapper.mapToPage((InputStream) response.getEntity());
        } catch (ExtensionException | ProcessingException | MalformedURLException e){
            throw new SystemException(e);
        }
    }

    /**
     * Calls the requester to get an Resource in the DB searching for the field Id
     * if not possible will reload the access token and retry
     * @param id to be looked after
     * @return Optional List of Resources
     * @throws SystemException in case it founds multiple actions or if URL is malformed
     */
    public Optional<SystemResource> getResourceById(Long id) throws SystemException {
        try {
            return getResourceByIdRequester(id);
        } catch (TokenExpiredException expiredException) {
            refreshToken();
            try{
                return getResourceByIdRequester(id);
            } catch (TokenExpiredException expiredException1){
                throw new SystemException(GenericErrorCodeMessage.EXPIRED_ACCESS_TOKEN.toString());
            }
        }
    }

    /**
     * Gets an Resource in the DB searching for the field Id
     *
     * @param id to be looked after
     * @return Optional List of Resources
     * @throws SystemException in case it founds multiple actions or if URL is malformed
     */
    private Optional<SystemResource> getResourceByIdRequester(Long id) throws SystemException {
        try {
            ResourceResourceClient client = clientServiceUtil.getResourceResourceClient(getOAF().
                    getProperty(OAFProperties.SYSTEM_MS_ENDPOINT_PERMISSIONMANAGEMENT));

            Response response = client.getById(id);
            SystemResource action = ResourceModelMapper.map((InputStream) response.getEntity());
            return Optional.ofNullable(action);

        } catch (ExtensionException | ProcessingException | MalformedURLException e){
            throw new SystemException(e);
        }
    }

    /**
     * Calls the requester all the actions in the DB searching for the field Name
     * if not possible will reload the access token and retry
     * @param name to be looked after
     * @return Optional List of Resources
     * @throws SystemException in case it founds multiple actions or if URL is malformed
     */
    public Optional<SystemResource> getResourceByName(String name) throws SystemException {
        try {
            return getResourceByNameRequester(name);
        } catch (TokenExpiredException expiredException) {
            refreshToken();
            try{
                return getResourceByNameRequester(name);
            } catch (TokenExpiredException expiredException1){
                throw new SystemException(GenericErrorCodeMessage.EXPIRED_ACCESS_TOKEN.toString());
            }
        }
    }

    /**
     * Gets all the actions in the DB searching for the field Name
     *
     * @param name to be looked after
     * @return Optional List of Resources
     * @throws SystemException in case it founds multiple actions or if URL is malformed
     */
    private Optional<SystemResource> getResourceByNameRequester(String name) throws SystemException {
        try {
            ResourceResourceClient client = clientServiceUtil.getResourceResourceClient(getOAF().
                    getProperty(OAFProperties.SYSTEM_MS_ENDPOINT_PERMISSIONMANAGEMENT));

            Response response = client.getResources(name,null,true,true);
            List<? extends SystemResource> list = map((InputStream) response.getEntity());
            if (list.size() == 1) {
                return Optional.ofNullable(list.get(0));
            } else {
                return Optional.empty();
            }
        } catch (ExtensionException | ProcessingException | MalformedURLException e){
            throw new SystemException(e);
        }
    }

    /**
     * Calls the requester to retrieve from DB a collection containing resources. The retrieval process will be
     * based on a list containing resource identifiers. In case of JWT expiration, the process will be attempt once more
     * @param ids list of resource identifiers
     * @return a list of resources found (matching the identifiers)
     * @throws SystemException in case of any found error
     */
    public List<? extends SystemResource> getResourcesByIds(List<Long> ids) throws SystemException {
        try {
            return getResourcesByIdsRequester(ids);
        } catch (TokenExpiredException expiredException) {
            refreshToken();
            try{
                return getResourcesByIdsRequester(ids);
            } catch (TokenExpiredException expiredException1){
                throw new SystemException(GenericErrorCodeMessage.EXPIRED_ACCESS_TOKEN.toString());
            }
        }
    }

    /**
     * Retrieves from DB a collection containing resources. The retrieval process will be based on a list of identifiers
     * @param ids list of identifiers
     * @throws SystemException in case of any error
     */
    private List<? extends SystemResource> getResourcesByIdsRequester(List<Long> ids) throws SystemException {
        try {
            ResourceResourceClient client = clientServiceUtil.getResourceResourceClient(getOAF().
                    getProperty(OAFProperties.SYSTEM_MS_ENDPOINT_PERMISSIONMANAGEMENT));
            Response response = client.getResources(null, ids,true, true);
            return map((InputStream) response.getEntity());
        }
        catch (ExtensionException | ProcessingException | MalformedURLException e){
            throw new SystemException(e);
        }
    }

    /**
     * Calls the requester to create a given action if not possible will reload the access token and retry again
     * @param action to be created
     * @return true if action has been created with success or false if not
     * @throws SystemException in case it founds multiple actions or if URL is malformed
     */
    public boolean create(SystemResource action) throws SystemException {
        try {
            return createRequester(action);
        } catch (TokenExpiredException expiredException) {
            refreshToken();
            try{
                return createRequester(action);
            } catch (TokenExpiredException expiredException1){
                throw new SystemException(GenericErrorCodeMessage.EXPIRED_ACCESS_TOKEN.toString());
            }
        }
    }

    /**
     * Creates given action
     * @param action to be created
     * @return true if action has been created with success or false if not
     * @throws SystemException in case it founds multiple actions or if URL is malformed
     */
    private boolean createRequester(SystemResource action) throws SystemException {
        ResourceResourceClient client;
        try {
            client = clientServiceUtil.getResourceResourceClient(getOAF().
                    getProperty(OAFProperties.SYSTEM_MS_ENDPOINT_PERMISSIONMANAGEMENT));
            Response response = client.save((Resource)action);
            if(response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
                return true;
            } else {
                String entity = response.readEntity(String.class);
                log.error(entity);
                return false;
            }
        } catch (ProcessingException | MalformedURLException e) {
            throw new SystemException(e);
        }
    }

    /**
     * Calls the requester to delete a given resource if not possible will reload the access token and retry again
     * @param resourceId to be deleted
     * @return true if resource has been created with success or false if not
     * @throws SystemException in case it founds multiple actions or if URL is malformed
     */
    public boolean delete(long resourceId) throws SystemException {
        try {
            return deleteRequester(resourceId);
        } catch (TokenExpiredException expiredException) {
            refreshToken();
            try{
                return deleteRequester(resourceId);
            } catch (TokenExpiredException expiredException1){
                throw new SystemException(GenericErrorCodeMessage.EXPIRED_ACCESS_TOKEN.toString());
            }
        }
    }

    /**
     * Deletes given resource
     * @param resourceId to be deleted
     * @return true if resource has been deleted with success or false if not
     * @throws SystemException in case it founds multiple actions or if URL is malformed
     */
    private boolean deleteRequester(long resourceId) throws SystemException {
        ResourceResourceClient client;
        try {
            client = clientServiceUtil.getResourceResourceClient(getOAF().
                    getProperty(OAFProperties.SYSTEM_MS_ENDPOINT_PERMISSIONMANAGEMENT));
            Response response = client.delete(resourceId);
            if(response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
                return true;
            } else {
                String entity = response.readEntity(String.class);
                log.error(entity);
                return false;
            }
        } catch (ProcessingException | MalformedURLException e) {
            throw new SystemException(e);
        }
    }

    /**
     * Mapper for a received input stream into a list of possible system action
     * @param is to be mapped
     * @return a list of system action information
     */
    protected List<? extends SystemResource> map(InputStream is) {
        try(JsonReader jsonReader = Json.createReader(is)) {
            JsonArray jsonArray = jsonReader.readArray();
            return ResourceFactory.convert(jsonArray);
        }
    }

    /**
     * Gets the current OAF access
     * @return the oaf object
     */
    public OAFAccess getOAF() {
        if(oaf == null) {
            oaf = CDI.current().select(OAFAccess.class).get();
        }
        return oaf;
    }
}
