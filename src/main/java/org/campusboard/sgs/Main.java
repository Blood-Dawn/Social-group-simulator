package org.campusboard.sgs;

import javax.swing.*;
import org.campusboard.sgs.controller.*;
import org.campusboard.sgs.Persistence.*;
import org.campusboard.sgs.view.MainWindow;

/**
 * Main entry point for the Campus Board application.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // TODO: Initialize repositories
            // TODO: PostRepository postRepo = new InMemoryPostRepository();
            // TODO: UserRepository userRepo = new InMemoryUserRepository();
            
            // TODO: Initialize event bus
            // TODO: EventBus eventBus = new EventBus();
            
            // TODO: Initialize controller
            // TODO: Controller controller = new Controller(postRepo, userRepo, eventBus);
            
            // TODO: Initialize and show main window
            // TODO: MainWindow mainWindow = new MainWindow(controller, eventBus);
            // TODO: mainWindow.setVisible(true);
            
            // Temporary simple window until TODO items are implemented
            JFrame frame = new JFrame("Campus Board - Coming Soon");
            frame.setSize(900, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new JLabel("Campus Board - Implementation in Progress", JLabel.CENTER));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
