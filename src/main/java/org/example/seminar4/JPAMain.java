package org.example.seminar4;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;


public class JPAMain {

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory()) {
            withSessionCRUD(sessionFactory);
        }
    }

    private static void withSessionCRUD(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            Post post = session.find(Post.class, 1L);
            System.out.println("Post(1) = " + post);
        }

        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Post post = new Post();
            post.setId(11L);
            post.setTitle("Post #11");

            session.persist(post); // insert
            tx.commit();
        }

        try (Session session = sessionFactory.openSession()) {
            Post toUpdate = session.find(Post.class, 22L);
            session.detach(toUpdate);
            toUpdate.setTitle("UPDATED");

            Transaction tx = session.beginTransaction();
            tx.commit();
        }

        try (Session session = sessionFactory.openSession()) {
            Post toDelete = session.find(Post.class, 1L);

            Transaction tx = session.beginTransaction();
            session.remove(toDelete);
            tx.commit();
        }
    }


}
