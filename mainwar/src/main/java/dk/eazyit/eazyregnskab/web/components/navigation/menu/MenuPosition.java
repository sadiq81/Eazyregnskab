package dk.eazyit.eazyregnskab.web.components.navigation.menu;

import dk.eazyit.eazyregnskab.web.components.page.LoggedInPage;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Trifork
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface MenuPosition {

    String name();

    Class<? extends LoggedInPage> parentPage();

    int subLevel();

    boolean topLevelPage();

    int topLevel() default 0;

    //  boolean newButtonHide();

}
