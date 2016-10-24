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

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
/**
 * 
 * @author jorge
 *
 */
public class TreeAbstract{
	private static TreeAbstract instance;
	private static XmlSourceCode instancexml;
	ArrayList<Nodo> arbol = new ArrayList<>();
	
	private TreeAbstract(XmlSourceCode archivoXml) {
		Document document = archivoXml.getDocument(false);
		if (document.getDocumentElement() != null) {
			instancexml=archivoXml;
			makeTree(document.getDocumentElement());
		}
	}
	/**
	 * 
	 * @param xml
	 * @return
	 */
	public static TreeAbstract getInstance(XmlSourceCode xml){
		if(instancexml!=xml){
			instance=null;
		}
		if(instance==null){
			instance=new TreeAbstract(xml);
		}
		return instance;
	}
	
	public ArrayList<Nodo> getArbol() {
		return arbol;
	}


	public void setArbol(ArrayList<Nodo> arbol) {
		this.arbol = arbol;
	}

	

	private void makeTree(Node node) {

		int pos = 0;

		for(Node sibling=node.getFirstChild();sibling!= null;sibling=sibling.getNextSibling()){
			if(sibling.getNodeName().equals(getVariables().getNodeListTaskCtt()))
			{
				NamedNodeMap attribute=sibling.getAttributes();
				String nombre = "";
				Node name=attribute.getNamedItem(getVariables().ATTRIBUTE_NAME);
				if(name!=null){
					nombre=name.getNodeValue();
				}
				String tipo = attribute.getNamedItem(getVariables().ATTRIBUTE_XSI_TYPE).getNodeValue();
				arbol.add(new Nodo (nombre, tipo, pos, sibling));
				pos++;
			}

		}


		for(Node sibling=node.getFirstChild();sibling!= null;sibling=sibling.getNextSibling()){
			if(sibling.getNodeName().equals(getVariables().getNodeListRelationTask()))
			{
				NamedNodeMap attribute=sibling.getAttributes();
				String tipo = attribute.getNamedItem(getVariables().ATTRIBUTE_XSI_TYPE).getNodeValue();
				String source = attribute.getNamedItem(getVariables().ATTRIBUTE_SOURCE).getNodeValue();
				String target = attribute.getNamedItem(getVariables().ATTRIBUTE_TARGET).getNodeValue();

				if (!(source.equals("") || target.equals(""))){
					String[] sourceText = source.split("\\.");
					int posSource = Integer.parseInt(sourceText[sourceText.length-1]);
					String[] targetText = target.split("\\.");
					int posTarget = Integer.parseInt(targetText[targetText.length-1]);
					int tarjetNode=-1;
					int sourceNode=-1;				

					for (int i = 0; i < arbol.size(); i++){

						if (arbol.get(i).getPosicion() == posSource){
							sourceNode = i;
						}
						if (arbol.get(i).getPosicion() == posTarget){
							tarjetNode = i;
						}
					}

					if (sourceNode != -1 && tarjetNode != -1){

						if (tipo.equals(getVariables().getCttAgregation())){

							boolean control1 = false; // TRUE = EL NODO NO TIENE ASOCIADO UN PADRE
							boolean control2 = false; // TRUE = EL NODO NO APARECE EN LA LISTA DE HIJOS
							
							if (arbol.get(tarjetNode).getPadre() == -1){
								control1 = true;
							}
							
							if (arbol.get(sourceNode).verificarHijo(tarjetNode)){
								control2 = true;
							}
							
							if (control1){
								arbol.get(tarjetNode).setPadre(sourceNode);
								
								if(control2){
									arbol.get(sourceNode).getHijosPos().add(tarjetNode);
								}else{
									//TODO REPORTAR ERROR, HIJO AGREGADO ANTERIORMENTE (DOBLE RELACIÓN PADRE HIJO)									
								}
								
							}else{
								//TODO REPORTAR ERROR, PADRE EXISTENTE ANTERIORMENTE
							}
							
						}
						else{
							//TODO ERROR, NODO NO EXISTE O NO CREADO
						}

						if (tipo.equals(getVariables().getCttEnabling())){
							
							boolean control1 = false; //TRUE EL TARGET NO TIENE RELACIÓN ANTERIOR
							boolean control2 = false; //TRUE EL SOURCE NO TIENE RELACIÓN SIGUIENTE
							
							if (arbol.get(tarjetNode).getAnteriorNodo() == -1){
								control1 = true;
							}
							
							if (arbol.get(sourceNode).getSiguienteNodo() == -1){
								control2 = true;
							}
							
							if (control1&& control2){
								arbol.get(sourceNode).setSiguienteNodo(tarjetNode);	
								arbol.get(sourceNode).setSiguienteRelation(getVariables().getCttEnabling());

								arbol.get(tarjetNode).setAnteriorNodo(sourceNode);
								arbol.get(tarjetNode).setAmteriorRelation(getVariables().getCttEnabling());
							}else{
								//TODO REPORTAR ERROR DE RELACIÓN REDUNDANTE
							}

						}
						
							if (tipo.equals(getVariables().getCttDisablig())){
							
							boolean control1 = false; //TRUE EL TARGET NO TIENE RELACIÓN ANTERIOR
							boolean control2 = false; //TRUE EL SOURCE NO TIENE RELACIÓN SIGUIENTE
							
							if (arbol.get(tarjetNode).getAnteriorNodo() == -1){
								control1 = true;
							}
							
							if (arbol.get(sourceNode).getSiguienteNodo() == -1){
								control2 = true;
							}
							
							if (control1&& control2){
								arbol.get(sourceNode).setSiguienteNodo(tarjetNode);	
								arbol.get(sourceNode).setSiguienteRelation(getVariables().getCttDisablig());

								arbol.get(tarjetNode).setAnteriorNodo(sourceNode);
								arbol.get(tarjetNode).setAmteriorRelation(getVariables().getCttDisablig());
							}else{
								//TODO REPORTAR ERROR DE RELACIÓN REDUNDANTE
							}

						}

						if (tipo.equals(getVariables().getCttIndependentConcurrency())){
							boolean control1 = false; //TRUE EL TARGET NO TIENE RELACIÓN ANTERIOR
							boolean control2 = false; //TRUE EL SOURCE NO TIENE RELACIÓN SIGUIENTE
							
							if (arbol.get(tarjetNode).getAnteriorNodo() == -1){
								control1 = true;
							}
							
							if (arbol.get(sourceNode).getSiguienteNodo() == -1){
								control2 = true;
							}
							
							if (control1&& control2){
								arbol.get(sourceNode).setSiguienteNodo(tarjetNode);	
								arbol.get(sourceNode).setSiguienteRelation(getVariables().getCttIndependentConcurrency());

								arbol.get(tarjetNode).setAnteriorNodo(sourceNode);
								arbol.get(tarjetNode).setAmteriorRelation(getVariables().getCttIndependentConcurrency());
							}else{
								//TODO REPORTAR ERROR DE RELACIÓN REDUNDANTE
							}
						}	

					}
					else{
						//TODO REPORTAR ERROR DE CONEXIÓN DE NODO
						//NODO SOURCE O TARGET NO ENCONTRADO
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
						makeTree(child);
				}

			}

		}
	private void isNodeValid(Node node){
		NamedNodeMap attribute=node.getAttributes();
		Node type=attribute.getNamedItem(getVariables().getAttributeTypeDiagramCtt());
		if(type!=null&&getVariables().getNodeTypeDiagramCtt().equals(type.getNodeValue()))
			makeTree(node);	
	}

	private Variables getVariables() {
		// TODO Auto-generated method stub
		return Variables.getInstance();
	}






	}
