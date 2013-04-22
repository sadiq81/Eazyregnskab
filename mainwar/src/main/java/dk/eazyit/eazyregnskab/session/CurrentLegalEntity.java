package dk.eazyit.eazyregnskab.session;

import dk.eazyit.eazyregnskab.web.components.models.LegalEntityModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author
 */
public class CurrentLegalEntity implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(CurrentLegalEntity.class);

    public static final String ATTRIBUTE_NAME = CurrentLegalEntity.class.getName();

    private LegalEntityModel legalEntityModel;

    public LegalEntityModel getLegalEntityModel() {
        if (legalEntityModel != null) LOG.trace("geeting LegalEntityModel " + legalEntityModel.getObject().toString());
        return legalEntityModel;
    }

    public void setLegalEntityModel(LegalEntityModel legalEntityModel) {
        LOG.trace("Setting LegalEntityModel " + legalEntityModel.getObject().toString());
        this.legalEntityModel = legalEntityModel;
    }

}