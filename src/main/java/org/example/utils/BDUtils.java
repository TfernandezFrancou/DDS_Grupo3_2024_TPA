package org.example.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;


public class BDUtils {

    private BDUtils(){}
    private static final EntityManagerFactory factory;

    static {
        factory = Persistence.createEntityManagerFactory("demo-hibernate-PU");
    }

    public static EntityManager getEntityManager() {
        return factory.createEntityManager();
    }

    public static EntityTransaction comenzarTransaccion(EntityManager em) {
        EntityTransaction tx = em.getTransaction();
        if (!tx.isActive()) {
            tx.begin();
        }
        return tx;
    }

    public static void commit(EntityManager em) {
        EntityTransaction tx = em.getTransaction();
        if (tx.isActive()) {
            tx.commit();
        }
    }

    public static void rollback(EntityManager em) {
        EntityTransaction tx = em.getTransaction();
        if (tx.isActive()) {
            tx.rollback();
        }
    }

}
