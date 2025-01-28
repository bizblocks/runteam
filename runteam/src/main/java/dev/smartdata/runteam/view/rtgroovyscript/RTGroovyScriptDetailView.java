package dev.smartdata.runteam.view.rtgroovyscript;

import com.vaadin.flow.router.Route;
import dev.smartdata.runteam.entity.RTGroovyScript;
import io.jmix.flowui.view.*;

@Route(value = "rTGroovyScripts/:id", layout = DefaultMainViewParent.class)
@ViewController(id = "rt_RTGroovyScript.detail")
@ViewDescriptor(path = "rt-groovy-script-detail-view.xml")
@EditedEntityContainer("rTGroovyScriptDc")
public class RTGroovyScriptDetailView extends StandardDetailView<RTGroovyScript> {
}