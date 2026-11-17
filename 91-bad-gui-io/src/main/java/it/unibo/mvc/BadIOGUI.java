package it.unibo.mvc;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * This class is a simple application that writes a random number on a file.
 *
 * <p>
 * This application does not exploit the model-view-controller pattern, and as
 * such is just to be used to learn the basics, not as a template for your
 * applications.
 */
public class BadIOGUI {

    private static final String TITLE = "A very simple GUI application";
    private static final String PATH = System.getProperty("user.home")
        + File.separator
        + BadIOGUI.class.getSimpleName() + ".txt";
    private static final int PROPORTION = 5;
    private final Random randomGenerator = new Random();
    private final JFrame frame = new JFrame(TITLE);

    /**
     * Creates a new BadIOGUI.
     */
    public BadIOGUI() {
        final JPanel canvas = new JPanel();
        canvas.setLayout(new BorderLayout());
        final JButton write = new JButton("Write on file");
        canvas.add(write, BorderLayout.CENTER);
        frame.setContentPane(canvas);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        /*
         * Ex 1 new JPanel
         */
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        canvas.add(panel, BorderLayout.CENTER);
        panel.add(write);
        /*
         * Ex 2 new Button
         */
        final JButton read = new JButton("Read");
        panel.add(read);
        /*
         * Handlers
         */
        write.addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent ignored) {
                    /*
                     * This would be VERY BAD in a real application.
                     *
                     * This makes the Event Dispatch Thread (EDT) work on an I/O
                     * operation. I/O operations may take a long time, during which
                     * your UI becomes completely unresponsive.
                     */
                    try (PrintStream ps = new PrintStream(PATH, StandardCharsets.UTF_8)) {
                        ps.print(randomGenerator.nextInt());
                    } catch (final IOException e) {
                        JOptionPane.showMessageDialog(frame, e, "Error", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace(); // NOPMD: allowed as this is just an exercise
                    }
                }
            }
        );
        read.addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent read) {
                    try {
                        final Path path = Paths.get(PATH);
                        final List<String> fileContent = Files.readAllLines(path, StandardCharsets.UTF_8);
                        IO.println(fileContent);
                    } catch (final IOException e) {
                        JOptionPane.showMessageDialog(frame, e, "Error", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace(); // NOPMD: allowed as this is just an exercise
                    }
                    //IO.print("Hello, World!");
                }
            }
        );
    }

    private void display() {
        /*
         * Make the frame one fifth the resolution of the screen. This very method is
         * enough for a single screen setup. In case of multiple monitors, the
         * primary is selected. In order to deal coherently with multimonitor
         * setups, other facilities exist (see the Java documentation about this
         * issue). It is MUCH better than manually specify the size of a window
         * in pixel: it takes into account the current resolution.
         */
        final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        final int sw = (int) screen.getWidth();
        final int sh = (int) screen.getHeight();
        frame.setSize(sw / PROPORTION, sh / PROPORTION);
        frame.pack();
        /*
         * Instead of appearing at (0,0), upper left corner of the screen, this
         * flag makes the OS window manager take care of the default positioning
         * on screen. Results may vary, but it is generally the best choice.
         */
        frame.setLocationByPlatform(true);
        /*
         * OK, ready to push the frame onscreen
         */
        frame.setVisible(true);
    }

    /**
     * Launches the application.
     */
    public static void main() {
       new BadIOGUI().display();
    }
}
