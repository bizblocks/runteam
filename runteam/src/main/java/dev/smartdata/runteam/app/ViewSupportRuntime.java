package dev.smartdata.runteam.app;

import io.jmix.core.security.CurrentAuthentication;
import io.jmix.flowui.sys.ViewSupport;
import io.jmix.flowui.sys.ViewXmlLoader;
import io.jmix.flowui.sys.ViewXmlParser;
import io.jmix.flowui.sys.autowire.AutowireManager;
import io.jmix.flowui.view.ViewInfo;
import io.jmix.flowui.view.ViewRegistry;
import io.jmix.flowui.view.navigation.RouteSupport;
import io.jmix.flowui.view.navigation.ViewNavigationSupport;
import io.micrometer.core.instrument.MeterRegistry;
import org.dom4j.Element;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Primary
@Component("rt_ViewSupport")
public class ViewSupportRuntime extends ViewSupport {
    public ViewSupportRuntime(ApplicationContext applicationContext, ViewXmlLoader viewXmlLoader, ViewRegistry viewRegistry, ViewNavigationSupport navigationSupport, CurrentAuthentication currentAuthentication, AutowireManager autowireManager, RouteSupport routeSupport, MeterRegistry meterRegistry) {
        super(applicationContext, viewXmlLoader, viewRegistry, navigationSupport, currentAuthentication, autowireManager, routeSupport, meterRegistry);
    }

    @Override
    protected Element loadViewXml(ViewInfo viewInfo) {
        Optional<String> templatePath = viewInfo.getTemplatePath();
        ViewXmlParser parser = new ViewXmlParser();

        return templatePath.map(s -> s.contains("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")?
                parser.parseDescriptor(s).getDocument().getRootElement() :
                viewXmlLoader.load(s)).orElse(null);
    }
}