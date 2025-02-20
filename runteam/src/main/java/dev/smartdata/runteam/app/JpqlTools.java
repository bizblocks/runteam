package dev.smartdata.runteam.app;

import io.jmix.core.Metadata;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component("rt_JpqlTools")
public class JpqlTools {
    @PersistenceContext
    protected EntityManager entityManager;

    public JpqlTools(Metadata metadata) {
    }

    @SuppressWarnings("rawtypes")
    @Transactional
    public List<Class> getQueryProperties(String query) throws Exception {
        List<Class> properties = new ArrayList<>();
        List one = entityManager.createQuery(query).setMaxResults(1).getResultList();
        if (one.isEmpty())
            throw new Exception("No results");
        Object oneObject = one.get(0);
        if (oneObject instanceof Object[]) {
            Object[] array = (Object[]) oneObject;
            for (Object element : array) {
                properties.add(element.getClass());
            }
        } else {
            properties.add(oneObject.getClass());
        }
        return properties;
    }
}