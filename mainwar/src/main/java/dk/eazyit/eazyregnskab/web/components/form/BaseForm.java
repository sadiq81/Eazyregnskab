package dk.eazyit.eazyregnskab.web.components.form;

import dk.eazyit.eazyregnskab.domain.AppUser;
import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.domain.EntityWithLongId;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.session.EazyregnskabSesssion;
import dk.eazyit.eazyregnskab.session.SessionAware;
import dk.eazyit.eazyregnskab.web.components.resource.JasperPdfReportsResource;
import dk.eazyit.eazyregnskab.web.components.resource.JasperXlsReportsResource;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author
 */
public abstract class BaseForm<T extends Serializable> extends Form<T> implements SessionAware {

    public static final String legal_entity_name = "QUERY_LEGAL_ENTITY_NAME";
    public static final String legal_entity_id = "QUERY_LEGAL_ENTITY_ID";
    public static final String appuser = "QUERY_APPUSER";
    public static final String locale = "REPORT_LOCALE";

    private static final Logger LOG = LoggerFactory.getLogger(BaseForm.class);

    protected BaseForm(String id) {
        super(id);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    protected BaseForm(String id, IModel<T> model) {
        super(id, model);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and model " + model);
    }

    public AppUser getCurrentUser() {
        return ((EazyregnskabSesssion) Session.get()).getCurrentUser();
    }

    public LegalEntity getCurrentLegalEntity() {
        return ((EazyregnskabSesssion) Session.get()).getCurrentLegalEntity();
    }

    public void setCurrentLegalEntity(LegalEntity legalEntity) {
        ((EazyregnskabSesssion) Session.get()).setCurrentLegalEntity(legalEntity);
    }

    public DailyLedger getCurrentDailyLedger() {
        return ((EazyregnskabSesssion) Session.get()).getCurrentDailyLedger();
    }

    public void setCurrentDailyLedger(DailyLedger dailyLedger) {
        ((EazyregnskabSesssion) Session.get()).setCurrentDailyLedger(dailyLedger);
    }

    protected void addToolTipToComponent(Component component, String resourceText) {
        component.add(AttributeModifier.append("rel", "tooltip"));
        component.add(AttributeModifier.append("data-placement", "top"));
        component.add(AttributeModifier.append("data-original-title", new ResourceModel(resourceText)));
    }

    protected void removeToolTipToComponent(Component component) {
        component.add(AttributeModifier.remove("rel"));
        component.add(AttributeModifier.remove("data-placement"));
        component.add(AttributeModifier.remove("data-original-title"));
    }

    protected String getJasperReportName() {
        return getPage().getPageClass().getSimpleName() + ".jasper";
    }

    public ResourceLink getJasperPdfResourceLink(String id, String reportName, String filename) {

        JasperPdfReportsResource jr = new JasperPdfReportsResource(reportName, filename, exportWithBeans()) {
            @Override
            protected HashMap<String, Object> getParametersForReport() {
                return getFormParametersForReport();
            }

            @Override
            protected EntityWithLongId[] getBeanArray() {
                return getCollectionForReport();
            }
        };
        return new ResourceLink(id, jr);
    }

    public ResourceLink getJasperXlsResourceLink(String id, String reportName, String filename) {

        JasperXlsReportsResource jr = new JasperXlsReportsResource(reportName, filename, exportWithBeans()) {
            @Override
            protected HashMap<String, Object> getParametersForReport() {
                return getFormParametersForReport();
            }

            @Override
            protected EntityWithLongId[] getBeanArray() {
                return getCollectionForReport();
            }
        };
        return new ResourceLink(id, jr);
    }

    protected abstract void addReports();

    protected boolean exportWithBeans() {
        return false;
    }

    protected EntityWithLongId[] getCollectionForReport() {
        if (exportWithBeans()) throw new NullPointerException("Method should be overriden");
        return new EntityWithLongId[0];
    }

    protected HashMap<String, Object> getFormParametersForReport() {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put(legal_entity_name, getCurrentLegalEntity().getName());
        parameters.put(legal_entity_id, getCurrentLegalEntity().getId());
        parameters.put(appuser, getCurrentUser().getUsername());
        parameters.put(locale, getSession().getLocale());
        return parameters;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        addReports();
    }
}
