package sample.WeekPlan;

import java.util.ArrayList;

/**
 * Created by Johnny Bishara on 28/03/2016.
 */
public class Coach {

    public String coachName;
    public int coachID;
    public ArrayList<Patient> patients;

    public Coach(){
        this.patients = new ArrayList<Patient>();
    }

    public ArrayList<Patient> getPatients() {
        return patients;
    }

    public void setPatients(ArrayList<Patient> patients) {
        this.patients = patients;
    }

    public int getCoachID() {
        return coachID;
    }

    public void setCoachID(int coachID) {
        this.coachID = coachID;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public Patient getPatientByName(String pName) {
        for (int x = 0; x < this.patients.size(); x++){
            if (this.patients.get(x).getPatientName().equals(pName)){
                System.out.println(this.patients.get(x).getPatientName() + " <<<< found patient");
                return this.patients.get(x);
            }
        }
        System.out.println("null <<<< didn't find patient");
        return null;
    }
    public int getPatientIndexByName(String pName){
        for (int x = 0; x < this.patients.size(); x++){
            if (this.patients.get(x).getPatientName().equals(pName)){
                System.out.println(this.patients.get(x).getPatientName() + " <<<< found patient");
                return x;
            }
        }
        System.out.println("null <<<< didn't find patient");
        return 99999999;
    }

}
