package infrastructure.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import testing.extensions.EventResolver;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith({EventResolver.class})
class EventControllerTest {

    @MockitoBean
    private EventService eventService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("should return nothing when there is no event planned")
    void should_return_no_events() throws Exception {
        when(eventService.listEvents()).thenReturn(List.of());

        MockHttpServletResponse response = mockMvc.perform(get("/events"))
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertEquals("{\"events\":[]}", response.getContentAsString());
    }

    @Test
    @DisplayName("should plan an event")
    void should_plan_event() throws Exception {
        String eventId = "477739f1-5a76-486f-bdd7-9144aee9b36f";
        when(eventService.planEvent(any(), any(), anyInt(), any())).thenReturn(UUID.fromString(eventId));

        EventRequestBody event = new EventRequestBody("An event", LocalDate.parse("2020-10-20"), 1);

        String response = mockMvc.perform(post("/events")
                        .header("user-id", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(eventId, response);
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
            String response = mockMvc.perform(get("/events"))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse().getContentAsString();

            String expected = """
                    {"events":[{"name":"A great event","date":"2025-03-02","attendees":0}]}""";

            assertEquals(expected, response);
        }

        @Test
        @DisplayName("should be able to register to the event")
        void should_register_to_event() throws Exception {
            AttendeeRequestBody attendee = new AttendeeRequestBody("Amy", "amy@email.com");
            mockMvc.perform(patch("/events/{id}/register", eventId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(attendee)))
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
            mockMvc.perform(patch("/events/{id}/register", eventId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(attendee)))
                    .andExpect(status().isBadRequest());
        }

    }

}