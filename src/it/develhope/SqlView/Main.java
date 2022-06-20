package it.develhope.SqlView;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
/**
 * @author Tania Ielpo
 */

import static java.sql.DriverManager.getConnection;

public class Main {
    public static void main(String[] args) throws SQLException {
        //Connection with the database
        Connection connection = getConnection(
                "jdbc:mysql://localhost:3306/newdb",
                "developer",
                "Developer123");
        Statement statement= connection.createStatement();

        //creating views
        createView(statement, "italian", "Italy");
        createView(statement, "german", "Germany");

        ArrayList<Student> italianStudents=new ArrayList<>();
        ArrayList<Student> germanStudents=new ArrayList<>();

        italianStudents=executeSelect(statement, italianStudents, "italian");
        germanStudents=executeSelect(statement, germanStudents,"german");
        connection.close();

    }

    /**
     * execute a select using the view
     * and put the result in an ArrayList of Student objects
     * @param statement
     * @param Students array of students
     * @param nationality
     * @throws SQLException
     * @return
     */

    private static ArrayList<Student> executeSelect(@NotNull Statement statement, ArrayList<Student> Students, String nationality) throws SQLException {
        ResultSet resultSet=statement.executeQuery("SELECT * FROM newdb."+nationality+"_students");
        Student student;
        while(resultSet.next()){
            student=new Student(resultSet.getString("first_name"),
                    resultSet.getString("last_name") );
            Students.add(student);
        }
        return Students;
    }

    /**
     * create a view that gets all the name and surname of the  students of a certain nationality
     * @param statement
     * @param nameOfView
     * @param country
     * @throws SQLException
     */

    private static void createView(@NotNull Statement statement, String nameOfView, String country)
            throws SQLException {
        statement.execute("CREATE VIEW "+nameOfView+"_students AS " +
                "SELECT first_name, last_name FROM students " +
                "WHERE country = '"+country+"'");
    }
}
