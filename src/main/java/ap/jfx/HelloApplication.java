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
import java.util.Objects;
import java.util.stream.IntStream;

import static java.util.Arrays.stream;
import static javafx.geometry.Pos.BASELINE_CENTER;
import static javafx.geometry.Pos.CENTER;

public class HelloApplication extends Application {
    ManageData jdbcConnect=new ManageData("eventmanagment");
    Controller auth=new Controller();
    Scene loginScn;
    Scene signupScn;
    Button home=new Button("Go back");
    Button home1=new Button("Go back");
    EventData clickedRow;
    VBox mainBox;
    boolean rsvp=false;
    String username;
    VBox leftPanel;

    @Override
    public void start(Stage stage) throws IOException {
        TextField nameField=new TextField();
        PasswordField passwordField=new PasswordField();
        Button loginbtn=new Button("Login");
        Button goLogin=new Button("Login");
        Label messagelbl=new Label("");
        Button goSignUp=new Button("Sign up");
        goSignUp.setOnAction(e->stage.setScene(signupScn));
        VBox loginBox=new VBox(10);
        loginBox.setPadding(new Insets(80));
        loginBox.getChildren().addAll(new Label("Username"), nameField,
                new Label("Password"),passwordField,
                loginbtn,messagelbl,goSignUp);

        goLogin.setOnAction(e->stage.setScene(loginScn));
        //DIsplay rsvped events
        VBox rsvpBox=new VBox(10);

        //SignUp
        ComboBox<String> choiceCmbox=new ComboBox<>();
        TextField nameFieldsu=new TextField();
        PasswordField passwordFieldsu=new PasswordField();
        PasswordField confirmPass=new PasswordField();
        Label confirmLabel=new Label("");
        Label signuplbl=new Label("");
        Button signUpbtn=new Button("SignUp");
        choiceCmbox.getItems().addAll("User","Organizer");
        choiceCmbox.setValue("User");
        VBox comboBox=new VBox(choiceCmbox);
        VBox signUpBox=new VBox(10);
        signUpBox.setPadding(new Insets(80));
        signUpBox.getChildren().addAll(new Label("Name"),nameFieldsu,
                new Label("Password"),passwordFieldsu,
                new Label("Confirm Password"),confirmPass,
                confirmLabel,comboBox,signuplbl,signUpbtn
                ,goLogin
        );
        signUpbtn.setOnAction(e->{

            if(!passwordFieldsu.getText().equals(confirmPass.getText())){
                confirmLabel.setText("Password doesn't match");
            }
            else if(passwordFieldsu.getText().length()>12){
                confirmLabel.setText("Password should be no more than 12 chars" +
                        " ");
            }else if(passwordFieldsu.getText().isEmpty() || nameFieldsu.getText().isEmpty()){
                signuplbl.setText("There is an empty field.");
            }
            else if(auth.signUp(nameFieldsu.getText(),passwordFieldsu.getText(),choiceCmbox.getValue().equals("Organizer"))){
                signUpBox.getChildren().stream().filter(node->node instanceof TextField).forEach(node -> ((TextField) node).setText(""));
                signUpBox.getChildren().stream().filter(node->node instanceof PasswordField).forEach(node -> ((PasswordField) node).setText(""));
                signuplbl.setText("Signed Up Successfully");
            }else{
                signuplbl.setText("The username is taken");
            }
        });
        //Table View
        TableView<EventData> table=new TableView<>();
        TableColumn<EventData,Integer> col1=new TableColumn<>("ID");
        col1.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<EventData,String> col2=new TableColumn<>("Title");
        col2.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<EventData,String> col3=new TableColumn<>("Organizer");
        col3.setCellValueFactory(new PropertyValueFactory<>("organizer"));
        TableColumn<EventData,String> col4=new TableColumn<>("Date");
        col4.setCellValueFactory(new PropertyValueFactory<>("date"));
        TableColumn<EventData,String> col5=new TableColumn<>("Location");
        col5.setCellValueFactory(new PropertyValueFactory<>("location"));

        EventHandler<ActionEvent> refresh=a->table.setItems(jdbcConnect.getUpcomingEvents());
        Text descripttiontxt=new Text();
        Button rsvpbtn=new Button("RSVP");
        Button showrsvpsbtn=new Button("RSVPed events");
        showrsvpsbtn.setOnAction(e->{
            VBox temp=new VBox(10);

            ManageData.getRsvped(username).forEach(val->temp.getChildren().add(new Label(val)));
            if(rsvp) leftPanel.getChildren().removeLast();
            else rsvp=true;
            leftPanel.getChildren().add(temp);
        });
        Label rsvplbl=new Label("");
        table.setRowFactory(tv -> {
            TableRow<EventData> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == javafx.scene.input.MouseButton.PRIMARY && event.getClickCount() == 1) {
                    clickedRow = row.getItem();
                    descripttiontxt.setText(jdbcConnect.getDescription(clickedRow.getId()));
                }
            });
            return row;
        });
        rsvpbtn.setOnAction(e-> {
            if(!ManageData.checkNameInFile(clickedRow.name,username)){
                jdbcConnect.rsvp(clickedRow.id);
                ManageData.appendNameToFile(clickedRow.name,username);
            }else{
                rsvplbl.setText("Already rspved");
            }
            //Objects.requireNonNull(ManageData.getRsvped(username)).forEach(str->rsvpBox.getChildren().add(new Label(str)));
        });

                Button refreshbtn=new Button("Refresh");
                table.getColumns().addAll(col1, col2, col3, col4, col5);
        table.setItems(jdbcConnect.getUpcomingEvents());
        VBox fnl=new VBox(10);
        leftPanel=new VBox(10);
        leftPanel.setPadding(new Insets(30));
        leftPanel.setMaxWidth(100);
        leftPanel.getChildren().addAll(new Label("Description"),descripttiontxt,rsvpbtn,showrsvpsbtn,rsvpBox);
        fnl.setPadding(new Insets(40));
        HBox eventsOp=new HBox(10);
        eventsOp.getChildren().addAll(fnl,leftPanel);
        fnl.getChildren().addAll(table,refreshbtn,home1);

        TextArea description=new TextArea();
        description.setMinHeight(100);
        description.setMaxWidth(200);

        //Scenes
        Scene homescn=new Scene(eventsOp);
        signupScn=new Scene(signUpBox);
        loginScn=new Scene(loginBox);
        //Button actions


        //addbtn.addEventHandler(ActionEvent.ACTION,a->stage.setScene(dataTablescn));
        home.setOnAction(a->stage.setScene(homescn));
        home1.setOnAction(a->stage.setScene(homescn));
        loginbtn.setOnAction(e->{

            if(auth.verify(nameField.getText(),passwordField.getText())){
                ArrayList<Object> user=auth.getUserData(nameField.getText());
                if((Boolean) user.get(1)){
                    Organizer a=new Organizer(jdbcConnect,(String) user.getFirst());

                try {
                    a.start(new Stage());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                }else{
                    username=nameField.getText();
                    stage.setScene(homescn);
                }
            } else messagelbl.setText("Username or password \ndo not match");
        });
        refreshbtn.setOnAction(refresh);

//        stage.setMinHeight(600);
//        stage.setMinWidth(500);
        stage.setScene(loginScn);
        stage.show();
    }

    //Homepage
    public static void main(String[] args) {
        launch();
    }
}