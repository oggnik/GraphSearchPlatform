package gsp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class Maze {

	private Tile[][] maze;
	private int height, width, goalX, goalY, startX, startY;

	enum GraphSearchMode {
		BFS, DFS, ASTAR
	}

	public Maze(int height, int width) {
		this.height = height;
		this.width = width;
		maze = new Tile[height][width];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int traversable = Math.random() > .1 ? 1 : -1;
				// int cost = (int) (traversable * Math.random() * 10.0);
				int cost = traversable;
				maze[y][x] = new Tile(x, y, cost);
			}
		}
		while (startX == goalX && startY == goalY) {
			this.goalX = (int) (Math.random() * width);
			this.goalY = (int) (Math.random() * height);
			this.startX = (int) (Math.random() * width);
			this.startY = (int) (Math.random() * height);
		}
		maze[goalY][goalX] = new Tile(goalX, goalY, 1);
		maze[startY][startX] = new Tile(startX, startY, 1);
	}

	public Tile getTile(int x, int y) {
		if (x < width && x >= 0 && y < height && y >= 0)
			return maze[y][x];
		return null;
	}

	public void printMaze() {
		printMaze(getStringMaze());
	}

	private void printMaze(String[][] strMaze) {
		for (String[] arr : strMaze) {
			for (String str : arr) {
				System.out.print(str);
			}
			System.out.println();
		}
	}

	private String[][] getStringMaze() {
		String[][] strMaze = new String[height][width];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Tile tile = maze[y][x];
				String result;
				if (startX == tile.getX() && startY == tile.getY()) {
					System.out.println(startX+"=="+tile.getX()+"&"+startY+"=="+tile.getY());
					result = "ME";
				} else if (tile.isGoal(goalX, goalY)) {
					result = "GL";
				} else if (tile.isTraversable()) {
					result = "[]";
				} else {
					result = "><";
				}
				strMaze[y][x] = result;
			}
		}
		return strMaze;
	}

	public void printShortestPath(GraphSearchMode mode) {
		List<Tile> path = calculateShortestPath(mode);
		if (path == null) {
			// No path, print maze
			printMaze(getStringMaze());
			return;
		}
		String[][] strMaze = getStringMaze();
		for (Tile tile : path) {
			strMaze[tile.getY()][tile.getX()] = "██";
		}
		printMaze(strMaze);
	}

	private List<Tile> getAdjacentTiles(Tile tile) {
		return getAdjacentTiles(tile.getX(), tile.getY());
	}

	private List<Tile> getAdjacentTiles(int x, int y) {
		ArrayList<Tile> adjTiles = new ArrayList<Tile>();
		Tile up = getTile(x, y + 1);
		if (up != null)
			adjTiles.add(up);
		Tile down = getTile(x, y - 1);
		if (down != null)
			adjTiles.add(down);
		Tile left = getTile(x - 1, y);
		if (left != null)
			adjTiles.add(left);
		Tile right = getTile(x + 1, y);
		if (right != null)
			adjTiles.add(right);
		return adjTiles;
	}

	public List<Tile> calculateShortestPath(GraphSearchMode mode) {
		switch (mode) {
		case DFS:
			return calculateDFS();
		case ASTAR:
			return calculateAStar();
		default:
			return calculateBFS();
		}
	}

	private List<Tile> calculateBFS() {
		Tile start = maze[startY][startX];
		start.setVisited(true);
		Queue<Tile> tiles = new LinkedList<Tile>();
		tiles.add(start);
		while (!tiles.isEmpty()) {
			Tile current = tiles.remove();
			if (current.isGoal(goalX, goalY)) {
				// This is the goal, return the list of nodes in the chain
				ArrayList<Tile> path = new ArrayList<Tile>();
				// Don't add the final one to the path (printing reasons)
				current = current.getParent();
				while (current.getParent() != null) {
					path.add(0, current);
					current = current.getParent();
				}
				return path;
			}
			// It is not the goal, add the unvisited neighbors
			List<Tile> neighbors = getAdjacentTiles(current);
			for (Tile t : neighbors) {
				if (!t.isVisited()) {
					// Unvisited, lets go there
					t.setVisited(true);
					// Set the parent
					t.setParent(current);
					// Push the tile onto the tiles to visit stack
					tiles.add(t);
				}
			}
		}
		return null;
	}

	private List<Tile> calculateDFS() {
		Tile start = maze[startY][startX];
		start.setVisited(true);
		Stack<Tile> tiles = new Stack<Tile>();
		tiles.push(start);
		while (!tiles.isEmpty()) {
			Tile current = tiles.pop();
			if (current.isGoal(goalX, goalY)) {
				// This is the goal, return the list of nodes in the chain
				ArrayList<Tile> path = new ArrayList<Tile>();
				// Don't add the final one to the path (printing reasons)
				current = current.getParent();
				while (current.getParent() != null) {
					path.add(0, current);
					current = current.getParent();
				}
				return path;
			}
			// It is not the goal, add the unvisited neighbors
			List<Tile> neighbors = getAdjacentTiles(current);
			for (Tile t : neighbors) {
				if (!t.isVisited()) {
					// Unvisited, lets go there
					t.setVisited(true);
					// Set the parent
					t.setParent(current);
					// Push the tile onto the tiles to visit stack
					tiles.push(t);
				}
			}
		}
		return null;
	}

	private List<Tile> calculateAStar() {
		// TODO Auto-generated method stub
		return null;
	}
}
