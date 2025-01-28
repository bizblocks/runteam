package dev.smartdata.runteam.app;

import io.jmix.flowui.view.ViewInfo;
import io.jmix.flowui.view.ViewRegistry;
import org.springframework.stereotype.Component;

@Component("rt_ViewRegistryTools")
public class ViewRegistryTools extends ViewRegistry {
    @Override
    public void registerView(String id, ViewInfo viewInfo) {
        super.registerView(id, viewInfo);
    }
}