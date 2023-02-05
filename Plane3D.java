//STUDENT NAME: Oleksander Turchyn 
//STUDENT NUMBER: 300174825

import java.lang.Math;

public class Plane3D {

    //Global Variables
    public double a;
    public double b;
    public double c;
    public double d;

    public Point3D p1;
    public Point3D p2;
    public Point3D p3;

    //Base instance of Plane 3D (empty by default)
    public static Plane3D SuperPlane = new Plane3D(0.0, 0.0, 0.0, 0.0);

    //Constructor
    public Plane3D(Point3D p1, Point3D p2, Point3D p3){
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    //Constructor
    public Plane3D(double a, double b, double c, double d){
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    //Helper method to calculate the plane given three points
    public static Plane3D calculatePlane(Point3D p1, Point3D p2, Point3D p3){

        //To find the equation of a plane given 3 points, we need the normal vector and a point

        //COMPUTING THE NORMAL VECTOR:
        double vectorA[] = new double[3];
        double vectorB[] = new double[3];

        vectorA[0] = p2.getX() - p1.getX(); 
        vectorA[1] = p2.getY() - p1.getY();
        vectorA[2] = p2.getZ() - p1.getZ(); 

        vectorB[0] = p3.getX() - p1.getX(); 
        vectorB[1] = p3.getY() - p1.getY();
        vectorB[2] = p3.getZ() - p1.getZ(); 
 
        double normalVector[] = new double[3];

        normalVector[0] = (vectorA[1] * vectorB[2]) - (vectorA[2] * vectorB[1]);
        normalVector[1] = (vectorA[2] * vectorB[0]) - (vectorA[0] * vectorB[2]);
        normalVector[2] = (vectorA[0] * vectorB[1]) - (vectorA[1] * vectorB[0]);

        double dValue = (normalVector[0] * -p1.getX()) + 
                   (normalVector[1] * -p1.getY()) + 
                   (normalVector[2] * -p1.getZ());

        dValue = dValue * - 1;

        Plane3D plane = new Plane3D(normalVector[0], normalVector[1], normalVector[2], dValue);

        SuperPlane = plane;

        return plane;
    }

    public static double getDistance(Point3D pt){
        //scale of projection

        //Distance = |normalVector * point|
        //           -----------------------
        //                |normalVector|
        
        return Math.abs((SuperPlane.a * pt.getX()) + (SuperPlane.b * pt.getY()) + (SuperPlane.c * pt.getZ()) + (SuperPlane.d*-1))
        / Math.sqrt(SuperPlane.a*SuperPlane.a + SuperPlane.b*SuperPlane.b +SuperPlane.c*SuperPlane.c);
    }

    public static void main(String args[]){
       
        Point3D p1 = new Point3D(1, -1, -2);
        Point3D p2 = new Point3D(2, -2, -3);
        Point3D p3 = new Point3D(3, 1, 2);

        calculatePlane(p1,p2,p3);

        System.out.println("SUPER PLANE:");
        System.out.println(SuperPlane.a +  "x + " + SuperPlane.b + "y + " + SuperPlane.c + "z " + "= " +SuperPlane.d);

        Point3D point = new Point3D(1, -1, 2);

        System.out.println("Distance: " + getDistance(point));

    }
}
