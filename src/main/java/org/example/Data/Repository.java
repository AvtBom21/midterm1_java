package org.example.Data;

import java.util.List;

public interface Repository <T>{
    boolean add(T item);
    T get(int id);
    List<T> getAll();
    boolean remove(String id);
    boolean remove(T item);
    boolean update(T item);
}
