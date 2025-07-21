package br.com.portfolio.api;

import br.com.portfolio.api.dto.request.CreateMemberRequest;
import br.com.portfolio.model.enums.RoleType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PersonApiControllerTest {
    @Value("${api.key.test}")
    private String apiKey;

    @Value("${api.members.base-url}")
    private String baseUrl;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateMemberRequest validRequest() {
        CreateMemberRequest request = new CreateMemberRequest();
        request.setName("John Test");
        request.setCpf("123.456.789-00");
        request.setBirthDate("1990-01-01");
        request.setRole(RoleType.EMPLOYEE);
        return request;
    }

    @Nested
    @DisplayName("POST /api/members")
    class CreateMember {

        @Test
        @DisplayName("Should create member successfully")
        void shouldCreateMemberSuccessfully() throws Exception {
            mockMvc.perform(post(baseUrl)
                            .header("x-api-key", apiKey)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest())))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value("SUCCESS"))
                    .andExpect(jsonPath("$.code").value(201))
                    .andExpect(jsonPath("$.data.name").value("John Test"));
        }

        @Test
        @DisplayName("Should return validation error when name is missing")
        void shouldReturnValidationErrorWhenMissingName() throws Exception {
            CreateMemberRequest request = validRequest();
            request.setName(null);

            mockMvc.perform(post(baseUrl)
                            .header("x-api-key", apiKey)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.status").value("ERROR"))
                    .andExpect(jsonPath("$.code").value(422));
        }

        @Test
        @DisplayName("Should return 500 when JSON is malformed")
        void shouldReturnInternalServerErrorOnMalformedJson() throws Exception {
            mockMvc.perform(post(baseUrl)
                            .header("x-api-key", apiKey)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"invalidJson\":true"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.status").value("ERROR"))
                    .andExpect(jsonPath("$.code").value(500))
                    .andExpect(jsonPath("$.message").value("An unexpected error occurred"));
        }

    }

    @Nested
    @DisplayName("PUT /api/members/{id}")
    class UpdateMember {

        @Test
        @DisplayName("Should return 404 when member does not exist")
        void shouldReturnNotFoundWhenUpdatingNonexistentMember() throws Exception {
            mockMvc.perform(put(baseUrl + "/99999")
                            .header("x-api-key", apiKey)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest())))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("ERROR"))
                    .andExpect(jsonPath("$.code").value(404))
                    .andExpect(jsonPath("$.message", containsString("Member not found")));
        }
    }

    @Nested
    @DisplayName("GET /api/members")
    class ListMembers {

        @Test
        @DisplayName("Should return paginated list successfully")
        void shouldReturnPaginatedList() throws Exception {
            mockMvc.perform(get(baseUrl)
                            .header("x-api-key", apiKey)
                            .param("page", "0")
                            .param("size", "5"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("SUCCESS"))
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.data").isArray());
        }
    }
}

