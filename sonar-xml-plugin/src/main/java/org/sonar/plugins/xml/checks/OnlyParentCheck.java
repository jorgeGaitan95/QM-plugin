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
/**
 * @author Jorge
 *
 */
@Rule(key="OnlyParentCheck",
name="Only one node can be root",
priority= Priority.BLOCKER)
@BelongsToProfile(title=CheckRepository.SONAR_WAY_PROFILE_NAME,priority=Priority.BLOCKER)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.ERRORS)
@SqaleConstantRemediation("30min")
public class OnlyParentCheck extends AbstractXmlCheck{

	public static final String MESSAGE="Existe mas de un nodo raiz en este diagrama";
	/**
	 * busca si en la lista de nodos hay alguna relacion de tipo agragacion en la que el nodo sea hijo
	 * @param posicionNodo posicion del nodo analizado
	 * @param padre Nodo del Diagrama ctt
	 * @return false si el nodo es hijo de otro nodo, true si no tiene un padre y por lo tanto es una raiz del diagrama
	 */
	private boolean isOnlyParent(int posicionNodo,Node padre){
		for (int i = 0; i < padre.getChildNodes().getLength(); i++) {
			if(padre.getChildNodes().item(i).getNodeName().equals(getVariables().getNodeListRelationTask()))
			{
				NamedNodeMap attribute=padre.getChildNodes().item(i).getAttributes();
				Node type=attribute.getNamedItem(getVariables().getCttAttributeXsiType());
				Node target=attribute.getNamedItem(getVariables().getCttAttributeTarget());
				if(validateNodes(type, target)&&type.getNodeValue().equals(getVariables().getCttAgregation())
						&&!"".equals(target.getNodeValue()))
				{
						String[] splitSource=target.getNodeValue().split("\\.");
						int posicionNodoTarget=Integer.parseInt(splitSource[splitSource.length-1]);
						if (posicionNodo==posicionNodoTarget) {
							return false;
						}
					
				}
			}
		}
		return true;
		//ENE STE PUNTO SE PUDE DECIR QUE EN NODO EN LA "posicionNode" ES UNA RAÍZ DEL ÁRBOL
		
	}
	private boolean validateNodes(Node type,Node target){
		return type!=null&&target!=null;
	}
	private void validateOnlyParent(Node node){
		int contador=0;
		int cantidad=0;
		for(Node sibling=node.getFirstChild();sibling!= null;sibling=sibling.getNextSibling()){
			if(sibling.getNodeName().equals(getVariables().getNodeListTaskCtt()))
			{
				//SI ESTE LA CONDICION DENTOR DEL IF ES TRUE, SIBILING ES UN NODO RAIZ
				if(isOnlyParent(contador,sibling.getParentNode()))
					cantidad+=1;
				if(cantidad>1)
				{
					createViolation(getWebSourceCode().getLineForNode(node), MESSAGE);
					return;
				}
				contador++;
			}
		}
		
		for (Node child=node.getFirstChild();child!=null;child=child.getNextSibling())
		{
			if(child.getNodeType()==Node.ELEMENT_NODE&&child.getNodeName().equals(getVariables().getNodeDiagramCtt()))
			{
				if(getVariables().isValidateCttByType()){
					isNodeValid(child);
				}
				else
					validateOnlyParent(child);
			}	
		}
	}
	private void isNodeValid(Node node){
		NamedNodeMap attribute=node.getAttributes();
		Node type=attribute.getNamedItem(getVariables().getAttributeTypeDiagramCtt());
		if(type!=null&&getVariables().getNodeTypeDiagramCtt().equals(type.getNodeValue()))
			validateOnlyParent(node);	
	}

	@Override
	public void validate(XmlSourceCode xmlSourceCode) {
		setWebSourceCode(xmlSourceCode);

		Document document = getWebSourceCode().getDocument(false);
		if (document.getDocumentElement() != null) {
			validateOnlyParent(document.getDocumentElement());
		}

	}

}

