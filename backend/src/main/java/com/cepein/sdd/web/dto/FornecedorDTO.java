package com.cepein.sdd.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FornecedorDTO {

    private Long idFornecedor;

    @NotBlank(message = "Razão Social é obrigatória")
    @Size(max = 255, message = "Razão Social deve ter no máximo 255 caracteres")
    private String razaoSocial;

    @NotBlank(message = "CNPJ é obrigatório")
    @Size(max = 18, message = "CNPJ inválido")
    private String cnpj;

    private String telefone;

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail corporativo inválido")
    private String email;

    private LocalDateTime dataCadastro;
}
