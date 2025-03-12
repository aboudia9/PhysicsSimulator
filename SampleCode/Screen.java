package PhysicsSimulator;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Screen extends JPanel implements ActionListener {

   private JButton button1;
   private JButton button2;

   private JTextField textField1;

   private JTextArea textArea1;
   private Shape circle;

   public Screen() {
      this.setLayout(null);

      // create buttons
      button1 = new JButton("Button 1");
      button1.setBounds(20, 20, 100, 30);
      add(button1);
      button1.addActionListener(this);

      button2 = new JButton("Button 2");
      button2.setBounds(150, 20, 100, 30);
      add(button2);
      button2.addActionListener(this);

      // create text fields
      textField1 = new JTextField(20);
      textField1.setBounds(20, 250, 200, 50);
      this.add(textField1);

      // create text areas
      textArea1 = new JTextArea();
      textArea1.setBounds(20, 400, 200, 150);
      textArea1.setEditable(false);
      textArea1.setFont(new Font("Arial", Font.PLAIN, 13));
     
      String text = "Display text";
      textArea1.setText(text);
      this.add(textArea1);

      // create objects
      circle = new Shape();
   }

   public Dimension getPreferredSize() {
      // Sets the size of the panel
      return new Dimension(800, 600);
   }

   public void paintComponent(Graphics g) {
      super.paintComponent(g);

      // draw background
      g.setColor(new Color(189, 230, 255));
      g.fillRect(0, 0, 800, 600);

      circle.drawMe(g); // draw shape
   }

   public void actionPerformed(ActionEvent e) {
      if (e.getSource() == button1) { // if button1 is clicked
      }

      if (e.getSource() == button2) { // if button2 is clicked
      }

      String textInput = textField1.getText(); //get text from textfield

      // refresh
      repaint();
   }

   public void animate() {
      while (true) {
         // move object
         circle.moveRight();

         // wait for .01 second
         try {
            Thread.sleep(10);
         } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
         }

         // repaint the graphics drawn
         repaint();
      }

   }

}