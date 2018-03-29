import java.util.*;

/**
* @author - Joseph Agnelli
* Equipment class to represent the equipment table in the Travel database
*/
public class Equipment
{
   private int equipId;
   private String equipName = null;
   private String equipDesc = null;
   private int equipCapacity = -1;
   MySQLDatabase mysqldb = null;

   
   /**
   * Default Constructor
   */
   public Equipment(){
      this(-1);
   }
   
   /**
    * Constructor to specify equipId
    * @param equipId  The equipment id for this piece of equipment
    */
   public Equipment(int equipId){
      this(equipId,null,null,-1);
   }
   
   /**
    * Constructor to specify everything
    * @param equipId  The equipment id for this piece of equipment
    * @param equipName The equipment name
    * @param equipDesc  The equipment description
    * @param equipCapacity  The equipment capacity
    */
   public Equipment(int equipId,String equipName, String equipDesc, int equipCapacity){
      this.equipId = equipId;
      this.equipName = equipName;
      this.equipDesc = equipDesc;
      this.equipCapacity = equipCapacity;
      mysqldb = new MySQLDatabase("root","student");
   }
   
   /**
    * Accessor for equipId
    * @return the eqiupID
    */
   public int getEquipId(){
      return equipId;
   }
    
    /**
    * Accessor for equipName
    * @return the equipName
    */
   public String getEquipName(){
      return equipName;
   }
    
    /**
    * Accessor for equipDesc
    * @return   the equipDesc
    */
   public String getEquipDesc(){
      return equipDesc;
   }
    
    /**
    * Accessor for equipCapacity
    * @return the equipCapacity
    */
   public int getEquipCapacity(){
      return equipCapacity;
   }
    
    /**
     * Mutator for equipId
     * @param equipId the new equipId
     */
   public void setEquipId(int equipId){
      this.equipId = equipId;
   }
     
      /**
     * Mutator for equipName
     * @param equipName the new equipName
     */
   public void setEquipName(String equipName){
      this.equipName = equipName;
   }
     
      /**
     * Mutator for equipDesc
     * @param equipDesc the new equipDesc
     */
   public void setEquipDesc(String equipDesc){
      this.equipDesc = equipDesc;
   }
     
      /**
     * Mutator for equipCapacity
     * @param equipCapacity the new equipCapacity
     */
   public void setEquipCapacity(int equipCapacity){
      this.equipCapacity = equipCapacity;
   }
   
   
   /**
    * Fetch - uses the equipId to retrive all other values from the database
    */
   public boolean fetch() throws DLException{ //stop trying to make 'fetch' happen
      String query = "SELECT EquipmentName,EquipmentDescription, EquipmentCapacity FROM equipment WHERE equipId = ?;";
      ArrayList<String> params = new ArrayList<>();
      params.add(String.valueOf(equipId));
      //System.out.println(query);
             
      try{
         ArrayList<String> result = mysqldb.getData(query,params).get(1);
            
         this.equipName = result.get(0);
         this.equipDesc = result.get(1);
         this.equipCapacity = Integer.parseInt(result.get(2));
            
      }
      catch(DLException dle){
         throw dle;
      }
      catch (IndexOutOfBoundsException ioobe){
         return false;
      }
      catch (Exception e){
         return false;
      }
      
      return true;
   
   }  // end fetch
    
   /**
    * post - Inserts this object into the database
    */
   public int post() throws DLException{
      String query = "INSERT INTO equipment (EquipId,EquipmentName,EquipmentDescription,EquipmentCapacity) VALUES (?,?,?,?)";
      ArrayList<String> params = new ArrayList<>();
      params.add(String.valueOf(equipId));
      params.add(equipName);
      params.add(equipDesc);
      params.add(String.valueOf(equipCapacity));
      
      //System.out.println(query);
      try{
         return mysqldb.setData(query,params);
      }
      catch(DLException dle){
         throw dle;
      }
   
   } // end post
   
   /**
    * put - updates the table's fields to match this object
    */
   public int put() throws DLException{
      //String query = "UPDATE equipment SET EquipmentName = '"+equipName+"', EquipmentDescription = '"+equipDesc+"', EquipmentCapacity = "+equipCapacity+" WHERE equipId = "+equipId+";";
      String query = "UPDATE equipment SET EquipmentName = ?, EquipmentDescription = ?, EquipmentCapacity = ? WHERE equipId = ?;";
      ArrayList<String> params = new ArrayList<>();
      params.add(equipName);
      params.add(equipDesc);
      params.add(String.valueOf(equipCapacity));
      params.add(String.valueOf(equipId));
   
      
      //System.out.println(query);
      try{
         return mysqldb.setData(query,params);
      }
      catch(DLException dle){
         throw dle;
      }
        
   
   } // end put
   
   /**
    * delete - deletes this row from the table
    */
   public int delete() throws DLException{
      String query = "DELETE FROM equipment WHERE equipId = ?;";
      ArrayList<String> params = new ArrayList<>();
      params.add(String.valueOf(equipId));
      
      //System.out.println(query);
     
      try{
         return mysqldb.setData(query,params);
      }
      catch(DLException dle){
         throw dle;
      }
         
   
   } // end put
   
   
   
   /** 
    * swap - a method that swaps the names of two different pieces of equipment
    * @param otherID an integer value, this represents the id of the other equipment who's name this one will swap with
    */
   public void swap(int otherID) throws DLException{
      try{
         if(mysqldb.startTrans()){
         //trasaction has started
            // get the other equpment
             Equipment equipOther = new Equipment(otherID);
             if(equipOther.fetch()){
               //the other equipment exists
               String tempName = equipOther.getEquipName();
               equipOther.setEquipName(this.getEquipName());
               this.setEquipName(tempName);
               this.put();
               equipOther.put();
               mysqldb.endTrans();
             }
             
         }
      }
      catch(DLException dle){
         dle.printStackTrace();
         mysqldb.rollbackTrans();
         throw dle;
      }
   }
   
   /**
   * toString - returns the object as a human readable string
   * @return A string representing the object
   */
//    public String toString(){
//       return String.format("equipId:%5d equipName: %-20s equipDesc: %-20s equipCapacity:%5d",equipId,equipName,equipDesc,equipCapacity);
//    }

}