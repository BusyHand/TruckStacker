package ru.liga.truckstacker.config.bpp;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import ru.liga.truckstacker.config.bpp.annotation.BoxStackerTypeAlgorithm;
import ru.liga.truckstacker.config.bpp.annotation.TypeAlgorithmToBeanName;
import ru.liga.truckstacker.service.stacker.BoxStackerService;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class TypeAlgorithmToBeanNameBPP implements BeanPostProcessor {

    private Map<TypeAlgorithm, String> typeAlgorithmToBeanNames = new HashMap<>();


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        registerTypeAlgorithmForBean(bean, beanName);
        postProcessAutowiredTypeAlgorithmFields(bean);
        return bean;
    }


    private void postProcessAutowiredTypeAlgorithmFields(Object bean) {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(TypeAlgorithmToBeanName.class) &&
                    isValidType(field)) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, bean, typeAlgorithmToBeanNames);
            }
        }
    }

    private boolean isValidType(Field field) {
        if (Map.class.isAssignableFrom(field.getType())) {
            Type genericType = field.getGenericType();
            if (genericType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericType;

                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

                if (actualTypeArguments.length == 2 &&
                        actualTypeArguments[0].equals(TypeAlgorithm.class) &&
                        actualTypeArguments[1].equals(String.class)) {
                    return true;
                }
            }
        }
        throw new IllegalArgumentException("Annotation TypeAlgorithmToBeanName must be on field with type Map<TypeAlgorithm, String>");
    }


    private void registerTypeAlgorithmForBean(Object bean, String beanName) {
        BoxStackerTypeAlgorithm annotation = bean.getClass().getAnnotation(BoxStackerTypeAlgorithm.class);
        if (annotation != null) {
            TypeAlgorithm typeAlgorithm = annotation.value();
            if (isValidBeanAndType(bean, typeAlgorithm)) {
                typeAlgorithmToBeanNames.put(typeAlgorithm, beanName);
            }
        }
    }

    private boolean isValidBeanAndType(Object bean, TypeAlgorithm typeAlgorithm) {
        boolean containsInterface = false;
        for (Class<?> beanInterface : bean.getClass().getInterfaces()) {
            if (beanInterface.equals(BoxStackerService.class)) {
                containsInterface = true;
                break;
            }
        }
        if (!containsInterface) {
            throw new IllegalArgumentException("Annotation BoxStackerTypeAlgorithm must implement BoxStackerService interface");
        }
        if (typeAlgorithmToBeanNames.containsKey(typeAlgorithm)) {
            throw new IllegalArgumentException("Annotation BoxStackerTypeAlgorithm with TypeAlgorithm must be unique for each BoxStackerService");
        }
        return true;
    }
}
