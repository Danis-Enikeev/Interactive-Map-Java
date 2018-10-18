package View;

import Controller.Config;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class LoginScreen extends GridPane {

    public LoginScreen(Config config) {
        setAlignment(Pos.CENTER);
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(25, 25, 25, 25));
        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        add(userName, 0, 1);

        TextField userTextField = new TextField();
        add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        add(pwBox, 1, 2);
        Button btn = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        add(hbBtn, 1, 4);

        final Text actiontarget = new Text();
        add(actiontarget, 1, 6);
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                if (config.getSettings().get("Login").equals(userTextField.getText()) && config.getSettings().get("Password").equals(pwBox.getText())) {
                    config.Write("Developer mode", "1");
                } else {
                    config.Write("Developer mode", "0");
                }
                if (config.getSettings().get("Developer mode").equals("0")) {
                    try {
                        File file = new File("err");
                        FileOutputStream fos = new FileOutputStream(file);
                        PrintStream ps = new PrintStream(fos);
                        System.setErr(ps);
                    } catch (FileNotFoundException err) {
                        System.err.println("Error opening error log file");
                    }
                }
                getScene().getWindow().hide();
            }
        });

    }
}
