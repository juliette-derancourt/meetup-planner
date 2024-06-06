package infrastructure;

import infrastructure.rest.dto.AttendeeRequestBody;
import infrastructure.rest.dto.EventRequestBody;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import testing.WorkflowTest;
import testing.dsl.TestDataFactory;
import testing.dsl.UserInterface;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WorkflowTest
class EventWorkflowTest {

    private UserInterface userInterface;

    private String eventId;

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

        EventRequestBody event = new EventRequestBody("An event", LocalDateTime.parse("2024-10-20T10:20:20"), 1);

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
                                    "date": "2024-10-20T10:20:20",
                                    "attendees": 0
                                }
                            ]
                        }
                        """, true));
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