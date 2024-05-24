package ap.jfx;

import java.io.*;

public class Password {
    public static String getPassword(){
        try {
            FileInputStream password_text=new FileInputStream("./password.txt");
            InputStreamReader readtxt=new InputStreamReader(password_text);
            BufferedReader readBuffer=new BufferedReader(readtxt);
            return readBuffer.readLine();
        } catch (IOException e ) {
            throw new RuntimeException(e);
        }
    }
}
