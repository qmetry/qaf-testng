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

import org.testng.collections.Lists;
import org.testng.collections.Objects;
import org.testng.internal.*;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.log4testng.Logger;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlTest;

import java.util.List;
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
  private transient  IAnnotationFinder annotationFinder = null;
  // The Strategy used to locate test methods (TestNG, JUnit, etc...)
  private transient ITestMethodFinder testMethodFinder = null;

  private IClass iClass = null;
  private String testName;
  private XmlTest xmlTest;
  private XmlClass xmlClass;

  private static final Logger LOG =Logger.getLogger(TestClass.class);

  protected TestClass(IClass cls,
                   ITestMethodFinder testMethodFinder,
                   IAnnotationFinder annotationFinder,
                   RunInfo runInfo,
                   XmlTest xmlTest,
                   XmlClass xmlClass) {
    init(cls, testMethodFinder, annotationFinder, xmlTest, xmlClass);
  }

  @Override
  public String getTestName() {
    return testName;
  }

  @Override
  public XmlTest getXmlTest() {
    return xmlTest;
  }

  @Override
  public XmlClass getXmlClass() {
    return xmlClass;
  }

  public IAnnotationFinder getAnnotationFinder() {
    return annotationFinder;
  }

  private void init(IClass cls,
                    ITestMethodFinder testMethodFinder,
                    IAnnotationFinder annotationFinder,
                    XmlTest xmlTest,
                    XmlClass xmlClass)
  {
    log(3, "Creating TestClass for " + cls);
    iClass = cls;
    m_testClass = cls.getRealClass();
    this.xmlTest = xmlTest;
    this.xmlClass = xmlClass;
    this.testMethodFinder = testMethodFinder;
    this.annotationFinder = annotationFinder;
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
        testName = ((ITest) instance).getTestName();
        break;
      }
    }
    if (testName == null) {
      testName = iClass.getTestName();
    }
  }

  @Override
  public Object[] getInstances(boolean create) {
    return iClass.getInstances(create);
  }

  @Override
  public long[] getInstanceHashCodes() {
    return iClass.getInstanceHashCodes();
  }

  @Deprecated
  @Override
  public int getInstanceCount() {
    return iClass.getInstanceCount();
  }

  @Override
  public void addInstance(Object instance) {
    iClass.addInstance(instance);
  }

  private void initMethods() {
    ITestNGMethod[] methods = testMethodFinder.getTestMethods(m_testClass, xmlTest);
    m_testMethods = createTestMethods(methods);

    for (Object instance : iClass.getInstances(false)) {
      m_beforeSuiteMethods = ConfigurationMethod
          .createSuiteConfigurationMethods(testMethodFinder.getBeforeSuiteMethods(m_testClass),
              annotationFinder,
                                           true,
                                           instance);
      m_afterSuiteMethods = ConfigurationMethod
          .createSuiteConfigurationMethods(testMethodFinder.getAfterSuiteMethods(m_testClass),
              annotationFinder,
                                           false,
                                           instance);
      m_beforeTestConfMethods = ConfigurationMethod
          .createTestConfigurationMethods(testMethodFinder.getBeforeTestConfigurationMethods(m_testClass),
              annotationFinder,
                                          true,
                                          instance);
      m_afterTestConfMethods = ConfigurationMethod
          .createTestConfigurationMethods(testMethodFinder.getAfterTestConfigurationMethods(m_testClass),
              annotationFinder,
                                          false,
                                          instance);
      m_beforeClassMethods = ConfigurationMethod
          .createClassConfigurationMethods(testMethodFinder.getBeforeClassMethods(m_testClass),
              annotationFinder,
                                           true,
                                           instance);
      m_afterClassMethods = ConfigurationMethod
          .createClassConfigurationMethods(testMethodFinder.getAfterClassMethods(m_testClass),
              annotationFinder,
                                           false,
                                           instance);
      m_beforeGroupsMethods = ConfigurationMethod
          .createBeforeConfigurationMethods(testMethodFinder.getBeforeGroupsConfigurationMethods(m_testClass),
              annotationFinder,
                                            true,
                                            instance);
      m_afterGroupsMethods = ConfigurationMethod
          .createAfterConfigurationMethods(testMethodFinder.getAfterGroupsConfigurationMethods(m_testClass),
              annotationFinder,
                                           false,
                                           instance);
      m_beforeTestMethods = ConfigurationMethod
          .createTestMethodConfigurationMethods(testMethodFinder.getBeforeTestMethods(m_testClass),
              annotationFinder,
                                                true,
                                                instance);
      m_afterTestMethods = ConfigurationMethod
          .createTestMethodConfigurationMethods(testMethodFinder.getAfterTestMethods(m_testClass),
              annotationFinder,
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
        for (Object o : iClass.getInstances(false)) {
          log(4, "Adding method " + tm + " on TestClass " + m_testClass);
          vResult.add(new TestNGScenario(/* tm.getRealClass(), */ m.getMethod(), annotationFinder, xmlTest,
              o));
        }
      }
      else {
        log(4, "Rejecting method " + tm + " for TestClass " + m_testClass);
      }
    }

    return vResult.toArray(new ITestNGMethod[vResult.size()]);
  }

  public ITestMethodFinder getTestMethodFinder() {
    return testMethodFinder;
  }

  private void log(int level, String s) {
    Utils.log("TestClass", level, s);
  }

  protected void dump() {
    LOG.info("===== Test class\n" + m_testClass.getName());
    for (ITestNGMethod m : m_beforeClassMethods) {
      LOG.info("  @BeforeClass " + m);
    }
    for (ITestNGMethod m : m_beforeTestMethods) {
      LOG.info("  @BeforeMethod " + m);
    }
    for (ITestNGMethod m : m_testMethods) {
      LOG.info("    @Test " + m);
    }
    for (ITestNGMethod m : m_afterTestMethods) {
      LOG.info("  @AfterMethod " + m);
    }
    for (ITestNGMethod m : m_afterClassMethods) {
      LOG.info("  @AfterClass " + m);
    }
    LOG.info("======");
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(getClass())
        .add("name", m_testClass)
        .toString();
  }

  public IClass getIClass() {
    return iClass;
  }
}
