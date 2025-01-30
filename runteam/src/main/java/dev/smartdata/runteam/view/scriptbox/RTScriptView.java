package dev.smartdata.runteam.view.scriptbox;

import dev.smartdata.runteam.entity.RTScript;
import io.jmix.flowui.view.StandardView;

abstract public class RTScriptView extends StandardView {
    abstract public void initNewItem(RTScript newScript);
}
