package telran.ashkelon2020.accounting.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.ashkelon2020.accounting.model.UserAccount;

public interface UserRepository extends MongoRepository<UserAccount, String> {

}
