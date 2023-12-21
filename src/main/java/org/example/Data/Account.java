package org.example.Data;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String phone;
    private String image;
    private String user;
    private String pass;
    private String name;
    private int age;
    private int status;
    @OneToMany
    private List<HistoryAccount> historyAccount = new ArrayList<>();

    public Account() {
    }

    public Account(String phone, String image, String user, String pass,
                   String name, int age, int status, List<HistoryAccount> historyAccount) {
        this.phone = phone;
        this.image = image;
        this.user = user;
        this.pass = pass;
        this.name = name;
        this.age = age;
        this.status = status;
        this.historyAccount = historyAccount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<HistoryAccount> getHistoryAccount() {
        return historyAccount;
    }
    public void addHistoryAccount(HistoryAccount historyAccount1){
        this.historyAccount.add(historyAccount1);
    }
    public void setHistoryAccount(List<HistoryAccount> historyAccount) {
        this.historyAccount = historyAccount;
    }
}
