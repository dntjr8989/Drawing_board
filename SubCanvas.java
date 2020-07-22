package drawing_board_;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Stack;
import java.util.Vector;

import javax.swing.JPanel;

import drawing_board_.Canvas.MyMouseListener;

import static drawing_board_.MainFrame.DEFAULT;
import static drawing_board_.MainFrame.LINE;
import static drawing_board_.MainFrame.RECT;
import static drawing_board_.MainFrame.CIRCLE;
import static drawing_board_.MainFrame.PENCIL;
import static drawing_board_.MainFrame.ERASE;
import static drawing_board_.MainFrame.ERASER;
import static drawing_board_.MainFrame.UNDO;
import static drawing_board_.MainFrame.REDO;

import static drawing_board_.SubFrame.img;
import static drawing_board_.SubFrame.imageflag;

class SubCanvas extends Canvas {

	//리스너
	MyMouseListener m = new MyMouseListener();
	
	//그림판 마우스 리스너 불러주기
	public SubCanvas(int option, Color mypencolor, Color myfillcolor, int thick, int eraserthick)
	{
		this.option = option;
		this.mypencolor = mypencolor;
		this.myfillcolor = myfillcolor;
		this.thick = thick;
		this.eraserthick = eraserthick;
		
		newshape = new Paints();
		
		addMouseListener(m);
		addMouseMotionListener(m);
		
	}

	//새로 그림 그리는 곳 
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		if(imageflag == 1)
		{
			 g.drawImage(img,0,0,this);
		}
		Graphics2D g2 =(Graphics2D)g;
		if(option == ERASE) {

			newshape.myfillcolor = Color.white;
			newshape.start = new Point(0, 0);
			newshape.end = new Point(1800, 1000);
			newshape.option = option;
			shape.add(newshape);
		}
		for(int i=0; i<shape.size(); i++) {
			g2.setPaint(shape.get(i).myfillcolor);
			g2.setStroke(new BasicStroke(shape.get(i).thick,BasicStroke.CAP_ROUND,0));
			switch(shape.get(i).option) {
			case LINE : 
				g2.setPaint(shape.get(i).mypencolor);
				g2.drawLine(shape.get(i).start.x, shape.get(i).start.y, shape.get(i).end.x, shape.get(i).end.y);
				break;
			case RECT :
				g2.fillRect(shape.get(i).minx, shape.get(i).miny, shape.get(i).width, shape.get(i).height);
				g2.setPaint(shape.get(i).mypencolor);
				g2.drawRect(shape.get(i).minx, shape.get(i).miny, shape.get(i).width, shape.get(i).height);
				break;
			case CIRCLE :
				g2.fillOval(shape.get(i).minx, shape.get(i).miny, shape.get(i).width, shape.get(i).height);
				g2.setColor(shape.get(i).mypencolor);
				g2.drawOval(shape.get(i).minx, shape.get(i).miny, shape.get(i).width, shape.get(i).height);
				break;
			case PENCIL : case ERASER :
				g2.setPaint(shape.get(i).mypencolor);
				for(int j = 1; j< shape.get(i).sketchSP.size(); j++) {
					g2.drawLine(shape.get(i).sketchSP.get(j-1).x, shape.get(i).sketchSP.get(j-1).y, shape.get(i).sketchSP.get(j).x, shape.get(i).sketchSP.get(j).y);
				}
				break;
			case ERASE :
				g2.setPaint(shape.get(i).myfillcolor);
				g2.fillRect(shape.get(i).start.x, shape.get(i).start.y, Math.abs(shape.get(i).end.x-shape.get(i).start.x), Math.abs(shape.get(i).end.y-shape.get(i).start.y));

			}
		}
	
		//바로 전 상황 그리기
		if(start != null) {

			if(option == ERASER) {
				g2.setColor(Color.white);
				g2.setStroke(new BasicStroke(eraserthick,BasicStroke.CAP_ROUND,0));
			}
			else {
				g2.setPaint(myfillcolor);
				g2.setStroke(new BasicStroke(thick,BasicStroke.CAP_ROUND,0));

			}
			if(option == LINE ) {
				g2.setPaint(mypencolor);
				g2.drawLine(start.x, start.y, end.x, end.y);
			}
			else if(option == RECT) {
				g2.fillRect(minx, miny, width, height);
				g2.setPaint(mypencolor);
				g2.drawRect(minx, miny, width, height);
			}
			else if(option == CIRCLE) {
				g2.fillOval(minx, miny, width, height);
				g2.setPaint(mypencolor);
				g2.drawOval(minx, miny, width, height);
			}
			else if(option == PENCIL) {
				g2.setPaint(mypencolor);
				for(int i = 1; i < sketSP.size(); i++) {

					g2.drawLine(sketSP.get(i-1).x, sketSP.get(i-1).y, sketSP.get(i).x, sketSP.get(i).y);
				}
			}
			else if(option == ERASER) {
				for(int i = 1; i < sketSP.size(); i++) {

					g2.drawLine(sketSP.get(i-1).x, sketSP.get(i-1).y, sketSP.get(i).x, sketSP.get(i).y);
				}

				g2.setStroke(new BasicStroke(1,BasicStroke.CAP_ROUND,0));
				g2.setColor(Color.black);
				if(end != null)
					g2.drawOval(end.x - eraserthick, end.y - eraserthick, eraserthick*2, eraserthick*2);

			}
		}
	}
}
