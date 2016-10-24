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

public class AbstractChildNodesCheckTest extends AbstractCheckTester {
	Variables var=Variables.getInstance();
	@Test
	public void checkOnlyParent() throws IOException {
		XmlSourceCode sourceCode = parseAndCheck(
				createTempFile(
						"<"+var.getNodeDiagramCtt()+" "+var.getAttributeTypeDiagramCtt()+"='"+var.getNodeTypeDiagramCtt()+"'>"
								+"<"+var.getNodeListTaskCtt()+" "+var.getCttAttributeXsiType()+"='"+var.getCttAbstractionTask()+"' "+var.getCttAttributeName()+"='Despegar'/>"
								+"<"+var.getNodeListTaskCtt()+" "+var.getCttAttributeXsiType()+"='"+var.getCttInteractionTask()+"' "+var.getCttAttributeName()+"='DefinirViaje' theTarea='//@itsPackage.2/@listDialogTask.0'/>"
								+"<"+var.getNodeListTaskCtt()+" "+var.getCttAttributeXsiType()+"='"+var.getCttInteractionTask()+"' "+var.getCttAttributeName()+"='SeleccionarBoleto' theTarea='//@itsPackage.3/@listDialogTask.0'/>"
								+"<"+var.getNodeListTaskCtt()+" "+var.getCttAttributeXsiType()+"='"+var.getCttInteractionTask()+"' "+var.getCttAttributeName()+"='RealizarPago' theTarea='//@itsPackage.4/@listDialogTask.0'/>"
								+"<"+var.getNodeListTaskCtt()+" "+var.getCttAttributeXsiType()+"='"+var.getCttAbstractionTask()+"' "+var.getCttAttributeName()+"='DefinirOrigen' theTarea='//@itsPackage.2/@listDialogTask.3'/>"
								+"<"+var.getNodeListRelationTask()+" "+var.getCttAttributeXsiType()+"='"+var.getCttAgregation()+"' "+var.getCttAttributeSource()+"='//@itsPackage.1/@listTaskCTT.0' "+var.getCttAttributeTarget()+"='//@itsPackage.1/@listTaskCTT.1'/>"
								+"<"+var.getNodeListRelationTask()+" "+var.getCttAttributeXsiType()+"='"+var.getCttAgregation()+"' "+var.getCttAttributeSource()+"='//@itsPackage.1/@listTaskCTT.0' "+var.getCttAttributeTarget()+"='//@itsPackage.1/@listTaskCTT.2'/>"
								+"<"+var.getNodeListRelationTask()+" "+var.getCttAttributeXsiType()+"='"+var.getCttAgregation()+"' "+var.getCttAttributeSource()+"='//@itsPackage.1/@listTaskCTT.2' "+var.getCttAttributeTarget()+"='//@itsPackage.1/@listTaskCTT.3'/>"
								+"<"+var.getNodeListRelationTask()+" "+var.getCttAttributeXsiType()+"='"+var.getCttAgregation()+"' "+var.getCttAttributeSource()+"='//@itsPackage.1/@listTaskCTT.2' "+var.getCttAttributeTarget()+"='//@itsPackage.1/@listTaskCTT.4'/>"
								+"</"+var.getNodeDiagramCtt()+">"
						),
				new AbstractChildNodesCheck());

		assertEquals(INCORRECT_NUMBER_OF_VIOLATIONS, 1, sourceCode.getXmlIssues().size());
	}
}
