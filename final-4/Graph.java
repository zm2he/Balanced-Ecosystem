import java.awt.geom.AffineTransform;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Graph extends JLabel//uses JLabel to display graph
{
    //set up things to draw
    private Graphics2D g;
    private DrawingPane drawingPane;
    private AffineTransform affineTransform = new AffineTransform();
    private int align = 0;

    private static double foodScale = 0.1;
    private static double preyScale = 0.25;
    private static double predatorScale = 2.5;

    public Graph()
    {
        //up JLabel
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.LINE_AXIS));
        drawingPane = new DrawingPane(2000,290);//intialize drawingPane

        drawingPane.draw();//call draw to draw axis and blank graph
    }

    //returns drawingPane (to be displayed)
    public DrawingPane getPane()
    {
        return drawingPane;
    }

    //used by GUI to update graph
    public void draw()
    {
        drawingPane.draw();
    }

    public int getAlign()
    {
        return align;
    }

    public static void setScaling(double food, double prey, double predator)
    {
        foodScale = food;
        preyScale = prey;
        predatorScale = predator;
    }

    public class DrawingPane extends BufferedImage//graph is a buffered image
    {
        private Graphics2D g;

        //set up buffered image, constructor
        public DrawingPane(int width, int height)
        {
            super(width,height,BufferedImage.TYPE_INT_ARGB);
        }

        //method that draws everything onto graph
        public void draw()
        {
            g = this.createGraphics();

            //draw background of white
            g.setColor(Color.white);
            g.fillRect(0,0,2000,300);

            //draw axis and axis labels
            g.setColor(Color.black);
            g.drawLine(19, 20, 19, 261);
            g.drawLine(19, 261, 220, 261);
            affineTransform.rotate(-Math.PI / 2);
            g.setFont(g.getFont().deriveFont(affineTransform)); 
            g.drawString("Population", 15, 190);
            affineTransform.rotate(Math.PI / 2);
            g.setFont(g.getFont().deriveFont(affineTransform));
            g.drawString("Generation", 90, 275);

            //draw x-axis all the way to the end of the graph
            for(int i = 200; i<2000; i = i+150)
            {
                g.setColor(Color.black);
                g.drawLine(19+i, 261, 220+1, 261);
                g.drawString("Generation", i, 275);
            }

            //setup variables used to draw
            int x1,x2,y1,y2;

            int j = 0;//initialize drawing counter
            for(int i = 0;i<BalancedEcosystemGUI.generation-1; i++)
            {	
                j = i%400;//j will reset back to 0, every 400, so it jumps from the end to the start of the graph to continue drawing
                if(j == 0)//if drawing starting from the beginning
                {
                    //redraw white background
                    g.setColor(Color.white);
                    g.fillRect(0,0,2000,300);

                    //redraw axis and axis labels
                    g.setColor(Color.black);
                    g.drawLine(19, 20, 19, 261);
                    g.drawLine(19, 261, 220, 261);
                    affineTransform.rotate(-Math.PI / 2);
                    g.setFont(g.getFont().deriveFont(affineTransform)); 
                    g.drawString("Population", 15, 190);
                    affineTransform.rotate(Math.PI / 2);
                    g.setFont(g.getFont().deriveFont(affineTransform));
                    g.drawString("Generation", 90, 275);

                    //redraw x-axis all the way to the end of the graph
                    for(int k = 200; k<2000; k = k+150)
                    {
                        g.setColor(Color.black);
                        g.drawLine(19+k, 261, 220+k, 261);
                        g.drawString("Generation", k, 275);
                    }
                }

                //food
                g.setColor(Color.green);
                x1 = j*5;//x scaling by 5
                x2 = (j+1)*5;
                y1 = drawingPane.getHeight() - (int)(Food.population[i]*foodScale) - 50;//y scaling by 1/10
                y2 = drawingPane.getHeight() - (int)(Food.population[i+1]*foodScale) - 50;

                align = x2; //to set scroll position

                //ensure the lines remains unbroken when drawing at the top of the graph
                if(y1>=0 && y2<=0)
                    g.drawLine(x1+19,y1+20,x2+19,20);
                else if(y1 <= 0 && y2 <=0)
                    g.drawLine(x1+19,20,x2+19,20);
                else if(y1<=0 && y2>=0)
                    g.drawLine(x1+19,20,x2+19,y2+20);
                else
                    g.drawLine(x1+19,y1+20,x2+19,y2+20);

                //prey
                g.setColor(Color.blue);
                x1 = j*5;
                x2 = (j+1)*5;
                y1 = drawingPane.getHeight() - (int)(Prey.population[i]*preyScale) - 50;//y scaling by 1/2
                y2 = drawingPane.getHeight() - (int)(Prey.population[i+1]*preyScale) - 50;
                //System.out.println("prey y1, y2:  " + y1+", " + y2);
                if(y1>=0 && y2<=0)
                    g.drawLine(x1+19,y1+20,x2+19,20);
                else if(y1 <= 0 && y2 <=0)
                    g.drawLine(x1+19,20,x2+19,20);
                else if(y1<=0 && y2>=0)
                    g.drawLine(x1+19,20,x2+19,y2+20);
                else
                    g.drawLine(x1+19,y1+20,x2+19,y2+20);

                //predator
                g.setColor(Color.red);
                x1 = j*5;
                x2 = (j+1)*5;
                y1 = drawingPane.getHeight() - (int)(Predator.population[i]*predatorScale) - 50;//y scaling by 4
                y2 = drawingPane.getHeight()- (int)(Predator.population[i+1]*predatorScale) - 50;
                if(y1>=0 && y2<=0)
                    g.drawLine(x1+19,y1+20,x2+19,20);
                else if(y1 <= 0 && y2 <=0)
                    g.drawLine(x1+19,20,x2+19,20);
                else if(y1<=0 && y2>=0)
                    g.drawLine(x1+19,20,x2+19,y2+20);
                else
                    g.drawLine(x1+19,y1+20,x2+19,y2+20);
            }
        }
    }
}