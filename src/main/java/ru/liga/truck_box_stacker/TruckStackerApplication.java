
package ru.liga.truck_box_stacker;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import ru.liga.truck_box_stacker.model.Box;
import ru.liga.truck_box_stacker.model.TruckList;
import ru.liga.truck_box_stacker.service.BoxReaderService;
import ru.liga.truck_box_stacker.service.BoxStackerService;
import ru.liga.truck_box_stacker.service.BoxStackerServiceFactory;

import java.util.List;

import static ru.liga.truck_box_stacker.convertor.ConvertCommandLineArgsToStringArgs.convertToSpringArgsValid;

@SpringBootApplication
@RequiredArgsConstructor
public class TruckStackerApplication {
    private final BoxReaderService boxReaderService;
    private final BoxStackerServiceFactory boxStackerServiceFactory;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(TruckStackerApplication.class);
        app.setBannerMode(Mode.OFF);
        args = convertToSpringArgsValid(args);
        app.run(args);
    }
    /**
     * Event listener that triggers once the application has started.
     * Reads the list of boxes, determines the appropriate stacking service,
     * and then stacks the boxes, finally printing the resulting TruckList to the console.
     *
     * @param event the event indicating that the application is ready.
     */
    @EventListener({ApplicationReadyEvent.class})
    public void doAfterStartup() {
        List<Box> boxes = boxReaderService.readAndGetBoxList();
        BoxStackerService boxStackerService = boxStackerServiceFactory.getBoxStackerServiceBySizeOfData(boxes.size());
        TruckList truckList = boxStackerService.stackBoxes(boxes);
        System.out.println(truckList);
    }

}
