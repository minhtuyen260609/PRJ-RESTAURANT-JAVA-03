package org.example.service;

import org.example.dao.MenuItemDAO;
import org.example.model.MenuItem;

import java.util.List;

public class MenuService {
    private final MenuItemDAO menuItemDAO;
    public MenuService() {
        menuItemDAO = new MenuItemDAO();
    }
    public boolean addMenuItem(String name, double price, int quantity) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        if (price <= 0) {
            return false;
        }
        if (quantity <= 0) {
            return false;
        }
        return menuItemDAO.insertMenuItem(name.trim(), price, quantity);
    }

    public boolean updateMenuItem(int id, String name, double price, int quantity) {
        if (menuItemDAO.findById(id) == null) {
            return false;
        }
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        if (price <= 0) {
            return false;
        }
        if (quantity <= 0) {
            return false;
        }

        return menuItemDAO.updateMenuItem(id, name.trim(), price, quantity);
    }

    public boolean deleteMenuItem(int id) {
        if (menuItemDAO.findById(id) == null) {
            return false;
        }

        return menuItemDAO.deleteMenuItem(id);
    }

    public List<MenuItem> getAllMenuItems() {
        return menuItemDAO.findAll();
    }
    public MenuItem findById(int id) {
        return menuItemDAO.findById(id);
    }
    public List<MenuItem> searchByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return menuItemDAO.findAll();
        }

        return menuItemDAO.findByName(keyword.trim());
    }
}
