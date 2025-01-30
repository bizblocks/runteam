package dev.smartdata.runteam.view.rtjpqlscript;

import com.vaadin.flow.router.Route;
import dev.smartdata.runteam.entity.RTJPQLScript;
import io.jmix.flowui.view.*;

@Route(value = "rtJPQLScripts/:id", layout = DefaultMainViewParent.class)
@ViewController(id = "rt_RTJPQLScript.detail")
@ViewDescriptor(path = "jpql-script-detail-view.xml")
@EditedEntityContainer("rTJPQLScriptDc")
public class RTJPQLScriptDetailView extends StandardDetailView<RTJPQLScript> {
}