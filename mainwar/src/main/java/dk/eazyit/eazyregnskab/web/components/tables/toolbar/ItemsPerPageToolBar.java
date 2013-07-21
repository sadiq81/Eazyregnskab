package dk.eazyit.eazyregnskab.web.components.tables.toolbar;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author
 */
public class ItemsPerPageToolBar extends AbstractToolbar {

    static List<Long> itemsPerPage = new ArrayList<Long>(Arrays.asList(20L, 50L, 100L, 500L));

    public ItemsPerPageToolBar(DataTable<?, ?> table) {
        super(table);

        WebMarkupContainer td = new WebMarkupContainer("td");
        add(td);

        td.add(AttributeModifier.replace("colspan", new AbstractReadOnlyModel<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String getObject() {
                return String.valueOf(getTable().getColumns().size());
            }
        }));
        td.add(new Label("itemsPerPageLabel", new ResourceModel("items.per.page")));
        td.add(new DropDownChoice<Long>("itemsPerPage", new ItemsPerPageModel(), itemsPerPage).add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(getTable());
            }
        }));

    }

    class ItemsPerPageModel implements IModel<Long> {
        @Override
        public Long getObject() {
            return getTable().getItemsPerPage();
        }

        @Override
        public void setObject(Long object) {
            getTable().setItemsPerPage(object);
        }

        @Override
        public void detach() {
        }
    }
}
