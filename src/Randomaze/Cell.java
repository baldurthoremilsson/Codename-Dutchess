package Randomaze;

import java.util.ArrayList;

public class Cell {
	private final int IN = 1;
	private final int OUT = 2;
	private final int FRONTIER = 3;
	private int state = OUT; 
	// state of the cell, can be IN, OUT, or FRONTIER. Is
														
	private boolean start = false; // cell is start cell
	private boolean end = false; // cell is end cell
	private ArrayList<Cell> neighbors; 
	// neighbors of this cell
	private boolean northWall = true; // north wall exists
	private boolean eastWall = true; // east wall exists
	private boolean southWall = true; // south wall exists
	private boolean westWall = true; // west wall exists
	private int x; 
	// x coordinate of this cell in the maze
	private int y; 
	// y coordinate of this cell in the maze

	// constructor
	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}

	// set this Cell as visited
	public void setFrontier() {
		this.state = FRONTIER;
	}

	//set this Cell as IN and all OUT neighbors as FRONTIER
	public void setIn() {
		this.state = IN;
		for (Cell c : this.outNeighbors()) {
			c.setFrontier();
		}
	}

	//Return true if this Cell's state matches the given state
	public boolean stateIs(int s) {
		return this.state == s;
	}

	// is this the end cell?
	public boolean isEnd() {
		return end;
	}

	// is this the start cell?
	public boolean isStart() {
		return start;
	}

	// set this cell as start
	public void setStart() {
		this.start = true;
	}

	// set this cell as end
	public void setEnd() {
		this.end = true;
	}

	// get x coordinate
	public int getX() {
		return x;
	}

	// get y coordinate
	public int getY() {
		return y;
	}

	// set the neighbors array
	public void setNeighbors(ArrayList<Cell> n) {
		this.neighbors = n;
	}

	// get IN neighbors
	private ArrayList<Cell> inNeighbors() {
		ArrayList<Cell> list = new ArrayList<Cell>();
		for (Cell c : neighbors) {
			if (c.stateIs(IN))
				list.add(c);
		}
		return list;
	}

	// get FRONTIER neighbors
	public ArrayList<Cell> frontierNeighbors() {
		ArrayList<Cell> list = new ArrayList<Cell>();
		for (Cell c : neighbors) {
			if (c.stateIs(FRONTIER))
				list.add(c);
		}
		return list;
	}

	// get OUT neighbors
	public ArrayList<Cell> outNeighbors() {
		ArrayList<Cell> list = new ArrayList<Cell>();
		for (Cell c : neighbors) {
			if (c.stateIs(OUT))
				list.add(c);
		}
		return list;
	}

	// pick a random IN neighbor
	public Cell randomInNeighbor() {
		return (Cell) Rand.pickRandom(inNeighbors());
	}

	// break walls between this cell and given cell
	public void breakWall(Cell n) {
		if (x == n.x) {
			if (y < n.y) {
				southWall = false;
				n.northWall = false;
			}
			if (y > n.y) {
				northWall = false;
				n.southWall = false;
			}
		} else if (y == n.y) {
			if (x < n.x) {
				eastWall = false;
				n.westWall = false;
			}
			if (x > n.x) {
				westWall = false;
				n.eastWall = false;
			}
		}
	}
/*
	// draw this cell to given Sketch - doesnt work on android
	public void drawCell(Sketch s) {
		// calculate origin (top-left corner) of this cell in the maze
		int originx = s.spacing + x * s.scale;
		int originy = s.spacing + y * s.scale;

		//draw colored block to show cell state
		//blue if OUT
		//green if FRONTIER
		//stays black if IN
		int x = 0;
		if (this.stateIs(OUT)) {
			s.stroke(s.color(0, 0, 100));
			s.fill(s.color(0, 0, 100));
			s.rect(originx + 1, originy + 1, s.scale - 2, s.scale - 2);
		} else if (this.stateIs(FRONTIER)) {
			s.stroke(s.color(0, 100, 0));
			s.fill(s.color(0, 100, 0));
			s.rect(originx + 1, originy + 1, s.scale - 2, s.scale - 2);
		}	

		s.stroke(153);
		s.fill(153);
		// if visited, draw intact walls
		if (northWall && !start)
			s.line(originx, originy, originx + s.scale, originy);
		if (southWall && !end)
			s.line(originx, originy + s.scale, originx + s.scale, originy + s.scale);
		if (eastWall)
			s.line(originx + s.scale, originy, originx + s.scale, originy + s.scale);
		if (westWall)
			s.line(originx, originy, originx, originy + s.scale);
	}
	*/
}
