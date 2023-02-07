//STUDENT NAME: Oleksander Turchyn 
//STUDENT NUMBER: 300174825

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.lang.Math;

public class PlaneRANSAC {

    //Global Variables
    static PointCloud pc = new PointCloud();
    static Double epsilon = 0.0;
    Plane3D dominantPlane;

    static ArrayList<Point3D> currentPoints = new ArrayList<>();
    static ArrayList<Point3D> dominantPoints = new ArrayList<>();

    static double percentageOfPointsOnPlane = 0.0;

    public PlaneRANSAC(PointCloud pc){
        this.pc = pc;

        dominantPlane = Plane3D.calculatePlane(pc.getPoint(), pc.getPoint(),pc.getPoint());

        for(int i = 0; i < pc.getSize(); i++){

            double distance = dominantPlane.getDistance(pc.get(i));

            if (distance < epsilon){
                currentPoints.add(pc.get(i));
            }
        }

        percentageOfPointsOnPlane = (double) currentPoints.size() / PointCloud.getSize();

    }


    public static int getNumberOfIterations(double confidence, double percentageOfPointsOnPlane){
        if(confidence > 1){
            throw new Error("Invalid confidence size. Must be less than 1");
        }
        return (int) (Math.log(1-confidence)/Math.log(1-Math.pow(percentageOfPointsOnPlane,3)));
    }

    public void run(int numberOfIterations, String filename){

        //Support is set to first iteration
        int support = currentPoints.size();
        System.out.println("SUPPORT: " + support);

        for(int i = 0; i < 2000; i++){

            //Clear data structures
            currentPoints.clear();

            //Generate a new Ransac object (random plane)
            //This will calculate a random plane and calculate the amount of points on it
            PlaneRANSAC plane = new PlaneRANSAC(pc);

            //Check to see if the randomly selected plane is larger than the current support
            //System.out.println("random size: " + currentPoints.size());
            if(currentPoints.size() > support){

                //If so, set it as the new support and write the points to the file
                System.out.println("HIT");
                support = currentPoints.size();
                dominantPoints = (ArrayList<Point3D>) currentPoints.clone();

                try {
                    savePoints(filename);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        //Now remove the dominant plane from the Point Cloud
        System.out.println("support size: " + support);
        System.out.println("size of dominant plane: " + dominantPoints.size());
        pc.getCloud().removeAll(dominantPoints);
        
    }

    
    public static void setEps(double eps){
        epsilon = eps;
    }

    public static double getEps(){
        return epsilon;
    }


    //Helper method that saves the points of a dominant plane to a new xyz file
    public void savePoints(String filename) throws IOException{
        File newFile = new File(filename);

        if(!newFile.exists()){
            newFile.createNewFile();
        }

        FileWriter myWriter = new FileWriter(filename);

        int count = 0;

        myWriter.write("x" + "     " + "y" + "     " + "z");
        myWriter.write(System.getProperty( "line.separator" ));

        for (int i = 0; i < dominantPoints.size(); i++){

            String x = String.valueOf(dominantPoints.get(count).x);
            String y = String.valueOf(dominantPoints.get(count).y);
            String z = String.valueOf(dominantPoints.get(count).z);

            myWriter.write(x + "\t" + y + " " + z);
            myWriter.write(System.getProperty( "line.separator" ));
            
            // System.out.println(x + "    " + y + " " + z);
            
            count++;

            if(count == (dominantPoints.size())){
                break;
            }
        }

        myWriter.close();

    }

    public static void main(String args[]) throws IOException{
        
        setEps(0.1);
        System.out.println("Epsilon value: " + getEps());

        pc = new PointCloud("PointCloud3.xyz");
        PlaneRANSAC plane = new PlaneRANSAC(pc);

        int iterations = getNumberOfIterations(0.99, percentageOfPointsOnPlane);


        plane.run(iterations, "PointCloud3_P1.xyz");

        System.out.println("Number of iterations for 99% confidence: " + iterations);
        
        // System.out.println("Percentage of points on the plane " + percentageOfPointsOnPlane);

    }

}
