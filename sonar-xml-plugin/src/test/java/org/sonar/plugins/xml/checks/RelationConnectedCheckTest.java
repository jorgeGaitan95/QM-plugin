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

public class RelationConnectedCheckTest extends AbstractCheckTester{
	Variables var= Variables.getInstance();
	@Test
	public void checkAttributeName() throws IOException {
		XmlSourceCode sourceCode = parseAndCheck(
				createTempFile(
						"<herramienta:ModelFactory xmi:version='2.0' xmlns:xmi='http://www.omg.org/XMI' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:herramienta='http:///herramienta.ecore' xmlns:herramienta.diagrams.domain='http:///herramienta/diagrams/domain.ecore' xmlns:herramienta.diagrams.entidadrelacion='http:///herramienta/diagrams/entidadrelacion.ecore' xmlns:herramienta.diagrams.interaction='http:///herramienta/diagrams/interaction.ecore' xmlns:herramienta.diagrams.usecase.ucabstract='http:///herramienta/diagrams/usecase/ucabstract.ecore' xmlns:herramienta.diagrams.usecase.ucconcret='http:///herramienta/diagrams/usecase/ucconcret.ecore'>"
								+"<itsPackage xsi:type='herramienta.diagrams.entidadrelacion:EntityDiagram'>"
								+"<listEntity name='Persona'>"
								+"</listEntity>"
								+"<listEntity name='Programa'>"
								+"<listAttribute name='NombrePrograma' type='String' propiedad='KEY'/>"
								+"<listAttribute name='NombrePrograma2' type='String' propiedad='N'/>"
								+"<listAttribute name='NombrePrograma3' type='String' propiedad='N'/>"
								+"</listEntity>"
								+"<itsRelationEntity xsi:type='herramienta.diagrams.entidadrelacion:ManyToMany' source='//@listEntity.1'/>"
								+"<itsRelationEntity xsi:type='herramienta.diagrams.entidadrelacion:OneToOne' source='' target='//@listEntity.1'/>"
								+"<itsRelationEntity xsi:type='herramienta.diagrams.entidadrelacion:OneToOneOptional' source='//@listEntity.0' target='//@listEntity.1'/>"
								+"<itsRelationEntity xsi:type='herramienta.diagrams.entidadrelacion:OneToManyOptional' source='//@listEntity.0' target='//@listEntity.1'/>"
								+"</itsPackage>"
								+"</herramienta:ModelFactory>"						
						),
				new RelationConnectedCheck());

		assertEquals(INCORRECT_NUMBER_OF_VIOLATIONS, 2, sourceCode.getXmlIssues().size());
	}
}
