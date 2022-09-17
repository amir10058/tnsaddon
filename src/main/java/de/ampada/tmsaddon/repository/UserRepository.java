package de.ampada.tmsaddon.repository;

import de.ampada.tmsaddon.entities.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId> {

    User findUserByUsername(String username);

    Boolean existsByUsername(String username);
}
