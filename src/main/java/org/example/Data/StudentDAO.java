package org.example.Data;

import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.List;

public class StudentDAO {
    private Session session;

    public StudentDAO(Session session) {
        this.session = session;
    }

    public boolean add(Student student) {
        Transaction transaction = session.beginTransaction();

        try {
            session.persist(student);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }
    public Student getByName(String name) {
        Transaction transaction = session.beginTransaction();
        Student student = null;

        try {
            // Use a TypedQuery to select a Student by name
            TypedQuery<Student> query = session.createQuery("FROM Student WHERE name = :name", Student.class);
            query.setParameter("name", name);

            // Execute the query and retrieve the result
            student = query.getSingleResult();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return student;
    }
    public Student get(int id) {
        Transaction transaction = session.beginTransaction();
        Student student = null;

        try {
            student = session.get(Student.class, id);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return student;
    }

    public List<Student> getAll() {
        Transaction transaction = session.beginTransaction();
        List<Student> students = null;

        try {
            students = session.createQuery("FROM Student", Student.class).getResultList();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return students;
    }

    public boolean remove(int id) {
        Transaction transaction = session.beginTransaction();

        try {
            Student student = session.get(Student.class, id);
            if (student != null) {
                session.delete(student);
                if (transaction.getStatus() == TransactionStatus.ACTIVE) {
                    transaction.commit();
                }
                return true;
            } else {
                if (transaction.getStatus() == TransactionStatus.ACTIVE) {
                    transaction.rollback();
                }
                return false;
            }
        } catch (Exception e) {
            if (transaction.getStatus() == TransactionStatus.ACTIVE) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }


    public boolean update(Student student) {
        Transaction transaction = session.beginTransaction();

        try {
            session.merge(student);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }
}
