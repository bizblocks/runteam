package dev.smartdata.runteam.app;

import io.jmix.flowui.view.ViewInfo;
import io.jmix.flowui.view.ViewRegistry;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component("rt_ViewRegistryTools")
public class ViewRegistryTools extends ViewRegistry {
    public static final String SCRIPTABLE_VIEW_GENERATED = "scriptable-view-generated";

    @Override
    public void registerView(String id, ViewInfo viewInfo) {
        if (SCRIPTABLE_VIEW_GENERATED.equals(id))
            views.put(id, viewInfo);
        else
            super.registerView(id, viewInfo);
    }
}