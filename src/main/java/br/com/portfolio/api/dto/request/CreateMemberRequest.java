package br.com.portfolio.api.dto.request;

import br.com.portfolio.model.enums.RoleType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateMemberRequest {

    @NotEmpty(message = "Name is required")
    private String name;

    private String cpf;

    @NotEmpty(message = "Birth date is required")
    private String birthDate;

    @NotNull(message = "Role is required (MANAGER or EMPLOYEE)")
    private RoleType role;
}
