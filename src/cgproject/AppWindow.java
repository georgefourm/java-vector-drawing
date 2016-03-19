package cgproject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import shapes.Circle;
import shapes.Line;
import shapes.Pen;
import shapes.Polygon;
import shapes.Rectangle;
import shapes.Shape;

public class AppWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	//Used to prevent automatic canvas resizing
	JPanel canvasContainer;
	//The Drawing Canvas
	Canvas currCanvas;
	//The List of Shapes
	JList<Shape> shapelist;
	//References
	ButtonGroup toolbarBtns;
	JMenu shapeMenu;
	JLabel statusLabel;
	
	public AppWindow(){
		super("Graphics Application");
		
		canvasContainer=new JPanel(new FlowLayout(FlowLayout.LEFT));
		JScrollPane mainPane= new JScrollPane(canvasContainer);
		
		JMenuBar menubar = createMenu();
		JToolBar toolbar = createToolbar();
		shapelist = new JList<Shape>();
		shapelist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		shapelist.addListSelectionListener(new ListSelectionListener(){
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				shapeMenu.setEnabled(true);
			}		
		});
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,toolbar,
				new JScrollPane(shapelist));
		splitPane.setPreferredSize(new Dimension(150,150));
		JPanel statusPane=new JPanel();
		statusLabel=new JLabel();
		statusPane.setLayout(new BorderLayout());
		statusPane.add(statusLabel,BorderLayout.WEST);
		statusPane.setPreferredSize(new Dimension(getSize().width,20));	
		this.setLayout(new BorderLayout());
		this.add(statusPane,BorderLayout.SOUTH);
		this.add(splitPane,BorderLayout.WEST);
		this.add(mainPane,BorderLayout.CENTER);
		this.setJMenuBar(menubar);	
		this.setSize(new Dimension(700,400));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		showNewDialog();
		this.setVisible(true);		
	}
	
 	private JToolBar createToolbar() {
		JToolBar toolbar= new JToolBar("Shapes",JToolBar.VERTICAL);
		toolbar.setFloatable(false);
		toolbar.setLayout(new GridLayout(3,2,5,10));
		 
		toolbarBtns=new ButtonGroup();
		JToggleButton linebtn=new JToggleButton("Line");
		linebtn.setToolTipText("Select to draw Lines");
		linebtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(currCanvas!=null){
					currCanvas.setCurrShape(Line.class);
				}
			}
			
		});
		toolbarBtns.add(linebtn);
		
		JToggleButton circleBtn=new JToggleButton("Circle");
		circleBtn.setToolTipText("Select to draw Circles");
		circleBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(currCanvas!=null){
					currCanvas.setCurrShape(Circle.class);
				}
			}			
		});
		toolbarBtns.add(circleBtn);
		
		JToggleButton rectBtn=new JToggleButton("Rectangle");
		rectBtn.setToolTipText("Select to draw rectangles");
		rectBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(currCanvas!=null){
					currCanvas.setCurrShape(Rectangle.class);
				}
			}			
		});
		toolbarBtns.add(rectBtn);
		
		JToggleButton polyBtn=new JToggleButton("Polygon");
		polyBtn.setToolTipText("Click to draw Polygons");
		polyBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(currCanvas!=null){
					currCanvas.setCurrShape(Polygon.class);
				}
			}			
		});
		toolbarBtns.add(polyBtn);
		
		JButton cancelBtn=new JButton("Cancel");
		cancelBtn.setToolTipText("Cancel Drawing shape");
		cancelBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				toolbarBtns.clearSelection();
				currCanvas.cancelDrawing();
			}			
		});
		toolbarBtns.add(cancelBtn);
		
		JToggleButton penBtn=new JToggleButton("Free Line");
		penBtn.setToolTipText("Select to draw Free line");
		penBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				currCanvas.setCurrShape(Pen.class);
			}			
		});
		toolbarBtns.add(penBtn);		
	
		toolbar.add(linebtn);
		toolbar.add(penBtn);
		toolbar.add(circleBtn);
		toolbar.add(rectBtn);
		toolbar.add(polyBtn);
		toolbar.add(cancelBtn);
		
		return toolbar;
	}

	private JMenuBar createMenu() {
		JMenuBar menubar= new JMenuBar();
		JMenu fileMenu= new JMenu("File");
		JMenu canvasMenu= new JMenu("Canvas");
		shapeMenu= new JMenu("Shape");
		
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
			
		});
		JMenuItem newCanvas= new JMenuItem("New Canvas");
		newCanvas.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showNewDialog();
			}
			
		});
		
		JMenuItem save= new JMenuItem("Save State");
		save.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				currCanvas.saveState("saved.shp");
			}
			
		});
		
		JMenuItem load= new JMenuItem("Load State");
		load.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				currCanvas.loadState("saved.shp");
			}
			
		});
		
		JMenuItem clearCanvas= new JMenuItem("Clear");
		clearCanvas.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				currCanvas.clear();
			}
			
		});
		
		JMenuItem colorCanvas= new JMenuItem("Change color");
		colorCanvas.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				currCanvas.setBackground(JColorChooser.showDialog(getParent(), "Choose Color:",Color.GRAY));
			}
			
		});
		
		JMenuItem colorDraw= new JMenuItem("Change drawing color");
		colorDraw.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				currCanvas.setDrawingColor(JColorChooser.showDialog(getParent(), "Choose Color:",Color.black));
			}
			
		});
		
		JMenuItem moveShapeUp= new JMenuItem("Move Up");
		moveShapeUp.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int i=currCanvas.moveShape(shapelist.getSelectedIndex(), true);
				shapelist.setSelectedIndex(i);
			}			
		});
		
		JMenuItem moveShapeDown= new JMenuItem("Move Down");
		moveShapeDown.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int i=currCanvas.moveShape(shapelist.getSelectedIndex(), false);
				shapelist.setSelectedIndex(i);
			}			
		});
		
		JMenuItem changeColor= new JMenuItem("Change Color");
		changeColor.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				shapelist.getSelectedValue().setCurrColor(
						JColorChooser.showDialog(getParent(), "Choose Color:",Color.black));
				currCanvas.repaint();
			}			
		});
		
		JMenuItem changePoints= new JMenuItem("Change Points");
		changePoints.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showShapeDialog(shapelist.getSelectedIndex());
				currCanvas.repaint();
			}			
		});
		
		JMenuItem fillShape= new JMenuItem("Fill");
		fillShape.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				shapelist.getSelectedValue().setFilled(true);
				currCanvas.repaint();
			}			
		});
		
		JMenuItem deleteShape= new JMenuItem("Delete");
		deleteShape.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				currCanvas.listmodel.remove(shapelist.getSelectedIndex());
				shapeMenu.setEnabled(false);
				currCanvas.repaint();
			}			
		});	
		
		shapeMenu.add(changeColor);
		shapeMenu.add(changePoints);
		shapeMenu.add(fillShape);
		shapeMenu.add(deleteShape);
		shapeMenu.add(moveShapeUp);
		shapeMenu.add(moveShapeDown);
		shapeMenu.setEnabled(false);
	
		canvasMenu.add(clearCanvas);
		canvasMenu.add(colorDraw);
		canvasMenu.add(colorCanvas);
		
		fileMenu.add(save);
		fileMenu.add(load);
		fileMenu.add(newCanvas);
		fileMenu.addSeparator();
		fileMenu.add(exit);
		
		menubar.add(fileMenu);
		menubar.add(canvasMenu);
		menubar.add(shapeMenu);
		return menubar;
	}
	
	private void createCanvas(int width,int height){
		toolbarBtns.clearSelection();
		//Remove previous Canvas
		canvasContainer.removeAll();
		//Create and Add new Canvas
		currCanvas=new Canvas(Color.GRAY);
		currCanvas.setPreferredSize(new Dimension(width,height));
		canvasContainer.add(currCanvas);
		shapelist.setModel(currCanvas.listmodel);
		currCanvas.addMouseMotionListener(new MouseAdapter() {             
            public void mouseMoved(MouseEvent e){
            	statusLabel.setText("("+e.getX()+","+e.getY()+")");
            }
        });	
		validate();
		repaint();
	}
	
	private void showNewDialog(){
		JTextField widthField = new JTextField();
		JTextField heightField = new JTextField();
		final JComponent[] inputs = new JComponent[] {
				new JLabel("Width:"),
				widthField,
				new JLabel("Height:"),
				heightField
		};
		JOptionPane.showMessageDialog(this, inputs,
				"Enter Canvas Size", JOptionPane.PLAIN_MESSAGE);
		try{
			int width= Integer.parseInt(widthField.getText());
			int height= Integer.parseInt(heightField.getText());
			createCanvas(width,height);
		}catch(NumberFormatException e){
			JOptionPane.showMessageDialog(this, "Invalid input values! Creating Default Canvas");
			createCanvas(500,500);
		}
	}
	
	private void showShapeDialog(int selectedIndex){
		JTextField startXField = new JTextField();
		JTextField startYField = new JTextField();
		JTextField stopXField = new JTextField();
		JTextField stopYField = new JTextField();
		final JComponent[] inputs = new JComponent[] {
				new JLabel("Shape Start:"),
				startXField,startYField,
				new JLabel("Shape End:"),
				stopXField,stopYField
		};
		JOptionPane.showMessageDialog(this, inputs,
				"Enter Shape Points", JOptionPane.PLAIN_MESSAGE);
		try{
			int startX= Integer.parseInt(startXField.getText());
			int startY= Integer.parseInt(startYField.getText());
			int stopX= Integer.parseInt(stopXField.getText());
			int stopY= Integer.parseInt(stopYField.getText());
			Point newStart=new Point(startX,startY);
			Point newEnd=new Point(stopX,stopY);
			currCanvas.changeShapePoints(newStart, newEnd, selectedIndex);
		}catch(NumberFormatException e){
			JOptionPane.showMessageDialog(this, "Invalid input values! Shape not Affected");
		}
	}
}
