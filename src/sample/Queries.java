package sample;
import com.mysql.jdbc.Driver;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Queries {

    // JDBC URL, username and password of MySQL server
    private static final String url = "jdbc:mysql://localhost:3306/test";
    private static final String user = "root";
    private static final String password = "root";

    private static Connection con;
    private static Statement stmt;

    Queries(){
        try {
            con = DriverManager.getConnection(url, user, password);
            // getting Statement object to execute query
            stmt = con.createStatement();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }


    ArrayList<String> query1(String username, Timestamp date, String color, String plate){
        String query_rent = String.format("SELECT registration_plate FROM rent WHERE username_customer=\'%s\' AND" +
                "time_rent LIKE \'%"+ date.toString() + "%\'", username);

        String query_car = String.format("SELECT registration_plate FROM car WHERE registration_plate LIKE \'"
                + color + "%\' AND color=\'%s\' AND", plate);
        ArrayList<String> cars = new ArrayList<>();

        try {
            ResultSet rs = stmt.executeQuery(query_rent + " INTERSECT " + query_car);
            while(rs.next()){
                cars.add(rs.getString("registration_plate"));
            }
            return cars;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    int[] query2(String uid, Timestamp date){

        String time[] = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14",
                "15", "16", "17", "18", "19", "20", "21", "22", "23"};
        int statistics[] = new int[24];
        try {

            for(int i = 0; i < 24; i++) {
                String query = String.format("SELECT COUNT(*) AS count FROM charging WHERE uid =\'%s\' AND " +
                        "time_start LIKE \'%s "+ time[i] +"%\' OR time_finish LIKE \'%s "+ time[i] +"%\'", uid, date.toString());
                statistics[i] = stmt.executeQuery(query).getInt("count");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return statistics;
    }


    float[][] query3(){

        LocalDate firstDay = LocalDate.now().minusDays(8);

        String query_cars = "SELECT COUNT(*) AS count FROM car";

        int busyNumber[][] = new int[7][3];
        float busy[][] = new float[7][3];
        String time[][] = {{"07:00:00", "10:00:00"}, {"12:00:00", "14:00:00"}, {"17:00:00", "19:00:00"}};
        try {
            int totalNumber = stmt.executeQuery(query_cars).getInt("count");

            for(int i = 0; i < 7; i++) {
                for (int j = 0; j < 3; j++) {
                    String query = String.format("SELECT DISTINCT registration_plate FROM rent WHERE " +
                                    "DATE(time_start)=DATE(\'%s\') AND ((TIME(time_start) >= TIME(\'%s\') AND " +
                                    "TIME(time_start) <= TIME(\'%s\')) OR (TIME(time_finish) >= TIME(\'%s\') AND " +
                                    "TIME(time_finish) <= TIME(\'%s\')))",
                            Date.valueOf(firstDay.plusDays(i)).toString(), time[j][0], time[j][1], time[j][0], time[j][1]);
                    busyNumber[i][j] = stmt.executeQuery("SELECT COUNT(*) as count FROM "+ query).getInt("count");
                    busy[i][j] = (float)100*busyNumber[i][j]/totalNumber;
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return busy;
    }

    //возвращает количество поездок, за которые пользователь заплатил больше одного раза. вроде..
    int query4(String username){
        LocalDate firstDay = LocalDate.now().minusDays(31);

        String query = String.format("SELECT COUNT(id_rent) as number FROM payment INNER JOIN rent " +
                "(ON payment.id_rent = rent.id) WHERE username = %s AND DATE(datetime) > \'%s\' GROUP BY id_rent HAVING count(*)>1)", username, firstDay.toString());
        try {
            return stmt.executeQuery(query).getInt("number");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


    float[] query5(Date date){

        float average[] = new float[2]; //0 - distance, 1 - time

        String query = String.format("SELECT AVG(distance) as distance_avg, " +
                "AVG(TIMEDIFF(time_finish, time_start) AS trip_avg, FROM rent WHERE DATE(time_start)=%s", date.toString());

        try {
            ResultSet rs = stmt.executeQuery(query);
            average[0] = rs.getFloat("distance_avg");
            average[1] = rs.getFloat("trip_avg");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return average;
    }


    String[][][] query6(){

        String top_places[][][] = new String[2][3][3]; //first index: 0 - from, 1 - to;
        //second index - time (morning, afternoon, evening), third - place

        String time[][] = {{"\'07:00:00\'", "\'10:00:00\'"}, {"\'12:00:00\'", "\'14:00:00\'"}, {"\'17:00:00\'", "\'19:00:00\'"}};

        try {

            //создаём временную таблицу со значениями времени
            String creating_query = String.format("CREATE TEMPORARY TABLE \'time_intervals\'(" +
                            "\'interval\' STRING NOT NULL" +
                            "\'start_time\' DATE" +
                            "\'end_time\' DATE" +
                            "PRIMARY KEY (\'interval\')" +
                            ")ENGINE=InnoDB DEFAULT CHARSET=utf8;" +
                            "" +
                            "" +
                            "INSERT INTO \'time_intervals\' VALUES (\'morning\', %s, %s)" +
                            "INSERT INTO \'time_intervals\' VALUES (\'afternoon\', %s, %s)" +
                            "INSERT INTO \'time_intervals\' VALUES (\'evening\', %s, %s)",
                    time[0][0], time[0][1], time[1][0], time[1][1], time[2][0], time[2][1]);

            stmt.executeQuery(creating_query);

            String fromto[] = {"from", "to"};

            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 3; j++) {
                    String query_from = String.format("SELECT \'%s\', COUNT(*) as number FROM rent WHERE " +
                                    "(TIME(time_start) >= TIME(%s) AND TIME(time_start) <= TIME(%s)) OR " +
                                    "(TIME(time_finish) >= TIME(%s) AND TIME(time_finish) <= TIME(%s) ORDER BY number DESC",
                            fromto[i], time[j][0], time[j][1], time[j][0], time[j][1]);

                    ResultSet rs = stmt.executeQuery(query_from);

                    for(int k = 0; k < 3; k++){
                        top_places[i][j][k] = rs.getString(fromto[i]);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return top_places;
    }


//    String query10(){
//
//        String type;
//
//        String query_charging = "SELECT charging.id_car, SUM(charging_station.cost*TIMESTAMPDIFF(MINUTE, charging.time_finish, charging.time_start))" +
//                "FROM charging INNER JOIN station (ON charging.id_charging_station = charging_station.uid) GROUP BY charging.id_car";
//
//        String query_repair = "SELECT id_car, SUM(cost) FROM is_repaired_in GROUP BY id_car";
//
//        String query = String.format("SELECT type FROM (%s) ch INNER JOIN (%s) rep (ON ch.id_car = rep.id_car) WHERE ", query_charging, query_repair);
//
//        stmt.executeQuery(query_repair);
//
//        return type;
//    }


}