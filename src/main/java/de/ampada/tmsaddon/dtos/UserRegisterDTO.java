package de.ampada.tmsaddon.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDTO {

    @NotBlank(message = "blank.username")
    private String username;
    @NotBlank(message = "blank.password")
    private String password;

}
