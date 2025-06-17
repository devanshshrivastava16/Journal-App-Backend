package dev.phenom.JournalApp.Repository;
import dev.phenom.JournalApp.Entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByUserName(String userName);
}