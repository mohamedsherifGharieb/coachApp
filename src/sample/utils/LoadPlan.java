package sample.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import sample.WeekPlan.Coach;
import sample.WeekPlan.Day;
import sample.WeekPlan.Patient;
import sample.WeekPlan.Program;
import sample.WeekPlan.Task;
import sample.WeekPlan.WeekPlan;

/**
 * Created by Johnny Bishara on 19/03/2016.
 */
public class LoadPlan extends Coach {
    public static ArrayList<WeekPlan> parse(Patient patient, String file){
        WeekPlan wPlan;
        ArrayList<WeekPlan> weekPlans = new ArrayList<WeekPlan>();
       
//     
        JSONParser parser = new JSONParser();
        try {
            System.out.println("Reading JSON file from Java program");
            JSONObject json = (JSONObject) parser.parse(file);
         
            if( json != null && !json.isEmpty()) {
                JSONArray plans = (JSONArray) json.get("plans");
                System.out.println("LPC The number of plans is: " + plans.size());
                Iterator<JSONObject> iterator = plans.iterator();
                int size = plans.size();
              //  while (iterator.hasNext()) {
                for(int i = 0; i <size;i++){
                    JSONObject jsonweek = (JSONObject) iterator.next();
                    wPlan = new WeekPlan();
                    String weekPlanName = (String) jsonweek.get("weekPlanName");
                    wPlan.setWeekPlanName(weekPlanName);
                    String weekPlanID = (String) jsonweek.get("weekPlanID");
                    wPlan.setWeekPlanID(Integer.parseInt(weekPlanID));
                    String weekPlanSDate = (String) jsonweek.get("weekPlanSDate");
                    wPlan.setWeekPlanSDate(weekPlanSDate);
                    String weekPlanEDate = (String) jsonweek.get("weekPlanEDate");
                    System.out.println(weekPlanEDate + "    null??");
                    wPlan.setWeekPlanEDate(weekPlanEDate);
                    JSONArray weekPlan = (JSONArray) jsonweek.get("weekPlan");
                    ArrayList<Day> Days = new ArrayList<Day>();
                    Iterator<JSONObject> iterator1 = weekPlan.iterator();
                    while (iterator1.hasNext()) {
                        JSONObject factObj = (JSONObject) iterator1.next();
                        ArrayList<Task> dayTasks = new ArrayList<Task>();
                        Day day = new Day();
                        String dayName = (String) factObj.get("dayName");
                        day.setDayName(dayName);
                        String dayID = (String) factObj.get("dayID");
                        day.setID(dayID);
                        System.out.println(dayName);
                        String dayProgress = (String) factObj.get("dayProgress");
                        day.setDayProgress(Double.parseDouble(dayProgress));
                        String totalTasksDration = (String) factObj.get("totalTasksDration");
                        day.setTotalTasksDuration(Integer.parseInt(totalTasksDration));
                        String numberOfTasks = (String) factObj.get("numberOfTasks");
                        day.setNumberOfTasks(Integer.parseInt(numberOfTasks));
                        String[] dTimeSlots = ((String) factObj.get("timeSlots")).split(",");
                        if(!((String)factObj.get("timeSlots")).equals("")) {
                            for (int x = 0; x < dTimeSlots.length; x++) {
                                day.getTimeSlots()[x] = Integer.parseInt(dTimeSlots[x]);
                            }
                        }

                        //System.out.println(day.getDayID());
                        JSONArray tasks = (JSONArray) factObj.get("tasks");
                        Iterator<JSONObject> iterator2 = tasks.iterator();
                        while (iterator2.hasNext()) {
                            ArrayList<Program> Programs = new ArrayList<Program>();
                            Task t = new Task();
                            JSONObject factObj2 = (JSONObject) iterator2.next();
                            String taskID = (String) factObj2.get("taskID");
                            t.setTaskID(taskID);
                            //System.out.println(t.taskID);
                            String taskName = (String) factObj2.get("taskName");
                            t.setTaskName(taskName);
                            //System.out.println(t.getTaskName());
                            String startTime = (String) factObj2.get("startTime");
                            t.setStartTime(startTime);
                            //System.out.println(t.startTimeH + ":" + t.startTimeM);
                            String endTime = (String) factObj2.get("endTime");
                            t.setEndTime(endTime);
                            //System.out.println(t.endTimeH + ":" + t.endTimeM);
                            //String finished = (String) factObj2.get("finished");
                            //t.setFinished(finished);
                            String description = (String) factObj2.get("description");
                            t.setTaskDescription(description);
                            String taskDuration = (String) factObj2.get("taskDuration");
                            t.setDurationInMinutes(Integer.parseInt(taskDuration));
                            String taskProgress = (String) factObj2.get("taskProgress");
                            t.setTaskProgress(Integer.parseInt(taskProgress));
                            String taskReview = (String) factObj2.get("taskReview");
                            t.setTaskReveiw(taskReview);
                            //                    String startTimeH = (String) factObj2.get("startTimeH");
                            //                    t.setStartTimeH(Integer.parseInt(startTimeH));
                            //                    String startTimeM = (String) factObj2.get("startTimeM");
                            //                    t.setStartTimeM(Integer.parseInt(startTimeM));
                            //                    String endTimeH = (String) factObj2.get("endTimeH");
                            //                    t.setEndTimeH(Integer.parseInt(endTimeH));
                            //                    String endTimeM = (String) factObj2.get("endTimeM");
                            //                    t.setEndTimeM(Integer.parseInt(startTimeH));
                            String submitted = (String) factObj2.get("submitted");
                            t.setSubmitted(Boolean.parseBoolean(submitted));
                            String percentageOfDay = (String) factObj2.get("percentageOfDay");
                            t.setPercentageOfDay(Double.parseDouble(percentageOfDay));
                            String status = (String) factObj2.get("status");
                            if(status.length() < 15 ){
                                status = "Missed";
                            }
                            else{
                            	
                                String[] statusTrimmed = status.split("%");
                                System.out.println("status"+status);
                                status = statusTrimmed[0] + "% " 
                                + statusTrimmed[1] ;
                            }

                            t.setStatus(status);
                            String submittedPercentage = (String) factObj2.get("submittedPercentage");
                            t.setSubmittedPercentage(Double.parseDouble(submittedPercentage));
                            //System.out.println(t.submitted);
                            JSONArray programs = (JSONArray) factObj2.get("programs");
                            Iterator<JSONObject> iterator3 = programs.iterator();
                            while (iterator3.hasNext()) {
                                JSONObject factObj3 = (JSONObject) iterator3.next();
                                String baseName = (String) factObj3.get("baseName");
                                Program p = new Program();
                                p.setBaseName(baseName);
                                //System.out.println(p.BaseName);
                                Programs.add(p);
                            }
                            t.programs = Programs;
                            dayTasks.add(t);


                        }

                        day.setDayTasks(dayTasks);
                        Days.add(day);
                    }
                    wPlan.setDays(Days);
                    System.out.println(wPlan.getDay(2).getTasks().size() + "........day tasks size!!!!!!!!!!!!!!!!!!!!!");
                    //Setting Task Duration for each Task
                    //int numOfTasks = wPlan.getDay(0).getTasks().size();
                    int totalDuration = 0;
                    int durationInMinutes = 0;
                    int nOm,nOh;

                    //Setting total duration of tasks in a day
                    for (int v = 0; v < wPlan.getDays().size(); v++){
                        if(wPlan.getDay(v).getTasks() != null) {
                            int total = 0;
                            for (int u = 0; u < wPlan.getDay(v).getTasks().size(); u++) {
                                total += wPlan.getDay(v).getTask(u).getDurationInMinutes();
                            }
                            wPlan.getDay(v).setTotalTasksDuration(total);
                            System.out.println(total + "...............Total tasks duration in day");
                        }
                        //
                    }

                    //Setting task percentage of day
                    for(int q = 0; q < wPlan.getDays().size(); q++){
                        if(wPlan.getDay(q).getTasks() != null) {
                            for (int k = 0; k < wPlan.getDay(q).getTasks().size(); k++) {
                                wPlan.getDay(q).getTask(k).setPercentageOfDay((double) (wPlan.getDay(q).getTask(k).getDurationInMinutes() * 100) / wPlan.getDay(q).getTotalTasksDuration());
                                //System.out.println(wPlan.getDay(q).getTask(k).getPercentageOfDay());
                            }
                        }
                    }
                    weekPlans.add(wPlan);
                }
                patient.setPlans(weekPlans);
                

            }
            System.out.println(patient.toString() + "\n"+"~~~~~~~~~~~~~~~~~~`");
        }
        catch(RuntimeException e){
        	throw e;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
       
        return weekPlans;
    }
   
}
