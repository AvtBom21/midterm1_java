package org.example.Data;

import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.PreparedStatement;
import java.util.List;

public class AccountDAO {
    private Session session;

    public AccountDAO() {
    }

    public AccountDAO(Session session) {
        this.session = session;
    }

    public boolean add(Account item) {
        try{
            session.persist(item);
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public Account get(String id) {
        Transaction transaction = session.beginTransaction();
        Account account = null;
        org.hibernate.query.Query<Account> query = session.createQuery("select p from Account p where p.phone = :stt", Account.class);
        query.setParameter("stt", id);
        account = query.uniqueResult();
        transaction.commit();
        return account;
    }

    public Account getbyUser(String User) {
        Transaction transaction = session.beginTransaction();
        Account account = null;
        org.hibernate.query.Query<Account> query = session.createQuery("select p from Account p where p.user = :stt", Account.class);
        query.setParameter("stt", User);
        account = query.uniqueResult();
        transaction.commit();
        return account;
    }

    public List<Account> getAll() {
        Transaction transaction = session.beginTransaction();
        List<Account> accounts = null;
        org.hibernate.query.Query<Account> query = session.createQuery("select A from Account A",Account.class);
        accounts = query.getResultList();
        transaction.commit();
        return accounts;
    }
    public boolean remove(String id) {
        Transaction transaction = session.beginTransaction();

        Query query = session.createQuery("DELETE FROM Account p where p.phone = ?1");
        query.setParameter(1, id);
        int result = query.executeUpdate();
        transaction.commit();
        return result == 1;
    }
    public boolean remove(Account item) {
        Transaction transaction = session.beginTransaction();

        try {
            // Xóa tất cả các HistoryAccount liên quan
            for (HistoryAccount historyAccount : item.getHistoryAccount()) {
                session.remove(historyAccount);
            }

            // Xóa Account
            session.remove(item);

            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Account item) {
        Transaction transaction = session.beginTransaction();

        try {

            Account account = session.get(Account.class, item.getId());
            account.setPhone(item.getPhone());
            account.setImage(item.getImage());
            account.setUser(item.getUser());
            account.setPass(item.getPass());
            account.setName(item.getName());
            account.setAge(item.getAge());
            account.setStatus(item.getStatus());

            session.merge(account);
            transaction.commit();
            return true;
        }
        catch (Exception e){
            transaction.rollback();
            return false;
        }
    }
}
