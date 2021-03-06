/*
 * SonarQube XML Plugin
 * Copyright (C) 2010 SonarSource
 * sonarqube@googlegroups.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sonar.plugins.xml.checks;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

public class NumberActorsCheckTest extends AbstractCheckTester {
	Variables var= Variables.getInstance();
	@Test
	public void checkAttributeName() throws IOException {
		XmlSourceCode sourceCode = parseAndCheck(
				createTempFile(
						"<usecase:Diagram xmi:version='2.0' xmlns:xmi='http://www.omg.org/XMI' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:usecase='http://usecase'>"
		    	    		  	+"<listUseCase name='caso1'/>"
		    	    		  	+"<listUseCase name='caso2'/>"
		    	    		  	+"<listUseCase/>"
		    	    		  	+"<listUseCase name='caso3'/>"
		    	    		  	+"<listActors name='actor1'/>"
		    	    		  	+"<listActors name=''/>"
		    	    		  	+"<listActors name='actor1'/>"
		    	    		  	+"<listActors name=''/>"
		    	    		  	+"<listActors name='actor1'/>"
		    	    		  	+"<listActors name=''/>"
		    	    		  	+"<listRelation xsi:type='usecase:Exclude' source='//@listUseCase.1' target='//@listUseCase.2'/>"
								+"<listRelation xsi:type='usecase:Exclude' source='//@listUseCase.0' target='//@listUseCase.1'/>"
								+"<listRelation xsi:type='usecase:Hiearchy' source='//@listUseCase.0' target='//@listUseCase.1'/>"
		    	    		    +"</usecase:Diagram>"
		    		  ),
						new NumberActorsCheck());

		assertEquals(INCORRECT_NUMBER_OF_VIOLATIONS, 1, sourceCode.getXmlIssues().size());
	}
}
