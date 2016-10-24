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

public class ValidateRelationshipNameCheckTest extends AbstractCheckTester {

	Variables var= Variables.getInstance();

	@Test
	public void checkNodeName() throws IOException {
		XmlSourceCode sourceCode = parseAndCheck(

				createTempFile(
						"<prj:ProjectMDD xmi:version='2.0' xmlns:xmi='http://www.omg.org/XMI' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:herramienta='http:///herramienta.ecore' xmlns:herramienta.diagrams.dialog.concret='http:///herramienta/diagrams/dialog/concret.ecore' xmlns:herramienta.diagrams.domain='http:///herramienta/diagrams/domain.ecore' xmlns:herramienta.diagrams.interaction='http:///herramienta/diagrams/interaction.ecore' xmlns:herramienta.diagrams.navegation='http:///herramienta/diagrams/navegation.ecore' xmlns:herramienta.diagrams.ui='http:///herramienta/diagrams/ui.ecore'>"
								+"<itsPackage "+var.getAttributeTypeDiagramCtt()+"='herramienta.diagrams.ui:UI_Diagram' name='CUI'>"
								+"</itsPackage>"
								+"<"+var.getNodeDiagramCtt()+" "+var.getAttributeTypeDiagramCtt()+"='"+var.getNodeTypeDiagramCtt()+"'>"
								+"<"+var.getNodeListTaskCtt()+" "+var.getCttAttributeXsiType()+"='"+var.getCttAbstractionTask()+"' "+var.getCttAttributeName()+"='4Despegar'/>"
		    	    		    +"<"+var.getNodeListTaskCtt()+" "+var.getCttAttributeXsiType()+"='"+var.getCttInteractionTask()+"' "+var.getCttAttributeName()+"='SeleccionarBoleto' theTarea='//@itsPackage.3/@listDialogTask.0'/>"
		    	    		    +"<"+var.getNodeListTaskCtt()+" "+var.getCttAttributeXsiType()+"='"+var.getCttInteractionTask()+"' "+var.getCttAttributeName()+"='' theTarea='//@itsPackage.4/@listDialogTask.0'/>"
		    	    		    +"<"+var.getNodeListTaskCtt()+" "+var.getCttAttributeXsiType()+"='"+var.getCttInteractionTask()+"' "+var.getCttAttributeName()+"='Definir.Origen' theTarea='//@itsPackage.2/@listDialogTask.3'/>"
		    	    		    +"<"+var.getNodeListTaskCtt()+" "+var.getCttAttributeXsiType()+"='"+var.getCttInteractionTask()+"' "+var.getCttAttributeName()+"='DefinirOrigen2' theTarea='//@itsPackage.2/@listDialogTask.3'/>"
		    	    		    +"<"+var.getNodeListRelationTask()+" "+var.getCttAttributeXsiType()+"='"+var.getCttIndependentConcurrency()+"' "+var.getCttAttributeSource()+"='//@itsPackage.1/@listTaskCTT.4' "+var.getCttAttributeTarget()+"='//@itsPackage.1/@listTaskCTT.5'"+" "+var.getCttAttributeName()+"='4Despegar'/>"
								+"<"+var.getNodeListRelationTask()+" "+var.getCttAttributeXsiType()+"='"+var.getCttAgregation()+"' "+var.getCttAttributeSource()+"='//@itsPackage.1/@listTaskCTT.4' "+var.getCttAttributeTarget()+"='//@itsPackage.1/@listTaskCTT.10'"+" "+var.getCttAttributeName()+"='despegar1'/>"
								+"<"+var.getNodeListRelationTask()+" "+var.getCttAttributeXsiType()+"='"+var.getCttAgregation()+"' "+var.getCttAttributeSource()+"='//@itsPackage.1/@listTaskCTT.5' "+var.getCttAttributeTarget()+"='//@itsPackage.1/@listTaskCTT.11'"+" "+var.getCttAttributeName()+"='Relacion.uno'/>"
								+"<"+var.getNodeListRelationTask()+" "+var.getCttAttributeXsiType()+"='"+var.getCttAgregation()+"' "+var.getCttAttributeSource()+"='//@itsPackage.1/@listTaskCTT.5' "+var.getCttAttributeTarget()+"='//@itsPackage.1/@listTaskCTT.12'"+" "+var.getCttAttributeName()+"='Relacion2'/>"
								+"<"+var.getNodeListRelationTask()+" "+var.getCttAttributeXsiType()+"='"+var.getCttAgregation()+"' "+var.getCttAttributeSource()+"='//@itsPackage.1/@listTaskCTT.5' "+var.getCttAttributeTarget()+"='//@itsPackage.1/@listTaskCTT.13'"+" "+var.getCttAttributeName()+"='relacion_3'/>"
								+"<"+var.getNodeListRelationTask()+" "+var.getCttAttributeXsiType()+"='"+var.getCttIndependentConcurrency()+"' "+var.getCttAttributeSource()+"='//@itsPackage.1/@listTaskCTT.2' "+var.getCttAttributeTarget()+"='//@itsPackage.1/@listTaskCTT.12'/>"
								+"<"+var.getNodeListRelationTask()+" "+var.getCttAttributeXsiType()+"='"+var.getCttIndependentConcurrency()+"' "+var.getCttAttributeSource()+"='//@itsPackage.1/@listTaskCTT.12' "+var.getCttAttributeTarget()+"='//@itsPackage.1/@listTaskCTT.13'/>"
								+"<"+var.getNodeListRelationTask()+" "+var.getCttAttributeXsiType()+"='"+var.getCttAgregation()+"' "+var.getCttAttributeSource()+"='//@itsPackage.1/@listTaskCTT.0' "+var.getCttAttributeTarget()+"='//@itsPackage.1/@listTaskCTT.2'/>"
								+"<"+var.getNodeListRelationTask()+" "+var.getCttAttributeXsiType()+"='"+var.getCttAgregation()+"' "+var.getCttAttributeSource()+"='//@itsPackage.1/@listTaskCTT.0' "+var.getCttAttributeTarget()+"='//@itsPackage.1/@listTaskCTT.3'/>"
								+"<"+var.getNodeListRelationTask()+" "+var.getCttAttributeXsiType()+"='"+var.getCttAgregation()+"' "+var.getCttAttributeSource()+"='//@itsPackage.1/@listTaskCTT.1' "+var.getCttAttributeTarget()+"='//@itsPackage.1/@listTaskCTT.6'/>"
								+"</"+var.getNodeDiagramCtt()+">"
		    	    		    +"</prj:ProjectMDD>"
		    		  ),

						new ValidateRelationshipNameCheck());

		assertEquals(INCORRECT_NUMBER_OF_VIOLATIONS, 3, sourceCode.getXmlIssues().size());
	}

	
}
