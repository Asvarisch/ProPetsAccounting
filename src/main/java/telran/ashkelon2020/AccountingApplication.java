package telran.ashkelon2020;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import telran.ashkelon2020.accounting.dao.UserRepository;
import telran.ashkelon2020.accounting.model.UserAccount;

@SpringBootApplication
//@EnableEurekaClient
public class AccountingApplication implements CommandLineRunner{
	
	@Autowired
	UserRepository userAccountRepository;

	public static void main(String[] args) {
		SpringApplication.run(AccountingApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		if (!userAccountRepository.existsById("admin")) {
			String hashPassword = BCrypt.hashpw("admin", BCrypt.gensalt());   //passwordEncoder.encode("admin");
			UserAccount admin = new UserAccount();
			admin.setEmail("admin@gmail.com");
			admin.setName("admin");
			admin.setPassword(hashPassword);
			admin.addRole("USER");
			admin.addRole("MODERATOR");
			admin.addRole("ADMINISTRATOR");
			userAccountRepository.save(admin);
		}

	}

}
