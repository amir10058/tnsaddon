package de.ampada.tmsaddon.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardSearchDTO {
    private String cardTitle;
    private List<String> memberUserIdList;
    private Boolean sortByModifiedOnDesc;
}
