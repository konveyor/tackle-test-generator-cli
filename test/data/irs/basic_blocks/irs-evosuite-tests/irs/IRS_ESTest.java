/*
 * This file was automatically generated by EvoSuite
 * Mon Jan 10 10:46:10 GMT 2022
 */

package irs;

import org.junit.Test;
import static org.junit.Assert.*;
import irs.IRS;
import irs.Salary;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true, useJEE = true) 
public class IRS_ESTest extends IRS_ESTest_scaffolding {

  @Test(timeout = 4000)
  public void test0()  throws Throwable  {
      IRS iRS0 = new IRS();
      LinkedList<Salary> linkedList0 = new LinkedList<Salary>();
      Salary salary0 = new Salary((-1), 1569, 1569);
      boolean boolean0 = linkedList0.add(salary0);
      iRS0.setSalaryList(linkedList0);
      Salary salary1 = iRS0.getSalaryfromIRS((-424));
  }

  @Test(timeout = 4000)
  public void test1()  throws Throwable  {
      IRS iRS0 = new IRS();
      LinkedList<Salary> linkedList0 = new LinkedList<Salary>();
      iRS0.setSalaryList(linkedList0);
      Salary salary0 = new Salary(0, 0, (-59.15059140257));
      boolean boolean0 = linkedList0.add(salary0);
      Salary salary1 = iRS0.getSalaryfromIRS(0);
  }

  @Test(timeout = 4000)
  public void test2()  throws Throwable  {
      IRS iRS0 = new IRS();
      LinkedList<Salary> linkedList0 = new LinkedList<Salary>();
      Salary salary0 = new Salary(0, 0, (-59.15059140257));
      boolean boolean0 = linkedList0.add(salary0);
      iRS0.setSalaryList(linkedList0);
      iRS0.setSalaryList(linkedList0);
  }

  @Test(timeout = 4000)
  public void test3()  throws Throwable  {
      IRS iRS0 = new IRS();
      Map<Integer, Set<Salary>> map0 = iRS0.getAllSalarySets();
  }

  @Test(timeout = 4000)
  public void test4()  throws Throwable  {
      IRS iRS0 = new IRS();
      Set<Salary> set0 = iRS0.getSalarySet(0);
  }

  @Test(timeout = 4000)
  public void test5()  throws Throwable  {
      IRS iRS0 = new IRS();
      List<Salary> list0 = iRS0.getSalaryList();
  }

  @Test(timeout = 4000)
  public void test6()  throws Throwable  {
      IRS iRS0 = new IRS();
      int int0 = iRS0.getYear();
  }

  @Test(timeout = 4000)
  public void test7()  throws Throwable  {
      IRS iRS0 = new IRS();
      iRS0.setYear(613);
  }

  @Test(timeout = 4000)
  public void test8()  throws Throwable  {
      IRS iRS0 = new IRS();
      HashMap<Integer, Set<Salary>> hashMap0 = new HashMap<Integer, Set<Salary>>();
      iRS0.setAllSalarySets(hashMap0);
  }
}
