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

public class TreeLuytenCheckTest extends AbstractCheckTester{

Variables var= Variables.getInstance();
	
	@Test
	  public void checkAttribute() throws IOException {
		
	    XmlSourceCode sourceCode = parseAndCheck(
	      createTempFile(
	    		  "<prj:ProjectMDD xmi:version='2.0' xmlns:xmi='http://www.omg.org/XMI' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:herramienta='http:///herramienta.ecore' xmlns:herramienta.diagrams.dialog.concret='http:///herramienta/diagrams/dialog/concret.ecore' xmlns:herramienta.diagrams.domain='http:///herramienta/diagrams/domain.ecore' xmlns:herramienta.diagrams.interaction='http:///herramienta/diagrams/interaction.ecore' xmlns:herramienta.diagrams.navegation='http:///herramienta/diagrams/navegation.ecore' xmlns:herramienta.diagrams.ui='http:///herramienta/diagrams/ui.ecore'>"
	    				  +"<"+var.getNodeDiagramCtt()+" "+var.getAttributeTypeDiagramCtt()+"='"+var.getNodeTypeDiagramCtt()+"'>"
							+"<"+var.getNodeListTaskCtt()+" "+var.getCttAttributeXsiType()+"='"+var.getCttAbstractionTask()+"' "+var.getCttAttributeName()+"='Despegar'/>"
	    	    		    +"<"+var.getNodeListTaskCtt()+" "+var.getCttAttributeXsiType()+"='"+var.getCttInteractionTask()+"' "+var.getCttAttributeName()+"='DefinirViaje' theTarea='//@itsPackage.2/@listDialogTask.0'/>"
	    	    		    +"<"+var.getNodeListTaskCtt()+" "+var.getCttAttributeXsiType()+"='"+var.getCttInteractionTask()+"' "+var.getCttAttributeName()+"='SeleccionarBoleto' theTarea='//@itsPackage.3/@listDialogTask.0'/>"
	    	    		    +"<"+var.getNodeListTaskCtt()+" "+var.getCttAttributeXsiType()+"='"+var.getCttInteractionTask()+"' "+var.getCttAttributeName()+"='RealizarPago' theTarea='//@itsPackage.4/@listDialogTask.0'/>"
	    	    		    +"<"+var.getNodeListTaskCtt()+" "+var.getCttAttributeXsiType()+"='"+var.getCttInteractionTask()+"' "+var.getCttAttributeName()+"='DefinirOrigen' theTarea='//@itsPackage.2/@listDialogTask.3'/>"
	    	    		    +"<"+var.getNodeListTaskCtt()+" "+var.getCttAttributeXsiType()+"='"+var.getCttInteractionTask()+"' "+var.getCttAttributeName()+"='NodoPrueba' theTarea='//@itsPackage.2/@listDialogTask.3'/>"
	    	    		    +"<"+var.getNodeListRelationTask()+" "+var.getCttAttributeXsiType()+"='"+var.getCttIndependentConcurrency()+"' "+var.getCttAttributeSource()+"='//@itsPackage.1/@listTaskCTT.3' "+var.getCttAttributeTarget()+"='//@itsPackage.1/@listTaskCTT.4'/>"
	    	    		    +"<"+var.getNodeListRelationTask()+" "+var.getCttAttributeXsiType()+"='"+var.getCttDisablig()+"' "+var.getCttAttributeSource()+"='//@itsPackage.1/@listTaskCTT.2' "+var.getCttAttributeTarget()+"='//@itsPackage.1/@listTaskCTT.3'/>"
	    	    		    +"<"+var.getNodeListRelationTask()+" "+var.getCttAttributeXsiType()+"='"+var.getCttIndependentConcurrency()+"' "+var.getCttAttributeSource()+"='//@itsPackage.1/@listTaskCTT.4' "+var.getCttAttributeTarget()+"='//@itsPackage.1/@listTaskCTT.5'/>"
	    	    		    +"<"+var.getNodeListRelationTask()+" "+var.getCttAttributeXsiType()+"='"+var.getCttEnabling()+"' "+var.getCttAttributeSource()+"='//@itsPackage.1/@listTaskCTT.1' target='//@itsPackage.1/@listTaskCTT.2'/>"
	    	    		    +"<"+var.getNodeListRelationTask()+" "+var.getCttAttributeXsiType()+"='"+var.getCttAgregation()+"' "+var.getCttAttributeSource()+"='//@itsPackage.1/@listTaskCTT.0' "+var.getCttAttributeTarget()+"='//@itsPackage.1/@listTaskCTT.1'/>"
	    	    		    +"<"+var.getNodeListRelationTask()+" "+var.getCttAttributeXsiType()+"='"+var.getCttAgregation()+"' "+var.getCttAttributeSource()+"='//@itsPackage.1/@listTaskCTT.0' "+var.getCttAttributeTarget()+"='//@itsPackage.1/@listTaskCTT.2'/>"
	    	    		    +"<"+var.getNodeListRelationTask()+" "+var.getCttAttributeXsiType()+"='"+var.getCttAgregation()+"' "+var.getCttAttributeSource()+"='//@itsPackage.1/@listTaskCTT.0' "+var.getCttAttributeTarget()+"='//@itsPackage.1/@listTaskCTT.3'/>"
	    	    		    +"<"+var.getNodeListRelationTask()+" "+var.getCttAttributeXsiType()+"='"+var.getCttAgregation()+"' "+var.getCttAttributeSource()+"='//@itsPackage.1/@listTaskCTT.0' "+var.getCttAttributeTarget()+"='//@itsPackage.1/@listTaskCTT.5'/>"
	    	    		    +"<"+var.getNodeListRelationTask()+" "+var.getCttAttributeXsiType()+"='"+var.getCttAgregation()+"' "+var.getCttAttributeSource()+"='//@itsPackage.1/@listTaskCTT.0' "+var.getCttAttributeTarget()+"='//@itsPackage.1/@listTaskCTT.4'/>"
	    	    		    +"</"+var.getNodeDiagramCtt()+">"
	    	    		    +"</prj:ProjectMDD>"
	    		  ),
	      new TreeLuytenCheck());
	    assertEquals(INCORRECT_NUMBER_OF_VIOLATIONS, 1, sourceCode.getXmlIssues().size());
	  }
}
