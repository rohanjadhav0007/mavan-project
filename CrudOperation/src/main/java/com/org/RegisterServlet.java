package com.org;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/register")  // form action="register"
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Your DB details
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/demo";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASS = "root";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uname = request.getParameter("uname");
        String email = request.getParameter("email");
        String pwd   = request.getParameter("pwd");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><body style='font-family:Arial; text-align:center;'>");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection con = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
                 PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO login(username, email, password) VALUES (?,?,?)"
                 )) {

                ps.setString(1, uname);
                ps.setString(2, email);
                ps.setString(3, pwd);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    out.println("<h2 style='color:green;'>Registration Successful!</h2>");
                    out.println("<p><a href='index.html'>Go to Login</a></p>");
                } else {
                    out.println("<h2 style='color:red;'>Registration Failed!</h2>");
                    out.println("<p><a href='register.html'>Try Again</a></p>");
                }

            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace(out);
            out.println("<h3>MySQL Driver not found.</h3>");
        } catch (SQLException e) {
            e.printStackTrace(out);
            out.println("<h3>SQL Error: " + e.getMessage() + "</h3>");
        }

        out.println("</body></html>");
        out.close();
    }
}
