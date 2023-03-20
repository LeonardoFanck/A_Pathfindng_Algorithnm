import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

public class DemoPanel extends JPanel{

	// CONFIGURAÇÕES DA TELA
	final int maxCol = 15;
	final int maxRow = 10;
	final int nodeSize = 70;
	final int screenWidth = maxCol * nodeSize;
	final int screenHeight = maxRow * nodeSize;
	
	// NODE
	Node[][] node = new Node[maxCol][maxRow];
	Node startNode, goalNode, currentNode;
	ArrayList<Node> openList = new ArrayList<>();
	ArrayList<Node> checkedList = new ArrayList<>();
	
	// OUTROS
	boolean goalReached = false;
	int step = 0;
	
	public DemoPanel() {
		
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setLayout(new GridLayout(maxRow, maxCol));
		this.addKeyListener(new KeyHandler(this));
		this.setFocusable(true);
		
		// COLOCANDO OS NODE
		int col = 0;
		int row = 0;
		
		while(col < maxCol && row < maxRow) {
			node[col][row] = new Node(col, row);
			this.add(node[col][row]);
			
			col++;
			if(col == maxCol) {
				col = 0;
				row++;
			}
		}	
		
		// SETTANDO O START E O GOAL
		setStartNode(3, 6);
		setGoalNode(11, 3);
		
		// COLOCANDO OS SOLID NODE
		setSolidNode(10, 2);
		setSolidNode(10, 3);
		setSolidNode(10, 4);
		setSolidNode(10, 5);
		setSolidNode(10, 6);
		setSolidNode(10, 7);
		setSolidNode(6, 2);
		setSolidNode(7, 2);
		setSolidNode(8, 2);
		setSolidNode(9, 2);
		setSolidNode(11, 7);
		setSolidNode(12, 7);
		setSolidNode(6, 1);
		
		// SETANDO OS CUSTOS
		setCostOnNodes();
		
	}
	
	private void setStartNode(int col, int row) {
		
		node[col][row].setAsStart();
		startNode = node[col][row];
		currentNode = startNode;
	}
	
	private void setGoalNode(int col, int row) {
		
		node[col][row].setAsGoal();
		goalNode = node[col][row];
	}
	
	private void setSolidNode(int col, int row) {
		
		node[col][row].setAsSolid();
	}
	
	private void setCostOnNodes() {
		
		int col = 0;
		int row = 0;
		
		while(col < maxCol && row < maxRow) {
			
			getCost(node[col][row]);
			col++;
			if(col == maxCol) {
				col = 0;
				row++;
			}
		}
	}
	
	private void getCost(Node node) {
		
		// PEGA O VALOR DE CUSTO DO 'G' - DISTÂNCIA DO START ATÉ O CURRENT
		int xDistance = Math.abs(node.col - startNode.col);
		int yDistance = Math.abs(node.row - startNode.row);
		node.gCost = xDistance + yDistance;
		
		// PEGA O VALOR DE CUSTO DO 'H' - DISTÂNCIA DO CURRENT ATÉ O GOAL
		xDistance = Math.abs(node.col - goalNode.col);
		yDistance = Math.abs(node.row - goalNode.row);
		node.hCost = xDistance + yDistance;
		
		// PEGA O VALOR DE CUSTO DO 'F' - TOTAL DA DISTÂNCIA - SOMA DO 'G' + 'H'
		node.fCost = node.gCost + node.hCost;
		
		// INSERI NA TELA O VALOR NO 'NODE'
		if(node != startNode && node != goalNode) {
			node.setText("<html>F: " + node.fCost + "<br>G: " + node.gCost + "<html>");
		}
	}
	
	public void search() {
		
		if(goalReached == false && step < 300) {
			int col = currentNode.col;
			int row = currentNode.row;
			
			currentNode.setAsChecked();
			checkedList.add(currentNode);
			openList.remove(currentNode);
			
			// ABRINDO A 'NODE' ACIMA DO 'CURRENT NODE'
			if(row - 1 >= 0) {
				openNode(node[col][row-1]);
			}
			
			// ABRINDO A 'NODE' A ESQUERDA DO 'CURRENT NODE'
			if(col - 1 >= 0) {
				openNode(node[col-1][row]);
			}
			
			// ABRINDO A 'NODE' ABAIXO DO 'CURRENT NODE'
			if(row + 1 < maxRow) {
				openNode(node[col][row+1]);
			}
			
			// ABRINDO A 'NODE' A DIREITA DO 'CURRENT NODE'
			if(col + 1 < maxCol) {
				openNode(node[col+1][row]);	
			}
			
			// ENCONTRANDO A MELHOR NODE
			int bestNodeIndex = 0;
			int bestNodeFCost = 999;
			
			for(int i = 0; i < openList.size(); i++) {
				// VERIFICANDO SE ESTE CUSTO DE F DO 'NODE' É O MELHOR
				if(openList.get(i).fCost < bestNodeFCost) {
					bestNodeIndex = i;
					bestNodeFCost = openList.get(i).fCost;
				}
				// SE O CUSTO DE 'F' É IGUAL, VERIFICA O CUSTO DE 'G'
				else if(openList.get(i).fCost == bestNodeFCost) {
					if(openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
						bestNodeIndex = i;
					}
				}
			}
			// DEPOIS DO LOOP, PEGAMOS A MELHOR 'NODE' QUE SERÁ NOSSO PROXIMO PASSO
			currentNode = openList.get(bestNodeIndex);
			
			if(currentNode == goalNode) {
				goalReached = true;
				trackThePath();
			}
		}
		step++;
	}
	
public void autoSearch() {
		
		while(goalReached == false) {
			int col = currentNode.col;
			int row = currentNode.row;
			
			currentNode.setAsChecked();
			checkedList.add(currentNode);
			openList.remove(currentNode);
			
			// ABRINDO A 'NODE' ACIMA DO 'CURRENT NODE'
			if(row - 1 >= 0) {
				openNode(node[col][row-1]);
			}
			
			// ABRINDO A 'NODE' A ESQUERDA DO 'CURRENT NODE'
			if(col - 1 >= 0) {
				openNode(node[col-1][row]);
			}
			
			// ABRINDO A 'NODE' ABAIXO DO 'CURRENT NODE'
			if(row + 1 < maxRow) {
				openNode(node[col][row+1]);
			}
			
			// ABRINDO A 'NODE' A DIREITA DO 'CURRENT NODE'
			if(col + 1 < maxCol) {
				openNode(node[col+1][row]);	
			}
			
			// ENCONTRANDO A MELHOR NODE
			int bestNodeIndex = 0;
			int bestNodeFCost = 999;
			
			for(int i = 0; i < openList.size(); i++) {
				// VERIFICANDO SE ESTE CUSTO DE F DO 'NODE' É O MELHOR
				if(openList.get(i).fCost < bestNodeFCost) {
					bestNodeIndex = i;
					bestNodeFCost = openList.get(i).fCost;
				}
				// SE O CUSTO DE 'F' É IGUAL, VERIFICA O CUSTO DE 'G'
				else if(openList.get(i).fCost == bestNodeFCost) {
					if(openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
						bestNodeIndex = i;
					}
				}
			}
			// DEPOIS DO LOOP, PEGAMOS A MELHOR 'NODE' QUE SERÁ NOSSO PROXIMO PASSO
			currentNode = openList.get(bestNodeIndex);
			
			if(currentNode == goalNode) {
				goalReached = true;
				trackThePath();
			}
		}
	}
	
	private void openNode(Node node) {
		
		if(node.open == false && node.checked == false && node.solid == false) {
			// SE O NODE NÃO ESTIVER ABERTO AINDA, ADICINAR PARA O 'OPEN LIST'
			node.setAsOpen();
			node.parent = currentNode;
			openList.add(node);
		}
	}
	
	private void trackThePath() {
		
		// RETROCEDE E DESENHA O MELHOR CAMINHO
		Node current = goalNode;
		
		while(current != startNode) {
			current = current.parent;
			
			if(current != startNode) {
				current.setAsPath();
			}
		}
	}
	
	
	
}
