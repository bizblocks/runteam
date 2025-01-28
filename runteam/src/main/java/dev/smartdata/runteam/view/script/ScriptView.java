package dev.smartdata.runteam.view.script;


import com.vaadin.flow.router.Route;
import dev.smartdata.runteam.app.ViewRegistryTools;
import groovy.lang.GroovyClassLoader;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.component.codeeditor.CodeEditor;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "script-view", layout = DefaultMainViewParent.class)
@ViewController(id = "rt_ScriptView")
@ViewDescriptor(path = "script-view.xml")
public class ScriptView extends StandardView {
    @Autowired
    protected DialogWindows dialogWindows;
    @Autowired
    protected ViewRegistryTools viewRegistryTools;
    @ViewComponent
    private CodeEditor controller;
    @ViewComponent
    private CodeEditor descriptor;

    @Subscribe("run")
    public void onRun(final ActionPerformedEvent event) {
        GroovyClassLoader loader = new GroovyClassLoader();
        Class viewClass = loader.parseClass(generateGroovyClass());
        viewRegistryTools.registerView("ext-view", new ViewInfo("ext-view", viewClass.getName(), viewClass, descriptor.getValue()));
        dialogWindows.view(this, viewClass)
                .open();
    }

    protected String generateGroovyClass() {
        StringBuilder sb = new StringBuilder();
        sb.append("package dev.smartdata.runteam.view.scriptable\n");
        sb.append("import dev.smartdata.runteam.view.scriptable.Scriptable\n");
        sb.append("class ScriptView extends Scriptable {\n");
        sb.append(controller.getValue()).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}