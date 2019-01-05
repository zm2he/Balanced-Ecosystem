import java.awt.*;
import javax.swing.*;
import java.awt.event.*;  // Needed for ActionListener
import javax.swing.event.*;  // Needed for ActionListener

//for start screen
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.swing.JSlider;
import java.io.File;

class BalancedEcosystemGUI extends JFrame implements ActionListener, ChangeListener, MouseListener
{
    ///////////////////code for simulation/////////////////////
    //set up environment
    private Environment environment = new Environment (0.05,0.01,0.001);

    //set up 2nd screen with user manipulable variables
    protected static ConstantControl control;

    private JSlider speedSldr = new JSlider ();//set up slider
    private Timer t;//timer

    //set up graph and scroll
    private Graph graph;
    private JScrollPane scroll;

    //buttons
    private JButton startBtn;
    private JButton stopBtn;
    private JButton incrementBtn;
    private JButton foodBtn;
    private JButton preyBtn;
    private JButton predatorBtn;
    private JButton togglePointerBtn;
    private JButton loadBtn;

    //options for different preset environments
    private JComboBox biome;

    //set up generation counter
    protected static int generation = 0; 

    // set up statistic labels
    private static JLabel generationCount = new JLabel("Generation: 0");
    protected static JLabel foodCount = new JLabel("Food: 0");
    protected static JLabel preyCount = new JLabel("Prey: 0");
    static JLabel predatorCount = new JLabel("Predator: 0");

    //set up label for speed slider
    private JLabel speedSlider = new JLabel("Speed Slider");

    ///////////////////code for mouse interactions////////////////
    //set up populate/eradicate rate
    //drop down box of options (0% - 100%)
    private JLabel density = new JLabel("Density (%): ");
    private JComboBox rate;
    protected static double percent = 0.5;

    //set up booleans for mouse events
    protected static boolean addFood = true;
    protected static boolean addPrey = false;
    protected static boolean addPredator = false;

    private boolean pointer = false;
    private boolean mousePress = false;
    private boolean populate = true;
    private boolean eradicate = false;

    //set up mouse points
    private static  Point pressed;
    private static  Point released;
    private static Point dragged;

    //set up necessary points for drawing
    private static int xDragged = 0;
    private static int yDragged = 0;

    //set up boolean so graph can stop drawing
    private boolean pause = true;

    //////////////////////code for start screens///////////////////////////////

    //set up labels
    private JLabel foodSpawnLbl, foodLimitLbl;
    private JLabel foodEnergyLbl, preyHungerLbl, preyFoodRequiredLbl, preyRepCoolDownLbl;
    private JLabel preyEnergyLbl, predatorHungerLbl, predatorFoodRequiredLbl, predatorRepCoolDownLbl;

    //set up sliders
    private static JSlider foodSpawnSl, foodLimitSl;
    private static JSlider foodEnergySl, preyHungerSl, preyFoodRequiredSl, preyRepCoolDownSl;
    private static JSlider preyEnergySl, predatorHungerSl, predatorFoodRequiredSl, predatorRepCoolDownSl;

    //set up buttons for choosing options
    JButton mouseBtn, rabbitBtn, deerBtn, snakeBtn, wolfBtn, bearBtn, defaultBtn;

    //set up panels (1st screen, 2md screen, simulation screen)
    private JPanel screen2 = new JPanel();
    private JPanel screen1 = new JPanel();
    private JPanel content = new JPanel ();

    //======================================================== constructor
    public BalancedEcosystemGUI ()
    {
        //make simulation grid and graph
        DrawArea board = new DrawArea (500, 525);
        graph = new Graph();

        //add listeners 
        board.addMouseListener(this);
        speedSldr.addChangeListener (this);

        //set up simulation screen layout
        content.setLayout (new BorderLayout ());

        //create major panels
        JPanel control = new JPanel();
        JPanel graphPanel = new JPanel();

        //make control panel
        startBtn = new JButton ("Start");
        stopBtn = new JButton("Stop");
        incrementBtn = new JButton ("Increment");
        loadBtn = new JButton("Load");

        startBtn.addActionListener(this);
        stopBtn.addActionListener(this);
        incrementBtn.addActionListener(this);
        loadBtn.addActionListener(this);

        control.setLayout(new GridLayout(2,2));
        control.add(startBtn);
        control.add(stopBtn);
        control.add(incrementBtn);
        control.add(loadBtn);

        //create options in drop down box for preset simulations
        String [] options = {"stable","unstable","invasive species","bacterial growth","slow growth","desert", "sandbox mode"};
        biome = new JComboBox(options);

        //////////////////set up for mouse events///////////////////

        //set up buttons --> to add food, prey, or predators
        foodBtn = new JButton("Food");
        foodBtn.addActionListener(new MouseControlListener());
        preyBtn = new JButton("Prey");
        preyBtn.addActionListener(new MouseControlListener());
        predatorBtn = new JButton("Predator");
        predatorBtn.addActionListener(new MouseControlListener());

        foodBtn.setOpaque(true);
        preyBtn.setOpaque(false);
        predatorBtn.setOpaque(false);

        //panel to organize mouse controls
        JPanel mouseControls = new JPanel();
        mouseControls.add(foodBtn);
        mouseControls.add(preyBtn);
        mouseControls.add(predatorBtn);

        //button to toggle pointer on and off
        togglePointerBtn = new JButton("Pointer Off");
        togglePointerBtn.addActionListener(new MouseControlListener());
        togglePointerBtn.setOpaque(false);

        //fill populate/eradicate rate combobox, 0% - 100%
        String[] temp = new String[11];
        for(int i = 0; i<11; i++)
            temp[i] = ""+i*10;
        rate = new JComboBox(temp);
        rate.setSelectedItem("50");

        //add all mouse event items to larger pabel
        JPanel mouseSettings = new JPanel();
        mouseSettings.add(togglePointerBtn);
        mouseSettings.add(density);
        mouseSettings.add(rate);

        //add everntyhing onto controlBox
        JPanel controlBox = new JPanel();
        controlBox.setLayout(new BoxLayout(controlBox, BoxLayout.PAGE_AXIS));
        controlBox.add(Box.createRigidArea(new Dimension (0,32)));
        controlBox.add(control);
        controlBox.add(biome);
        controlBox.add(mouseControls);
        controlBox.add(mouseSettings);
        togglePointerBtn.setAlignmentX(Component.CENTER_ALIGNMENT );
        controlBox.setMaximumSize(new Dimension(300,10));

        //setup graph
        graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.PAGE_AXIS));
        graphPanel.setPreferredSize(new Dimension(250,270));

        //paint blank graph and create scroll bars
        graph.draw();
        ImageIcon icon = new ImageIcon(graph.getPane());
        scroll = new JScrollPane(new JLabel(icon));
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        //add counters and graph to larger panel
        graphPanel.add(generationCount);
        graphPanel.add(foodCount);
        graphPanel.add(preyCount);
        graphPanel.add(predatorCount);
        graphPanel.add(scroll);

        //set up west and east 
        content.setLayout(new BorderLayout());
        JPanel west = new JPanel();
        west.setLayout(new BoxLayout(west, BoxLayout.PAGE_AXIS));

        JPanel east = new JPanel();
        east.setLayout(new BoxLayout(east, BoxLayout.PAGE_AXIS));
        east.setPreferredSize(new Dimension (330,500));

        //add everything to content pane
        west.add(speedSlider);
        west.add(speedSldr);
        west.add(board);
        east.add(controlBox);
        east.add(graphPanel);

        //fix alignment of speedslider
        speedSlider.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(west,"West");
        content.add(east,"East");

        //call methods to load start screens
        setUpScreen1();
        setUpScreen2();

        //finish
        setContentPane (screen1);
        pack ();
        setTitle ("Balanced Ecosystem");
        setSize(850,600);
        setResizable(false);
        setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo (null);
    }

    public void setUpScreen1()
    {
        //this is the intro screen 
        Color customColor = new Color(0,128,255); //or (13,148,255)
        screen1.setBackground(customColor);
        BtnListener btnListener = new BtnListener (); // listener for all buttons

        screen1.setLayout(new GridBagLayout()); //creatins the gridbagLayout
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;

        //button to move to next screen plus all gridBagLayout formatting
        JButton startBtn = new JButton ("Start");
        startBtn.addActionListener (btnListener);
        startBtn.setPreferredSize (new Dimension (150,50));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,0,0,0);
        c.gridx = 4;
        c.gridy = 4;
        screen1.add(startBtn,c);

        //buttons for each animal in selection

        //load image of population cycles
        BufferedImage img = null;
        try
        {
            img = ImageIO.read (new File ("cycle.jpg"));
        }
        catch (IOException e)
        {
            System.out.println("File not found");   
        }
        ImageIcon imgIcon = new ImageIcon(img);
        JLabel cycle = new JLabel(new ImageIcon(img));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,0,0,0);
        c.gridx = 0;
        c.gridy = 0;
        screen1.add(cycle,c);
    }

    public void setUpScreen2()
    {
        //set up layout of 2nd screen
        screen2.setLayout(new GridBagLayout());
        screen2.setBackground(new Color(0,128,255));

        screen2.setForeground(Color.black);

        GridBagConstraints c = new GridBagConstraints();

        //set up spawn rate slider and label
        foodSpawnLbl = new JLabel("Food Spawn rate (%):");
        foodSpawnLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        foodSpawnLbl.setAlignmentY(Component.CENTER_ALIGNMENT);
        screen2.add(foodSpawnLbl);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,0,0,0);
        c.gridx = 0;
        c.gridy = 1;
        c.gridheight = 1;
        screen2.add(foodSpawnLbl,c);

        foodSpawnSl = new JSlider(JSlider.HORIZONTAL, 0, 5, 0);
        foodSpawnSl.addChangeListener (new SliderListener1());
        foodSpawnSl.setMinorTickSpacing(1);
        foodSpawnSl.setMajorTickSpacing(5);
        foodSpawnSl.setPaintTicks(true);
        foodSpawnSl.setPaintLabels(true);
        foodSpawnSl.setPreferredSize(new Dimension(300, 80));
        c.insets = new Insets(0,0,0,0);
        c.gridx = 2;
        c.gridy = 1;
        c.gridwidth = 5;
        c.weightx = 8;
        screen2.add(foodSpawnSl,c);

        c.gridwidth = 1;
        c.weightx = 1;

        //set up food limit slider and label
        foodLimitLbl = new JLabel("Food Expiration Rate (# of turns):");//, GridBagLayout.NORTH);
        foodLimitLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,0,0,0);
        c.gridx = 0;
        c.gridy = 2;
        screen2.add(foodLimitLbl,c);

        foodLimitSl = new JSlider(JSlider.HORIZONTAL, 10, 30, 10);
        foodLimitSl.addChangeListener (new SliderListener2());
        foodLimitSl.setMinorTickSpacing(1);
        foodLimitSl.setMajorTickSpacing(5);
        foodLimitSl.setPaintTicks(true);
        foodLimitSl.setPaintLabels(true);
        foodLimitSl.setPreferredSize(new Dimension(300, 80));
        c.insets = new Insets(0,0,0,0);
        c.gridx = 2;
        c.gridy = 2;
        c.gridwidth = 3;
        screen2.add(foodLimitSl,c);
        c.gridwidth = 1;

        //set up food energy slider and label
        foodEnergyLbl = new JLabel("Food Energy:");//, GridBagLayout.NORTH);
        foodEnergyLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,0,0,0);
        c.gridx = 0;
        c.gridy = 3;
        screen2.add(foodEnergyLbl,c);

        foodEnergySl = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        foodEnergySl.addChangeListener (new SliderListener3());
        foodEnergySl.setMinorTickSpacing(5);
        foodEnergySl.setMajorTickSpacing(10);
        foodEnergySl.setPaintTicks(true);
        foodEnergySl.setPaintLabels(true);
        foodEnergySl.setPreferredSize(new Dimension(300, 80));
        c.insets = new Insets(0,0,0,0);
        c.gridx = 2;
        c.gridy = 3;
        c.gridwidth = 3;
        screen2.add(foodEnergySl,c);
        c.gridwidth = 1;

        //set up prey hunger slider and label
        preyHungerLbl = new JLabel("Prey Hunger:");
        preyHungerLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,0,0,0);
        c.gridx = 0;
        c.gridy = 4;
        screen2.add(preyHungerLbl,c);

        preyHungerSl = new JSlider(JSlider.HORIZONTAL, 5, 30, 5);
        preyHungerSl.addChangeListener (new SliderListener4());
        preyHungerSl.setMinorTickSpacing(1);
        preyHungerSl.setMajorTickSpacing(5);
        preyHungerSl.setPaintTicks(true);
        preyHungerSl.setPaintLabels(true);
        preyHungerSl.setPreferredSize(new Dimension(300, 80));
        c.insets = new Insets(0,0,0,0);
        c.gridx = 2;
        c.gridy = 4;
        c.gridwidth = 3;
        screen2.add(preyHungerSl,c);
        c.gridwidth = 1;

        //set up prey food required slider and label
        preyFoodRequiredLbl = new JLabel("Food Required for Prey Reproduction:");
        preyFoodRequiredLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,0,0,0);
        c.gridx = 0;
        c.gridy = 5;
        screen2.add(preyFoodRequiredLbl,c);

        preyFoodRequiredSl = new JSlider(JSlider.HORIZONTAL, 0,30, 0);
        preyFoodRequiredSl.addChangeListener (new SliderListener5());
        preyFoodRequiredSl.setMinorTickSpacing(1);
        preyFoodRequiredSl.setMajorTickSpacing(5);
        preyFoodRequiredSl.setPaintTicks(true);
        preyFoodRequiredSl.setPaintLabels(true);
        preyFoodRequiredSl.setPreferredSize(new Dimension(300, 80));
        c.insets = new Insets(0,0,0,0);
        c.gridx = 2;
        c.gridy = 5;
        c.gridwidth = 3;
        screen2.add(preyFoodRequiredSl,c);
        c.gridwidth = 1;

        //set up prey reproduction slider and label
        preyRepCoolDownLbl = new JLabel("Prey Reproduction Period:");
        preyRepCoolDownLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,0,0,0);
        c.gridx =0;
        c.gridy = 6;
        screen2.add(preyRepCoolDownLbl,c);

        preyRepCoolDownSl = new JSlider(JSlider.HORIZONTAL, 0, 30, 0);
        preyRepCoolDownSl.addChangeListener (new SliderListener6());
        preyRepCoolDownSl.setMinorTickSpacing(1);
        preyRepCoolDownSl.setMajorTickSpacing(5);
        preyRepCoolDownSl.setPaintTicks(true);
        preyRepCoolDownSl.setPaintLabels(true);
        preyRepCoolDownSl.setPreferredSize(new Dimension(300, 80));
        c.insets = new Insets(0,0,0,0);
        c.gridx = 2;
        c.gridy = 6;
        c.gridwidth = 3;
        screen2.add(preyRepCoolDownSl,c);
        c.gridwidth = 1;

        //set up prey energy slider and label
        preyEnergyLbl = new JLabel("Prey Energy:");
        preyEnergyLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,0,0,0);
        c.gridx = 0;
        c.gridy = 7;
        screen2.add(preyEnergyLbl,c);

        preyEnergySl = new JSlider(JSlider.HORIZONTAL, 0, 30, 0);
        preyEnergySl.addChangeListener (new SliderListener7());
        preyEnergySl.setMinorTickSpacing(1);
        preyEnergySl.setMajorTickSpacing(5);
        preyEnergySl.setPaintTicks(true);
        preyEnergySl.setPaintLabels(true);
        preyEnergySl.setPreferredSize(new Dimension(300, 80));
        c.insets = new Insets(0,0,0,0);
        c.gridx = 2;
        c.gridy = 7;
        c.gridwidth = 3;
        screen2.add(preyEnergySl,c);
        c.gridwidth = 1;

        //set up predator hunger slider and label
        predatorHungerLbl = new JLabel("Predator Hunger:");
        predatorHungerLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,0,0,0);
        c.gridx = 0;
        c.gridy = 8;
        screen2.add(predatorHungerLbl,c);

        predatorHungerSl = new JSlider(JSlider.HORIZONTAL, 5, 50, 5);
        predatorHungerSl.addChangeListener (new SliderListener8());
        predatorHungerSl.setMinorTickSpacing(5);
        predatorHungerSl.setMajorTickSpacing(10);
        predatorHungerSl.setPaintTicks(true);
        predatorHungerSl.setPaintLabels(true);
        predatorHungerSl.setPreferredSize(new Dimension(300, 80));
        c.insets = new Insets(0,0,0,0);
        c.gridx = 2;
        c.gridy = 8;
        c.gridwidth = 3;
        screen2.add(predatorHungerSl,c);
        c.gridwidth = 1;

        //set up predator food required slider and label
        predatorFoodRequiredLbl = new JLabel("Food Required for Predator Reproduction:");
        predatorFoodRequiredLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,0,0,0);
        c.gridx = 0;
        c.gridy = 9;
        screen2.add(predatorFoodRequiredLbl,c);

        predatorFoodRequiredSl = new JSlider(JSlider.HORIZONTAL, 0, 50, 0);
        predatorFoodRequiredSl.addChangeListener (new SliderListener9());
        predatorFoodRequiredSl.setMinorTickSpacing(5);
        predatorFoodRequiredSl.setMajorTickSpacing(10);
        predatorFoodRequiredSl.setPaintTicks(true);
        predatorFoodRequiredSl.setPaintLabels(true);
        predatorFoodRequiredSl.setPreferredSize(new Dimension(300, 80));
        c.insets = new Insets(0,0,0,0);
        c.gridx = 2;
        c.gridy = 9;
        c.gridwidth = 3;
        screen2.add(predatorFoodRequiredSl,c);
        c.gridwidth = 1;

        //set up predator reproduction slider and label
        predatorRepCoolDownLbl = new JLabel("Predator Reproduction Period:");
        predatorRepCoolDownLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,0,0,0);
        c.gridx = 0;
        c.gridy = 10;
        screen2.add(predatorRepCoolDownLbl,c);

        predatorRepCoolDownSl = new JSlider(JSlider.HORIZONTAL, 0, 50, 0);
        predatorRepCoolDownSl.addChangeListener (new SliderListener10());
        predatorRepCoolDownSl.setMinorTickSpacing(5);
        predatorRepCoolDownSl.setMajorTickSpacing(10);
        predatorRepCoolDownSl.setPaintTicks(true);
        predatorRepCoolDownSl.setPaintLabels(true);
        predatorRepCoolDownSl.setPreferredSize(new Dimension(300, 80));
        c.insets = new Insets(0,0,0,0);
        c.gridx = 2;
        c.gridy = 10;
        c.gridwidth = 3;
        screen2.add(predatorRepCoolDownSl,c);
        c.gridwidth = 1;

        //set initial values of all sliders to default values
        foodSpawnSl.setValue(1);
        foodLimitSl.setValue(20);
        foodEnergySl.setValue(4);
        preyHungerSl.setValue(15);
        preyFoodRequiredSl.setValue(15);
        preyRepCoolDownSl.setValue(5);
        preyEnergySl.setValue(5);
        predatorHungerSl.setValue(30);
        predatorFoodRequiredSl.setValue(100);
        predatorRepCoolDownSl.setValue(5);

        //set text of all labels to default values (same as sliders)
        foodSpawnLbl.setText("Food Spawn Rate (%): " + foodSpawnSl.getValue());
        foodLimitLbl.setText("Food Expiration Rate ( # of turns): " + foodLimitSl.getValue());
        foodEnergyLbl.setText("Food Energy: " + foodEnergySl.getValue());
        preyHungerLbl.setText("Prey Hunger: " + preyHungerSl.getValue());
        preyFoodRequiredLbl.setText("Food Required for Prey Reproduction: " + preyFoodRequiredSl.getValue());
        preyRepCoolDownLbl.setText("Prey Reproduction Rate: " + preyRepCoolDownSl.getValue());
        preyEnergyLbl.setText("Prey Energy: " + preyEnergySl.getValue());
        predatorHungerLbl.setText("Predator Hunger: " + predatorHungerSl.getValue());
        predatorFoodRequiredLbl.setText("Food Required for Predator Reproduction: " + predatorFoodRequiredSl.getValue());
        predatorRepCoolDownLbl.setText("Predator Reproduction Rate: " + predatorRepCoolDownSl.getValue());

        //color labels
        foodSpawnLbl.setForeground(Color.green);
        foodLimitLbl.setForeground(Color.green);

        foodEnergyLbl.setForeground(new Color(0,191,255));
        preyHungerLbl.setForeground(new Color(0,191,255));
        preyFoodRequiredLbl.setForeground(new Color(0,191,255));
        preyRepCoolDownLbl.setForeground(new Color(0,191,255));

        preyEnergyLbl.setForeground(Color.red);
        predatorHungerLbl.setForeground(Color.red);
        predatorFoodRequiredLbl.setForeground(Color.red);
        predatorRepCoolDownLbl.setForeground(Color.red);

        //add go button at the bottom of the screen
        //the go button to progess to the next screen
        JButton goBtn = new JButton ("GO!");
        goBtn.setBackground(Color.orange);
        goBtn.setOpaque(true);
        BtnListener btnListener = new BtnListener (); // listener for all buttons
        goBtn.addActionListener(btnListener);

        //set up panel with preset options for prey and predator
        JPanel options = new JPanel();
        options.setLayout(new GridLayout(2,3));

        //creat buttons
        mouseBtn = new JButton("Mouse");
        rabbitBtn = new JButton("Rabbit");
        deerBtn = new JButton("Deer");
        snakeBtn = new JButton("Snake");
        wolfBtn = new JButton("Wolf");
        bearBtn = new JButton("Bear");
        defaultBtn = new JButton("Default");

        //add listeners
        mouseBtn.addActionListener(new BtnListener());
        rabbitBtn.addActionListener(new BtnListener());
        deerBtn.addActionListener(new BtnListener());
        snakeBtn.addActionListener(new BtnListener());
        wolfBtn.addActionListener(new BtnListener());
        bearBtn.addActionListener(new BtnListener());
        defaultBtn.addActionListener(new BtnListener());

        //color the buttons
        mouseBtn.setBackground(new Color(0,191,255));
        mouseBtn.setOpaque(true);
        rabbitBtn.setBackground(new Color(0,191,255));
        rabbitBtn.setOpaque(true);
        deerBtn.setBackground(new Color(0,191,255));
        deerBtn.setOpaque(true);

        snakeBtn.setBackground(Color.red);
        snakeBtn.setOpaque(true);
        wolfBtn.setBackground(Color.red);
        wolfBtn.setOpaque(true);
        bearBtn.setBackground(Color.red);
        bearBtn.setOpaque(true);

        //finish
        options.add(mouseBtn);
        options.add(rabbitBtn);
        options.add(deerBtn);
        options.add(snakeBtn);
        options.add(wolfBtn);
        options.add(bearBtn);

        //panel for defaultBtn and goBtn
        JPanel other = new JPanel();
        other.setLayout(new BoxLayout(other, BoxLayout.LINE_AXIS));
        other.add(defaultBtn);
        other.add(Box.createRigidArea(new Dimension(20,0)));
        other.add(goBtn);
        other.setOpaque(false);

        JPanel total = new JPanel();
        total.setLayout(new BoxLayout(total, BoxLayout.PAGE_AXIS));
        total.add(options);
        total.add(other);
        total.setOpaque(false);

        //add options and default btn to screen2
        screen2.add(total);
    }

    class BtnListener implements ActionListener // Button menu
    {
        public void actionPerformed (ActionEvent e)
        {
            if (e.getActionCommand ().equals ("Start"))
            {
                screen1.removeAll(); 
                // removes the first panel, adds the second, then refreshes so user can progress.
                screen1.setVisible(false);
                screen2.setVisible(true);
                setContentPane(screen2);
                //replace content pane with 2nd screen
            }

            else if (e.getActionCommand ().equals ("GO!"))
            {
                //replaces content pane with simulation screen
                screen2.setVisible(false);
                content.setVisible(true);
                setContentPane(content);
                content.updateUI();

                //set food variables
                Food.setFoodSpawn(foodSpawnSl.getValue()/100.0);
                Food.setFoodLimit((int)foodLimitSl.getValue());

                //set prey variables
                Prey.setFoodEnergy((int)foodEnergySl.getValue());
                Prey.setHunger((int)preyHungerSl.getValue());
                Prey.setFoodRequired((int)preyFoodRequiredSl.getValue());
                Prey.setRepCoolDown((int)preyRepCoolDownSl.getValue());

                //set predator variables
                Predator.setPreyEnergy((int)preyEnergySl.getValue());
                Predator.setHunger((int)predatorHungerSl.getValue());
                Predator.setFoodRequired((int)predatorFoodRequiredSl.getValue());
                Predator.setRepCoolDown((int)predatorRepCoolDownSl.getValue());

                //make new window for variable controls
                control = new ConstantControl();
                control.setVisible(true);
            }

            else if(e.getSource() == mouseBtn)//
            {
                //change values on slider to that of mouse
                foodEnergySl.setValue(4);
                preyHungerSl.setValue(20);
                preyFoodRequiredSl.setValue(5);
                preyRepCoolDownSl.setValue(5);

                //change values on labels to match slider
                foodEnergyLbl.setText("Food Energy: " + foodEnergySl.getValue());
                preyHungerLbl.setText("Prey Hunger: " + preyHungerSl.getValue());
                preyFoodRequiredLbl.setText("Food Required for Prey Reproduction: " + preyFoodRequiredSl.getValue());
                preyRepCoolDownLbl.setText("Prey Reproduction Period: " + preyRepCoolDownSl.getValue());
            }
            else if(e.getSource() == rabbitBtn)
            {
                //change values on slider to that of rabbit
                foodEnergySl.setValue(3);
                preyHungerSl.setValue(30);
                preyFoodRequiredSl.setValue(15);
                preyRepCoolDownSl.setValue(5);

                //change values on labels to match slider
                foodEnergyLbl.setText("Food Energy: " + foodEnergySl.getValue());
                preyHungerLbl.setText("Prey Hunger: " + preyHungerSl.getValue());
                preyFoodRequiredLbl.setText("Food Required for Prey Reproduction: " + preyFoodRequiredSl.getValue());
                preyRepCoolDownLbl.setText("Prey Reproduction Period: " + preyRepCoolDownSl.getValue());
            }
            else if(e.getSource() == deerBtn)
            {
                foodEnergySl.setValue(7);
                preyHungerSl.setValue(20);
                preyFoodRequiredSl.setValue(10);
                preyRepCoolDownSl.setValue(15);

                foodEnergyLbl.setText("Food Energy: " + foodEnergySl.getValue());
                preyHungerLbl.setText("Prey Hunger: " + preyHungerSl.getValue());
                preyFoodRequiredLbl.setText("Food Required for Prey Reproduction: " + preyFoodRequiredSl.getValue());
                preyRepCoolDownLbl.setText("Prey Reproduction Period: " + preyRepCoolDownSl.getValue());
            }
            else if(e.getSource() == snakeBtn)
            {
                preyEnergySl.setValue(10);
                predatorHungerSl.setValue(50);
                predatorFoodRequiredSl.setValue(20);
                predatorRepCoolDownSl.setValue(30);

                preyEnergyLbl.setText("Prey Energy: " + preyEnergySl.getValue());
                predatorHungerLbl.setText("Predator Hunger: " + predatorHungerSl.getValue());
                predatorFoodRequiredLbl.setText("Food Required for Predator Reproduction: " + predatorFoodRequiredSl.getValue());
                predatorRepCoolDownLbl.setText("Predator Reproduction Period: " + predatorRepCoolDownSl.getValue());
            }
            else if(e.getSource() == wolfBtn)
            {
                preyEnergySl.setValue(5);
                predatorHungerSl.setValue(40);
                predatorFoodRequiredSl.setValue(10);
                predatorRepCoolDownSl.setValue(15);

                preyEnergyLbl.setText("Prey Energy: " + preyEnergySl.getValue());
                predatorHungerLbl.setText("Predator Hunger: " + predatorHungerSl.getValue());
                predatorFoodRequiredLbl.setText("Food Required for Predator Reproduction: " + predatorFoodRequiredSl.getValue());
                predatorRepCoolDownLbl.setText("Predator Reproduction Period: " + predatorRepCoolDownSl.getValue());
            }
            else if(e.getSource() == bearBtn)
            { 
                preyEnergySl.setValue(20);
                predatorHungerSl.setValue(50);
                predatorFoodRequiredSl.setValue(20);
                predatorRepCoolDownSl.setValue(50);

                preyEnergyLbl.setText("Prey Energy: " + preyEnergySl.getValue());
                predatorHungerLbl.setText("Predator Hunger: " + predatorHungerSl.getValue());
                predatorFoodRequiredLbl.setText("Food Required for Predator Reproduction: " + predatorFoodRequiredSl.getValue());
                predatorRepCoolDownLbl.setText("Predator Reproduction Period: " + predatorRepCoolDownSl.getValue());
            }
            else if(e.getSource() == defaultBtn)
            {
                //change all slider values to default values
                foodSpawnSl.setValue(1);
                foodLimitSl.setValue(20);
                foodEnergySl.setValue(4);
                preyHungerSl.setValue(15);
                preyFoodRequiredSl.setValue(15);
                preyRepCoolDownSl.setValue(5);
                preyEnergySl.setValue(5);
                predatorHungerSl.setValue(30);
                predatorFoodRequiredSl.setValue(100);
                predatorRepCoolDownSl.setValue(5);

                //changle all labels to match slider values
                foodSpawnLbl.setText("Food Spawn Rate (%): " + foodSpawnSl.getValue());
                foodLimitLbl.setText("Food Expiration Rate ( # of turns): " + foodLimitSl.getValue());
                foodEnergyLbl.setText("Food Energy: " + foodEnergySl.getValue());
                preyHungerLbl.setText("Prey Hunger: " + preyHungerSl.getValue());
                preyFoodRequiredLbl.setText("Food Required for Prey Reproduction: " + preyFoodRequiredSl.getValue());
                preyRepCoolDownLbl.setText("Prey Reproduction Rate: " + preyRepCoolDownSl.getValue());
                preyEnergyLbl.setText("Prey Energy: " + preyEnergySl.getValue());
                predatorHungerLbl.setText("Predator Hunger: " + predatorHungerSl.getValue());
                predatorFoodRequiredLbl.setText("Food Required for Predator Reproduction: " + predatorFoodRequiredSl.getValue());
                predatorRepCoolDownLbl.setText("Predator Reproduction Rate: " + predatorRepCoolDownSl.getValue());
            }
        }
    }

    //slider that controls food spawn
    class SliderListener1 implements ChangeListener{
        public void stateChanged(ChangeEvent ce)
        {
            JSlider foodSpawnSl = (JSlider)ce.getSource();//declare slider that is source
            if(!foodSpawnSl.getValueIsAdjusting()) //if the value has changed
            {
                Food.setFoodSpawn(foodSpawnSl.getValue()/100.0);//changes value in Food class
                foodSpawnLbl.setText("Food Spawn Rate (%): " + foodSpawnSl.getValue());//update label
            }
        }
    }

    //food limit
    class SliderListener2 implements ChangeListener{
        public void stateChanged(ChangeEvent ce)
        {
            JSlider foodLimitSl = (JSlider)ce.getSource();
            if(!foodLimitSl.getValueIsAdjusting()) //if the value has changed
            {
                Food.setFoodLimit((int)foodLimitSl.getValue()); //
                foodLimitLbl.setText("Food Expiration Rate ( # of turns): " + foodLimitSl.getValue());
            }
        }
    }

    //food energy
    class SliderListener3 implements ChangeListener{
        public void stateChanged(ChangeEvent ce)
        {
            JSlider foodEnergySl = (JSlider)ce.getSource();
            if(!foodEnergySl.getValueIsAdjusting()) //if the value has changed
            {
                Prey.setFoodEnergy((int)foodEnergySl.getValue()); //
                foodEnergyLbl.setText("Food Energy: " + foodEnergySl.getValue());
            }
        }
    }

    //prey hunger
    class SliderListener4 implements ChangeListener{
        public void stateChanged(ChangeEvent ce)
        {
            JSlider preyHungerSl = (JSlider)ce.getSource();
            if(!preyHungerSl.getValueIsAdjusting()) //if the value has changed
            {
                Prey.setHunger( (int)preyHungerSl.getValue()); //
                preyHungerLbl.setText("Prey Hunger: " + preyHungerSl.getValue());

            }
        }
    }

    // prey food required
    class SliderListener5 implements ChangeListener{
        public void stateChanged(ChangeEvent ce)
        {
            JSlider preyFoodRequiredSl = (JSlider)ce.getSource();
            if(!preyFoodRequiredSl.getValueIsAdjusting()) //if the value has changed
            {
                Prey.setFoodRequired((int)preyFoodRequiredSl.getValue()); //
                preyFoodRequiredLbl.setText("Food Required for Prey Reproduction: " + preyFoodRequiredSl.getValue());
            }
        }
    }

    //prey reproduction
    class SliderListener6 implements ChangeListener{
        public void stateChanged(ChangeEvent ce)
        {
            JSlider preyRepCoolDownSl = (JSlider)ce.getSource();
            if(!preyRepCoolDownSl.getValueIsAdjusting()) //if the value has changed
            {
                Prey.setRepCoolDown((int)preyRepCoolDownSl.getValue()); //
                preyRepCoolDownLbl.setText("Prey Reproduction Period: " + preyRepCoolDownSl.getValue());
            }
        }
    }

    //prey energy
    class SliderListener7 implements ChangeListener{
        public void stateChanged(ChangeEvent ce)
        {
            JSlider preyEnergySl = (JSlider)ce.getSource();
            if(!preyEnergySl.getValueIsAdjusting()) //if the value has changed
            {
                Predator.setPreyEnergy((int)preyEnergySl.getValue()); //
                preyEnergyLbl.setText("Prey Energy: " + preyEnergySl.getValue());
            }
        }
    }

    //predator hunger
    class SliderListener8 implements ChangeListener{
        public void stateChanged(ChangeEvent ce)
        {
            JSlider predatorHungerSl = (JSlider)ce.getSource();
            if(!predatorHungerSl.getValueIsAdjusting()) //if the value has changed
            {
                Predator.setHunger((int)predatorHungerSl.getValue()); //
                predatorHungerLbl.setText("Predator Hunger: " + predatorHungerSl.getValue());
            }
        }
    }

    //predator food required
    class SliderListener9 implements ChangeListener{
        public void stateChanged(ChangeEvent ce)
        {
            JSlider predatorFoodRequiredSl = (JSlider)ce.getSource();
            if(!predatorFoodRequiredSl.getValueIsAdjusting()) //if the value has changed
            {
                Predator.setFoodRequired((int)predatorFoodRequiredSl.getValue()); //
                predatorFoodRequiredLbl.setText("Food Required for Predator Reproduction: " + predatorFoodRequiredSl.getValue());
            }
        }
    }

    //predator reproduction
    class SliderListener10 implements ChangeListener{
        public void stateChanged(ChangeEvent ce)
        {
            JSlider predatorRepCoolDownSl = (JSlider)ce.getSource();
            if(!predatorRepCoolDownSl.getValueIsAdjusting()) //if the value has changed
            {
                Predator.setRepCoolDown((int)predatorRepCoolDownSl.getValue()); //
                predatorRepCoolDownLbl.setText("Predator Reproduction Period: " + predatorRepCoolDownSl.getValue());
            }
        }
    }
    ////////////////end of code for start screens/////////////////////////

    /////////////////code for simulation///////////////
    public void stateChanged (ChangeEvent e)//speed control
    {
        if (t != null)
            t.setDelay (400 - 4 * speedSldr.getValue ()); // 0 to 400 ms
    }

    //buttons in controlBox
    public void actionPerformed (ActionEvent e)
    {
        if (e.getSource() == startBtn)
        {
            //start timer
            if(pause) // check if time is stopped
            {
                Movement moveEnvironment = new Movement (environment); // ActionListener
                t = new Timer (400 - 4 * speedSldr.getValue (), moveEnvironment); // set up timer
                t.start (); // start simulation

                pause = false;
            }
        }
        else if(e.getSource() == stopBtn)
        {
            //stop timer
            try
            {
                t.stop();
            }
            catch(NullPointerException ee)
            {}
            pause = true;
        }
        else if(e.getSource() == incrementBtn)
        {
            //stop timer
            pause = true;
            try
            {
                t.stop();
            }
            catch(NullPointerException ee)
            {}

            //update grid, stats, and graph only 1x
            environment.advance();
            updateStats();
            graph.draw();
        }
        else if(e.getSource() == loadBtn)
        {
            //stop timer
            try
            {
                t.stop();
            }
            catch(NullPointerException ee)
            {}
            pause = true;

            //call method in database to load proper preset sim
            if(biome.getSelectedItem().equals("stable"))
                Database.stable();
            else if(biome.getSelectedItem().equals("unstable"))
                Database.unstable();
            else if(biome.getSelectedItem().equals("invasive species"))
                Database.invasive();
            else if(biome.getSelectedItem().equals("bacterial growth"))
                Database.bacteria();
            else if(biome.getSelectedItem().equals("slow growth"))
                Database.slowGrowth();
            else if(biome.getSelectedItem().equals("desert"))
                Database.desert();
            else if(biome.getSelectedItem().equals("sandbox mode"))
                Database.sandbox();

            //redraw the graph, clears previous data
            graph.draw();
        }

        revalidate();
        repaint ();  
    }

    //controls all buttons related to users clicking the simulation grid
    class MouseControlListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            //adjust booleans for adding according to buttons clicked
            if(e.getSource() == foodBtn)
            {
                addFood = true;
                addPrey = false;
                addPredator = false;

                foodBtn.setOpaque(true);
                preyBtn.setOpaque(false);
                predatorBtn.setOpaque(false);
            }
            else if(e.getSource() == preyBtn)
            {
                addFood = false;
                addPrey = true;
                addPredator = false;

                foodBtn.setOpaque(false);
                preyBtn.setOpaque(true);
                predatorBtn.setOpaque(false);
            }
            else if(e.getSource() == predatorBtn)
            {
                addFood = false;
                addPrey = false;
                addPredator = true;

                foodBtn.setOpaque(false);
                preyBtn.setOpaque(false);
                predatorBtn.setOpaque(true);
            }

            //toggle text and turn populate on/off
            else if(e.getSource() == togglePointerBtn)
            {
                if(togglePointerBtn.getText().equals("Pointer On"))
                {
                    togglePointerBtn.setText("Pointer Off");
                    togglePointerBtn.setOpaque(false);
                    pointer = false;

                    rate.setEnabled(true);

                    populate = true;
                }
                else//pointer off
                {
                    togglePointerBtn.setText("Pointer On");
                    togglePointerBtn.setOpaque(true);
                    pointer = true;

                    rate.setEnabled(false);
                    //get value of item is done elsewhere
                    populate = false;
                    eradicate = false;
                }
            }
            revalidate();
            repaint();
        }
    }

    public void mouseClicked(MouseEvent e)
    {
        if(pointer)
        {
            int x = e.getX()/5*5;
            int y = e.getY()/5*5;

            Environment.toggle(x,y);//turns cells on/off
            repaint();
        }
    }

    //necessary for inheritance
    public void mouseExited(MouseEvent e)
    {
    }

    public void mouseEntered(MouseEvent e)
    {
    }

    public void mousePressed(MouseEvent e)
    {
        pressed = e.getPoint();
        mousePress = true;
        //if populate/eradicate is turned on
        if((populate == true && eradicate == false) ||(populate == false && eradicate == true))
        {
            //check left or right mouse click and set populate/eradicate accordingly
            if(SwingUtilities.isLeftMouseButton(e))
            {
                populate = true;
                eradicate = false;
            }
            else
            {
                populate = false;
                eradicate = true;
            }
        }   
        repaint();
    }

    public void mouseReleased(MouseEvent e)
    {
        //get released point
        released = e.getPoint();

        //get rounded x and y value
        int x = (int)(pressed.getX()/5*5);
        int y = (int)(pressed.getY()/5*5);    

        //get change in x and y
        int width = x - (int)(released.getX()/5*5);
        int height = y - (int)(released.getY()/5*5);

        //set direction based on positive/negative change in x and y
        int direction = 0;
        if(width<0 && height > 0)
            direction = 1;
        else if(width>0 && height>0)
            direction = 2;
        else if(width>0 && height<0)
            direction = 3;
        else if(width<0 && height<0)
            direction = 4;

        //make change positive, so it becomes absolute width and height
        if(width<0)
            width = width*-1;
        if(height<0)
            height = height*-1;

        percent = Integer.parseInt(rate.getSelectedItem().toString())/100.0;

        //apply appropriate action by calling method to populate/eradicate
        if(populate)
            Environment.populate(x,y,width,height,direction);
        else if (eradicate)
            Environment.eradicate(x,y,width,height, direction);

        //reset dragged points
        xDragged = (int)(released.getX()/5*5);
        yDragged = (int)(released.getY()/5*5);

        mousePress = false;
        repaint();
    }

    public static void updateStats()
    {
        //redraw labels with correct stats
        generationCount.setText("Generation: " + (generation+1));
        foodCount.setText("Food: " + Food.population[generation]);
        preyCount.setText("Prey: " + Prey.population[generation]);
        predatorCount.setText("Predator: " + Predator.population[generation]);
        generation++;//update counter
    }

    class DrawArea extends JPanel implements MouseMotionListener
    {
        //constructor
        public DrawArea (int width, int height)
        {
            this.setPreferredSize (new Dimension (width, height));
            addMouseMotionListener(this);
        }

        public void mouseMoved(MouseEvent e)
        {
        }

        //for populate/eradcate
        public void mouseDragged(MouseEvent e)
        {
            dragged = e.getPoint();//get point

            //update dragged values
            xDragged = (int)(dragged.getX()/5*5);
            yDragged = (int)(dragged.getY()/5*5);
            if(pointer)//if populate/eradicate is off
            {
                int x = e.getX()/5*5;
                int y = e.getY()/5*5;

                //call method to turn cells on and off
                Environment.toggle(x,y);

                revalidate();
                repaint();
            }

        }

        public void paintComponent (Graphics g)
        {
            environment.show (g);
            try
            {
                //if mouse is pressed and eradicate/populate function is on
                if(pointer == false)
                {
                    if(mousePress)//if mouse is pressed
                    {
                        //set appropriate colour (populate = green, eradicate = red)
                        g.setColor (Color.green);
                        if(eradicate)
                            g.setColor(Color.red);

                        //draws single rectanlge at point pressed
                        int xPress = (int)(pressed.getX()/5*5);
                        int yPress = (int)(pressed.getY()/5*5);
                        g.fillRect(xPress,yPress,5,5);

                        //draws rectangle as mouse is moved
                        //get change in x and y 
                        int width = xPress - xDragged;
                        int height = yPress - yDragged;

                        //get direction based on x nd y change
                        int direction = 0;
                        if(width<0 && height > 0)
                            direction = 1;
                        else if(width>0 && height>0)
                            direction = 2;
                        else if(width>0 && height<0)
                            direction = 3;
                        else if(width<0 && height<0)
                            direction = 4;

                        //set to absolute value, so it is positive
                        if(width<0)
                            width = width*-1;
                        if(height<0)
                            height = height*-1;

                        //draw appropriate rectangle based on direction of dragging
                        if(direction == 4)//4
                            g.drawRect(xPress,yPress,width,height);
                        else if(direction == 3)//3
                            g.drawRect(xPress-width,yPress,width,height);
                        else if(direction == 2)//2
                            g.drawRect(xPress-width,yPress-height,width,height);
                        else if(direction == 1)//1
                            g.drawRect(xPress,yPress-height,width,height);
                    }
                }
            }
            catch(NullPointerException e)
            {}

            //set color of buttons
            //colors turned on/off by .setOpaque(true/false);
            foodBtn.setBackground(Color.green);
            preyBtn.setBackground(new Color(0,191,255));
            predatorBtn.setBackground(Color.red);
            togglePointerBtn.setBackground(new Color(132,112,255));

            revalidate();
            repaint();
        }
    }

    class Movement implements ActionListener
    {
        private Environment environment;

        public Movement (Environment enviro)
        {
            environment = enviro;
        }

        public void actionPerformed (ActionEvent event)
        {
            //update grid, stats, and graph
            environment.advance ();
            updateStats();
            graph.draw();

            //set scroll position on scroll bar, so the graph will automatically scroll
            scroll.getHorizontalScrollBar().setValue(graph.getAlign() - 200);

            revalidate();
            repaint ();
        }
    }

    //======================================================== method main
    public static void main (String[] args)
    {
        BalancedEcosystemGUI window = new BalancedEcosystemGUI ();
        window.setVisible (true);

        //2nd for stat control called in methods that control 2nd start screen
    }
}

class Environment
{
    private static Life grid[][];
    public Environment (double foodDensity, double preyDensity, double predatorDensity)
    {
        //create grid and fill with Life
        grid = new Life [105] [100];
        for (int row = 0 ; row < grid.length ; row++)
            for (int col = 0 ; col < grid [0].length ; col++)
            {   
                if(Math.random () < foodDensity)
                {
                    Food temp = new Food(0);
                    //food disappears (goes bad) after a set number of turns
                    //generate a random number for this counter, so it dies off more naturally
                    temp.setAliveCount((int)(Math.random()*5+1));
                    grid[row][col] = temp;
                }
                else if(Math.random()< preyDensity)
                {
                    grid[row][col] = new Prey(row,col);
                }
                else if(Math.random()<predatorDensity)
                {
                    grid[row][col] = new Predator(row,col);
                }
            }
    }

    public void show (Graphics g)
    {
        //go through grid and set appropriate colours for life forms
        for (int row = 0 ; row < grid.length ; row++)
            for (int col = 0 ; col < grid [0].length ; col++)
            {
                if (grid [row] [col] instanceof Food) // food
                    g.setColor(Color.green);
                else if (grid[row][col] instanceof Prey)
                    g.setColor(Color.blue);
                else if (grid[row][col] instanceof Predator)
                    g.setColor(Color.red);
                else 
                    g.setColor (Color.white);
                g.fillRect (col * 5, row * 5, 5, 5); // draw life form
            }

        //set colors of statistic labels
        BalancedEcosystemGUI.foodCount.setForeground(new Color(0,100,0));
        BalancedEcosystemGUI.preyCount.setForeground(Color.blue);              
        BalancedEcosystemGUI.predatorCount.setForeground(Color.red);
    }

    public void advance ()
    {
        Life nextGen[] [] = new Life [grid.length] [grid [0].length]; // create next generation of life forms

        //call methods that advance food, prey, then predator
        nextGen = foodAdvance(nextGen);
        nextGen = preyAdvance(nextGen);
        nextGen = predatorAdvance(nextGen);

        grid = nextGen; // update life forms
        updatePopulations();//update graphs and graph data
    }

    public static Life[][] foodAdvance(Life[][] nextGen)
    {
        //food
        for (int row = 0 ; row < grid.length ; row++)
            for (int col = 0 ; col < grid [0].length ; col++)
            {
                if(grid[row][col] instanceof Food)
                {
                    Food temp = (Food)(grid[row][col]);//create food object
                    if(temp.live())//if food is to stay to next gen
                        nextGen[row][col] = temp;
                    else
                        nextGen [row][col] = null;
                    temp = null;//clear temp
                }
                else if(Life.spawnFood(grid[row][col]))//call method that spawns food on blank cells
                    nextGen[row][col] = new Food(0);
            }
        return nextGen;
    }

    public static Life[][] preyAdvance(Life[][] nextGen)
    {
        for (int row = 0 ; row < grid.length ; row++)
            for (int col = 0 ; col < grid [0].length ; col++)
                if (grid[row][col] instanceof Prey)//only do if prey
                {
                    Prey animal = (Prey)grid[row][col];

                    animal.updateLive();//check is it can still live
                    if (animal.getLive() == true)//can live
                    {
                        int[] position = animal.eat(nextGen);//returns position of food to eat
                        if (position[0] != -1 && position[1] != -1)//eaten something
                        {
                            Food temp = (Food)(nextGen[position[0]][position[1]]);
                            temp.setAliveCount(0);

                            animal.changeRow(position[0]);
                            animal.changeCol(position[1]);
                            nextGen[position[0]][position[1]] = animal;//moves onto location of food
                            nextGen[row][col] = null;//remove original spot
                        }
                        else//no food eaten
                        {
                            animal.preyMove(grid);//updates row and col in prey

                            int r = animal.getRow();
                            int c = animal.getCol();
                            if(grid[r][c] instanceof Predator || grid[r][c] instanceof Prey)//don't move onto predator, stay in spot
                            {
                                animal.changeRow(row);
                                animal.changeCol(col);
                                nextGen[row][col] = animal;
                            }
                            else//not anything there
                            {
                                nextGen[r][c] = animal;
                                nextGen[row][col] = null;
                            }
                        }
                        animal.updateReproduce();//checks it it can reproduce

                        position = animal.reproduce(grid,row,col);//return position to reproduce onto

                        if(position[0]!=-1 && position[1]!=-1)  
                            nextGen[position[0]][position[1]] = new Prey(position[0],position[1]);
                    }
                    else
                        nextGen[row][col] = null;//kill animal
                }
        return nextGen;
    }

    public static Life[][] predatorAdvance(Life[][] nextGen)
    {
        for (int row = 0 ; row < grid.length ; row++)
            for (int col = 0 ; col < grid [0].length ; col++)
                if(grid[row][col] instanceof Predator)
                {
                    Predator animal = (Predator)grid[row][col];

                    animal.updateLive();//check if it can live
                    if (animal.getLive() == true)
                    {
                        int[] position = animal.eat(nextGen);//return position it will eat on

                        if (position[0] != -1 && position[1] != -1)//eaten something
                        {
                            nextGen[position[0]][position[1]] = animal;
                            nextGen[row][col] = null;
                        }
                        else//no food eaten
                        {
                            animal.predatorMove(grid);//updates row and col in animal

                            int r = animal.getRow();
                            int c = animal.getCol();
                            if(grid[r][c] instanceof Predator || nextGen[r][c] instanceof Food)//don't move onto prey
                            {
                                animal.changeRow(row);
                                animal.changeCol(col);
                                nextGen[row][col] = animal;
                            }
                            else//not anything there
                            {
                                nextGen[r][c] = animal;
                                nextGen[row][col] = null;
                            }
                        }
                        animal.updateReproduce();//check if it can reproduce

                        position = animal.reproduce(grid,row,col);//returns row and col of where to reproduce onto
                        if(position[0]!=-1 && position[1]!=-1)  
                            nextGen[position[0]][position[1]] = new Predator(position[0],position[1]);
                    }
                    else
                        nextGen[row][col] = null;//kill animal
                }
        return nextGen;
    }

    //getter methods
    public Life getValue(int row, int col)
    {
        return grid[row][col];
    }

    public void updatePopulations()
    {
        int food = 0;
        int prey = 0;
        int predator = 0;
        //counts number of food, prey, and predator
        for (int row = 0 ; row < grid.length ; row++)
            for (int col = 0 ; col < grid [0].length ; col++)
            {
                if(grid[row][col] instanceof Food)
                    food++;
                if(grid[row][col] instanceof Prey)
                    prey++;
                if(grid[row][col] instanceof Predator)
                    predator++;
            }

        //adds it to int[] using updatePopulation method
        Food.updatePopulation(food);
        Prey.updatePopulation(prey);
        Predator.updatePopulation(predator);
    }

    //clears data so graph will be blank
    public static void resetPopulations()
    {
        //calls method to clear data
        Food.reset();
        Prey.reset();
        Predator.reset();
        BalancedEcosystemGUI.generation = 0;//reset counter
    }

    public static void reset(double foodDensity, double preyDensity, double predatorDensity)
    {
        //kills previous grid
        //creates new grid
        for (int row = 0 ; row < grid.length ; row++)
            for (int col = 0 ; col < grid [0].length ; col++)
            {   
                grid[row][col] = null;
                if(Math.random () < foodDensity)
                {
                    Food temp = new Food(0);
                    temp.setAliveCount((int)(Math.random()*5+1));
                    grid[row][col] = temp;
                }
                else if(Math.random()< preyDensity)
                {
                    grid[row][col] = new Prey(row,col);
                }
                else if(Math.random()<predatorDensity)
                {
                    grid[row][col] = new Predator(row,col);
                }
            }
    }

    public static void toggle(int x, int y)
    {
        for(int row = 0; row<grid.length; row++)
            for(int col = 0; col<grid[0].length; col++)
            //if click is within 2 pixels of a drawn cell
                if(Math.abs(col*5 - x)<2 && Math.abs(row*5 - y)<2)
                {
                    //turns off cells if it is alive
                    if(grid[row][col] instanceof Food)
                        grid[row][col] = null;
                    else if(grid[row][col] instanceof Prey)
                        grid[row][col] = null;
                    else if(grid[row][col] instanceof Predator)
                        grid[row][col] = null;
                    else if(grid[row][col] == null)
                    {
                        //if it's dead, fills cell with either food, prey, or predator
                        //checks with boolean to see which one to add
                        if(BalancedEcosystemGUI.addFood)
                            grid[row][col] = new Food(1);
                        else if(BalancedEcosystemGUI.addPrey)
                            grid[row][col] = new Prey(row,col);
                        else if(BalancedEcosystemGUI.addPredator)
                            grid[row][col] = new Predator(row,col);
                    }
                }
    }

    //accepts x,y coordinate, (positive)width+ height, and direction, populates cells in that rectangle
    public static void populate(int x, int y, int width, int height, int direction)
    {
        for (int row = 0 ; row < grid.length ; row++)
            for (int col = 0 ; col < grid [0].length ; col++)
            {
                //set up points on grid
                int xpoint = col*5;
                int ypoint = row*5;

                //call spawn method on appropriate rectangle area
                if(direction == 1)
                {
                    if(xpoint>x && ypoint<y)
                        if(xpoint<(x+width) && ypoint>(y-height))
                            spawn(row, col);
                }
                else if(direction == 2)
                {
                    if(xpoint<x && ypoint<y)
                        if(xpoint>(x-width) && ypoint>(y-height))
                            spawn(row, col);
                }
                else if(direction == 3)
                {
                    if(xpoint<x && ypoint>y)
                        if(xpoint>(x-width) && ypoint<(y+height))
                            spawn(row, col);
                }
                else if(direction == 4)
                {
                    if(xpoint>x && ypoint>y)
                        if(xpoint<(x+width) && ypoint<(y+height))
                            spawn(row, col);
                }
            }
    }

    //accepts x,y coordinate, (positive)width+ height, and direction, eradicates cells in that rectangle
    public static void eradicate(int x, int y, int width, int height, int direction)
    {
        for (int row = 0 ; row < grid.length ; row++)
            for (int col = 0 ; col < grid [0].length ; col++)
            {
                //set up change x and y points on grid
                int xpoint = col*5;
                int ypoint = row*5;

                //call kill method on appropriate rectangle area
                if(direction == 1)
                {
                    if(xpoint>x && ypoint<y)
                        if(xpoint<(x+width) && ypoint>(y-height))
                            kill(row, col);
                }
                else if(direction == 2)
                {
                    if(xpoint<x && ypoint<y)
                        if(xpoint>(x-width) && ypoint>(y-height))
                            kill(row, col);
                }
                else if(direction == 3)
                {
                    if(xpoint<x && ypoint>y)
                        if(xpoint>(x-width) && ypoint<(y+height))
                            kill(row, col);
                }
                else if(direction == 4)
                {
                    if(xpoint>x && ypoint>y)
                        if(xpoint<(x+width) && ypoint<(y+height))
                            kill(row, col);
                }
            }
    }

    public static void spawn(int row, int col)
    {
        //compares with live percent value determined by user
        if(Math.random()<BalancedEcosystemGUI.percent)
        {
            //checks which object to add
            if(BalancedEcosystemGUI.addFood)
                grid[row][col] = new Food(1);
            else if(BalancedEcosystemGUI.addPrey)
                grid[row][col] = new Prey(row,col);
            else if(BalancedEcosystemGUI.addPredator)
                grid[row][col] = new Predator(row,col);
        }
    }

    //accepts row and col, randomly kills it based on density specified from before
    public static void kill(int row, int col)
    {
        //compares with live percent value determined by user
        if(Math.random()<BalancedEcosystemGUI.percent)
            grid[row][col] = null;//kills cell
    }

    //in some preset sims, i only want to have 1 predator
    public static void addPredator(int row, int col)
    {
        //adds predator to grid on given row,col
        grid[row][col] = new Predator(row,col);
    }
}