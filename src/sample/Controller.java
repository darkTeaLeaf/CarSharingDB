package sample;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;

public class Controller {


    public TextField RegPlate;
    public TextField Color;
    public TableView<CarParameter> CarParameterTable;
    public TextField UsernameCarParameter;
    public DatePicker DateCarParameter;
    public TableColumn TableCarParameterPlate;

    public DatePicker DateStation;
    public TextField IDStation;
    public TableView<StationAmount> TableStation;
    public TableColumn<StationAmount, String> TableStationTime;
    public TableColumn<StationAmount, Integer> TableStationAmount;

    public TableView TableStatisticsBusy;
    public Button UpdateButtonStatisticsBusy;
    public TableColumn TableStatisticsDay;
    public TableColumn TableStatisticsTime;
    public TableColumn TableStatisticsAmount;

    public TextField UsernameCustomer;
    public TableView TableCustomer;
    public TableColumn TableUsernameCustomer;
    public TableColumn TableOrderCustomer;

    public DatePicker StatisticsDateAverage;
    public TableView TableStatisticsAverage;
    public TableColumn TableStatisticsAverageName;
    public TableColumn TableStatisticsAverageAmount;

    public TableView TableStatisticsPopular;
    public TableColumn TableStatisticsPopularTime;
    public TableColumn TableStatisticsPopularPick;
    public TableColumn TableStatisticsPopularDestination;
    public TableColumn TableStatisticsPopularPlace;
    public Button UpdateButtonStatisticsPopular;

    public TableView TableCarAmount;
    public Button UpdateButtonCarAmount;
    public TableColumn TableCarAmountId;

    public Button UpdateButtonStatisticsType;
    public TextArea TextTypeStatistics;

    public Button UpdateButtonWorkshop;
    public TableView TableWorkshop;
    public TableColumn TableWorkshopId;
    public TableColumn TableWorshopPart;
    
    public DatePicker StatisticsDateLocations;
    public TableView TableStatisticsLocation;
    public TableColumn TableStatisticsLocationUsername;
    public TableColumn TableStatisticsLocationAmount;

    public void initialize(){
        //First query

        Queries query = new Queries("jdbc:mysql://localhost:3306/test");

        RegPlate.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                query1(query);
            }
        });

        Color.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                query1(query);
            }
        });

        UsernameCarParameter.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                query1(query);
            }
        });

        DateCarParameter.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                query1(query);
            }
        });

        //Second query
        IDStation.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                query2(query);
            }
        });

        DateStation.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                query2(query);
            }
        });


        //Third query
        UpdateButtonStatisticsBusy.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            query3(query);
        });

        //Forth query
        UsernameCustomer.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                query4(query);
            }
        });

        //Fifth query
        StatisticsDateAverage.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                query5(query);
            }
        });

        //Sixth query
        UpdateButtonStatisticsPopular.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            query6(query);
        });

        //Seventh query
        UpdateButtonCarAmount.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            query7(query);
        });

        //Eighth query
        StatisticsDateLocations.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                query8(query);
            }
        });

        //Ninth query
        UpdateButtonWorkshop.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            query9(query);
        });

        //Tenth query
        UpdateButtonStatisticsType.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            TextTypeStatistics.setText("");
            query10(query);
        });

    }

    private void query1(Queries query){
        LocalDate localDate = DateCarParameter.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        java.util.Date date = Date.from(instant);
        java.sql.Date sDate = new java.sql.Date(date.getTime());

        ArrayList<String> rows = query.query1(UsernameCarParameter.getText(), sDate,
                Color.getText(), RegPlate.getText());

        ObservableList<CarParameter> data = FXCollections.observableArrayList();

        for (int i = 0; i < rows.size(); i++) {
            data.add(new CarParameter(rows.get(i)));
        }

        TableCarParameterPlate.setCellValueFactory(new PropertyValueFactory<>("id"));

        CarParameterTable.setItems(data);

        RegPlate.setText("");
        UsernameCarParameter.setText("");
        Color.setText("");
        DateCarParameter.setValue(null);
    }

    private void query2(Queries query){
        LocalDate localDate = DateStation.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        java.util.Date date = Date.from(instant);
        java.sql.Date sDate = new java.sql.Date(date.getTime());

        ArrayList<Integer> rows = query.query2(IDStation.getText(), sDate);

        ObservableList<StationAmount> data = FXCollections.observableArrayList();

        String time[] = {"00h-01h", "01h-02h", "02h-03h", "03h-04h", "04h-05h", "05h-06h", "06h-07h", "07h-08h",
                "08h-09h", "09h-10h", "10h-11h", "11h-12h", "12h-13h", "13-14h", "14h-15h",
                "15h-16h", "16h-17h", "17h-18h", "18h-19h", "19h-20h", "20h-21h", "21h-22h", "22h-23h", "23h-24h"};

        for (int i = 0; i < rows.size(); i++) {
            data.add(new StationAmount(time[i], rows.get(i)));
        }

        TableStationTime.setCellValueFactory(new PropertyValueFactory<StationAmount, String>("time"));
        TableStationAmount.setCellValueFactory(new PropertyValueFactory<StationAmount, Integer>("amount"));

        TableStation.setItems(data);

        IDStation.setText("");
        DateStation.setValue(null);
    }

    private void query3(Queries query){
        float[][] rows = query.query3();
        String time[] = {"Morning", "Afternoon", "Evening"};

        ObservableList<StatisticsBusy> data = FXCollections.observableArrayList();

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 3; j++) {
                data.add(new StatisticsBusy(String.valueOf(8 - i) + " days ago", time[j],rows[i][j]));
            }
        }

        TableStatisticsDay.setCellValueFactory(new PropertyValueFactory<>("day"));
        TableStatisticsTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        TableStatisticsAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableStatisticsBusy.setItems(data);
    }

    private void query4(Queries query){
        ArrayList<Integer> rows = query.query4(UsernameCustomer.getText());

        ObservableList<Customer> data = FXCollections.observableArrayList();

        for (int i = 0; i < rows.size(); i++) {
            data.add(new Customer(UsernameCustomer.getText(), rows.get(i)));
        }

        TableUsernameCustomer.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableOrderCustomer.setCellValueFactory(new PropertyValueFactory<>("orderID"));

        TableCustomer.setItems(data);

        UsernameCustomer.setText("");
    }

    private void query5(Queries query){
        LocalDate localDate = StatisticsDateAverage.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        java.util.Date date = Date.from(instant);
        java.sql.Date sDate = new java.sql.Date(date.getTime());

        float[] rows = query.query5(sDate);

        ObservableList<StaristicsAverage> data = FXCollections.observableArrayList();

        data.add(new StaristicsAverage("Average distance", rows[0]));
        data.add(new StaristicsAverage("Average trip duration", rows[1]));

        TableStatisticsAverageName.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableStatisticsAverageAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableStatisticsAverage.setItems(data);
        StatisticsDateAverage.setValue(null);
    }

    private void query6(Queries query){
        String[][][] rows = query.query6();
        String time[] = {"Morning", "Afternoon", "Evening"};

        ObservableList<StatisticsPopular> data = FXCollections.observableArrayList();

        data.add(new StatisticsPopular(rows[0][0][0], time[0], 1, rows[1][0][0]));
        data.add(new StatisticsPopular(rows[0][0][1], time[0], 2, rows[1][0][1]));
        data.add(new StatisticsPopular(rows[0][0][2], time[0], 3, rows[1][0][2]));

        data.add(new StatisticsPopular(rows[0][1][0], time[1], 1, rows[1][1][0]));
        data.add(new StatisticsPopular(rows[0][1][1], time[1], 2, rows[1][1][1]));
        data.add(new StatisticsPopular(rows[0][1][2], time[1], 3, rows[1][1][2]));

        data.add(new StatisticsPopular(rows[0][2][0], time[2], 1, rows[1][2][0]));
        data.add(new StatisticsPopular(rows[0][2][1], time[2], 2, rows[1][2][1]));
        data.add(new StatisticsPopular(rows[0][2][2], time[2], 3, rows[1][2][2]));

        TableStatisticsPopularPick.setCellValueFactory(new PropertyValueFactory<>("pickUp"));
        TableStatisticsPopularTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        TableStatisticsPopularPlace.setCellValueFactory(new PropertyValueFactory<>("place"));
        TableStatisticsPopularDestination.setCellValueFactory(new PropertyValueFactory<>("destination"));

        TableStatisticsPopular.setItems(data);
    }

    private void query7(Queries query){
        ArrayList<Integer> rows = query.query7();

        ObservableList<CarAmount> data = FXCollections.observableArrayList();

        for (int i = 0; i < rows.size(); i++) {
            data.add(new CarAmount(rows.get(i)));
        }

        TableCarAmountId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableCarAmount.setItems(data);
    }

    private void query8(Queries query){
        LocalDate localDate = StatisticsDateLocations.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        java.util.Date date = Date.from(instant);
        java.sql.Date sDate = new java.sql.Date(date.getTime());

//        HashMap<String, Integer> rows = query.query8(sDate);

        ObservableList<StatisticsLocation> data = FXCollections.observableArrayList();

//        for(String key: rows.keySet()){
//            data.add(new StatisticsLocation(rows.get(key), key));
//        }

        TableStatisticsLocationUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableStatisticsLocationAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableStatisticsLocation.setItems(data);
        StatisticsDateLocations.setValue(null);
    }


    private void query9(Queries query){
        HashMap<Integer,String> rows = query.query9();

        ObservableList<Workshop> data = FXCollections.observableArrayList();

        for(Integer key: rows.keySet()){
            data.add(new Workshop(key, rows.get(key)));
        }

        TableWorkshopId.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableWorshopPart.setCellValueFactory(new PropertyValueFactory<>("part"));

        TableWorkshop.setItems(data);
    }

    private void query10(Queries query){
        String row = query.query10();

        TextTypeStatistics.setText("The most expensive type of car to maintain is " + row);
    }

    public class CarParameter{
        private SimpleStringProperty id;

        CarParameter(String id){
            this.id = new SimpleStringProperty(id);
        }

        public String getId(){
            return id.get();
        }

        public void setId(String id){
            this.id.set(id);
        }
    }

    public class StationAmount{
        private SimpleStringProperty time;
        private SimpleIntegerProperty amount;

        StationAmount(String time, int amount){
            this.time = new SimpleStringProperty(time);
            this.amount = new SimpleIntegerProperty(amount);
        }

        public String getTime(){
            return time.get();
        }

        public void setTime(String time){
            this.time.set(time);
        }

        public int getAmount(){
            return amount.get();
        }

        public void setAmount(int amount){
            this.amount.set(amount);
        }
    }

    public class StatisticsBusy{
        private SimpleStringProperty day;
        private SimpleStringProperty time;
        private SimpleFloatProperty amount;

        StatisticsBusy(String day, String time, float amount){
            this.day = new SimpleStringProperty(day);
            this.time = new SimpleStringProperty(time);
            this.amount = new SimpleFloatProperty(amount);
        }

        public String getDay(){
            return day.get();
        }

        public void setDay(String day){
            this.day.set(day);
        }

        public String getTime(){
            return time.get();
        }

        public void setTime(String time){
            this.time.set(time);
        }

        public float getAmount(){
            return amount.get();
        }

        public void setAmount(float amount){
            this.amount.set(amount);
        }

    }

    public class Customer{
        private SimpleStringProperty username;
        private SimpleIntegerProperty orderID;

        Customer(String username, int orderID){
            this.username = new SimpleStringProperty(username);
            this.orderID = new SimpleIntegerProperty(orderID);
        }

        public String getUsername(){
            return username.get();
        }

        public void setUsername(String day){
            this.username.set(day);
        }


        public int getOrderID(){
            return orderID.get();
        }

        public void setOrderID(int orderID){
            this.orderID.set(orderID);
        }

    }

    public class StaristicsAverage{
        private SimpleStringProperty name;
        private SimpleFloatProperty amount;

        StaristicsAverage(String name, float amount){
            this.name = new SimpleStringProperty(name);
            this.amount = new SimpleFloatProperty(amount);
        }

        public String getName(){
            return name.get();
        }

        public void setName(String day){
            this.name.set(day);
        }


        public float getAmount(){
            return amount.get();
        }

        public void setAmount(float orderID){
            this.amount.set(orderID);
        }
    }

    public class StatisticsPopular{
        private SimpleStringProperty time;
        private SimpleStringProperty pickUp;
        private SimpleStringProperty destination;
        private SimpleIntegerProperty place;

        StatisticsPopular(String pickUp, String time, int place, String destination){
            this.time = new SimpleStringProperty(time);
            this.pickUp = new SimpleStringProperty(pickUp);
            this.destination = new SimpleStringProperty(destination);
            this.place = new SimpleIntegerProperty(place);
        }

        public String getTime(){
            return time.get();
        }

        public void setTime(String time){
            this.time.set(time);
        }

        public String getPickUp(){
            return pickUp.get();
        }

        public void setPickUp(String pickUp){
            this.pickUp.set(pickUp);
        }

        public String getDestination(){
            return destination.get();
        }

        public void setAmount(String destination){
            this.destination.set(destination);
        }

        public int getPlace(){
            return this.place.get();
        }

        public void setPlace(int place){
            this.place.set(place);
        }
    }

    class CarAmount{
        private SimpleIntegerProperty id;

        CarAmount(int id){
            this.id = new SimpleIntegerProperty(id);
        }

        public int getId(){
            return id.get();
        }

        public void setId(int id){
            this.id.set(id);
        }
    }

    public class Workshop{
        private SimpleIntegerProperty id;
        private SimpleStringProperty part;

        Workshop(int id, String part){
            this.id = new SimpleIntegerProperty(id);
            this.part = new SimpleStringProperty(part);
        }

        public int getId(){
            return id.get();
        }

        public void setId(int id){
            this.id.set(id);
        }

        public String getPart(){
            return part.get();
        }

        public void setPart(String part){
            this.part.set(part);
        }
    }

    public class StatisticsLocation{
        private SimpleIntegerProperty amount;
        private SimpleStringProperty username;

        StatisticsLocation(int amount, String username){
            this.amount = new SimpleIntegerProperty(amount);
            this.username = new SimpleStringProperty(username);
        }

        public int getAmount(){
            return amount.get();
        }

        public void setAmount(int amount){
            this.amount.set(amount);
        }

        public String getUsername(){
            return username.get();
        }

        public void setUsername(String username){
            this.username.set(username);
        }
    }

}
