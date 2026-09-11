package dev.smartdata.runteam.security;

import dev.smartdata.runteam.entity.Dummy;
import dev.smartdata.runteam.entity.RTGroovyScript;
import dev.smartdata.runteam.entity.RTJPQLScript;
import dev.smartdata.runteam.entity.RTScript;
import io.jmix.security.model.EntityAttributePolicyAction;
import io.jmix.security.model.EntityPolicyAction;
import io.jmix.security.role.annotation.EntityAttributePolicy;
import io.jmix.security.role.annotation.EntityPolicy;
import io.jmix.security.role.annotation.ResourceRole;

@ResourceRole(name = "RunnerRole", code = RunnerRole.CODE)
public interface RunnerRole {
    String CODE = "runner-role";

    @io.jmix.securityflowui.role.annotation.MenuPolicy(menuIds = {
            "rt_RunGroovy",
            "rt_RunJpql",
            "rt_ScriptView"
    })
    @io.jmix.securityflowui.role.annotation.ViewPolicy(viewIds = {
            "rt_RTGroovyScript.detail",
            "rt_RTJPQLScript.detail",
            "rt_RunGroovy",
            "rt_RunJpql",
            "rt_ScriptView"
    })
    void screens();

    @EntityAttributePolicy(entityClass = Dummy.class,
            attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = Dummy.class, actions = EntityPolicyAction.ALL)
    void dummy();

    @EntityAttributePolicy(entityClass = RTGroovyScript.class,
            attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = RTGroovyScript.class, actions = EntityPolicyAction.ALL)
    void rTGroovyScript();

    @EntityAttributePolicy(entityClass = RTJPQLScript.class,
            attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = RTJPQLScript.class, actions = EntityPolicyAction.ALL)
    void rTJPQLScript();

    @EntityAttributePolicy(entityClass = RTScript.class,
            attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = RTScript.class, actions = EntityPolicyAction.ALL)
    void rTScript();
}