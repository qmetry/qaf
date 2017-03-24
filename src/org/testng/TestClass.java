/*******************************************************************************
 * QMetry Automation Framework provides a powerful and versatile platform to author 
 * Automated Test Cases in Behavior Driven, Keyword Driven or Code Driven approach
 *                
 * Copyright 2016 Infostretch Corporation
 *
 * THIS IS A MODIFIED SOURCE VERSION OF ORIGINAL TESTNG COMPONENT (REFER LICENSING TERMS FOR TESTNG: http://www.apache.org/licenses/LICENSE-2.0.html
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT
 * OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE
 *
 * You should have received a copy of the GNU General Public License along with this program in the name of LICENSE.txt in the root folder of the distribution. If not, see https://opensource.org/licenses/gpl-3.0.html
 *
 * See the NOTICE.TXT file in root folder of this source files distribution 
 * for additional information regarding copyright ownership and licenses
 * of other open source software / files used by QMetry Automation Framework.
 *
 * For any inquiry or need additional information, please contact support-qaf@infostretch.com
 *******************************************************************************/
package org.testng;

import java.util.List;

import org.testng.collections.Lists;
import org.testng.collections.Objects;
import org.testng.internal.ConfigurationMethod;
import org.testng.internal.ConstructorOrMethod;
import org.testng.internal.NoOpTestClass;
import org.testng.internal.RunInfo;
import org.testng.internal.Utils;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlTest;

import com.qmetry.qaf.automation.step.client.TestNGScenario;

/**
 * This class represents a test class:
 * - The test methods
 * - The configuration methods (test and method)
 * - The class file
 *
 * @author <a href="mailto:cedric@beust.com">Cedric Beust</a>
 * @author <a href='mailto:the_mindstorm@evolva.ro'>Alexandru Popescu</a>
 */
class TestClass extends NoOpTestClass implements ITestClass {
	/* generated */
	private static final long serialVersionUID = -8077917128278361294L;
  transient private IAnnotationFinder m_annotationFinder = null;
  // The Strategy used to locate test methods (TestNG, JUnit, etc...)
  transient private ITestMethodFinder m_testMethodFinder = null;

  private IClass m_iClass = null;
  private RunInfo m_runInfo = null;
  private String m_testName;
  private XmlTest m_xmlTest;
  private XmlClass m_xmlClass;

  protected TestClass(IClass cls,
                   ITestMethodFinder testMethodFinder,
                   IAnnotationFinder annotationFinder,
                   RunInfo runInfo,
                   XmlTest xmlTest,
                   XmlClass xmlClass) {
    init(cls, testMethodFinder, annotationFinder, runInfo, xmlTest, xmlClass);
  }

  @Override
  public String getTestName() {
    return m_testName;
  }

  @Override
  public XmlTest getXmlTest() {
    return m_xmlTest;
  }

  @Override
  public XmlClass getXmlClass() {
    return m_xmlClass;
  }

  public IAnnotationFinder getAnnotationFinder() {
    return m_annotationFinder;
  }

  private void init(IClass cls,
                    ITestMethodFinder testMethodFinder,
                    IAnnotationFinder annotationFinder,
                    RunInfo runInfo,
                    XmlTest xmlTest,
                    XmlClass xmlClass)
  {
    log(3, "Creating TestClass for " + cls);
    m_iClass = cls;
    m_testClass = cls.getRealClass();
    m_xmlTest = xmlTest;
    m_xmlClass = xmlClass;
    m_runInfo = runInfo;
    m_testMethodFinder = testMethodFinder;
    m_annotationFinder = annotationFinder;
    initTestClassesAndInstances();
    initMethods();
  }

  private void initTestClassesAndInstances() {
    //
    // TestClasses and instances
    //
    Object[] instances = getInstances(false);
    for (Object instance : instances) {
      if (instance instanceof ITest) {
        m_testName = ((ITest) instance).getTestName();
        break;
      }
    }
    if (m_testName == null) {
      m_testName = m_iClass.getTestName();
    }
  }

  @Override
  public Object[] getInstances(boolean create) {
    return m_iClass.getInstances(create);
  }

  @Override
  public long[] getInstanceHashCodes() {
    return m_iClass.getInstanceHashCodes();
  }

  @Deprecated
  @Override
  public int getInstanceCount() {
    return m_iClass.getInstanceCount();
  }

  @Override
  public void addInstance(Object instance) {
    m_iClass.addInstance(instance);
  }

  private void initMethods() {
    ITestNGMethod[] methods = m_testMethodFinder.getTestMethods(m_testClass, m_xmlTest);
    m_testMethods = createTestMethods(methods);

    for (Object instance : m_iClass.getInstances(false)) {
      m_beforeSuiteMethods = ConfigurationMethod
          .createSuiteConfigurationMethods(m_testMethodFinder.getBeforeSuiteMethods(m_testClass),
                                           m_annotationFinder,
                                           true,
                                           instance);
      m_afterSuiteMethods = ConfigurationMethod
          .createSuiteConfigurationMethods(m_testMethodFinder.getAfterSuiteMethods(m_testClass),
                                           m_annotationFinder,
                                           false,
                                           instance);
      m_beforeTestConfMethods = ConfigurationMethod
          .createTestConfigurationMethods(m_testMethodFinder.getBeforeTestConfigurationMethods(m_testClass),
                                          m_annotationFinder,
                                          true,
                                          instance);
      m_afterTestConfMethods = ConfigurationMethod
          .createTestConfigurationMethods(m_testMethodFinder.getAfterTestConfigurationMethods(m_testClass),
                                          m_annotationFinder,
                                          false,
                                          instance);
      m_beforeClassMethods = ConfigurationMethod
          .createClassConfigurationMethods(m_testMethodFinder.getBeforeClassMethods(m_testClass),
                                           m_annotationFinder,
                                           true,
                                           instance);
      m_afterClassMethods = ConfigurationMethod
          .createClassConfigurationMethods(m_testMethodFinder.getAfterClassMethods(m_testClass),
                                           m_annotationFinder,
                                           false,
                                           instance);
      m_beforeGroupsMethods = ConfigurationMethod
          .createBeforeConfigurationMethods(m_testMethodFinder.getBeforeGroupsConfigurationMethods(m_testClass),
                                            m_annotationFinder,
                                            true,
                                            instance);
      m_afterGroupsMethods = ConfigurationMethod
          .createAfterConfigurationMethods(m_testMethodFinder.getAfterGroupsConfigurationMethods(m_testClass),
                                           m_annotationFinder,
                                           false,
                                           instance);
      m_beforeTestMethods = ConfigurationMethod
          .createTestMethodConfigurationMethods(m_testMethodFinder.getBeforeTestMethods(m_testClass),
                                                m_annotationFinder,
                                                true,
                                                instance);
      m_afterTestMethods = ConfigurationMethod
          .createTestMethodConfigurationMethods(m_testMethodFinder.getAfterTestMethods(m_testClass),
                                                m_annotationFinder,
                                                false,
                                                instance);
    }
  }

  /**
   * Create the test methods that belong to this class (rejects
   * all those that belong to a different class).
   */
  private ITestNGMethod[] createTestMethods(ITestNGMethod[] methods) {
    List<ITestNGMethod> vResult = Lists.newArrayList();
    for (ITestNGMethod tm : methods) {
      ConstructorOrMethod m = tm.getConstructorOrMethod();
      if (m.getDeclaringClass().isAssignableFrom(m_testClass)) {
        for (Object o : m_iClass.getInstances(false)) {
          log(4, "Adding method " + tm + " on TestClass " + m_testClass);
          vResult.add(new TestNGScenario(/* tm.getRealClass(), */ m.getMethod(), m_annotationFinder, m_xmlTest,
              o));
        }
      }
      else {
        log(4, "Rejecting method " + tm + " for TestClass " + m_testClass);
      }
    }

    ITestNGMethod[] result = vResult.toArray(new ITestNGMethod[vResult.size()]);
    return result;
  }

  private RunInfo getRunInfo() {
    return m_runInfo;
  }

  public ITestMethodFinder getTestMethodFinder() {
    return m_testMethodFinder;
  }

  private void log(int level, String s) {
    Utils.log("TestClass", level, s);
  }

  private static void ppp(String s) {
    System.out.println("[TestClass] " + s);
  }

  protected void dump() {
    System.out.println("===== Test class\n" + m_testClass.getName());
    for (ITestNGMethod m : m_beforeClassMethods) {
      System.out.println("  @BeforeClass " + m);
    }
    for (ITestNGMethod m : m_beforeTestMethods) {
      System.out.println("  @BeforeMethod " + m);
    }
    for (ITestNGMethod m : m_testMethods) {
      System.out.println("    @Test " + m);
    }
    for (ITestNGMethod m : m_afterTestMethods) {
      System.out.println("  @AfterMethod " + m);
    }
    for (ITestNGMethod m : m_afterClassMethods) {
      System.out.println("  @AfterClass " + m);
    }
    System.out.println("======");
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(getClass())
        .add("name", m_testClass)
        .toString();
  }

  public IClass getIClass() {
    return m_iClass;
  }
}