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

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Variables {
	//CTT Diagram
	private boolean validateCttByType;
	private String attributeTypeDiagramCtt;
	private String nodeTypeDiagramCtt;
	private String nodeDiagramCtt;
	//Relaciones del diagrama Ctt
	private String cttAgregation;
	private String cttEnabling;
	private String cttDisablig;
	private String cttIndependentConcurrency;
	//Relaciones y Nodos del diagrama CTT
	private String nodeListRelationTask;
	private String nodeListTaskCtt;
	//name
	private  String cttAttributeName;
	//source
	private  String cttAttributeSource;
	//xsi:type
	private  String cttAttributeXsiType;
	//target
	private  String cttAttributeTarget;
	//Tipos de nodos del diagrama Ctt
	private  String cttAbstractionTask;
	private  String cttSystemTask;
	private  String cttUserTask;
	private  String cttInteractionTask;

	public String ATTRIBUTE_SOURCE="source";
	public String ATTRIBUTE_TARGET="target";
	public String ATTRIBUTE_XSI_TYPE="xsi:type";
	public String ATTRIBUTE_NAME="name";
	private static Variables instance;
	//nombre etiqueta diagrama clase
	public String CLASS_DIAGRAM_NAME="itsPackage";
	//valor xsi:type del diagrama de clase
	public String TYPE_CLASS="herramienta.diagrams.domain:Domain_Diagram";
	//etiqueta clase
	public String NODE_CLASS="itsClases";
	//ETIQUETA RELACION
	public String NODE_RELATIONSHIP_CLASS="itsArc";
	//etiqueta atributo del diagrama de clases
	public String NODE_ATTRIBUTE_CLASS="listaAtributos";
	//etiqueta metodos del diagrama de clases
	public String NODE_OPERATION_CLASS="listMetodos";
	//etiqueta de los atributos de un método
	public String NODE_ATTRIBUTE_OPERATION="listParametros";
	//atributo del tipo de atributo del diagrama de clase
	public String ATTRIBUTE_TYPE_ATTRIBUTENODE="tipo";
	//ATRIBUTO DEL CONTENIDO DE LA OPRACION DEL DIAGRAMA DE CLASES
	public String ATTRIBUTE_BODY_CLASS="body";
	public String ATTRIBUTE_RETURN_OPERATION_CLASS="returnType";
	//valor xsi:type de la relacion de herencia
	public String CLASS_INHERITED_RELATION="herramienta.diagrams.domain:Herencia";

	public String[] CLASS_RESERVED_WORDS={"void","int","if","public","void","static","for","class"};

	//CASE USE DIAGRAM
	public int NUMBER_USE_CASE=9;
	public int NUMBER_ACTORS=5;
	public int NUMBER_COMMUNICATIONS=9;
	//nombre etiqueta diagrama de casos de uso
	public String USE_CASE_DIAGRAM_NAME="usecase:Diagram";
	//ETIQUETA RELACION
	public String NODE_ACTOR="listActors";
	//etiqueta atributo del diagrama de clases
	public String NODE_USE_CASE="listUseCase";
	public String NODE_RELATION_USECASE="listRelation";
	public String RELATION_INCLUDE="usecase:Include";
	public String RELATION_EXCLUDE="usecase:Exclude";
	public String RELATION_HIEARCHY="usecase:Hiearchy";
	public String RELATIONS_ASSOCIATIONS="usecase:Associations";

	//E-R DIAGRAM
	public String ATTRIBUTE_TIPOATRIBUTO="tipo";
	public String XSITYPE_ER="xsi:type";
	public String NODE_ATTRIBUTE_ER="listAtributes";
	public String ER_DIAGRAM_NAME="er:Diagram";
	public String NODE_TABLE="listTable";
	public String NODE_RELATIONER="listRelation";
	public String RELATION_MANYTOMANY="er:ManyToMany";
	public String RELATION_ONETOONE="er:OneToOne";
	public String RELATION_ONETOONEOPTIONAL="er:OneToOneOptional";
	public String RELATION_ONETOMANYOPTIONAL="er:OneToManyOptional";
	//PERMITE DETERMINAR SI EL ATRIBUTO ES CLAVE PRIMARIA, FORANEA O UNICA
	public String PROPIEDAD_ATTRIBUTE="propiedad";
	public String[] ER_RESERVED_WORDS={"void","int","if","public","static","for","class"};
	//nombreAtributo ER
	public  String ATTRIBUTE_NAME_ATRIBUTE_TABLE="nombreAtributo";
	//nombreAtributo ER
	public  String ATTRIBUTE_NAME_TABLE="nombreTabla";

	public int MAX_MANY_TO_MANY_RELATIONS=3;
	public int MAX_COMPLEX_ESTRUCTURE=2;

	//BPMN CONSTANTES
	public String BPNM_MODEL_PROCESS="model:process";

	//BPMN START eVENT
	public String BPNM_START_EVENT="model:startEvent";
	public static Variables getInstance(){
		if(instance==null){
			instance=new Variables();
		}
		return instance;
	}


	private Variables() {

		inicializarVariables();
	}

	private void inicializarVariables() {
		try {
			Properties propiedades = new Properties();
			propiedades
			.load(getClass().getResourceAsStream("/variables/variables.properties"));
			//Variables del Diagrama CTT
			validateCttByType="true".equals(propiedades.getProperty("validateCttByType"));
			attributeTypeDiagramCtt=propiedades.getProperty("attributeTypeDiagramCtt");
			nodeTypeDiagramCtt=propiedades.getProperty("nodeTypeDiagramCtt");
			nodeDiagramCtt=propiedades.getProperty("nodeDiagramCtt");
			cttAgregation = propiedades.getProperty("cttAgregation");
			cttEnabling = propiedades.getProperty("cttEnabling");
			cttDisablig=propiedades.getProperty("cttDisablig");
			cttIndependentConcurrency=propiedades.getProperty("cttIndependentConcurrency");
			nodeListRelationTask=propiedades.getProperty("nodeListRelationTask");
			nodeListTaskCtt=propiedades.getProperty("nodeListTaskCtt");
			cttAttributeName=propiedades.getProperty("cttAttributeName");
			cttAttributeSource=propiedades.getProperty("cttAttributeSource");
			cttAttributeXsiType=propiedades.getProperty("cttAttributeXsiType");
			cttAttributeTarget=propiedades.getProperty("cttAttributeTarget");
			cttAbstractionTask=propiedades.getProperty("cttAbstractionTask");
			cttSystemTask=propiedades.getProperty("cttSystemTask");
			cttUserTask=propiedades.getProperty("cttUserTask");
			cttInteractionTask=propiedades.getProperty("cttInteractionTask");

		} catch (FileNotFoundException e) {
			System.out.println("Error, El archivo no exite");
		} catch (IOException e) {
			System.out.println("Error, No se puede leer el archivo");
		}
	}


	/**
	 * @return the validateCttByType
	 */
	public boolean isValidateCttByType() {
		return validateCttByType;
	}


	/**
	 * @return the nodeTypeDiagramCtt
	 */
	public String getNodeTypeDiagramCtt() {
		return nodeTypeDiagramCtt;
	}

	/**
	 * 
	 * @return the attributeTypeDiagramCtt
	 */
	public String getAttributeTypeDiagramCtt() {
		return attributeTypeDiagramCtt;
	}


	/**
	 * @return the nodeDiagramCtt
	 */
	public String getNodeDiagramCtt() {
		return nodeDiagramCtt;
	}


	/**
	 * @return the cttAgregation
	 */
	public String getCttAgregation() {
		return cttAgregation;
	}


	/**
	 * @return the cttEnabling
	 */
	public String getCttEnabling() {
		return cttEnabling;
	}


	/**
	 * @return the cttDisablig
	 */
	public String getCttDisablig() {
		return cttDisablig;
	}


	/**
	 * @return the cttIndependentConcurrency
	 */
	public String getCttIndependentConcurrency() {
		return cttIndependentConcurrency;
	}


	/**
	 * @return the nodeListRelationTask
	 */
	public String getNodeListRelationTask() {
		return nodeListRelationTask;
	}


	/**
	 * @return the nodeListTaskCtt
	 */
	public String getNodeListTaskCtt() {
		return nodeListTaskCtt;
	}


	/**
	 * @return the cttAttributeName
	 */
	public String getCttAttributeName() {
		return cttAttributeName;
	}


	/**
	 * @return the cttAttributeSource
	 */
	public String getCttAttributeSource() {
		return cttAttributeSource;
	}


	/**
	 * @return the cttAttributeXsiType
	 */
	public String getCttAttributeXsiType() {
		return cttAttributeXsiType;
	}


	/**
	 * @return the cttAttributeTarget
	 */
	public String getCttAttributeTarget() {
		return cttAttributeTarget;
	}


	/**
	 * @return the cttAbstractionTask
	 */
	public String getCttAbstractionTask() {
		return cttAbstractionTask;
	}


	/**
	 * @return the cttSystemTask
	 */
	public String getCttSystemTask() {
		return cttSystemTask;
	}


	/**
	 * @return the cttUserTask
	 */
	public String getCttUserTask() {
		return cttUserTask;
	}


	/**
	 * @return the cttInteractionTask
	 */
	public String getCttInteractionTask() {
		return cttInteractionTask;
	}







	/**
	public static String getNombreNodo(String source,Node node){

		if(source.equals("")||source==null)
		{
			return "";
		}
		String[] splitSource=source.split("\\.");
		int numeroNodo=Integer.parseInt(splitSource[splitSource.length-1]);
		String nombreNodo="";

		int contador=0;
		for (Node sibling=node.getFirstChild();sibling!= null;sibling=sibling.getNextSibling()) {

			if(sibling.getNodeName()==Variables.NODE_LIST_TASK_CTT)
			{
				if(contador==numeroNodo)
				{
					NamedNodeMap attribute=sibling.getAttributes();
					nombreNodo=attribute.getNamedItem(Variables.ATTRIBUTE_NAME).getNodeValue();
					return nombreNodo;
				}
				else
					contador++;
			}
		}
		return nombreNodo;
	}
	 */
}
