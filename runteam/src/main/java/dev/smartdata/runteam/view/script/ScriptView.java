package dev.smartdata.runteam.view.script;


import com.vaadin.flow.router.Route;
import dev.smartdata.runteam.app.ViewRegistryTools;
import groovy.lang.GroovyClassLoader;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.component.codeeditor.CodeEditor;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "script-view", layout = DefaultMainViewParent.class)
@ViewController(id = "rt_ScriptView")
@ViewDescriptor(path = "script-view.xml")
public class ScriptView extends StandardView {
    protected static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    @Autowired
    protected DialogWindows dialogWindows;
    @Autowired
    protected ViewRegistryTools viewRegistryTools;
    @ViewComponent
    private CodeEditor controller;
    @ViewComponent
    private CodeEditor descriptor;
    @Autowired
    private CurrentAuthentication currentAuthentication;

    @Subscribe
    public void onReady(final ReadyEvent event) {
        descriptor.setValue("<view xmlns=\"http://jmix.io/schema/flowui/view\"\n" +
                "      title=\"Пример\">\n" +
                "    <layout>\n" +
                "        <nativeLabel text=\"Лабел!!\"/>\n" +
                "    </layout>\n" +
                "</view>");
        controller.setValue("@Override\n" +
                "void onInit(InitEvent event) {\n" +
                "    showNotification()\n" +
                "}");
        currentAuthentication.getUser();
    }

    @Subscribe("run")
    public void onRun(final ActionPerformedEvent event) {
        GroovyClassLoader loader = new GroovyClassLoader();
        Class viewClass = loader.parseClass(generateGroovyClass());
        viewRegistryTools.registerView("scriptable-view-generated", new ViewInfo("scriptable-view-generated", viewClass.getName(), viewClass, generateDescriptor()));
        dialogWindows.view(this, viewClass)
                .open();
    }

    protected String generateGroovyClass() {
        StringBuilder sb = new StringBuilder();
        sb.append("package dev.smartdata.runteam.view.scriptable\n");
        sb.append("import dev.smartdata.runteam.view.scriptable.ScriptableView\n");
        sb.append("import io.jmix.flowui.view.*\n");
        sb.append("@ViewController(id = \"scriptable-view-generated\")\n");
        sb.append("class ScriptableViewGenerated extends ScriptableView {\n");
        sb.append(controller.getValue()).append("\n");
        sb.append("}\n");
        return sb.toString();
    }

    protected String generateDescriptor() {
        StringBuilder sb = new StringBuilder(descriptor.getValue());
        if(!sb.toString().contains(XML_HEADER))
            sb.insert(0, XML_HEADER);
        return sb.toString();
    }
}