package boot.repo;

import org.springframework.data.repository.CrudRepository;

import boot.model.Account;

public interface AccountRepo extends CrudRepository<Account, Long> {

}
