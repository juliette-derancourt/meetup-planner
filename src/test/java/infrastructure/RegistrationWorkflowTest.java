package infrastructure;

import infrastructure.rest.dto.AttendeeRequestBody;
import infrastructure.rest.dto.EventRequestBody;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import testing.dsl.RestApi;
import testing.dsl.TestDataFactory;
import testing.extensions.RestApiExtension;
import testing.extensions.TestDataFactoryExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith({RestApiExtension.class, TestDataFactoryExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RegistrationWorkflowTest {

    RestApi api;
    TestDataFactory testDataFactory;

    private String eventId;
    private final LocalDate tomorrow = LocalDate.now().plusDays(1);

    @Test
    void scenario() throws Exception {
        there_is_no_events();
        an_organizer_plans_an_event();
        there_is_an_event_available();
        someone_registers_to_the_event();
        the_event_is_full();
    }

    void there_is_no_events() throws Exception {
        api.consultEventCalendar()
                .andExpect(jsonPath("$.events").isEmpty());
    }

    void an_organizer_plans_an_event() throws Exception {
        UUID organizerId = testDataFactory.givenAnOrganizer("ee8b1e44-b0e1-4cc0-9d6e-b24ce343111e");

        EventRequestBody event = new EventRequestBody("An event", tomorrow, 1);

        eventId = api.planAnEvent(event, organizerId)
                .andReturn().getResponse().getContentAsString();
    }

    void there_is_an_event_available() throws Exception {
        api.consultEventCalendar()
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

    void someone_registers_to_the_event() throws Exception {
        api.registerToEvent(eventId, new AttendeeRequestBody("Amy", "amy@email.com"));

        api.consultEventCalendar()
                .andExpect(jsonPath("$.events[0].attendees").value(1));
    }

    void the_event_is_full() throws Exception {
        api.registerToEvent(eventId, new AttendeeRequestBody("Rory", "rory@email.com"))
                .andExpect(status().isBadRequest());

        api.consultEventCalendar()
                .andExpect(jsonPath("$.events[0].attendees").value(1));
    }

}