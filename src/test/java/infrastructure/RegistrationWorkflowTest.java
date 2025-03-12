package infrastructure;

import infrastructure.rest.dto.AttendeeRequestBody;
import infrastructure.rest.dto.EventRequestBody;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import testing.dsl.TestDataFactory;
import testing.dsl.UserInterface;
import testing.extensions.TestDataFactoryExtension;
import testing.extensions.UserInterfaceExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith({UserInterfaceExtension.class, TestDataFactoryExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RegistrationWorkflowTest {

    UserInterface userInterface;

    private String eventId;
    private final LocalDate tomorrow = LocalDate.now().plusDays(1);

    @Test
    @Order(1)
    void there_is_no_events() throws Exception {
        userInterface.consultEventCalendar()
                .andExpect(jsonPath("$.events").isEmpty());
    }

    @Test
    @Order(2)
    void an_organizer_plans_an_event(TestDataFactory testDataFactory) throws Exception {
        UUID organizerId = testDataFactory.givenAnOrganizer("ee8b1e44-b0e1-4cc0-9d6e-b24ce343111e");

        EventRequestBody event = new EventRequestBody("An event", tomorrow, 1);

        eventId = userInterface.planAnEvent(event, organizerId)
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    @Order(3)
    void there_is_an_event_available() throws Exception {
        userInterface.consultEventCalendar()
                .andExpect(content().json("""
                        {
                            "events": [
                                {
                                    "name": "An event",
                                    "date": "%s",
                                    "attendees": 0
                                }
                            ]
                        }
                        """.formatted(tomorrow)));
    }

    @Test
    @Order(4)
    void someone_registers_to_the_event() throws Exception {
        userInterface.registerToEvent(eventId, new AttendeeRequestBody("Amy", "amy@email.com"));

        userInterface.consultEventCalendar()
                .andExpect(jsonPath("$.events[0].attendees").value(1));
    }

    @Test
    @Order(5)
    void the_event_is_full() throws Exception {
        userInterface.registerToEvent(eventId, new AttendeeRequestBody("Rory", "rory@email.com"))
                .andExpect(status().isBadRequest());

        userInterface.consultEventCalendar()
                .andExpect(jsonPath("$.events[0].attendees").value(1));
    }

}