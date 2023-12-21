package org.example.Data;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @OneToMany
    private List<Certificate> certificates;

    public Student() {
    }

    public Student(String name, List<Certificate> certificates) {
        this.name = name;
        this.certificates = certificates;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Certificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<Certificate> certificates) {
        this.certificates = certificates;
    }
    public void addCertificates(Certificate certificate) {
        this.certificates.add(certificate);
    }
    public void rmCertificates(Certificate certificate) {
        this.certificates.remove(certificate);
    }
    public void clearCertificates() {
        certificates.clear();
    }
}
