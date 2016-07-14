package boot.repo;

import org.springframework.data.repository.CrudRepository;

import boot.model.Bar;

public interface BarRepo extends CrudRepository<Bar, Long> {

}
