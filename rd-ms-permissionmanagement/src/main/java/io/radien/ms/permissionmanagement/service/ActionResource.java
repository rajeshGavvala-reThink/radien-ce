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
package io.radien.ms.permissionmanagement.service;

import io.radien.api.model.permission.SystemAction;
import io.radien.api.service.permission.ActionServiceAccess;
import io.radien.exception.GenericErrorMessagesToResponseMapper;
import io.radien.exception.UniquenessConstraintException;
import io.radien.ms.permissionmanagement.client.entities.ActionSearchFilter;
import io.radien.ms.permissionmanagement.client.services.ActionResourceClient;
import io.radien.ms.permissionmanagement.model.ActionEntity;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Controller implementation responsible for deal with CRUD
 * operations requests (CRUD) regarding Action domain object
 *
 * @author Newton Carvalho
 */
@Path("action")
@RequestScoped
public class ActionResource implements ActionResourceClient {

	@Inject
	private ActionServiceAccess actionServiceAccess;

	/**
	 * Retrieves a page object containing actions that matches search parameter.
	 * In case of omitted (empty) search parameter retrieves ALL actions
	 * @param search search parameter for matching actions (optional).
	 * @param pageNo page number
	 * @param pageSize page size
	 * @param sortBy Sorting fields
	 * @param isAscending Defines if ascending or descending in relation of sorting fields
	 * @return In case of successful operation returns OK (http status 200)
	 * and the page object (filled or not).<br>
	 * Otherwise, in case of operational error, returns Internal Server Error (500)
	 */
	public Response getAll(String search,
						   int pageNo,
						   int pageSize,
						   List<String> sortBy,
						   boolean isAscending) {
		try {
			return Response.ok(actionServiceAccess.getAll(search, pageNo, pageSize, sortBy, isAscending)).build();
		} catch (Exception e) {
			return GenericErrorMessagesToResponseMapper.getGenericError(e);
		}
	}

	/**
	 * Finds all actions that matches a name
	 * @param name action name
	 * @param ids action ids to be found
	 * @param isExact indicates if the match is for approximated value or not
	 * @param isLogicalConjunction specifies if the parameters will be unified by AND (true) or OR (false)
	 * @return In case of successful operation returns 200 (http status)
	 * and the collection (filled or not).<br>
	 * Otherwise, in case of operational error, returns 500
	 */
	public Response getActions(String name, List<Long> ids, boolean isExact,
							   boolean isLogicalConjunction) {

		try {
			return Response.ok(actionServiceAccess.getActions(new ActionSearchFilter(name, ids,
					isExact, isLogicalConjunction))).build();
		} catch (Exception e) {
			return GenericErrorMessagesToResponseMapper.getGenericError(e);
		}
	}

	/**
	 * Retrieves an action by its identifier
	 * @param id action identifier
	 * @return If action exists returns 200 status (and the correspondent object)
	 * Otherwise, if does not exist, return 404 status
	 * In case of operational error return 500 status
	 */
	public Response getById(Long id) {
		try {
			SystemAction systemAction = actionServiceAccess.get(id);
			if(systemAction == null){
				return GenericErrorMessagesToResponseMapper.getResourceNotFoundException();
			}
			return Response.ok(systemAction).build();
		} catch(Exception e) {
			return GenericErrorMessagesToResponseMapper.getGenericError(e);
		}
	}

	/**
	 * Deletes an action by its identifier
	 * @param id action identifier
	 * @return Returns 200 status, Otherwise, in case of operational error return 500 status
	 */
	public Response delete(long id) {
		try {
			actionServiceAccess.delete(id);
		} catch (Exception e){
			return GenericErrorMessagesToResponseMapper.getGenericError(e);
		}
		return Response.ok().build();
	}

	/**
	 * Saves an action (Creation or Update).
	 * @param action action to be created or update
	 * @return Http status 200 in case of successful operation.<br>
	 * Bad request (404) in case of trying to create an action with repeated description.<br>
	 * Internal Server Error (500) in case of operational error
	 */
	public Response save(io.radien.ms.permissionmanagement.client.entities.Action action) {
		try {
			actionServiceAccess.save(new ActionEntity(action));
			return Response.ok().build();
		} catch (UniquenessConstraintException e) {
			return GenericErrorMessagesToResponseMapper.getInvalidRequestResponse(e.getMessage());
		} catch (Exception e) {
			return GenericErrorMessagesToResponseMapper.getGenericError(e);
		}
	}
}