package dev.smartdata.runteam.view.scriptable;


import com.vaadin.flow.router.Route;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.view.*;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "scriptable", layout = DefaultMainViewParent.class)
@ViewController(id = "rt_Scriptable")
@ViewDescriptor(path = "scriptable.xml")
@RolesAllowed("system-full-access")
public class ScriptableView extends StandardView {
    @Autowired
    private Notifications notifications;

    @Subscribe
    public void onInit(final InitEvent event) {
    }

    protected void showNotification() {
        notifications.create("Ура!").show();
    }
}