package br.com.portfolio.api.controller;

import br.com.portfolio.api.dto.request.CreateMemberRequest;
import br.com.portfolio.api.dto.response.PersonResponse;
import br.com.portfolio.api.mapper.PersonMapper;
import br.com.portfolio.api.util.ApiResponse;
import br.com.portfolio.api.util.ApiStatus;
import br.com.portfolio.model.Person;
import br.com.portfolio.service.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Slf4j
public class PersonApiController {

    private final PersonService personService;
    private final PersonMapper personMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<PersonResponse>> createMember(@Valid @RequestBody CreateMemberRequest request) {
        log.info("Received request to create member: {}", request.getName());
        Person created = personService.createFromRequest(request);
        PersonResponse dto = personMapper.toPersonResponse(created);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<PersonResponse>builder()
                        .status(ApiStatus.SUCCESS)
                        .code(HttpStatus.CREATED.value())
                        .message("Member created successfully!")
                        .data(dto)
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PersonResponse>>> getAllMembers(@RequestParam(defaultValue = "0") int page,
                                                                            @RequestParam(defaultValue = "10") int size) {
        log.info("Fetching members - page: {}, size: {}", page, size);
        Page<Person> peoplePage = personService.findPaginated(page, size);

        List<PersonResponse> dtos = personMapper.toPersonResponseList(peoplePage.getContent());

        return ResponseEntity.ok(
                ApiResponse.<List<PersonResponse>>builder()
                        .status(ApiStatus.SUCCESS)
                        .code(HttpStatus.OK.value())
                        .message("Members retrieved successfully!")
                        .data(dtos)
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PersonResponse>> updateMember(@PathVariable Long id,
                                                                    @Valid @RequestBody CreateMemberRequest request) {
        log.info("Updating member with ID: {}", id);
        Person updated = personService.update(id, request);
        PersonResponse dto = personMapper.toPersonResponse
                (updated);

        return ResponseEntity.ok(
                ApiResponse.<PersonResponse>builder()
                        .status(ApiStatus.SUCCESS)
                        .code(HttpStatus.OK.value())
                        .message("Member updated successfully!")
                        .data(dto)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMember(@PathVariable Long id) {
        log.info("Deleting member with ID: {}", id);
        personService.delete(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .status(ApiStatus.SUCCESS)
                        .code(HttpStatus.OK.value())
                        .message("Member deleted successfully!")
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PersonResponse>> getMemberById(@PathVariable Long id) {
        log.info("Fetching member with ID: {}", id);
        Person person = personService.findById(id);
        PersonResponse dto = personMapper.toPersonResponse(person);

        return ResponseEntity.ok(
                ApiResponse.<PersonResponse>builder()
                        .status(ApiStatus.SUCCESS)
                        .code(HttpStatus.OK.value())
                        .message("Member retrieved successfully!")
                        .data(dto)
                        .build()
        );
    }
}