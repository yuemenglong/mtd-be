package boot.controller;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

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
import kit.JSON;

@RestController
@Transactional
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private AccountRepo repo;
	@Autowired
	private EntityManager em;

	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String list() {
		Iterable<boot.model.Account> ret = repo.findAll();
		return JSON.stringify(ret);
	}

	@RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public String addAccount(@RequestBody String json) throws Exception {
		Account account = JSON.parse(json, Account.class);
		if (account == null || account.getName() == null || account.getName().length() == 0) {
			throw new Exception("Invalid Account Json");
		}
		account = repo.save(account);
		return JSON.stringify(account);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void deleteAccount(@PathVariable long id) {
		repo.delete(id);
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
