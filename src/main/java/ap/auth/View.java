package ap.auth;

import ap.jfx.Controller;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class View extends Application {
    Controller auth=new Controller();
    Scene loginScn;
    Scene signupScn;
    @Override
    public void start(Stage stage) throws Exception {
        //Login page
        TextField nameField=new TextField();
        PasswordField passwordField=new PasswordField();
        Button loginbtn=new Button("Login");
        Button goLogin=new Button("Login");
        Label messagelbl=new Label("");
        VBox loginBox=new VBox(10);
        loginBox.setPadding(new Insets(80));
        loginBox.getChildren().addAll(new Label("Username"), nameField,
                    new Label("Password"),passwordField,
                loginbtn,messagelbl);
        loginbtn.setOnAction(e->{

            if(auth.verify(nameField.getText(),passwordField.getText()))messagelbl.setText("Logged in ");
            else messagelbl.setText("Username or password \ndo not match");
        });
        goLogin.setOnAction(e->stage.setScene(loginScn));
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
                confirmLabel.setText("Password should no more than 12 chars" +
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
        //Scenes
        signupScn=new Scene(signUpBox);
        loginScn=new Scene(loginBox);
        stage.setScene(signupScn);
        stage.show();
    }
}
