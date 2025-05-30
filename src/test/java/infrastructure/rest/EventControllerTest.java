package infrastructure.rest;

import domain.EventService;
import domain.model.Event;
import domain.model.exceptions.AttendeeAlreadyRegisteredException;
import domain.model.exceptions.EventAlreadyFullException;
import domain.model.exceptions.EventIsOverException;
import infrastructure.rest.dto.AttendeeRequestBody;
import infrastructure.rest.dto.EventRequestBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import testing.dsl.RestApi;
import testing.extensions.EventResolver;
import testing.extensions.RestApiExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.approvaltests.JsonApprovals.verifyJson;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
@ExtendWith({RestApiExtension.class, EventResolver.class})
@Execution(ExecutionMode.SAME_THREAD)
class EventControllerTest {

    @MockitoBean
    private EventService eventService;

    RestApi api;

    @Test
    @DisplayName("should return nothing when there is no event planned")
    void should_return_no_events() throws Exception {
        when(eventService.listEvents()).thenReturn(List.of());

        api.consultEventCalendar()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events").isEmpty());
    }

    @Test
    @DisplayName("should plan an event")
    void should_plan_event() throws Exception {
        String eventId = "477739f1-5a76-486f-bdd7-9144aee9b36f";
        when(eventService.planEvent(any(), any(), anyInt(), any())).thenReturn(UUID.fromString(eventId));

        EventRequestBody event = new EventRequestBody("An event", LocalDate.parse("2020-10-20"), 1);

        api.planAnEvent(event, UUID.randomUUID())
                .andExpect(status().isCreated())
                .andExpect(content().string(eventId));
    }

    @Nested
    @DisplayName("When an event is planned")
    class AnEventExists {

        private String eventId;

        @BeforeEach
        void setUp(Event event) {
            when(eventService.listEvents()).thenReturn(List.of(event));
            eventId = event.id().toString();
        }

        @Test
        @DisplayName("should return the event")
        void should_return_event() throws Exception {
            String response = api.consultEventCalendar()
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse().getContentAsString();

            verifyJson(response);
        }

        @Test
        @DisplayName("should be able to register to the event")
        void should_register_to_event() throws Exception {
            AttendeeRequestBody attendee = new AttendeeRequestBody("Amy", "amy@email.com");
            api.registerToEvent(eventId, attendee)
                    .andExpect(status().isOk());
        }

        @ParameterizedTest
        @DisplayName("should respond with bad request")
        @ValueSource(classes = {
                AttendeeAlreadyRegisteredException.class,
                EventIsOverException.class,
                EventAlreadyFullException.class
        })
        void should_respond_400(Class<? extends Throwable> exception) throws Exception {
            doThrow(exception).when(eventService).registerTo(any(), any(), any());

            AttendeeRequestBody attendee = new AttendeeRequestBody("Amy", "amy@email.com");
            api.registerToEvent(eventId, attendee)
                    .andExpect(status().isBadRequest());
        }

    }

}