package testing.dsl;

import com.fasterxml.jackson.databind.ObjectMapper;
import infrastructure.rest.dto.AttendeeRequestBody;
import infrastructure.rest.dto.EventRequestBody;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class RestApi {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    public RestApi(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public ResultActions consultEventCalendar() throws Exception {
        return mockMvc.perform(get("/events"));
    }

    public ResultActions planAnEvent(EventRequestBody event, UUID userId) throws Exception {
        return mockMvc.perform(post("/events")
                .header("user-id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event)));
    }

    public ResultActions registerToEvent(String eventId, AttendeeRequestBody attendee) throws Exception {
        return mockMvc.perform(patch("/events/{id}/register", eventId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(attendee)));
    }

}
