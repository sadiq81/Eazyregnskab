package dk.eazyit.eazyregnskab.session;

import dk.eazyit.eazyregnskab.web.components.models.AppUserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author
 */
public class CurrentUser implements Serializable {

    private Logger log = LoggerFactory.getLogger(CurrentUser.class);

    public static final String ATTRIBUTE_NAME = CurrentUser.class.getName();

    private AppUserModel appUserModel;

    public AppUserModel getAppUserModel() {
        return appUserModel;
    }

    public void setAppUserModel(AppUserModel appUserModel) {
        this.appUserModel = appUserModel;
    }
}
