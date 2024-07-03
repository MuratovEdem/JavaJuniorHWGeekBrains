package org.example.seminar3;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class JDBC {
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:test")) {
            createTableDepartment(connection);
            createTablePerson(connection);
            insertData(connection);
            selectData(connection);

            System.out.println(getPersonDepartmentName(connection, 3));

        } catch (SQLException e) {
            System.err.println("Во время подключения произошла ошибка: " + e.getMessage());
        }
    }

    // 4 задание
    private static String getPersonDepartmentName(Connection connection, long personId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("""
                    SELECT person.id AS PersonID, person.name AS Name, person.age AS age, person.active AS active, 
                            department.name AS Dep_name, department.id AS Dep_id FROM person
                    JOIN department ON person.id_department = department.id
                    where person.id = ?
                         """)) {
            statement.setLong(1, personId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("Name") + " - " + resultSet.getString("Dep_name");
            } else throw new IllegalArgumentException("пользователь  c id не найден");
        }
    }

    // 1 задание
    private static void createTablePerson(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
        CREATE TABLE person (
         	id BIGINT AUTO_INCREMENT PRIMARY KEY,
            name VARCHAR(256),
            age INTEGER,
            active BOOLEAN,
            id_department INTEGER,
            CONSTRAINT department_fk FOREIGN KEY (id_department) REFERENCES department (id)
         );
        """);
        } catch (SQLException e) {
            System.err.println("Во время создания таблицы произошла ошибка: " + e.getMessage());
            throw e;
        }
        System.out.println("Создана таблица person");
    }

    // 2 задание
    private static void createTableDepartment(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
        CREATE TABLE department (
            id BIGINT AUTO_INCREMENT PRIMARY KEY,
            name VARCHAR(256) NOT NULL)
        """);
        } catch (SQLException e) {
            System.err.println("Во время создания таблицы произошла ошибка: " + e.getMessage());
            throw e;
        }
        System.out.println("Создана таблица department");
    }


    private static void insertData(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            StringBuilder insertInPerson = new StringBuilder("insert into person(id, name, age, active, id_department) values\n");
            StringBuilder insertInDepartment = new StringBuilder("insert into department(id, name) values\n");

            for (int i = 1; i <= 10; i++) {
                int age = ThreadLocalRandom.current().nextInt(20, 60);
                int department = ThreadLocalRandom.current().nextInt(1, 10);
                boolean active = ThreadLocalRandom.current().nextBoolean();
                insertInPerson.append(String.format("(%s, '%s', %s, %s, %s)", i, "Person #" + i, age, active, department));
                insertInDepartment.append(String.format("(%s, '%s')", i, "Department #" + i));

                if (i != 10) {
                    insertInPerson.append(",\n");
                    insertInDepartment.append(",\n");
                }
            }

            statement.executeUpdate(insertInDepartment.toString());
            int insertCount = statement.executeUpdate(insertInPerson.toString());
            System.out.println("Вставлено строк: " + insertCount);
        }
    }

    private static List<String> selectNamesByAge(Connection connection, String age) throws SQLException {
//    try (Statement statement = connection.createStatement()) {
//      statement.executeQuery("select name from person where age = " + age);
//      // where age = 1 or 1=1
//    }

        try (PreparedStatement statement =
                     connection.prepareStatement("select name from person where age = ?")) {
            statement.setInt(1, Integer.parseInt(age));
            ResultSet resultSet = statement.executeQuery();

            List<String> names = new ArrayList<>();
            while (resultSet.next()) {
                names.add(resultSet.getString("name"));
            }
            return names;
        }
    }

    private static void selectData(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("""
        select *
        from person""");

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                long id_department = resultSet.getLong("id_department");
                // persons.add(new Person(id, name, age))
                System.out.println("Найдена строка: [id = " + id + ", name = " + name + ", age = " + age + " id_department = " + id_department);
            }
        }

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("""
        select *
        from department""");

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                // persons.add(new Person(id, name, age))
                System.out.println("Найдена строка: [id = " + id + ", name = " + name + "]");
            }
        }
    }

}
