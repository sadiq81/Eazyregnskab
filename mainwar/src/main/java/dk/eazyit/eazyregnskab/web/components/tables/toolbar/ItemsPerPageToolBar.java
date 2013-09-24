package dk.eazyit.eazyregnskab.web.components.tables.toolbar;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
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
public class ItemsPerPageToolBar extends SessionAwareToolbar {

    static List<Integer> itemsPerPage = new ArrayList<Integer>(Arrays.asList(20, 50, 100, 500, Integer.MAX_VALUE));

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
        td.add(new DropDownChoice<>("itemsPerPage", new ItemsPerPageModel(), itemsPerPage).add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(getTable());
            }
        }));

    }

    class ItemsPerPageModel implements IModel<Integer> {
        @Override
        public Integer getObject() {
            return getCurrentUser().getItemsPerPage();
        }

        @Override
        public void setObject(Integer object) {
            getTable().setItemsPerPage(object);
            if (!object.equals(getCurrentUser().getItemsPerPage())) {
                getCurrentUser().setItemsPerPage(object);
                loginService.saveUser(getCurrentUser());
            }
        }

        @Override
        public void detach() {
        }
    }

    @Override
    public boolean isVisible() {
        return getTable().getRowCount() > 20;
    }
}
