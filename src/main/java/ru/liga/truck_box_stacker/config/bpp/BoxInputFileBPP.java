package ru.liga.truck_box_stacker.config.bpp;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import ru.liga.truck_box_stacker.config.bpp.annotation.BoxInputFile;
import ru.liga.truck_box_stacker.model.CommandLineArgs;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.List;

/**
 * A Spring BeanPostProcessor that injects a file path
 * from command-line arguments into fields annotated with
 * @BoxInputFile in application beans.
 */
@Component
@RequiredArgsConstructor
public class BoxInputFileBPP implements BeanPostProcessor {

    private final ApplicationArguments applicationArguments;
    private boolean alreadyChecked = false; // Flag to ensure command-line arguments are only checked once.
    private Path pathToFile; // Holds the path to the input file.

    /**
     * Post-processes the bean before its initialization.
     * If the application arguments contain a value for PATH_INPUT_FILE,
     * it retrieves the path and injects it into fields annotated with @BoxInputFile.
     *
     * @param bean     the bean instance being post-processed.
     * @param beanName the name of the bean in the application context.
     * @return the modified or original bean instance.
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (!alreadyChecked && applicationArguments.containsOption(CommandLineArgs.PATH_INPUT_FILE.name())) {
            alreadyChecked = true;
            List<String> optionValues = applicationArguments.getOptionValues(CommandLineArgs.PATH_INPUT_FILE.name());
            String pathFile = optionValues.get(0);
            pathToFile = Path.of(pathFile);
        }
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(BoxInputFile.class)) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, bean, pathToFile); // Inject the path to the field.
            }
        }
        return bean;
    }
}
