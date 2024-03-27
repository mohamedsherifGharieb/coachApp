package sample;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Calendar;
import java.util.zip.DeflaterOutputStream;

import javafx.application.Application;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Control;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;

import org.apache.commons.io.IOUtils;

import sample.WeekPlan.Patient;
import sample.WeekPlan.Program;
import sample.utils.Adaptor;
import sample.utils.LoadPlan;
//import sun.security.x509.AVA;
import sample.utils.SavePlan;

public class Main extends Application {

    Adaptor adaptor = Adaptor.getInstance();
    Stage addPStage = new Stage();
    Stage removePStage = new Stage();
    Stage addTStage = new Stage();
    Stage removeTStage = new Stage();
    Stage newWStage = new Stage();
    Stage removeWStage = new Stage();
    Stage saveAndExitStage = new Stage();
    Stage errorMessage = new Stage();
    Stage overPerfStage = new Stage();
    Stage loginStage = new Stage();
    Stage webView = new Stage();
    Stage signupStage=  new Stage();
    String  setFiles = "";
    String pgRequest = "";
    final String local = "http://localhost:8080";
    final String heroku = "https://adhdserver.herokuapp.com";
    String urlFinal = heroku; //local //heroku
//    Button signupEBtn = new Button();
//    Calendar calendar = new GregorianCalendar();

    VBox signupVBox = new VBox();
    PasswordField repeatPassField = new PasswordField();
    TextField userNameSField = new TextField();
    PasswordField passwordSField = new PasswordField();

    public void start(Stage stage) throws Exception {
        //setting buttons effect
        int depth = 3;
        DropShadow borderGlow= new DropShadow();
        borderGlow.setOffsetY(0f);
        borderGlow.setOffsetX(0f);
        borderGlow.setColor(Color.WHITE);
        borderGlow.setWidth(depth);
        borderGlow.setHeight(depth);

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        root.setStyle("-fx-accent: lightblue;-fx-focus-color: lightblue;");
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.setWidth(1003);
        stage.setHeight(660);

        //webView to connect to server
        WebView web = new WebView();

        web.setPrefSize(640, 390);
        webView = new Stage();
        webView.initModality(Modality.APPLICATION_MODAL);
        webView.initOwner(stage);
        webView.centerOnScreen();
        webView.setScene(new Scene(web));

        final WebEngine webengine = web.getEngine();



        //error message
        VBox errorMessageVBox = new VBox();
        //error Label
        Label errorLabel = new Label("Error");
        errorLabel.setTextAlignment(TextAlignment.CENTER);
        errorLabel.setTextFill(Color.WHITE);
        errorLabel.setAlignment(Pos.CENTER);
        errorLabel.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 24));
        errorLabel.setContentDisplay(ContentDisplay.CENTER);
        errorLabel.setPrefWidth(Control.USE_COMPUTED_SIZE);
        errorLabel.setPrefHeight(60);

        HBox errorLabelHBox = new HBox();
        errorLabelHBox.getChildren().add(errorLabel);
        errorLabelHBox.setAlignment(Pos.CENTER);
        errorLabelHBox.setStyle("-fx-background-color: #2e6a6f;");

        //error Messaage
        Label errorText = new Label("Error");
        errorText.setTextAlignment(TextAlignment.CENTER);
        errorText.setAlignment(Pos.CENTER);
        errorText.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 16));
        errorText.setContentDisplay(ContentDisplay.CENTER);
        errorText.setPrefWidth(460);
        errorText.setPrefHeight(70);

        HBox errorTextHBox = new HBox();
        errorTextHBox.getChildren().addAll(errorText);
        errorTextHBox.setAlignment(Pos.CENTER);

        //Buttons
        Button errorOkButton = new Button("Ok");
        errorOkButton.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 20));
        errorOkButton.setTextFill(Color.WHITE);
        errorOkButton.setPrefWidth(150);
        errorOkButton.setPrefHeight(25);
        errorOkButton.setStyle("-fx-background-color: #2b686d55;");

        errorOkButton.setOnMouseEntered(event4 -> {
            errorOkButton.setEffect(borderGlow);
        });
        errorOkButton.setOnMouseExited(event4 -> {
            errorOkButton.setEffect(null);
        });

        HBox buttonsError = new HBox();
        buttonsError.setStyle("-fx-background-color: #2e6a6f;");
        buttonsError.setAlignment(Pos.CENTER);
        buttonsError.setSpacing(70);
        buttonsError.getChildren().addAll(errorOkButton);

        errorMessageVBox.getChildren().addAll(errorLabelHBox, errorTextHBox, buttonsError);
        errorMessageVBox.setAlignment(Pos.CENTER);
        errorMessageVBox.setSpacing(30);
        Scene errorScene = new Scene(errorMessageVBox, 500, 208);

        errorMessage.initModality(Modality.APPLICATION_MODAL);
        errorMessage.initOwner(stage);
        errorMessage.centerOnScreen();
        errorMessage.initStyle(StageStyle.TRANSPARENT);
        errorMessage.setAlwaysOnTop(true);
        errorMessage.setResizable(false);

        //Login window
        VBox loginVBox = new VBox();
        //Login Label
        Label loginLabel = new Label("Login");
        loginLabel.setTextAlignment(TextAlignment.CENTER);
        loginLabel.setAlignment(Pos.CENTER);
        loginLabel.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 24));
        loginLabel.setContentDisplay(ContentDisplay.CENTER);
        loginLabel.setTextFill(Color.WHITE);
        loginLabel.setPrefWidth(Control.USE_COMPUTED_SIZE);
        loginLabel.setPrefHeight(60);

        HBox loginLabelHBox = new HBox();
        loginLabelHBox.getChildren().add(loginLabel);
        loginLabelHBox.setAlignment(Pos.CENTER);
        loginLabelHBox.setStyle("-fx-background-color: #2e6a6f;");

        //user Name HBox
        Label userNameLabel = new Label("User Name:");
        userNameLabel.setTextAlignment(TextAlignment.CENTER);
        userNameLabel.setAlignment(Pos.CENTER);
        userNameLabel.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 15));
        userNameLabel.setContentDisplay(ContentDisplay.CENTER);
        userNameLabel.setPrefWidth(118);
        userNameLabel.setPrefHeight(40);

        TextField userNameField = new TextField();
        userNameField.setPrefSize(270, 40);
        userNameField.setEditable(true);
        userNameField.requestFocus();

        HBox userNameHBox = new HBox();
        userNameHBox.getChildren().addAll(userNameLabel, userNameField);
        userNameHBox.setAlignment(Pos.CENTER);
        userNameHBox.setSpacing(5);

        // password HBox
        Label passwordLabel = new Label("Password:");
        passwordLabel.setTextAlignment(TextAlignment.CENTER);
        passwordLabel.setAlignment(Pos.CENTER);
        passwordLabel.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 15));
        passwordLabel.setContentDisplay(ContentDisplay.CENTER);
        passwordLabel.setPrefWidth(118);
        passwordLabel.setPrefHeight(40);

        PasswordField passwordField = new PasswordField();
        passwordField.setPrefSize(270, 40);
        passwordField.setEditable(true);
        passwordField.setPromptText("min. 8 characters");

        HBox passwordHBox = new HBox();
        passwordHBox.getChildren().addAll(passwordLabel, passwordField);
        passwordHBox.setAlignment(Pos.CENTER);
        passwordHBox.setSpacing(5);

        //Buttons
        Button loginBtn = new Button("Login");
        loginBtn.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 20));
        loginBtn.setTextFill(Color.WHITE);
        loginBtn.setPrefWidth(150);
        loginBtn.setPrefHeight(25);
        loginBtn.setStyle("-fx-background-color: #2b686d55;");
        loginBtn.setDisable(true);

        Button signupPBtn = new Button("Signup");
        signupPBtn.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 20));
        signupPBtn.setTextFill(Color.WHITE);
        signupPBtn.setPrefWidth(150);
        signupPBtn.setPrefHeight(25);
        signupPBtn.setStyle("-fx-background-color: #2e6a6f55;");

        Button cancelLogin = new Button("Exit");
        cancelLogin.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 20));
        cancelLogin.setTextFill(Color.WHITE);
        cancelLogin.setPrefWidth(150);
        cancelLogin.setPrefHeight(25);
        cancelLogin.setStyle("-fx-background-color: #2e6a6f55;");

        HBox buttonsLogin = new HBox();
        buttonsLogin.setStyle("-fx-background-color: #2e6a6f;");
        buttonsLogin.setAlignment(Pos.CENTER);
        buttonsLogin.setSpacing(40);
        buttonsLogin.getChildren().addAll(loginBtn, signupPBtn, cancelLogin);

        loginVBox.getChildren().addAll(loginLabelHBox, userNameHBox, passwordHBox,  buttonsLogin);
        loginVBox.setAlignment(Pos.CENTER);
        loginVBox.setSpacing(20);
        Scene loginScene = new Scene(loginVBox, 570, 235);

        loginStage.initModality(Modality.APPLICATION_MODAL);
        loginStage.initOwner(stage);
        loginStage.centerOnScreen();
        loginStage.initStyle(StageStyle.TRANSPARENT);
        loginStage.setAlwaysOnTop(true);
        loginStage.setScene(loginScene);
        loginStage.setResizable(false);
        adaptor.getMainHBox().setDisable(true);
        loginStage.show();
        loginStage.setOnCloseRequest(event -> {
            webengine.load(null);
        });
        signupStage.initModality(Modality.APPLICATION_MODAL);
        signupStage.initOwner(stage);
        signupStage.centerOnScreen();
        signupStage.initStyle(StageStyle.TRANSPARENT);
        signupStage.setAlwaysOnTop(true);
        //Signup Label
        Label signupLabel = new Label("Signup");
        signupLabel.setTextAlignment(TextAlignment.CENTER);
        signupLabel.setAlignment(Pos.CENTER);
        signupLabel.setTextFill(Color.WHITE);
        signupLabel.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 24));
        signupLabel.setContentDisplay(ContentDisplay.CENTER);
        signupLabel.setPrefWidth(Control.USE_COMPUTED_SIZE);
        signupLabel.setPrefHeight(60);

        HBox signupLabelHBox = new HBox();
        signupLabelHBox.getChildren().add(signupLabel);
        signupLabelHBox.setAlignment(Pos.CENTER);
        signupLabelHBox.setStyle("-fx-background-color: #2e6a6f;");

        //user Name HBox
        Label userNameSLabel = new Label("User Name:");
        userNameSLabel.setTextAlignment(TextAlignment.CENTER);
        userNameSLabel.setAlignment(Pos.CENTER);
        userNameSLabel.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 15));
        userNameSLabel.setContentDisplay(ContentDisplay.CENTER);
        userNameSLabel.setPrefWidth(190);
        userNameSLabel.setPrefHeight(40);

        userNameSField.setPrefSize(270, 40);
        userNameSField.setEditable(true);

        HBox userNameSHBox = new HBox();
        userNameSHBox.getChildren().addAll(userNameSLabel, userNameSField);
        userNameSHBox.setAlignment(Pos.CENTER);
        userNameSHBox.setSpacing(5);

        // password HBox
        Label passwordSLabel = new Label("Password:");
        passwordSLabel.setTextAlignment(TextAlignment.CENTER);
        passwordSLabel.setAlignment(Pos.CENTER);
        passwordSLabel.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 15));
        passwordSLabel.setContentDisplay(ContentDisplay.CENTER);
        passwordSLabel.setPrefWidth(190);
        passwordSLabel.setPrefHeight(40);

        passwordSField.setPrefSize(270, 40);
        passwordSField.setEditable(true);
        passwordSField.setPromptText("min. 8 characters");

        HBox passwordSHBox = new HBox();
        passwordSHBox.getChildren().addAll(passwordSLabel, passwordSField);
        passwordSHBox.setAlignment(Pos.CENTER);
        passwordSHBox.setSpacing(5);

        Label repeatPassLabel = new Label("Repeat Password:");
        repeatPassLabel.setTextAlignment(TextAlignment.CENTER);
        repeatPassLabel.setAlignment(Pos.CENTER);
        repeatPassLabel.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 15));
        repeatPassLabel.setContentDisplay(ContentDisplay.CENTER);
        repeatPassLabel.setPrefWidth(190);
        repeatPassLabel.setPrefHeight(40);

        repeatPassField.setPrefSize(270, 40);
        repeatPassField.setEditable(true);

        HBox repeatPassHBox = new HBox();
        repeatPassHBox.getChildren().addAll(repeatPassLabel, repeatPassField);
        repeatPassHBox.setAlignment(Pos.CENTER);
        repeatPassHBox.setSpacing(5);

        //Buttons
        Button signupSBtn = new Button("Signup");
        signupSBtn.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 20));
        signupSBtn.setTextFill(Color.WHITE);
        signupSBtn.setPrefWidth(150);
        signupSBtn.setPrefHeight(25);
        signupSBtn.setStyle("-fx-background-color: #2b686d55;");

        Button cancelSignup = new Button("Cancel");
        cancelSignup.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 20));
        cancelSignup.setTextFill(Color.WHITE);
        cancelSignup.setPrefWidth(150);
        cancelSignup.setPrefHeight(25);
        cancelSignup.setStyle("-fx-background-color: #2b686d55;");

        HBox buttonsSignup = new HBox();
        buttonsSignup.setStyle("-fx-background-color: #2e6a6f;");
        buttonsSignup.setAlignment(Pos.CENTER);
        buttonsSignup.setSpacing(70);
        buttonsSignup.getChildren().addAll(signupSBtn, cancelSignup);

        signupVBox.getChildren().addAll(signupLabelHBox, userNameSHBox, passwordSHBox, repeatPassHBox, buttonsSignup);
        signupVBox.setAlignment(Pos.CENTER);
        signupVBox.setSpacing(20);
        Scene signupScene = new Scene(signupVBox, 560, 295);

        BooleanBinding bb3 = new BooleanBinding() {
            {
                super.bind(userNameField.textProperty(), passwordField.textProperty());
            }

            @Override
            protected boolean computeValue() {
                return (userNameField.getText().isEmpty() || passwordField.getText().isEmpty() || passwordField.getText().length()<8);
            }
        };
        loginBtn.disableProperty().bind(bb3);

        loginBtn.setOnMouseEntered(event -> {
            loginBtn.setEffect(borderGlow);
        });
        loginBtn.setOnMouseExited(event -> {
            loginBtn.setEffect(null);
        });
        loginBtn.setOnMouseClicked(event -> {
            pgRequest = "coachLogin";
                web.getEngine().load(urlFinal + "/coachLogin/?userName=" + userNameField.getText()+ "&password=" + passwordField.getText());
        });

        cancelLogin.setOnMouseEntered(event -> {
            cancelLogin.setEffect(borderGlow);
        });
        cancelLogin.setOnMouseExited(event -> {
            cancelLogin.setEffect(null);
        });
        cancelLogin.setOnMouseClicked(event -> {
            loginStage.close();
            stage.close();
        });

        signupPBtn.setOnMouseEntered(event -> {
            signupPBtn.setEffect(borderGlow);
        });
        signupPBtn.setOnMouseExited(event -> {
            signupPBtn.setEffect(null);
        });

        signupPBtn.setOnMouseClicked(event -> {
            loginStage.close();
            signupStage = new Stage();
            signupStage.initStyle(StageStyle.TRANSPARENT);
            signupStage.initOwner(stage);
            signupStage.centerOnScreen();
            signupStage.setAlwaysOnTop(true);

            signupStage.setScene(signupScene);
            signupStage.setResizable(false);
            adaptor.getMainHBox().setDisable(true);
            signupStage.show();

            signupSBtn.setOnMouseEntered(event1 -> {
                signupSBtn.setEffect(borderGlow);
            });
            signupSBtn.setOnMouseExited(event1 -> {
                signupSBtn.setEffect(null);
            });
            signupSBtn.setOnMouseClicked(event1 -> {
                if(passwordSField.getText().equals(repeatPassField.getText())){
                    System.out.println("passwords Match!!!");
                    pgRequest = "coachSignup";
                    web.getEngine().load(urlFinal + "/coachSignup/?userName="+userNameSField.getText()+"&password="+passwordSField.getText());
                }
                else{
                    System.out.println("passwrods do not Match!!!");
                    errorText.setText("Passwords do not match");
                    signupVBox.setDisable(true);
                    signupStage.hide();
                    errorMessage.setScene(errorScene);
                    errorMessage.show();
                    errorOkButton.setOnMouseClicked(event2 -> {
                        errorMessage.close();
                        signupVBox.setDisable(false);
                        signupStage.show();
                        repeatPassField.requestFocus();
                        repeatPassField.positionCaret(repeatPassField.getText().length());
                    });
                }

            });

            BooleanBinding bb4 = new BooleanBinding() {
                {
                    super.bind(userNameSField.textProperty(), passwordSField.textProperty(), repeatPassField.textProperty());
                }

                @Override
                protected boolean computeValue() {
                    return (userNameSField.getText().isEmpty() || passwordSField.getText().isEmpty()
                            || repeatPassField.getText().isEmpty() || passwordSField.getText().length()<8 || repeatPassField.getText().length()<8);
                }
            };
            signupSBtn.disableProperty().bind(bb4);

            cancelSignup.setOnMouseEntered(event1 -> {
                cancelSignup.setEffect(borderGlow);
            });
            cancelSignup.setOnMouseExited(event1 -> {
                cancelSignup.setEffect(null);
            });
            cancelSignup.setOnMouseClicked(event1 -> {
                signupStage.close();
                userNameField.setText("");
                passwordField.setText("");
                loginStage.show();
            });

        });

        //removing Task
        VBox removeTVBox = new VBox();
        //Save Label
        Label removeTLabel = new Label("remove this Task?");
        removeTLabel.setTextAlignment(TextAlignment.CENTER);
        removeTLabel.setAlignment(Pos.CENTER);
        removeTLabel.setTextFill(Color.WHITE);
        removeTLabel.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 24));
        removeTLabel.setContentDisplay(ContentDisplay.CENTER);
        removeTLabel.setPrefWidth(Control.USE_COMPUTED_SIZE);
        removeTLabel.setPrefHeight(60);

        HBox removeTLabelHBox = new HBox();
        removeTLabelHBox.getChildren().add(removeTLabel);
        removeTLabelHBox.setAlignment(Pos.CENTER);
        removeTLabelHBox.setStyle("-fx-background-color: #2e6a6f;");

        Label removeTName = new Label(adaptor.getRemoveTName());
        removeTName.setTextAlignment(TextAlignment.CENTER);
        removeTName.setAlignment(Pos.CENTER);
        removeTName.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 18));
        removeTName.setContentDisplay(ContentDisplay.CENTER);
        removeTName.setPrefWidth(400);
        removeTName.setPrefHeight(40);

        HBox removeTNameHBox = new HBox();
        removeTNameHBox.getChildren().addAll(removeTName);
        removeTNameHBox.setAlignment(Pos.CENTER);

        //Buttons
        Button removeTOkButton = new Button("Ok");
        removeTOkButton.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 20));
        removeTOkButton.setTextFill(Color.WHITE);
        removeTOkButton.setPrefWidth(150);
        removeTOkButton.setPrefHeight(25);
        removeTOkButton.setStyle("-fx-background-color: #2b686d55;");

        removeTOkButton.setOnMouseEntered(event4 -> {
            removeTOkButton.setEffect(borderGlow);
        });
        removeTOkButton.setOnMouseExited(event4 -> {
            removeTOkButton.setEffect(null);
        });

        Button cancelTRemove = new Button("Cancel");
        cancelTRemove.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 20));
        cancelTRemove.setTextFill(Color.WHITE);
        cancelTRemove.setPrefWidth(150);
        cancelTRemove.setPrefHeight(25);
        cancelTRemove.setStyle("-fx-background-color: #2b686d55;");
        cancelTRemove.setOnMouseEntered(event8 -> {
            cancelTRemove.setEffect(borderGlow);
        });
        cancelTRemove.setOnMouseExited(event8 -> {
            cancelTRemove.setEffect(null);
        });

        HBox buttonsTRemove = new HBox();
        buttonsTRemove.setStyle("-fx-background-color: #2e6a6f;");
        buttonsTRemove.setAlignment(Pos.CENTER);
        buttonsTRemove.setSpacing(55);
        buttonsTRemove.getChildren().addAll(removeTOkButton, cancelTRemove);

        removeTOkButton.setOnMouseClicked(event8 -> {
            adaptor.getMainHBox().setDisable(false);
            adaptor.setRemoveTaskName(removeTName.getText());
            removeTName.setText("");
            adaptor.getRemoveTbtn().defaultButtonProperty().setValue(true);
            removeTStage.close();
        });
        cancelTRemove.setOnMouseClicked(event8 -> {
            adaptor.getMainHBox().setDisable(false);
            removeTName.setText("");
            removeTStage.close();
        });

        removeTVBox.getChildren().addAll(removeTLabelHBox, removeTNameHBox, buttonsTRemove);
        removeTVBox.setAlignment(Pos.CENTER);
        removeTVBox.setSpacing(30);
        Scene removeTScene = new Scene(removeTVBox, 400, 195);

        adaptor.getRemoveTaskButton().defaultButtonProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    adaptor.getRemoveTaskButton().defaultButtonProperty().setValue(false);
                    removeTStage = new Stage();
                    removeTStage.initModality(Modality.APPLICATION_MODAL);
                    removeTStage.initOwner(stage);
                    removeTStage.centerOnScreen();
                    removeTName.setText(adaptor.getRemoveTName());
                    removeTStage.initStyle(StageStyle.TRANSPARENT);
                    removeTStage.setAlwaysOnTop(true);
                    removeTStage.setScene(removeTScene);
                    removeTStage.setResizable(false);
                    adaptor.getMainHBox().setDisable(true);
                    removeTStage.show();
                    File file = new File("/lib/Notification.mp3");
                    Media media = new Media(file.toURI().toString());
                    MediaPlayer mediaplayer = new MediaPlayer(media);
                    mediaplayer.play();
                }
            }
        });

        //saving Dialogue
        VBox saveMessageVBox = new VBox();
        //Save Label
        Label saveLabel = new Label("Save Changes");
        saveLabel.setTextAlignment(TextAlignment.CENTER);
        saveLabel.setTextFill(Color.WHITE);
        saveLabel.setAlignment(Pos.CENTER);
        saveLabel.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 24));
        saveLabel.setContentDisplay(ContentDisplay.CENTER);
        saveLabel.setPrefWidth(Control.USE_COMPUTED_SIZE);
        saveLabel.setPrefHeight(60);

        HBox saveLabelHBox = new HBox();
        saveLabelHBox.getChildren().add(saveLabel);
        saveLabelHBox.setAlignment(Pos.CENTER);
        saveLabelHBox.setStyle("-fx-background-color: #2e6a6f;");

        //Save Messaage
        Label saveText = new Label("Do you want to save the changes?");
        saveText.setTextAlignment(TextAlignment.CENTER);
        saveText.setAlignment(Pos.CENTER);
        saveText.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 18));
        saveText.setContentDisplay(ContentDisplay.CENTER);
        saveText.setPrefWidth(420);
        saveText.setPrefHeight(40);

        HBox saveTextHBox = new HBox();
        saveTextHBox.getChildren().addAll(saveText);
        saveTextHBox.setAlignment(Pos.CENTER);

        //Buttons
        Button saveOkButton = new Button("Yes");
        saveOkButton.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 20));
        saveOkButton.setTextFill(Color.WHITE);
        saveOkButton.setPrefWidth(150);
        saveOkButton.setPrefHeight(25);
        saveOkButton.setStyle("-fx-background-color: #2b686d55;");

        saveOkButton.setOnMouseEntered(event4 -> {
            saveOkButton.setEffect(borderGlow);
        });
        saveOkButton.setOnMouseExited(event4 -> {
            saveOkButton.setEffect(null);
        });

        Button cancelSave = new Button("No");
        cancelSave.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 20));
        cancelSave.setTextFill(Color.WHITE);
        cancelSave.setPrefWidth(150);
        cancelSave.setPrefHeight(25);
        cancelSave.setStyle("-fx-background-color: #2b686d55;");
        cancelSave.setOnMouseEntered(event8 -> {
            cancelSave.setEffect(borderGlow);
        });
        cancelSave.setOnMouseExited(event8 -> {
            cancelSave.setEffect(null);
        });

        HBox buttonsSave = new HBox();
        buttonsSave.setStyle("-fx-background-color: #2e6a6f;");
        buttonsSave.setAlignment(Pos.CENTER);
        buttonsSave.setSpacing(40);
        buttonsSave.getChildren().addAll(saveOkButton, cancelSave);

        saveOkButton.setOnMouseClicked(event8 -> {
//            try {
                pgRequest = "c";
                if(adaptor.getCoach().patients.size() != 0) {
                    for (int s = 0; s < adaptor.getCoach().patients.size(); s++) {
                        //TODO: UPDATE EACH PATIENT FILE IN POSTGRESQL !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                  	 try {
								 SavePlan.savePlan(adaptor.getCoach().patients.get(s)); 
								 System.out.println(adaptor.getCoach().patients.get(s));
								 String urlParameter1 = "patientName=" + adaptor.getCoach().patients.get(s).getPatientName();
	                    		 String urlParamter2 = "file=" + adaptor.getCoach().patients.get(s).getFile();
	                    		 String param = urlParameter1 + "&" + urlParamter2;	                    		 
	                    		 URL url = new URL(urlFinal + "/setPatientFile/"); 
	                    		 HttpURLConnection con = (HttpURLConnection) url.openConnection();
	                    		 con.setDoOutput(true);
	                    		 con.setDoInput(true);	                    		 
	                    		 con.setRequestMethod("POST");
	                    		 con.setRequestProperty("User-Agent", "Mozilla/5.0");	                    	
	                    		 try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
	                                 wr.writeBytes(param.toString());
	                                 wr.flush();
	                                 wr.close();
	                             }
	                    		 StringBuilder content;

	                             try (BufferedReader in = new BufferedReader(
	                                     new InputStreamReader(con.getInputStream()))) {

	                                 String line;
	                                 content = new StringBuilder();

	                                 while ((line = in.readLine()) != null) {
	                                     content.append(line);
	                                     content.append(System.lineSeparator());
	                                 }
	                                 setFiles = content.toString();
	                                 con.disconnect();
	          
	                                 if(setFiles.contains("?fileSetSuccess?")){
	                                	
	                                 	System.out.println("saved files");
	                                     stage.close();
	                                 }
	                                 else {
	                                     System.out.println("error fel remove!!!");
	                                     errorText.setText("");
	                                     saveAndExitStage.hide();
	                                     errorText.setText("Couldn't save the changes, please try again");
	                                     errorMessage.setScene(errorScene);
	                                     errorMessage.show();
	                                     errorOkButton.setOnMouseClicked(event2 -> {
	                                         errorMessage.close();
	                                         saveAndExitStage.show();
	                                     });
	                                 }
	                             }

	                    		
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                    		
                    }
                }
                else {
                    stage.close();
                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        });
        cancelSave.setOnMouseClicked(event8 -> {
            stage.close();
        });

        saveMessageVBox.getChildren().addAll(saveLabelHBox, saveTextHBox, buttonsSave);
        saveMessageVBox.setAlignment(Pos.CENTER);
        saveMessageVBox.setSpacing(30);
        Scene saveScene = new Scene(saveMessageVBox, 420, 178);

        adaptor.getSaveAndExitButton().setOnMouseClicked(event -> {
            saveAndExitStage = new Stage();
            saveAndExitStage.initModality(Modality.APPLICATION_MODAL);
            saveAndExitStage.initOwner(stage);
            saveAndExitStage.centerOnScreen();
            saveAndExitStage.initStyle(StageStyle.TRANSPARENT);
            saveAndExitStage.setAlwaysOnTop(true);
            saveAndExitStage.setScene(saveScene);
            saveAndExitStage.setResizable(false);
            adaptor.getMainHBox().setDisable(true);
            saveAndExitStage.show();
//            File file = new File("/lib/Notification.mp3");
//            Media media = new Media(file.toURI().toString());
//            MediaPlayer mediaplayer = new MediaPlayer(media);
//            mediaplayer.play();
        });

        //adding Tasks
        VBox addTVBox = new VBox();
        //add task Label
        Label addTLabel = new Label("Add Task");
        addTLabel.setTextAlignment(TextAlignment.CENTER);
        addTLabel.setAlignment(Pos.CENTER);
        addTLabel.setTextFill(Color.WHITE);
        addTLabel.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 24));
        addTLabel.setContentDisplay(ContentDisplay.CENTER);
        addTLabel.setPrefWidth(Control.USE_COMPUTED_SIZE);
        addTLabel.setPrefHeight(60);

        HBox addTLHBox = new HBox();
        addTLHBox.getChildren().add(addTLabel);
        addTLHBox.setAlignment(Pos.CENTER);
        addTLHBox.setStyle("-fx-background-color: #2e6a6f;");

        //task Name
        Label tNameLabel = new Label("Task Title:*");
        tNameLabel.setTextAlignment(TextAlignment.CENTER);
        tNameLabel.setAlignment(Pos.CENTER);
        tNameLabel.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 18));
        tNameLabel.setContentDisplay(ContentDisplay.CENTER);
        tNameLabel.setPrefWidth(150);
        tNameLabel.setPrefHeight(40);

        TextField tNameField = new TextField();
        tNameField.setPrefSize(300, 40);
        tNameField.setEditable(true);

        HBox tNameHBox = new HBox();
        tNameHBox.getChildren().addAll(tNameLabel, tNameField);
        tNameHBox.setAlignment(Pos.CENTER);
        tNameHBox.setSpacing(5);

        //description
        Label d = new Label("Description:");
        d.setTextAlignment(TextAlignment.CENTER);
        d.setAlignment(Pos.CENTER);
        d.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 18));
        d.setContentDisplay(ContentDisplay.CENTER);
        d.setPrefWidth(150);
        d.setPrefHeight(40);

        TextArea da = new TextArea();
        da.setPrefSize(300, 170);
        da.setWrapText(true);
        da.setEditable(true);

        HBox dHBox = new HBox();
        dHBox.setSpacing(5);
        dHBox.getChildren().addAll(d, da);
        dHBox.setAlignment(Pos.TOP_CENTER);

        //Start time
        Label tStartLabel = new Label("Start Time:*");
        tStartLabel.setTextAlignment(TextAlignment.CENTER);
        tStartLabel.setAlignment(Pos.CENTER);
        tStartLabel.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 15));
        tStartLabel.setContentDisplay(ContentDisplay.CENTER);
        tStartLabel.setPrefWidth(118);
        tStartLabel.setPrefHeight(40);

//       
        ComboBox<String> sHour = new ComboBox<String>();
//       
        sHour.setPrefSize(70, 40);

        for(int sTh  = 0; sTh < 24; sTh++){

            if(!(adaptor.getDaySelected().getTimeSlots()[sTh*12] != 0 && adaptor.getDaySelected().getTimeSlots()[sTh*12+1] != 0
            && adaptor.getDaySelected().getTimeSlots()[sTh*12+2] != 0 && adaptor.getDaySelected().getTimeSlots()[sTh*12+3] != 0
            && adaptor.getDaySelected().getTimeSlots()[sTh*12+4] != 0 && adaptor.getDaySelected().getTimeSlots()[sTh*12+5] != 0
            && adaptor.getDaySelected().getTimeSlots()[sTh*12+6] != 0 && adaptor.getDaySelected().getTimeSlots()[sTh*12+7] != 0
            && adaptor.getDaySelected().getTimeSlots()[sTh*12+8] != 0 && adaptor.getDaySelected().getTimeSlots()[sTh*12+9] != 0
            && adaptor.getDaySelected().getTimeSlots()[sTh*12+10] != 0 && adaptor.getDaySelected().getTimeSlots()[sTh*12+11] != 0)){
                sHour.getItems().add((sTh/10)+""+sTh%10);
            }

        }

        Label seperatesLabel = new Label(":");
        seperatesLabel.setTextAlignment(TextAlignment.CENTER);
        seperatesLabel.setAlignment(Pos.CENTER);
        seperatesLabel.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 15));
        seperatesLabel.setContentDisplay(ContentDisplay.CENTER);
        seperatesLabel.setPrefWidth(5);
        seperatesLabel.setPrefHeight(40);

        ComboBox<String> sMin = new ComboBox<String>();
//        sMin.setVisibleRowCount(12);
//        sMin.getItems().add("00");
//        sMin.getItems().add("05");
//        for (int sm = 10; sm < 60; sm+=5){
//            sMin.getItems().add(sm + "");
//        }
        sMin.setPrefSize(70, 40);
        //sMin.setVisibleRowCount(12);
        HBox tStartHBox = new HBox();
        tStartHBox.getChildren().addAll(tStartLabel, sHour, seperatesLabel, sMin);
        tStartHBox.setAlignment(Pos.CENTER);
        tStartHBox.setSpacing(5);

        //End Time
        Label tEndLabel = new Label("End Time:*");
        tEndLabel.setTextAlignment(TextAlignment.CENTER);
        tEndLabel.setAlignment(Pos.CENTER);
        tEndLabel.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 15));
        tEndLabel.setContentDisplay(ContentDisplay.CENTER);
        tEndLabel.setPrefWidth(118);
        tEndLabel.setPrefHeight(40);

        ComboBox<String> eHour = new ComboBox<String>();
        //eHour.setVisibleRowCount(12);
        //eHour.getItems().addAll("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23");

        eHour.setPrefSize(70, 40);
        //eHour.setVisibleRowCount(12);
        Label seperateELabel = new Label(":");
        seperateELabel.setTextAlignment(TextAlignment.CENTER);
        seperateELabel.setAlignment(Pos.CENTER);
        seperateELabel.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 15));
        seperateELabel.setContentDisplay(ContentDisplay.CENTER);
        seperateELabel.setPrefWidth(5);
        seperateELabel.setPrefHeight(40);

        ComboBox<String> eMin = new ComboBox<String>();
        
        Calendar calendar = Calendar.getInstance();
        eMin.setPrefSize(70, 40);
        //eMin.setVisibleRowCount(12);
        HBox tEndHBox = new HBox();
        tEndHBox.getChildren().addAll(tEndLabel, eHour, seperateELabel, eMin);
        tEndHBox.setAlignment(Pos.CENTER);
        tEndHBox.setSpacing(5);

        //Programs
        Label progsLabel = new Label("Programs Needed:");
        progsLabel.setTextAlignment(TextAlignment.CENTER);
        progsLabel.setAlignment(Pos.CENTER);
        progsLabel.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 18));
        progsLabel.setContentDisplay(ContentDisplay.CENTER);
        progsLabel.setPrefWidth(200);
        progsLabel.setPrefHeight(40);

        TextField progNameField = new TextField();
        progNameField.setPrefSize(200, 40);
        progNameField.setEditable(true);

        Button listAdd = new Button("ADD");
        listAdd.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 14));
        listAdd.setPrefWidth(80);
        listAdd.setPrefHeight(40);
        listAdd.setStyle("-fx-background-color: #2b686d55;");

//        TableView progsTabel = new TableView();
//        TableColumn progsCol = new TableColumn("Programs");
//        progsCol.setEditable(true);
//        progsTabel.getItems().add(progsCol);
//        progsTabel.setPrefSize(200, 100);
//        progsTabel.setMaxSize(200, 100);
//        progsTabel.setMinSize(200, 100);

        Label listLabel = new Label();
        listLabel.setPrefWidth(200);
        listLabel.setPrefHeight(40);

        ListView progsList = new ListView();

        progsList.getItems().add("This is another prog");
        progsList.getItems().add("This is a program");
        progsList.setEditable(true);
        progsList.setPrefSize(200, 100);
        progsList.setMaxSize(200, 100);
        progsList.setMinSize(200, 100);

        Button listRemove = new Button("Remove");
        listRemove.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 14));
        listRemove.setPrefWidth(80);
        listRemove.setPrefHeight(40);
        listRemove.setStyle("-fx-background-color: #2b686d55;");

        listAdd.setOnMouseEntered(event8 -> {
            listAdd.setStyle("-fx-background-color: #5A54C455;");
        });
        listAdd.setOnMouseExited(event8 -> {
            listAdd.setStyle("-fx-background-color: #2b686d55;");
        });
        listAdd.setOnMouseClicked(event8 -> {
            progsList.getItems().add(progNameField.getText());
            progNameField.setText("");
        });
        BooleanBinding bb2 = new BooleanBinding() {
            {
                super.bind(progNameField.textProperty());
            }

            @Override
            protected boolean computeValue() {
                return (progNameField.getText().isEmpty());
            }
        };
        listAdd.disableProperty().bind(bb2);

        listRemove.setDisable(true);
        listRemove.setOnMouseEntered(event8 -> {
            listRemove.setStyle("-fx-background-color: #FF000055;");
        });
        listRemove.setOnMouseExited(event8 -> {
            listRemove.setStyle("-fx-background-color: #2b686d55;");
        });
        progsList.setOnMouseClicked(event8 -> {
            listRemove.setDisable(false);
            listRemove.setOnMouseClicked(event -> {
                progsList.getItems().remove(progsList.getSelectionModel().getSelectedItem());
                listRemove.setDisable(true);
            });

        });

        HBox progsHBox = new HBox();
        progsHBox.setSpacing(5);
        progsHBox.getChildren().addAll(progsLabel, progNameField, listAdd);
        progsHBox.setAlignment(Pos.CENTER);

        HBox listHBox = new HBox();
        listHBox.setSpacing(5);
        listHBox.getChildren().addAll(listLabel, progsList, listRemove);
        listHBox.setAlignment(Pos.TOP_CENTER);
        //Buttons
        Button addTBtn = new Button("Add");
        addTBtn.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 20));
        addTBtn.setTextFill(Color.WHITE);
        addTBtn.setPrefWidth(150);
        addTBtn.setPrefHeight(25);
        addTBtn.setStyle("-fx-background-color: #2b686d55;");

        Button cancelTadd = new Button("Cancel");
        cancelTadd.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 20));
        cancelTadd.setTextFill(Color.WHITE);
        cancelTadd.setPrefWidth(150);
        cancelTadd.setPrefHeight(25);
        cancelTadd.setStyle("-fx-background-color: #2b686d55;");

        HBox buttonsTadd = new HBox();
        buttonsTadd.setStyle("-fx-background-color: #2e6a6f;");
        buttonsTadd.setAlignment(Pos.CENTER);
        buttonsTadd.setSpacing(70);
        buttonsTadd.getChildren().addAll(addTBtn, cancelTadd);

        addTVBox.getChildren().addAll(addTLHBox, tNameHBox, dHBox, tStartHBox, tEndHBox, progsHBox, listHBox, buttonsTadd);
        addTVBox.setAlignment(Pos.CENTER);
        addTVBox.setSpacing(10);
        Scene addTScene = new Scene(addTVBox, 525, 550);

        addTBtn.setOnMouseEntered(event7 -> {
            addTBtn.setEffect(borderGlow);
        });
        addTBtn.setOnMouseExited(event7 -> {
            addTBtn.setEffect(null);
        });

        BooleanBinding bb5 = new BooleanBinding() {
        	
            {
            	
                super.bind(sHour.selectionModelProperty(), sMin.selectionModelProperty(),
                        eHour.selectionModelProperty(), eMin.selectionModelProperty(),tNameField.textProperty());
                
            }

            @Override
            protected boolean computeValue() {
            	
                return (
                		tNameField.getText().isEmpty()
                || sHour.getSelectionModel().isEmpty() 
                || sMin.getSelectionModel().isEmpty()
                || eHour.getSelectionModel().isEmpty() 
                || eMin.getSelectionModel().isEmpty()   
                
                );
            }
        };
      //  addTBtn.disableProperty().bind(bb5);
        
        addTBtn.setOnMouseClicked(event -> {
        	
        	if(!(tNameField.getText().isEmpty()
                    || sHour.getSelectionModel().isEmpty() 
                    || sMin.getSelectionModel().isEmpty()
                    || eHour.getSelectionModel().isEmpty() 
                    || eMin.getSelectionModel().isEmpty() )){
            		System.out.println("i am here");
            	
            Boolean uniqueTName = true;
            for (int x = 0; x < adaptor.getDaySelected().getTasks().size(); x++) {
                if (adaptor.getDaySelected().getTasks().get(x).getTaskName().equals(tNameField.getText())) {
                    uniqueTName = false;
                    break;
                }
            }
            
            if(uniqueTName){
                adaptor.setAddTName(tNameField.getText());
                adaptor.setAddTDescription(da.getText());
                adaptor.setAddTSHour(sHour.getSelectionModel().getSelectedItem());
                adaptor.setAddTSMin(sMin.getSelectionModel().getSelectedItem());
                adaptor.setAddTEHour(eHour.getSelectionModel().getSelectedItem());
                adaptor.setAddTEMin(eMin.getSelectionModel().getSelectedItem());
                for(int x = 0 ; x < progsList.getItems().size(); x++){
                    adaptor.getAddTPrograms().add(new Program(progsList.getItems().get(x).toString()));
                }
                System.out.println("addtbtn progslist bef: " + progsList.getItems().size());
                progsList.getItems().clear();
                System.out.println("addtbtn progslist bef: " + progsList.getItems().size());
                addTStage.close();
                adaptor.getMainHBox().setDisable(false);
                adaptor.getAddTbtn().defaultButtonProperty().setValue(true);
            }
            else {
                addTStage.hide();
                errorText.setText("Task name already used,\n Please use another");
                errorMessage.setScene(errorScene);
                errorMessage.show();
                errorOkButton.setOnMouseClicked(event2 -> {
                    errorMessage.close();
                    tNameField.requestFocus();
                    tNameField.positionCaret(tNameField.getText().length());
                    addTStage.show();
                });
            }
        	}
        	else{
        		addTStage.hide();
                errorText.setText("Data Incomplete.");
                errorMessage.setScene(errorScene);
                errorMessage.show();
                errorOkButton.setOnMouseClicked(event2 -> {
                    errorMessage.close();
                    tNameField.requestFocus();
                    tNameField.positionCaret(tNameField.getText().length());
                    addTStage.show();
                });
        	}
        });

        cancelTadd.setOnMouseEntered(event7 -> {
            cancelTadd.setEffect(borderGlow);
        });
        cancelTadd.setOnMouseExited(event7 -> {
            cancelTadd.setEffect(null);
        });
        cancelTadd.setOnMouseClicked(event7 -> {
            tNameField.setText("");
            da.setText("");
            progsList.getItems().clear();
            sHour.getSelectionModel().clearSelection();
            sMin.getSelectionModel().clearSelection();
            eHour.getSelectionModel().clearSelection();
            eMin.getSelectionModel().clearSelection();
            addTStage.close();
            adaptor.getMainHBox().setDisable(false);
        });
        sMin.setDisable(true);
        eHour.setDisable(true);
        eMin.setDisable(true);
        sHour.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue == null){
                    sMin.setDisable(true);
                    eHour.setDisable(true);
                    eMin.setDisable(true);
                }
                else{
                    sMin.getItems().clear();
                    for(int sTm = 0; sTm < 12; sTm++){
                        if (adaptor.getDaySelected().getTimeSlots()[Integer.parseInt(sHour.getValue())*12 + sTm] == 0) {
                            sMin.getItems().add(((sTm*5)/10)+""+(sTm*5)%10);
                        }
                    }
                    sMin.setDisable(false);
                }
            }
        });

        sMin.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue == null){
                    eHour.setDisable(true);
                    eMin.setDisable(true);
                }
                else{
                    eHour.getItems().clear();
                    int start = Integer.parseInt(sHour.getValue())*12 + Integer.parseInt(sMin.getValue())/5;
                    for (int eTh= start; eTh < adaptor.getDaySelected().getTimeSlots().length; eTh++){
                        if (adaptor.getDaySelected().getTimeSlots()[eTh] != 0) {
                            eHour.getItems().add((((eTh/12))/10)+""+((eTh/12))%10);
                            break;
                        }
                        if (eTh%12 == 0 &&  eTh != start) {
                            eHour.getItems().add((((eTh/12)-1)/10)+""+((eTh/12)-1)%10);
                        }
                        if (eTh == adaptor.getDaySelected().getTimeSlots().length-1) {
                            eHour.getItems().add("23");
                        }
                    }
                    eHour.setDisable(false);
                }
            }
        });

        eHour.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue == null){
                    eMin.setDisable(true);
                }
                else {
                    eMin.getItems().clear();
                    //int start = Integer.parseInt(sMin.getValue())/5;
                    for(int eTm = 0; eTm < 12; eTm++){

                        if (((eTm*5) > (Integer.parseInt(sMin.getSelectionModel().getSelectedItem()))
                                && eHour.getSelectionModel().getSelectedItem().equals(sHour.getSelectionModel().getSelectedItem()))
                                || !(eHour.getSelectionModel().getSelectedItem().equals(sHour.getSelectionModel().getSelectedItem())) ) {
                            if (adaptor.getDaySelected().getTimeSlots()[Integer.parseInt(eHour.getValue())*12 + eTm] == 0) {
                                eMin.getItems().add(((eTm * 5) / 10) + "" + (eTm * 5) % 10);
                            }
                            else {
                                break;
                            }
                        }
                    }
                    eMin.setDisable(false);
                }
            }
        });

        adaptor.getEditTaskbtn().defaultButtonProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    addTStage = new Stage();
                    addTStage.initModality(Modality.APPLICATION_MODAL);
                    addTLabel.setText("Edit Task");
                    tNameField.setText(adaptor.getTaskSelectedName());
                    
//                    da.setText(adaptor.getDaySelected().getTaskByName(adaptor.getTaskSelectedName()).getTaskDescription());
//                    sHour.setPromptText(adaptor.getDaySelected().getTaskByName(adaptor.getTaskSelectedName()).getStartTimeH()+"");
//                    sMin.setPromptText(adaptor.getDaySelected().getTaskByName(adaptor.getTaskSelectedName()).getStartTimeM()+"");
//                    eHour.setPromptText(adaptor.getDaySelected().getTaskByName(adaptor.getTaskSelectedName()).getEndTimeH()+"");
//                    eMin.setPromptText(adaptor.getDaySelected().getTaskByName(adaptor.getTaskSelectedName()).getEndTimeM()+"");
                    
//
//                    sHour.getSelectionModel().clearAndSelect(adaptor.getDaySelected().getTaskByName(adaptor.getTaskSelectedName()).getStartTimeH());
//                    sMin.getSelectionModel().clearAndSelect(adaptor.getDaySelected().getTaskByName(adaptor.getTaskSelectedName()).getStartTimeM());
//                    eHour.getSelectionModel().clearAndSelect(adaptor.getDaySelected().getTaskByName(adaptor.getTaskSelectedName()).getEndTimeH());
//                    eMin.getSelectionModel().clearAndSelect(adaptor.getDaySelected().getTaskByName(adaptor.getTaskSelectedName()).getEndTimeM());
                    progsList.getItems().clear();
                    for(int x = 0; x < adaptor.getDaySelected().getTaskByName(adaptor.getTaskSelectedName()).getPrograms().size(); x++){
                        progsList.getItems().add(adaptor.getDaySelected().getTaskByName(adaptor.getTaskSelectedName()).getProgram(x).getBaseName());
                    }
                    adaptor.setRemoveTaskName(adaptor.getDaySelected().getTaskByName(adaptor.getTaskSelectedName()).getTaskName());
                    String oldName = adaptor.getRemoveTaskName();
                    addTBtn.setText("Edit");
                    addTBtn.setOnMouseClicked(event -> {
                    	if(!(tNameField.getText().isEmpty()
                                || sHour.getSelectionModel().isEmpty() 
                                || sMin.getSelectionModel().isEmpty()
                                || eHour.getSelectionModel().isEmpty() 
                                || eMin.getSelectionModel().isEmpty() )){
                        		System.out.println("i am here");
                        	
                        Boolean uniqueTName = true;
                        for (int x = 0; x < adaptor.getDaySelected().getTasks().size(); x++) {
                            if (adaptor.getDaySelected().getTasks().get(x).getTaskName().equals(tNameField.getText())) {
                                uniqueTName = false;
                                break;
                            }
                        }
                        
                        if(uniqueTName || tNameField.getText().equals(oldName)){
                            adaptor.setAddTName(tNameField.getText());
                            adaptor.setAddTDescription(da.getText());
                            adaptor.setAddTSHour(sHour.getSelectionModel().getSelectedItem());
                            adaptor.setAddTSMin(sMin.getSelectionModel().getSelectedItem());
                            adaptor.setAddTEHour(eHour.getSelectionModel().getSelectedItem());
                            adaptor.setAddTEMin(eMin.getSelectionModel().getSelectedItem());
                            for(int x = 0 ; x < progsList.getItems().size(); x++){
                                adaptor.getAddTPrograms().add(new Program(progsList.getItems().get(x).toString()));
                            }
                            System.out.println("addtaskbutton progslist bef: " + progsList.getItems().size());
                            progsList.getItems().clear();
                            System.out.println("addtaskbutton progslist aft: " + progsList.getItems().size());
                            addTStage.close();
                            adaptor.getMainHBox().setDisable(false);
                            adaptor.getEditTaskbtn().defaultButtonProperty().setValue(false);
                            adaptor.getEditTbtn().defaultButtonProperty().setValue(true);
                        }
                        else {
                            addTStage.hide();
                            errorText.setText("Task name already used,\n Please use another");
                            errorMessage.setScene(errorScene);
                            errorMessage.show();
                            errorOkButton.setOnMouseClicked(event2 -> {
                                errorMessage.close();
                                tNameField.requestFocus();
                                tNameField.positionCaret(tNameField.getText().length());
                                addTStage.show();
                            });
                        }
                    	}
                    	else{
                    		addTStage.hide();
                            errorText.setText("Data Incomplete.");
                            errorMessage.setScene(errorScene);
                            errorMessage.show();
                            errorOkButton.setOnMouseClicked(event2 -> {
                                errorMessage.close();
                                tNameField.requestFocus();
                                tNameField.positionCaret(tNameField.getText().length());
                                addTStage.show();
                            });
                    	}
                             
                    	
                    });
                    cancelTadd.setOnMouseClicked(event -> {
                        tNameField.setText("");
                        da.setText("");
                        progsList.getItems().clear();
                        sHour.getSelectionModel().clearSelection();
                        sMin.getSelectionModel().clearSelection();
                        eHour.getSelectionModel().clearSelection();
                        eMin.getSelectionModel().clearSelection();
                        addTStage.close();
                        adaptor.getMainHBox().setDisable(false);
                        adaptor.getEditTaskbtn().defaultButtonProperty().setValue(false);
                    });
                    addTStage.initOwner(stage);

                    addTStage.centerOnScreen();
                    addTStage.initStyle(StageStyle.TRANSPARENT);
                    addTStage.setAlwaysOnTop(true);
                    addTStage.setScene(addTScene);
                    addTStage.setResizable(false);
                    adaptor.getMainHBox().setDisable(true);
                    addTStage.show();
                }
            }
        });
        adaptor.getAddTaskButton().setOnMouseClicked(event6 -> {
            addTStage = new Stage();
            addTLabel.setText("Add Task");
            tNameField.setText("");
            da.setText("");
            sHour.setPromptText("");
            sHour.getSelectionModel().select(null);
            sMin.setPromptText("");
            sMin.getSelectionModel().select(null);
            eHour.setPromptText("");
            eHour.getSelectionModel().select(null);
            eMin.setPromptText("");
            eMin.getSelectionModel().select(null);
            progsList.getItems().clear();
            addTBtn.setText("Add");
            addTBtn.setOnMouseClicked(event -> {
            	if(!(tNameField.getText().isEmpty()
                        || sHour.getSelectionModel().isEmpty() 
                        || sMin.getSelectionModel().isEmpty()
                        || eHour.getSelectionModel().isEmpty() 
                        || eMin.getSelectionModel().isEmpty() )
                        ){
                		System.out.println("i am here");
                	
                Boolean uniqueTName = true;
                for (int x = 0; x < adaptor.getDaySelected().getTasks().size(); x++) {
                    if (adaptor.getDaySelected().getTasks().get(x).getTaskName().equals(tNameField.getText())) {
                        uniqueTName = false;
                        break;
                    }
                }
                
                if(uniqueTName){
                    adaptor.setAddTName(tNameField.getText());
                    adaptor.setAddTDescription(da.getText());
                    adaptor.setAddTSHour(sHour.getSelectionModel().getSelectedItem());
                    adaptor.setAddTSMin(sMin.getSelectionModel().getSelectedItem());
                    adaptor.setAddTEHour(eHour.getSelectionModel().getSelectedItem());
                    adaptor.setAddTEMin(eMin.getSelectionModel().getSelectedItem());
                    for(int x = 0 ; x < progsList.getItems().size(); x++){
                        adaptor.getAddTPrograms().add(new Program(progsList.getItems().get(x).toString()));
                    }
                    System.out.println("addtaskbutton progslist bef: " + progsList.getItems().size());
                    progsList.getItems().clear();
                    System.out.println("addtaskbutton progslist aft: " + progsList.getItems().size());
                    addTStage.close();
                    adaptor.getMainHBox().setDisable(false);
                    adaptor.getAddTbtn().defaultButtonProperty().setValue(true);
                }
                else {
                    addTStage.hide();
                    errorText.setText("Task name already used,\n Please use another");
                    errorMessage.setScene(errorScene);
                    errorMessage.show();
                    errorOkButton.setOnMouseClicked(event2 -> {
                        errorMessage.close();
                        tNameField.requestFocus();
                        tNameField.positionCaret(tNameField.getText().length());
                        addTStage.show();
                    });
                }
            	}
            	else{
            		addTStage.hide();
                    errorText.setText("Data Incomplete.");
                    errorMessage.setScene(errorScene);
                    errorMessage.show();
                    errorOkButton.setOnMouseClicked(event2 -> {
                        errorMessage.close();
                        tNameField.requestFocus();
                        tNameField.positionCaret(tNameField.getText().length());
                        addTStage.show();
                    });
            	}
            });
            addTStage.initModality(Modality.APPLICATION_MODAL);
            addTStage.initOwner(stage);
            addTStage.centerOnScreen();
            addTStage.initStyle(StageStyle.TRANSPARENT);
            addTStage.setAlwaysOnTop(true);
            addTStage.setScene(addTScene);
            addTStage.setResizable(false);
            adaptor.getMainHBox().setDisable(true);
            addTStage.show();
        });

        System.out.println(sHour.getValue() + "<<<<<<<<<<<< TimeSlots");
        sHour.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                System.out.println(sHour.getValue() + "<<<<<<<<<<<< TimeSlots");
            }
        });

        //overall performance dialoge
        adaptor.getOverPerfButton().setOnMouseClicked(event5 -> {
            VBox overPerfVBox = new VBox();
            //Label
            Label overPerfLabel = new Label("Overall Performance");
            overPerfLabel.setTextAlignment(TextAlignment.CENTER);
            overPerfLabel.setAlignment(Pos.CENTER);
            overPerfLabel.setTextFill(Color.WHITE);
            overPerfLabel.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 24));
            overPerfLabel.setContentDisplay(ContentDisplay.CENTER);
            overPerfLabel.setPrefWidth(Control.USE_COMPUTED_SIZE);
            overPerfLabel.setPrefHeight(60);

            HBox overPerfHBox = new HBox();
            overPerfHBox.getChildren().add(overPerfLabel);
            overPerfHBox.setAlignment(Pos.CENTER);
            overPerfHBox.setStyle("-fx-background-color: #2e6a6f;");
            //Chart
            final NumberAxis yAxis = new NumberAxis();
            final CategoryAxis xAxis = new CategoryAxis();
            xAxis.setLabel("Days");
            yAxis.setLowerBound(0);
            yAxis.setUpperBound(100);
            yAxis.setTickUnit(1);
            yAxis.setLabel("Completed Percentage");
            final LineChart<String,Number> lineChart =
                    new LineChart<String,Number>(xAxis,yAxis);

            lineChart.setTitle(adaptor.getPatientSelected().getPatientName());

            for(int f = 0; f < adaptor.getPatientSelected().plans.size(); f++) {
                XYChart.Series series = new XYChart.Series();
                series.setName(adaptor.getPatientSelected().plans.get(f).getWeekPlanName());
                for (int v = 0; v < 7; v++) {
                    series.getData().add(new XYChart.Data(adaptor.getPatientSelected().plans.get(f).days.get(v).getDayName(), adaptor.getPatientSelected().plans.get(f).days.get(v).getDayProgress()*100));
                }
                lineChart.getData().add(series);
            }

            //Buttons
            Button overPerfCloseBtn = new Button("Close");
            overPerfCloseBtn.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 20));
            overPerfCloseBtn.setTextFill(Color.WHITE);
            overPerfCloseBtn.setPrefWidth(150);
            overPerfCloseBtn.setPrefHeight(25);
            overPerfCloseBtn.setStyle("-fx-background-color: #2b686d55;");

            HBox buttonsOPClose = new HBox();
            buttonsOPClose.setStyle("-fx-background-color: #2e6a6f;");
            buttonsOPClose.setAlignment(Pos.CENTER);
            buttonsOPClose.setSpacing(70);
            buttonsOPClose.getChildren().addAll(overPerfCloseBtn);

            overPerfCloseBtn.setOnMouseEntered(event -> {
                overPerfCloseBtn.setEffect(borderGlow);
            });
            overPerfCloseBtn.setOnMouseExited(event -> {
                overPerfCloseBtn.setEffect(null);
            });

            overPerfCloseBtn.setOnMouseClicked(event -> {
                overPerfStage.close();
                adaptor.getMainHBox().setDisable(false);
            });

            overPerfVBox.getChildren().addAll(overPerfHBox, lineChart, buttonsOPClose);
            overPerfVBox.setAlignment(Pos.CENTER);
            overPerfVBox.setSpacing(20);
            Scene overPerfScene = new Scene(overPerfVBox, 800, 500);

            overPerfStage = new Stage();
            overPerfStage.initModality(Modality.APPLICATION_MODAL);
            overPerfStage.initOwner(stage);
            overPerfStage.centerOnScreen();
            overPerfStage.initStyle(StageStyle.TRANSPARENT);
            overPerfStage.setAlwaysOnTop(true);
            overPerfStage.setScene(overPerfScene);
            overPerfStage.setResizable(false);
            adaptor.getMainHBox().setDisable(true);
            overPerfStage.show();
        });

        //upon adding Patient
        VBox addPVBox = new VBox();
        //AddPLabel
        Label addPLabel = new Label("Add Patient");
        addPLabel.setTextAlignment(TextAlignment.CENTER);
        addPLabel.setAlignment(Pos.CENTER);
        addPLabel.setTextFill(Color.WHITE);
        addPLabel.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 24));
        addPLabel.setContentDisplay(ContentDisplay.CENTER);
        addPLabel.setPrefWidth(Control.USE_COMPUTED_SIZE);
        addPLabel.setPrefHeight(60);

        HBox addPLabelHBox = new HBox();
        addPLabelHBox.getChildren().add(addPLabel);
        addPLabelHBox.setAlignment(Pos.CENTER);
        addPLabelHBox.setStyle("-fx-background-color: #2e6a6f;");


        //P name HBox
        Label pNameLabel = new Label("Select Patient:");
        pNameLabel.setTextAlignment(TextAlignment.CENTER);
        pNameLabel.setAlignment(Pos.CENTER);
        pNameLabel.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 15));
        pNameLabel.setContentDisplay(ContentDisplay.CENTER);
        pNameLabel.setPrefWidth(150);
        pNameLabel.setPrefHeight(40);

        ListView pNameList = new ListView();
        pNameList.setEditable(true);
        pNameList.setPrefSize(200, 100);
        pNameList.setMaxSize(200, 100);
        pNameList.setMinSize(200, 100);

        HBox pNameHBox = new HBox();
        pNameHBox.getChildren().addAll(pNameLabel, pNameList);
        pNameHBox.setAlignment(Pos.CENTER);
        pNameHBox.setSpacing(20);

        //Buttons
        Button addPBtn = new Button("Add");
        addPBtn.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 20));
        addPBtn.setTextFill(Color.WHITE);
        addPBtn.setPrefWidth(150);
        addPBtn.setPrefHeight(25);
        addPBtn.setStyle("-fx-background-color: #2b686d55;");

        Button cancelPadd = new Button("Cancel");
        cancelPadd.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 20));
        cancelPadd.setTextFill(Color.WHITE);
        cancelPadd.setPrefWidth(150);
        cancelPadd.setPrefHeight(25);
        cancelPadd.setStyle("-fx-background-color: #2b686d55;");

        HBox buttonsPadd = new HBox();
        buttonsPadd.setStyle("-fx-background-color: #2e6a6f;");
        buttonsPadd.setAlignment(Pos.CENTER);
        buttonsPadd.setSpacing(70);
        buttonsPadd.getChildren().addAll(addPBtn, cancelPadd);

        //tip
        Label addPTip = new Label("*If patient name not in the list above, please ask your patient to signup first then try again.");
        addPTip.setTextAlignment(TextAlignment.CENTER);
        addPTip.setAlignment(Pos.CENTER);
        addPTip.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 14));
        addPTip.setContentDisplay(ContentDisplay.CENTER);
        addPTip.setWrapText(true);
        addPTip.setPrefWidth(400);
        addPTip.setPrefHeight(60);

        HBox addPTipHBox = new HBox();
        addPTipHBox.getChildren().addAll(addPTip);
        addPTipHBox.setAlignment(Pos.CENTER);
        addPTipHBox.setSpacing(20);

        addPVBox.getChildren().addAll(addPLabelHBox, pNameHBox,addPTipHBox, buttonsPadd);
        addPVBox.setAlignment(Pos.CENTER);
        addPVBox.setSpacing(20);
        Scene addPScene = new Scene(addPVBox, 470, 295);

        //addPBtn.setDisable(true);

        addPBtn.setOnMouseClicked(event -> {
            if(!pNameList.getSelectionModel().isEmpty()){
                webengine.load(urlFinal +"/addPatient/?coachName="
                        + adaptor.getCoach().getCoachName() + "&patientName="
                        + pNameList.getSelectionModel().getSelectedItem().toString());
                pgRequest = "addPatient";
            }
            else {
                addPStage.hide();
                removePStage.close();
                errorText.setText("Please select a Patient first");
                errorMessage.setScene(errorScene);
                errorMessage.show();
                errorOkButton.setOnMouseClicked(event2 -> {
                    errorMessage.close();
                    addPStage.show();
                });
            }
        });
        addPBtn.setOnMouseEntered(event -> {
            addPBtn.setEffect(borderGlow);
        });
        addPBtn.setOnMouseExited(event -> {
            addPBtn.setEffect(null);
        });

        cancelPadd.setOnMouseClicked(event -> {
            webengine.load(null);
            adaptor.getMainHBox().setDisable(false);
            addPStage.close();
        });
        cancelPadd.setOnMouseEntered(event -> {
            cancelPadd.setEffect(borderGlow);
        });
        cancelPadd.setOnMouseExited(event -> {
            cancelPadd.setEffect(null);
        });


//        BooleanBinding bb4 = new BooleanBinding() {
//            {
//                super.bind(pNameList.selectionModelProperty());
//            }
//
//            @Override
//            protected boolean computeValue() {
//                return (!pNameList.selectionModelProperty().equals(null));
//            }
//        };
//        addPBtn.disableProperty().bind(bb4);

        adaptor.getAddPatientButton().setOnMouseClicked(event -> {
            addPStage = new Stage();
            addPStage.initModality(Modality.APPLICATION_MODAL);
            addPStage.initOwner(stage);
            addPStage.centerOnScreen();
            addPStage.initStyle(StageStyle.TRANSPARENT);
            addPStage.setAlwaysOnTop(true);
            addPStage.setScene(addPScene);
            addPStage.setResizable(false);
            adaptor.getToggleDays().selectToggle(null);
            adaptor.getTogglePatients().selectToggle(null);
            adaptor.getMainHBox().setDisable(true);
            pgRequest = "getAvailablePatients";
            webengine.load(urlFinal + "/getAvailablePatients/");
            addPStage.show();
        });

        //Removing WeekPlan
        VBox removeWVBox = new VBox();
        //Remove Title
        Label removeWLabel = new Label("Remove WeekPlan");
        removeWLabel.setTextAlignment(TextAlignment.CENTER);
        removeWLabel.setAlignment(Pos.CENTER);
        removeWLabel.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 24));
        removeWLabel.setContentDisplay(ContentDisplay.CENTER);
        removeWLabel.setPrefWidth(Control.USE_COMPUTED_SIZE);
        removeWLabel.setPrefHeight(60);

        HBox removeWLabelHBox = new HBox();
        removeWLabelHBox.getChildren().add(removeWLabel);
        removeWLabelHBox.setAlignment(Pos.CENTER);
        removeWLabelHBox.setStyle("-fx-background-color: #2e6a6f;");

        //P name HBox
        Label wRNameLabel = new Label("");
        wRNameLabel.setTextAlignment(TextAlignment.CENTER);
        wRNameLabel.setAlignment(Pos.CENTER);
        wRNameLabel.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 18));
        wRNameLabel.setContentDisplay(ContentDisplay.CENTER);
        wRNameLabel.setPrefWidth(420);
        wRNameLabel.setPrefHeight(40);

        HBox wRNameHBox = new HBox();
        wRNameHBox.getChildren().addAll(wRNameLabel);
        wRNameHBox.setAlignment(Pos.CENTER);

        //Buttons
        Button removeWBtn = new Button("Remove");
        removeWBtn.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 20));
        removeWBtn.setPrefWidth(150);
        removeWBtn.setPrefHeight(25);
        removeWBtn.setStyle("-fx-background-color: #2b686d55;");

        Button cancelWremove = new Button("Cancel");
        cancelWremove.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 20));
        cancelWremove.setPrefWidth(150);
        cancelWremove.setPrefHeight(25);
        cancelWremove.setStyle("-fx-background-color: #2b686d55;");

        HBox buttonsWremove = new HBox();
        buttonsWremove.setStyle("-fx-background-color: #2e6a6f;");
        buttonsWremove.setAlignment(Pos.CENTER);
        buttonsWremove.setSpacing(70);
        buttonsWremove.getChildren().addAll(removeWBtn, cancelWremove);

        removeWVBox.getChildren().addAll(removeWLabelHBox, wRNameHBox, buttonsWremove);
        removeWVBox.setAlignment(Pos.CENTER);
        removeWVBox.setSpacing(30);
        Scene removeWScene = new Scene(removeWVBox, 420, 178);

        removeWBtn.setOnMouseClicked(event -> {
            adaptor.setRemoveWplanName(wRNameLabel.getText());
            adaptor.getMainHBox().setDisable(false);
            removeWStage.close();
            adaptor.getRemoveWbtn().defaultButtonProperty().setValue(true);
        });
        removeWBtn.setOnMouseEntered(event -> {
            removeWBtn.setEffect(borderGlow);
        });
        removeWBtn.setOnMouseExited(event -> {
            removeWBtn.setEffect(null);
        });

        cancelWremove.setOnMouseClicked(event -> {
            adaptor.getMainHBox().setDisable(false);
            removeWStage.close();
        });
        cancelWremove.setOnMouseEntered(event -> {
            cancelWremove.setEffect(borderGlow);
        });
        cancelWremove.setOnMouseExited(event -> {
            cancelWremove.setEffect(null);
        });

        adaptor.getRemoveWeekPlanButton().setOnMouseClicked(event -> {
            removeWStage = new Stage();
            removeWStage.initModality(Modality.APPLICATION_MODAL);
            removeWStage.initOwner(stage);
            removeWStage.centerOnScreen();
            wRNameLabel.setText(adaptor.getWeekPlanSelected().getWeekPlanName());
            removeWStage.initStyle(StageStyle.TRANSPARENT);
            removeWStage.setAlwaysOnTop(true);
            removeWStage.setScene(removeWScene);
            removeWStage.setResizable(false);
            adaptor.getToggleDays().selectToggle(null);
            adaptor.getMainHBox().setDisable(true);
            removeWStage.show();
            File file = new File("Notification.mp3");
            Media media = new Media(file.toURI().toString());
            MediaPlayer mediaplayer = new MediaPlayer(media);
            mediaplayer.play();
        });

        //Removing Patient
        VBox removePVBox = new VBox();
        //Remove Title
        Label removePLabel = new Label("Remove Patient");
        removePLabel.setTextAlignment(TextAlignment.CENTER);
        removePLabel.setAlignment(Pos.CENTER);
        removePLabel.setTextFill(Color.WHITE);
        removePLabel.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 24));
        removePLabel.setContentDisplay(ContentDisplay.CENTER);
        removePLabel.setPrefWidth(Control.USE_COMPUTED_SIZE);
        removePLabel.setPrefHeight(60);

        HBox removePLabelHBox = new HBox();
        removePLabelHBox.getChildren().add(removePLabel);
        removePLabelHBox.setAlignment(Pos.CENTER);
        removePLabelHBox.setStyle("-fx-background-color: #2e6a6f;");

        //P name HBox
        Label pRNameLabel = new Label("");
        pRNameLabel.setTextAlignment(TextAlignment.CENTER);
        pRNameLabel.setAlignment(Pos.CENTER);
        pRNameLabel.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 18));
        pRNameLabel.setContentDisplay(ContentDisplay.CENTER);
        pRNameLabel.setPrefWidth(420);
        pRNameLabel.setPrefHeight(40);

        HBox pRNameHBox = new HBox();
        pRNameHBox.getChildren().addAll(pRNameLabel);
        pRNameHBox.setAlignment(Pos.CENTER);

        //Buttons
        Button removePBtn = new Button("Remove");
        removePBtn.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 20));
        removePBtn.setTextFill(Color.WHITE);
        removePBtn.setPrefWidth(150);
        removePBtn.setPrefHeight(25);
        removePBtn.setStyle("-fx-background-color: #2b686d55;");

        Button cancelPremove = new Button("Cancel");
        cancelPremove.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 20));
        cancelPremove.setTextFill(Color.WHITE);
        cancelPremove.setPrefWidth(150);
        cancelPremove.setPrefHeight(25);
        cancelPremove.setStyle("-fx-background-color: #2b686d55;");

        HBox buttonsPremove = new HBox();
        buttonsPremove.setStyle("-fx-background-color: #2e6a6f;");
        buttonsPremove.setAlignment(Pos.CENTER);
        buttonsPremove.setSpacing(70);
        buttonsPremove.getChildren().addAll(removePBtn, cancelPremove);
        removePVBox.getChildren().addAll(removePLabelHBox, pRNameHBox, buttonsPremove);
        removePVBox.setAlignment(Pos.CENTER);
        removePVBox.setSpacing(30);
        Scene removePScene = new Scene(removePVBox, 420, 178);

        removePBtn.setOnMouseClicked(event -> {
            pgRequest = "removePatient";
            System.out.println("in remove button");
            webengine.load(urlFinal + "/removePatient/?coachName=" + adaptor.getCoach().getCoachName()
                    + "&patientName=" + pRNameLabel.getText());
        });
        removePBtn.setOnMouseEntered(event -> {
            removePBtn.setEffect(borderGlow);
        });
        removePBtn.setOnMouseExited(event -> {
            removePBtn.setEffect(null);
        });

        cancelPremove.setOnMouseClicked(event -> {
            adaptor.getMainHBox().setDisable(false);
            removePStage.close();
        });
        cancelPremove.setOnMouseEntered(event -> {
            cancelPremove.setEffect(borderGlow);
        });
        cancelPremove.setOnMouseExited(event -> {
            cancelPremove.setEffect(null);
        });

        adaptor.getRemovePatientButton().setOnMouseClicked(event -> {
            removePStage = new Stage();
            removePStage.initModality(Modality.APPLICATION_MODAL);
            removePStage.initOwner(stage);
            removePStage.centerOnScreen();
            pRNameLabel.setText(adaptor.getPatientSelected().getPatientName());
            removePStage.initStyle(StageStyle.TRANSPARENT);
            removePStage.setAlwaysOnTop(true);
            removePStage.setScene(removePScene);
            removePStage.setResizable(false);
            adaptor.getToggleDays().selectToggle(null);
            adaptor.getTogglePatients().selectToggle(null);
            adaptor.getMainHBox().setDisable(true);
            removePStage.show();
            File file = new File("Notification.mp3");
            Media media = new Media(file.toURI().toString());
            MediaPlayer mediaplayer = new MediaPlayer(media);
            mediaplayer.play();
        });

        //New WeekPlan
        VBox newWeekPlanVBox = new VBox();
        //New WeekPlan title
        Label newWeekPlanLabel = new Label("New WeekPlan");
        newWeekPlanLabel.setTextAlignment(TextAlignment.CENTER);
        newWeekPlanLabel.setAlignment(Pos.CENTER);
        newWeekPlanLabel.setTextFill(Color.WHITE);
        newWeekPlanLabel.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 24));
        newWeekPlanLabel.setContentDisplay(ContentDisplay.CENTER);
        newWeekPlanLabel.setPrefWidth(Control.USE_COMPUTED_SIZE);
        newWeekPlanLabel.setPrefHeight(60);

        HBox newWPLHBox = new HBox();
        newWPLHBox.getChildren().add(newWeekPlanLabel);
        newWPLHBox.setAlignment(Pos.CENTER);
        newWPLHBox.setStyle("-fx-background-color: #2e6a6f;");

        //WeekPlan name
        Label wPlanNameLabel = new Label("WeekPlan Name:");
        wPlanNameLabel.setTextAlignment(TextAlignment.CENTER);
        wPlanNameLabel.setAlignment(Pos.CENTER);
        wPlanNameLabel.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 15));
        wPlanNameLabel.setContentDisplay(ContentDisplay.CENTER);
        wPlanNameLabel.setPrefWidth(160);
        wPlanNameLabel.setPrefHeight(40);

        TextField wPNameField = new TextField();
        wPNameField.setPrefSize(230, 40);
        wPNameField.setEditable(true);

        HBox wPNHBox = new HBox();
        wPNHBox.getChildren().addAll(wPlanNameLabel, wPNameField);
        wPNHBox.setAlignment(Pos.CENTER);
        wPNHBox.setSpacing(5);

        int today = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        LocalDate local = LocalDate.now();
        //selecting Start Date
        Label wPlanStartLabel = new Label("Start Date:");
        wPlanStartLabel.setTextAlignment(TextAlignment.CENTER);
        wPlanStartLabel.setAlignment(Pos.CENTER);
        wPlanStartLabel.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 15));
        wPlanStartLabel.setContentDisplay(ContentDisplay.CENTER);
        wPlanStartLabel.setPrefWidth(160);
        wPlanStartLabel.setPrefHeight(40);

        adaptor.setStartDatePicker(new DatePicker());
        DatePicker datePicker = new DatePicker();
        datePicker.setPrefSize(230, 40);
        datePicker.setShowWeekNumbers(true);
        datePicker.setPromptText("Only Mondays are Valid");

        StringConverter<LocalDate> defaultConverter = datePicker.getConverter();

        datePicker.setConverter(new StringConverter<LocalDate>() {

            @Override
            public String toString(LocalDate object) {
                return defaultConverter.toString(object);
            }

            @Override
            public LocalDate fromString(String string) {
                LocalDate date = defaultConverter.fromString(string);
                if (date.getDayOfWeek() == DayOfWeek.MONDAY) {
                    return date ;
                } else {
                    // not a Monday. Revert to previous value.
                    // You could also, e.g., return the closest Monday here.
                    return adaptor.getStartDatePicker().getValue();
                }
            }

        });

        datePicker.setDayCellFactory(dp -> new DateCell() {
        @Override
        public void updateItem(LocalDate item, boolean empty) {
            super.updateItem(item, empty);
            if(adaptor.getPatientSelected().plans.size() == 0){
                if(item.getDayOfWeek() != DayOfWeek.MONDAY
                        || ((today == item.getDayOfMonth())&& month == item.getMonthValue() && year == item.getYear())
                        || item.isBefore(local)){
                    setDisable(true);
                }
            } else {
                for (int h = 0; h < adaptor.getPatientSelected().plans.size(); h++) {
                    String[] sdays = adaptor
                    		.getPatientSelected()
                    		.plans.get(h)
                    		.getWeekPlanSDate()
                    		.split("/");
                    if (((sdays[0].equals(item.getDayOfMonth() + "")) && (sdays[1].equals(item.getMonthValue() + "")) && (sdays[2].equals(item.getYear() + "")))
                            || item.getDayOfWeek() != DayOfWeek.MONDAY
                            || ((today == item.getDayOfMonth()) && month == item.getMonthValue() && year == item.getYear())
                            || item.isBefore(local)) {
                        System.out.println(item + "<<<<<<<");
                        //System.out.println(adaptor.getPatientSelected().plans.get(h).getWeekPlanSDate() + ">>>>>>>>>>>");
                        setDisable(true);
                    }
                }
            }
            //setDisable(empty || item.getDayOfWeek() != DayOfWeek.MONDAY);
        }
    });
        //adaptor.setStartDatePicker(startDate);

        HBox wPSHBox = new HBox();
        wPSHBox.getChildren().addAll(wPlanStartLabel, datePicker);
        wPSHBox.setAlignment(Pos.CENTER);
        wPSHBox.setSpacing(5);

        //Buttons
        Button newWPBtn = new Button("Add");
        newWPBtn.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 20));
        newWPBtn.setTextFill(Color.WHITE);
        newWPBtn.setPrefWidth(150);
        newWPBtn.setPrefHeight(25);
        newWPBtn.setStyle("-fx-background-color: #2b686d55;");

        Button cancelNewWPBtn = new Button("Cancel");
        cancelNewWPBtn.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 20));
        cancelNewWPBtn.setTextFill(Color.WHITE);
        cancelNewWPBtn.setPrefWidth(150);
        cancelNewWPBtn.setPrefHeight(25);
        cancelNewWPBtn.setStyle("-fx-background-color: #2b686d55;");

        HBox btnsNewWP = new HBox();
        btnsNewWP.setStyle("-fx-background-color: #2e6a6f;");
        btnsNewWP.setAlignment(Pos.CENTER);
        btnsNewWP.setSpacing(70);
        btnsNewWP.getChildren().addAll(newWPBtn, cancelNewWPBtn);

        newWeekPlanVBox.getChildren().addAll(newWPLHBox, wPNHBox, wPSHBox, btnsNewWP);
        newWeekPlanVBox.setAlignment(Pos.CENTER);
        newWeekPlanVBox.setSpacing(30);
        Scene newWeekScene = new Scene(newWeekPlanVBox, 420, 250);

        newWPBtn.setOnMouseEntered(event3 -> {
            newWPBtn.setEffect(borderGlow);
        });
        newWPBtn.setOnMouseExited(event3 -> {
            newWPBtn.setEffect(null);
        });

        newWPBtn.setOnMouseClicked(event2 -> {
//            if(startDate.getValue().getDayOfWeek().getValue() != 1){
//                System.out.println(startDate.getValue() + "  selected Date Value<<<");
//                errorMessage = new Stage();
//                errorMessage.initModality(Modality.APPLICATION_MODAL);
//                errorMessage.initOwner(stage);
//                errorMessage.centerOnScreen();
//                errorText.setText("Only Mondays are valid");
//                errorMessage.initStyle(StageStyle.TRANSPARENT);
//                errorMessage.setAlwaysOnTop(true);
//                errorMessage.setScene(errorScene);
//                errorMessage.setResizable(false);
//                errorMessage.show();
//                File file = new File("Notification.mp3");
//                Media media = new Media(file.toURI().toString());
//                MediaPlayer mediaplayer = new MediaPlayer(media);
//                mediaplayer.play();
//                newWStage.close();
//                errorOkButton.setOnMouseClicked(event -> {
//                    newWStage.show();
//                    errorMessage.close();
//                });
//            }
//            else{
            boolean uniqueWPName = true;
            for(int x = 0; x < adaptor.getPatientSelected().getPlans().size(); x++){
                if(adaptor.getPatientSelected().getPlans().get(x).getWeekPlanName().equals(wPNameField.getText())){
                    uniqueWPName = false;
                    break;
                }
            }
            if(uniqueWPName) {
                adaptor.setNewWPName(wPNameField.getText());
                adaptor.setSelectedSDate(datePicker.getValue());
                adaptor.setsDaySelected(datePicker.getValue().getDayOfMonth());
                adaptor.setsMonthSelected(datePicker.getValue().getMonthValue());
                adaptor.setsYearSelected(datePicker.getValue().getYear());
                adaptor.getMainHBox().setDisable(false);
                adaptor.getNewWbtn().defaultButtonProperty().setValue(true);
                wPNameField.setText("");

                // adaptor.setStartDatePicker(startDate);

                datePicker.setValue(null);
                newWStage.close();
            }
            else{
                newWStage.hide();
                errorText.setText("This WeekPlan name is already used,\n please change it");
                errorMessage.setScene(errorScene);
                errorMessage.show();
                errorOkButton.setOnMouseClicked(event7 -> {
                    errorMessage.close();
                    wPNameField.positionCaret(wPNameField.getText().length());
                    newWStage.show();
                });
            }
          //  }
        });

        BooleanBinding bb1 = new BooleanBinding() {
            {
                super.bind(datePicker.valueProperty(), wPNameField.textProperty());
            }

            @Override
            protected boolean computeValue() {
                return (datePicker.getValue() == null || wPNameField.getText().isEmpty());
            }
        };
        newWPBtn.disableProperty().bind(bb1);

        cancelNewWPBtn.setOnMouseClicked(event1 -> {
            wPNameField.setText("");
            adaptor.getMainHBox().setDisable(false);
            newWStage.close();
        });
        cancelNewWPBtn.setOnMouseEntered(event1 -> {
            cancelNewWPBtn.setEffect(borderGlow);
        });
        cancelNewWPBtn.setOnMouseExited(event1 -> {
            cancelNewWPBtn.setEffect(null);
        });

        adaptor.getNewWeekPlanButton().setOnMouseClicked(event -> {

            adaptor.getStartDatePicker().setDayCellFactory(dp -> new DateCell() {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    for(int h = 0; h < adaptor.getPatientSelected().plans.size(); h++) {
                        String[] sdays = adaptor.getPatientSelected().plans.get(h).getWeekPlanSDate().split("/");
                        if((sdays[0].equals(item.getDayOfMonth()+"")&&(sdays[1].equals(item.getMonthValue()+""))&& (sdays[2].equals(item.getYear()+"")))
                                || item.getDayOfWeek() != DayOfWeek.MONDAY
                                || ((today == item.getDayOfMonth())&& month == item.getMonthValue() && year == item.getYear())
                                || item.isBefore(local)){
                            System.out.println(item+ "<<<<<<<");
                           // System.out.println(adaptor.getPatientSelected().plans.get(h).getWeekPlanSDate()+ ">>>>>>>>>>>");
                            setDisable(true);
                        }
                    }
                    //setDisable(empty || item.getDayOfWeek() != DayOfWeek.MONDAY);
                }
            });
            newWStage = new Stage();
            newWStage.initModality(Modality.APPLICATION_MODAL);
            newWStage.initOwner(stage);
            newWStage.centerOnScreen();
            pRNameLabel.setText(adaptor.getPatientSelected().getPatientName());
            newWStage.initStyle(StageStyle.TRANSPARENT);
            newWStage.setAlwaysOnTop(true);
            newWStage.setScene(newWeekScene);
            newWStage.setResizable(false);
            adaptor.getToggleWeekPlans().selectToggle(null);
            adaptor.getToggleDays().selectToggle(null);
            adaptor.getMainHBox().setDisable(true);
            newWStage.show();
        });

        webengine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                if (newState == Worker.State.SUCCEEDED) {
                    //System.out.println(webengine.getDocument().getDocumentElement().getTextContent());
                    switch (pgRequest){
                        case "coachSignup":
                        	
                            if(webengine.getDocument().getDocumentElement().getTextContent().equals("?errorSignup?")){
                                signupStage.hide();
                                errorText.setText("Username already used, please choose another");
                                errorMessage.setScene(errorScene);
                                errorMessage.show();
                                errorOkButton.setOnMouseClicked(event2 -> {
                                    errorMessage.close();
                                    signupVBox.setDisable(false);
                                    signupStage.show();
                                    userNameSField.requestFocus();
                                    repeatPassField.positionCaret(repeatPassField.getText().length());
                                });
                            }
                            else {
                                signupStage.close();
                                //Logged in
                                userNameField.setText("");
                                passwordField.setText("");
                                userNameSField.setText("");
                                passwordSField.setText("");
                                repeatPassField.setText("");
                                loginStage.show();
                            }
                            break;
                        case "coachLogin":{
                        	//System.out.println(webengine.getDocument().getDocumentElement().getTextContent()+ " ~~~");
                            if (webengine.getDocument().getDocumentElement().getTextContent().equals("?errorLogin?")) {
                                loginStage.close();
                                errorText.setText("Wrong UserName or Password");
                                errorMessage.setScene(errorScene);
                                errorMessage.show();
                                errorOkButton.setOnMouseClicked(event2 -> {
                                    errorMessage.close();
                                    loginStage.show();
                                    userNameField.requestFocus();
                                    userNameField.positionCaret(userNameField.getText().length());
                                });
                            }
                            else {//?noPatients?
                                if(webengine.getDocument().getDocumentElement().getTextContent().equals("?noPatients?")){
                                    //Logged in
                                	//System.out.println(webengine.getDocument().getDocumentElement().getTextContent());
                                    adaptor.getMainHBox().setDisable(false);
                                    adaptor.getCoach().setCoachName(userNameField.getText());
                                    loginStage.close();
                                    stage.show();
                                }
                                else {
                                    //Logged in
                                	//System.out.println(webengine.getDocument().getDocumentElement().getTextContent());
                                    adaptor.getMainHBox().setDisable(false);
                                    adaptor.getCoach().setCoachName(userNameField.getText());
                                    if (!webengine.getDocument().getDocumentElement().getTextContent().isEmpty()) {
                                        String[] resultPatients = webengine.getDocument().getDocumentElement().getTextContent().split("%25,%25");
                                        for (int x = 0; x < resultPatients.length; x++) {
                                            if (!(resultPatients[x].equals(""))) {
                                            	
                                                String[] pInfo = resultPatients[x].split("%25..%25");
                                                if(pInfo.length > 1)
                                                    adaptor.getCoach().patients.add(new Patient(pInfo[0], adaptor.getCoach().patients.size()+1,
                                                        userNameField.getText(), pInfo[1]));
                                                else{
                                                	if(pInfo.length != 0)
                                                		adaptor.getCoach().patients.add(new Patient(pInfo[0],adaptor.getCoach().patients.size()+1,
                                                				userNameField.getText(),""));
                                                }

                                            }
                                        }
                                        for(int x = 0; x < adaptor.getCoach().patients.size(); x++) {
                                            if (adaptor.getCoach().getPatientByName(adaptor.getCoach().patients.get(x).getPatientName()).getFile().length() > 10) {
                                                adaptor.getCoach().getPatientByName(adaptor.getCoach().patients.get(x).getPatientName())
                                                        .setPlans(LoadPlan.parse(adaptor.getCoach().patients.get(x), adaptor.getCoach().patients.get(x).file));
                                            }
                                        }
                                        adaptor.addPatientsToVBox();
                                    }
                                    loginStage.close();
                                    stage.show();
                                }
                            }}
                            break;
                        case "getAvailablePatients":
                            if(!webengine.getDocument().getDocumentElement().getTextContent().equals("?errorGettingAvailablePatients?")){
                                pNameList.getItems().clear();
                                String[] resultFiles = webengine.getDocument().getDocumentElement().getTextContent().split(",");
                                for (int x = 0; x < resultFiles.length; x++) {
                                    if(!resultFiles[x].equals(""))
                                        pNameList.getItems().add(resultFiles[x]);
                                }
                            }
                            break;
                        case "addPatient":
                            if(!webengine.getDocument().getDocumentElement().getTextContent().equals("?error?")){
                                adaptor.setAddPatientName(pNameList.getSelectionModel().getSelectedItem().toString());
                                String pAddfile = "{\"patientName\":\"" + adaptor.getAddPatientName() +"\",\n" +
                                        "\"coachName\": \""+ adaptor.getCoach().coachName+"\",\n" +
                                        "\"plans\": [\n"+
                                        "]\n" +
                                        "}";
                              //  System.out.println(webengine.getDocument().getDocumentElement().getTextContent() + "  ,,,,,add patient file");
                                if(!webengine.getDocument().getDocumentElement().getTextContent().equals("{}")) {
                                    adaptor.setAddPatientFile(webengine.getDocument().getDocumentElement().getTextContent());
                                }
                                else {
                                    adaptor.setAddPatientFile(pAddfile);
                                }
                                //System.out.println(adaptor.getAddPatientName() + "   added a patient<<<<<<<<<<<<");
                                adaptor.getMainHBox().setDisable(false);
                                addPStage.close();
                                adaptor.getAddPbtn().defaultButtonProperty().setValue(true);
                            }
                            else {
                                addPStage.close();
                                errorText.setText("Patient already added");
                                errorMessage.setScene(errorScene);
                                errorMessage.show();
                                errorOkButton.setOnMouseClicked(event2 -> {
                                    errorMessage.close();
                                    addPStage.show();
                                });
                            }
                            break;
                        case "removePatient":
                            System.out.println("wesel el remove!!!");
                            if(webengine.getDocument().getDocumentElement().getTextContent().equals("?removeSuccess?")){
                                adaptor.getMainHBox().setDisable(false);
                                removePStage.close();
                                adaptor.getRemovePbtn().defaultButtonProperty().setValue(true);
                            }
                            else {
                                System.out.println("error fel remove!!!");
                                removePStage.close();
                                errorText.setText("");
                                errorText.setText("Error while removing Patient");
                                errorMessage.setScene(errorScene);
                                errorMessage.show();
                                errorOkButton.setOnMouseClicked(event2 -> {
                                    errorMessage.close();
                                    removePStage.show();
                                });
                            }
                            break;
                        case "setPatientFile":
                            
                            if(setFiles.equals("?fileSetSuccess?")){
                            	System.out.println("saved files");
                                stage.close();
                            }
                            else {
                                System.out.println("error fel remove!!!");
                                errorText.setText("");
                                saveAndExitStage.hide();
                                errorText.setText("Couldn't save the changes, please try again");
                                errorMessage.setScene(errorScene);
                                errorMessage.show();
                                errorOkButton.setOnMouseClicked(event2 -> {
                                    errorMessage.close();
                                    saveAndExitStage.show();
                                });
                            }
                            break;
                        case "":
                            break;
                        default:
                            break;
                    }
                }
            }
        });
        webView.setOnCloseRequest(new EventHandler<WindowEvent>(){
            @Override public void handle(WindowEvent event) {
                web.getEngine().load(null);
             
            }
        });
        
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
