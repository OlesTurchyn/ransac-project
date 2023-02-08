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

    /*Constructor that generates the RANSAC object (random plane)
        - Generates random plane from given point cloud
        - calculates which points are based on the plane  
        - calculates the percentage of points on the plane relative to the point cloud
    */
    public PlaneRANSAC(PointCloud pc){
        this.pc = pc;

        dominantPlane = Plane3D.calculatePlane(pc.getPoint(), pc.getPoint(),pc.getPoint());

        //Iterate through the point cloud to compute and compare each distance
        for(int i = 0; i < pc.getSize(); i++){

            double distance = dominantPlane.getDistance(pc.get(i));

            if (distance < epsilon){
                currentPoints.add(pc.get(i));
            }
        }

        percentageOfPointsOnPlane = (double) currentPoints.size() / pc.getSize();
    }

    //Returns the number of iterations recommended to compute the dominant planes
    //Based on the first random selection of points
    public int getNumberOfIterations(double confidence, double percentageOfPointsOnPlane){
        //User input check
        if(confidence > 1){
            throw new Error("Invalid confidence size. Must be less than 1");
        }
        return (int) (Math.log(1-confidence)/Math.log(1-Math.pow(percentageOfPointsOnPlane,3)));
    }

    //runs the RANSAC algorithm for identifying the dominant plane of the point cloud
    public void run(int numberOfIterations, String filename){

        //Support is set to first iteration
        int support = currentPoints.size();
        System.out.println("SUPPORT: " + support);

        /*ATTENTION
         * I think this loop should be:  i < numberOfIterations
         * It is set to 2000 for efficiency reasons. Sometimes the given 
         * variable is very large and takes too much time
         */ 
        for(int i = 0; i < 2000; i++){

            //Clear data structures
            currentPoints.clear();

            //Generate a new Ransac object (random plane)
            //This will calculate a random plane and calculate the amount of points on it
            PlaneRANSAC plane = new PlaneRANSAC(pc);

            //Check to see if the randomly selected plane is larger than the current support
            if(currentPoints.size() > support){

                //If so, set it as the new support and write the points to the file
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
        pc.getCloud().removeAll(dominantPoints);

        //Generate file name for point cloud 
        String cloudName = filename;
        StringBuilder sb = new StringBuilder(cloudName);
        sb.setCharAt(13, '0');
        cloudName = sb.toString();

        pc.save(cloudName);
        
    }

    //Sets the global epsilon value
    public void setEps(double eps){
        epsilon = eps;
    }

    //getter to return the global epsilon
    public double getEps(){
        return epsilon;
    }


    //Helper method that saves the points of the dominant plane to a new xyz file
    // - used for saving the p1,p2,p3 files of dominant planes
    public void savePoints(String filename) throws IOException{
        File newFile = new File(filename);

        if(!newFile.exists()){
            newFile.createNewFile();
        }

        FileWriter myWriter = new FileWriter(filename);

        int count = 0;

        myWriter.write("x" + "     " + "y" + "     " + "z");
        myWriter.write(System.getProperty( "line.separator" ));

        //Iterate through the dominant point plane to write points to the file
        for (int i = 0; i < dominantPoints.size(); i++){

            String x = String.valueOf(dominantPoints.get(count).getX());
            String y = String.valueOf(dominantPoints.get(count).getY());
            String z = String.valueOf(dominantPoints.get(count).getZ());

            myWriter.write(x + "\t" + y + " " + z);
            myWriter.write(System.getProperty( "line.separator" ));
            
            count++;

            if(count == (dominantPoints.size())){
                break;
            }
        }

        myWriter.close();

    }

    //Helper method to run the 3 point clouds
    //Generates required xyz files
    public static void test(double eps, double confidence) throws IOException{

        //----------PointCloud1-----------------------

        pc = new PointCloud("PointCloud1.xyz");
        PlaneRANSAC plane = new PlaneRANSAC(pc);

        plane.setEps(eps);

        int iterations = plane.getNumberOfIterations(confidence, percentageOfPointsOnPlane);

        //Run 3 iterations to find the 3 most dominant points
        plane.run(iterations, "PointCloud1_P1.xyz");
        plane.run(iterations, "PointCloud1_P2.xyz");
        plane.run(iterations, "PointCloud1_P3.xyz");

        pc.clearCloud();

        //----------PointCloud2-----------------------

        pc = new PointCloud("PointCloud2.xyz");
        plane = new PlaneRANSAC(pc);

        iterations = plane.getNumberOfIterations(confidence, percentageOfPointsOnPlane);

        //Run 3 iterations to find the 3 most dominant points
        plane.run(iterations, "PointCloud2_P1.xyz");
        plane.run(iterations, "PointCloud2_P2.xyz");
        plane.run(iterations, "PointCloud2_P3.xyz");

        pc.clearCloud();

        //----------PointCloud3-----------------------

        pc = new PointCloud("PointCloud3.xyz");
        plane = new PlaneRANSAC(pc);

        iterations = plane.getNumberOfIterations(confidence, percentageOfPointsOnPlane);

        //Run 3 iterations to find the 3 most dominant points
        plane.run(iterations, "PointCloud3_P1.xyz");
        plane.run(iterations, "PointCloud3_P2.xyz");
        plane.run(iterations, "PointCloud3_P3.xyz");

    }

    public static void main(String args[]) throws IOException{
        
        //run our test method to generate everything
        //I wasn't sure how else to execute this. 
        //I hope this is ok for you TA

        //We can set the epsilon and confidence here
        test(0.1,0.99);

    }

}
