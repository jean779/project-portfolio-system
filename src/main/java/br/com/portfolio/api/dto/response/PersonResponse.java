package br.com.portfolio.api.dto.response;

import br.com.portfolio.model.enums.RoleType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PersonResponse {
    private Long id;
    private String name;
    private String cpf;
    private String birthDate;
    private RoleType role;
}