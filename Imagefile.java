package drawing_board_;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Stack;

import drawing_board_.SubFrame.Moven;

class Imagefile{
	
	int minx=  0;
	int miny = 0;
	int maxx = 480;
	int maxy = 370;
	
	int width = 480;
	int height = 370;
	
	BufferedImage img;
	
	int moved = 0;
	int imagex = 0;
	int imagey = 0;

	Moven moveinform;
}