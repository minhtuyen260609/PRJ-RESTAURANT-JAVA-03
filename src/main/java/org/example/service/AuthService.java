package org.example.service;

import org.example.dao.UserDAO;
import org.example.model.User;
import org.example.util.PasswordHasher;

public class AuthService {
    private final UserDAO userDAO;

    public AuthService() {
        userDAO = new UserDAO();
        createDefaultUser("admin", "123456", "manager");
        createDefaultUser("chef", "123456", "chef");
    }

    public boolean registerCustomer(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        if (findByUsername(username) != null) {
            return false;
        }

        return userDAO.insertUser(
                username.trim(),
                PasswordHasher.hashPassword(password),
                "customer",
                true
        );
    }

    public User login(String username, String password) {
        User user = findByUsername(username);
        if (user == null) {
            return null;
        }
        if (!user.isActive()) {
            return null;
        }

        if (!PasswordHasher.checkPassword(password, user.getPassword())) {
            return null;
        }

        return user;
    }

    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    private void createDefaultUser(String username, String password, String role) {
        if (findByUsername(username) != null) {
            return;
        }
        userDAO.insertUser(username, PasswordHasher.hashPassword(password), role, true);
    }
}
