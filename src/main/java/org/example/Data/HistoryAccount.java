package org.example.Data;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
public class HistoryAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String dayLogin;
    private String timeLogin;
    @ManyToOne
    private Account account;

    public HistoryAccount() {
    }

    public HistoryAccount(Account account) {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        int year = currentDate.getYear();
        int month = currentDate.getMonthValue();
        int day = currentDate.getDayOfMonth();

        int hour = currentTime.getHour();
        int minute = currentTime.getMinute();

        this.dayLogin = String.format("%04d/%02d/%02d", year, month, day);
        this.timeLogin = String.format("%02d:%02d", hour, minute);

        this.account = account;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDayLogin() {
        return dayLogin;
    }

    public void setDayLogin(String dayLogin) {
        this.dayLogin = dayLogin;
    }

    public String getTimeLogin() {
        return timeLogin;
    }

    public void setTimeLogin(String timeLogin) {
        this.timeLogin = timeLogin;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

}
