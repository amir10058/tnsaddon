package de.ampada.tmsaddon.repository;

import de.ampada.tmsaddon.entities.Card;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CardRepository extends MongoRepository<Card, ObjectId> {

    List<Card> findAllByBoard_Id(ObjectId boardId);
}
