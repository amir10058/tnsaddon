package de.ampada.tmsaddon.entities;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Document(collection = "card")
public class Card {

    @MongoId
    private ObjectId id;

    @Indexed(unique = true)
    @NotBlank(message = "blank.cardTitle")
    private String cardTitle;

    private Board board;
    @CreatedDate
    private Date createdOn;

    @LastModifiedDate
    private Date modifiedOn;

    private List<User> memberUserList;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public List<User> getMemberUserList() {
        return memberUserList;
    }

    public void setMemberUserList(List<User> memberUserList) {
        this.memberUserList = memberUserList;
    }
}
