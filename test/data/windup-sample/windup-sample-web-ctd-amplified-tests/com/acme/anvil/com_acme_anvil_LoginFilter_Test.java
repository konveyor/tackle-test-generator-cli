package com.acme.anvil;

import org.junit.runners.MethodSorters;
import javax.servlet.http.HttpSession;
import javax.servlet.FilterChain;
import static org.junit.Assert.assertNull;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import org.junit.runner.RunWith;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.servlet.http.HttpServletRequest;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import org.evosuite.runtime.EvoRunnerParameters;
import com.acme.anvil.LoginFilter;
import org.evosuite.runtime.EvoRunner;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletResponseWrapper;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import static org.junit.Assert.assertEquals;
import org.junit.FixMethodOrder;
import static org.junit.Assert.fail;

public class com_acme_anvil_LoginFilter_Test {
	@Test
	public void test_init_0() throws Throwable {
		com.acme.anvil.LoginFilter loginFilter0 = new com.acme.anvil.LoginFilter();
		javax.servlet.FilterConfig filterConfig1 = null;
		loginFilter0.init(filterConfig1);
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
