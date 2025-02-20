package dev.smartdata.runteam.view.runjpql;


import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import dev.smartdata.runteam.app.JpqlTools;
import dev.smartdata.runteam.entity.RTScript;
import dev.smartdata.runteam.view.scriptbox.RTScriptView;
import dev.smartdata.runteam.view.scriptbox.ScriptBox;
import io.jmix.core.DataManager;
import io.jmix.core.Entity;
import io.jmix.core.FluentValuesLoader;
import io.jmix.core.Metadata;
import io.jmix.core.entity.KeyValueEntity;
import io.jmix.core.entity.annotation.SystemLevel;
import io.jmix.core.metamodel.model.MetaProperty;
import io.jmix.data.entity.ReferenceToEntity;
import io.jmix.flowui.component.codeeditor.CodeEditor;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.component.textarea.JmixTextArea;
import io.jmix.flowui.data.grid.ContainerDataGridItems;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.*;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Route(value = "run-jpql", layout = DefaultMainViewParent.class)
@ViewController(id = "rt_RunJpql")
@ViewDescriptor(path = "run-jpql.xml")
public class RunJpql extends RTScriptView {
    @Autowired
    protected DataManager dataManager;
    @ViewComponent
    protected CodeEditor code;
    @ViewComponent
    protected JmixTextArea textResult;
    @Autowired
    private JpqlTools jpqlTools;
    @Autowired
    private DataComponents dataComponents;
    @ViewComponent
    private HorizontalLayout tableButtons;
    @ViewComponent
    private DataGrid<KeyValueEntity> result;
    @ViewComponent
    private DataGrid<ReferenceToEntity> entityResult;
    @Autowired
    private Metadata metadata;
    @ViewComponent
    private JmixButton btnExport;

    @Subscribe("run")
    public void onRun(final ActionPerformedEvent event) {
        String queryString = code.getValue();
        try {
            result.setVisible(true);
            generateTable(queryString);
        } catch (Exception e) {
            result.setVisible(false);
            textResult.setValue(e.toString());
        }
        tableButtons.setVisible(result.isVisible() || entityResult.isVisible());
        textResult.setVisible(!(result.isVisible() || entityResult.isVisible()));
    }

    @SuppressWarnings("rawtypes")
    protected void generateTable(String queryString) throws Exception {
        List<Class> properties = jpqlTools.getQueryProperties(queryString);
        if (properties.isEmpty())
            throw new Exception("No results");
        result.removeAllColumns();

        if (properties.size() == 1 && (Entity.class.isAssignableFrom(properties.get(0)))) {
            setupEntityResult(queryString, properties);
        } else {
            setupResult(queryString, properties);
        }
    }

    protected void setupResult(String queryString, List<Class> properties) {
        btnExport.setAction(result.getAction("export"));
        result.setVisible(true);
        entityResult.setVisible(false);
        KeyValueCollectionContainer container = dataComponents.createKeyValueCollectionContainer();
        List<String> columns = new ArrayList<>();
        properties.forEach(p -> {
            String col = "" + columns.size();
            container.addProperty(col, p);
            result.addColumn(col, Objects.requireNonNull(container.getEntityMetaClass().getPropertyPath(col)));
            columns.add(col);
        });
        FluentValuesLoader loader = dataManager.loadValues(queryString)
                .properties(columns);

        container.setItems(loader.list());
        result.setItems(new ContainerDataGridItems<>(container));
    }

    protected void setupEntityResult(String queryString, List<Class> properties) {
        btnExport.setAction(entityResult.getAction("export"));
        result.setVisible(false);
        entityResult.setVisible(true);
        CollectionContainer container = dataComponents.createCollectionContainer(properties.get(0));
        CollectionLoader loader = dataComponents.createCollectionLoader();
        loader.setQuery(queryString);
        loader.setContainer(container);
        loader.setDataContext(getViewData().getDataContext());
        loader.load();
        if (properties.isEmpty())
            return;
        for (MetaProperty property : metadata.getClass(properties.get(0)).getProperties()) {
            if (property.getAnnotatedElement().isAnnotationPresent(SystemLevel.class))
                continue;
            entityResult.addColumn(metadata.getClass(properties.get(0)).getPropertyPath(property.getName()));
        }
        entityResult.setItems(new ContainerDataGridItems<>(container));
    }

    @Subscribe("scriptBox")
    protected void onScriptBoxClear(final ScriptBox.ClearEvent event) {
        clearResult();
        code.setValue("select o from _ o");
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

    protected void clearResult() {
        result.setVisible(false);
        textResult.setVisible(true);
        textResult.setValue(textResult.getEmptyValue());
    }

    @Override
    public void initNewItem(RTScript newScript) {
        newScript.setScript(code.getValue());
    }
}