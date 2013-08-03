package dk.eazyit.eazyregnskab.web.components.panels.action;

import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationMessage;
import dk.eazyit.eazyregnskab.domain.FiscalYear;
import dk.eazyit.eazyregnskab.services.FiscalYearService;
import dk.eazyit.eazyregnskab.util.CalenderUtil;
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

                    fiscalYearService.openFiscalYear(fiscalYear);
                    getSession().success(new NotificationMessage(new ResourceModel("year.opened")).hideAfter(Duration.seconds(DURATION)));
                }
                target.add(getPage());
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisibilityAllowed(!getItem().isOpen());
            }
        });
        addToolTipToComponent(open, "button.open");

        add(close = new LoadingAjaxLink("close") {
            @Override
            public void onClick(AjaxRequestTarget target) {

                FiscalYear fiscalYear = getItem();

                List after = fiscalYearService.findOpenFiscalYearByLegalEntityBeforeDate(fiscalYear.getLegalEntity(), fiscalYear.getStart());

                FiscalYear next;

                if (after.size() > 0) {
                    getSession().error(new NotificationMessage(new ResourceModel("can.only.close.if.previous.closed")).hideAfter(Duration.seconds(DURATION)));
                } else if ((next = fiscalYearService.findNextFiscalYearByLegalEntity(fiscalYear.getLegalEntity(), CalenderUtil.add(fiscalYear.getEnd(), 0, 0, 1))) == null) {
                    getSession().error(new NotificationMessage(new ResourceModel("next.fiscal.year.not.found")).hideAfter(Duration.seconds(DURATION)));
                } else {
                    fiscalYearService.closeFiscalYear(fiscalYear, next.getStart());
                    getSession().success(new NotificationMessage(new ResourceModel("year.closed")).hideAfter(Duration.seconds(DURATION)));
                }
                target.add(getPage());
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisibilityAllowed(getItem().isOpen());
            }
        });
        addToolTipToComponent(close, "button.close");

        getSelect().setVisibilityAllowed(false);

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