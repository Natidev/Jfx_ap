package ap.jfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class ManageData {
    String databaseName;
    static String url = "jdbc:mysql://localhost:3306/eventmanagment";
    String password;
    Connection mysql;
    Statement st;
    String tablename = "events";
    PreparedStatement insert2Events;
    PreparedStatement insert2Events2;
    PreparedStatement modifyData;

    public ManageData(String databaseName) {
        this.password = Password.getPassword();
        this.databaseName = databaseName;
        try {
            mysql = DriverManager.getConnection(url, "root", password);
            st = mysql.createStatement();
            insert2Events = mysql.prepareStatement("insert into events(e_title,organizer,event_date,location) " +
                    "values(?,?,?,?)");
            insert2Events2 = mysql.prepareStatement("insert into events(e_title,organizer,event_date,location,description) " +
                    "values(?,?,?,?,?)");
            modifyData = mysql.prepareStatement("Update eventmanagment.events set e_title=?, event_date=?, location=?,description=? where id=?");
            System.out.println("connected");
            //Loger.Success("Connected successfully");
        } catch (SQLException e) {
            Loger.recordError(e.getMessage());
        }
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public String getDescription(int id) {
        try {
            ResultSet insertedData = st.executeQuery("select description from " + tablename + " where id=" + id);

            insertedData.next();
            return insertedData.getString("description");
        } catch (SQLException e) {
            Loger.recordError(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public boolean createTable() {
        try {
            //st.execute("CREATE DATABASE eventmanagment;");
            st.execute("CREATE TABLE `events` (\n" +
                    "  id int NOT NULL AUTO_INCREMENT,\n" +
                    "  e_title varchar(40) DEFAULT NULL,\n" +
                    "  organizer varchar(20) DEFAULT NULL,\n" +
                    "  PRIMARY KEY (`id`)\n" +
                    ")");
            Loger.Success("Table is created");
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Loger.recordError(e.getMessage());
            return false;
        }

    }

    public boolean insertEvent(String organizer, String eventTitle, String date, String location) {
        try {
            insert2Events.setString(1, eventTitle);
            insert2Events.setString(2, organizer);
            insert2Events.setString(3, date);
            insert2Events.setString(4, location);
            Loger.Success("Inserted an event Succesfully");
            return insert2Events.execute();
        } catch (SQLException e) {
            Loger.recordError(e.getMessage());
            System.out.println(e.getMessage());
            return false;
        }

    }

    public boolean insertEvent(String organizer, String eventTitle, String date, String location, String description) {
        try {
            insert2Events2.setString(1, eventTitle);
            insert2Events2.setString(2, organizer);
            insert2Events2.setString(3, date);
            insert2Events2.setString(4, location);
            insert2Events2.setString(5, description);
            Loger.Success("Inserted an event Succesfully");
            return insert2Events2.execute();
        } catch (SQLException e) {
            Loger.recordError(e.getMessage());
            System.out.println(e.getMessage());
            return false;
        }

    }

    public ObservableList<EventData> getUpcomingEvents() {
        try {
            ResultSet rs = st.executeQuery("Select * from events where event_date>=current_Date()");
            ResultSet rsCount = rs;
            ResultSetMetaData rsm = rs.getMetaData();
            int ic = rsm.getColumnCount();
            ObservableList<EventData> tableData = FXCollections.observableArrayList();

            while (rs.next()) {
                tableData.add(new EventData(rs.getInt("id"), rs.getString("e_title"),
                        rs.getString("organizer"), rs.getString("event_date"),
                        rs.getString("location"), rs.getString("description")));
            }
            return tableData;

        } catch (SQLException e) {
            Loger.recordError(e.getMessage());
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean RemoveEvent(int eventID) {
        try {
            st.execute("delete from eventmanagment.events where id=" + eventID + ";");
            Loger.Success("An event with ID=" + eventID + " had been succesfully deleted");
        } catch (SQLException e) {
            Loger.recordError(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean ModifyEvent(String name, String date, String location, String description, int id) {
        try {
            modifyData.setString(1, name);
            modifyData.setString(2, date);
            modifyData.setString(3, location);
            modifyData.setString(4, description);
            modifyData.setInt(5, id);
            return modifyData.execute();
        } catch (SQLException e) {
            Loger.recordError(e.getLocalizedMessage());
        }
        return false;
    }
public void rsvp(int id){
    ResultSet rs= null;
    try {
        rs = mysql.createStatement().executeQuery("select rsvps from eventmanagment.events where id="+id);
    rs.next();
        int count=rs.getInt(1);
        count++;
        mysql.createStatement().execute("update eventmanagment.events set rsvps="+count+" where id="+id);
    } catch (SQLException e) {
        Loger.recordError(e.getLocalizedMessage());
    }
}
    public static void appendNameToFile(String name,String user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(user+".dat", true))) {
            writer.write(name);
            writer.newLine();
            System.out.println("Name appended successfully.");
        } catch (IOException e) {
            System.err.println("An error occurred while appending the name: " + e.getMessage());
        }
    }

    public static boolean checkNameInFile(String name,String user) {
        try (BufferedReader reader = new BufferedReader(new FileReader(user+".dat"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().equalsIgnoreCase(name)) {
                    return true;
                }
            }
        } catch (IOException e) {
            Loger.recordError(e.getLocalizedMessage());
        }
        return false;
    }
    public static ArrayList<String> getRsvped(String name){

        try{
            FileOutputStream a=new FileOutputStream(name+".dat",true);
            a.close();
            BufferedReader reader = new BufferedReader(new FileReader(name+".dat"));
            String line;
            ArrayList<String> rt=new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                rt.add(line);
            }
            return rt;
        } catch (IOException e) {
            Loger.recordError(e.getLocalizedMessage());
        }
        return null;
    }
}
