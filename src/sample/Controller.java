package sample;

import com.sun.javafx.geom.AreaOp;
//import com.sun.xml.internal.bind.util.Which;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.joda.time.LocalDate;
import sample.WeekPlan.*;
import sample.utils.Adaptor;
import sample.utils.LoadPlan;
import sample.utils.PlanParser;
import sample.utils.SavePlan;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;


public class Controller extends PlanParser implements Initializable {


    @FXML Button addPatientButton;
    @FXML Button removePatientButton;
    @FXML Button newWeekPlanButton;
    @FXML Button removeWeekPlanButton;
    @FXML Button saveAndExitButton;
    Button emailButton;
    Button chatButton;
    Button addTaskButton = new Button("Add");

    Button removeTaskButton = new Button();
    @FXML Button overPerfButton;

    @FXML ScrollPane patientsScroll;
    @FXML ScrollPane weekPlansScroll;

    final ToggleGroup toggleDays = new ToggleGroup();
    @FXML ToggleButton Monday;
    @FXML ToggleButton Tuesday;
    @FXML ToggleButton Wednesday;
    @FXML ToggleButton Thursday;
    @FXML ToggleButton Friday;
    @FXML ToggleButton Saturday;
    @FXML ToggleButton Sunday;

    @FXML HBox mainHBox;
    @FXML VBox weekPlanVBox;
    @FXML HBox weekPlanHBox;
    @FXML VBox patientsVBox;
    @FXML HBox tasksHBox;
    @FXML HBox daysHBox;
    @FXML VBox fullWeekPlanVBox;
    @FXML VBox weekTitleVBox;
    @FXML VBox tasksVBox;
    HBox buttonBoxChatandemail;

    @FXML Label weekPlanLabel;
    @FXML Label sDateLabel;
    @FXML Label eDateLabel;

    @FXML ScrollPane scrollTasks;



    final ToggleGroup togglePatients = new ToggleGroup();
    final ToggleGroup toggleWeekPlans = new ToggleGroup();

    Calendar calendar = new GregorianCalendar();
    LocalDate now = new LocalDate();
    int today = calendar.get(Calendar.DAY_OF_WEEK);
    //Hour of day
    int hour = calendar.get(Calendar.HOUR);
    int minute = calendar.get(Calendar.MINUTE);
    public Coach coach = new Coach();

    public Patient patientSelected;
    public WeekPlan weekPlanSelected;
    public Day daySelected;
    public int daySelectedID;


    DropShadow borderGlow= new DropShadow();

    Adaptor adaptor = Adaptor.getInstance();

    public void initialize(URL url, ResourceBundle rb) {
        addTaskButton.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 20));
        patientsVBox.getChildren().clear();
        patientsVBox.getChildren().add(addPatientButton);

        weekPlanVBox.getChildren().clear();
        weekPlanVBox.getChildren().add(newWeekPlanButton);

        adaptor.setCoach(coach);


        adaptor.setMainHBox(mainHBox);
        adaptor.setFullWeekPlanVBox(fullWeekPlanVBox);
        adaptor.setAddPatientButton(addPatientButton);
        adaptor.setAddTaskButton(addTaskButton);
        adaptor.setNewWeekPlanButton(newWeekPlanButton);
        adaptor.setOverPerfButton(overPerfButton);
        adaptor.setPatientsVBox(patientsVBox);
        adaptor.setRemovePatientButton(removePatientButton);
        adaptor.setRemoveTaskButton(removeTaskButton);
        adaptor.setRemoveWeekPlanButton(removeWeekPlanButton);
        adaptor.setTasksHBox(tasksHBox);
        adaptor.setWeekPlanHBox(weekPlanHBox);
        adaptor.setWeekPlanVBox(weekPlanVBox);
        adaptor.setSaveAndExitButton(saveAndExitButton);
        adaptor.setToggleDays(toggleDays);
        adaptor.setTogglePatients(togglePatients);
        adaptor.setToggleWeekPlans(toggleWeekPlans);

        adaptor.setPatientSelected(patientSelected);
        adaptor.setWeekPlanSelected(weekPlanSelected);

        weekPlanVBox.setDisable(true);
        removeWeekPlanButton.setDisable(true);
        tasksVBox.setDisable(true);
        daysHBox.setDisable(true);
        weekTitleVBox.setDisable(true);
        saveAndExitButton.setDisable(false);
        adaptor.getAddTaskButton().setDisable(true);
        adaptor.getRemoveTaskButton().setDisable(true);

        weekPlanLabel.setText("Select or Add a WeekPlan");
        sDateLabel.setText("");
        eDateLabel.setText("");


          // Chat Button
          chatButton = new Button("Chat");
          chatButton.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 20));
          chatButton.setTextFill(Color.BLACK);
          chatButton.setPrefWidth(75);
          chatButton.setPrefHeight(25);
          chatButton.setStyle("-fx-base: white; -fx-background-color: #FFFFFFEE;");
          chatButton.setOnMouseEntered(event -> {
              chatButton.setEffect(borderGlow);
          });
          chatButton.setOnMouseExited(event -> {
              chatButton.setEffect(null);
          });
          // Add event handler for chat button
          chatButton.setOnAction(event -> {
              // Add your chat functionality here
              System.out.println("Chat button clicked");
          });
          // Create HBox container
          buttonBoxChatandemail = new HBox();
          buttonBoxChatandemail.setAlignment(Pos.CENTER);
          buttonBoxChatandemail.setSpacing(1); // Set spacing between buttons
          
          // Add chat button to the HBox
          buttonBoxChatandemail.getChildren().addAll(chatButton);
          
          
        
        Monday.setUserData(2);
        Monday.setToggleGroup(toggleDays);
        Monday.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue){
                    Monday.setStyle("-fx-background-color:  #EF973944");
                }
                else {
                    Monday.setStyle("-fx-background-color:  #EF9739EE");
                }
            }
        });
        Tuesday.setUserData(3);
        Tuesday.setToggleGroup(toggleDays);
        Tuesday.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    Tuesday.setStyle("-fx-background-color:  #EF973944");
                }
                else {
                    Tuesday.setStyle("-fx-background-color:  #EF9739EE");
                }
            }
        });
        Wednesday.setUserData(4);
        Wednesday.setToggleGroup(toggleDays);
        Wednesday.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    Wednesday.setStyle("-fx-background-color:  #EF973944");
                }
                else {
                    Wednesday.setStyle("-fx-background-color:  #EF9739EE");
                }
            }
        });
        Thursday.setUserData(5);
        Thursday.setToggleGroup(toggleDays);
        Thursday.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    Thursday.setStyle("-fx-background-color:  #EF973944");
                }
                else {
                    Thursday.setStyle("-fx-background-color:  #EF9739EE");
                }
            }
        });
        Friday.setUserData(6);
        Friday.setToggleGroup(toggleDays);
        Friday.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue) {
                    Friday.setStyle("-fx-background-color:  #EF973944");
                }
                else {
                    Friday.setStyle("-fx-background-color:  #EF9739EE");
                }
            }
        });
        Saturday.setUserData(7);
        Saturday.setToggleGroup(toggleDays);
        Saturday.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue) {
                    Saturday.setStyle("-fx-background-color:  #EF973944");
                }
                else {
                    Saturday.setStyle("-fx-background-color:  #EF9739EE");
                }
            }
        });
        Sunday.setUserData(1);
        Sunday.setToggleGroup(toggleDays);
        Sunday.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue) {
                    Sunday.setStyle("-fx-background-color:  #EF973944");
                }
                else {
                    Sunday.setStyle("-fx-background-color:  #EF9739EE");
                }
            }
        });


        int depth = 35;

        borderGlow.setOffsetY(0f);
        borderGlow.setOffsetX(0f);
        borderGlow.setColor(Color.WHITE);
        borderGlow.setWidth(depth);
        borderGlow.setHeight(depth);

        Monday.setOnMouseEntered(event1 -> {
            Monday.setEffect(borderGlow);
        });
        Monday.setOnMouseExited(event1 -> {
            Monday.setEffect(null);
        });

        Tuesday.setOnMouseEntered(event1 -> {
            Tuesday.setEffect(borderGlow);
        });
        Tuesday.setOnMouseExited(event1 -> {
            Tuesday.setEffect(null);
        });

        Wednesday.setOnMouseEntered(event1 -> {
            Wednesday.setEffect(borderGlow);
        });
        Wednesday.setOnMouseExited(event1 -> {
            Wednesday.setEffect(null);
        });

        Thursday.setOnMouseEntered(event1 -> {
            Thursday.setEffect(borderGlow);
        });
        Thursday.setOnMouseExited(event1 -> {
            Thursday.setEffect(null);
        });

        Friday.setOnMouseEntered(event1 -> {
            Friday.setEffect(borderGlow);
        });
        Friday.setOnMouseExited(event1 -> {
            Friday.setEffect(null);
        });

        Saturday.setOnMouseEntered(event1 -> {
            Saturday.setEffect(borderGlow);
        });
        Saturday.setOnMouseExited(event1 -> {
            Saturday.setEffect(null);
        });

        Sunday.setOnMouseEntered(event1 -> {
            Sunday.setEffect(borderGlow);
        });
        Sunday.setOnMouseExited(event1 -> {
            Sunday.setEffect(null);
        });

        overPerfButton.setDisable(true);
        overPerfButton.setOnMouseEntered(event2 -> {
            overPerfButton.setEffect(borderGlow);
        });
        overPerfButton.setOnMouseExited(event2 -> {
            overPerfButton.setEffect(null);
        });

        addPatientButton.setOnMouseEntered(event1 -> {
            addPatientButton.setEffect(borderGlow);
        });
        addPatientButton.setOnMouseExited(event1 -> {
            addPatientButton.setEffect(null);
        });

        addTaskButton.setOnMouseEntered(event1 -> {
            addTaskButton.setEffect(borderGlow);
        });
        addTaskButton.setOnMouseExited(event1 -> {
            addTaskButton.setEffect(null);
        });

        newWeekPlanButton.setOnMouseEntered(event1 -> {
            newWeekPlanButton.setEffect(borderGlow);
        });
        newWeekPlanButton.setOnMouseExited(event1 -> {
            newWeekPlanButton.setEffect(null);
        });

        removePatientButton.setDisable(true);
        removePatientButton.setOnMouseEntered(event1 -> {
            removePatientButton.setEffect(borderGlow);
        });
        removePatientButton.setOnMouseExited(event1 -> {
            removePatientButton.setEffect(null);
        });

        removeWeekPlanButton.setOnMouseEntered(event1 -> {
            removeWeekPlanButton.setEffect(borderGlow);
        });
        removeWeekPlanButton.setOnMouseExited(event1 -> {
            removeWeekPlanButton.setEffect(null);
        });

        removeTaskButton.setOnMouseEntered(event1 -> {
            removeTaskButton.setEffect(borderGlow);
        });
        removeTaskButton.setOnMouseExited(event1 -> {
            removeTaskButton.setEffect(null);
        });

        saveAndExitButton.setOnMouseEntered(event1 -> {
            saveAndExitButton.setEffect(borderGlow);
        });
        saveAndExitButton.setOnMouseExited(event1 -> {
            saveAndExitButton.setEffect(null);
        });

        //adding patients to VBox
        addPatientsToVBox();

        ObservableList<Node> patients = patientsVBox.getChildren();
        for (Node x: patients) {
            x.setOnMouseEntered(event -> {
                x.setEffect(borderGlow);
            });
            x.setOnMouseExited(event -> {
                x.setEffect(null);
            });
        }

        ObservableList<Node> weekPlans = weekPlanVBox.getChildren();
        for (Node x: weekPlans) {
            x.setOnMouseEntered(event -> {
                x.setEffect(borderGlow);
            });
            x.setOnMouseExited(event -> {
                x.setEffect(null);
            });
        }

        adaptor.getToggleWeekPlans().selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if(newValue == null){
                    adaptor.getRemoveWeekPlanButton().setDisable(true);
                    tasksVBox.setDisable(true);
                    daysHBox.setDisable(true);
                    toggleDays.selectToggle(null);
                    tasksHBox.getChildren().clear();
                    weekTitleVBox.setDisable(true);
                    toggleWeekPlans.selectToggle(null);
                    adaptor.setWeekPlanSelected(null);
                    weekPlanLabel.setText("Select or Add a WeekPlan");
                    sDateLabel.setText("");
                    eDateLabel.setText("");
                }
                else {
                    adaptor.getRemoveWeekPlanButton().setDisable(false);
                    toggleDays.selectToggle(null);
                    tasksVBox.setDisable(false);
                    daysHBox.setDisable(false);
                    weekTitleVBox.setDisable(false);
                    weekPlanSelected = patientSelected.plans.get((int) toggleWeekPlans.getSelectedToggle().getUserData() - 1);
                    weekPlanLabel.setText(weekPlanSelected.getWeekPlanName());
                    sDateLabel.setText(weekPlanSelected.getWeekPlanSDate());
                    eDateLabel.setText(weekPlanSelected.getWeekPlanEDate());
                    adaptor.setWeekPlanSelected(weekPlanSelected);
                }
            }
        });

        adaptor.getTogglePatients().selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if(newValue==null) {
                    removePatientButton.setDisable(true);
                    removeWeekPlanButton.setDisable(true);
                    chatButton.setVisible(true);
                    emailButton.setVisible(true);
                    tasksVBox.setDisable(true);
                    daysHBox.setDisable(true);
                    toggleDays.selectToggle(null);
                    tasksHBox.getChildren().clear();
                    weekTitleVBox.setDisable(true);
                    adaptor.setWeekPlanSelected(null);
                    weekPlanLabel.setText("Select or Add a WeekPlan");
                    sDateLabel.setText("");
                    eDateLabel.setText("");
                    //adaptor.setRemovePatientButton(removePatientButton);
                    weekPlanVBox.setDisable(true);
                    weekPlanVBox.getChildren().clear();
                    weekPlanVBox.getChildren().add(adaptor.getNewWeekPlanButton());
                    toggleDays.selectToggle(null);
                    adaptor.setPatientSelected(null);
                    overPerfButton.setDisable(true);
                    adaptor.getOverPerfButton().setDisable(true);
                }
                else {
                    overPerfButton.setDisable(false);
                    adaptor.getOverPerfButton().setDisable(false);
                    removePatientButton.setDisable(false);
                    adaptor.getRemovePatientButton().setDisable(false);
                    weekPlanVBox.setDisable(false);
                    patientSelected = coach.getPatientByName(togglePatients.getSelectedToggle().getUserData()+ "");

                    adaptor.setPatientSelected(patientSelected);
                    adaptor.setSelectedPName(patientSelected.getPatientName());
                    addWeekPlansToVBox();
                }
            }
        });

        adaptor.getEditTbtn().defaultButtonProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    coach.getPatientByName(patientSelected.getPatientName()).getWeekPlanByName(weekPlanSelected.getWeekPlanName()).getDayByName(daySelected.getDayName())
                            .tasks.remove(daySelected.getTaskByName(adaptor.getRemoveTaskName()));
                    tasksHBox.getChildren().clear();
                    //TODO: update values here!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    String addTStartTime = adaptor.getAddTSHour() + ":" + adaptor.getAddTSMin();
                    String addTEndTime = adaptor.getAddTEHour() + ":" + adaptor.getAddTEMin();
                    int addTaskSTime = Integer.parseInt(adaptor.getAddTSHour()) * 60 + Integer.parseInt(adaptor.getAddTSMin());
                    int dayTasksSize = daySelected.getTasks().size();
                    for(int x =0; x < daySelected.getTasks().size(); x++){
                        int taskSTime = daySelected.getTasks().get(x).getStartTimeH() * 60 + daySelected.getTasks().get(x).getStartTimeM();
                        if(taskSTime > addTaskSTime) {
                            coach.getPatientByName(patientSelected.getPatientName()).getWeekPlanByName(weekPlanSelected.weekPlanName)
                                    .getDayByName(daySelected.getDayName()).getTasks().add(x, new Task(((daySelected.tasks.size()+1)+""), adaptor.getAddTName(),
                                    addTStartTime, addTEndTime, adaptor.getAddTDescription(), adaptor.getAddTPrograms(), "Waiting"));
                            break;
                        }
                    }
                    if(dayTasksSize == daySelected.getTasks().size()){
                        coach.getPatientByName(patientSelected.getPatientName()).getWeekPlanByName(weekPlanSelected.weekPlanName)
                                .getDayByName(daySelected.getDayName()).getTasks().add(new Task((daySelected.getNumberOfTasks()+1)+"", adaptor.getAddTName(),
                                addTStartTime, addTEndTime, adaptor.getAddTDescription(), adaptor.getAddTPrograms(), "Waiting"));
                    }
                    int startTimeIndex = (Integer.parseInt(adaptor.getAddTSHour())*12) + (Integer.parseInt(adaptor.getAddTSMin())/5);
                    int endTimeIndex = (Integer.parseInt(adaptor.getAddTEHour())*12) + (Integer.parseInt(adaptor.getAddTEMin())/5);
                    for(int x = startTimeIndex; x <= endTimeIndex; x++){
                        coach.getPatientByName(patientSelected.getPatientName()).getWeekPlanByName(weekPlanSelected.getWeekPlanName())
                                .getDayByName(daySelected.getDayName()).getTimeSlots()[x] = daySelected.tasks.size()+1;
                    }
                    System.out.println(coach.getPatientByName(patientSelected.getPatientName()).getWeekPlanByName(weekPlanSelected.weekPlanName)
                            .getDayByName(daySelected.getDayName()).getTasks().size() + "..........tasks array size");
                    addTasksToHBox();
                    adaptor.getEditTbtn().defaultButtonProperty().setValue(false);
                }
            }
        });


        adaptor.getToggleDays().selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if(newValue==null) {
                    daySelected = null;
                    adaptor.setDaySelected(daySelected);
                    tasksHBox.getChildren().clear();
                    adaptor.getAddTaskButton().setDisable(true);
                    adaptor.getRemoveTaskButton().setDisable(true);
                }
                else {
                    daySelected = weekPlanSelected.days.get((int) toggleDays.getSelectedToggle().getUserData() - 1);
                    adaptor.getAddTaskButton().setDisable(false);
                    adaptor.getRemoveTaskButton().setDisable(false);
                    addTasksToHBox();
                    adaptor.setDaySelected(daySelected);
                    adaptor.setDaySelectedArray(daySelected.getTimeSlots());
//                    adaptor.getDaySelected().getTimeSlots()[0] = 1;
//                    adaptor.getDaySelected().getTimeSlots()[1] = 1;
//                    adaptor.getDaySelected().getTimeSlots()[2] = 1;
//                    adaptor.getDaySelected().getTimeSlots()[3] = 1;
//                    adaptor.getDaySelected().getTimeSlots()[4] = 1;
//                    adaptor.getDaySelected().getTimeSlots()[5] = 1;
//
//                    adaptor.getDaySelected().getTimeSlots()[16] = 2;
//                    adaptor.getDaySelected().getTimeSlots()[17] = 2;
//                    adaptor.getDaySelected().getTimeSlots()[18] = 2;
//                    adaptor.getDaySelected().getTimeSlots()[19] = 2;
//                    adaptor.getDaySelected().getTimeSlots()[20] = 2;
//                    adaptor.getDaySelected().getTimeSlots()[21] = 2;
//                    adaptor.getDaySelected().getTimeSlots()[22] = 2;
                }
            }
        });

        //adding patient
        adaptor.getAddPbtn().defaultButtonProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                if(newValue){
//                    Patient newPatient = LoadPlan.parse("C:\\Users\\" + System.getProperty("user.name") +"\\Desktop\\SavePlan.txt", adaptor.getAddPatientName());
//                    coach.patients.add(newPatient);
                    coach.patients.add(new Patient(adaptor.getAddPatientName(), coach.patients.size() + 1, coach.coachName, adaptor.getAddPatientFile()));
                    coach.getPatientByName(adaptor.getAddPatientName())
                            .setPlans(LoadPlan.parse(coach.getPatientByName(adaptor.getAddPatientName()), adaptor.getAddPatientFile()));

//                    for (int x = 0; x < coach.patients.size(); x++){
//                        Patient p1 = LoadPlan.parse("C:\\Users\\" + System.getProperty("user.name") +"\\Desktop\\SavePlan.txt");
//                        Patient p2 = LoadPlan.parse("C:\\Users\\" + System.getProperty("user.name") +"\\Desktop\\SavePlan2.txt");
//                        coach.patients.get(x).plans.add(p1.plans.get(p1.plans.size()-1));
//                        coach.patients.get(x).plans.add(p2.plans.get(p2.plans.size()-1));
//                        System.out.println(coach.patients.get(x).getPatientName() + "   coach patients<<<<<<<<<<<<");
//                    }
                    //System.out.println(coach.patients.get(1) + "   added a patient<<<<<<<<<<<<");
                    addPatientsToVBox();
                    adaptor.getAddPbtn().defaultButtonProperty().setValue(false);
                    //System.out.println(coach.patients.size() + "   added a patient<<<<<<<<<<<<");

                }
            }
        });

        //adding task
        adaptor.getAddTbtn().defaultButtonProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                	ArrayList<Program> programs = new ArrayList<Program>();
                	if(adaptor.getAddTPrograms().size() > 0){
                	for(int i = 0; i < adaptor.getAddTPrograms().size();i++){
                		programs.add(adaptor.getAddTPrograms().get(i));
                	}
                //		
                
                	}
                    String addTStartTime = adaptor.getAddTSHour() + ":" + adaptor.getAddTSMin();
                    String addTEndTime = adaptor.getAddTEHour() + ":" + adaptor.getAddTEMin();
                    int addTaskSTime = Integer.parseInt(adaptor.getAddTSHour()) * 60 + Integer.parseInt(adaptor.getAddTSMin());
                    int dayTasksSize = daySelected.getTasks().size();
                    for(int x =0; x < daySelected.getTasks().size(); x++){
                        int taskSTime = daySelected.getTasks().get(x).getStartTimeH() * 60 + daySelected.getTasks().get(x).getStartTimeM();
                        if(taskSTime > addTaskSTime) {
                            coach.getPatientByName(patientSelected.getPatientName()).getWeekPlanByName(weekPlanSelected.weekPlanName)
                                    .getDayByName(daySelected.getDayName()).getTasks().add(x, new Task(((daySelected.getNumberOfTasks()+1)+""), adaptor.getAddTName(),
                                    addTStartTime, addTEndTime, adaptor.getAddTDescription(), programs, "Waiting"));
                            break;
                        }
                    }
                    if(dayTasksSize == daySelected.getTasks().size()){
                        coach.getPatientByName(patientSelected.getPatientName()).getWeekPlanByName(weekPlanSelected.weekPlanName)
                                .getDayByName(daySelected.getDayName()).getTasks().add(new Task((daySelected.getNumberOfTasks()+1)+"", adaptor.getAddTName(),
                                addTStartTime, addTEndTime, adaptor.getAddTDescription(), programs, "Waiting"));
                    }
                    coach.getPatientByName(patientSelected.getPatientName()).getWeekPlanByName(weekPlanSelected.getWeekPlanName())
                            .getDayByName(daySelected.getDayName()).setNumberOfTasks(daySelected.getNumberOfTasks()+1);
                    int startTimeIndex = (Integer.parseInt(adaptor.getAddTSHour())*12) + (Integer.parseInt(adaptor.getAddTSMin())/5);
                    int endTimeIndex = (Integer.parseInt(adaptor.getAddTEHour())*12) + (Integer.parseInt(adaptor.getAddTEMin())/5);
                    for(int x = startTimeIndex; x <= endTimeIndex; x++){
                        coach.getPatientByName(patientSelected.getPatientName()).getWeekPlanByName(weekPlanSelected.getWeekPlanName())
                                .getDayByName(daySelected.getDayName()).getTimeSlots()[x] = daySelected.tasks.size()+1;
                    }
                    System.out.println(coach.getPatientByName(patientSelected.getPatientName()).getWeekPlanByName(weekPlanSelected.weekPlanName)
                            .getDayByName(daySelected.getDayName()).getTasks().size() + "..........tasks array size");
                    adaptor.getAddTbtn().defaultButtonProperty().setValue(false);
                    addTasksToHBox();
                	adaptor.getAddTPrograms().clear();
                }
                
            }
        });

        //removing Patient
        adaptor.getRemovePbtn().defaultButtonProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    patientsVBox.getChildren().clear();
                    if (coach.getPatientIndexByName(patientSelected.getPatientName()) != 99999999){
                        coach.patients.remove(coach.getPatientIndexByName(patientSelected.getPatientName()));
                        addPatientsToVBox();
                        adaptor.setPatientSelected(null);
                        adaptor.getRemovePbtn().defaultButtonProperty().setValue(false);
                        System.out.println(coach.patients.size() + "   removed a patient<<<<<<<<<<<<");
                    }
                    else {
                        System.out.println("ERROR WHILE REMOVING PATIENT: didn't find patient<<<<<<<<<<<<");
                    }
                }
            }
        });

        //new WeekPlan
        adaptor.getNewWbtn().defaultButtonProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    coach.getPatientByName(patientSelected.getPatientName()).plans.add(
                            new WeekPlan(adaptor.getNewWPName(), adaptor.getSelectedSDate(), patientSelected.plans.size()+1));
                    weekPlanVBox.getChildren().clear();
                    addWeekPlansToVBox();
//                    try {
//                        SavePlan.savePlan(coach.getPatientByName(patientSelected.getPatientName()));
//                    } catch (IOException e) {
//
//                    }
                    adaptor.getNewWbtn().defaultButtonProperty().setValue(false);
                }
            }
        });

        //Remove Weekplan
        adaptor.getRemoveWbtn().defaultButtonProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    weekPlanVBox.getChildren().clear();
                    coach.getPatientByName(patientSelected.getPatientName()).plans.remove(patientSelected.getWeekPlanByName(adaptor.getRemoveWplanName()));
                    adaptor.setWeekPlanSelected(null);
                    removeWeekPlanButton.setDisable(true);
                    adaptor.getRemoveWbtn().defaultButtonProperty().setValue(false);
                    addWeekPlansToVBox();
                }
            }
        });

        //remove Task
        adaptor.getRemoveTbtn().defaultButtonProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    for(int x = 0; x < daySelected.getTimeSlots().length; x++){
                        if(coach.getPatientByName(patientSelected.getPatientName()).getWeekPlanByName(weekPlanSelected.getWeekPlanName())
                                .getDayByName(daySelected.getDayName()).getTimeSlots()[x] == daySelected.getTaskByName(adaptor.getRemoveTaskName()).getTaskID())
                        coach.getPatientByName(patientSelected.getPatientName()).getWeekPlanByName(weekPlanSelected.getWeekPlanName())
                                .getDayByName(daySelected.getDayName()).getTimeSlots()[x] = 0;
                    }
                    coach.getPatientByName(patientSelected.getPatientName()).getWeekPlanByName(weekPlanSelected.getWeekPlanName()).getDayByName(daySelected.getDayName())
                            .tasks.remove(daySelected.getTaskByName(adaptor.getRemoveTaskName()));
                    tasksHBox.getChildren().clear();
                    addTasksToHBox();
                    adaptor.getRemoveTbtn().defaultButtonProperty().setValue(false);
                }
            }
        });

    }
    public void addTasksToHBox(){
        tasksHBox.getChildren().clear();
        if(weekPlanSelected != null) {
            daySelected = weekPlanSelected.days.get((int) toggleDays.getSelectedToggle().getUserData() - 1);
            daySelectedID = daySelected.getDayID();
            System.out.println(daySelected.tasks.size());

            addTaskButton.setMinSize(100,100);
            addTaskButton.setStyle("-fx-base: white; -fx-background-color: #FFFFFFEE;");
            addTaskButton.setOnMouseEntered(event -> {
                addTaskButton.setEffect(borderGlow);
            });
            addTaskButton.setOnMouseExited(event -> {
                addTaskButton.setEffect(null);
            });
            VBox addTaskVBox = new VBox();
            addTaskVBox.setPrefSize(275, 455);
            addTaskVBox.setMaxSize(275, 455);
            addTaskVBox.setMinSize(275, 455);
            addTaskVBox.setAlignment(Pos.CENTER);
            addTaskVBox.getChildren().add(addTaskButton);
            addTaskVBox.setStyle("-fx-background-color: #a5b0b0;-fx-border-color: black");
            tasksHBox.getChildren().add(addTaskVBox);
            for (int t = 0; t < daySelected.tasks.size(); t++) {
                //task Name
                Label taskName = new Label(daySelected.tasks.get(t).getTaskName());
                taskName.setTextAlignment(TextAlignment.CENTER);
                taskName.setAlignment(Pos.CENTER);
                taskName.setFont(new Font("Copperplate Gothic Bold", 16));
                taskName.setContentDisplay(ContentDisplay.CENTER);
                taskName.setPrefWidth(Control.USE_COMPUTED_SIZE);
                taskName.setPrefHeight(40);
                taskName.setPrefWidth(275);
                taskName.setStyle("-fx-background-color: #909090;");

                HBox taskNameHBox = new HBox();
                taskNameHBox.setPrefSize(275, 40);
                taskNameHBox.setAlignment(Pos.CENTER);
                taskNameHBox.getChildren().add(taskName);

                //Start and end
                Label startLabel = new Label("Start Time " + daySelected.tasks.get(t).getStartTimeH() + ":" + daySelected.getTask(t).getStartTimeM());
                startLabel.setTextAlignment(TextAlignment.CENTER);
                startLabel.setAlignment(Pos.CENTER);
                startLabel.setFont(new Font("Copperplate Gothic Bold", 14));
                startLabel.setContentDisplay(ContentDisplay.CENTER);
                startLabel.setPrefWidth(Control.USE_COMPUTED_SIZE);
                startLabel.setPrefHeight(30);
                startLabel.setPrefWidth(138);

                Label endLabel = new Label("End Time " + daySelected.tasks.get(t).getEndTimeH() + ":" + daySelected.getTask(t).getEndTimeM());
                endLabel.setTextAlignment(TextAlignment.CENTER);
                endLabel.setAlignment(Pos.CENTER);
                endLabel.setFont(new Font("Copperplate Gothic Bold", 14));
                endLabel.setContentDisplay(ContentDisplay.CENTER);
                endLabel.setPrefWidth(Control.USE_COMPUTED_SIZE);
                endLabel.setPrefHeight(30);
                endLabel.setPrefWidth(121);

                HBox startEndHBox = new HBox();
                startEndHBox.setPrefSize(275, 30);
                startEndHBox.setAlignment(Pos.CENTER);
                startEndHBox.setSpacing(10);
                startEndHBox.getChildren().addAll(startLabel, endLabel);

                //Description
                Label desLabel = new Label("Description:");
                desLabel.setTextAlignment(TextAlignment.CENTER);
                desLabel.setAlignment(Pos.CENTER);
                desLabel.setFont(new Font("Copperplate Gothic Bold", 15));
                desLabel.setContentDisplay(ContentDisplay.CENTER);
                desLabel.setPrefWidth(Control.USE_COMPUTED_SIZE);
                desLabel.setPrefHeight(30);
                desLabel.setPrefWidth(110);

                HBox desHBox = new HBox();
                desHBox.setPrefSize(273, 30);
                desHBox.setAlignment(Pos.CENTER_LEFT);
                desHBox.getChildren().add(desLabel);

                TextArea descText = new TextArea();
                descText.setText(daySelected.tasks.get(t).getTaskDescription());
                descText.setFont(new Font("Copperplate Gothic Bold", 14));
                descText.setEditable(false);
                descText.setPrefSize(273, 126);
                descText.setMinSize(273, 126);
                descText.setMaxSize(273, 126);
                descText.setWrapText(true);
                descText.setMouseTransparent(true);

                VBox desVBox = new VBox();
                desVBox.setPrefSize(273, 156);
                desVBox.setAlignment(Pos.CENTER);
                desVBox.getChildren().addAll(desHBox, descText);

                //Programs
                Label progLabel = new Label("Programs:");
                progLabel.setTextAlignment(TextAlignment.CENTER);
                progLabel.setAlignment(Pos.CENTER);
                progLabel.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 15));
                progLabel.setContentDisplay(ContentDisplay.CENTER);
                progLabel.setPrefWidth(Control.USE_COMPUTED_SIZE);
                progLabel.setPrefHeight(30);
                progLabel.setPrefWidth(110);

                HBox progHBox = new HBox();
                progHBox.setPrefSize(273, 30);
                progHBox.setAlignment(Pos.CENTER_LEFT);
                progHBox.getChildren().add(progLabel);

                TextArea progText = new TextArea();
                String progs = "";
                StringBuffer buf = new StringBuffer();
                for (int d = 0; d < daySelected.tasks.get(t).getPrograms().size(); d++) {
                 //   progs += "- " + daySelected.tasks.get(t).getProgram(d).getBaseName() + "\n";
                    buf.append("- " + daySelected.tasks.get(t).getProgram(d).getBaseName() + "\n");
                }
                progs = buf.toString();
                progText.setText(progs);
                progText.setFont(new Font("Copperplate Gothic Bold", 14));
                progText.setEditable(false);
                progText.setPrefSize(273, 70);
                progText.setMinSize(273, 70);
                progText.setMaxSize(273, 70);
                progText.setWrapText(true);
                progText.setMouseTransparent(true);

                VBox progVBox = new VBox();
                progVBox.setPrefSize(273, 100);
                progVBox.setAlignment(Pos.CENTER);
                progVBox.getChildren().addAll(progHBox, progText);

                //patient review
                Label revLabel = new Label("Review:");
                revLabel.setTextAlignment(TextAlignment.CENTER);
                revLabel.setAlignment(Pos.CENTER);
                revLabel.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 15));
                revLabel.setContentDisplay(ContentDisplay.CENTER);
                revLabel.setPrefWidth(Control.USE_COMPUTED_SIZE);
                revLabel.setPrefHeight(30);
                revLabel.setPrefWidth(110);

                HBox revHBox = new HBox();
                revHBox.setPrefSize(273, 30);
                revHBox.setAlignment(Pos.CENTER_LEFT);
                revHBox.getChildren().add(revLabel);

                TextArea revText = new TextArea();
                revText.setText(daySelected.tasks.get(t).getTaskReview());
                revText.setFont(new Font("Copperplate Gothic Bold", 14));
                revText.setEditable(false);
                revText.setPrefSize(273, 100);
                revText.setMinSize(273, 100);
                revText.setMaxSize(273, 100);
                revText.setWrapText(true);
                revText.setMouseTransparent(true);

                VBox revVBox = new VBox();
                revVBox.setPrefSize(273, 130);
                revVBox.setAlignment(Pos.CENTER);
                revVBox.getChildren().addAll(revHBox, revText);

                //Status
                Label statusLabel = new Label(daySelected.tasks.get(t).getStatus());
                statusLabel.setTextAlignment(TextAlignment.CENTER);
                statusLabel.setFont(new Font("Copperplate Gothic Bold", 16));
                statusLabel.setContentDisplay(ContentDisplay.CENTER);
                statusLabel.setAlignment(Pos.CENTER);
                statusLabel.setPrefWidth(Control.USE_COMPUTED_SIZE);
                statusLabel.setPrefSize(275, 45);

                //removeHBox
                Button removeTask = new Button("Remove");
                removeTask.setId(t + "");
                removeTask.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 14));
                removeTask.setPrefWidth(128);
                removeTask.setPrefHeight(30);
                removeTask.setStyle("-fx-background-color: white;");
                HBox removeHBox = new HBox();
                removeHBox.setAlignment(Pos.CENTER);
                removeHBox.setPrefSize(275, 30);
                removeHBox.setPadding(new Insets(0,0,8,70));
                removeHBox.setAlignment(Pos.CENTER_LEFT);
                removeHBox.getChildren().add(removeTask);

                removeTask.setOnMouseEntered(event1 -> {
                    removeTask.setEffect(borderGlow);
                });
                removeTask.setOnMouseExited(event1 -> {
                    removeTask.setEffect(null);
                });

                removeTask.setOnMouseClicked(event -> {
                    System.out.println(daySelected.tasks.get(Integer.parseInt(removeTask.getId())).getTaskName() + "<<<TaskName");
                    adaptor.setRemoveTName(daySelected.tasks.get(Integer.parseInt(removeTask.getId())).getTaskName());
                    adaptor.setRemoveTaskName(taskName.getText());
                    adaptor.getRemoveTaskButton().defaultButtonProperty().setValue(true);
                });


                //taskVBox
                VBox taskVBox = new VBox();
                taskVBox.setPrefSize(275, 455);
                taskVBox.setMaxSize(275, 455);
                taskVBox.setMinSize(275, 455);
                taskVBox.setAlignment(Pos.CENTER);
                taskVBox.setId("" + daySelected.tasks.get(t).getTaskID());
                taskVBox.getChildren().addAll(taskNameHBox, startEndHBox, desVBox, progVBox, revVBox, statusLabel, removeHBox);
                taskVBox.setStyle("-fx-background-color: #a5b0b0;-fx-border-color: black");
                if(daySelected.getDayID() != now.getDayOfWeek()) {
                    taskVBox.setOnMouseEntered(event -> {
                        taskVBox.setStyle("-fx-background-color: lightgrey;-fx-border-color: white;");
                    });
                    taskVBox.setOnMouseExited(event -> {
                        taskVBox.setStyle("-fx-background-color: #a5b0b0;-fx-border-color: black;");
                    });
                    taskVBox.setOnMouseClicked(event -> {
                        adaptor.setTaskSelectedName(taskName.getText());
                        adaptor.getEditTaskbtn().defaultButtonProperty().setValue(true);
                    });
                }
                else {
                    removeTask.setDisable(true);
                }
                tasksHBox.getChildren().add(taskVBox);
            }

        }

    }
    public void addPatientsToVBox(){
        patientsVBox.getChildren().clear();
        patientsVBox.getChildren().add(addPatientButton);
        for(int x = 0; x < coach.patients.size(); x++) {
            if (!coach.patients.get(x).getPatientName().equals("")) {
                ToggleButton patientName = new ToggleButton(coach.patients.get(x).getPatientName());
                coach.patients.get(x).setPatientID(x + 1);  //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<change id to the one from postres
                //System.out.println(coach.getPatientByName(coach.patients.get(x).getPatientName()));
                patientName.setUserData(coach.patients.get(x).getPatientName());
                patientName.setTextAlignment(TextAlignment.CENTER);
                patientName.setAlignment(Pos.CENTER);
                patientName.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 12));
                patientName.setStyle("-fx-base: white; -fx-background-color: #FFFFFFAA");
                patientName.setContentDisplay(ContentDisplay.CENTER);
                patientName.setPrefWidth(Control.USE_COMPUTED_SIZE);
                patientName.setPrefSize(155, 45);
                patientName.setMinSize(155, 45);
                patientName.setMaxSize(155, 45);
                patientName.setToggleGroup(togglePatients);
                patientName.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        if(newValue){
                            patientName.setStyle("-fx-background-color:  #FFFFFF44");
                        }
                        else {
                            patientName.setStyle("-fx-background-color:  #FFFFFFEE");
                        }
                    }
                });
                patientName.setOnMouseEntered(event -> {
                    patientName.setEffect(borderGlow);
                });
                patientName.setOnMouseExited(event -> {
                    patientName.setEffect(null);
                });
                patientsVBox.getChildren().add(patientName);
            }
        }
//        ObservableList<Node> patients = patientsVBox.getChildren();
//        for (Node x : patients) {
//            x.setOnMouseEntered(event -> {
//                x.setEffect(borderGlow);
//            });
//            x.setOnMouseExited(event -> {
//                x.setEffect(null);
//            });
//        }
    }

    public void addWeekPlansToVBox(){
        weekPlanVBox.getChildren().clear();
        weekPlanVBox.getChildren().addAll(buttonBoxChatandemail,newWeekPlanButton);
        System.out.println("from controller: weekplans number: " + adaptor.getPatientSelected().plans.size() );
        for(int x = 0; x < adaptor.getPatientSelected().plans.size(); x++){
            ToggleButton weekPlanName = new ToggleButton(patientSelected.plans.get(x).getWeekPlanName());
            coach.getPatientByName(patientSelected.getPatientName()).plans.get(x).setWeekPlanID(x+1);
            weekPlanName.setUserData(patientSelected.plans.get(x).getWeekPlanID());
            weekPlanName.setTextAlignment(TextAlignment.CENTER);
            weekPlanName.setAlignment(Pos.CENTER);
            weekPlanName.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 12));
            weekPlanName.setStyle("-fx-base: white; -fx-background-color: #FFFFFFAA");
            weekPlanName.setContentDisplay(ContentDisplay.CENTER);
            weekPlanName.setPrefWidth(Control.USE_COMPUTED_SIZE);
            weekPlanName.setPrefSize(155, 45);
            weekPlanName.setMinSize(155, 45);
            weekPlanName.setMaxSize(155, 45);
            weekPlanName.setToggleGroup(toggleWeekPlans);
            weekPlanName.setOnMouseEntered(event -> {
                weekPlanName.setEffect(borderGlow);
            });
            weekPlanName.setOnMouseExited(event -> {
                weekPlanName.setEffect(null);
            });
            weekPlanName.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if(newValue){
                        weekPlanName.setStyle("-fx-background-color: #FFFFFF44");
                    }
                    else {
                        weekPlanName.setStyle("-fx-background-color: #FFFFFFEE");
                    }
                }
            });
            weekPlanVBox.getChildren().add(weekPlanName);
        }

        ObservableList<Node> weekPlans = weekPlanVBox.getChildren();
        for (Node x: weekPlans) {
            x.setOnMouseEntered(event -> {
                x.setEffect(borderGlow);
            });
            x.setOnMouseExited(event -> {
                x.setEffect(null);
            });
        }
    }
}
