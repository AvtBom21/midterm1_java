package org.example.Data;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class HistoryAccountDAO implements Repository<HistoryAccount>{
    private Session session;

    public HistoryAccountDAO() {
    }

    public HistoryAccountDAO(Session session) {
        this.session = session;
    }

    @Override
    public boolean add(HistoryAccount item) {
        Transaction transaction = session.beginTransaction();
        try{
            session.persist(item);
            transaction.commit();
            return true;
        }
        catch (Exception e){
            transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public HistoryAccount get(int id) {
        Transaction transaction = session.beginTransaction();
        HistoryAccount account = null;
        org.hibernate.query.Query<HistoryAccount> query = session.createQuery("select p from HistoryAccount p where p.id = :stt", HistoryAccount.class);
        query.setParameter("stt", id);
        account = query.uniqueResult();
        transaction.commit();
        return account;
    }

    @Override
    public List<HistoryAccount> getAll() {
        Transaction transaction = session.beginTransaction();
        List<HistoryAccount> accounts = null;
        org.hibernate.query.Query<HistoryAccount> query = session.createQuery("select A from HistoryAccount A",HistoryAccount.class);
        accounts = query.getResultList();
        transaction.commit();
        return accounts;
    }

    @Override
    public boolean remove(String id) {
        return false;
    }

    @Override
    public boolean remove(HistoryAccount item) {
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            // Trước khi xóa HistoryAccount, xóa liên kết trong Account
            Account account = item.getAccount();
            account.getHistoryAccount().remove(item);

            session.update(account); // Cập nhật Account sau khi xóa liên kết

            // Xóa HistoryAccount
            session.delete(item);

            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(HistoryAccount item) {
        return false;
    }
}
