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

@Rule(key="AttributeValidTypeCheck",
name="Attributes with Invalid Type",
priority= Priority.BLOCKER)
@BelongsToProfile(title = CheckRepository.SONAR_WAY_PROFILE_NAME, priority = Priority.BLOCKER)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.ERRORS)
@SqaleConstantRemediation("5min")
public class AttributeValidTypeCheck extends AbstractXmlCheck{
	
public static final String MESSAGE="Chectk that the Attribute have a valid type";
	
	private boolean isTipoValido(String tipoNodo){
		if(tipoNodo.equals("String")||tipoNodo.equals("Number")||tipoNodo.equals("Character")||tipoNodo.equals("boolean")||
			tipoNodo.equals("float")||tipoNodo.equals("Double")||tipoNodo.equals("Date")){
				return true;
		}else
			return false;
	}
	
	public void evaluateAttributesClass(Node node){
		NodeList childNodes=node.getChildNodes();
		int length=childNodes.getLength();
		for (int i = 0; i < length; i++) {
			Node nodoAux=childNodes.item(i);
			if(nodoAux.getNodeType()==Node.ELEMENT_NODE&&nodoAux.getNodeName().equals(getVariables().NODE_ATTRIBUTE_ER))
			{
				NamedNodeMap attribute=nodoAux.getAttributes();
				if(attribute.getNamedItem(getVariables().ATTRIBUTE_TIPOATRIBUTO)!=null){
					String tipo=attribute.getNamedItem(getVariables().ATTRIBUTE_TIPOATRIBUTO).getNodeValue();
					if(tipo.equals("")||tipo==null||!isTipoValido(tipo))
						createViolation(getWebSourceCode().getLineForNode(nodoAux), MESSAGE);
				}else{
					createViolation(getWebSourceCode().getLineForNode(nodoAux), MESSAGE);
				}
			}
		}
	}
	
	public void validateAttributeType(Node node){
		for(Node sibling=node.getFirstChild();sibling!= null;sibling=sibling.getNextSibling()){
			if(sibling.getNodeName().equals(getVariables().NODE_TABLE)&&sibling.hasChildNodes())
			{
				evaluateAttributesClass(sibling);
			}
		}
		
		for (Node child=node.getFirstChild();child!=null;child=child.getNextSibling())
		{
			if(child.getNodeType()==Node.ELEMENT_NODE&&child.getNodeName().equals(getVariables().ER_DIAGRAM_NAME))
			{
				if(getVariables().VALIDATE_ER_BY_TYPE){
					isNodeValid(child);
				}
				else
					validateAttributeType(child);
			}

		}
	}

	private void isNodeValid(Node node){
		NamedNodeMap attribute=node.getAttributes();
		Node type=attribute.getNamedItem(getVariables().attributeTypeERDiagram);
		if(type!=null&&getVariables().nodeTypeERDiagram.equals(type.getNodeValue()))
			validateAttributeType(node);

	}
	@Override
	public void validate(XmlSourceCode xmlSourceCode) {
		setWebSourceCode(xmlSourceCode);

	    Document document = getWebSourceCode().getDocument(false);
	    if (document.getDocumentElement() != null) {
	    	validateAttributeType(document.getDocumentElement());
	    }
	}

}
