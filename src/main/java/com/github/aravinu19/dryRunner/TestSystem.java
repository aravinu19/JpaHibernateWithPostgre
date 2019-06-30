package com.github.aravinu19.dryRunner;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class TestSystem {
	
	private static EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("JpaHibernateWithPostgre");

	public static void main(String[] args) {

		addCustomer(1, "Super", "su");
		addCustomer(2, "dev", "Arun");
		addCustomer(3, "papu", "Mani");
		addCustomer(4, "raj", "Aravind");
		
		getCustomer(2);
		getCustomers();
		
		changeFname(1, "Sundhar");
		
		deleteCustomer(4);
		
		ENTITY_MANAGER_FACTORY.close();

	}
	
	public static void addCustomer(int id, String fname, String lname) {
		EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
		EntityTransaction et = null;
		
		try {
			et = em.getTransaction();
			et.begin();
			Customer cust = new Customer();
			cust.setId(id);
			cust.setfName(fname);
			cust.setlName(lname);
			em.persist(cust);
			et.commit();
		} catch (Exception e) {
			if (et != null) {
				et.rollback();
			}
			e.printStackTrace();
		} finally {
			em.close();
		}
		
	}
	
	public static void getCustomer(int id) {
		EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
		String query = "SELECT c FROM Customer c WHERE c.id = :custID";
		
		TypedQuery<Customer> tq = em.createQuery(query, Customer.class);
		tq.setParameter("custID", id);
		Customer cust = null;
		
		try {
			cust = tq.getSingleResult();
			System.out.println(cust.getfName() + " " + cust.getlName());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			em.close();
		}
		
	}
	
	public static void getCustomers() {
		
		EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
		String strQuery = "SELECT c FROM Customer c WHERE c.id IS NOT NULL";
		
		TypedQuery<Customer> tq = em.createQuery(strQuery, Customer.class);
		List<Customer> custs;
		
		try {
			custs = tq.getResultList();
			custs.forEach(cust->System.out.println(cust.getfName() + " " + cust.getlName()));
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			em.close();
		}
		
	}
	
	public static void changeFname(int id, String fname) {
		EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
		EntityTransaction et = null;

		Customer cust = null;
		
		try {
			et = em.getTransaction();
			et.begin();

			cust = em.find(Customer.class, id);
			cust.setfName(fname);
			
			em.persist(cust);
			et.commit();

		} catch (Exception e) {
			if (et != null) {
				et.rollback();
			}
			e.printStackTrace();
		} finally {
			em.close();
		}
		
	}
	
	public static void deleteCustomer(int id) {
		EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
		EntityTransaction et = null;

		Customer cust = null;
		
		try {
			
			et = em.getTransaction();
			et.begin();
			cust = em.find(Customer.class, id);
			em.remove(cust);
			et.commit();

		} catch (Exception e) {
			
			if (et != null) {
				et.rollback();
			}
			e.printStackTrace();
			
		} finally {
			
			em.close();
			
		}
		
	}

}
