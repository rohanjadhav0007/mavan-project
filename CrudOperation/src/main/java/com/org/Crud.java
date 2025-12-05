package com.org;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/Crud")
public class Crud extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String JDBC_URL  = "jdbc:mysql://localhost:3306/demo";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASS = "root";

    public Crud() {
        super();
    }

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            throw new ServletException("MySQL Driver not found", e);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";  
        }

        try {
            switch (action) {
                case "insert":
                    insertStudent(request, response);
                    break;
                case "delete":
                    deleteStudent(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "update":
                    updateStudent(request, response);
                    break;
                default:
                    listStudents(response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            PrintWriter out = response.getWriter();
            out.println("<h2>Error: " + e.getMessage() + "</h2>");
        }
    }

    // ================= CREATE =================
    private void insertStudent(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String name  = request.getParameter("name");
        String email = request.getParameter("email");

        String sql = "INSERT INTO student(name, email) VALUES(?, ?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.executeUpdate();
        }

        // After insert, show list
        listStudents(response);
    }

    private void listStudents(HttpServletResponse response) throws Exception {
        String sql = "SELECT id, name, email FROM student";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery();
             PrintWriter out = response.getWriter()) {

        	out.println("<html><head>");
        	out.println("<meta charset='UTF-8'>");
        	out.println("<title>Student CRUD</title>");
        	out.println("<style>/* ======= RESET ======= */\r\n"
        			+ "* {\r\n"
        			+ "    margin: 0;\r\n"
        			+ "    padding: 0;\r\n"
        			+ "    box-sizing: border-box;\r\n"
        			+ "}\r\n"
        			+ "\r\n"
        			+ "/* ======= BODY ======= */\r\n"
        			+ "body {\r\n"
        			+ "    font-family: \"Roboto\", Arial, sans-serif;\r\n"
        			+ "    background: #0f172a;           /* deep navy */\r\n"
        			+ "    color: #e5e7eb;\r\n"
        			+ "    min-height: 100vh;\r\n"
        			+ "    padding: 40px 0;\r\n"
        			+ "}\r\n"
        			+ "\r\n"
        			+ "/* Center content */\r\n"
        			+ "h2, h3, form, table, hr, a {\r\n"
        			+ "    max-width: 900px;\r\n"
        			+ "    margin-left: auto;\r\n"
        			+ "    margin-right: auto;\r\n"
        			+ "}\r\n"
        			+ "\r\n"
        			+ "/* Title */\r\n"
        			+ "h2 {\r\n"
        			+ "    text-align: center;\r\n"
        			+ "    font-size: 26px;\r\n"
        			+ "    margin-bottom: 20px;\r\n"
        			+ "    color: #f9fafb;\r\n"
        			+ "}\r\n"
        			+ "\r\n"
        			+ "/* Section headings */\r\n"
        			+ "h3 {\r\n"
        			+ "    margin-top: 25px;\r\n"
        			+ "    margin-bottom: 8px;\r\n"
        			+ "    font-size: 18px;\r\n"
        			+ "    color: #e5e7eb;\r\n"
        			+ "}\r\n"
        			+ "\r\n"
        			+ "/* Form card */\r\n"
        			+ "form {\r\n"
        			+ "    background: #020617;\r\n"
        			+ "    border: 1px solid #1f2937;\r\n"
        			+ "    padding: 18px 20px;\r\n"
        			+ "    border-radius: 10px;\r\n"
        			+ "    box-shadow: 0 15px 30px rgba(0,0,0,0.6);\r\n"
        			+ "    margin-bottom: 20px;\r\n"
        			+ "}\r\n"
        			+ "\r\n"
        			+ "/* Inputs */\r\n"
        			+ "input[type=\"text\"],\r\n"
        			+ "input[type=\"email\"] {\r\n"
        			+ "    width: 100%;\r\n"
        			+ "    padding: 10px 12px;\r\n"
        			+ "    margin: 8px 0 16px;\r\n"
        			+ "    border-radius: 6px;\r\n"
        			+ "    border: 1px solid #1f2937;\r\n"
        			+ "    background: #020617;\r\n"
        			+ "    color: #e5e7eb;\r\n"
        			+ "    font-size: 14px;\r\n"
        			+ "    transition: border 0.2s, box-shadow 0.2s, background 0.2s;\r\n"
        			+ "}\r\n"
        			+ "\r\n"
        			+ "input[type=\"text\"]:focus,\r\n"
        			+ "input[type=\"email\"]:focus {\r\n"
        			+ "    outline: none;\r\n"
        			+ "    border-color: #38bdf8;\r\n"
        			+ "    box-shadow: 0 0 0 2px rgba(56, 189, 248, 0.4);\r\n"
        			+ "    background: #020617;\r\n"
        			+ "}\r\n"
        			+ "\r\n"
        			+ "/* Button */\r\n"
        			+ "input[type=\"submit\"] {\r\n"
        			+ "    background: linear-gradient(135deg, #22c55e, #16a34a);\r\n"
        			+ "    color: #f9fafb;\r\n"
        			+ "    border: none;\r\n"
        			+ "    border-radius: 999px;\r\n"
        			+ "    font-size: 14px;\r\n"
        			+ "    padding: 8px 22px;\r\n"
        			+ "    cursor: pointer;\r\n"
        			+ "    font-weight: 600;\r\n"
        			+ "    transition: transform 0.1s, box-shadow 0.2s, opacity 0.2s;\r\n"
        			+ "    box-shadow: 0 10px 25px rgba(22, 163, 74, 0.4);\r\n"
        			+ "}\r\n"
        			+ "\r\n"
        			+ "input[type=\"submit\"]:hover {\r\n"
        			+ "    transform: translateY(-1px);\r\n"
        			+ "    opacity: 0.95;\r\n"
        			+ "}\r\n"
        			+ "\r\n"
        			+ "/* Separator */\r\n"
        			+ "hr {\r\n"
        			+ "    border: none;\r\n"
        			+ "    border-top: 1px solid #1f2937;\r\n"
        			+ "    margin: 25px auto;\r\n"
        			+ "}\r\n"
        			+ "\r\n"
        			+ "/* Table */\r\n"
        			+ "table {\r\n"
        			+ "    width: 900px;\r\n"
        			+ "    border-collapse: collapse;\r\n"
        			+ "    background: #020617;\r\n"
        			+ "    border-radius: 10px;\r\n"
        			+ "    overflow: hidden;\r\n"
        			+ "    box-shadow: 0 18px 40px rgba(0,0,0,0.8);\r\n"
        			+ "    margin-bottom: 40px;\r\n"
        			+ "}\r\n"
        			+ "\r\n"
        			+ "th, td {\r\n"
        			+ "    padding: 10px 14px;\r\n"
        			+ "    font-size: 14px;\r\n"
        			+ "}\r\n"
        			+ "\r\n"
        			+ "th {\r\n"
        			+ "    background: #111827;\r\n"
        			+ "    color: #e5e7eb;\r\n"
        			+ "    text-align: left;\r\n"
        			+ "    font-weight: 600;\r\n"
        			+ "    border-bottom: 1px solid #1f2937;\r\n"
        			+ "}\r\n"
        			+ "\r\n"
        			+ "tr:nth-child(even) {\r\n"
        			+ "    background: #020617;\r\n"
        			+ "}\r\n"
        			+ "\r\n"
        			+ "tr:nth-child(odd) {\r\n"
        			+ "    background: #030712;\r\n"
        			+ "}\r\n"
        			+ "\r\n"
        			+ "tr:hover td {\r\n"
        			+ "    background: #111827;\r\n"
        			+ "}\r\n"
        			+ "\r\n"
        			+ "/* Links */\r\n"
        			+ "a {\r\n"
        			+ "    color: #38bdf8;\r\n"
        			+ "    text-decoration: none;\r\n"
        			+ "    font-weight: 500;\r\n"
        			+ "}\r\n"
        			+ "\r\n"
        			+ "a[href*=\"delete\"] {\r\n"
        			+ "    color: #fb7185;\r\n"
        			+ "}\r\n"
        			+ "\r\n"
        			+ "a:hover {\r\n"
        			+ "    text-decoration: underline;\r\n"
        			+ "}\r\n"
        			+ "\r\n"
        			+ "/* Back link */\r\n"
        			+ "body > a {\r\n"
        			+ "    display: block;\r\n"
        			+ "    max-width: 900px;\r\n"
        			+ "    margin-top: 15px;\r\n"
        			+ "    font-size: 14px;\r\n"
        			+ "}\r\n"
        			+ "</style>");


            out.println("<h2>Student CRUD Example</h2>");

            out.println("<h3>Add New Student</h3>");
            out.println("<form action='Crud' method='post'>");
            out.println("Name: <input type='text' name='name' required /> <br/>");
            out.println("Email: <input type='email' name='email' required /> <br/>");
            out.println("<input type='hidden' name='action' value='insert' />");
            out.println("<input type='submit' value='Save' />");
            out.println("</form>");
            out.println("<hr/>");

            out.println("<h3>All Students</h3>");
            out.println("<table border='1' cellpadding='5' cellspacing='0'>");
            out.println("<tr><th>ID</th><th>Name</th><th>Email</th><th>Actions</th></tr>");

            while (rs.next()) {
                int id       = rs.getInt("id");
                String name  = rs.getString("name");
                String email = rs.getString("email");

                out.println("<tr>");
                out.println("<td>" + id + "</td>");
                out.println("<td>" + name + "</td>");
                out.println("<td>" + email + "</td>");
                out.println("<td>");
                out.println("<a href='Crud?action=edit&id=" + id + "'>Edit</a> | ");
                out.println("<a href='Crud?action=delete&id=" + id + "' onclick='return confirm(\"Delete this?\")'>Delete</a>");
                out.println("</td>");
                out.println("</tr>");
            }

            out.println("</table>");
            out.println("</body></html>");
        }
    }

    // ================= DELETE =================
    private void deleteStudent(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String idStr = request.getParameter("id");
        int id = Integer.parseInt(idStr);

        String sql = "DELETE FROM student WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }

        listStudents(response);
    }

 // ================= SHOW EDIT FORM =================
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String idStr = request.getParameter("id");
        int id = Integer.parseInt(idStr);

        String sql = "SELECT id, name, email FROM student WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery();
                 PrintWriter out = response.getWriter()) {

                if (!rs.next()) {
                    out.println("<h2>Student not found</h2>");
                    return;
                }

                String name  = rs.getString("name");
                String email = rs.getString("email");

                out.println("<!DOCTYPE html>");
                out.println("<html><head>");
                out.println("<meta charset='UTF-8'>");
                out.println("<title>Edit Student</title>");
                out.println("<style>"
                        + "body {"
                        + "    font-family: \"Poppins\", sans-serif;"
                        + "    background: linear-gradient(135deg, #ece9f6, #d7cff5);"
                        + "    display: flex;"
                        + "    justify-content: center;"
                        + "    align-items: center;"
                        + "    height: 100vh;"
                        + "    margin: 0;"
                        + "}"
                        + ".edit-card {"
                        + "    width: 420px;"
                        + "    padding: 32px;"
                        + "    background: rgba(255, 255, 255, 0.6);"
                        + "    backdrop-filter: blur(18px);"
                        + "    border-radius: 20px;"
                        + "    border: 1px solid rgba(120, 115, 190, 0.3);"
                        + "    box-shadow: 0 18px 30px rgba(120, 115, 190, 0.3);"
                        + "    animation: fadeIn 0.7s ease;"
                        + "}"
                        + ".edit-card h2 {"
                        + "    text-align: center;"
                        + "    color: #5b4bc6;"
                        + "    margin-bottom: 20px;"
                        + "    font-size: 26px;"
                        + "}"
                        + ".input-box {"
                        + "    margin-bottom: 16px;"
                        + "}"
                        + ".input-box label {"
                        + "    color: #6b63c4;"
                        + "    font-weight: 600;"
                        + "    font-size: 14px;"
                        + "    display: block;"
                        + "    margin-bottom: 6px;"
                        + "}"
                        + ".input-box input {"
                        + "    width: 100%;"
                        + "    padding: 11px;"
                        + "    border: 1px solid #b8b3e9;"
                        + "    border-radius: 10px;"
                        + "    background: rgba(255, 255, 255, 0.9);"
                        + "    font-size: 15px;"
                        + "}"
                        + ".input-box input:focus {"
                        + "    outline: none;"
                        + "    border-color: #6b63c4;"
                        + "    box-shadow: 0 0 5px rgba(107, 99, 196, 0.5);"
                        + "}"
                        + ".btn-submit {"
                        + "    width: 100%;"
                        + "    padding: 12px;"
                        + "    background: #6b63c4;"
                        + "    color: white;"
                        + "    border: none;"
                        + "    border-radius: 30px;"
                        + "    cursor: pointer;"
                        + "    font-weight: 600;"
                        + "    font-size: 15px;"
                        + "    margin-top: 8px;"
                        + "}"
                        + ".btn-submit:hover {"
                        + "    background: #5a52b5;"
                        + "}"
                        + ".back-link {"
                        + "    margin-top: 15px;"
                        + "    text-align: center;"
                        + "    display: block;"
                        + "    color: #6b63c4;"
                        + "    text-decoration: none;"
                        + "    font-size: 14px;"
                        + "}"
                        + ".back-link:hover {"
                        + "    text-decoration: underline;"
                        + "}"
                        + "@keyframes fadeIn {"
                        + "    from { opacity: 0; transform: translateY(20px); }"
                        + "    to   { opacity: 1; transform: translateY(0); }"
                        + "}"
                        + "</style>");
                out.println("</head><body>");

                out.println("<div class='edit-card'>");
                out.println("<h2>Edit Student</h2>");

                out.println("<form action='Crud' method='post'>");
                out.println("<input type='hidden' name='action' value='update' />");
                out.println("<input type='hidden' name='id' value='" + id + "' />");

                out.println("<div class='input-box'>");
                out.println("<label for='name'>Name</label>");
                out.println("<input type='text' id='name' name='name' value='" + name + "' required />");
                out.println("</div>");

                out.println("<div class='input-box'>");
                out.println("<label for='email'>Email</label>");
                out.println("<input type='email' id='email' name='email' value='" + email + "' required />");
                out.println("</div>");

                out.println("<button type='submit' class='btn-submit'>Update</button>");
                out.println("</form>");

                out.println("<a href='Crud' class='back-link'>&larr; Back to List</a>");
                out.println("</div>"); // .edit-card

                out.println("</body></html>");
            }
        }
    }

    // ================= UPDATE =================
    private void updateStudent(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        int id       = Integer.parseInt(request.getParameter("id"));
        String name  = request.getParameter("name");
        String email = request.getParameter("email");

        String sql = "UPDATE student SET name = ?, email = ? WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setInt(3, id);

            ps.executeUpdate();
        }

        listStudents(response);
    }
}
