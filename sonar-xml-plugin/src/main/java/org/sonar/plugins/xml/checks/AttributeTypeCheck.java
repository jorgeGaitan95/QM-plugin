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

/**
 * 
 * @author Jorge Gaitán
 *
 */
@Rule(key="AttributeTypeCheck",
name="Attributes without Type",
priority= Priority.BLOCKER)
@BelongsToProfile(title = CheckRepository.SONAR_WAY_PROFILE_NAME, priority = Priority.BLOCKER)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.ERRORS)
@SqaleConstantRemediation("30min")
public class AttributeTypeCheck extends AbstractXmlCheck{
	public static final String MESSAGE="Check that the Attribute have a type";

	private void validateTableName(Node node){
		for(Node sibling=node.getFirstChild();sibling!= null;sibling=sibling.getNextSibling()){
			if(sibling.getNodeName().equals(getVariables().NODE_TABLE))
			{
				NamedNodeMap attribute=sibling.getAttributes();
				if(attribute.getNamedItem(getVariables().ATTRIBUTE_TIPOATRIBUTO)!=null){
					String type=attribute.getNamedItem(getVariables().ATTRIBUTE_TIPOATRIBUTO).getNodeValue();
					if(type.equals("")||type==null)
						createViolation(getWebSourceCode().getLineForNode(sibling), MESSAGE);
				}
				else{
					createViolation(getWebSourceCode().getLineForNode(sibling), MESSAGE);
				}
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
					validateTableName(child);
			}

		}
	}

	private void isNodeValid(Node node){
		NamedNodeMap attribute=node.getAttributes();
		Node type=attribute.getNamedItem(getVariables().attributeTypeERDiagram);
		if(type!=null&&getVariables().nodeTypeERDiagram.equals(type.getNodeValue()))
			validateTableName(node);

	}
	@Override
	public void validate(XmlSourceCode xmlSourceCode) {
		setWebSourceCode(xmlSourceCode);

		Document document = getWebSourceCode().getDocument(false);
		if (document.getDocumentElement() != null) {
			validateTableName(document.getDocumentElement());
		}
	}

}
