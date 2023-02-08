//STUDENT NAME: Oleksander Turchyn 
//STUDENT NUMBER: 300174825

public class Point3D {
    /*getX, getY and getZ methods returning double
        ▪ public double getX()
        ▪ public double getY()
        ▪ public double getZ()
     */

     //Global variables 
     private double x;
     private double y;
     private double z;

     //Constructor
     public Point3D(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
     }

     //Getter methods:

     public double getX(){
        return this.x;
     }

     public double getY(){
        return this.y;
     }

     public double getZ(){
        return this.z;
     }

     
    
}
