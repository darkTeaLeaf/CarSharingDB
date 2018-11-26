package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

public class Controller {


    public TextField RegPlate;
    public TextField Color;
    public TableView<String> CarParameterTable;
    public TextField UsernameCarParameter;
    public DatePicker DateCarParameter;

    public TableView TableStatisticsBusy;
    public Button UpdateButtonStatisticsBusy;

    public DatePicker DateStation;
    public TextField IDStation;
    public TableView<StationAmount> TableStation;
    public TableColumn<StationAmount, String> TableStationTime;
    public TableColumn<StationAmount, Integer> TableStationAmount;

    public void initialize(){
        //First query

        Queries query = new Queries(""); //TODO

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

        });

        //Forth query



    }

    private void query1(Queries query){
        LocalDate localDate = DateCarParameter.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        java.util.Date date = Date.from(instant);
        java.sql.Date sDate = new java.sql.Date(date.getTime());

        ArrayList<String> rows = query.query1(UsernameCarParameter.getText(), sDate,
                Color.getText(), RegPlate.getText());

        ObservableList<String> data = FXCollections.observableArrayList();
        data.addAll(rows);
        CarParameterTable.setItems(data);

        RegPlate.setText("");
        UsernameCarParameter.setText("");
        Color.setText("");
    }

    private void query2(Queries query){
        LocalDate localDate = DateStation.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        java.util.Date date = Date.from(instant);
        java.sql.Date sDate = new java.sql.Date(date.getTime());

        ArrayList<Integer> rows = query.query2(IDStation.getText(), sDate);

        ObservableList<StationAmount> data = FXCollections.observableArrayList();

        TableStationTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        TableStationAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        String time[] = {"00h-01h", "01h-02h", "02h-03h", "03h-04h", "04h-05h", "05h-06h", "06h-07h", "07h-08h",
                "08h-09h", "09h-10h", "10h-11h", "11h-12h", "12h-13h", "13-14h", "14h-15h",
                "15h-16h", "16h-17h", "17h-18h", "18h-19h", "19h-20h", "20h-21h", "21h-22h", "22h-23h", "23h-24h"};

        for (int i = 0; i < rows.size(); i++) {
            data.add(new StationAmount(time[i], rows.get(i)));
        }

        TableStation.setItems(data);

        IDStation.setText("");
    }

    private void query3(Queries query){
    }




    class StationAmount{
        private String time;
        private int amount;

        StationAmount(String time, int amount){
            this.time = time;
            this.amount = amount;
        }
    }

}
