package drawing_board_;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import drawing_board_.Canvas;
import drawing_board_.MainFrame.ButtonAction;
import drawing_board_.MainFrame.SpinnerDetector;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import static drawing_board_.Canvas.imageshape;
import static drawing_board_.Canvas.tempimage;

public class SubFrame extends JFrame
{
   public static final int DEFAULT = 0;
   public static final int LINE = 1;
   public static final int RECT = 2;
   public static final int CIRCLE = 3;
   public static final int PENCIL = 4;
   public static final int ERASE = 5;
   public static final int ERASER = 6;
   public static final int UNDO = 7;
   public static final int REDO = 8;
   public static final int MoveRight = 9;
   public static final int MoveLeft = 10;
   public static final int MoveUp = 11;
   public static final int MoveDown = 12;
   public static final int CHARACTER = 13;
   public static final int SAVE = 14;
   
   
   //프레임 안에 있는 요소들
   int suboption = 0;
   int subthick = 2;
   int suberaserthick = 5;
   Imagefile t;
   
   public static class Moven
   {
	   char[] t = new char[100];
	   int tn = 0;
   }
   
   Moven moveinform = new Moven();
   
   //설정을 위한 변수들
   Color submypencolor = Color.black;
   Color submyfillcolor = Color.white;
   JColorChooser pen, fill;
      
   SubCanvas subcanvas = new SubCanvas(suboption, submypencolor, submyfillcolor, subthick, suberaserthick);
      
   JPanel toolbar = new JPanel();
   
   public static BufferedImage img = null;
   public static int imageflag = 0;
   
   SpinnerNumberModel scroll = new SpinnerNumberModel(2, 1, 20, 1);
   JSpinner spinner = new JSpinner(scroll);
   
   BufferedImage bi = new BufferedImage(480,370,BufferedImage.TYPE_INT_RGB); 
   
   public SubFrame()
   {
	 	  
      setTitle("SubFrame");
      setBounds(30, 30, 1000, 500);
      setLayout(new GridLayout(1, 2));
      setResizable(false); 
      setVisible(true);
      
      //fillcolor 하얀색이면 투명도
      if(submyfillcolor == Color.white)
      {
         submyfillcolor = new Color(255, 255, 255, 0);
      }
      
      toolbar.setLayout(new FlowLayout());
      
      JButton[] btn = new JButton[5]; 
      btn[0] = new JButton("ImageLoad");
      btn[1] = new JButton("Line");
      btn[2] = new JButton("Rectangular");
      btn[3] = new JButton("Circle");
      btn[4] = new JButton("Pencil");
      for(int i = 0; i<btn.length; i++) {
         toolbar.add(btn[i]);
      
         btn[i].addActionListener(new ButtonAction());
      }
      

      JButton[] sbtn = new JButton[6]; 
      sbtn[0] = new JButton("Undo");
      sbtn[1] = new JButton("Redo");
      sbtn[2] = new JButton("지우개");
      sbtn[3] = new JButton("전체 지우기");
      sbtn[4] = new JButton("펜색");
      sbtn[5] = new JButton("채우기색");

      for(int i = 0; i<sbtn.length; i++) {
         toolbar.add(sbtn[i]);
         sbtn[i].addActionListener(new ButtonAction());
      }

      JLabel thickLabel = new JLabel("두께");
      thickLabel.setPreferredSize(new Dimension(30,40));
      toolbar.add(thickLabel);
      toolbar.add(spinner);
      
      JButton save = new JButton("저장");
      JButton moveright = new JButton("오른쪽");
      JButton moveleft = new JButton("왼쪽");
      JButton moveup = new JButton("위");
      JButton movedown = new JButton("아래");
      
      toolbar.add(save);
      toolbar.add(moveright);
      toolbar.add(moveleft);
      toolbar.add(moveup);
      toolbar.add(movedown);
      
      save.addActionListener(new ButtonAction());
      moveright.addActionListener(new ActionListener()
    		  {
    		  		public void actionPerformed(ActionEvent e)
    		  		{
    		  			moveinform.t[moveinform.tn] = 'r';
    		  			moveinform.tn++;
    		  		}
    		  });
      moveleft.addActionListener(new ActionListener()
	  {
	  		public void actionPerformed(ActionEvent e)
	  		{
	  			moveinform.t[moveinform.tn] = 'l';
	  			moveinform.tn++;
	  		}
	  });
      moveup.addActionListener(new ActionListener()
	  {
	  		public void actionPerformed(ActionEvent e)
	  		{
	  			moveinform.t[moveinform.tn] = 'u';
	  			moveinform.tn++;
	  		}
	  });
      movedown.addActionListener(new ActionListener()
	  {
	  		public void actionPerformed(ActionEvent e)
	  		{
	  			moveinform.t[moveinform.tn] = 'd';
	  			moveinform.tn++;
	  		}
	  });

      this.add(toolbar);
      this.add(subcanvas);
      subcanvas.setBounds(30, 50, 500, 500);
      subcanvas.setBackground(Color.white);
      
      spinner.addChangeListener(new SpinnerDetector());
      
   }
   class SpinnerDetector implements ChangeListener{

      @Override
      public void stateChanged(ChangeEvent e) {
         
         subthick = (Integer)spinner.getValue();
      }

   }

   class ExitAction implements ActionListener{
      public void actionPerformed (ActionEvent e) {
         System.exit(0);
      }
   }
   
   //버튼 액션 리스너 클래스
   class ButtonAction implements ActionListener{
      public void actionPerformed (ActionEvent e) {
         
         JButton nowButton = (JButton)e.getSource();
      
         String temp = nowButton.getText();


         if(temp.equals("Line")) {
            if(suboption == LINE) suboption = DEFAULT;
            else suboption = LINE;
            subcanvas.canvasupdate(suboption, submypencolor, submyfillcolor, subthick, suberaserthick);
         }
         else if(temp.equals("Rectangular")) {
            if(suboption == RECT)suboption = DEFAULT;
            else suboption = RECT;
            subcanvas.canvasupdate(suboption, submypencolor, submyfillcolor, subthick, suberaserthick);
         }
         else if(temp.equals("Circle")) {
            if(suboption == CIRCLE)suboption = DEFAULT;
            else suboption = CIRCLE;
            subcanvas.canvasupdate(suboption, submypencolor, submyfillcolor, subthick, suberaserthick);
         }
         
         else if(temp.equals("Pencil")) {
            if(suboption == PENCIL)suboption = DEFAULT;
            suboption = PENCIL;
            subcanvas.canvasupdate(suboption, submypencolor, submyfillcolor, subthick, suberaserthick);
         }
         
         else if(temp.equals("ImageLoad")) {
            JFileChooser jfc = new JFileChooser();
             int returnVal = jfc.showOpenDialog(null);
            
             if(returnVal == 0) {
                File file = jfc.getSelectedFile();
                try {
               img = ImageIO.read(new File(jfc.getSelectedFile().toString()));
	            } catch (IOException e1) {
	               // TODO Auto-generated catch block
	               e1.printStackTrace();
	            }
                imageflag = 1;
                suboption = DEFAULT;
                subcanvas.canvasupdate(suboption, submypencolor, submyfillcolor, subthick, suberaserthick);
             }
             else
             {
                 System.out.println("파일 열기를 취소하였습니다.");
             }
         }
               
         else if(temp.equals("펜색")) {
            submypencolor = pen.showDialog(null, "색선정", Color.black);
            subcanvas.canvasupdate(suboption, submypencolor, submyfillcolor, subthick, suberaserthick);
         }
         
         else if(temp.equals("채우기색")) {
            submyfillcolor = fill.showDialog(null, "색선정", Color.white);
            //fillcolor 하얀색이면 투명도
            if(submyfillcolor == Color.white)
            {
               submyfillcolor = new Color(255, 255, 255, 0);
            }
            subcanvas.canvasupdate(suboption, submypencolor, submyfillcolor, subthick, suberaserthick);
         }

         else if(temp.equals("지우개")) {
            if(suboption == ERASER) suboption = DEFAULT;
            else {
               suboption = ERASER;
               JFrame eraser = new JFrame("지우개");
               eraser.setVisible(true);
               eraser.setSize(250,100);
               eraser.setLocation(200,200);

               JSlider eraserSize = new JSlider(JSlider.HORIZONTAL, 0, 50, 15);
               eraserSize.setMajorTickSpacing(10);
               eraserSize.setMinorTickSpacing(1);
               eraserSize.setPaintTicks(true);
               eraserSize.setPaintLabels(true);

               eraser.add(eraserSize);

               eraserSize.addChangeListener(new ChangeListener() {

                  @Override
                  public void stateChanged(ChangeEvent e) {
                     // TODO Auto-generated method stub
                     JSlider source = (JSlider)e.getSource();
                     if(!source.getValueIsAdjusting()) {
                        suberaserthick = (int)source.getValue();
                     }
                  }
               });
            }
            subcanvas.canvasupdate(suboption, submypencolor, submyfillcolor, subthick, suberaserthick);
         }

         else if(temp.equals("전체 지우기")) {
            suboption = ERASE;
            subcanvas.canvasupdate(suboption, submypencolor, submyfillcolor, subthick, suberaserthick);
            subcanvas.repaint();
         }

         else if(temp.equals("Undo")) {
            suboption = UNDO;
               
            if(subcanvas.shapeisEmptyed()==false) {
               if(subcanvas.lastshapemoved() == 1) {
                  subcanvas.pushredoshape(subcanvas.popshape());
                  subcanvas.pushshape(subcanvas.popmoveshape());
               }
               else   subcanvas.pushredoshape(subcanvas.popshape());
            }
            subcanvas.canvasupdate(suboption, submypencolor, submyfillcolor, subthick, suberaserthick);
            subcanvas.repaint();
         }
         else if(temp.equals("Redo")) {
            suboption = REDO;
            if(subcanvas.redoshapeisEmptyed() == false) {
               if(subcanvas.lastredoshapemoved() == 1) {
                  subcanvas.pushmoveshape(subcanvas.popshape());
                  subcanvas.pushshape(subcanvas.popredoshape());
               }
               else subcanvas.pushshape(subcanvas.popredoshape());
            }
            subcanvas.canvasupdate(suboption, submypencolor, submyfillcolor, subthick, suberaserthick);
            subcanvas.repaint();
         }
         
         else if(temp.equals("저장")) {
          suboption = SAVE;
          subcanvas.printAll(bi.getGraphics());
		  try {
				OutputStream out = new FileOutputStream("t" + imageshape.size() + ".png");
				try {
			           ImageIO.write(bi,"PNG",new File("t" + imageshape.size() + ".png"));
			           JFrame frame = new JFrame();
			           JOptionPane.showMessageDialog(frame, "저장완료");
			     }
			      catch(IOException e1)
			      {
			         	JFrame frame = new JFrame();
			        	JOptionPane.showMessageDialog(frame, "잘못된 경로입니다.!");
			             e1.printStackTrace();
			      }
			}
			catch (IOException ee)
			{
				JFrame frame = new JFrame();
	           	JOptionPane.showMessageDialog(frame, "잘못된 경로입니다.!");
	            ee.printStackTrace();
			}
		  
		  	try {
		  		img = ImageIO.read(new File("t" + imageshape.size() + ".png"));
		  	}
		  	catch(IOException e1)
		  	{
		  		e1.printStackTrace();
		  	}
		  	
		
		  	imageshape.add(new Imagefile());
			imageshape.elementAt(imageshape.size()-1).imagex = 200;
			imageshape.elementAt(imageshape.size()-1).imagey = 200;
			imageshape.elementAt(imageshape.size()-1).minx = 200;
			imageshape.elementAt(imageshape.size()-1).maxx = 680;
			imageshape.elementAt(imageshape.size()-1).miny = 200;
			imageshape.elementAt(imageshape.size()-1).maxy = 570;
			imageshape.elementAt(imageshape.size()-1).img = img;
			imageshape.elementAt(imageshape.size()-1).moveinform = new Moven();
			imageshape.elementAt(imageshape.size()-1).moveinform.tn = moveinform.tn;
			
			for(int i=0; i<moveinform.tn; i++)
		  	{
				System.out.println(moveinform.t[i]);
		  		imageshape.elementAt(imageshape.size()-1).moveinform.t[i] =  moveinform.t[i];
		  	}
			  	
		    dispose();
          }
      }
   }
}