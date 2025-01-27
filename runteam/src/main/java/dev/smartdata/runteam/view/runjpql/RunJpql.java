package dev.smartdata.runteam.view.runjpql;


import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import dev.smartdata.runteam.app.JpqlTools;
import io.jmix.core.DataManager;
import io.jmix.core.FluentValuesLoader;
import io.jmix.core.entity.KeyValueEntity;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.component.codeeditor.CodeEditor;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.component.textarea.JmixTextArea;
import io.jmix.flowui.data.grid.ContainerDataGridItems;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.model.DataComponents;
import io.jmix.flowui.model.KeyValueCollectionContainer;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Route(value = "run-jpql", layout = DefaultMainViewParent.class)
@ViewController(id = "rt_RunJpql")
@ViewDescriptor(path = "run-jpql.xml")
public class RunJpql extends StandardView {
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
        tableButtons.setVisible(result.isVisible());
        textResult.setVisible(!result.isVisible());
    }

    @SuppressWarnings("rawtypes")
    private void generateTable(String queryString) throws Exception {
        List<Class> properties = jpqlTools.getQueryProperties(queryString);
        if (properties.isEmpty())
            throw new Exception("No results");
        result.removeAllColumns();

        KeyValueCollectionContainer container = dataComponents.createKeyValueCollectionContainer();
        List<String> columns = new ArrayList<>();
        properties.forEach(p -> {
            String col = "column" + columns.size();
            container.addProperty(col, p);
            result.addColumn(col, Objects.requireNonNull(container.getEntityMetaClass().getPropertyPath(col)));
            columns.add(col);
        });
        FluentValuesLoader loader = dataManager.loadValues(queryString)
                .properties(columns);
        container.setItems(loader.list());
        result.setItems(new ContainerDataGridItems<>(container));
    }
}