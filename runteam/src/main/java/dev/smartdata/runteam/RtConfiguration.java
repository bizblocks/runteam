package dev.smartdata.runteam;

import io.jmix.core.annotation.JmixModule;
import io.jmix.core.impl.scanning.AnnotationScanMetadataReaderFactory;
import io.jmix.eclipselink.EclipselinkConfiguration;
import io.jmix.flowui.FlowuiConfiguration;
import io.jmix.flowui.sys.ActionsConfiguration;
import io.jmix.flowui.sys.ViewControllersConfiguration;
import io.jmix.flowui.sys.ViewSupport;
import io.jmix.gridexportflowui.GridExportFlowuiConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Collections;

@Configuration
@ComponentScan
@ConfigurationPropertiesScan
@JmixModule(dependsOn = {EclipselinkConfiguration.class, FlowuiConfiguration.class, GridExportFlowuiConfiguration.class})
@PropertySource(name = "dev.smartdata.runteam", value = "classpath:/dev/smartdata/runteam/module.properties")
public class RtConfiguration {

    @Bean("rt_RtViewControllers")
    public ViewControllersConfiguration screens(final ApplicationContext applicationContext,
                                                final AnnotationScanMetadataReaderFactory metadataReaderFactory) {
        final ViewControllersConfiguration viewControllers
                = new ViewControllersConfiguration(applicationContext, metadataReaderFactory);
        viewControllers.setBasePackages(Collections.singletonList("dev.smartdata.runteam"));
        return viewControllers;
    }

    @Bean("rt_RtActions")
    public ActionsConfiguration actions(final ApplicationContext applicationContext,
                                        final AnnotationScanMetadataReaderFactory metadataReaderFactory) {
        final ActionsConfiguration actions
                = new ActionsConfiguration(applicationContext, metadataReaderFactory);
        actions.setBasePackages(Collections.singletonList("dev.smartdata.runteam"));
        return actions;
    }

    @Bean("flowui_ViewSupport")
    public ViewSupport viewSupport(final ApplicationContext applicationContext) {
        return (ViewSupport) applicationContext.getBean("rt_ViewSupport");
    }
}
