package com.org;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/login")   // matches form action="login"
public class LoginPage extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/demo";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASS = "root";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uname = request.getParameter("uname");
        String pwd   = request.getParameter("pwd");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try(Connection con = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
                PreparedStatement ps = con.prepareStatement(
                        "SELECT * FROM login WHERE username=? AND password=?")) {

                ps.setString(1, uname);
                ps.setString(2, pwd);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    RequestDispatcher rd = request.getRequestDispatcher("/Crud");
                    rd.forward(request, response);
                } else {
                    RequestDispatcher rd = request.getRequestDispatcher("fail.html");
                    rd.include(request, response);
                }

            }

        } catch (Exception e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }
        
        out.close();
    }
}
