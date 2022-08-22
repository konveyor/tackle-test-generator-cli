package irs;

import org.junit.runners.MethodSorters;
import java.util.List;
import static org.junit.Assert.assertNull;
import irs.Employer;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.EvoRunner;
import org.junit.runner.RunWith;
import irs.Employee;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;

public class irs_Employer_Test {
	@Test
	public void test_setEmployerName_0() throws Throwable {
		irs.Employer employer0 = new irs.Employer();
		assertNull(((irs.Employer) employer0).getEmployerName());
		employer0.setEmployerName("n0M6;+L{H");
	}

	@Test
	public void test_setEmployees_0() throws Throwable {
		irs.Employer employer0 = new irs.Employer();
		assertNull(((irs.Employer) employer0).getEmployerName());
		java.util.ArrayList<irs.Employee> employeeList1 = new java.util.ArrayList<irs.Employee>();
		irs.Employee employee6 = new irs.Employee(10, "XYZ", 100, (double)10.0f);
		assertEquals("XYZ", ((irs.Employee) employee6).getLevel());
		assertEquals(100, ((irs.Employee) employee6).getHours());
		assertEquals(10.0, (double) ((irs.Employee) employee6).getRate(), 0.015);
		assertEquals("XYZ", ((irs.Employee) employee6).getName());
		assertEquals(10, ((irs.Employee) employee6).getEmployeeId());
		boolean boolean7 = employeeList1.add(employee6);
		assertEquals(true, boolean7);
		employer0.setEmployees((java.util.List<irs.Employee>)employeeList1);
	}

	@Test
	public void test_addEmployees_0() throws Throwable {
		irs.Employer employer0 = new irs.Employer();
		assertNull(((irs.Employer) employer0).getEmployerName());
		irs.Employee employee5 = new irs.Employee(10, "XYZ", 100, (double)10.0f);
		assertEquals("XYZ", ((irs.Employee) employee5).getLevel());
		assertEquals(100, ((irs.Employee) employee5).getHours());
		assertEquals(10.0, (double) ((irs.Employee) employee5).getRate(), 0.015);
		assertEquals("XYZ", ((irs.Employee) employee5).getName());
		assertEquals(10, ((irs.Employee) employee5).getEmployeeId());
		irs.Employee[] employeeArray6 = new irs.Employee[] { employee5 };
		employer0.addEmployees(employeeArray6);
	}

	@Test
	public void test_setEmployerAttributes_0() throws Throwable {
		irs.Employer employer0 = new irs.Employer();
		assertNull(((irs.Employer) employer0).getEmployerName());
		java.util.ArrayList<irs.Employee> employeeList3 = new java.util.ArrayList<irs.Employee>();
		irs.Employee employee8 = new irs.Employee(10, "XYZ", 100, (double)10.0f);
		assertEquals("XYZ", ((irs.Employee) employee8).getLevel());
		assertEquals(100, ((irs.Employee) employee8).getHours());
		assertEquals(10.0, (double) ((irs.Employee) employee8).getRate(), 0.015);
		assertEquals("XYZ", ((irs.Employee) employee8).getName());
		assertEquals(10, ((irs.Employee) employee8).getEmployeeId());
		boolean boolean9 = employeeList3.add(employee8);
		assertEquals(true, boolean9);
		employer0.setEmployerAttributes((-2062), "n0M6;+L{H", (java.util.List<irs.Employee>)employeeList3);
	}

	@Test
	public void test_setEmployerId_0() throws Throwable {
		irs.Employer employer0 = new irs.Employer();
		assertNull(((irs.Employer) employer0).getEmployerName());
		employer0.setEmployerId((-2062));
	}

	private java.lang.Object getFieldValue(java.lang.Object obj, String fieldName) throws java.lang.reflect.InvocationTargetException, java.lang.SecurityException, java.lang.IllegalArgumentException, java.lang.IllegalAccessException {
		try {
			java.lang.reflect.Field field = obj.getClass().getField(fieldName);
			return field.get(obj);
		} catch (java.lang.NoSuchFieldException e) {
			for (java.lang.reflect.Method publicMethod : obj.getClass().getMethods()) {
				if (publicMethod.getName().startsWith("get") && publicMethod.getParameterCount() == 0 && 
					publicMethod.getName().toLowerCase().equals("get"+fieldName.toLowerCase())) {
					return publicMethod.invoke(obj);
				}
			}
		}
		throw new IllegalArgumentException("Could not find field or getter "+fieldName+" for class "+obj.getClass().getName());
	}
}
