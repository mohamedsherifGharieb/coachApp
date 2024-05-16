package sample.utils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import sample.WeekPlan.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;


public class Adaptor {
    DropShadow borderGlow= new DropShadow();
    Calendar calendar = Calendar.getInstance();
    int today = calendar.get(Calendar.DAY_OF_MONTH);
    int month = calendar.get(Calendar.MONTH);
    int year = calendar.get(Calendar.YEAR);
    LocalDate local = LocalDate.now();

    private static Adaptor instance = null;

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    private Coach coach = new Coach();

    private HBox mainHBox;
    private HBox tasksHBox;
    private VBox weekPlanVBox;
    private HBox patientsVBox;
    private HBox weekPlanHBox;

    public VBox getFullWeekPlanVBox() {
        return fullWeekPlanVBox;
    }

    public void setFullWeekPlanVBox(VBox fullWeekPlanVBox) {
        this.fullWeekPlanVBox = fullWeekPlanVBox;
    }

    public ToggleGroup getToggleWeekPlans() {
        return toggleWeekPlans;
    }

    public void setToggleWeekPlans(ToggleGroup toggleWeekPlans) {
        this.toggleWeekPlans = toggleWeekPlans;
    }

    private VBox fullWeekPlanVBox;
    private Button addPatientButton;
    private Button removePatientButton;
    private Button newWeekPlanButton;
    private Button removeWeekPlanButton;
    private Button saveAndExitButton;
    private Button removeTaskButton;
    private Button addTaskButton;
    private Button overPerfButton;

    private Button addPbtn = new Button();
    private Button removePbtn = new Button();
    private Button newWbtn = new Button();
    private Button removeWbtn = new Button();
    private Button addTbtn = new Button();
    private Button removeTbtn = new Button();

    public Button getEditTbtn() {
        return editTbtn;
    }

    public void setEditTbtn(Button editTbtn) {
        this.editTbtn = editTbtn;
    }

    private Button editTbtn = new Button();

    public Button getOverPerfButton() {
        return overPerfButton;
    }

    public void setOverPerfButton(Button overPerfButton) {
        this.overPerfButton = overPerfButton;
    }

    public HBox getMainHBox() {
        return mainHBox;
    }

    public void setMainHBox(HBox mainHBox) {
        this.mainHBox = mainHBox;
    }

    public Button getRemoveTbtn() {
        return removeTbtn;
    }

    public void setRemoveTbtn(Button removeTbtn) {
        this.removeTbtn = removeTbtn;
    }

    public Button getAddTbtn() {
        return addTbtn;
    }

    public void setAddTbtn(Button addTbtn) {
        this.addTbtn = addTbtn;
    }

    public Button getAddPbtn() {
        return addPbtn;
    }

    public void setAddPbtn(Button addPbtn) {
        this.addPbtn = addPbtn;
    }

    public Button getRemovePbtn() {
        return removePbtn;
    }

    public void setRemovePbtn(Button removePbtn) {
        this.removePbtn = removePbtn;
    }

    public Button getNewWbtn() {
        return newWbtn;
    }

    public void setNewWbtn(Button addWbtn) {
        this.newWbtn = addWbtn;
    }

    public Button getRemoveWbtn() {
        return removeWbtn;
    }

    public void setRemoveWbtn(Button removewbtn) {
        this.removeWbtn = removewbtn;
    }



    public Button getAddTaskButton() {
        return addTaskButton;
    }

    public void setAddTaskButton(Button addTaskButton) {
        this.addTaskButton = addTaskButton;
    }

    public Button getRemoveTaskButton() {
        return removeTaskButton;
    }

    public void setRemoveTaskButton(Button removeTaskButton) {
        this.removeTaskButton = removeTaskButton;
    }

    public Button getRemoveWeekPlanButton() {
        return removeWeekPlanButton;
    }

    public void setRemoveWeekPlanButton(Button removeWeekPlanButton) {
        this.removeWeekPlanButton = removeWeekPlanButton;
    }

    public Button getSaveAndExitButton() {
        return saveAndExitButton;
    }

    public void setSaveAndExitButton(Button saveAndExitButton) {
        this.saveAndExitButton = saveAndExitButton;
    }

    public HBox getTasksHBox() {
        return tasksHBox;
    }

    public void setTasksHBox(HBox tasksHBox) {
        this.tasksHBox = tasksHBox;
    }

    public VBox getWeekPlanVBox() {
        return weekPlanVBox;
    }

    public void setWeekPlanVBox(VBox weekPlanVBox) {
        this.weekPlanVBox = weekPlanVBox;
    }

    public HBox getPatientsVBox() {
        return patientsVBox;
    }

    public void setPatientsVBox(HBox patientsVBox) {
        this.patientsVBox = patientsVBox;
    }

    public HBox getWeekPlanHBox() {
        return weekPlanHBox;
    }

    public void setWeekPlanHBox(HBox weekPlanHBox) {
        this.weekPlanHBox = weekPlanHBox;
    }

    public Button getAddPatientButton() {
        return addPatientButton;
    }

    public void setAddPatientButton(Button addPatientButton) {
        this.addPatientButton = addPatientButton;
    }

    public Button getNewWeekPlanButton() {
        return newWeekPlanButton;
    }

    public void setNewWeekPlanButton(Button newWeekPlanButton) {
        this.newWeekPlanButton = newWeekPlanButton;
    }

    public Button getRemovePatientButton() {
        return removePatientButton;
    }

    public void setRemovePatientButton(Button removePatientButton) {
        this.removePatientButton = removePatientButton;
    }


    private Adaptor(){

    }
    public static void setInstance(Adaptor instance){
        Adaptor.instance = instance;
    }
    public static Adaptor getInstance(){
        if(instance == null)
            instance = new Adaptor();
        return instance;
    }

    public String getAddPatientName() {
        return addPatientName;
    }

    public void setAddPatientName(String addPatientName) {
        this.addPatientName = addPatientName;
    }


    public Patient getPatientSelected() {
        return patientSelected;
    }

    public void setPatientSelected(Patient patientSelected) {
        this.patientSelected = patientSelected;
    }

    private Patient patientSelected = new Patient();
    private String addPatientName = "";

    public ToggleGroup getTogglePatients() {
        return togglePatients;
    }

    public void setTogglePatients(ToggleGroup togglePatients) {
        this.togglePatients = togglePatients;
    }

    public ToggleGroup getToggleDays() {
        return toggleDays;
    }

    public void setToggleDays(ToggleGroup toggleDays) {
        this.toggleDays = toggleDays;
    }

    private ToggleGroup togglePatients;
    private ToggleGroup toggleDays;
    private ToggleGroup toggleWeekPlans;

    public WeekPlan getWeekPlanSelected() {
        return weekPlanSelected;
    }

    public void setWeekPlanSelected(WeekPlan weekPlanSelected) {
        this.weekPlanSelected = weekPlanSelected;
    }

    private WeekPlan weekPlanSelected = new WeekPlan();

    public LocalDate getSelectedSDate() {
        return selectedSDate;
    }

    public void setSelectedSDate(LocalDate selectedSDate) {
        this.selectedSDate = selectedSDate;
    }

    private LocalDate selectedSDate;

    public Day getDaySelected() {
        return daySelected;
    }

    public void setDaySelected(Day daySelected) {
        this.daySelected = daySelected;
    }

    private Day daySelected = new Day();

    public DatePicker getStartDatePicker() {
        return startDatePicker;
    }

    public void setStartDatePicker(DatePicker startDatePicker) {
        startDatePicker.setDayCellFactory(dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if(patientSelected.plans.size() == 0){
                    if(item.getDayOfWeek() != DayOfWeek.MONDAY
                            || ((today == item.getDayOfMonth())&& month == item.getMonthValue() && year == item.getYear())
                            || item.isBefore(local)){
                        setDisable(true);
                    }
                } else {
                    for (int h = 0; h < patientSelected.plans.size(); h++) {
                        String[] sdays = patientSelected.plans.get(h).getWeekPlanSDate().split("/");
                        if (((sdays[0].equals(item.getDayOfMonth() + "")) && (sdays[1].equals(item.getMonthValue() + "")) && (sdays[2].equals(item.getYear() + "")))
                                || item.getDayOfWeek() != DayOfWeek.MONDAY
                                || ((today == item.getDayOfMonth()) && month == item.getMonthValue() && year == item.getYear())
                                || item.isBefore(local)) {
                            setDisable(true);
                        }
                    }
                }
                //setDisable(empty || item.getDayOfWeek() != DayOfWeek.MONDAY);
            }
        });
        this.startDatePicker = startDatePicker;
    }

    private DatePicker startDatePicker = new DatePicker();


    public int getsDaySelected() {
        return sDaySelected;
    }

    public void setsDaySelected(int sDaySelected) {
        this.sDaySelected = sDaySelected;
    }

    public int getsMonthSelected() {
        return sMonthSelected;
    }

    public void setsMonthSelected(int sMonthSelected) {
        this.sMonthSelected = sMonthSelected;
    }

    public int getsYearSelected() {
        return sYearSelected;
    }

    public void setsYearSelected(int sYearSelected) {
        this.sYearSelected = sYearSelected;
    }

    public String getNewWPName() {
        return newWPName;
    }

    public void setNewWPName(String newWPName) {
        this.newWPName = newWPName;
    }

    private String newWPName;
    private int sDaySelected, sMonthSelected, sYearSelected;

    public String getRemoveTaskName() {
        return removeTaskName;
    }

    public void setRemoveTaskName(String removeTaskName) {
        this.removeTaskName = removeTaskName;
    }

    private String removeTaskName;

    public String getRemoveWplanName() {
        return removeWplanName;
    }

    public void setRemoveWplanName(String removeWplanName) {
        this.removeWplanName = removeWplanName;
    }

    private String removeWplanName;

    public String getRemoveTName() {
        return removeTName;
    }

    public void setRemoveTName(String removeTName) {
        this.removeTName = removeTName;
    }

    private String removeTName;

    public String getAddPatientFile() {
        return addPatientFile;
    }

    public void setAddPatientFile(String addPatientFile) {
        this.addPatientFile = addPatientFile;
    }

    private String addPatientFile = "";


    private String selectedPName = "";

    public int[] getDaySelectedArray() {
        return daySelectedArray;
    }

    public void setDaySelectedArray(int[] daySelectedArray) {
        this.daySelectedArray = daySelectedArray;
    }

    public String getTaskSelectedName() {
        return taskSelectedName;
    }

    public void setTaskSelectedName(String taskSelectedName) {
        this.taskSelectedName = taskSelectedName;
    }

    /////////////////////////////////////////
    private String taskSelectedName = "";

    public Button getEditTaskbtn() {
        return editTaskbtn;
    }

    public void setEditTaskbtn(Button editTaskbtn) {
        this.editTaskbtn = editTaskbtn;
    }

    private Button editTaskbtn = new Button();
    //////////////////////////////////////////////
    private int[] daySelectedArray = new int[288];
    //////////////////////////////////////////////
    private String addTName = "";
    private String addTDescription = "";
    private String addTSHour = "";
    private String addTSMin = "";
    private String addTEHour = "";
    private String addTEMin = "";

    public ArrayList<Program> getAddTPrograms() {
        return addTPrograms;
    }

    public void setAddTPrograms(ArrayList<Program> addTPrograms) {
        this.addTPrograms = addTPrograms;
    }

    public String getAddTName() {
        return addTName;
    }

    public void setAddTName(String addTName) {
        this.addTName = addTName;
    }

    public String getAddTSHour() {
        return addTSHour;
    }

    public void setAddTSHour(String addTSHour) {
        this.addTSHour = addTSHour;
    }

    public String getAddTDescription() {
        return addTDescription;
    }

    public void setAddTDescription(String addTDescription) {
        this.addTDescription = addTDescription;
    }

    public String getAddTEHour() {
        return addTEHour;
    }

    public void setAddTEHour(String addTEHour) {
        this.addTEHour = addTEHour;
    }

    public String getAddTSMin() {
        return addTSMin;
    }

    public void setAddTSMin(String addTSMin) {
        this.addTSMin = addTSMin;
    }

    public String getAddTEMin() {
        return addTEMin;
    }

    public void setAddTEMin(String addTEMin) {
        this.addTEMin = addTEMin;
    }

    private ArrayList<Program> addTPrograms = new ArrayList<>();
    //////////////////////////////////////////////

    public String getSelectedPName() {
        return selectedPName;
    }

    public void setSelectedPName(String selectedPName) {
        this.selectedPName = selectedPName;
    }

    public void addPatientsToVBox(){
        int depth = 35;
        borderGlow.setOffsetY(0f);
        borderGlow.setOffsetX(0f);
        borderGlow.setColor(Color.WHITE);
        borderGlow.setWidth(depth);
        borderGlow.setHeight(depth);
        patientsVBox.getChildren().clear();

        for(int x = 0; x < coach.patients.size(); x++){
            ToggleButton patientName = new ToggleButton(coach.patients.get(x).getPatientName());
            coach.patients.get(x).setPatientID(x+1);  

            patientName.setUserData(coach.patients.get(x).getPatientName());
            patientName.setTextAlignment(TextAlignment.CENTER);
            patientName.setAlignment(Pos.CENTER);
            patientName.setFont(new javafx.scene.text.Font("Copperplate Gothic Bold", 12));
            patientName.setStyle("-fx-background-color: #103F66; " +
            "-fx-border-width: 2px; " +
            "-fx-border-color: transparent; " + 
            "-fx-text-fill: white; " +
            "-fx-background-radius: 50%; " + 
            "-fx-min-height: 100px; " + 
            "-fx-min-width: 100px; " + 
            "-fx-pref-height: 100px; " +
            "-fx-pref-width: 100px; " +
            "-fx-max-height: 100px; " +
            "-fx-max-width: 100px;");
            patientName.setContentDisplay(ContentDisplay.CENTER);
            patientName.setPrefWidth(Control.USE_COMPUTED_SIZE);
            patientName.setPrefSize(155, 100);
            patientName.setMinSize(155, 100);
            patientName.setMaxSize(155, 100);
            patientName.setToggleGroup(togglePatients);
            patientName.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (newValue) {
                        patientName.setStyle("-fx-background-color: #006400; "  + 
                                             "-fx-border-width: 2px; " +
                                             "-fx-border-color: transparent; " + 
                                             "-fx-text-fill: white; " +
                                             "-fx-background-radius: 50%; " + 
                                             "-fx-min-height: 100px; " + 
                                             "-fx-min-width: 100px; " + 
                                             "-fx-pref-height: 100px; " +
                                             "-fx-pref-width: 100px; " +
                                             "-fx-max-height: 100px; " +
                                             "-fx-max-width: 100px;");
                    } else {
                        patientName.setStyle("-fx-background-color: #103F66; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-color: transparent; " + 
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 50%; " + 
                        "-fx-min-height: 100px; " + 
                        "-fx-min-width: 100px; " + 
                        "-fx-pref-height: 100px; " +
                        "-fx-pref-width: 100px; " +
                        "-fx-max-height: 100px; " +
                        "-fx-max-width: 100px;");
                    }
                }
            });
            patientName.setOnMouseEntered(event -> {
                patientName.setEffect(borderGlow);
            });
            patientName.setOnMouseExited(event -> {
                patientName.setEffect(null);
            });

            patientsVBox.setStyle("-fx-padding: 10 0 0 10;");
            patientsVBox.setBackground(new Background(new BackgroundFill(Color.web("#bebeb6"), CornerRadii.EMPTY, Insets.EMPTY)));
            patientsVBox.setSpacing(10);
            patientsVBox.getChildren().add(patientName);
          
        }
        ObservableList<Node> patients = patientsVBox.getChildren();
        for (Node x: patients) {
            x.setOnMouseEntered(event -> {
                x.setEffect(borderGlow);
            });
            x.setOnMouseExited(event -> {
                x.setEffect(null);
            });
        }
    }
}
