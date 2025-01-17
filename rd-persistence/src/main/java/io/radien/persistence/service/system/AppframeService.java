/*
 * Copyright (c) 2016-present radien.io & its legal owners. All rights reserved.
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
package io.radien.persistence.service.system;

import java.util.List;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import io.radien.persistence.entities.system.Appframe;

/**
 * @author Marco Weiland <m.weiland@radien.io>
 *
 */
@Stateful
public class AppframeService {
	@PersistenceContext(unitName = "persistenceUnitLocal", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    public void addAppframe(Appframe appframe) {
        entityManager.persist(appframe);
    }

    public void deleteAppframe(Appframe appframe) {
        entityManager.remove(appframe);
    }

    public List<Appframe> getAppframes() {
        Query query = entityManager.createQuery("SELECT a from Appframe as a");
        return query.getResultList();
    }
}
