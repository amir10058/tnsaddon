package de.ampada.tmsaddon.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {

    private String id;
    @NotBlank
    private String boardName;
    private Long createdOn;
    private Long modifiedOn;
    @NotBlank
    private String creatorId;

}
