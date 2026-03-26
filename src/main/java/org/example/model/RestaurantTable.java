package org.example.model;

import org.example.constant.TableStatus;

public class RestaurantTable {
    private int id;
    private String name;
    private int capacity;
    private TableStatus status;

    public RestaurantTable() {
    }

    public RestaurantTable(int id, String name, int capacity, TableStatus status) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.status = status;
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public TableStatus getStatus() {
        return status;
    }

    public void setStatus(TableStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RestaurantTable{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                ", status=" + status +
                '}';
    }
}
