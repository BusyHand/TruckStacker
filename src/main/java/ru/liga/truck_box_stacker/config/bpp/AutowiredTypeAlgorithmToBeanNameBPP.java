package ru.liga.truck_box_stacker.config.bpp;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import ru.liga.truck_box_stacker.config.bpp.annotation.AutowiredTypeAlgorithmToBeanName;
import ru.liga.truck_box_stacker.config.bpp.annotation.UseStackerWhen;
import ru.liga.truck_box_stacker.model.TypeAlgorithm;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * A Spring BeanPostProcessor that automatically wires a map of TypeAlgorithm
 * to bean names for beans annotated with @AutowiredTypeAlgorithmToBeanName.
 * This configuration allows beans to receive algorithm-specific settings
 * during initialization.
 */
@Component
@RequiredArgsConstructor
public class AutowiredTypeAlgorithmToBeanNameBPP implements BeanPostProcessor {

    private Map<TypeAlgorithm, String> typeAlgorithmToBeanNames = new HashMap<>();

    /**
     * Post-processes a bean before its initialization.
     * Updates the typeAlgorithmToBeanNames map by inspecting
     * the bean’s annotations and injecting the corresponding settings if applicable.
     *
     * @param bean     the bean instance being post-processed.
     * @param beanName the name of the bean in the application context.
     * @return the modified or original bean instance.
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        useStackerConfiguration(bean, beanName);
        autowiredTypeAlgorithmToBeanNameConfiguration(bean);
        return bean;
    }

    /**
     * Configures the bean's fields that are annotated with
     * @AutowiredTypeAlgorithmToBeanName. This method injects
     * the accessible fields with the typeAlgorithmToBeanNames map.
     *
     * @param bean the bean instance being configured.
     */
    private void autowiredTypeAlgorithmToBeanNameConfiguration(Object bean) {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(AutowiredTypeAlgorithmToBeanName.class)) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, bean, typeAlgorithmToBeanNames); // Inject the map.
            }
        }
    }

    /**
     * Updates the typeAlgorithmToBeanNames map based on the annotation
     * present on the bean.
     *
     * @param bean     the bean instance being processed.
     * @param beanName the name of the bean in the application context.
     */
    private void useStackerConfiguration(Object bean, String beanName) {
        UseStackerWhen annotation = bean.getClass().getAnnotation(UseStackerWhen.class);
        if (annotation != null) {
            TypeAlgorithm type = annotation.value();
            typeAlgorithmToBeanNames.put(type, beanName); // Map TypeAlgorithm to bean name.
        }
    }
}
