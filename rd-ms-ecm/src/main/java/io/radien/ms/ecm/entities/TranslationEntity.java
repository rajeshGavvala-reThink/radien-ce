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
package io.radien.ms.ecm.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jnosql.artemis.Column;
import org.jnosql.artemis.Embeddable;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * The translation entity class object composed by a language and a description
 *
 * @author andresousa
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Embeddable
public class TranslationEntity {

    @JsonProperty("language")
    @NotNull
    @Column
    private String language;

    @JsonProperty("description")
    @Column
    private String description;

    /**
     * Translation entity language getter method
     * @return the translation entity language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Translation entity language setter method
     * @param language to be set
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Translation entity description getter
     * @return the translation entity description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Translation entity description setter
     * @param description to be set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * The equals method implements an equivalence relation on non-null object references:
     * @param o the reference object with which to compare.
     * @return if this object is the same as the obj argument; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TranslationEntity that = (TranslationEntity) o;
        return language.equals(that.language) && description.equals(that.description);
    }

    /**
     * Returns a hash code value for the object. This method is supported for the benefit of hash tables such
     * as those provided by HashMap.
     * The general contract of hashCode is:
     * Whenever it is invoked on the same object more than once during an execution of a Java application,
     * the hashCode method must consistently return the same integer, provided no information used in equals
     * comparisons on the object is modified. This integer need not remain consistent from one execution of an
     * application to another execution of the same application.
     * If two objects are equal according to the equals(Object) method, then calling the hashCode method on each of
     * the two objects must produce the same integer result.
     * It is not required that if two objects are unequal according to the equals(java.lang.Object) method, then
     * calling the hashCode method on each of the two objects must produce distinct integer results. However, the
     * programmer should be aware that producing distinct integer results for unequal objects may improve the
     * performance of hash tables.
     * As much as is reasonably practical, the hashCode method defined by class Object does return distinct integers
     * for distinct objects. (This is typically implemented by converting the internal address of the object into an
     * integer, but this implementation technique is not required by the JavaTM programming language.)
     * @return hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(language, description);
    }
}
