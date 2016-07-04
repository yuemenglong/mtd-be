package boot.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import kit.JSON;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import boot.model.Account;
import boot.model.Order;
import boot.repo.AccountRepo;
import boot.repo.OrderRepo;

@RestController
@Transactional
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private AccountRepo accountRepo;
	@Autowired
	private OrderRepo orderRepo;
	@Autowired
	private EntityManager em;

	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String list() {
		Iterable<boot.model.Account> ret = accountRepo.findAll();
		return JSON.stringify(ret);
	}

	@RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public String addAccount(@RequestBody String json) throws Exception {
		Account account = JSON.parse(json, Account.class);
		if (account == null || account.getName() == null || account.getName().length() == 0) {
			throw new Exception("Invalid Account Json");
		}
		account = accountRepo.save(account);
		return JSON.stringify(account);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void deleteAccount(@PathVariable long id) {
		accountRepo.delete(id);
		return;
	}

	@RequestMapping(value = "/{id}/order", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String listOrder(@PathVariable long id) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Order> query = cb.createQuery(Order.class);
		Root<Order> root = query.from(Order.class);
		Predicate cond = cb.equal(root.get("account").get("id"), id);
		query.where(cond);
		List<Order> result = em.createQuery(query).getResultList();
		return JSON.stringify(result);
	}

	@RequestMapping(value = "/{id}/order", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public String sendOrder(@PathVariable long id, @RequestBody String json) {
		Order order = JSON.parse(json, Order.class);
		order.setAccount(new Account());
		order.getAccount().setId(id);
		order = orderRepo.save(order);
		return JSON.stringify(order);
	}

	@RequestMapping(value = "/{id}/order/{orderId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public String updateOrder(@PathVariable long id, @PathVariable long orderId, @RequestBody String json) {
		Order order = JSON.parse(json, Order.class);
		order = orderRepo.save(order);
		return JSON.stringify(order);
	}

	@RequestMapping(value = "/{id}/order", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public String updateOrders(@PathVariable long id, @RequestBody String json) {
		Order[] orders = JSON.parse(json, Order[].class);
		for (Order order : orders) {
			order = orderRepo.save(order);
		}
		return JSON.stringify(orders);
	}

	@RequestMapping(value = "/{id}/order/{orderId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void deleteOrder(@PathVariable long id, @PathVariable long orderId) {
		orderRepo.delete(orderId);
		return;
	}

	@SuppressWarnings("unused")
	public void criteria() {
		CriteriaQuery<Order> c = em.getCriteriaBuilder().createQuery(Order.class);
		Root<Order> root = c.from(Order.class);
		root.fetch("a").fetch("b");
		Predicate e1 = em.getCriteriaBuilder().equal(root.get("Order.purchaser.firstName"), "zhang");
		Predicate e2 = em.getCriteriaBuilder().equal(root.get("Order.purchaser.lastName"), "san");
		Predicate cond = em.getCriteriaBuilder().and(e1, e2);
		c.where(e1, e2);
		em.createQuery(c).getResultList();
	}
}
