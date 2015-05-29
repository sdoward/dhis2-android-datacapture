/*
 * Copyright (c) 2015, University of Oslo
 *
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.dhis2.mobile.sdk.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.dhis2.mobile.sdk.persistence.database.DhisDatabase;

import java.util.ArrayList;
import java.util.List;

@Table(databaseName = DhisDatabase.NAME)
public final class CategoryCombo extends BaseIdentifiableObject {
    @JsonProperty("displayName") @Column String displayName;
    @JsonProperty("dimensionType") @Column String dimensionType;
    @JsonProperty("skipTotal") @Column boolean skipTotal;
    @JsonProperty("categoryOptionCombos") List<CategoryOptionCombo> categoryOptionCombos;
    @JsonProperty("categories") List<Category> categories;

    public CategoryCombo() {
    }

    @JsonProperty
    public String getDisplayName() {
        return displayName;
    }

    @JsonProperty
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @JsonProperty
    public boolean isSkipTotal() {
        return skipTotal;
    }

    @JsonProperty
    public void setSkipTotal(boolean skipTotal) {
        this.skipTotal = skipTotal;
    }

    @JsonProperty
    public List<CategoryOptionCombo> getCategoryOptionCombos() {
        return categoryOptionCombos;
    }

    @JsonProperty
    public void setCategoryOptionCombos(List<CategoryOptionCombo> categoryOptionCombos) {
        this.categoryOptionCombos = categoryOptionCombos;
    }

    @JsonProperty
    public List<Category> getCategories() {
        return categories;
    }

    @JsonProperty
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @JsonProperty
    public String getDimensionType() {
        return dimensionType;
    }

    @JsonProperty
    public void setDimensionType(String dimensionType) {
        this.dimensionType = dimensionType;
    }

    public static List<Category> getRelatedCategoriesFromDb(String comboId) {
        List<CategoryComboToCategoryRelation> relations = new Select()
                .from(CategoryComboToCategoryRelation.class)
                .where(Condition.column(CategoryComboToCategoryRelation$Table
                        .CATEGORYCOMBO_CATEGORYCOMBO).is(comboId))
                .queryList();
        List<Category> categories = new ArrayList<>();
        if (relations != null && !relations.isEmpty()) {
            for (CategoryComboToCategoryRelation relation : relations) {
                categories.add(relation.getCategory());
            }
        }
        return categories;
    }
}