package dk.eazyit.eazyregnskab.web.components.navigation.menu;

import dk.eazyit.eazyregnskab.web.components.page.LoggedInPage;
import org.reflections.Reflections;

import java.util.*;

/**
 * @author EazyIT
 */
public class MenuSetup {

    public static List<Class<? extends LoggedInPage>> createSubMenuList(Class<? extends LoggedInPage> menuClass) {

        Reflections reflections = new Reflections("dk.eazyit.eazyregnskab.web.app.secure");
        List<Class<? extends LoggedInPage>> annotated = new ArrayList<Class<? extends LoggedInPage>>(reflections.getSubTypesOf(LoggedInPage.class));
        Collections.sort(annotated, new Comparator<Class<? extends LoggedInPage>>() {
            @Override
            public int compare(Class<? extends LoggedInPage> aClass, Class<? extends LoggedInPage> aClass2) {
                Integer integer = ((MenuPosition) aClass.getAnnotation(MenuPosition.class)).subLevel();
                Integer integer2 = ((MenuPosition) aClass2.getAnnotation(MenuPosition.class)).subLevel();
                return integer.compareTo(integer2);
            }
        });

        List<Class<? extends LoggedInPage>> classes = new LinkedList<Class<? extends LoggedInPage>>();
        for (Class<? extends LoggedInPage> _class : annotated) {
            MenuPosition menuPosition = (MenuPosition) _class.getAnnotation(MenuPosition.class);
            if (menuPosition.parentPage() == menuClass) {
                classes.add(_class);
            }
        }
        return classes;
    }

    public static List<Class<? extends LoggedInPage>> GetTopLevelList() {

        Reflections reflections = new Reflections("dk.eazyit.eazyregnskab.web.app.secure");

        List<Class<? extends LoggedInPage>> annotated = new ArrayList<Class<? extends LoggedInPage>>(reflections.getSubTypesOf(LoggedInPage.class));

        Collections.sort(annotated, new Comparator<Class<? extends LoggedInPage>>() {
            @Override
            public int compare(Class<? extends LoggedInPage> aClass, Class<? extends LoggedInPage> aClass2) {
                Integer integer = ((MenuPosition) aClass.getAnnotation(MenuPosition.class)).topLevel();
                Integer integer2 = ((MenuPosition) aClass2.getAnnotation(MenuPosition.class)).topLevel();
                return integer.compareTo(integer2);
            }
        });

        List<Class<? extends LoggedInPage>> classes = new LinkedList<Class<? extends LoggedInPage>>();
        for (Class<? extends LoggedInPage> _class : annotated) {
            MenuPosition menuPosition = (MenuPosition) _class.getAnnotation(MenuPosition.class);
            if (menuPosition.topLevelPage()) {
                classes.add(_class);
            }
        }
        return classes;
    }
}
