package sample;

import java.sql.*;
import java.util.ArrayList;

public class Scripts {
    // JDBC URL, username and password of MySQL server
    private static final String url = "jdbc:mysql://localhost:3306/carsharingdb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String user = "root";
    private static final String password = "root";

    private static java.sql.PreparedStatement prst = null;
    private static Connection con = null;

    Scripts(){
        try {
            con = DriverManager.getConnection(url, user, password);
            //makingRents();
            //makingParts();
            //makingRepaired();
            makingChargings();

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }
    public void makingChargings(){
        try{
            for (int i = 0; i <47; i++) {

                prst = con.prepareStatement("INSERT INTO charging(time_start, time_finish, id_car, id_charging_station) VALUES(?,?,?,?) ");

                int day = 1 + (int) (Math.random() * 30);
                int hour1 = 7 + (int) (Math.random() * 22);
                int min1 = 1 + (int) (Math.random() * 59);
                int min2 = (min1 + 1) + (int) (Math.random() * 59);
                int sec = 0 + (int) (Math.random() * 59);
                int sec1 = 0 + (int) (Math.random() * 59);
                prst.setTimestamp(1, Timestamp.valueOf("2018-11-" + day + " " + hour1 + ":" + min1 + ":" + sec));
                prst.setTimestamp(2, Timestamp.valueOf("2018-11-" + day + " " + hour1 + ":" + min2 + ":" + sec1));
                prst.setInt(3, 1 + (int) (Math.random() * (7 - 1)));
                prst.setInt(4, 1 + (int) (Math.random() * (6 - 1)));
                prst.executeUpdate();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void makingRepaired(){
        try{
         int idNumber =1;

            ArrayList<String> usedPartsList = new ArrayList();
            for (int i = 0; i <30; i++) {
//                prst = con.prepareStatement("INSERT INTO is_repaired_in(id,date_p,report,id_car,id_workshop) VALUES (?,?,?,?,?)");
//                prst.setInt(1,idNumber);
//                prst.setTimestamp(2,Timestamp.valueOf("2018-11-"+(1+(int)(Math.random()*31-1))+" "+
//                        (0+(int)(Math.random()*23-0))+":"+(0+(int)(Math.random()*59-0))+":"+(0+(int)(Math.random()*59-0))));
//                prst.setString(3,"something broken");
//                prst.setInt(4,(1+(int)(Math.random()*6-1)));
//                prst.setInt(5,(1+(int)(Math.random()*4-1)));
//                prst.executeUpdate();
                idNumber++;


                int idNumber2=1;
                int n = (1 + (int)(Math.random()*(31-1)));
                Statement stmt = con.createStatement();
                int randPart;

                ArrayList<String> allPartsList = new ArrayList();
                ResultSet rs = stmt.executeQuery("SELECT * FROM car_part");
                while (rs.next()){
                    allPartsList.add(rs.getString("vendor_code"));
                }

                for (int j = 0; j < n; j++) {
                    prst = con.prepareStatement("INSERT INTO used_part(id_is_repaired_in, vendor_code_part, amount) VALUES(?,?,?) ");
                    prst.setInt(1, idNumber2);
                    randPart =0 + (int) (Math.random() *(30 - 0));
                    boolean checkWasIt= true;
                    for (int k = 0; k <usedPartsList.size() ; k++) {
                        if (allPartsList.get(randPart).equals(usedPartsList.get(k))){
                            checkWasIt=false;
                            while (allPartsList.get(randPart).equals(usedPartsList.get(k))){
                                randPart =0 + (int) (Math.random() *(30 - 0));
                            }
                            k=-1;
                        }
                    }
                    if (checkWasIt) {
                        prst.setString(2, allPartsList.get(randPart));
                        prst.setInt(3, 1 + (int) (Math.random() *(20 - 1)));
                        usedPartsList.add(allPartsList.get(randPart));
                        prst.executeUpdate();
                    }
                    idNumber2++;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public  void makingParts(){
        try{
            int idNumber= 1;
            for (int i = 0; i < 30; i++) {
                prst= con.prepareStatement("INSERT INTO car_part(vendor_code, name_p, type_p) VALUES (?,?,?)");
                String[] arr= new String[6];
                String str="";
                for (int j = 0; j < 6; j++) {
                    arr[j] = String.valueOf((char)(97 + (int)(Math.random()*(122 - 97))));
                    str=str+arr[j];
                }
                prst.setString(1,str);
                String[] partsArr = {"hinge","bumper", "headlight","hub","knuckle","reducer","gear","turbine","valve","brake cylinder","propshaft"};
                //System.out.println((97 + (int)(Math.random()*(122 - 97))));
                prst.setString(2,partsArr[(int)(Math.random()*10)]);
                String[] typeArr ={"coupe","sedan","suv","pickup"};
                prst.setString(3,typeArr[(int)(Math.random()*3)]);
                prst.executeUpdate();

                if ((0+(int)(Math.random()*(2-0)))==1) {
                    prst = con.prepareStatement("INSERT INTO has_in_stock_provider(id_provider_in_stock,vendor_code_car_part_provider,amount) VALUES (?,?,?)");
                    prst.setInt(1, 1 + (int) (Math.random() * (4 - 1)));
                    prst.setString(2, str);
                    prst.setInt(3, (int) (Math.random() * 30));
                    prst.executeUpdate();
                }
                if ((0+(int)(Math.random()*(2-0)))==1) {
                    prst = con.prepareStatement("INSERT INTO has_in_stock_workshop(id_workshop_in_stock,vendor_code_car_part_workshop,amount) VALUES (?,?,?)");
                    prst.setInt(1, 1 + (int) (Math.random() * (4 - 1)));
                    prst.setString(2, str);
                    prst.setInt(3, (int) (Math.random() * 30));
                    prst.executeUpdate();
                }
                if ((0+(int)(Math.random()*(2-0)))==1) {
                    prst = con.prepareStatement("INSERT INTO provide_part(id, date_p, vendor_code, cost, amount, id_provider, id_workshop) VALUES (?,?,?,?,?,?,?)");
                    prst.setInt(1, idNumber);
                    prst.setTimestamp(2, Timestamp.valueOf("2018-11-" + (1 + (int) (Math.random() * 30)) + " " +
                            (0 + (int) (Math.random() * 24)) + ":" + (0 + (int) (Math.random() * 59)) + ":00"));
                    prst.setString(3,str);
                    prst.setInt(4,100+(int)(Math.random()*(15000-100)));
                    prst.setInt(5,(1+(int)(Math.random()*(15-1))));
                    prst.setInt(6,(1+(int)(Math.random()*(5-1))));
                    prst.setInt(7,(1+(int)(Math.random()*(4-1))));

                    prst.executeUpdate();
                }
                idNumber++;
            }
            } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void makingRents(){
        try{
            int idNumber= 1;
            for (int i = 0; i < 100; i++) {


                prst = con.prepareStatement("INSERT INTO rent(id,time_rent,time_start, time_finish, from_p, to_p, feedback, username_customer, id_car, distance) values(?,?,?,?,?,?,?,?,?,?) ");
                prst.setInt(1, idNumber);

                int day = 1 + (int) (Math.random() * 30);
                int hour1 = 7 + (int)(Math.random() * 22);
                int hour2 = hour1 + (int) (Math.random() * 22);
                int min1 = 1 + (int) (Math.random() * 59);
                int min2 = (min1+1) +(int)(Math.random() * 59);
                int min3 = min2 +(int)(Math.random() * 59);
                int sec = 0 + (int) (Math.random() * 59);
                prst.setTimestamp(2, Timestamp.valueOf("2018-11-" + day + " "+hour1+":"+min1+":"+sec));
                prst.setTimestamp(3, Timestamp.valueOf("2018-11-" + day + " "+hour1+":"+min2+":"+sec));
                prst.setTimestamp(4, Timestamp.valueOf("2018-11-" + day + " "+hour2+":"+min3+":"+sec));

                int coord = 100000 + (int) (Math.random() * 999999);
                int coord1 = 100000 + (int) (Math.random() * 999999);
                int coord2 = 100000 + (int) (Math.random() * 999999);
                int coord3 = 100000 + (int) (Math.random() * 999999);
                prst.setString(5, "51."+coord+" 35."+coord1);
                prst.setString(6, "51."+coord2+" 35."+coord3);

                prst.setString(7, "");
                String[] arr = {"kor1999","ae86best","loluser","mamunsimpotyaga","markpyshka ","nagibator228","noname","papinbrodyaga","player1"};
                String user = arr[0 + (int)(Math.random() * 8)];
                prst.setString(8, user);
                int idCar = 1 + (int)(Math.random() * 6);
                prst.setInt(9, idCar);
                prst.setInt(10, (0 + (int)(Math.random() * 15)));
                prst.executeUpdate();

                prst = con.prepareStatement("INSERT INTO payment(id, id_rent,cost, date_p) VALUES (?,?,?,?)");
                prst.setInt(1,idNumber);
                prst.setInt(2,idNumber);
                int randCost = 50 + (int) (Math.random() * 1000);
                prst.setInt(3,randCost);
                int min4 = min3 + (int) (Math.random() * 59);
                int sec1 = sec + (int) (Math.random() * 59);
                prst.setTimestamp(4,Timestamp.valueOf("2018-11-" + day + " "+hour2+":"+min4+":"+sec1));
                prst.executeUpdate();
                idNumber++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
