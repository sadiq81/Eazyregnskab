package dk.eazyit.eazyregnskab.session;

import dk.eazyit.eazyregnskab.web.components.models.AppUserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author
 */
public class CurrentUser implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(CurrentUser.class);

    public static final String ATTRIBUTE_NAME = CurrentUser.class.getName();

    private AppUserModel appUserModel;

    public AppUserModel getAppUserModel() {
       if (appUserModel != null) LOG.trace("geeting appUserModel " + appUserModel.getObject().toString());
        return appUserModel;
    }

    public void setAppUserModel(AppUserModel appUserModel) {
        LOG.trace("Setting appUserModel " + appUserModel.getObject().toString());
        this.appUserModel = appUserModel;
    }
}
