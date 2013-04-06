package dk.eazyit.eazyregnskab.web.components.navigation.menu;

import dk.eazyit.eazyregnskab.web.components.navigation.LabeledLink;
import dk.eazyit.eazyregnskab.web.components.page.LoggedInPage;
import org.reflections.Reflections;

import java.util.*;

/**
 * @author EazyIT
 */
public class MenuSetup {

    public static List<LabeledLink> createSideMenuList(Class<? extends LoggedInPage> menuClass) {

        List<LabeledLink> links = new LinkedList<LabeledLink>();
        Reflections reflections = new Reflections("dk.eazyit.eazyregnskab.web.app.secure");
        List<Class<? extends LoggedInPage>> annotated = new ArrayList<Class<? extends LoggedInPage>>(reflections.getSubTypesOf(LoggedInPage.class));

        Collections.sort(annotated, new Comparator<Class<? extends LoggedInPage>>() {
            @Override
            public int compare(Class<? extends LoggedInPage> o, Class<? extends LoggedInPage> o2) {
                return (Integer.valueOf(o.getAnnotation(MenuPosition.class).subLevel()).compareTo(Integer.valueOf(o2.getAnnotation(MenuPosition.class).subLevel())));
            }
        });

        for (Class<? extends LoggedInPage> _class : annotated) {
            MenuPosition menuPosition = _class.getAnnotation(MenuPosition.class);
            if (menuPosition.parentPage() == menuClass) {
                links.add(new LabeledLink(menuPosition.name(), _class));
            }
        }

        return links;
    }

}
