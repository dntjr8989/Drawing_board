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

import drawing_board_.SubFrame.Moven;

import static drawing_board_.MainFrame.DEFAULT;
import static drawing_board_.MainFrame.LINE;
import static drawing_board_.MainFrame.RECT;
import static drawing_board_.MainFrame.CIRCLE;
import static drawing_board_.MainFrame.PENCIL;
import static drawing_board_.MainFrame.ERASE;
import static drawing_board_.MainFrame.ERASER;
import static drawing_board_.MainFrame.UNDO;
import static drawing_board_.MainFrame.REDO;
import static drawing_board_.Canvas.imageshape;
import static drawing_board_.Canvas.tempimage;
import static drawing_board_.MainFrame.CHARACTER;


//그림판 설정
	class Canvas extends JPanel {


		int option;
		Color mypencolor;
		Color myfillcolor;
		
		Point start = null;
		Point end = null;
		Point mouse = new Point(-10, -10);
		
		public static Imagefile tempimage = new Imagefile();

		int minx;
		int miny;
		int maxx;
		int maxy;
		int px, py;
		int width;
		int height;
		int move = 0;
		
		int psize = 0;
		int thick;
		int eraserthick;
		
		//각 도형들의 포인트를 저장해 놓는 벡터
		Vector<Point> sketSP = new Vector<Point>();
		Paints newshape;
		Stack<Paints> shape = new Stack<Paints>();
		Stack<Paints> redoshape = new Stack<Paints>();
		Stack<Paints> moveshape = new Stack<Paints>();
		
		public static Vector<Imagefile> imageshape = new Vector<Imagefile>();
		Stack<Imagefile> moveimage = new Stack<Imagefile>();
		Stack<Imagefile> redoimage = new Stack<Imagefile>();
		
		//리스너
		MyMouseListener m = new MyMouseListener();
		
		//그림판 마우스 리스너 불러주기
		public Canvas()
		{
			super();
		}
		public Canvas(int option, Color mypencolor, Color myfillcolor, int thick, int eraserthick){
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
			
			Graphics2D g2 =(Graphics2D)g;
			if(option == ERASE) {

				newshape.myfillcolor = Color.white;
				newshape.start = new Point(0, 0);
				newshape.end = new Point(1800, 1000);
				newshape.option = option;
				shape.add(newshape);
			}
					
			for(int i=0; i<imageshape.size(); i++)
			{
				g.drawImage(imageshape.elementAt(i).img, imageshape.elementAt(i).minx, imageshape.elementAt(i).miny, this);
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
			
			//그림자 그리기
			if(start != null) {
				
				for(int i=0; i<imageshape.size(); i++)
				{
					g.drawImage(imageshape.elementAt(i).img, imageshape.elementAt(i).minx, imageshape.elementAt(i).miny, this );
				}
				if(tempimage.moved == 1)
				{
					g.drawImage(tempimage.img, tempimage.minx, tempimage.miny, this);
				}

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

		//마우스 리스너 클래스
		class MyMouseListener extends MouseAdapter implements MouseMotionListener{
			int temp = 0;
			int makeinstance = 0;
			
			Paints newshape;
			Paints tempshape;
			int top = -1;
			
			int mousepointx, mousepointy;
			
			public MyMouseListener()
			{
		
			}
			//마우스 프레스 됐을 때
			public void mousePressed(MouseEvent e) {
					
				tempimage = new Imagefile();
					if(option == DEFAULT) {
					
					top = imageshape.size();
					mousepointx = e.getX();
					mousepointy = e.getY();
					while(imageshape.isEmpty() == false)
					{
						if(top == 0) break;
						
						top--;
						tempimage.img = imageshape.get(top).img;
						tempimage.minx = imageshape.get(top).minx;
						tempimage.maxx = imageshape.get(top).maxx;
						tempimage.miny = imageshape.get(top).miny;
						tempimage.maxy = imageshape.get(top).maxy;
						tempimage.imagex = imageshape.get(top).imagex;
						tempimage.imagey = imageshape.get(top).imagey;
						tempimage.moveinform = new Moven();
						tempimage.moveinform.tn = imageshape.get(top).moveinform.tn;
						
						for(int i=0; i<imageshape.get(top).moveinform.tn; i++)
					  	{
							tempimage.moveinform.t[i] = imageshape.get(top).moveinform.t[i];
					  	}
						tempimage.moved = 0;
						
						int maxx = tempimage.maxx;
						int maxy = tempimage.maxy;
						int minx = tempimage.minx;
						int miny = tempimage.miny;
						
						if(mousepointx <= maxx && mousepointx >= minx && mousepointy <= maxy && mousepointy>= miny) {
				
							tempimage.moved = 1;
							moveimage.push(imageshape.remove(top));
							move = 2;
							break;
						}
					}
					if(move != 2)
					{
						top = shape.size();
						while(shape.isEmpty() == false) {
	
							if(top == 0) break;
							tempshape = shape.get(--top);
							int maxx = tempshape.maxx;
							int maxy = tempshape.maxy;
							int minx = tempshape.minx;
							int miny = tempshape.miny;
							
							if(mousepointx <= maxx && mousepointx >= minx && mousepointy <= maxy && mousepointy>= miny) {
								moveshape.push(shape.remove(top));
								myfillcolor = tempshape.myfillcolor;
								mypencolor = tempshape.mypencolor;
								thick = tempshape.thick;
								move = 1;
								newshape = new Paints();
								
								break;
							}
						}
					}
				}
				if(makeinstance == 0 && (option == LINE || option == RECT || option == PENCIL ||  option == CIRCLE || option == ERASER)) {
					redoshape.removeAllElements();
					newshape = new Paints();
					
					if(option == ERASER) {
						newshape.mypencolor = Color.white;
						newshape.thick = eraserthick;
					}


					else {
						newshape.mypencolor = mypencolor;
						newshape.myfillcolor = myfillcolor;
						newshape.thick = thick;
					}
					newshape.option = option;
					makeinstance = 1;
				}

				
				if(e.getButton()==MouseEvent.BUTTON2) {
					option = DEFAULT;
				}

				else if(option == PENCIL || option == ERASER) {
					newshape.sketchSP.add(e.getPoint());
					sketSP.add(e.getPoint());
				}
				
				start = e.getPoint();
			}

			//마우스 릴리즈 됐을 때
			public void mouseReleased(MouseEvent e) {
				if(option != DEFAULT && move == 0) { shape.add(newshape); }
				if(move == 2)
				{
					imageshape.add(new Imagefile());
					imageshape.elementAt(imageshape.size()-1).imagex = tempimage.imagex;
					imageshape.elementAt(imageshape.size()-1).imagey = tempimage.imagey;
					imageshape.elementAt(imageshape.size()-1).minx = tempimage.minx;
					imageshape.elementAt(imageshape.size()-1).maxx = tempimage.maxx;
					imageshape.elementAt(imageshape.size()-1).miny = tempimage.miny;
					imageshape.elementAt(imageshape.size()-1).maxy = tempimage.maxy;
					imageshape.elementAt(imageshape.size()-1).img = tempimage.img;
					imageshape.elementAt(imageshape.size()-1).moveinform = new Moven();
					imageshape.elementAt(imageshape.size()-1).moveinform.tn = tempimage.moveinform.tn;
					
					for(int i=0; i<tempimage.moveinform.tn; i++)
				  	{
						System.out.println(tempimage.moveinform.t[i]);
				  		imageshape.elementAt(imageshape.size()-1).moveinform.t[i] =  tempimage.moveinform.t[i];
				  	}
					tempimage.moved = 0;
				}
				if(move == 1) {
					newshape.option =option; 
					newshape.minx = minx;
					newshape.miny = miny;
					newshape.maxx = maxx;
					newshape.maxy = maxy;
					newshape.width = width;
					newshape.height = height;
					newshape.start = start;
					newshape.end = end;
					newshape.size = psize;
					newshape.thick = thick;
					newshape.moved = 1;
					newshape.myfillcolor = myfillcolor;
					newshape.mypencolor = mypencolor;
				
					newshape.sketchSP.addAll(sketSP);
					shape.push(newshape);
					option = DEFAULT;
				}
				end = e.getPoint();
				
				if(option == ERASER) end = null;
				
				makeinstance = 0;
				
				if((option == RECT || option == CIRCLE)) {
					minx = (int)Math.min(start.getX(), end.getX());
					miny = (int)Math.min(start.getY(), end.getY());
					width = (int)Math.abs(start.getX()- end.getX());
					height = (int)Math.abs(start.getY()- end.getY());

					newshape.minx = minx;
					newshape.miny = miny;
					newshape.maxx = minx+width;
					newshape.maxy = miny+height;

					newshape.width = width;
					newshape.height = height;
				}
				else if(option == LINE ) {
					minx = (int)Math.min(start.getX(), end.getX());
					miny = (int)Math.min(start.getY(), end.getY());
					width = (int)Math.abs(start.getX()- end.getX());
					height = (int)Math.abs(start.getY()- end.getY());

					newshape.minx = minx;
					newshape.miny = miny;
					newshape.maxx = minx+width;
					newshape.maxy = miny+height;

					newshape.start = start;
					newshape.end = end;

				}
				else if(option == PENCIL || option == ERASER || move == 0) {

					sketSP.removeAllElements();
				}
				move = 0;
				repaint();
			}



			@Override
			//마우스 드레그 됐을 때
			public void mouseDragged(MouseEvent e) {
				
				px = e.getX() - mousepointx;
				py = e.getY() - mousepointy;
				// TODO Auto-generated method stub
				if(option == PENCIL || option == ERASER) {
					newshape.sketchSP.add(e.getPoint());
					sketSP.add(e.getPoint());
					newshape.maxx = (int)Math.max(e.getPoint().getX(), newshape.maxx);
					newshape.minx = (int)Math.min(e.getPoint().getX(), newshape.minx);
					newshape.maxy = (int)Math.max(e.getPoint().getY(), newshape.maxy);
					newshape.miny = (int)Math.max(e.getPoint().getY(), newshape.maxy);

				}
				end = e.getPoint();
				if(option == RECT || option == CIRCLE) {
					minx = (int)Math.min(start.getX(), end.getX());
					miny = (int)Math.min(start.getY(), end.getY());
					width = (int)Math.abs(start.getX()- end.getX());
					height = (int)Math.abs(start.getY() - end.getY());
				}

				if(move == 2)
				{
					tempimage.minx = tempimage.minx + px/50;
					tempimage.miny = tempimage.miny + py/50;
					tempimage.maxx = tempimage.maxx + px/50;
					tempimage.maxy = tempimage.maxy + py/50;
					repaint();
				}
				
				if(move == 1) {
					option = tempshape.option;
					minx = tempshape.minx + px;
					miny = tempshape.miny + py;
					maxx = tempshape.maxx + px;
					maxy = tempshape.maxy + py;
					width = tempshape.width;
					height = tempshape.height;
			
					start = new Point(tempshape.start.x + px, tempshape.start.y + py);
					end = new Point(tempshape.end.x + px, tempshape.end.y + py);
					psize = tempshape.size;
				
					for(int i = 0; i <tempshape.sketchSP.size(); i++) {
						Point pt; 
						pt = tempshape.sketchSP.get(i);
						pt.move(pt.x + px, pt.y + py);
						sketSP.add(pt);
					}

				}
				repaint();
			}

			@Override
			//마우스 움직일 때
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				mouse = e.getPoint();
			}
		}
		void canvasupdate(int option, Color mypencolor, Color myfillcolor, int thick, int eraserthick)
		{
			this.option = option;
			this.mypencolor = mypencolor;
			this.myfillcolor = myfillcolor;
			this.thick = thick;
			this.eraserthick = eraserthick;
			if(option == DEFAULT)
			{
				repaint();
			}
		}
		boolean shapeisEmptyed()
		{
			return shape.isEmpty();
		}
		int lastshapemoved()
		{
			return shape.get(shape.size()-1).moved;
		}
		boolean redoshapeisEmptyed()
		{
			return redoshape.isEmpty();
		}
		int lastredoshapemoved()
		{
			return redoshape.get(redoshape.size()-1).moved;
		}
		
		void pushredoshape(Paints t)
		{
			redoshape.push(t);
		}
		Paints popredoshape()
		{
			return redoshape.pop();
		}
		
		void pushshape(Paints t)
		{
			shape.push(t);
		}
		Paints popshape()
		{
			return shape.pop();
		}
		
		void pushmoveshape(Paints t)
		{
			moveshape.push(t);
		}
		Paints popmoveshape()
		{
			return moveshape.pop();
		}
	}
