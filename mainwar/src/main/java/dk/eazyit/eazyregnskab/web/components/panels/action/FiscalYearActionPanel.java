package dk.eazyit.eazyregnskab.web.components.panels.action;

import dk.eazyit.eazyregnskab.domain.FiscalYear;
import dk.eazyit.eazyregnskab.web.components.form.BaseCreateEditForm;
import dk.eazyit.eazyregnskab.web.components.models.entities.FiscalYearModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
public class FiscalYearActionPanel extends ActionPanel<FiscalYear> {

    BaseCreateEditForm<FiscalYear> form;

    private static final Logger LOG = LoggerFactory.getLogger(FiscalYearActionPanel.class);

    /**
     * @param id    component id
     * @param model model for contact
     */
    public FiscalYearActionPanel(String id, IModel<FiscalYear> model, BaseCreateEditForm<FiscalYear> form) {
        super(id, model);
        this.form = form;
        add(new AjaxLink("open") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                FiscalYear fiscalYear = getItem();
                target.add(getPage());
            }
        });
        add(new AjaxLink("close") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                FiscalYear fiscalYear = getItem();
                target.add(getPage());
            }
        });

    }

    @Override
    protected BaseCreateEditForm<FiscalYear> selectItem(FiscalYear fiscalYear) {
        LOG.debug("Selected item " + fiscalYear.toString());
        form.setDefaultModel(new CompoundPropertyModel<FiscalYear>(new FiscalYearModel(fiscalYear)));
        return form;
    }

    @Override
    protected void deleteItem(FiscalYear fiscalYear) {
        form.deleteEntity(fiscalYear);
    }
}