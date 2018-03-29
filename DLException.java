import java.io.*;
import java.util.*;

/**
 * @author Joseph Agnelli
 * A class to handle custom database exceptions
 */
public class DLException extends Exception{
   String[] s = {};
   /**
    * DLException constructor take an exception paramater
    * @param E An Exception object
    */
   public DLException(Exception e){
      super();
      writeLog();
   }
   /**
    * DLException Constructor
    * @param E An exception object
    * @param s Any number of string values
    */
   public DLException(Exception e, String...s){
      super(s[0]);
      this.s = s;
      writeLog();
   }
   
   /**
    * writeLog writes out all the available information
    */
   public void writeLog(){
      BufferedWriter buff = null;
      try{
         buff = new BufferedWriter(new FileWriter(new File("log.txt"),true));
         buff.write(new Date().toString());
         for(String eachString:s){
            buff.newLine();
            buff.write(eachString);
         }
         for(StackTraceElement ste:super.getStackTrace()){
            buff.write(ste.toString()+"\n");
         }
         buff.newLine();
         buff.newLine();
         buff.flush();
      }
      catch(IOException ioe){
         ioe.printStackTrace();
         //im not sure how else to handle this error
      }
      finally{
         try{
            buff.close();
         }
         catch(IOException ioe){
            ioe.printStackTrace();
            //this would be bad
         }
      }
   }
}