import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCMain {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/demo2";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final String ALL_USERS_WITH_LASTNAME = "SELECT * from users where lastName=?";
    private static final String USER_BY_EMAIL_PROCEDURE = "{call selectEmail(?}";

    public static void main(String[] args) {
        try(Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            PreparedStatement preparedStatement = connection.prepareStatement(ALL_USERS_WITH_LASTNAME);
            CallableStatement callableStatement = connection.prepareCall(USER_BY_EMAIL_PROCEDURE)
        ) {
//            statementUpdate(statement);
            ResultSet allUsers = statementQuery(statement);
            List<User> users = new ArrayList<>();
            while (allUsers.next()){
                User user = User.builder()
                        .id(allUsers.getInt(1))
                        .firstName(allUsers.getString(2))
                        .lastName(allUsers.getString("lastName"))
                        .birthDate(allUsers.getDate(5))
                        .email(allUsers.getString("email"))
                        .build();
                users.add(user);
            }
            users.forEach( u -> System.out.println(u));
//            preperedStatementSelect(preparedStatement);

//            callableStatement(callableStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void callableStatement(CallableStatement callableStatement) throws SQLException {
        callableStatement.setString(1,"andrei@gmail.com");
        ResultSet resultSet = callableStatement.executeQuery();
    }

    private static void preperedStatementSelect(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1,"Andrei");
        ResultSet resultSet = preparedStatement.executeQuery();
    }

    private static ResultSet statementQuery(Statement statement) throws SQLException {
        String getAllUsers = "Select * from users";
        return statement.executeQuery(getAllUsers);
    }

    private static void statementUpdate(Statement statement) throws SQLException {
        String insertAStudent = "INSERT INTO users VALUES (null, 'Alexandru', 'Marin', 'alexandru.marin2@gmail.com', '1998/2/24')";
        statement.executeUpdate(insertAStudent);
    }
}
