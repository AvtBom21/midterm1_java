package org.example.Data;

import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CertificateDAO implements Repository<Certificate> {
    private Session session;

    public CertificateDAO(Session session) {
        this.session = session;
    }

    @Override
    public boolean add(Certificate item) {
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(item);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Certificate get(int id) {
        Certificate certificate = session.get(Certificate.class, id);
        return certificate;
    }

    public Certificate getByName(String certificateName) {
        Query query = session.createQuery("FROM Certificate c WHERE c.name = :certName", Certificate.class);
        query.setParameter("certName", certificateName);

        List<Certificate> result = query.getResultList();

        if (result != null && result.size() > 0) {
            return result.get(0);
        } else {
            return null; // Trả về null nếu không tìm thấy Certificate có tên nhất định
        }
    }

    @Override
    public List<Certificate> getAll() {
        List<Certificate> certificates = session.createQuery("FROM Certificate", Certificate.class).list();
        return certificates;
    }

    @Override
    public boolean remove(String id) {
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createQuery("DELETE FROM Certificate p where p.id = :certId");
            query.setParameter("certId", Integer.parseInt(id));
            int result = query.executeUpdate();
            transaction.commit();
            return result == 1;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }
    public boolean removeByStudentId(int studentId) {
        Transaction transaction = session.beginTransaction();

        try {
            // Sử dụng HQL để xóa Certificate dựa trên studentId
            Query query = session.createQuery("DELETE FROM Certificate c WHERE c.student.id = :studentId");
            query.setParameter("studentId", studentId);

            int result = query.executeUpdate();

            transaction.commit();

            return result > 0;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean remove(Certificate item) {
        Transaction transaction = session.beginTransaction();
        try {
            // Kiểm tra xem certificate có tồn tại trong cơ sở dữ liệu hay không
            Certificate existingCertificate = session.get(Certificate.class, item.getId());
            if (existingCertificate != null) {
                // Kiểm tra xem có Student nào đang tham chiếu đến Certificate không
                Student student = existingCertificate.getStudent();
                if (student != null) {
                    // Xóa liên kết giữa Student và Certificate
                    student.rmCertificates(existingCertificate); // Assuming you have a method to remove a certificate from the student
                }

                // Xóa Certificate khỏi cơ sở dữ liệu
                session.remove(existingCertificate);
                transaction.commit();
                return true;
            } else {
                // Certificate không tồn tại trong cơ sở dữ liệu
                transaction.rollback();
                return false;
            }
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean update(Certificate certificate) {
        Transaction transaction = session.beginTransaction();

        try {
            // Kiểm tra xem certificate có tồn tại trong cơ sở dữ liệu hay không
            Certificate existingCertificate = session.get(Certificate.class, certificate.getId());
            if (existingCertificate != null) {
                existingCertificate.setName(certificate.getName());
                existingCertificate.setDescribe(certificate.getDescribe());

                session.merge(existingCertificate);
                transaction.commit();
                return true;
            } else {
                // Certificate không tồn tại trong cơ sở dữ liệu
                transaction.rollback();
                return false;
            }
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }
}
