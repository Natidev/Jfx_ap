package ap.jfx;

import java.io.*;
import java.time.LocalTime;
import java.time.LocalDateTime;
public class Loger {

    public static void recordError(String message) {
        try {
            FileOutputStream  log = new FileOutputStream("log.txt",true);
            BufferedWriter writer=new BufferedWriter( new OutputStreamWriter(log));
            LocalDateTime ts=LocalDateTime.now();
            String error="["+ts.toString()+"] [error]: "+message+"\n";
            writer.write(error);
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void clearLog(){
        try {
            FileOutputStream clr=new FileOutputStream("log.txt");
            clr.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void Success(String action){
        LocalDateTime ts=LocalDateTime.now();
        String done="["+ts.toString()+"] [Success]: "+action+"\n";
        try{

        BufferedWriter wr=new BufferedWriter(new OutputStreamWriter(new FileOutputStream("log.txt",true)));
        wr.write(done);
        wr.flush();
        wr.close();
        }catch(IOException e){
            recordError(e.getMessage());
        }
    }
}
