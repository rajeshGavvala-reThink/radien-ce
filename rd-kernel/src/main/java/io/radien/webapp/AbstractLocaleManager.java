/*
 * Copyright (c) 2006-present openappframe.org & its legal owners. All rights reserved.
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
package io.radien.webapp;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.radien.api.webapp.i18n.LocaleManagerAccess;
import io.radien.util.ZoneComparator;

/**
 * A Class responsible for managing openappframe web app i8n in the resource
 * bundle
 *
 * @author Marco Weiland
 */
public abstract class AbstractLocaleManager implements LocaleManagerAccess {

	protected static final Logger log = LoggerFactory.getLogger(AbstractLocaleManager.class);
	private static final long serialVersionUID = -6320273528639534837L;
	private Locale activeLocale;
	private String activeLanguage;
	private List<String> supportedLanguages;
	private String clientTzOffset;

	/**
	 * Locale Manager initialization to start managing the OAF WebApp i18n resources
	 */
	@PostConstruct
	protected void init() {

		Locale requestLocale;
		try {
			requestLocale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
		} catch (Exception e) {
			log.warn("Locale Manager could not get browser locale, using Locale.ENGLISH");
			requestLocale = Locale.ENGLISH;
		}

		activeLocale = getOAF().findLocale(requestLocale.getLanguage());
		activeLanguage = activeLocale.getLanguage();

		log.debug("Locale loaded: {}", activeLocale);
		supportedLanguages = new ArrayList<>();
		supportedLanguages.addAll(getOAF().getSupportedLocales().keySet());

		Calendar instance = Calendar.getInstance(activeLocale);
		String tzID = instance.getTimeZone().getID();
		clientTzOffset = String.format("%s (%s)", tzID, getOffset(LocalDateTime.now(), ZoneId.of(tzID)));
	}

	/**
	 * Abstract Locale Manager active language getter
	 * @return the current active language
	 */
	public String getActiveLanguage() {
		return activeLanguage;
	}

	/**
	 * Abstract Locale Manager active language setter
	 * @param activeLocale to be set
	 */
	public void setActiveLanguage(String activeLocale) {
		Locale localeToset = getOAF().findLocale(activeLocale);
		FacesContext.getCurrentInstance().getViewRoot().setLocale(localeToset);
		this.activeLanguage = localeToset.getLanguage();
		log.info("locale set : {}", FacesContext.getCurrentInstance().getViewRoot().getLocale());

	}

	/**
	 * Returns the Locale of the current language
	 * @return the {@link Locale} object based on the current user language
	 */
	public Locale getActiveLocale() {
		activeLocale = getOAF().findLocale(getActiveLanguage());
		return activeLocale;
	}

	/**
	 * Returns all the timezones that can be handled by the JVM and JSF, sorted
	 * by offset
	 * @return a List of timezones represented as a String
	 */
	public List<String> getTimeZoneList() {

		LocalDateTime now = LocalDateTime.now();
		return ZoneId.getAvailableZoneIds().stream().map(ZoneId::of).sorted(new ZoneComparator())
				.map(id -> String.format("%s (%s)", id.getId(), getOffset(now, id))).collect(Collectors.toList());
	}

	/**
	 * Returns a String with the offset of a ZoneId
	 * @param dateTime the {@link LocalDateTime} to get offset
	 * @param id the ZoneId containing the offset value
	 * @return a String with the offset value
	 */
	private String getOffset(LocalDateTime dateTime, ZoneId id) {
		return dateTime.atZone(id).getOffset().getId().replace("Z", "+00:00");
	}

	/**
	 * Abstract Locale Manager get all possible the supported languages
	 * @return a list of all the possible and supported languages
	 */
	public List<String> getSupportedLanguages() {
		return supportedLanguages;
	}

	/**
	 * Abstract Locale Manager get all possible the supported locales
	 * @return a list of all the possible and supported locales
	 */
	public Collection<Locale> getSupportedLocales() {
		return getOAF().getSupportedLocales().values();
	}

	/**
	 * Abstract Locale Manager get current client time zone offset
	 * @return the current time zone offset
	 */
	public String getClientTzOffset() {
		return this.clientTzOffset;
	}

	/**
	 * Abstract Locale Manager setter for the client time zones offset
	 * @param clientTzOffset to be set
	 */
	public void setClientTzOffset(String clientTzOffset) {
		this.clientTzOffset = clientTzOffset;
	}

}
