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

import java.util.ArrayList;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Rule(key="SameAttributeClassCheck",
name="there should be no duplicate Attributes",
priority= Priority.BLOCKER)
@BelongsToProfile(title=CheckRepository.SONAR_WAY_PROFILE_NAME,priority=Priority.BLOCKER)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.ERRORS)
@SqaleConstantRemediation("30min")

public class SameAttributeClassCheck extends AbstractXmlCheck{
	
	public static final String MESSAGE="Check that no exist two or more Attributes whit the same name";
	


	public void evaluateAttributesClass(Node node){ //Recive un nodo Clase
		
		NodeList childNodes=node.getChildNodes(); //Obtiene los hijos de la clase
		
		int length=childNodes.getLength(); //Cantidad de Elementos (Atributos y Operaciones)

		ArrayList<String> atributos = new ArrayList<String>(); //Lista de Nombres de Atributos 
		
		for (int i = 0; i < length; i++) {
		
			Node nodoAux=childNodes.item(i); //Similar al .get(i)

			if(nodoAux.getNodeType()==Node.ELEMENT_NODE && nodoAux.getNodeName().equals(getVariables().NODE_ATTRIBUTE_CLASS)) // Solo valida los "listaAtributos"
			{
				NamedNodeMap attribute=nodoAux.getAttributes(); //Obtiene las propiedades del Atributo (nombre, Tipo, etc)
				
				if(attribute.getNamedItem(getVariables().ATTRIBUTE_NAME)!=null){ //revisa si el Atributo "name" Exista
					String nombre= attribute.getNamedItem(getVariables().ATTRIBUTE_NAME).getNodeValue();
					if (nombre != null || !nombre.equals("")){
						if (atributos.contains(nombre) == true){
							createViolation(getWebSourceCode().getLineForNode(nodoAux), MESSAGE);		
						}
						else{
							atributos.add(nombre);
						}						
					}
				}
			}
		}
	}
	
	public void validateAttributeType(Node node){
		for(Node sibling=node.getFirstChild();sibling!= null;sibling=sibling.getNextSibling()){
			if(sibling.getNodeName().equals(getVariables().NODE_CLASS)&&sibling.hasChildNodes()) //Revisa que el nodo tenga la etiqueta itsClases
			{
				evaluateAttributesClass(sibling); //Evía el nodo Clase
			}
		}
		
		for (Node child=node.getFirstChild();child!=null;child=child.getNextSibling())
		{
			if(child.getNodeType()==Node.ELEMENT_NODE&&child.getNodeName().equals(getVariables().CLASS_DIAGRAM_NAME)) //Revisa que tenga la etiqueta itsClases, y si la tiene, da profundidad
			{
				NamedNodeMap attribute=child.getAttributes();
				String type=attribute.getNamedItem(getVariables().ATTRIBUTE_TYPE_CLASS_DIAGRAM).getNodeValue();
				if(type.equals(getVariables().TYPE_CLASS)){
					validateAttributeType(child); //Envía al hijo a evaluarse
				}
			}
			
		}
	}
	
	

	
	
	@Override
	public void validate(XmlSourceCode xmlSourceCode) {
		setWebSourceCode(xmlSourceCode);

	    Document document = getWebSourceCode().getDocument(false);
	    if (document.getDocumentElement() != null) {
	    	validateAttributeType(document.getDocumentElement()); //Envía la raiz
	    }
	}

}
