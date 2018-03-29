import java.sql.*;
import java.util.*;

public class MySQLDatabase{
   
   private String username = null;
   private String password = null;
   private Connection conn = null;
   private String uri = "jdbc:mysql://localhost/travel?autoReconnect=true&useSSL=false";
   private boolean inTransaction = false;
   public static final int MAX_WIDTH = 30;
   
   public MySQLDatabase(String user, String pass){
      username = user;
      password = pass;
   }
   
    /**
      Open - opens the database connection
      @return - Boolean value describing success of the open
   */
   public boolean connect() throws DLException{
   
      String driver = "com.mysql.jdbc.Driver";
      
      if(inTransaction){
         return true;
      }
      
      try{
         Class.forName( driver );
      }
      catch(ClassNotFoundException cnfe){
         throw new DLException(cnfe,"Failed to connect",username,"Driver Not Loaded");
          
      }
      catch(Exception e){
         throw new DLException(e,"Failed to connect",username,"Driver Not Loaded");
      }
      
      
      
      try {
         conn = DriverManager.getConnection( uri, username, password );
       
      }
      catch(SQLException sqle) {
         throw new DLException(sqle,"Bad username or password.",username,"Couldn't Connect");
      }
      catch(Exception e){
         throw new DLException(e,"Failed to connect",username,"Couldn't Connect");
      }
      
      return true;
   } // connect end
   
   /**
      Close - closes the database connection
      @return - Boolean value describing success of close
   */
   public boolean close() throws DLException{
      if(inTransaction){
         return true;
      }
      
      try{
         if(conn != null){
            conn.close();
         }
         
      }
      catch(SQLException sqle) {
         throw new DLException(sqle,"Failed to close",username,"Couldn't Close");
      }
      catch(Exception e){
         throw new DLException(e,"Failed to close",username,"Couldn't Close"); 
      }      
      return true;
      
   } // end close  
   
   /**
   * getData - pulls data from the database
   * @return - 2-dArrayList containing the resultset of the query 
   * @param -  query - The query passed
   * @overload
   */
   
   public ArrayList<ArrayList<String>> getData(String query) throws DLException{
      ArrayList<ArrayList<String>> result = new ArrayList<>();
      int res = -1;
      
      if(this.connect()){
         try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            res = rsmd.getColumnCount();
            while(rs.next()){
               ArrayList<String> row = new ArrayList<>();
               for(int i =1;i<=res;i++){
                  row.add(rs.getString(i));
               }
               result.add(row);
            }        
         }
         catch(SQLException sqle){
            throw new DLException(sqle,"Bad query, failed to access data.",query,"res: "+res,username);       
         }// enc catch
         catch(Exception e){
            throw new DLException(e,"Bad query, failed to access data.",query,"res: "+res,username); 
         }
         finally{
            this.close();
         }
      }
      return result;
   } // end getData
   
    /**
   * getData - pulls data from the database
   * @return - 2-dArrayList containing the resultset of the query 
   * @param -  query - The query passed
   * @param - header - a boolean determining whether the header should be included in the arraylist
   * @overload
   */
   
   public ArrayList<ArrayList<String>> getData(String query, boolean header) throws DLException{
      ArrayList<ArrayList<String>> result = new ArrayList<>();
      ArrayList<String> headerArray = new ArrayList<>();
      int res = -1;
      
      if(header){
         if(this.connect()){
            try{
               Statement stmt = conn.createStatement();
               ResultSet rs = stmt.executeQuery(query);
               ResultSetMetaData rsmd = rs.getMetaData();
               res = rsmd.getColumnCount();
               
               for(int i =1;i<=res;i++){
                  headerArray.add(rsmd.getColumnName(i));
               }
               
               result = getData(query);
               result.add(0,headerArray);
                       
            }
            catch(SQLException sqle){
               throw new DLException(sqle,"Bad query, failed to access data.",query,"res: "+res,username);       
            }// enc catch
            catch(Exception e){
               throw new DLException(e,"Bad query, failed to access data.",query,"res: "+res,username); 
            }
            finally{
               this.close();
            }
         }
      }
      else{
         result = getData(query);
      }
      return result;
   
   } // end getData
   
     
  /**
   * getData executes a query using a prepared statement
   * @return 2-dArrayList containing the resultset of the query 
   * @param   query  The query passed
   * @param  params an ArrayList of string values to bind to the string
   * @Overload
   */
   
   public ArrayList<ArrayList<String>> getData(String query, ArrayList<String> params) throws DLException{
   
      ArrayList<ArrayList<String>> out = new ArrayList<>();
      ArrayList<String> header = new ArrayList<>();
      int res = 0;
      
      try{
         if(this.connect()){
            PreparedStatement stmt = null;
            try{
               stmt = this.prepare(query,params);
            }
            catch(DLException dle){
               return null;
            }
         
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            res = rsmd.getColumnCount();
            for(int i =1; i<=res; i++){
               header.add(rsmd.getColumnName(i));
            }
         
            out.add(header);
            while(rs.next()){
               ArrayList<String> row = new ArrayList<>();
               for(int i =1;i<=res;i++){
                  row.add(rs.getString(i));
               }
               out.add(row);
            }
         }
      }
      
      catch(SQLException sqle){
         System.out.println("sqle");
         throw new DLException(sqle,"Bad query, failed to access data.",query,"res: "+res,username);  
      }// enc catch
      catch(Exception e){
         System.out.println("e");
         throw new DLException(e,"Bad query, failed to access data.",query,"res: "+res,username);
      }
      finally{
         this.close();
      }
      
      return out;
   }
   
   /**
    * Prepare returns a prepared statement from the input
    * @param query a mySql string to prepare
    * @param params an ArrayList of string values to bind to the string
    */
    
   private PreparedStatement prepare(String query, ArrayList<String> params) throws DLException{
      PreparedStatement stmt = null;
      try{
         if(this.connect()){
            stmt = conn.prepareStatement(query);
         
            for(int i = 1, len = params.size();i<=len;i++){
               
               stmt.setString(i,(params.get(i-1)));
               
            }
            
         }
      }
      catch(SQLException sqle){
         throw new DLException(sqle,"Bad query, failed to prepare statement.",query,params.toString());   
      }// end catch
      catch(Exception e){
         throw new DLException(e,"Bad query, failed to insert data.",query,params.toString()); 
      }
      return stmt;
   } // end prepare statement
   
    /**
   * descTable - Describes the table, returns a formatted output
   * @return - The formatted output 
   * @param -  query - The query passed
   * Overloaded
   */
   public String descTable(String query) throws DLException{
      ArrayList<ArrayList<String>> result = new ArrayList<>();
      ArrayList<String> header = new ArrayList<>();
      int[] headerSizes;
      int res = -1;
      StringBuilder outStringBuilder = new StringBuilder();
      
      if(this.connect()){
         try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            res = rsmd.getColumnCount();
            
            // print columns retrieved
            outStringBuilder.append(res + " columns retrieved.\n\n");
            
            //print 2 column report
            for(int i =1;i<=res;i++){
               header.add(rsmd.getColumnName(i));
               outStringBuilder.append(String.format("%20s%10s%n",header.get(i-1), rsmd.getColumnTypeName(i)));
            }
            
            //generate select statement     
            while(rs.next()){
               
               ArrayList<String> row = new ArrayList<>();
               
               for(int i =1;i<=res;i++){
                  row.add(rs.getString(i));
               }
               result.add(row);
            }    
            
            headerSizes = new int[header.size()];
          
            // find the header sizes
            for(int j = 0;j<res;j++){
               for(int i = 0,len = result.size(); i<len;i++){ 
                  if(result.get(i).get(j) != null){
                     headerSizes[j] = Math.max(headerSizes[j],result.get(i).get(j).length());
                  }
               }
            }
            
            // set the size to either the current size, the length of the header, or the max width
            for(int i = 0,len = headerSizes.length;i<len;i++){
               headerSizes[i] = Math.min(MAX_WIDTH,Math.max(headerSizes[i],header.get(i).length()));
               
            }
            
            //make seperator string and format string
            StringBuilder formatStringBuilder = new StringBuilder("|");
            StringBuilder outLineStringBuilder = new StringBuilder("\n+");
            
            for(int i = 1;i<res+1;i++){
               outLineStringBuilder.append(new String(new char[headerSizes[i-1]]).replace('\u0000','-') + '+');
               
               int colType = rsmd.getColumnType(i);
             //  System.out.println(rsmy.getColumnName(i)+" : "(colType >= Types.TINYINT && colType <= Types.DOUBLE) +" : " +colType );
               formatStringBuilder.append(((colType >= Types.TINYINT && colType <= Types.DOUBLE && colType!=Types.CHAR) ? "%":"%-")+headerSizes[i-1]+"s|");
               
            }
           
            outLineStringBuilder.append("\n");
            String formatString = formatStringBuilder.toString();
           
           //add header
            outStringBuilder.append(outLineStringBuilder);
            outStringBuilder.append(String.format(formatString,header.toArray()));
            outStringBuilder.append(outLineStringBuilder);
            
            
           //add everything else
            for(int i = 0,len = result.size();i<len;i++){
               outStringBuilder.append(String.format(formatString,result.get(i).toArray()));
               outStringBuilder.append(outLineStringBuilder);
            }
           
            return outStringBuilder.toString();
         
         }
         catch(SQLException sqle){
            sqle.printStackTrace();
            throw new DLException(sqle,"Bad query, failed to access data.",query,"res: "+res,username);       
         }// enc catch
         catch(Exception e){
            e.printStackTrace();
            throw new DLException(e,"Bad query, failed to access data.",query,"res: "+res,username); 
         }
         finally{
            this.close();
         }
      } // end if
      
      return "Failed to access data in table";
   } // end descTable

   /**
   * setData - modifies the data in the database
   * @return - a boolean determining the success of the method
   * @param -  query - the query to be performed
   * @Overload
   */
   public int setData(String query) throws DLException{
      int outNum = -1;
      if(this.connect()){
         try{
            Statement stmt = conn.createStatement();
            outNum = (stmt.executeUpdate(query));
         }
         catch(SQLException sqle){
            throw new DLException(sqle,"Bad query, failed to insert data.",query,username);   
         }// end catch
         catch(Exception e){
            throw new DLException(e,"Bad query, failed to insert data.",query,username); 
         }
         finally{
            this.close();
            return outNum; 
         }
      } // endif
      return outNum;
   } // end setData
   
   /**
   * setData - modifies the data in the database
   * @param query a string representation of a prepared statement to be bound and executed
   * @param params an ArrayList of parameters to be bound to the statement
   * @return a int representing the number of rows changed
   * @Overload
   */
   public int setData(String query, ArrayList<String> params) throws DLException{
      return executeStmt(query,params);
   } // end setData

   
   /**
    * executeStmt executes the passed statement
    * @param query a string representation of a prepared statement to be bound and executed
    * @param params an ArrayList of parameters to be bound to the statement
    * @return an int value representing the number of rows affected
    */
   public int executeStmt(String query, ArrayList<String> params) throws DLException{
      int out = -1;
      
      try{
         PreparedStatement stmt = this.prepare(query,params);
         if(this.connect()){
            out = stmt.executeUpdate();
            this.close();
         }
      }
      catch(SQLException sqle){
         throw new DLException(sqle,"Bad query, failed to insert data.",query,params.toString(),username);   
      }// end catch
      catch(Exception e){
         throw new DLException(e,"Bad query, failed to insert data.",query,params.toString(),username); 
      }
   
      return out;
   } // end executeStmt
   
   
   /**
    * startTrans - a method that starts a transaction
    * @return a boolean incidating the success of the start of the transaction
    */
   public boolean startTrans() throws DLException{
      // start a ttransaction, maybe return true if success?
      try{
         if(this.connect()){
            conn.setAutoCommit(false);
            inTransaction = true;
            return true;
         }
      }  
      catch(SQLException sqle){
         throw new DLException(sqle,"failed to start transaction.",username);   
      }// end catch
      catch(Exception e){
         throw new DLException(e,"failed to start transaction.",username); 
      }
      return false;
   }
    
    
    /**
     * endTrans - a method to end a transaction
     * @return a boolean incidating the success of the end of the transaction
     */
   public boolean endTrans() throws DLException{
      // end a transaction
      try{
         if(this.connect()){
            conn.commit();
            conn.setAutoCommit(true);
            inTransaction = false;
            this.close();
            return true;
         }
      }
      catch(SQLException sqle){
         sqle.printStackTrace();
         throw new DLException(sqle,"failed to end transaction.",username);   
      }// end catch
      catch(Exception e){
         throw new DLException(e,"failed to end transaction.",username); 
      }
      return false;
   }
     
     /**
      * rollbackTrans - a method to rollback the transaction (rather than commit it)
      * @return a boolean incidating the success of the rollback of the transaction
      */
   public boolean rollbackTrans() throws DLException{
         // rollback a transaction
      try{
         if(this.connect()){
            conn.rollback();
            conn.setAutoCommit(true);
            inTransaction = false;
            this.close();
            return true;
         }
      }
      catch(SQLException sqle){
         throw new DLException(sqle,"failed to rollback transaction.",username);   
      }// end catch
      catch(Exception e){
         throw new DLException(e,"failed to rollback transaction.",username); 
      }
      return false;
   }
      
} // class end