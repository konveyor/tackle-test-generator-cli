package irs;

public class Employee {
	
	private int employeeId;
	private String name;
	private int hours;
	private double rate;
	private String level = "XYZ";

// 	static {
// 		level = "XYZ";
// 		System.out.println("In Employee static block: level "+level);
// 	}
	
	public String getLevel() {
		System.out.println("In Employee static getLevel: level "+level);
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
		System.out.println("In Employee static setLevel: level "+level);
	}
	
	public Employee() {}
	
	public Employee(int employeeId, String name, int hours, double rate){
		this.employeeId = employeeId;
		this.name = name;
		this.hours = hours;
		this.rate = rate;
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public int getEmployeeId() {
		return employeeId;
	}

    public void setEmployeeId(int employeeId) { 
		this.employeeId = employeeId;
    }

    public String getName() {
		return name;
    }

    public void setName(String name) {
		this.name = name;
    }
	
}
