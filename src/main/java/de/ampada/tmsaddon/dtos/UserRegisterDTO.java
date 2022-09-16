package de.ampada.tmsaddon.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDTO {

    @NotBlank(message = "UserRegisterDTO.username.field.is.blank")
    private String username;
    @NotBlank(message = "UserRegisterDTO.password.field.is.blank")
    private String password;

}
