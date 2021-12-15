package irs;

import org.junit.runners.MethodSorters;
import static org.junit.Assert.assertNull;
import irs.Employer;
import irs.BusinessProcess;
import org.junit.runner.RunWith;
import irs.Employee;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.EvoRunner;
import static org.junit.Assert.assertEquals;
import org.junit.FixMethodOrder;

public class irs_BusinessProcess_Test {
	@Test
	public void test_main_0() throws Throwable {
		java.lang.String str0 = null;
		java.lang.String[] strArray1 = new java.lang.String[] { str0 };
		irs.BusinessProcess.main(strArray1);
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
