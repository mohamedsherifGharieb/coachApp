package sample.WeekPlan;

public class Program extends Task{
	
	public String BaseName;
	
	public Program(){
		this.BaseName = "";
	}
	public Program(String pName){
		this.BaseName = pName;
	}
	public String getBaseName(){
		return this.BaseName;
	}
	public void setBaseName(String name){
		this.BaseName = name;
	}
}
