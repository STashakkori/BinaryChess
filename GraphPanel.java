package binarychess;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import javax.swing.*;
import javax.swing.event.*;

/***************************
 ****** Binary Chess *******
 ***************************
 * author: Sina Tashakkori *
 ***************************/
public class GraphPanel extends JComponent {
	private static final long serialVersionUID = 1L;
	private static final int WIDE = 640;
    private static final int HIGH = 480;
    private static final int RADIUS = 20;
    private ControlPanel control = new ControlPanel();
    private int radius = RADIUS;
    private Kind kind = Kind.Circular;
    private List<Node> nodes = new ArrayList<Node>();
    private List<Node> selected = new ArrayList<Node>();
    private static List<Edge> edges = new ArrayList<Edge>();
    private Point mousePt = new Point(WIDE / 2, HIGH / 2);
    private Rectangle mouseRect = new Rectangle();
    private boolean selecting = false;
    static String name = "Binary Node Chess";
    static int numOfVertices;
    static int[][] nodeArray;
    static boolean[] statusArray;
    static int[] xCoord;
    static int[] yCoord;
    static int gameCount;
    static int nodeCount;
    static boolean[] isPicked;
    static String winner;
    static boolean illegalMove;
    static boolean gameover;
    static boolean[] redStatus;
    static boolean[] blueStatus;
    static int gameStatus;
    
    static enum turnColor{
    	BLUE, 
    	RED
    }

    public static void main(String[] args) throws Exception {
    	gameCount = 0;
        JFrame f = new JFrame(name);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GraphPanel gp = new GraphPanel();
        //System.out.println("arr "+nodeArray.length);
        redStatus=new boolean[nodeArray.length];
        blueStatus=new boolean[nodeArray.length];
        for(int i = 0; i<nodeArray.length;i++){
        	for(int j = 0; j<nodeArray[i].length;j++){
        		if(nodeArray[i][j]!=-1 && statusArray[nodeArray[i][j]] == false){
        			Point pt = new Point(xCoord[nodeArray[i][j]],yCoord[nodeArray[i][j]]);
                    Node n = new Node(pt, RADIUS, Color.lightGray, Kind.Circular, nodeArray[i][j], false);
                    gp.nodes.add(n);
                    statusArray[nodeArray[i][j]] = true; 
        		}
        	}
        }
    	for(int a = 0; a<nodeArray.length;a++){
			if(nodeArray[a][0] != -1){
    			Point pt1 = new Point(xCoord[nodeArray[a][0]],yCoord[nodeArray[a][0]]);
        		Point pt2 = new Point(xCoord[nodeArray[a][1]],yCoord[nodeArray[a][1]]);
        		Node n1 = new Node(pt1, RADIUS, Color.lightGray, Kind.Circular, nodeArray[a][0], false);
				Node n2 = new Node(pt2, RADIUS, Color.lightGray, Kind.Circular, nodeArray[a][1], false);
                Edge e = new Edge(n1, n2);
                edges.add(e);
			}
			else{
				break;
			}
		}
    	gameStatus = 0;
        System.out.println("size "+gp.nodes.size());
        for(Edge pair : edges)
        	System.out.println("n1: "+pair.n1.nodeID+" n2: "+pair.n2.nodeID);
        f.add(gp.control, BorderLayout.NORTH);
        JScrollPane jp = new JScrollPane(gp);
        jp.setBackground(Color.green);
        f.add(jp, BorderLayout.CENTER);
        f.getRootPane().setDefaultButton(gp.control.defaultButton);
        f.pack();
        f.setLocationByPlatform(true);
        f.setVisible(true);
        gp.control.stat.setText("Whichever player is going first: click on a node of your choice to begin the game");
        while(gameCount!=nodeCount){
        	if(gameStatus == 1){
        		gp.control.stat.setText("GAME OVER: Tie game.");
        		gp.control.setBackground(Color.magenta);
        		jp.setBackground(Color.magenta);
        	}
        	if(gameStatus == 2) {
        		gp.control.stat.setText("GAME OVER: " + turnColor.RED + " wins.");
        		gp.control.setBackground(Color.red);
        		jp.setBackground(Color.red);
        	}
        	if(gameStatus == 3){
        		gp.control.stat.setText("GAME OVER: " + turnColor.BLUE + " wins.");
        		gp.control.setBackground(Color.blue);
        		jp.setBackground(Color.blue);
        	}
        	if(gameStatus == 0 && gameCount%2==0) gp.control.stat.setText("It is " + turnColor.BLUE + " player's turn: please choose a node.");
        	else if(gameStatus == 0 && gameCount%2!=0) gp.control.stat.setText("It is " + turnColor.RED + " player's turn: please choose a node.");
        }
        gp.control.stat.setText("GAME OVER");
	}
    public GraphPanel() {
    	BinaryChess bin = new BinaryChess();
        nodeArray = bin.nodeArray.clone();
        numOfVertices = bin.seenBefore.length;
        System.out.println("size of node "+nodeArray.length);
        statusArray = bin.seenBefore.clone();
        xCoord = bin.xPositionList.clone();
     	yCoord = bin.yPositionList.clone();
     	nodeCount = bin.seenBefore.clone().length;
     	boolean[] isPicked = new boolean[statusArray.length];
     	for(int i=0;i<statusArray.length;i++)
     		isPicked[i] = false;
        this.setOpaque(true);
        this.addMouseListener(new MouseHandler());
        this.addMouseMotionListener(new MouseMotionHandler());
        winner = "";
        gameover = false;
    }
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDE, HIGH);
    }
    @Override
    public void paintComponent(Graphics g) {
        g.setColor(new Color(0x00f0f0f0));
        g.fillRect(0, 0, getWidth(), getHeight());
        for (Edge e : edges) {
            e.draw(g);
        }
	        for (Node n : nodes) {
	            n.draw(g);
	        }
        if (selecting) {
            g.setColor(Color.lightGray);
            g.drawRect(mouseRect.x, mouseRect.y,
                mouseRect.width, mouseRect.height);
        }
    }
    private class MouseHandler extends MouseAdapter {
        @Override
        public void mouseReleased(MouseEvent e) {
            selecting = false;
            mouseRect.setBounds(0, 0, 0, 0);
            if (e.isPopupTrigger()) {
                showPopup(e);
            }
            e.getComponent().repaint();
        }
        @Override
        public void mousePressed(MouseEvent e) {
            mousePt = e.getPoint();
            if (e.isShiftDown()) {
                Node.selectToggle(nodes, mousePt);
            } 
            else if (e.isPopupTrigger()) {
                Node.selectOne(nodes, mousePt);
                showPopup(e);
            } 
            else if (Node.selectOne(nodes, mousePt)) {
            	selecting = false;
            } 
            else {
                Node.selectNone(nodes);
                selecting = true;
            }    
            e.getComponent().repaint();
        }
        private void showPopup(MouseEvent e) {
            control.popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }
    private class MouseMotionHandler extends MouseMotionAdapter {
        @Override
        public void mouseDragged(MouseEvent e) {
        }
    }

    public JToolBar getControlPanel() {
        return control;
    }

    private class ControlPanel extends JToolBar {
		private static final long serialVersionUID = 1L;
		private Action newNode = new NewNodeAction("New");
        private Action kind = new KindComboAction("Kind");
        private Action color = new ColorAction("Color");
        private Action connect = new ConnectAction("Connect");
        private Action delete = new DeleteAction("Delete");
        private JButton defaultButton = new JButton(newNode);
        private JComboBox kindCombo = new JComboBox();
        private ColorIcon hueIcon = new ColorIcon(Color.blue);
        private JPopupMenu popup = new JPopupMenu();
        private JLabel stat = new JLabel("",JLabel.CENTER); 
        ControlPanel() {
            this.setLayout(new FlowLayout(FlowLayout.LEFT));
            this.setBackground(Color.GREEN);
            this.add(stat);
            JSpinner js = new JSpinner();
            js.setModel(new SpinnerNumberModel(RADIUS, 5, 100, 5));
            js.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    JSpinner s = (JSpinner) e.getSource();
                    radius = (Integer) s.getValue();
                    Node.updateRadius(nodes, radius);
                    GraphPanel.this.repaint();
                }
            });
            popup.add(new JMenuItem(newNode));
            popup.add(new JMenuItem(color));
            popup.add(new JMenuItem(connect));
            popup.add(new JMenuItem(delete));
            JMenu subMenu = new JMenu("Kind");
            for (Kind k : Kind.values()) {
                kindCombo.addItem(k);
                subMenu.add(new JMenuItem(new KindItemAction(k)));
            }
            popup.add(subMenu);
            kindCombo.addActionListener(kind);
        }

        class KindItemAction extends AbstractAction {
			private static final long serialVersionUID = 1L;
			private Kind k;

            public KindItemAction(Kind k) {
                super(k.toString());
                this.k = k;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                kindCombo.setSelectedItem(k);
            }
        }
    }
    private class ColorAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public ColorAction(String name) {
            super(name);
        }
        public void actionPerformed(ActionEvent e) {
            Color color = control.hueIcon.getColor();
            color = JColorChooser.showDialog(
                GraphPanel.this, "Choose a color", color);
            if (color != null) {
                Node.updateColor(nodes, color);
                control.hueIcon.setColor(color);
                control.repaint();
                repaint();
            }
        }
    }
    private class ConnectAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public ConnectAction(String name) {
            super(name);
        }
        public void actionPerformed(ActionEvent e) {
            Node.getSelected(nodes, selected);
            if (selected.size() > 1) {
                for (int i = 0; i < selected.size() - 1; ++i) {
                    Node n1 = selected.get(i);
                    Node n2 = selected.get(i + 1);
                    edges.add(new Edge(n1, n2));
                }
            }
            repaint();
        }
    }
    private class DeleteAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public DeleteAction(String name) {
            super(name);
        }
        public void actionPerformed(ActionEvent e) {
            ListIterator<Node> iter = nodes.listIterator();
            while (iter.hasNext()) {
                Node n = iter.next();
                if (n.isSelected()) {
                    deleteEdges(n);
                    iter.remove();
                }
            }
            repaint();
        }
        private void deleteEdges(Node n) {
            ListIterator<Edge> iter = edges.listIterator();
            while (iter.hasNext()) {
                Edge e = iter.next();
                if (e.n1 == n || e.n2 == n) {
                    iter.remove();
                }
            }
        }
    }
    private class KindComboAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public KindComboAction(String name) {
            super(name);
        }
        public void actionPerformed(ActionEvent e) {
            JComboBox combo = (JComboBox) e.getSource();
            kind = (Kind) combo.getSelectedItem();
            Node.updateKind(nodes, kind);
            repaint();
        }
    }
    private class NewNodeAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public NewNodeAction(String name) {
            super(name);
        }
        public void actionPerformed(ActionEvent e) {
            Node.selectNone(nodes);
            Point p = mousePt.getLocation();
            Color color = control.hueIcon.getColor();
            Node n = new Node(p, radius, color, kind, -1,false);
            n.setSelected(true);
            nodes.add(n);
            repaint();
        }
    }
    /**
     * The kinds of node in a graph.
     */
    private enum Kind {
        Circular, Rounded, Square;
    }

    /**
     * An Edge is a pair of Nodes.
     */
    private static class Edge {
    	private Node n1;
        private Node n2;
        public Edge(Node n1, Node n2) {
            this.n1 = n1;
            this.n2 = n2;
        }
        public void draw(Graphics g) {
            Point p1 = n1.getLocation();
            Point p2 = n2.getLocation();
            g.setColor(Color.lightGray);
            g.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }
    /**
     * A Node represents a node in a graph.
     */
    private static class Node {
        private Point p;
        private int r;
        private Color color;
        private Kind kind;
        private boolean selected = false;
        private Rectangle b = new Rectangle();
        private int nodeID;
        private boolean seenBefore = false;
		private boolean adjacent;
        /**
         * Construct a new node.
         */
        public Node(Point p, int r, Color color, Kind kind, int nodeID, boolean adjacent) {
            this.p = p;
            this.r = r;
            this.color = color;
            this.kind = kind;
            this.nodeID = nodeID;
            this.adjacent = adjacent;
            setBoundary(b);
        }
        /**
         * Calculate this node's rectangular boundary.
         */
        private void setBoundary(Rectangle b) {
            b.setBounds(p.x - r, p.y - r, 2 * r, 2 * r);
        }
        // getter method for Node field nodeID
        public int getID()
        {
        	return this.nodeID;
        }
        /**
         * Draw this node.
         */
        public void draw(Graphics g) {
            g.setColor(this.color);
            if (this.kind == Kind.Circular) {
                g.fillOval(b.x, b.y, b.width, b.height);
            } 
            else if (this.kind == Kind.Rounded) {
                g.fillRoundRect(b.x, b.y, b.width, b.height, r, r);
            } 
            else if (this.kind == Kind.Square) {
                g.fillRect(b.x, b.y, b.width, b.height);
            }
            if(selected && !seenBefore){
	            if(gameCount % 2 == 0){
	              	this.color = Color.blue;
	              	for(Edge e: edges){
	              		if(this.nodeID==e.n1.nodeID)
	              			e.n1.color=Color.blue;
	              		if(this.nodeID==e.n2.nodeID)
	              			e.n2.color=Color.blue;
	              	}
	              	this.seenBefore = true;
	            }
	            else {
	            	this.color = Color.red;
	            	for(Edge e: edges){
		            	if(this.nodeID==e.n1.nodeID)
		          			e.n1.color=Color.red;
		          		if(this.nodeID==e.n2.nodeID)
		          			e.n2.color=Color.red;
	            	}
	            	this.seenBefore = true;
	            }
	            gameCount++;
	            if(illegalMove && adjacent){
	            	this.color = Color.lightGray;
	            	gameCount--;
	            }
            }
            g.setColor(Color.YELLOW);
            g.drawString(this.getID() +"", xCoord[this.getID()]-4, yCoord[this.getID()]+4);
        }
        /**
         * Return this node's location.
         */
        public Point getLocation() {
            return p;
        }
        /**
         * Return true if this node contains p.
         */
        public boolean contains(Point p) {
            return b.contains(p);
        }
        /**
         * Return true if this node is selected.
         */
        public boolean isSelected() {
            return selected;
        }
        /**
         * Mark this node as selected.
         */
        public void setSelected(boolean selected) {
            this.selected = selected;
        }
        /**
         * Collected all the selected nodes in list.
         */
        public static void getSelected(List<Node> list, List<Node> selected) {
            selected.clear();
            Node chosen;
            for (Node n : list) {
                if (n.isSelected()) {
                    selected.add(n);
                }
            }
        }
        /**
         * Select no nodes.
         */
        public static void selectNone(List<Node> list) {
        	for (Node n : list) {
                n.setSelected(false);
            }
        }
        /**
         * Select a single node; return true if not already selected.
         */
        public static boolean selectOne(List<Node> list, Point p) {
           Node chosen;
        	for (Node n : list) {
                if (n.contains(p)) {
                	chosen = n;
                    if (!n.isSelected()) {
                        Boolean valid = true;
                        if(gameCount != 0){
	                        for(Edge pair: edges){
	                        	if(n.nodeID == pair.n1.nodeID){
	                        		if(gameCount%2 == 0 && pair.n2.color == Color.blue) {
	                        			valid = false;
	                        			checkGameStatus(chosen.nodeID, false);
	                        			return false;
	                        		}
	                        		else if(gameCount%2 != 0 && pair.n2.color == Color.red){
	                        			valid = false;
	                        			checkGameStatus(chosen.nodeID, false);
	                        			return false;
	                        		}
	                        	}
	                        	else if(n.nodeID == pair.n2.nodeID){
	                        		if(gameCount%2 == 0 && pair.n1.color == Color.blue) {
	                        			valid = false;
	                        			checkGameStatus(chosen.nodeID, false);
	                        			return false;
	                        		}
	                        		else if(gameCount%2 != 0 && pair.n1.color == Color.red){
	                        			valid = false;
	                        			checkGameStatus(chosen.nodeID, false);
	                        			return false;
	                        		}
	                        	}
	                        }
                        }
                        if(valid)
                        	checkGameStatus(chosen.nodeID, true);
                        	n.setSelected(true);
                    }
                    return true;
                }
            }
            return false;
        }
        public static void checkGameStatus(int id, boolean validated) {
        	int redCount = 0;
        	int blueCount = 0;
        	int typeWin;
        	
        	if(validated) {
        		blueStatus[id] = true;
        		redStatus[id] = true;
        	}
        	else{
        		switch(gameCount%2){
	        		case 0: blueStatus[id] = true;
	        		case 1: redStatus[id] = true;
        		}
        	}
        	
        	for(int i = 0; i < redStatus.length; i++)
        		if(redStatus[i]) redCount++;
        	
        	for(int i = 0; i < blueStatus.length; i++)
        		if(blueStatus[i]) blueCount++;
        	
        	if(redCount == numOfVertices && gameCount%2 == 1) gameStatus = 2;
        	else if(blueCount == numOfVertices && gameCount%2 == 0) gameStatus = 3;	
        	else gameStatus = 0;
		}
		/**
         * Toggle selected state of each node containing p.
         */
        public static void selectToggle(List<Node> list, Point p) {
            for (Node n : list) {
                if (n.contains(p)) {
                    n.setSelected(!n.isSelected());
                }
            }
        }

        /**
         * Update each node's radius r.
         */
        public static void updateRadius(List<Node> list, int r) {
            for (Node n : list) {
                if (n.isSelected()) {
                    n.r = r;
                    n.setBoundary(n.b);
                }
            }
        }

        /**
         * Update each node's color.
         */
        public static void updateColor(List<Node> list, Color color) {
            for (Node n : list) {
                if (n.isSelected()) {
                    n.color = color;
                }
            }
        }

        /**
         * Update each node's kind.
         */
        public static void updateKind(List<Node> list, Kind kind) {
            for (Node n : list) {
                if (n.isSelected()) {
                    n.kind = kind;
                }
            }
        }
         
    }

    private static class ColorIcon implements Icon {

        private static final int WIDE = 20;
        private static final int HIGH = 20;
        private Color color;

        public ColorIcon(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(color);
            g.fillRect(x, y, WIDE, HIGH);
        }

        public int getIconWidth() {
            return WIDE;
        }

        public int getIconHeight() {
            return HIGH;
        }
    }
}
