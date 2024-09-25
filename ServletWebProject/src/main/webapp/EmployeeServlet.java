package com.example.servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/employee")
public class EmployeeServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/db_employee";
    private static final String USER = "root"; // change as per your DB setup
    private static final String PASS = "password"; // change as per your DB setup

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("insert".equals(action)) {
            insertEmployee(request, response);
        } else if ("update".equals(action)) {
            updateEmployee(request, response);
        } else if ("delete".equals(action)) {
            deleteEmployee(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("selectById".equals(action)) {
            selectById(request, response);
        } else if ("selectAll".equals(action)) {
            selectAll(request, response);
        }
    }

    private void insertEmployee(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        double salary = Double.parseDouble(request.getParameter("salary"));

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO employee (name, salary) VALUES (?, ?)")) {
            stmt.setString(1, name);
            stmt.setDouble(2, salary);
            stmt.executeUpdate();
            response.getWriter().write("Employee added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("Error adding employee.");
        }
    }

    private void updateEmployee(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        double salary = Double.parseDouble(request.getParameter("salary"));

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement("UPDATE employee SET name=?, salary=? WHERE id=?")) {
            stmt.setString(1, name);
            stmt.setDouble(2, salary);
            stmt.setInt(3, id);
            stmt.executeUpdate();
            response.getWriter().write("Employee updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("Error updating employee.");
        }
    }

    private void deleteEmployee(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM employee WHERE id=?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            response.getWriter().write("Employee deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("Error deleting employee.");
        }
    }

    private void selectById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM employee WHERE id=?")) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            PrintWriter out = response.getWriter();
            if (rs.next()) {
                out.println("ID: " + rs.getInt("id"));
                out.println("Name: " + rs.getString("name"));
                out.println("Salary: " + rs.getDouble("salary"));
            } else {
                out.println("Employee not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("Error retrieving employee.");
        }
    }

    private void selectAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Employee> employees = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM employee");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Employee emp = new Employee();
                emp.setId(rs.getInt("id"));
                emp.setName(rs.getString("name"));
                emp.setSalary(rs.getDouble("salary"));
                employees.add(emp);
            }

            PrintWriter out = response.getWriter();
            for (Employee emp : employees) {
                out.println("ID: " + emp.getId() + ", Name: " + emp.getName() + ", Salary: " + emp.getSalary());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("Error retrieving employees.");
        }
    }
}
