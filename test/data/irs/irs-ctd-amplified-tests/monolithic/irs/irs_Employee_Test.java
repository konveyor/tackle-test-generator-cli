package irs;

import org.junit.runners.MethodSorters;
import static org.junit.Assert.assertNull;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.EvoRunner;
import org.junit.runner.RunWith;
import irs.Employee;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;

public class irs_Employee_Test {
	@Test
	public void test_Employee_0() throws Throwable {
		irs.Employee employee4 = new irs.Employee(10, "XYZ", 100, (double)10.0f);
		assertEquals("XYZ", ((irs.Employee) employee4).getLevel());
		assertEquals(100, ((irs.Employee) employee4).getHours());
		assertEquals(10.0, (double) ((irs.Employee) employee4).getRate(), 0.015);
		assertEquals("XYZ", ((irs.Employee) employee4).getName());
		assertEquals(10, ((irs.Employee) employee4).getEmployeeId());
	}

	@Test
	public void test_setHours_0() throws Throwable {
		irs.Employee employee0 = new irs.Employee();
		assertEquals("XYZ", ((irs.Employee) employee0).getLevel());
		assertEquals(0, ((irs.Employee) employee0).getHours());
		assertEquals(0.0, (double) ((irs.Employee) employee0).getRate(), 0.015);
		assertNull(((irs.Employee) employee0).getName());
		assertEquals(0, ((irs.Employee) employee0).getEmployeeId());
		double double1 = employee0.getRate();
		assertEquals(0.0, (java.lang.Double) double1, 0.015);
		employee0.setHours(100);
	}

	@Test
	public void test_setRate_0() throws Throwable {
		irs.Employee employee0 = new irs.Employee();
		assertEquals("XYZ", ((irs.Employee) employee0).getLevel());
		assertEquals(0, ((irs.Employee) employee0).getHours());
		assertEquals(0.0, (double) ((irs.Employee) employee0).getRate(), 0.015);
		assertNull(((irs.Employee) employee0).getName());
		assertEquals(0, ((irs.Employee) employee0).getEmployeeId());
		java.lang.String str1 = employee0.getName();
		employee0.setRate((-1.0d));
	}

	@Test
	public void test_setEmployeeId_0() throws Throwable {
		irs.Employee employee0 = new irs.Employee();
		assertEquals("XYZ", ((irs.Employee) employee0).getLevel());
		assertEquals(0, ((irs.Employee) employee0).getHours());
		assertEquals(0.0, (double) ((irs.Employee) employee0).getRate(), 0.015);
		assertNull(((irs.Employee) employee0).getName());
		assertEquals(0, ((irs.Employee) employee0).getEmployeeId());
		employee0.setEmployeeId(1);
	}

	@Test
	public void test_setName_0() throws Throwable {
		irs.Employee employee0 = new irs.Employee();
		assertEquals("XYZ", ((irs.Employee) employee0).getLevel());
		assertEquals(0, ((irs.Employee) employee0).getHours());
		assertEquals(0.0, (double) ((irs.Employee) employee0).getRate(), 0.015);
		assertNull(((irs.Employee) employee0).getName());
		assertEquals(0, ((irs.Employee) employee0).getEmployeeId());
		employee0.setName("hi!");
	}

	@Test
	public void test_setLevel_0() throws Throwable {
		irs.Employee employee4 = new irs.Employee(1, "", 1, (double)1);
		assertEquals("XYZ", ((irs.Employee) employee4).getLevel());
		assertEquals(1, ((irs.Employee) employee4).getHours());
		assertEquals(1.0, (double) ((irs.Employee) employee4).getRate(), 0.015);
		assertEquals("", ((irs.Employee) employee4).getName());
		assertEquals(1, ((irs.Employee) employee4).getEmployeeId());
		employee4.setLevel("");
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
