package com.varunsingh.ambientnoiseremover;

import java.awt.CardLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UserInterface extends JFrame {

    private static final long serialVersionUID = 5893292753279699800L;

    public UserInterface() {
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        add(displayFilterMethods());

        setVisible(true);
    }

    private JPanel displayFilterMethods() {
        JPanel completeFilterPanel = new JPanel();

        completeFilterPanel.add(new JLabel("Which filter would you like to use?"));

        String[] filterMethods = new String[] { 
            "Alpha Filter", 
            "Alpha Beta Filter", 
            "Alpha Beta Gamma Filter", 
            "One Dimensional Kalman Filter", 
            "Multi Dimensional Kalman Filter"
        };
        
        JPanel filterCards = new JPanel(new CardLayout());

        JComboBox<String> filterMethodsGroup = new JComboBox<>(filterMethods);
        filterMethodsGroup.setEditable(false);
        filterMethodsGroup.addItemListener(evt -> {
            CardLayout cl = (CardLayout)(filterCards.getLayout());
            cl.show(filterCards, (String)evt.getItem());
        });

        completeFilterPanel.add(filterMethodsGroup);
        
        filterCards.add(displayAlphaFilterControls(), "Alpha Filter");
        filterCards.add(new JPanel(), "Alpha Beta Filter");
        filterCards.add(new JPanel(), "Alpha Beta Gamma Filter");

        completeFilterPanel.add(filterCards);
        completeFilterPanel.add(new JButton("Calculate estimates"));

        return completeFilterPanel;
    }

    private JPanel displayAlphaFilterControls() {
        JPanel alphaFilterControls = new JPanel();

        alphaFilterControls.add(new JTextField("Initial Guess"));

        return alphaFilterControls;
    }

}
