package dev.smartdata.runteam.view.rungroovy;


import com.vaadin.flow.router.Route;
import dev.smartdata.runteam.entity.RTGroovyScript;
import dev.smartdata.runteam.entity.RTScript;
import dev.smartdata.runteam.view.scriptbox.RTScriptView;
import dev.smartdata.runteam.view.scriptbox.ScriptBox;
import io.jmix.flowui.component.codeeditor.CodeEditor;
import io.jmix.flowui.component.textarea.JmixTextArea;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.*;
import org.springframework.scripting.ScriptCompilationException;
import org.springframework.scripting.groovy.GroovyScriptEvaluator;
import org.springframework.scripting.support.StaticScriptSource;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Route(value = "run-groovy", layout = DefaultMainViewParent.class)
@ViewController(id = "rt_RunGroovy")
@ViewDescriptor(path = "run-groovy.xml")
public class RunGroovy extends RTScriptView {

    protected GroovyScriptEvaluator engine;
    @ViewComponent
    protected CodeEditor code;
    @ViewComponent
    protected JmixTextArea result;
    @ViewComponent
    protected CollectionLoader<RTGroovyScript> rtScriptsDl;
    @ViewComponent
    private JmixTextArea console;

    @Subscribe("run")
    public void onRun(final ActionPerformedEvent event) {
        result.clear();
        try {
            StringWriter console = new StringWriter();
            Map<String, Object> binding = new HashMap<>();
            binding.put("out", console);

            Object res = engine.evaluate(new StaticScriptSource(code.getValue()), binding);
            String str = "null";
            if (res != null)
                str = res.toString();
            result.setValue(str);
            this.console.setValue(console.toString());
        } catch (ScriptCompilationException e) {
            result.setValue(e.getCause().toString());
        }

    }

    @Subscribe
    public void onInit(final InitEvent event) {
        engine = new GroovyScriptEvaluator();
    }

    @Subscribe("scriptBox")
    protected void onScriptBoxClear(final ScriptBox.ClearEvent event) {
        clearResult();
        code.setValue("return null");
    }

    private void clearResult() {
        result.clear();
        console.clear();
    }

    @Subscribe("scriptBox")
    protected void onScriptBoxLoad(final ScriptBox.LoadEvent event) {
        clearResult();
        //FIXME:обход проблемы CodeEditor
        code.getElement().callJsFunction("_onValueChange", code.getValue());
    }

    @Subscribe("scriptBox")
    protected void onScriptBoxRemove(final ScriptBox.RemoveEvent event) {
        clearResult();
        code.setValue("select o from _ o");
    }

    @Override
    public void initNewItem(RTScript newScript) {
        newScript.setScript(code.getValue());
    }
}