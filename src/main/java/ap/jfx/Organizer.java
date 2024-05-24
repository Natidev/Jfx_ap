package ap.jfx;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static javafx.geometry.Pos.CENTER;
public class Organizer extends Application{
    Button home = new Button("Go back");
    Button home1 = new Button("Go back");
    VBox mainBox;
    ManageData jdbcConnect;
    String organizer;
    public Organizer(ManageData jdbcConnect,String organizer){
        this.jdbcConnect=jdbcConnect;
        this.organizer=organizer;
    }


    public class FormElements {
        VBox InputArea;
        TextInputControl inptField;
        Label info;

        public FormElements(TextInputControl a, Label b) {
            if (a.getClass().getName().equals("javafx.scene.control.TextField")) a.setMaxWidth(150);
            inptField = a;
            info = b;
            InputArea = new VBox();
            InputArea.getChildren().add(inptField);
            InputArea.getChildren().add(info);
        }

        public VBox getBox() {
            return InputArea;
        }

        public boolean isEmpty() {
            return inptField.getText().isEmpty();
        }

        public void setMessage(String s) {
            info.setText(s);
        }
    }


    @Override
    public void start(Stage stage) throws IOException {
        //Table View
        TableView<EventData> table = new TableView<>();
        TableColumn<EventData, Integer> col1 = new TableColumn<>("ID");
        col1.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<EventData, String> col2 = new TableColumn<>("Title");
        col2.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<EventData, String> col3 = new TableColumn<>("Organizer");
        col3.setCellValueFactory(new PropertyValueFactory<>("organizer"));
        TableColumn<EventData, String> col4 = new TableColumn<>("Date");
        col4.setCellValueFactory(new PropertyValueFactory<>("date"));
        TableColumn<EventData, String> col5 = new TableColumn<>("Location");
        col5.setCellValueFactory(new PropertyValueFactory<>("location"));

        EventHandler<ActionEvent> refresh = a -> table.setItems(jdbcConnect.getUpcomingEvents());
        Text descripttiontxt = new Text();
        TextField rmvEventInpt = new TextField();
        Button rmvEvent = new Button("Remove");
        rmvEvent.setVisible(false);
        rmvEvent.setManaged(false);
        //Modify Event panel
        VBox modBox=new VBox(10);
        modBox.setPadding(new Insets(5));
        TextField eventTitle=new TextField();
        TextField eventDate=new TextField();
        TextField eventLocation=new TextField();
        TextArea eventDescription=new TextArea();
        Label modMessage=new Label("");
        Button modify=new Button("Modify");
        modify.setOnAction(e->{
            if(jdbcConnect.ModifyEvent(eventTitle.getText(),eventDate.getText(),eventLocation.getText(),eventDescription.getText(),Integer.parseInt(rmvEventInpt.getText())))modBox.setVisible(false);
            else modMessage.setText("Something went wrong check your input");
        });
        modBox.setVisible(false);
        modBox.setManaged(false);
        modBox.getChildren().addAll(new Label("Title"),eventTitle,
            new Label("Date"),eventDate,
                new Label("Location"),eventLocation,
                new Label("Description"),eventDescription,
                modify);
        modify.addEventHandler(ActionEvent.ACTION, refresh);

        rmvEventInpt.setDisable(true);
        table.setRowFactory(tv -> {
            TableRow<EventData> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == javafx.scene.input.MouseButton.PRIMARY && event.getClickCount() == 1) {
                    EventData clickedRow = row.getItem();
                    descripttiontxt.setText(jdbcConnect.getDescription(clickedRow.getId()));
                    if(organizer.equals(clickedRow.organizer)){
                        modBox.setVisible(true);
                        modBox.setManaged(true);
                        modBox.setVisible(true);
                        modBox.setManaged(true);
                    rmvEventInpt.setText("" + clickedRow.getId());
                    eventTitle.setText(clickedRow.name);
                    eventDate.setText(clickedRow.date);
                    eventLocation.setText(clickedRow.location);
                    eventDescription.setText(clickedRow.description);
                    }else{
                        rmvEventInpt.setText("");
                        eventTitle.setText("");
                        eventDate.setText("");
                        eventLocation.setText("");
                        eventDescription.setText("");

                        modBox.setVisible(false);
                        modBox.setManaged(false);
                        modBox.setVisible(false);
                        modBox.setManaged(false);
                    }
                }
            });
            return row;
        });

        //Remove an event

        Label rmvLbl = new Label("Id of the event");
        Label rmvMessage = new Label("");
        Button refreshbtn = new Button("Refresh");
        EventHandler<ActionEvent> rmvAction = a -> {
            jdbcConnect.RemoveEvent(Integer.parseInt(rmvEventInpt.getText()));
            rmvEventInpt.setText("");
            rmvMessage.setText("Removed event");
        };
        rmvEvent.addEventHandler(ActionEvent.ACTION, rmvAction);
        rmvEvent.addEventHandler(ActionEvent.ACTION, refresh);


        table.getColumns().addAll(col1, col2, col3, col4, col5);
        table.setItems(jdbcConnect.getUpcomingEvents());
        VBox fnl = new VBox(10);
        VBox leftPanel = new VBox(10);
        leftPanel.setPadding(new Insets(30));
        VBox removeOp = new VBox(20);
        removeOp.getChildren().addAll(rmvLbl, rmvEventInpt, rmvEvent);
        leftPanel.getChildren().addAll(new Label("Description"), descripttiontxt, removeOp,
                modBox);
        fnl.setPadding(new Insets(40));
        HBox eventsOp = new HBox(10);
        eventsOp.getChildren().addAll(fnl, leftPanel);
        fnl.getChildren().addAll(table, refreshbtn, home1);

        //Insertion Form
        List<Label> formlbl = new ArrayList<>(Arrays.asList(new Label("Event Title: ")
                , new Label("Organizer: "),
                new Label("Event Date: "),
                new Label(""),
                new Label("Location:"),
                new Label("Description")
        ));
        TextArea description = new TextArea();
        description.setMinHeight(100);
        description.setMaxWidth(200);
        List<FormElements> inpt = new ArrayList<>(
                Arrays.asList(
                        new FormElements(new TextField(), new Label("Unique")),
                        new FormElements(new TextField(), new Label("")),
                        new FormElements(new TextField(), new Label("yyyy-mm-dd")),
                        new FormElements(new TextField(), new Label("24hr format 6:30")),
                        new FormElements(new TextField(""), new Label("Place")),
                        new FormElements(description, new Label("Description"))
                ));
        inpt.get(1).inptField.setText(organizer);
        inpt.get(1).inptField.setDisable(true);
        GridPane grid = new GridPane();
        IntStream.range(0, 6).forEach(i -> {
            grid.addColumn(0, formlbl.get(i));
            grid.addColumn(1, inpt.get(i).getBox());
        });
        Button addbtn = new Button("Verify and Add");
        addbtn.addEventHandler(ActionEvent.ACTION, e -> {
            inpt.stream().filter(fl -> fl.isEmpty() && !fl.getClass().getName().equals("javafx.scene.control.TextField")).forEach(fl -> fl.setMessage("*Required Field"));
            if (inpt.stream().filter(fl -> fl.isEmpty() && fl.getClass().getName() != "javafx.scene.control.TextField").count() == 0) {
                Object[] a = inpt.stream().map(fl -> fl.inptField.getText()).toArray();
                inpt.stream().forEach(obj -> obj.inptField.setText(""));
                if (a[5] == null)
                    jdbcConnect.insertEvent((String) a[1], (String) a[0], (String) a[2] + " " + (String) a[3] + ":00", (String) a[4]);
                else
                    jdbcConnect.insertEvent((String) a[1], (String) a[0], (String) a[2] + " " + (String) a[3] + ":00", (String) a[4], (String) a[5]);
            }

        });
        addbtn.addEventHandler(ActionEvent.ACTION, refresh);

        grid.addRow(4, addbtn);
        grid.setPadding(new Insets(40));
        VBox insertionForm = new VBox(10);
        insertionForm.setFillWidth(true);
        insertionForm.setPadding(new Insets(40));
        insertionForm.getChildren().addAll(home, grid);

        Label welcome = new Label("Welcome choose how to proceed");
        Button addEvents = new Button("Add an Event");
        Button showEvents = new Button("Show events");
        HBox actionBox = new HBox(30);
        actionBox.getChildren().add(addEvents);
        actionBox.getChildren().add(showEvents);
        actionBox.setAlignment(CENTER);

        mainBox = new VBox(25);
        mainBox.setFillWidth(true);
        mainBox.setAlignment(CENTER);
        mainBox.setPadding(new Insets(20));
        mainBox.getChildren().addAll(welcome, actionBox);
        //Save event Objects

        //Scenes
        Scene homescn = new Scene(mainBox);
        Scene dataTablescn = new Scene(eventsOp);
        Scene formscn = new Scene(insertionForm);

        //Button actions
        addEvents.setOnAction(a -> stage.setScene(formscn));
        showEvents.setOnAction(a -> stage.setScene(dataTablescn));
        //addbtn.addEventHandler(ActionEvent.ACTION,a->stage.setScene(dataTablescn));
        home.setOnAction(a -> stage.setScene(homescn));
        home1.setOnAction(a -> stage.setScene(homescn));
        refreshbtn.setOnAction(refresh);
        stage.setScene(homescn);
        stage.show();
    }
}