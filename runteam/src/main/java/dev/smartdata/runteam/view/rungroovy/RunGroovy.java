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

    @Subscribe("run")
    public void onRun(final ActionPerformedEvent event) {
        result.clear();
        try {
            Object res = engine.evaluate(new StaticScriptSource(code.getValue()));
            String str = "null";
            if (res != null)
                str = res.toString();
            result.setValue(str);
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
        result.clear();
        code.setValue("return null");
    }

    @Subscribe("scriptBox")
    protected void onScriptBoxLoad(final ScriptBox.LoadEvent event) {
        result.clear();
        //FIXME:обход проблемы CodeEditor
        code.getElement().callJsFunction("_onValueChange", code.getValue());
    }

    @Subscribe("scriptBox")
    protected void onScriptBoxRemove(final ScriptBox.RemoveEvent event) {
        result.clear();
        code.setValue("select o from _ o");
    }

//    @Subscribe("saveScript")
//    public void onSaveScript(final ActionPerformedEvent event) {
//        RTGroovyScript script;
//        if (rtGroovyScript.getValue() != null) {
//            script = rtGroovyScript.getValue();
//            dataManager.save(script);
//        } else {
//            dialogWindows.detail(this, RTGroovyScript.class)
//                    .newEntity()
//                    .withField(rtGroovyScript)
//                    .withInitializer(newScript -> newScript.setScript(code.getValue()))
//                    .withAfterCloseListener(closeEvent -> rtGroovyScriptsDl.load())
//                    .open();
//        }
//    }


    @Override
    public void initNewItem(RTScript newScript) {
        newScript.setScript(code.getValue());
    }
}