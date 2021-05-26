package irs;

public class Salary {

	private int employerId;
	private int employeeId;
	private double salary;
	    
	public Salary() {}
	
    public Salary(int employerId, int employeeId, double salary) {
		this.employerId = employerId;
		this.employeeId = employeeId;
		this.salary = salary;
	}
    
	public int getEmployerId() {
		return employerId;
	}

	public void setEmployerId(int employerId) {
		this.employerId = employerId;
	}

	public int getEmployeeId() {
		System.out.println("In Salary, before return employeeId");
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

}
