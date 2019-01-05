//set variables to corresponding values of that simulation
public class Database
{
    //set up variables
    private static double foodSpawn = 0.01;
    private static int foodLimit = 20;

    private static int foodEnergy = 4;
    private static int preyHunger = 15;
    private static int preyFoodRequired = 15;
    private static int preyRepCoolDown = 5;

    private static int preyEnergy = 5;
    private static int predatorHunger = 25;
    private static int predatorFoodRequired = 100;
    private static int predatorRepCoolDown = 5;

    //variables necessary for stable simulation
    public static void stable()
    {
        //(food %, prey %, predator %)
        Environment.reset(0.05,0.01,0.0005);//clear grid, populate with new life
        Environment.resetPopulations();//clear poplulation data (for graph)

        //change variables accordingly
        foodSpawn = 0.01;
        foodLimit = 20;

        foodEnergy = 4;
        preyHunger = 15;
        preyFoodRequired = 15;
        preyRepCoolDown = 5;

        preyEnergy = 5;
        predatorHunger = 25;
        predatorFoodRequired = 100;
        predatorRepCoolDown = 5;

        //call method to load new variables into Food, Prey, and Predator class
        updateConstants();
        //call method to update sliders and labels in constant control window
        updateConstantControl();

        //change scaling of graph so lines don't go off the graph
        Graph.setScaling(0.1,0.25,2.5);
    }

    //variables for unstable population
    public static void unstable()
    {
        Environment.reset(0.05,0.01,0.001);
        Environment.resetPopulations();

        foodSpawn = 0.01;
        foodLimit = 20;

        foodEnergy = 4;
        preyHunger = 15;
        preyFoodRequired = 5;
        preyRepCoolDown = 5;

        preyEnergy = 5;
        predatorHunger = 40;
        predatorFoodRequired = 50;
        predatorRepCoolDown = 40;

        updateConstants();
        updateConstantControl();

        Graph.setScaling(0.1,0.3,2);
    }

    //invasive species
    //uncontrolled predator growth
    public static void invasive()
    {
        //notice predator % is 0, because i only want 1 predator
        Environment.reset(0.05,0.01,0);
        Environment.resetPopulations();

        foodSpawn = 0.01;
        foodLimit = 20;

        foodEnergy = 4;
        preyHunger = 15;
        preyFoodRequired = 5;
        preyRepCoolDown = 5;

        preyEnergy = 5;
        predatorHunger = 40;
        predatorFoodRequired = 20;
        predatorRepCoolDown = 15;

        updateConstants();
        updateConstantControl();

        //only add 1 predator
        //do this by calling addPredator method on random row and col
        Environment.addPredator((int)(Math.random()*105),(int)(Math.random()*100));

        Graph.setScaling(0.1,0.17,0.8);
    }

    //bacterial growth
    //lots of food, fast reproduction
    public static void bacteria()
    {
        Environment.reset(0.05,0.01,0.0005);
        Environment.resetPopulations();

        foodSpawn = 0.03;
        foodLimit = 20;

        foodEnergy = 4;
        preyHunger = 15;
        preyFoodRequired = 5;
        preyRepCoolDown = 5;

        preyEnergy = 5;
        predatorHunger = 100;
        predatorFoodRequired = 100;
        predatorRepCoolDown = 500;

        updateConstants();
        updateConstantControl();

        Graph.setScaling(0.025,0.05,1);
    }

    //very slow changes, long time for predator to reproduce
    public static void slowGrowth()
    {
        Environment.reset(0.05,0.01,0.001);
        Environment.resetPopulations();

        foodSpawn = 0.01;
        foodLimit = 20;

        foodEnergy = 4;
        preyHunger = 15;
        preyFoodRequired = 5;
        preyRepCoolDown = 5;

        preyEnergy = 5;
        predatorHunger = 100;
        predatorFoodRequired = 100;
        predatorRepCoolDown = 500;

        updateConstants();
        updateConstantControl();

        Graph.setScaling(0.1,0.2,3);
    }

    //little food, less prey and predators
    //need less food to survive
    public static void desert()
    {
        Environment.reset(0.01,0.01,0);
        Environment.resetPopulations();

        foodSpawn = 0.0003;
        foodLimit = 50;

        foodEnergy = 50;
        preyHunger = 100;
        preyFoodRequired = 95;
        preyRepCoolDown = 20;

        preyEnergy = 100;
        predatorHunger = 20;
        predatorFoodRequired = 100;
        predatorRepCoolDown = 500;

        updateConstants();
        updateConstantControl();

        //only add 1 predator
        //do this by calling addPredator method on random row and col
        Environment.addPredator((int)(Math.random()*105),(int)(Math.random()*100));

        Graph.setScaling(2,1,15);
    }

    //nothing ever dies, just to play around
    public static void sandbox()
    {
        Environment.reset(0,0,0);//clear grid, populate with new life
        Environment.resetPopulations();//clear poplulation data (for graph)

        foodSpawn = 0.0;
        foodLimit = 20000;

        foodEnergy = 4;
        preyHunger = 15345;
        preyFoodRequired = 15;
        preyRepCoolDown = 5345543;

        preyEnergy = 5;
        predatorHunger = 23455;
        predatorFoodRequired = 100;
        predatorRepCoolDown = 5345345;

        updateConstants();
        updateConstantControl();

        //change scaling of graph so lines don't go off the graph
        Graph.setScaling(0.1,0.25,2.5);
    }

    public static void updateConstants()
    {
        //use method in Food, Prey, and Predator class to change
        //must be static methods
        
        //set food variables
        Food.setFoodSpawn(foodSpawn);
        Food.setFoodLimit(foodLimit);

        //set prey variables
        Prey.setFoodEnergy(foodEnergy);
        Prey.setHunger(preyHunger);
        Prey.setFoodRequired(preyFoodRequired);
        Prey.setRepCoolDown(preyRepCoolDown);

        //set predator variables
        Predator.setPreyEnergy(preyEnergy);
        Predator.setHunger(predatorHunger);
        Predator.setFoodRequired(predatorFoodRequired);
        Predator.setRepCoolDown(predatorRepCoolDown);
    }

    public static void updateConstantControl()
    {
        //update sliders and labels in other window by calling update() in ConstantControl
        BalancedEcosystemGUI.control.update(foodSpawn, foodLimit, foodEnergy, preyHunger, preyFoodRequired, preyRepCoolDown, preyEnergy, predatorHunger, predatorFoodRequired, predatorRepCoolDown);
    }
}
