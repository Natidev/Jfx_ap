package ap.jfx;

import ap.jfx.Loger;
import ap.jfx.Password;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

public class Controller {

        String databaseName;
        static String url="jdbc:mysql://localhost:3306/eventmanagment";
        Connection mysql;
        PreparedStatement pst;
    PreparedStatement signUppst;
        public Controller(){
    try{
        mysql= DriverManager.getConnection(url,"root", Password.getPassword());
        pst=mysql.prepareStatement("select password from eventmanagment.users where name=?");
        signUppst=mysql.prepareStatement("insert into eventmanagment.users(name, password, organizer ) values(?,?,?)");
    }catch(SQLException e){
        Loger.recordError(e.getLocalizedMessage());
    }
        }
        public boolean verify(String username,String password){
            try {
                pst.setString(1,username);
                ResultSet pass=pst.executeQuery();
                pass.next();
                return pass.getString("password").equals(password);
            } catch (SQLException e) {
                Loger.recordError(e.getLocalizedMessage());
            }
            return false;
        }
        public boolean signUp(String username,String password,boolean organizer){
            try {
                signUppst.setString(1,username);
                signUppst.setString(2,password);
            signUppst.setBoolean(3, organizer);
            signUppst.execute();
            return true;
            } catch (SQLException e) {
                Loger.recordError(e.getLocalizedMessage());

            }
        return false;
        }
        public ArrayList<Object> getUserData(String name){
            ArrayList<Object> rt=new ArrayList<>();
            try {
                ResultSet data=mysql.createStatement().executeQuery("select * from eventmanagment.users where name=\""+name+"\"");
                data.next();
                rt.add(data.getString("name"));
                rt.add(data.getBoolean("organizer"));
                return rt;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
}
