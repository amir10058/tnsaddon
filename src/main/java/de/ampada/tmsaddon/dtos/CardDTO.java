package de.ampada.tmsaddon.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardDTO {

    private String id;
    @NotBlank(message = "blank.cardTitle")
    private String cardTitle;
    private String boardId;
    private Long createdOn;
    private Long modifiedOn;
    private List<String> memberUserIdList;

}
