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
 * Aggregation relationship Target type is unique
 * @author Jorge Gaitán
 *
 */
@Rule(key="ChildNodeCheck",
name="A node can only have one parent",
priority= Priority.BLOCKER)
@BelongsToProfile(title=CheckRepository.SONAR_WAY_PROFILE_NAME,priority=Priority.BLOCKER)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.ERRORS)
@SqaleConstantRemediation("30min")
public class ChildNodeCheck extends AbstractXmlCheck{

	public static final String MESSAGE="Check that the node only has a single aggregation relationship where this is the target";

	private int contarPadres(Node node,String hijo){
		int contador=1;
		for (Node sibling=node.getNextSibling();sibling!= null;sibling=sibling.getNextSibling()) {

			if(sibling.getNodeName().equals(getVariables().getNodeListRelationTask()))
			{
				NamedNodeMap attribute=sibling.getAttributes();
				Node type=attribute.getNamedItem(getVariables().ATTRIBUTE_XSI_TYPE);
				Node target=attribute.getNamedItem(getVariables().ATTRIBUTE_TARGET);
				if(getVariables().getCttAgregation().equals(type.getNodeValue())&&hijo.equals(target.getNodeValue()))
					contador++;
			}
		}
		return contador;
	}

	private boolean hasNodeNull(Node type,Node target){
		return type!=null&&target!=null;
	}
	private void validateNode(Node node,String target){
		if(contarPadres(node, target)>1)
		{
			createViolation(getWebSourceCode().getLineForNode(node),MESSAGE);
		}
	}
	private void validateChildsNodes(Node node)
	{
		for(Node sibling=node.getFirstChild();sibling!= null;sibling=sibling.getNextSibling()){
			if(sibling.getNodeName().equals(getVariables().getNodeListRelationTask()))
			{
				NamedNodeMap attribute=sibling.getAttributes();
				Node type=attribute.getNamedItem(getVariables().ATTRIBUTE_XSI_TYPE);
				Node target=attribute.getNamedItem(getVariables().ATTRIBUTE_TARGET);
				if(hasNodeNull(type,target)&&getVariables().getCttAgregation().equals(type.getNodeValue())&&!"".equals(target.getNodeValue()))
				{
					validateNode(sibling,target.getNodeValue());
				}
			}
		}
		for (Node child=node.getFirstChild();child!=null;child=child.getNextSibling())
		{
			if(child.getNodeType()==Node.ELEMENT_NODE&&getVariables().getNodeDiagramCtt().equals(child.getNodeName()))
			{
				if(getVariables().isValidateCttByType()){
					isNodeValid(child);
				}
				else
					validateChildsNodes(child);
			}

		}
	}

	private void isNodeValid(Node node){
		NamedNodeMap attribute=node.getAttributes();
		Node type=attribute.getNamedItem(getVariables().getAttributeTypeDiagramCtt());
		if(type!=null&&getVariables().getNodeTypeDiagramCtt().equals(type.getNodeValue()))
			validateChildsNodes(node);	
	}

	@Override
	public void validate(XmlSourceCode xmlSourceCode) {
		setWebSourceCode(xmlSourceCode);

		Document document = getWebSourceCode().getDocument(false);
		if (document.getDocumentElement() != null) {
			validateChildsNodes(document.getDocumentElement());
		}

	}

}
