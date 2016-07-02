package boot.repo;

import org.springframework.data.repository.CrudRepository;

import boot.model.Order;

public interface OrderRepo extends CrudRepository<Order, Long> {

}
