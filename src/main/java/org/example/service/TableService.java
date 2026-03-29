package org.example.service;

import org.example.dao.TableDAO;
import org.example.model.RestaurantTable;

import java.util.List;

public class TableService {
    private final TableDAO tableDAO;

    public TableService() {
        tableDAO = new TableDAO();
    }

    public boolean addTable(String name, int capacity) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        if (capacity <= 0) {
            return false;
        }
        return tableDAO.insertTable(name.trim(), capacity, "empty");
    }

    public boolean updateTable(int id, String name, int capacity) {
        if (tableDAO.findById(id) == null) {
            return false;
        }
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        if (capacity <= 0) {
            return false;
        }
        return tableDAO.updateTable(id, name.trim(), capacity);
    }

    public boolean deleteTable(int id) {
        if (tableDAO.findById(id) == null) {
            return false;
        }

        return tableDAO.deleteTable(id);
    }

    public List<RestaurantTable> getAllTables() {
        return tableDAO.findAll();
    }
    public RestaurantTable findById(int id) {
        return tableDAO.findById(id);
    }
}
