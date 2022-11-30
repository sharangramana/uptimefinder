package com.personalprojects.uptimefinder.repository;

import com.personalprojects.uptimefinder.entity.Service;
import com.personalprojects.uptimefinder.entity.UptimeMonitor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
@Transactional
public class ServiceRepository {

	@Autowired
	private SessionFactory sessionFactory;

	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * Save the service in to database
	 * 
	 * @param service
	 * @return persisted service contains generated Id
	 */
	@Transactional
	public Service saveService(Service service) {
		getSession().save(service);
		return service;
	}

	/**
	 * Updates the service as enable/disable, disable service will not do monitoring of
	 * website
	 * 
	 * @param isEnable
	 * @param id
	 */
	@SuppressWarnings("rawtypes")
	@Transactional
	public void updateService(boolean isEnable, int id) {
		Query query = getSession().createQuery("UPDATE Service SET enabled=:isEnable, updatedAt=:updatedAt WHERE id=:id");
		query.setParameter("isEnable", isEnable);
		query.setParameter("id", id);
		query.setParameter("updatedAt", new Timestamp(System.currentTimeMillis()));
		query.executeUpdate();
	}

	/**
	 * Returns the filtered services by interval
	 * 
	 * @param keysToFilter
	 * @return filtered services
	 */
	public List<Service> getFilteredServices(HashMap<String, String> keysToFilter, int page, int pageSize) {

		CriteriaBuilder builder = getSession().getCriteriaBuilder();
		CriteriaQuery<Service> criteria = builder.createQuery(Service.class);

		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		countQuery.select(builder.count(countQuery.from(Service.class)));
		Long count = getSession().createQuery(countQuery).getSingleResult();

		List<Service> data = new ArrayList<>();
		if(page < count.intValue()) {
			Root<Service> root = criteria.from(Service.class);
			List<Predicate> predicates = new ArrayList<>();
			keysToFilter.forEach((key, value) -> {
				if (key.equals("enabled") && value.equals("true"))
					predicates.add(builder.equal(root.get(key), true));
				else if (key.equals("enabled") && value.equals("false"))
					predicates.add(builder.equal(root.get(key), false));
				else
					predicates.add(builder.equal(root.get(key), value));
			});

			criteria.where(builder.and(predicates.toArray(new Predicate[0])));
			data = getSession().createQuery(criteria)
					.setFirstResult(page-1)
					.setMaxResults(pageSize)
					.getResultList();
		}
		return data;
	}

	/**
	 * Fetches only active services from the database
	 * 
	 * @return
	 */
	public List<Service> getActiveServices() {
		CriteriaBuilder builder = getSession().getCriteriaBuilder();
		CriteriaQuery<Service> criteria = builder.createQuery(Service.class);
		Root<Service> root = criteria.from(Service.class);
		criteria.where(builder.equal(root.get("enabled"), true));
		List<Service> data = getSession().createQuery(criteria).getResultList();
		return data;
	}

	/**
	 * Return service by its ID
	 * 
	 * @param id
	 * @return Service
	 */
	public Service getService(int id) {
		CriteriaBuilder builder = getSession().getCriteriaBuilder();
		CriteriaQuery<Service> criteria = builder.createQuery(Service.class);
		Root<Service> root = criteria.from(Service.class);
		criteria.where(builder.equal(root.get("id"), id));
		List<Service> data = getSession().createQuery(criteria).getResultList();
		return data.get(0);
	}

	/**
	 * Save the status, ping, response time, url of website by the scheduled jobs
	 * 
	 * @param monitor
	 */
	@Transactional
	public void saveWebsiteMonitor(UptimeMonitor monitor) {
		getSession().save(monitor);
	}

	/**
	 * Return the object which contains website status, url, average response time
	 * and uptime/downtime
	 * 
	 * @param websiteUrl
	 * @return
	 */
	public List<UptimeMonitor> getServiceStatus(String websiteUrl, int page, int pageSize) {
		CriteriaBuilder builder = getSession().getCriteriaBuilder();

		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		countQuery.select(builder.count(countQuery.from(UptimeMonitor.class)));
		Long count = getSession().createQuery(countQuery).getSingleResult();

		List<UptimeMonitor> data = new ArrayList<>();
		if(page < count.intValue()) {
			CriteriaQuery<UptimeMonitor> criteria = builder.createQuery(UptimeMonitor.class);
			Root<UptimeMonitor> root = criteria.from(UptimeMonitor.class);
			criteria.orderBy(builder.desc(root.get("id")));
			criteria.where(builder.like(root.get("website_url"), websiteUrl.trim()));
			data = getSession().createQuery(criteria).setFirstResult(page-1).setMaxResults(pageSize)
					.getResultList();
		}
		return data;
	}

}
