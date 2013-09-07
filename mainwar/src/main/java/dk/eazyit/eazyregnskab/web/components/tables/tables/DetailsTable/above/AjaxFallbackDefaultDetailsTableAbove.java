/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.eazyit.eazyregnskab.web.components.tables.tables.detailsTable.above;

import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackHeadersToolbar;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxNavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NoRecordsToolbar;
import org.apache.wicket.markup.repeater.data.IDataProvider;

import java.util.List;

/**
 * An implementation of the DetailsTableBelow that aims to solve the 90% usecase by adding navigation,
 * headers, an no-records-found toolbars to a standard {@link dk.eazyit.eazyregnskab.web.components.tables.tables.detailsTable.below.DetailsTableBelow}.
 * <p/>
 * The {@link org.apache.wicket.extensions.markup.html.repeater.data.table.NavigationToolbar} and the {@link org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar} are added as top toolbars, while the
 * {@link org.apache.wicket.extensions.markup.html.repeater.data.table.NoRecordsToolbar} toolbar is added as a bottom toolbar.
 *
 * @param <T> The model object type
 * @param <S> The type of the sorting parameter
 * @author William Speirs (wspeirs)
 * @see org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable
 * @see org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar
 * @see org.apache.wicket.extensions.markup.html.repeater.data.table.NavigationToolbar
 * @see org.apache.wicket.extensions.markup.html.repeater.data.table.NoRecordsToolbar
 */
public class AjaxFallbackDefaultDetailsTableAbove<T, S> extends DetailsTableAbove<T, S> {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     *
     * @param id           component id
     * @param columns      list of columns
     * @param dataProvider data provider
     * @param rowsPerPage  number of rows per page
     */
    public AjaxFallbackDefaultDetailsTableAbove(final String id, final List<IColumn<T, S>> columns, final IDataProvider<T> dataProvider, final int rowsPerPage) {
        super(id, columns, dataProvider, rowsPerPage);
        setOutputMarkupId(true);
        setVersioned(false);
        addTopToolbar(new AjaxNavigationToolbar(this));

        if (dataProvider instanceof ISortableDataProvider) {
            addTopToolbar(new AjaxFallbackHeadersToolbar(this, (ISortableDataProvider) dataProvider));
        } else {
            addTopToolbar(new HeadersToolbar(this, null));
        }

        addBottomToolbar(new NoRecordsToolbar(this));
    }

}
