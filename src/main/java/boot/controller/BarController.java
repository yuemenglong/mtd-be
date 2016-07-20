package boot.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import kit.JSON;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import boot.model.Bar;
import boot.repo.BarRepo;

@RestController
@Transactional
@RequestMapping("/bar")
public class BarController {

	@Autowired
	private BarRepo repo;
	@Autowired
	private EntityManager em;

	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String list(@DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date to, Integer limit) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Bar> query = cb.createQuery(Bar.class);
		Root<Bar> root = query.from(Bar.class);
		ArrayList<Predicate> ps = new ArrayList<Predicate>();
		query.orderBy(cb.asc(root.get("datetime")));
		if (from != null) {
			ps.add(cb.greaterThanOrEqualTo(root.get("datetime"), from));
		}
		if (to != null) {
			ps.add(cb.lessThanOrEqualTo(root.get("datetime"), to));
			query.orderBy(cb.desc(root.get("datetime")));
		}
		if (limit == null) {
			limit = 450;
		}
		Predicate[] cond = ps.toArray(new Predicate[0]);
		query.where(cond);

		List<Bar> list = em.createQuery(query).setMaxResults(limit).getResultList();
		if (to != null) {
			Collections.reverse(list);
		}
		return JSON.stringify(list);
	}

}
