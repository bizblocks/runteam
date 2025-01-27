package dev.smartdata.runteam.view.rungroovy;


import com.vaadin.flow.router.Route;
import io.jmix.flowui.component.codeeditor.CodeEditor;
import io.jmix.flowui.component.textarea.JmixTextArea;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.view.*;
import org.springframework.scripting.groovy.GroovyScriptEvaluator;
import org.springframework.scripting.support.StaticScriptSource;

@Route(value = "run-groovy", layout = DefaultMainViewParent.class)
@ViewController(id = "rt_RunGroovy")
@ViewDescriptor(path = "run-groovy.xml")
public class RunGroovy extends StandardView {

    protected GroovyScriptEvaluator engine;
    @ViewComponent
    private CodeEditor code;
    @ViewComponent
    private JmixTextArea result;

    @Subscribe("run")
    public void onRun(final ActionPerformedEvent event) {
        result.clear();
        try {
            String res = engine.evaluate(new StaticScriptSource(code.getValue())).toString();
            result.setValue(res);
        } catch (Exception e) {
            result.setValue(e.toString());
        }

    }

    @Subscribe
    public void onInit(final InitEvent event) {
        engine = new GroovyScriptEvaluator();
    }
}