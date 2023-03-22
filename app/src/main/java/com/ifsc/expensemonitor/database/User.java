package com.ifsc.expensemonitor.database;

import java.util.ArrayList;
import java.util.List;

enum Status{
    NotPaid,
    Paid,
    Late
}

enum expenseType{
    Recurrent,
    Parcel,
    Single
}

public class User {
    public String email;
    public String password;
    public List<Expense> expenses = new ArrayList<>();
    public User() {}

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", expenses=" + expenses +
                '}';
    }
}

class Expense{
    public String id;

    public String name;
    public String description;
    public Status status;
    public float value;
    public int daysRemaining;
    public Notif notif;
    public Local local;
    public expenseType type;
    public List<Attach> attachments;

    public void load(String id, String name, String description, float value, expenseType type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.value = value;
        this.type = type;
        this.notif = new Notif();
        this.local = new Local();
        this.attachments = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", value=" + value +
                ", daysRemaining=" + daysRemaining +
                ", attachments=" + attachments +
                ", notif=" + notif +
                ", local=" + local +
                ", type=" + type +
                '}';
    }


    static class Attach {
        public String path;
    }

    static class Notif{
        public int daysBefore;

        @Override
        public String toString() {
            return "Notif{" +
                    ", daysBefore=" + daysBefore +
                    '}';
        }
    }

    static class Local{
        public String hood;
        public String city;
        public String address;

        @Override
        public String toString() {
            return "Local{" +
                    ", hood='" + hood + '\'' +
                    ", city='" + city + '\'' +
                    ", address='" + address + '\'' +
                    '}';
        }
    }

    static class Type {
        public expenseType eType;
        public Type(expenseType eType) {
            this.eType = eType;
            generate();
        }
        public void generate() {
            if(this.eType == expenseType.Single) {
                //TODO
            } else if(this.eType == expenseType.Parcel) {
                //TODO
            } else if (this.eType == expenseType.Recurrent) {
                //TODO
            }
        }
    }
}
