package org.example.Account;

import javax.servlet.http.Cookie;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class UserService {
    public static final UserService USER_SERVICE = new UserService();
    public Connection connection;
    public UserProfile getUser(String filter, String arg) throws SQLException, ClassNotFoundException {
        PreparedStatement st = getConnection().prepareStatement("SELECT login, password, email FROM users WHERE " + filter + " = ?");
        st.setString(1, arg);
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            return new UserProfile(rs.getString("login"), rs.getString("password"), rs.getString("email"));
        } else {
            return null;
        }
    }
    public void addUser(UserProfile user) throws SQLException, ClassNotFoundException {
        PreparedStatement st = getConnection().prepareStatement("INSERT INTO users (login, password, email) VALUES (?, ?, ?)");
        st.setString(1, user.getLogin());
        st.setString(2, user.getPassword());
        st.setString(3, user.getEmail());
        st.executeUpdate();
    }
    public void addUserBySession(String session, UserProfile user) throws SQLException, ClassNotFoundException {
        PreparedStatement st = getConnection().prepareStatement("UPDATE users SET session = ? WHERE login = ?");
        st.setString(1, session);
        st.setString(2, user.getLogin());
        st.executeUpdate();
    }
    public void removeUserBySession(String session) throws SQLException, ClassNotFoundException {
        PreparedStatement st = getConnection().prepareStatement("UPDATE users SET session = ? WHERE session = ?");
        st.setString(1, null);
        st.setString(2, session);
        st.executeUpdate();
    }
    public boolean containsUserByLogin(String login) throws SQLException, ClassNotFoundException {
        PreparedStatement st = getConnection().prepareStatement("SELECT * FROM users WHERE login = ?");
        st.setString(1, login);
        ResultSet rs = st.executeQuery();
        return rs.next();
    }
    public UserProfile getUserByCookies(Cookie[] cookies) throws SQLException, ClassNotFoundException {
        String session;
        UserProfile user;
        if ((session = UserCookies.getValue(cookies, "JSESSIONID")) == null || (user = getUser("session", session)) == null) {
            return null;
        }
        return user;
    }

    private Connection getConnection() throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()) {
            return connection;
        }
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/file_manager", "root", "qwerty1234");
        return connection;
    }
}
