package irs;

import org.junit.runners.MethodSorters;
import static org.junit.Assert.assertNull;
import irs.Salary;
import irs.IRS;
import org.junit.runner.RunWith;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.EvoRunner;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import org.junit.FixMethodOrder;
import java.util.LinkedList;

public class irs_IRS_Test {
	@Test
	public void test_setAllSalarySets_0() throws Throwable {
		irs.IRS iRS0 = new irs.IRS();
		java.util.HashMap<java.lang.Integer,java.util.Set<irs.Salary>> intMap1 = new java.util.HashMap<java.lang.Integer,java.util.Set<irs.Salary>>();
		java.util.HashSet<irs.Salary> salarySet3 = new java.util.HashSet<irs.Salary>();
		irs.Salary salary7 = new irs.Salary(1, (-1), (double)100.0f);
		assertEquals(1, ((irs.Salary) salary7).getEmployerId());
		assertEquals(-1, ((irs.Salary) salary7).getEmployeeId());
		assertEquals(100.0, (double) ((irs.Salary) salary7).getSalary(), 0.015);
		boolean boolean8 = salarySet3.add(salary7);
		assertEquals(true, boolean8);
		java.util.Set<irs.Salary> salarySet9 = intMap1.put((-2062), (java.util.Set<irs.Salary>)salarySet3);
		iRS0.setAllSalarySets((java.util.Map<java.lang.Integer,java.util.Set<irs.Salary>>)intMap1);
	}

	@Test
	public void test_setSalaryList_0() throws Throwable {
		irs.IRS iRS0 = new irs.IRS();
		java.util.ArrayList<irs.Salary> salaryList1 = new java.util.ArrayList<irs.Salary>();
		irs.Salary salary5 = new irs.Salary(1, (-1), (double)100.0f);
		assertEquals(1, ((irs.Salary) salary5).getEmployerId());
		assertEquals(-1, ((irs.Salary) salary5).getEmployeeId());
		assertEquals(100.0, (double) ((irs.Salary) salary5).getSalary(), 0.015);
		boolean boolean6 = salaryList1.add(salary5);
		assertEquals(true, boolean6);
		iRS0.setSalaryList((java.util.List<irs.Salary>)salaryList1);
	}

	@Test
	public void test_getSalarySet_0() throws Throwable {
		irs.IRS iRS0 = new irs.IRS();
		java.util.Set<irs.Salary> salarySet2 = iRS0.getSalarySet(0);
	}

	@Test
	public void test_setYear_0() throws Throwable {
		irs.IRS iRS0 = new irs.IRS();
		iRS0.setYear(0);
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
