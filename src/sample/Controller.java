package sample;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

    public void initialize(){
        //First query

        Queries query = new Queries();

        RegPlate.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER)  {
                    query1(query);
                }
            }
        });

        Color.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER)  {
                    query1(query);
                }
            }
        });

        UsernameCarParameter.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER)  {
                    query1(query);
                }
            }
        });

        //Third query
        UpdateButtonStatisticsBusy.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

        });


    }

    private void query1(Queries query){
        LocalDate localDate = DateCarParameter.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        java.util.Date date = Date.from(instant);
        java.sql.Date sDate = new java.sql.Date(date.getTime());
//        java.sql.Date sqlDate = java.sql.Date.valueOf(DateCarParameter.getValue());
//        System.out.println(sqlDate);

        ArrayList<String> rows = query.query1(UsernameCarParameter.getText(), sDate,
                Color.getText(), RegPlate.getText());

        ObservableList<String> data = CarParameterTable.getItems();
        data.addAll(rows);

        RegPlate.setText("");
        UsernameCarParameter.setText("");
        Color.setText("");
    }

    private void query3(Queries query){
    }

}
