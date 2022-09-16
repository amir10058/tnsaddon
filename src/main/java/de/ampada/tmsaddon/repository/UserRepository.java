package de.ampada.tmsaddon.repository;

import de.ampada.tmsaddon.domains.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    User findUserByUsername(String username);

    Boolean existsByUsername(String username);
}
