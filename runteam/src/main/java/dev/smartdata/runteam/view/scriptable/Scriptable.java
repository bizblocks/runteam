package dev.smartdata.runteam.view.scriptable;


import com.vaadin.flow.router.Route;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "scriptable", layout = DefaultMainViewParent.class)
@ViewController(id = "rt_Scriptable")
@ViewDescriptor(path = "scriptable.xml")
public class Scriptable extends StandardView {
    @Autowired
    private Notifications notifications;

    @Subscribe
    public void onInit(final InitEvent event) {
    }

    protected void showNotification() {
        notifications.create("Ура!").show();
    }
}