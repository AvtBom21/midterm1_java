package org.example.Data;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {
    private Session session;
    public HibernateUtils(){
        this.session = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(Account.class)
                .addAnnotatedClass(Student.class)
                .addAnnotatedClass(Certificate.class)
                .addAnnotatedClass(HistoryAccount.class).buildSessionFactory().openSession();
    }

    public Session getSession() {
        return session;
    }
}
