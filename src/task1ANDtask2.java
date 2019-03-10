import java.sql.*;
import java.util.*;

public class task1ANDtask2 {

    static final String DB_URL = "jdbc:postgresql://192.168.99.100:5432/wg_forge_db";
    static final String USER = "wg_forge";
    static final String PASS = "42a";

    public static void main(String[] argv) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
            return;
        }

        System.out.println("PostgreSQL JDBC Driver successfully connected");
        Connection connection = null;

        try {
            connection = DriverManager
                    .getConnection(DB_URL, USER, PASS);

        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return;
        }

        if (connection != null) {
            System.out.println("You successfully connected to database now");
        } else {
            System.out.println("Failed to make connection to database");
        }

        Statement stmt;
        List<OneCat> catsArray = new ArrayList<OneCat>();

        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM CATS;" );

            while ( rs.next() ) {
                catsArray.add(new OneCat(rs.getString("name"),
                        rs.getString("color"),rs.getInt("tail_length"),rs.getInt("whiskers_length")));
                //System.out.println(String.format("NAME=%s COLOR=%s TAIL_LENGTH=%s WHISKERS_LENGTH=%s",name,color,tail_length,whiskers_length));
            }
            System.out.println("count = "+catsArray.size());
            System.out.println("-- Operation SELECT done successfully");

            ////////////task1
            System.out.println ("task 1 starts");
            Map <String, Integer> colorMap = new HashMap<>();
            colorMap.put("black",0);
            colorMap.put("white",0);
            colorMap.put("black & white",0);
            colorMap.put("red",0);
            colorMap.put("red & white",0);
            colorMap.put("red & black & white",0);

            int count = catsArray.size();
            String temp="";
            for (int i=0;i<count;i++)
            {
                    temp = catsArray.get(i).getColor();
                    colorMap.put(temp, colorMap.get(temp) + 1);
            }
            colorMap.forEach((k, v) -> System.out.println(k + ": " + v));
            String sqlDelete = "DELETE FROM cat_colors_info;";
            stmt.executeUpdate(sqlDelete);
            for (Map.Entry<String, Integer> pair : colorMap.entrySet()) {
                stmt = connection.createStatement();
                String sqlInsert = "INSERT INTO cat_colors_info (color, count) "
                        + "VALUES ('"+pair.getKey()+"' ,"+pair.getValue()+");";
               stmt.executeUpdate(sqlInsert);
            }
            rs = stmt.executeQuery( "SELECT * FROM cat_colors_info;" );
            while ( rs.next() ) {

                String  color = rs.getString("color");
                int count1  = rs.getInt("count");
                System.out.print( "color = " + color);
                System.out.println( " count = " + count1 );
                System.out.println();
            }

            //////////////////task2
            System.out.println ("task 2 starts");
            double tail_length_mean=0;
            double tail_length_median=0;
            List<Integer> tail_length_mode=new ArrayList<Integer>();
            double whiskers_length_mean=0;
            double whiskers_length_median=0;
            List<Integer> whiskers_length_mode=new ArrayList<Integer>();
            Map <Integer, Integer> tailMap = new HashMap<>();
            int tempTail=0;
            Map <Integer, Integer> whiskersMap = new HashMap<>();
            int tempWhiskers=0;
            double[] arrayTails = new double[count];
            double[] arrayWhiskers = new double[count];
            double t1, t2;
            for (int i=0;i<count;i++)
            {
                t1 = catsArray.get(i).getTail_length();
                t2 = catsArray.get(i).getWhiskers_length();
                tail_length_mean+= t1;
                whiskers_length_mean+= t2;
                arrayTails[i] = t1;
                arrayWhiskers[i] = t2;
                tempTail = catsArray.get(i).getTail_length();
                if (tailMap.containsKey(tempTail))
                {
                    tailMap.put(tempTail, tailMap.get(tempTail) + 1);
                }
                else
                {
                    tailMap.put(tempTail, 1);
                }
                tempWhiskers = catsArray.get(i).getWhiskers_length();
                if (whiskersMap.containsKey(tempWhiskers))
                {
                    whiskersMap.put(tempWhiskers, whiskersMap.get(tempWhiskers) + 1);
                }
                else
                {
                    whiskersMap.put(tempWhiskers, 1);
                }

            }
            tail_length_mean/=count;
            whiskers_length_mean/=count;
            Arrays.sort (arrayTails);
            tail_length_median = arrayTails[count/2];
            Arrays.sort (arrayWhiskers);
            whiskers_length_median = arrayWhiskers [count/2];
            int maxTail=0;
            int maxWhiskers=0;
            for (Map.Entry<Integer, Integer> entry : tailMap.entrySet()) {
                if (maxTail< entry.getValue())
                {
                    maxTail =  entry.getValue();
                }
            }
            for (Map.Entry<Integer, Integer> entry : whiskersMap.entrySet()) {
                if (maxWhiskers< entry.getValue())
                {
                    maxWhiskers =  entry.getValue();
                }
            }
            int t=0;
            for (Map.Entry<Integer, Integer> entry : tailMap.entrySet()) {
                if (maxTail== entry.getValue())
                {
                    tail_length_mode.add(entry.getKey());
                }
            }
            for (Map.Entry<Integer, Integer> entry : whiskersMap.entrySet()) {
                if (maxWhiskers== entry.getValue())
                {
                    whiskers_length_mode.add(entry.getKey());
                }
            }

            System.out.println ("tail_length_mean = "+tail_length_mean);
            System.out.println ("tail_length_median = "+tail_length_median);
            System.out.println ("whiskers_length_mean = "+whiskers_length_mean);
            System.out.println ("whiskers_length_median = "+ whiskers_length_median);
            System.out.println (tail_length_mode);
            System.out.println (whiskers_length_mode);
            String sqlInsert = "INSERT INTO cats_stat (tail_length_mean) "+ "VALUES ("+tail_length_mean+"); " +
                    "INSERT INTO cats_stat (tail_length_median) "+ "VALUES ("+tail_length_median+"); "+
                    "INSERT INTO cats_stat (tail_length_mode) "+ "VALUES (array[";
            for (int i=0;i<tail_length_mode.size()-1;i++){
                sqlInsert+=tail_length_mode.get(i)+", ";
            }
            sqlInsert+=tail_length_mode.get(tail_length_mode.size()-1);
            sqlInsert+= "]); "+
                    "INSERT INTO cats_stat (whiskers_length_mean) "+ "VALUES ("+whiskers_length_mean+"); "+
                    "INSERT INTO cats_stat (whiskers_length_median) "+ "VALUES ("+whiskers_length_median+"); "
                    +"INSERT INTO cats_stat (whiskers_length_mode) "+ "VALUES (array[";
            for (int i=0;i<whiskers_length_mode.size()-1;i++){
                sqlInsert+=whiskers_length_mode.get(i)+", ";
            }
            sqlInsert+=whiskers_length_mode.get(whiskers_length_mode.size()-1);
            sqlInsert+="]); "
            ;
            //System.out.println(sqlInsert);
            stmt.executeUpdate(sqlInsert);

            ////////////task3







            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}