package Semag;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class SEMAG implements Serializable {

    private static final String save = "REPLACE INTO doge (serialized_id, object_name,serialized_object ) VALUES (?,?,?)";
    private static final String load = "SELECT serialized_object FROM doge WHERE serialized_id = ?";
    private static final String createTable = "CREATE TABLE IF NOT EXISTS doge(serialized_id int NOT NULL AUTO_INCREMENT, object_name varchar(255), serialized_object blob,PRIMARY KEY(serialized_id))";
    private static Connection connection = null;
//    private LinkedList<String> list = new LinkedList<>();
//    Thread t;
//    private transient DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    String url = "jdbc:mysql://localhost:3306/doge";
    String username = "root";
    String password = "@Dogepass1234";
    Window obj = new Window();
// USER INTERFACE

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        Scanner sc = new Scanner(System.in);
        SEMAG o = new SEMAG();
        o = (SEMAG) deSerializeJavaObjectFromDB(connection, 1);
        while (true) {
            System.out.println("login / register");
            String input1 = sc.nextLine();
            if (!o.obj.ac(input1)) {
                continue;
            }
            o.obj.userwindow();
            serializeJavaObjectToDB(connection, o);
            connection.close();
        }
    }

    public static void serializeJavaObjectToDB(Connection conn, Object objectToSerialize) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(createTable);
        ps.executeUpdate();
        ps = conn.prepareStatement(save, Statement.RETURN_GENERATED_KEYS);

        // just setting the class name
        ps.setObject(1, 1);
        ps.setString(2, objectToSerialize.getClass().getName());
        ps.setObject(3, objectToSerialize);

        ps.executeUpdate();

        ps.close();
        System.out.println("Java object serialized to database. Object: " + objectToSerialize);

    }

    public static Object deSerializeJavaObjectFromDB(Connection conn, long serialized_id) throws SQLException, IOException, ClassNotFoundException {
        PreparedStatement ps = conn.prepareStatement(load);

        ps.setLong(1, serialized_id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            byte[] buf = rs.getBytes(1);
            ObjectInputStream objectIn = null;
            if (buf != null) {
                objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));
            }

            Object deSerializedObject = objectIn.readObject();

            rs.close();
            ps.close();

            System.out.println("Java object de-serialized from database. Object: " + deSerializedObject + " Classname: " + deSerializedObject.getClass().getName());
            return deSerializedObject;

        }
        return new SEMAG();
    }

}
