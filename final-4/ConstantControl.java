import java.awt.*;
import javax.swing.*;
import java.awt.event.*;  // Needed for ActionListener
import javax.swing.event.*;  // Needed for ActionListener
import java.util.*;
import java.lang.Math;
import javax.swing.JSlider;

public class ConstantControl extends JFrame
{
    //create panel
    private JPanel content = new JPanel();
    private JButton update;//create button

    //set up labels
    private static JLabel foodSpawnLbl, foodLimitLbl;
    private static JLabel foodEnergyLbl, preyHungerLbl, preyFoodRequiredLbl, preyRepCoolDownLbl;
    private static JLabel preyEnergyLbl, predatorHungerLbl, predatorFoodRequiredLbl, predatorRepCoolDownLbl;

    //set up sliders
    private static JSlider foodSpawnSl, foodLimitSl;
    private static JSlider foodEnergySl, preyHungerSl, preyFoodRequiredSl, preyRepCoolDownSl;
    private static JSlider preyEnergySl, predatorHungerSl, predatorFoodRequiredSl, predatorRepCoolDownSl;
    public ConstantControl()
    {
        //set up layout
        content.setLayout(new GridLayout(11,2));
        //////////////////////////////////////////////////
        //food spawn
        foodSpawnLbl = new JLabel("Food Spawn Rate (%: )");//make label
        content.add(foodSpawnLbl);

        //create slider
        foodSpawnSl = new JSlider(JSlider.HORIZONTAL, 0, 5, 1);
        foodSpawnSl.addChangeListener (new SliderListener1());
        foodSpawnSl.setMinorTickSpacing(1);
        foodSpawnSl.setMajorTickSpacing(5);
        foodSpawnSl.setPaintTicks(true);
        foodSpawnSl.setPaintLabels(true);
        foodSpawnSl.setPreferredSize(new Dimension(300, 80));
        content.add(foodSpawnSl);
        //////////////////////////////////////////////

        //food limit
        foodLimitLbl = new JLabel("Food Limit: ");//make label
        content.add(foodLimitLbl);

        //create slider
        foodLimitSl = new JSlider(JSlider.HORIZONTAL, 10, 30, 10);
        foodLimitSl.addChangeListener (new SliderListener2());
        foodLimitSl.setMinorTickSpacing(1);
        foodLimitSl.setMajorTickSpacing(5);
        foodLimitSl.setPaintTicks(true);
        foodLimitSl.setPaintLabels(true);
        foodLimitSl.setPreferredSize(new Dimension(300, 80));
        content.add(foodLimitSl);
        //////////////////////////////////////////////////////

        //food energy
        foodEnergyLbl = new JLabel("Food Energy");//make label
        content.add(foodEnergyLbl);

        //create slider
        foodEnergySl = new JSlider(JSlider.HORIZONTAL, 0, 100, 4);
        foodEnergySl.addChangeListener (new SliderListener3());
        foodEnergySl.setMinorTickSpacing(5);
        foodEnergySl.setMajorTickSpacing(10);
        foodEnergySl.setPaintTicks(true);
        foodEnergySl.setPaintLabels(true);
        foodEnergySl.setPreferredSize(new Dimension(300, 80));
        content.add(foodEnergySl);

        ///////////////////////////////////////////
        //prey hunger
        preyHungerLbl = new JLabel("Prey Hunger: ");//
        content.add(preyHungerLbl);

        preyHungerSl = new JSlider(JSlider.HORIZONTAL, 5, 30, 5);
        preyHungerSl.addChangeListener (new SliderListener4());
        preyHungerSl.setMinorTickSpacing(1);
        preyHungerSl.setMajorTickSpacing(5);
        preyHungerSl.setPaintTicks(true);
        preyHungerSl.setPaintLabels(true);
        preyHungerSl.setPreferredSize(new Dimension(300, 80));
        content.add(preyHungerSl);

        ///////////////////////////////////////////
        //prey food required
        preyFoodRequiredLbl = new JLabel("Food Required for Prey Reproduction: ");//
        content.add(preyFoodRequiredLbl);

        preyFoodRequiredSl = new JSlider(JSlider.HORIZONTAL, 0, 30, 0);
        preyFoodRequiredSl.addChangeListener (new SliderListener5());
        preyFoodRequiredSl.setMinorTickSpacing(1);
        preyFoodRequiredSl.setMajorTickSpacing(5);
        preyFoodRequiredSl.setPaintTicks(true);
        preyFoodRequiredSl.setPaintLabels(true);
        preyFoodRequiredSl.setPreferredSize(new Dimension(300, 80));
        content.add(preyFoodRequiredSl);

        ///////////////////////////////////////////
        //prey reproduction
        preyRepCoolDownLbl = new JLabel("Prey Reproduction Period: ");//
        content.add(preyRepCoolDownLbl);

        preyRepCoolDownSl = new JSlider(JSlider.HORIZONTAL, 0, 30, 0);
        preyRepCoolDownSl.addChangeListener (new SliderListener6());
        preyRepCoolDownSl.setMinorTickSpacing(1);
        preyRepCoolDownSl.setMajorTickSpacing(5);
        preyRepCoolDownSl.setPaintTicks(true);
        preyRepCoolDownSl.setPaintLabels(true);
        preyRepCoolDownSl.setPreferredSize(new Dimension(300, 80));
        content.add(preyRepCoolDownSl);

        ///////////////////////////////////////////
        //prey energy
        preyEnergyLbl = new JLabel("Prey Energy: ");//
        content.add(preyEnergyLbl);

        preyEnergySl = new JSlider(JSlider.HORIZONTAL, 0, 30, 5);
        preyEnergySl.addChangeListener (new SliderListener7());
        preyEnergySl.setMinorTickSpacing(1);
        preyEnergySl.setMajorTickSpacing(5);
        preyEnergySl.setPaintTicks(true);
        preyEnergySl.setPaintLabels(true);
        preyEnergySl.setPreferredSize(new Dimension(300, 80));
        content.add(preyEnergySl);

        ///////////////////////////////////////////
        //predator hunger
        predatorHungerLbl = new JLabel("Predator Hunger: ");
        content.add(predatorHungerLbl);

        predatorHungerSl = new JSlider(JSlider.HORIZONTAL, 5, 50, 25);
        predatorHungerSl.addChangeListener (new SliderListener8());
        predatorHungerSl.setMinorTickSpacing(5);
        predatorHungerSl.setMajorTickSpacing(10);
        predatorHungerSl.setPaintTicks(true);
        predatorHungerSl.setPaintLabels(true);
        predatorHungerSl.setPreferredSize(new Dimension(300, 80));
        content.add(predatorHungerSl);

        ///////////////////////////////////////////
        //predator food required
        predatorFoodRequiredLbl = new JLabel("Food Required for Predator Reproduction: ");//
        content.add(predatorFoodRequiredLbl);

        predatorFoodRequiredSl = new JSlider(JSlider.HORIZONTAL, 0, 50, 5);
        predatorFoodRequiredSl.addChangeListener (new SliderListener9());
        predatorFoodRequiredSl.setMinorTickSpacing(5);
        predatorFoodRequiredSl.setMajorTickSpacing(10);
        predatorFoodRequiredSl.setPaintTicks(true);
        predatorFoodRequiredSl.setPaintLabels(true);
        predatorFoodRequiredSl.setPreferredSize(new Dimension(300, 80));
        content.add(predatorFoodRequiredSl);

        ///////////////////////////////////////////
        //predator reproduction
        predatorRepCoolDownLbl = new JLabel("Predator Reproduction Period: ");//
        content.add(predatorRepCoolDownLbl);

        predatorRepCoolDownSl = new JSlider(JSlider.HORIZONTAL, 0, 50, 20);
        predatorRepCoolDownSl.addChangeListener (new SliderListener10());
        predatorRepCoolDownSl.setMinorTickSpacing(5);
        predatorRepCoolDownSl.setMajorTickSpacing(10);
        predatorRepCoolDownSl.setPaintTicks(true);
        predatorRepCoolDownSl.setPaintLabels(true);
        predatorRepCoolDownSl.setPreferredSize(new Dimension(300, 80));
        content.add(predatorRepCoolDownSl);
        ///////////////////////////////////////////

        //ensure slider values are the same as values from simulation
        //create food object, get food spawn and food limit
        //set slider value to that
        Food temp = new Food(1);
        foodSpawnSl.setValue((int)(temp.getFoodSpawn()*100));
        foodLimitSl.setValue(temp.getFoodLimit());

        //create prey object, get necessary values
        //set slider value to that
        Prey temp1 = new Prey(0,0);
        foodEnergySl.setValue(temp1.getFoodEnergy());
        preyHungerSl.setValue(temp1.getHunger());
        preyFoodRequiredSl.setValue(temp1.getFoodRequired());
        preyRepCoolDownSl.setValue(temp1.getRepCoolDown());

        //create predator object, get necessary values
        //set slider value to that
        Predator temp2 = new Predator(0,0);
        preyEnergySl.setValue(temp2.getPreyEnergy());
        predatorHungerSl.setValue(temp2.getHunger());
        predatorFoodRequiredSl.setValue(temp2.getFoodRequired());
        predatorRepCoolDownSl.setValue(temp2.getRepCoolDown());

        /////////////////////////////////////////
        //set labels to correct slider values
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

        /////////////////////////////////////////
        //color the labels
        foodSpawnLbl.setForeground(new Color(0,100,0));
        foodLimitLbl.setForeground(new Color(0,100,0));

        foodEnergyLbl.setForeground(new Color(0,191,255));
        preyHungerLbl.setForeground(new Color(0,191,255));
        preyFoodRequiredLbl.setForeground(new Color(0,191,255));
        preyRepCoolDownLbl.setForeground(new Color(0,191,255));

        preyEnergyLbl.setForeground(Color.red);
        predatorHungerLbl.setForeground(Color.red);
        predatorFoodRequiredLbl.setForeground(Color.red);
        predatorRepCoolDownLbl.setForeground(Color.red);
        /////////////////////////////////////////

        //create update button
        update = new JButton("Update");
        update.addActionListener(new btnListener());
        content.add (update);

        //finish
        setContentPane(content);
        pack();
        setTitle ("Change Organism Characteristics");
        setSize(800,600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    class btnListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource() == update)
            {
                //if update is pressed
                //change all values in classes
                Food.setFoodSpawn(foodSpawnSl.getValue()/100.0); //get the new value
                Food.setFoodLimit((int)foodLimitSl.getValue()); //get the new value
                Prey.setFoodEnergy((int)foodEnergySl.getValue()); //get the new value
                Prey.setHunger( (int)preyHungerSl.getValue()); //get the new value
                Prey.setFoodRequired((int)preyFoodRequiredSl.getValue()); //get the new value
                Prey.setRepCoolDown((int)preyRepCoolDownSl.getValue()); //get the new value
                Predator.setPreyEnergy((int)preyEnergySl.getValue()); //get the new value
                Predator.setHunger((int)predatorHungerSl.getValue()); //get the new value
                Predator.setFoodRequired((int)predatorFoodRequiredSl.getValue()); //get the new value
                Predator.setRepCoolDown((int)predatorRepCoolDownSl.getValue()); //get the new value
            }
        }
    }

    //food spawn
    class SliderListener1 implements ChangeListener{
        public void stateChanged(ChangeEvent ce)
        {
            JSlider foodSpawnSl = (JSlider)ce.getSource();//declare slider that is source
            if(!foodSpawnSl.getValueIsAdjusting()) //if the value has changed
            {
                //update label
                foodSpawnLbl.setText("Food Spawn Rate (%): " + foodSpawnSl.getValue());
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
                preyHungerLbl.setText("Prey Hunger: " + preyHungerSl.getValue());
            }
        }
    }

    //prey food required
    class SliderListener5 implements ChangeListener{
        public void stateChanged(ChangeEvent ce)
        {
            JSlider preyFoodRequiredSl = (JSlider)ce.getSource();
            if(!preyFoodRequiredSl.getValueIsAdjusting()) //if the value has changed
            {
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
                preyRepCoolDownLbl.setText("Prey Reproduction Rate: " + preyRepCoolDownSl.getValue());
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
                predatorRepCoolDownLbl.setText("Predator Reproduction Rate: " + predatorRepCoolDownSl.getValue());
            }
        }
    }

    //accepts values for all variables, updates them in constant control window
    public void update(double foodSpawn, int foodLimit, int foodEnergy, int preyHunger, int preyFoodRequired, int preyCoolDown, int preyEnergy, int predatorHunger, int predatorFoodRequired, int predatorRepCoolDown)
    {
        //set all values in sliders
        foodSpawnSl.setValue((int)(foodSpawn*100));
        foodLimitSl.setValue(foodLimit);
        foodEnergySl.setValue(foodEnergy);
        preyHungerSl.setValue(preyHunger);
        preyFoodRequiredSl.setValue(preyFoodRequired);
        preyRepCoolDownSl.setValue(preyCoolDown);
        preyEnergySl.setValue(preyEnergy);
        predatorHungerSl.setValue(predatorHunger);
        predatorFoodRequiredSl.setValue(predatorFoodRequired);
        predatorRepCoolDownSl.setValue(predatorRepCoolDown);

        //change labels to match all sliders
        foodSpawnLbl.setText("Food Spawn Rate (%): " + foodSpawnSl.getValue());
        foodLimitLbl.setText("Food Expiration Rate ( # of turns): " + foodLimitSl.getValue());
        foodEnergyLbl.setText("Food Energy: " + foodEnergySl.getValue());
        preyHungerLbl.setText("Prey Hunger: " + preyHungerSl.getValue());
        preyFoodRequiredLbl.setText("Food Required for Prey Reproduction: " + preyFoodRequiredSl.getValue());
        preyRepCoolDownLbl.setText("Prey Reproduction Period: " + preyRepCoolDownSl.getValue());
        preyEnergyLbl.setText("Prey Energy: " + preyEnergySl.getValue());
        predatorHungerLbl.setText("Predator Hunger: " + predatorHungerSl.getValue());
        predatorFoodRequiredLbl.setText("Food Required for Predator Reproduction: " + predatorFoodRequiredSl.getValue());
        predatorRepCoolDownLbl.setText("Predator Reproduction Period: " + predatorRepCoolDownSl.getValue());
    }
}
