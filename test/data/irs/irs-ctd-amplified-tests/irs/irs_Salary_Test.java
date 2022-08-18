package irs;

import org.junit.runners.MethodSorters;
import static org.junit.Assert.assertNull;
import irs.Salary;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.EvoRunner;
import org.junit.runner.RunWith;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;

public class irs_Salary_Test {
	@Test
	public void test_setSalary_0() throws Throwable {
		irs.Salary salary0 = new irs.Salary();
		assertEquals(0, ((irs.Salary) salary0).getEmployerId());
		assertEquals(0, ((irs.Salary) salary0).getEmployeeId());
		assertEquals(0.0, (double) ((irs.Salary) salary0).getSalary(), 0.015);
		salary0.setSalary((double)(-479));
	}

	@Test
	public void test_setEmployerId_0() throws Throwable {
		irs.Salary salary0 = new irs.Salary();
		assertEquals(0, ((irs.Salary) salary0).getEmployerId());
		assertEquals(0, ((irs.Salary) salary0).getEmployeeId());
		assertEquals(0.0, (double) ((irs.Salary) salary0).getSalary(), 0.015);
		salary0.setEmployerId(0);
	}

	@Test
	public void test_setEmployeeId_0() throws Throwable {
		irs.Salary salary0 = new irs.Salary();
		assertEquals(0, ((irs.Salary) salary0).getEmployerId());
		assertEquals(0, ((irs.Salary) salary0).getEmployeeId());
		assertEquals(0.0, (double) ((irs.Salary) salary0).getSalary(), 0.015);
		salary0.setEmployeeId((-479));
	}

	@Test
	public void test_Salary_0() throws Throwable {
		irs.Salary salary3 = new irs.Salary(1, (-1), (double)100.0f);
		assertEquals(1, ((irs.Salary) salary3).getEmployerId());
		assertEquals(-1, ((irs.Salary) salary3).getEmployeeId());
		assertEquals(100.0, (double) ((irs.Salary) salary3).getSalary(), 0.015);
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
