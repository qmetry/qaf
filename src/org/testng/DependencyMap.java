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
import java.util.Set;
import java.util.regex.Pattern;

import org.testng.collections.ListMultiMap;
import org.testng.collections.Lists;
import org.testng.collections.Maps;

import com.qmetry.qaf.automation.step.client.Scenario;

/**
 * Helper class to keep track of dependencies.
 *
 * @author Cedric Beust <cedric@beust.com>
 */
public class DependencyMap {
  private ListMultiMap<String, ITestNGMethod> m_dependencies = Maps.newListMultiMap();
  private ListMultiMap<String, ITestNGMethod> m_groups = Maps.newListMultiMap();

  public DependencyMap(ITestNGMethod[] methods) {
    for (ITestNGMethod m : methods) {
    	m_dependencies.put( m.getQualifiedName(), m);
      for (String g : m.getGroups()) {
        m_groups.put(g, m);
      }
    }
  }

  public List<ITestNGMethod> getMethodsThatBelongTo(String group, ITestNGMethod fromMethod) {
    Set<String> uniqueKeys = m_groups.keySet();

    List<ITestNGMethod> result = Lists.newArrayList();

    for (String k : uniqueKeys) {
      if (Pattern.matches(group, k)) {
        result.addAll(m_groups.get(k));
      }
    }

    if (result.isEmpty() && !fromMethod.ignoreMissingDependencies()) {
      throw new TestNGException("DependencyMap::Method \"" + fromMethod
          + "\" depends on nonexistent group \"" + group + "\"");
    } else {
      return result;
    }
  }

  public ITestNGMethod getMethodDependingOn(String methodName, ITestNGMethod fromMethod) {
    List<ITestNGMethod> l = m_dependencies.get(methodName);
    if (l.isEmpty() && fromMethod.ignoreMissingDependencies()){
    	return fromMethod;
    }
    for (ITestNGMethod m : l) {
      // If they are in the same class hierarchy, they must belong to the same instance,
      // otherwise, it's a method depending on a method in a different class so we
      // don't bother checking the instance
      if (fromMethod.getRealClass().isAssignableFrom(m.getRealClass())) {
        if (m.getInstance() == fromMethod.getInstance()
        		|| m.getRealClass().isAssignableFrom(Scenario.class)) 
        	return m;
      } else {
        return m;
      }
    }
    throw new TestNGException("Method \"" + fromMethod
        + "\" depends on nonexistent method \"" + methodName + "\"");
  }
}
