//STUDENT NAME: Oleksander Turchyn 
//STUDENT NUMBER: 300174825

import java.io.IOException;
import java.util.ArrayList;
import java.lang.Math;

public class PlaneRANSAC {

    //Global Variables
    static PointCloud pc = new PointCloud();
    static Double epsilon = 0.0;
    Plane3D dominantPlane;

    static ArrayList<Point3D> dominantPoints = new ArrayList<>();

    static double percentageOfPointsOnPlane = 0.0;

    public PlaneRANSAC(PointCloud pc){
        this.pc = pc;

        dominantPlane = Plane3D.calculatePlane(pc.getPoint(), pc.getPoint(),pc.getPoint());

        for(int i = 0; i < pc.getSize(); i++){

            double distance = dominantPlane.getDistance(pc.get(i));

            if (distance < epsilon){
                dominantPoints.add(pc.get(i));
            }
        }

        percentageOfPointsOnPlane = (double) dominantPoints.size() / PointCloud.getSize();

        // System.out.println("plane size: " + dominantPoints.size() + "\ncloud size: " +  PointCloud.getSize());
        // System.out.println("Percentage: " + percentageOfPointsOnPlane);

    }


    public static int getNumberOfIterations(double confidence, double percentageOfPointsOnPlane){
        if(confidence > 1){
            throw new Error("Invalid confidence size. Must be less than 1");
        }
        return (int) (Math.log(1-confidence)/Math.log(1-Math.pow(percentageOfPointsOnPlane,3)));
    }

    public void run(int numberOfIterations, String filename){

        int support = dominantPoints.size();

        for(int i = 0; i < numberOfIterations; i++){

            //Clear data structures
            dominantPoints.clear();
            //percentageOfPointsOnPlane = 0.0;

            //Generate new Ransac object (random plane)
            PlaneRANSAC plane = new PlaneRANSAC(pc);


            System.out.println("Size: " + plane.dominantPoints.size());
            if(plane.dominantPoints.size() > support){
                System.out.println("HIT");
                support = dominantPoints.size();

                PointCloud.save(filename);
            }

        }
    }

    
    public static void setEps(double eps){
        epsilon = eps;
    }

    public static double getEps(){
        return epsilon;
    }

    public static void main(String args[]) throws IOException{
        
        setEps(0.5);
        System.out.println("Epsilon value: " + getEps());

        pc = new PointCloud("PointCloud3.xyz");
        PlaneRANSAC plane = new PlaneRANSAC(pc);

        int iterations = getNumberOfIterations(0.99, percentageOfPointsOnPlane);


        plane.run(iterations, "PointCloud_P1");

        System.out.println("Number of iterations for 99% confidence: " + iterations);
        
        // System.out.println("Percentage of points on the plane " + percentageOfPointsOnPlane);

    }

}
