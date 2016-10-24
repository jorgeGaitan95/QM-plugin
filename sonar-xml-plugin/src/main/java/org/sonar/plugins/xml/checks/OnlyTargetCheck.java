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
/**
 * a node can only have a unique relationship with a previous node
 * @author Jorge
 *
 */
@Rule(key="OnlyTargetCheck",
name="Target of the relationship must be unique",
priority= Priority.BLOCKER)
@BelongsToProfile(title=CheckRepository.SONAR_WAY_PROFILE_NAME,priority=Priority.BLOCKER)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.ERRORS)
@SqaleConstantRemediation("30min")
public class OnlyTargetCheck extends AbstractXmlCheck{
	 
	public static final String MESSAGE="check that the node is connected to a only node previous";
	 
	private int contarTargetIguales(Node node,String nodoTarget){
		int contador=1;
		for (Node sibling=node.getNextSibling();sibling!= null;sibling=sibling.getNextSibling()) {
			
			if(sibling.getNodeName().equals(getVariables().getNodeListRelationTask()))
			{
				NamedNodeMap attribute=sibling.getAttributes();
				Node type=attribute.getNamedItem(getVariables().getCttAttributeXsiType());
				Node target=attribute.getNamedItem(getVariables().getCttAttributeSource());
				if(type!=null&&target!=null&&(!type.getNodeValue().equals(getVariables().getCttAgregation())&&target.getNodeValue().equals(nodoTarget)))
				   contador++;
			}
		}
		return contador;
	}
	
	private static boolean isTargetComparado(ArrayList<String>targetComparados,String target)
	{
		for (int i = 0; i < targetComparados.size(); i++) {
			if(targetComparados.get(i).equals(target)){
				return true;
			}
		}
		return false;
	}
	private boolean validateNodes(Node type, Node target){
		return type!=null&&target!=null&&!"".equals(type.getNodeValue())&&!type.equals(getVariables().getCttAgregation()) 
				&&!"".equals(target.getNodeValue());
	}
	private void validateNodeTarget(Node node)
	{
		ArrayList<String> targetComparados=new ArrayList<String>();
		for(Node sibling=node.getFirstChild();sibling!= null;sibling=sibling.getNextSibling()){
			if(sibling.getNodeName().equals(getVariables().getNodeListRelationTask()))
			{
				NamedNodeMap attribute=sibling.getAttributes();
				Node type=attribute.getNamedItem(getVariables().getCttAttributeXsiType());
				Node target=attribute.getNamedItem(getVariables().getCttAttributeTarget());
				if(validateNodes(type, target) && !isTargetComparado(targetComparados,target.getNodeValue()))
				{
					targetComparados.add(target.getNodeValue());
					int contador=contarTargetIguales(sibling, target.getNodeValue());
					if(contador>1)
					{
						createViolation(getWebSourceCode().getLineForNode(sibling), MESSAGE);
					}
				}
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
					validateNodeTarget(child);
			}
		}
	}
	private void isNodeValid(Node node){
		NamedNodeMap attribute=node.getAttributes();
		Node type=attribute.getNamedItem(getVariables().getAttributeTypeDiagramCtt());
		if(type!=null&&getVariables().getNodeTypeDiagramCtt().equals(type.getNodeValue()))
			validateNodeTarget(node);	
	}
	@Override
	public void validate(XmlSourceCode xmlSourceCode) {
		setWebSourceCode(xmlSourceCode);

	    Document document = getWebSourceCode().getDocument(false);
	    if (document.getDocumentElement() != null) {
	      validateNodeTarget(document.getDocumentElement());
	    }
		
	}

}
