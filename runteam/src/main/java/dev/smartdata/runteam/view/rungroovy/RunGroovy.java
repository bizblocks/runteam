package dev.smartdata.runteam.view.rungroovy;


import com.vaadin.flow.router.Route;
import dev.smartdata.runteam.entity.RTGroovyScript;
import io.jmix.core.DataManager;
import io.jmix.core.Messages;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.Dialogs;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.action.DialogAction;
import io.jmix.flowui.component.codeeditor.CodeEditor;
import io.jmix.flowui.component.combobox.EntityComboBox;
import io.jmix.flowui.component.textarea.JmixTextArea;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.action.BaseAction;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private Notifications notifications;
    @Autowired
    private Messages messages;
    @Autowired
    private Dialogs dialogs;
    @ViewComponent
    private EntityComboBox<RTGroovyScript> rtGroovyScript;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private DialogWindows dialogWindows;
    @ViewComponent
    private BaseAction newScript;
    @ViewComponent
    private CollectionLoader<RTGroovyScript> rtGroovyScriptsDl;

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

    @Subscribe("newScript")
    public void onNewScript(final ActionPerformedEvent event) {
        dialogs.createOptionDialog()
                .withText(messages.getMessage("confirmClear"))
                .withActions(
                        new DialogAction(DialogAction.Type.YES).withHandler(e -> clear()),
                        new DialogAction(DialogAction.Type.CANCEL))
                .open();
    }

    protected void clear() {
        rtGroovyScript.setValue(rtGroovyScript.getEmptyValue());
        result.clear();
        code.setValue("return null");
    }

    @Subscribe("saveScript")
    public void onSaveScript(final ActionPerformedEvent event) {
        RTGroovyScript script;
        if (rtGroovyScript.getValue() != null) {
            script = rtGroovyScript.getValue();
            dataManager.save(script);
        } else {
            dialogWindows.detail(this, RTGroovyScript.class)
                    .newEntity()
                    .withField(rtGroovyScript)
                    .withInitializer(newScript -> newScript.setScript(code.getValue()))
                    .withAfterCloseListener(closeEvent -> rtGroovyScriptsDl.load())
                    .open();
        }
    }

    @Subscribe("removeScript")
    public void onRemoveScript(final ActionPerformedEvent event) {
        if (rtGroovyScript.getValue() == null)
            return;
        dialogs.createOptionDialog()
                .withText(messages.getMessage("confirmRemove"))
                .withActions(
                        new DialogAction(DialogAction.Type.YES).withHandler(e -> {
                            dataManager.remove(rtGroovyScript.getValue());
                            rtGroovyScriptsDl.load();
                        }),
                        new DialogAction(DialogAction.Type.CANCEL)
                )
                .open();
    }

    @Subscribe("loadScript")
    public void onLoadScript(final ActionPerformedEvent event) {
        if(rtGroovyScript.getValue() == null)
            return;
        result.clear();
        code.setValue(rtGroovyScript.getValue().getScript());
    }

    @Subscribe("editScript")
    public void onEditScript(final ActionPerformedEvent event) {
        if(rtGroovyScript.getValue() == null)
            return;
        dialogWindows.detail(rtGroovyScript)
                .withAfterCloseListener(closeEvent -> rtGroovyScriptsDl.load())
                .open();
    }
}