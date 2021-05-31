package irs;

import com.ibm.cardinal.util.KluInterface;

import java.util.ArrayList;
import java.util.List;

public class Employer implements KluInterface {
    //Cardinal generated attributes
    private String klu__referenceID = "";

    //Cardinal generated method
    public String getKlu__referenceID() {
        return klu__referenceID;
    }

    //Cardinal generated method
    public void setKlu__referenceID(String refID) {
        klu__referenceID = refID;
    }




	private int employerId;
	String employerName;
	private List<Employee> employees;
	
	public Employer() {
		employees = new ArrayList<>();
	}
	   
	protected Employer(int employerId, String employerName, List<Employee> employees){
		this.employerId = employerId;
		this.employerName = employerName;
		this.employees = employees;
	}

	protected int getEmployerId() {
		return employerId;
	}

	protected void setEmployerId(int employerId) {
		this.employerId = employerId;
	}

	protected void setEmployerAttributes(int employerId, String employerName, List<Employee> employees) {
		this.employerId = employerId;
		this.employerName = employerName;
		this.employees = employees;
	}

	public void addEmployees(Employee... emps) {
		for (Employee emp : emps) {
			this.employees.add(emp);
		}
	}

	public String getEmployerName() {
		return employerName;
	}

	public void setEmployerName(String employerName) {
		this.employerName = employerName;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

}

