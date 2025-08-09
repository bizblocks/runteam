package dev.smartdata.runteam.view.scriptbox;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;
import dev.smartdata.runteam.entity.RTScript;
import io.jmix.core.DataManager;
import io.jmix.core.Id;
import io.jmix.core.Messages;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.Dialogs;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.action.DialogAction;
import io.jmix.flowui.component.combobox.EntityComboBox;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.action.BaseAction;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@FragmentDescriptor("script-box.xml")
public class ScriptBox extends Fragment<VerticalLayout> {
    @ViewComponent
    private EntityComboBox<Object> rtScript;
    @ViewComponent
    private CollectionContainer<RTScript> rtScriptsDc;
    @Autowired
    private Dialogs dialogs;
    @Autowired
    private Messages messages;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private DialogWindows dialogWindows;
    @Autowired
    private Notifications notifications;
    @ViewComponent
    private CollectionLoader<? extends RTScript> rtScriptsDl;
    @ViewComponent
    private BaseAction saveScript;

    @Subscribe(target = Target.HOST_CONTROLLER)
    public void onHostInit(final View.InitEvent event) {
        rtScript.setMetaClass(rtScriptsDc.getEntityMetaClass());
    }

    @Subscribe(target = Target.HOST_CONTROLLER)
    public void onHostReady(final View.ReadyEvent event) {
        getFragmentData().loadAll();
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
        rtScript.setValue(rtScript.getEmptyValue());
        fireEvent(new ClearEvent(this, true));
    }

    @Subscribe("saveScript")
    public void onSaveScript(final ActionPerformedEvent event) {
        RTScript script;
        if (rtScript.getValue() != null) {
            script = rtScriptsDc.getItem();
            rtScript.setValue(dataManager.save(script));
            fireEvent(new SaveEvent(this, true));
        } else {
            dialogWindows.detail((View<?>) getParentController(), rtScriptsDc.getEntityMetaClass().getJavaClass())
                    .newEntity()
                    .withInitializer(newScript -> {
                        initializeRTScript((RTScript) newScript);
                        rtScript.setValue(newScript);
                    })
                    .withAfterCloseListener(closeEvent -> {
                        getFragmentData().loadAll();
                        fireEvent(new SaveEvent(this, true));
                        if(closeEvent.closedWith(StandardOutcome.SAVE)) {
                            RTScript saveScript = rtScriptsDc.getItem();
                            rtScriptsDl.load();
                            rtScript.setValue(rtScript.getEmptyValue());
                            rtScript.setValue(saveScript);
                        }
                    })
                    .open();
        }
    }

    protected void initializeRTScript(RTScript newScript) {
        if (getParentController() instanceof RTScriptView)
            ((RTScriptView) getParentController()).initNewItem(newScript);
        else
            notifications.create(messages.getMessage("wrongViewAncestor"));
    }

    @Subscribe("removeScript")
    public void onRemoveScript(final ActionPerformedEvent event) {
        if (rtScript.getValue() == null)
            return;
        dialogs.createOptionDialog()
                .withText(messages.getMessage("confirmRemove"))
                .withActions(
                        new DialogAction(DialogAction.Type.YES).withHandler(e -> {
                            dataManager.remove(rtScript.getValue());
                            getFragmentData().loadAll();
                            fireEvent(new RemoveEvent(this, true));
                        }),
                        new DialogAction(DialogAction.Type.CANCEL)
                )
                .open();
    }

    @Subscribe("loadScript")
    public void onLoadScript(final ActionPerformedEvent event) {
        if (rtScript.getValue() == null)
            return;
        if (rtScriptsDl.getDataContext().isModified(rtScript.getValue())) {
            dialogs.createOptionDialog()
                    .withText(messages.getMessage("confirmLoad"))
                    .withActions(
                            new DialogAction(DialogAction.Type.YES).withHandler(e -> loadScript()),
                            new DialogAction(DialogAction.Type.CANCEL)
                    ).
                    open();
        } else {
            loadScript();
        }
    }

    protected void loadScript() {
        RTScript script = (RTScript) rtScript.getValue();
        rtScriptsDc.replaceItem(dataManager.load(Id.of(script)).one());
        fireEvent(new LoadEvent(this, true));
    }

    @Subscribe("editScript")
    public void onEditScript(final ActionPerformedEvent event) {
        if (rtScript.getValue() == null)
            return;
        dialogWindows.detail(rtScript)
                .withAfterCloseListener(closeEvent -> getFragmentData().loadAll())
                .open();
    }

    public Registration addClearListener(ComponentEventListener<ClearEvent> listener) {
        return addListener(ClearEvent.class, listener);
    }

    public Registration addLoadListener(ComponentEventListener<LoadEvent> listener) {
        return addListener(LoadEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addEitListener(ComponentEventListener<EditEvent> listener) {
        return addListener(EditEvent.class, listener);
    }

    public Registration addRemoveListener(ComponentEventListener<RemoveEvent> listener) {
        return addListener(RemoveEvent.class, listener);
    }

    public static class ClearEvent extends ComponentEvent<ScriptBox> {

        /**
         * Creates a new event using the given source and indicator whether the
         * event originated from the client side or the server side.
         *
         * @param source     the source component
         * @param fromClient <code>true</code> if the event originated from the client
         *                   side, <code>false</code> otherwise
         */
        public ClearEvent(Component source, boolean fromClient) {
            super((ScriptBox) source, fromClient);
        }
    }

    public static class LoadEvent extends ComponentEvent<ScriptBox> {

        /**
         * Creates a new event using the given source and indicator whether the
         * event originated from the client side or the server side.
         *
         * @param source     the source component
         * @param fromClient <code>true</code> if the event originated from the client
         *                   side, <code>false</code> otherwise
         */
        public LoadEvent(ScriptBox source, boolean fromClient) {
            super(source, fromClient);
        }
    }

    public static class EditEvent extends ComponentEvent<ScriptBox> {
        /**
         * Creates a new event using the given source and indicator whether the
         * event originated from the client side or the server side.
         *
         * @param source     the source component
         * @param fromClient <code>true</code> if the event originated from the client
         *                   side, <code>false</code> otherwise
         */
        public EditEvent(ScriptBox source, boolean fromClient) {
            super(source, fromClient);
        }
    }

    public static class SaveEvent extends ComponentEvent<ScriptBox> {
        /**
         * Creates a new event using the given source and indicator whether the
         * event originated from the client side or the server side.
         *
         * @param source     the source component
         * @param fromClient <code>true</code> if the event originated from the client
         *                   side, <code>false</code> otherwise
         */
        public SaveEvent(ScriptBox source, boolean fromClient) {
            super(source, fromClient);
        }
    }

    public static class RemoveEvent extends ComponentEvent<ScriptBox> {

        /**
         * Creates a new event using the given source and indicator whether the
         * event originated from the client side or the server side.
         *
         * @param source     the source component
         * @param fromClient <code>true</code> if the event originated from the client
         *                   side, <code>false</code> otherwise
         */
        public RemoveEvent(ScriptBox source, boolean fromClient) {
            super(source, fromClient);
        }
    }
}