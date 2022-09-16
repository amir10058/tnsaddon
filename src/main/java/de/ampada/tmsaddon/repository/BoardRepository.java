package de.ampada.tmsaddon.repository;

import de.ampada.tmsaddon.entities.Board;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BoardRepository extends MongoRepository<Board, String> {

}
