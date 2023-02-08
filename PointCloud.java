//STUDENT NAME: Oleksander Turchyn 
//STUDENT NUMBER: 300174825

import java.util.ArrayList;
import java.util.Iterator;
import java.io.*;

public class PointCloud {

   //Global pointCloud array list
    static ArrayList<Point3D> superCloud = new ArrayList<Point3D>();

    //Constructor to produce a pointcloud given an xyz file
    PointCloud(String filename) throws IOException{
        File file = new File(filename);

        //Exception handling to identify and read the input file
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String st;
        
            //Reads and ignores the first line 
            br.readLine();

            while ((st = br.readLine()) != null){

                String[] points = st.split("\\s+");

                superCloud.add(new Point3D(Double.parseDouble(points[0]), 
                                           Double.parseDouble(points[1]), 
                                           Double.parseDouble(points[2])));
            
           }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: File not found");
            e.printStackTrace();
        }
    }

    //Constructor to make an empty PointCloud
    PointCloud(){
        ArrayList<Point3D> pointCloud = new ArrayList<>();
    }

    //Adds point to the PointCloud
    public void addPoint(Point3D pt){
        superCloud.add(pt);
    }

    //Returns a random point from the PointCloud
    Point3D getPoint(){

        //generate a random number between 0 and n
        int random = (int) (Math.random() * (superCloud.size()));

        return superCloud.get(random);
    }

    //Getter method returns supercloud size
    public int getSize(){
        return superCloud.size();
    }

    //Returns a point from the superCloud given an index
    public Point3D get(int index){
        return superCloud.get(index);
    }

    //Returns an instance of the supercloud arraylist
    public ArrayList<Point3D> getCloud(){
        return superCloud;
    }

    //Clears the supercloud arraylist
    public void clearCloud(){
        superCloud.clear();
    }

    //Saves the PointCloud into a .xyz file
    public void save(String filename){

        //Structure borrowed from W3 Schools. Listed in references:
        try {
            File newFile = new File(filename);
            if (newFile.createNewFile()) {
              System.out.println("File created: " + newFile.getName());
            } else {
                FileWriter clear = new FileWriter(filename);
                clear.close();
            }
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            FileWriter myWriter = new FileWriter(filename);
            
            int count = 0;

            myWriter.write("x" + "     " + "y" + "     " + "z");
            myWriter.write(System.getProperty( "line.separator" ));

            while (superCloud.iterator().hasNext()){

                String x = String.valueOf(superCloud.get(count).getX());
                String y = String.valueOf(superCloud.get(count).getY());
                String z = String.valueOf(superCloud.get(count).getZ());

                myWriter.write(x + "\t" + y + " " + z);
                myWriter.write(System.getProperty( "line.separator" ));
                
                // System.out.println(x + "    " + y + " " + z);
                
                count++;

                if(count == (superCloud.size())){
                    break;
                }
                
            }

            myWriter.close();
            System.out.println("Successfully wrote to the file.");

          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
        }

    Iterator<Point3D> iterator(){
        Iterator<Point3D> it = superCloud.iterator();
        return it;
    }
    
}
