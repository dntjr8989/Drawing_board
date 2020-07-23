package drawing_board_;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Stack;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;

import drawing_board_.SubFrame.Moven;

import static drawing_board_.Canvas.imageshape;
import static drawing_board_.Canvas.tempimage;

//전체 프레임

public class MainFrame extends JFrame{

	public static final int DEFAULT = 0;
	public static final int LINE = 1;
	public static final int RECT = 2;
	public static final int CIRCLE = 3;
	public static final int PENCIL = 4;
	public static final int ERASE = 5;
	public static final int ERASER = 6;
	public static final int UNDO = 7;
	public static final int REDO = 8;
	public static final int CHARACTER = 9;
	
	//프레임 안에 있는 요소들
	int option = 0;
	int thick = 5;
	int eraserthick = 15;
	
	//설정을 위한 변수들
	Color mypencolor = Color.black;
	Color myfillcolor = Color.white;
	JColorChooser pen, fill;
	
	Canvas canvas;
	
	JMenuBar menubar = new JMenuBar();
	JPanel toolbar = new JPanel();
	
	SpinnerNumberModel scroll = new SpinnerNumberModel(5, 1, 30, 1);
	JSpinner spinner = new JSpinner(scroll);
	
	BufferedImage bi = new BufferedImage(1800,1000,BufferedImage.TYPE_INT_RGB);

	//프레임 설정해주는 곳
	public MainFrame() {

		canvas = new Canvas(option, mypencolor, myfillcolor, thick, eraserthick);
		
		//프레임 설정
		setTitle("그림판");
		setBounds(60, 60, 1800, 1000);
		setLayout(null);
		setResizable(false); 
		setVisible(true);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//fillcolor 하얀색이면 투명도
		if(myfillcolor == Color.white)
		{
			myfillcolor = new Color(255, 255, 255, 0);
		}

		//아이템들 위치 선정
		this.add(canvas);
		this.add(toolbar);
		setJMenuBar(menubar);
		canvas.setBounds(60, 60, 1800, 1000);
		canvas.setBackground(Color.white);
		canvas.setLocation(0,50);

		
		//메뉴를 위한 정보 입력
		JMenu file = new JMenu("File");
		menubar.add(file);
		JMenuItem save = new JMenuItem("Save");
		JMenuItem exit = new JMenuItem("Exit");
		file.add(save);
		file.add(exit);
		save.addActionListener(new SaveAction());
		exit.addActionListener(new ExitAction());

		//툴바 설정
		toolbar.setBackground(Color.gray);
		toolbar.setBounds(0, 0, 1800, 100);
		toolbar.setLayout(new FlowLayout());
		
		JButton[] btn = new JButton[6]; 
		btn[0] = new JButton("Line");
		btn[1] = new JButton("Rectangular");
		btn[2] = new JButton("Circle");
		btn[3] = new JButton("Pencil");
		btn[4] = new JButton("Character");
		btn[5] = new JButton("동작");
		
		for(int i = 0; i<btn.length; i++) {
			toolbar.add(btn[i]);
		
			btn[i].setPreferredSize(new Dimension(80,40));
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
		
			sbtn[i].setPreferredSize(new Dimension(80,40));
			sbtn[i].addActionListener(new ButtonAction());
		}

		JLabel thickLabel = new JLabel("두께");
		thickLabel.setPreferredSize(new Dimension(40,40));
		toolbar.add(thickLabel);
		toolbar.add(spinner);

		spinner.addChangeListener(new SpinnerDetector());
	}

	class SpinnerDetector implements ChangeListener{

		@Override
		public void stateChanged(ChangeEvent e) {
			
			thick = (Integer)spinner.getValue();
		}

	}
	class SaveAction implements ActionListener{
		public void actionPerformed (ActionEvent e) {
			JFrame t = new JFrame();
			t.setLayout(new GridLayout());
			t.setSize(500, 100);
			t.setLocation(500, 500);
			JTextField tt = new JTextField("");
			JButton ttt = new JButton("확인");
			t.add("West", tt);
			t.add("East", ttt);
			t.setVisible(true);
			ttt.addActionListener(new ActionListener() {
			      
		    	public void actionPerformed(ActionEvent e)
		    	{
					canvas.printAll(bi.getGraphics());
					try {
						OutputStream out = new FileOutputStream(tt.getText());
						try {
				               ImageIO.write(bi,"PNG",new File(tt.getText()));
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
		    	}
		   });
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
				if(option == LINE) option = DEFAULT;
				else option = LINE;
				canvas.canvasupdate(option, mypencolor, myfillcolor, thick, eraserthick);
			}
			else if(temp.equals("Rectangular")) {
				if(option == RECT)option = DEFAULT;
				else option = RECT;
				canvas.canvasupdate(option, mypencolor, myfillcolor, thick, eraserthick);
			}
			else if(temp.equals("Circle")) {
				if(option == CIRCLE)option = DEFAULT;
				else option = CIRCLE;
				canvas.canvasupdate(option, mypencolor, myfillcolor, thick, eraserthick);
			}
			
			else if(temp.equals("Pencil")) {
				if(option == PENCIL)option = DEFAULT;
				option = PENCIL;
				canvas.canvasupdate(option, mypencolor, myfillcolor, thick, eraserthick);
			}
			else if(temp.equals("Character")) {
				
				tempimage = new Imagefile();
				option = DEFAULT;
				new SubFrame();
				canvas.canvasupdate(option, mypencolor, myfillcolor, thick, eraserthick);
			}
			else if(temp.equals("동작")) {
				option = DEFAULT;
				if(imageshape.size() > 0)
				{
					new Thread()
					{
						public void run()
						{
							int j=0;
							while( true )
							{
								try {
									Thread.sleep(500);
								}
								catch(Exception E3)
								{
									E3.printStackTrace();
								}
								option = DEFAULT;
								int i;
								int flag = 0;
								for( i=0; i < imageshape.size(); i++)
								{
									if(imageshape.elementAt(i).moveinform.tn > j)
									{
										flag = 1;
				
										if(imageshape.elementAt(i).moveinform.t[j] == 'r')
										{
											imageshape.elementAt(i).minx += 120;
										}
										else if(imageshape.elementAt(i).moveinform.t[j] == 'l')
										{
											imageshape.elementAt(i).minx -= 120;
										}
										else if(imageshape.elementAt(i).moveinform.t[j] == 'u')
										{
											imageshape.elementAt(i).miny -= 120;
										}
										else if(imageshape.elementAt(i).moveinform.t[j] == 'd')
										{
											imageshape.elementAt(i).miny += 120;
										}
									}
								}
								if(flag == 0)
								{
									break;
								}
								canvas.canvasupdate(option, mypencolor, myfillcolor, thick, eraserthick);
								canvas.revalidate();
								j++;
							}
						}
					}.start();
				}
				try {
		            Thread.sleep(100);
		         } catch (InterruptedException e3) {}
				canvas.canvasupdate(option, mypencolor, myfillcolor, thick, eraserthick);	
				canvas.revalidate();
			}
			
			else if(temp.equals("펜색")) {
				mypencolor = pen.showDialog(null, "색선정", Color.black);
				canvas.canvasupdate(option, mypencolor, myfillcolor, thick, eraserthick);
			}
			
			else if(temp.equals("채우기색")) {
				myfillcolor = fill.showDialog(null, "색선정", Color.white);
				//fillcolor 하얀색이면 투명도
				if(myfillcolor == Color.white)
				{
					myfillcolor = new Color(255, 255, 255, 0);
				}
				canvas.canvasupdate(option, mypencolor, myfillcolor, thick, eraserthick);
			}

			else if(temp.equals("지우개")) {
				if(option == ERASER) option = DEFAULT;
				else {
					option = ERASER;
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
								eraserthick = (int)source.getValue();
							}
						}
					});
				}
				canvas.canvasupdate(option, mypencolor, myfillcolor, thick, eraserthick);
			}

			else if(temp.equals("전체 지우기")) {
				option = ERASE;
				canvas.canvasupdate(option, mypencolor, myfillcolor, thick, eraserthick);
				canvas.repaint();
			}

			else if(temp.equals("Undo")) {
				option = UNDO;
					
				if(canvas.shapeisEmptyed()==false) {
					if(canvas.lastshapemoved() == 1) {
						canvas.pushredoshape(canvas.popshape());
						canvas.pushshape(canvas.popmoveshape());
					}
					else	canvas.pushredoshape(canvas.popshape());
				}
				canvas.canvasupdate(option, mypencolor, myfillcolor, thick, eraserthick);
				canvas.repaint();
			}
			else if(temp.equals("Redo")) {
				option = REDO;
				if(canvas.redoshapeisEmptyed() == false) {
					if(canvas.lastredoshapemoved() == 1) {
						canvas.pushmoveshape(canvas.popshape());
						canvas.pushshape(canvas.popredoshape());
					}
					else canvas.pushshape(canvas.popredoshape());
				}
				canvas.canvasupdate(option, mypencolor, myfillcolor, thick, eraserthick);
				canvas.repaint();
			}
		}
	}
}