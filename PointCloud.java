//STUDENT NAME: Oleksander Turchyn 
//STUDENT NUMBER: 300174825

import java.util.ArrayList;
import java.util.Iterator;
import java.io.*;

public class PointCloud {

   //Global pointCloud array list
    static ArrayList<Point3D> superCloud = new ArrayList<Point3D>();

    //Constructor
    PointCloud(String filename) throws IOException{
        //TO CHANGE PATH
        File file = new File("C:\\Users\\Oles\\Desktop\\dev\\ransac-project\\" + filename);

        //Exception handling to identify and read the input file
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String st;
            int count = 0;

            //Reads and ignores the first line 
            br.readLine();

            while ((st = br.readLine()) != null){

                String[] points = st.split("\\s+");

                superCloud.add(new Point3D(Double.parseDouble(points[0]), 
                                           Double.parseDouble(points[1]), 
                                           Double.parseDouble(points[2])));
                
                //System.out.println(superCloud.get(count).z);

                count++;
           }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            System.out.println("ERROR: File not found");
            e.printStackTrace();
        }

    }

    //Constructor makes an empty PointCloud
    PointCloud(){
        ArrayList<Point3D> pointCloud = new ArrayList<>();
    }

    //Adds point to the PointCloud
    public void addPoint(Point3D pt){
        superCloud.add(pt);
    }

    //Returns a random point from the PointCloud
    static Point3D getPoint(){

        //generate a random number between 0 and n
        int random = (int) (Math.random() * (superCloud.size()));

        //System.out.println("Random index: " + random);

        return superCloud.get(random);
    }

    //Getter method
    public static int getSize(){
        return superCloud.size();
    }

    //Returns a point from the superCloud given an index
    public static Point3D get(int index){
        return superCloud.get(index);
    }

    //Saves the PointCloud into a .xyz file
    public static void save(String filename){

        //Structure borrowed from W3 Schools. Listed in references:
        try {
            File newFile = new File(filename);
            if (newFile.createNewFile()) {
              System.out.println("File created: " + newFile.getName());
            } else {
                FileWriter clear = new FileWriter(filename);
                clear.close();

              System.out.println("Error: File already exists.");
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

                String x = String.valueOf(superCloud.get(count).x);
                String y = String.valueOf(superCloud.get(count).y);
                String z = String.valueOf(superCloud.get(count).z);

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

    public static void main(String args[]){

       try {
        PointCloud pointC = new PointCloud("PointCloud3.xyz");
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    Point3D randomPoint = getPoint();

    System.out.println("Random Point: " + randomPoint.x + " " + randomPoint.y + " " + randomPoint.z);


    save("test.xyz");

    }


    
}
