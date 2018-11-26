package sample;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Queries {

    // JDBC URL, username and password of MySQL server
    private static final String user = "root";
    private static final String password = "enaca2225";

    private static Connection con;
    private static Statement stmt;

    Queries(String url){
        try {
            con = DriverManager.getConnection(url, user, password);
            // getting Statement object to execute query
            stmt = con.createStatement();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }


    ArrayList<String> query1(String username, Date date, String color, String plate){
        String query_rent = String.format("SELECT id_car FROM rent WHERE username_customer=\'%s\' AND " +
                "time_rent LIKE \'%%%s%%\'", username, date.toString());

        String query_car = String.format("SELECT id AS id_car FROM car WHERE registration_plate LIKE \'%s%%\'" +
                " AND color=\'%s\'", plate, color);
        ArrayList<String> cars = new ArrayList<>();

        try {
            String query = String.format("SELECT id_car FROM (%s) A WHERE A.id_car IN (%s)", query_rent, query_car);
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                cars.add(String.valueOf(rs.getInt("id_car")));
            }
            return cars;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    ArrayList<Integer> query2(String uid, Date date){

        String time[] = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14",
                "15", "16", "17", "18", "19", "20", "21", "22", "23"};
        ArrayList<Integer> statistics = new ArrayList<>(24);
        try {

            for(int i = 0; i < 24; i++) {
                String query = String.format("SELECT COUNT(*) AS count FROM charging WHERE id_charging_station =\'%s\' AND " +
                        "(time_start LIKE \'%s %s%%\' OR time_finish LIKE \'%s %s%%\')", uid, date.toString(), time[i], date.toString(), time[i]);
                ResultSet rs = stmt.executeQuery(query);
                rs.next();
                statistics.add(rs.getInt("count"));
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
            ResultSet rs = stmt.executeQuery(query_cars);
            rs.next();
            int totalNumber = rs.getInt("count");

            for(int i = 0; i < 7; i++) {
                for (int j = 0; j < 3; j++) {
                    String query = String.format("SELECT DISTINCT id_car FROM rent WHERE " +
                                    "DATE(time_start)=DATE(\'%s\') AND ((TIME(time_start) >= TIME(\'%s\') AND " +
                                    "TIME(time_start) <= TIME(\'%s\')) OR (TIME(time_finish) >= TIME(\'%s\') AND " +
                                    "TIME(time_finish) <= TIME(\'%s\')))",
                            Date.valueOf(firstDay.plusDays(i)).toString(), time[j][0], time[j][1], time[j][0], time[j][1]);
                    rs = stmt.executeQuery(String.format("SELECT COUNT(*) as count FROM (%s) AS a", query));
                    rs.next();
                    busyNumber[i][j] = rs.getInt("count");
                    busy[i][j] = (float)100*busyNumber[i][j]/totalNumber;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return busy;
    }

    //возвращает количество поездок, за которые пользователь заплатил больше одного раза. вроде..
    ArrayList<Integer> query4(String username){
        LocalDate firstDay = LocalDate.now().minusDays(31);

        String query = String.format("SELECT id_rent FROM payment INNER JOIN rent " +
                "ON payment.id_rent = rent.id WHERE username_customer = \'%s\' AND DATE(date_p) > \'%s\' GROUP BY id_rent HAVING count(*)>1", username, firstDay.toString());
        ArrayList<Integer> result = new ArrayList<>();
        try {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            result.add(rs.getInt("id_rent"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    float[] query5(Date date){

        float average[] = new float[2]; //0 - distance, 1 - time

        String query = String.format("SELECT AVG(distance) as distance_avg, " +
                "AVG(TIMEDIFF(time_finish, time_start)) AS trip_avg FROM rent WHERE DATE(time_start)=\'%s\'", date.toString());

        try {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
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

            String fromto[] = {"from_p", "to_p"};

            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 3; j++) {
                    String query_from = String.format("SELECT %s, COUNT(*) as number FROM rent WHERE " +
                                    "(TIME(time_start) >= TIME(%s) AND TIME(time_start) <= TIME(%s)) OR " +
                                    "(TIME(time_finish) >= TIME(%s) AND TIME(time_finish) <= TIME(%s)) ORDER BY number DESC",
                            fromto[i], time[j][0], time[j][1], time[j][0], time[j][1]);

                    ResultSet rs = stmt.executeQuery(query_from);
                    rs.next();
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


    ArrayList<Integer> query7(){
        String amount_query = "SELECT COUNT(id) AS amount FROM car";


        ArrayList<Integer> result = new ArrayList<>();

        try {
            ResultSet rs = stmt.executeQuery(amount_query);
            rs.next();
            int amount = rs.getInt("amount");

            String query = "SELECT car.id, COUNT(rent.id) as num FROM rent RIGHT OUTER JOIN car ON rent.id_car = car.id " +
                    "WHERE time_rent IS NULL OR DATEDIFF(NOW(), time_rent) <= 90 GROUP BY car.id ORDER BY num LIMIT " +
                    (int)amount/10;

            rs = stmt.executeQuery(query);
            while (rs.next()){
                result.add(rs.getInt("car.id"));
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }




    //не могу понять задание

    /*HashMap<String, Integer> query8(Date starting_date){

        String query = String.format("SELECT username, COUNT(*) FROM rent INNER JOIN (SELECT time_start FROM charging ")



    }*/

    HashMap<Integer, String> query9(){

        String min_day_query = "(SELECT MIN(date_p) FROM is_repaired_in)";

        String query = String.format("SELECT id_workshop, vendor_code_part, MAX(s/(DATEDIFF(NOW(), %s)/7))" +
                "FROM (SELECT id_workshop, vendor_code_part, SUM(amount) as s FROM used_part JOIN is_repaired_in ON used_part.id_is_repaired_in = is_repaired_in.id GROUP BY id_workshop, vendor_code_part) A GROUP BY id_workshop", min_day_query);

        HashMap<Integer, String> result = new HashMap<>();

        try {
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()){
                result.put(rs.getInt("id_workshop"), rs.getString("vendor_code_part"));
            }
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    String query10(){

        String query_charging = "SELECT charging.id_car, " +
                "SUM(charging_station.cost*TIMESTAMPDIFF(MINUTE, charging.time_start, charging.time_finish)) as ch_sum " +
                "FROM charging INNER JOIN charging_station ON charging.id_charging_station = charging_station.uid GROUP BY charging.id_car\n";

        String query_repair = "SELECT id_car, SUM(cost) as rep_sum FROM is_repaired_in GROUP BY id_car";

        String query_sum = String.format("SELECT ch.id_car, ((ch_sum + rep_sum)/DATEDIFF(NOW(), (SELECT date_start FROM car where id = ch.id_car))) as s FROM (%s) ch INNER JOIN (%s) rep ON ch.id_car = rep.id_car", query_charging, query_repair);

        String query = String.format("SELECT type, MAX(s) FROM car INNER JOIN (%s) as s_t ON car.id = s_t.id_car GROUP BY type", query_sum);

        try {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            return rs.getString("type");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


}