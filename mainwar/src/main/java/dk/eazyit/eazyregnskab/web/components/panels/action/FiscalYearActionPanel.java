package dk.eazyit.eazyregnskab.web.components.panels.action;

import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationMessage;
import dk.eazyit.eazyregnskab.domain.FiscalYear;
import dk.eazyit.eazyregnskab.domain.FiscalYearStatus;
import dk.eazyit.eazyregnskab.services.FiscalYearService;
import dk.eazyit.eazyregnskab.web.components.form.BaseCreateEditForm;
import dk.eazyit.eazyregnskab.web.components.link.LoadingAjaxLink;
import dk.eazyit.eazyregnskab.web.components.models.entities.FiscalYearModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author
 */
public class FiscalYearActionPanel extends ActionPanel<FiscalYear> {

    @SpringBean
    FiscalYearService fiscalYearService;

    BaseCreateEditForm<FiscalYear> form;
    LoadingAjaxLink open;
    LoadingAjaxLink close;

    private static final Logger LOG = LoggerFactory.getLogger(FiscalYearActionPanel.class);

    /**
     * @param id    component id
     * @param model model for contact
     */
    public FiscalYearActionPanel(String id, IModel<FiscalYear> model, BaseCreateEditForm<FiscalYear> form) {
        super(id, model);
        this.form = form;
        add(open = new LoadingAjaxLink("open") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                FiscalYear fiscalYear = getItem();
                List after = fiscalYearService.findLockedFiscalYearByLegalEntityAfterDate(fiscalYear.getLegalEntity(), fiscalYear.getEnd());

                if (after.size() > 0) {
                    getSession().error(new NotificationMessage(new ResourceModel("can.only.open.if.next.open")).hideAfter(Duration.seconds(DURATION)));
                } else {

                    fiscalYear.setFiscalYearStatus(FiscalYearStatus.OPEN);
                    fiscalYearService.save(fiscalYear);
                    getSession().success(new NotificationMessage(new ResourceModel("year.opened")).hideAfter(Duration.seconds(DURATION)));
                }
                target.add(getPage());
            }
        });
        addToolTipToComponent(open, "button.open");
        add(close = new LoadingAjaxLink("close") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                FiscalYear fiscalYear = getItem();
                List after = fiscalYearService.findOpenFiscalYearByLegalEntityBeforeDate(fiscalYear.getLegalEntity(), fiscalYear.getStart());

                if (after.size() > 0) {
                    getSession().error(new NotificationMessage(new ResourceModel("can.only.close.if.previous.closed")).hideAfter(Duration.seconds(DURATION)));
                } else {
                    fiscalYear.setFiscalYearStatus(FiscalYearStatus.LOCKED);
                    fiscalYearService.save(fiscalYear);
                    getSession().success(new NotificationMessage(new ResourceModel("year.closed")).hideAfter(Duration.seconds(DURATION)));
                }
                target.add(getPage());
            }
        });
        addToolTipToComponent(close,"button.close");

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