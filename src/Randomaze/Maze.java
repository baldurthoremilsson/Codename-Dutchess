package Randomaze;

import java.util.ArrayList;

public class Maze {
	private ArrayList<ArrayList<Cell>> m = new ArrayList<ArrayList<Cell>>(); //Matrix of Cells
	private int width; //width in Cells
	private int height; //height in Cells

	//constructor
	public Maze(int width, int height) {
		this.width = width;
		this.height = height;
		initMaze();
		setNeighbors();
	}

	//build the matrix, initializing all Cells
	private void initMaze() {
		for (int y = 0; y < height; y++) {
			m.add(new ArrayList<Cell>());
			for (int x = 0; x < width; x++) {
				m.get(y).add(new Cell(x, y));
			}
		}
	}

	//set the neighbors of each Cell in the maze
	private void setNeighbors() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				ArrayList<Cell> n = new ArrayList<Cell>();
				Cell c = this.getCell(x, y);
				if (y != 0) {
					n.add(this.getCell(x, y - 1));
				}
				if (y != height - 1) {
					n.add(this.getCell(x, y + 1));
				}
				if (x != 0) {
					n.add(this.getCell(x - 1, y));
				}
				if (x != width - 1) {
					n.add(this.getCell(x + 1, y));
				}
				c.setNeighbors(n);
			}
		}
	}

	//get Cell at given coordinates
	public Cell getCell(int x, int y) {
		return m.get(y).get(x);
	}
/*
	//Doesnt work on android - needs a lib
	//Draw every Cell in the maze
	public void drawMaze(Sketch s) {
		for (ArrayList<Cell> a : m) {
			for (Cell c : a) {
				c.drawCell(s);
			}
		}
	}
	*/
}
