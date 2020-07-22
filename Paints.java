package drawing_board_;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

class Paints{
	int option;
	
	int minx= 1500;
	int miny = 1500;
	int maxx = 0;
	int maxy = 0;
	Point start = new Point(0,0);
	Point end = new Point(0,0);
	int width = 0;
	int height = 0;
	int size = 0;
	int moved = 0;
	ArrayList<Point> sketchSP = new ArrayList<Point>();	
		
	Color mypencolor = Color.white;
	Color myfillcolor = Color.white;
	int thick = 0;
	
}