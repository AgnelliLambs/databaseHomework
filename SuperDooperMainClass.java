/**
 * SuperDooperMainClass a tester class for the updates to MySQLDatabase.java
 * @author Joseph Agnelli
 * @version 3/19/2018
 */
public class SuperDooperMainClass{

   public static void main(String[] args){
      //instantiate an equipment data object
      Equipment eq1 = new Equipment(568);
      
      try{
         //call the data object's fetch method
         eq1.fetch();
      
         //display the values to the user
         System.out.println("eq1: Id: " + eq1.getEquipId() + " Capacity: " +eq1.getEquipCapacity() +
            " Name: " + eq1.getEquipName() + " Desc: " + eq1.getEquipDesc());
           
           
         eq1.swap(894);
         eq1.fetch();
         
         System.out.println("eq1: Id: " + eq1.getEquipId() + " Capacity: " +eq1.getEquipCapacity() +
            " Name: " + eq1.getEquipName() + " Desc: " + eq1.getEquipDesc());
         
         
         Equipment eq2 = new Equipment(894);
         eq2.fetch();
         System.out.println("eq2: Id: " + eq2.getEquipId() + " Capacity: " +eq2.getEquipCapacity() +
            " Name: " + eq2.getEquipName() + " Desc: " + eq2.getEquipDesc());
      }
      catch(DLException dle){
         System.out.println(dle.toString());
      }
   }
}

                                                                                                    
//                                                                                                     
//               When you put another spongebob meme in your homework                                                                                      
//               -///:::-.`                                                                            
//               -oo++++o++++:-..`                                                                     
//                -o+oo++++++++++o+/:-`                                                                
//                 -oshyso++++++++++++o+//-.`                                                          
//                  `:ssso++++osyys+++++++++o+::.`                                                     
//                    ./+++++++osyyo++++++oo+++ooo+:-`                                                 
//                      `-/++++++++++++++sms+oo++++++++:-`                                             
//                         +ysso++++++++odmydms++++++++++++/-.`                                        
//                         -yoooo+++++++hmmdmho++++++++++++++++/-.                                     
//                          `/sssso++++omyoymo+osysoooosso+++++++++:-`                                 
//                             .::----:oooohyoys:.``````-sdyso+++++++++--`                   ``````````
//                                 -///+++++oh/``````````-hsdo+ooo+++++++++:--:::::::+/+oooosyyyyyyyyyy
//                                 `:-:ooooosh.``````.``/hshmshdyoo+++++ssyyssssssoooo+++++++++++++++++
//                                 `/+ys+/:/oh:```.smNhyy+/ydosdds+++++++++++++++++++++++++++++++++++++
//                                   ..``````.s/::+mmds+//sdsoydds+++++++++++++++++++++++++++++++++++++
//                                  `.````````+hsooss//oshs++sdddyo++++++++++++++++++++++osyys+++++++++
//                                   .``-/:..+hoohdyyyyso+++oddddo++++++++++++++++++++++++osso+++++++++
//                                  `..-mMNdyo//+hs+++++++oydddds++++++++++++++++++++++++++++++++++++++
//                                  ./ssys+////sho++++++oydddddy+++++++++++++++++++++++++++++++++++++++
//                                   ` `:yssyyys+++++oyhdddddds++++++++++++++++++++++++++++++++++++++++
//                                      +hsso++oosyhdddddddhs++++++++++++++++++++++++++++++++++++++++++
//                                   .oysshdyhddmddddhhyso++oo+++++++++++++++++++++++++++++++++++++++++
//                                  .so++oyo+ooooooo+++osssss+++++++++++++++++++++++++++++++++++++++++o
//                                `:+++++oys++++++++++++++++++++++++++++++++++++++++++++++++++++++osyss
//                              `:++++++++sy+++++++++++++++++++++++++++++++++++++++++++++++++++ossso+++
//                             -++++++++++sy+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//                           ./+++++++++++sy+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//                          /o++++++++++++oyo++++++++++++++++++++++++++++++++++++++++++++++++++++++++++