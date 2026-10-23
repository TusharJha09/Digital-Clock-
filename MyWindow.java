```java
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MyWindow extends JFrame {

    // Colors
    private final Color backgroundColor = new Color(18, 24, 38);
    private final Color cardColor = new Color(29, 38, 58);
    private final Color accentColor = new Color(0, 210, 190);
    private final Color textColor = new Color(235, 240, 250);
    private final Color secondaryColor = new Color(155, 170, 195);

    // Components
    private JLabel headingLabel;
    private JLabel clockLabel;
    private JLabel dateLabel;
    private JLabel dayLabel;
    private JLabel statusLabel;

    private JButton startButton;
    private JButton stopButton;
    private JButton formatButton;

    private Timer timer;

    private boolean is24HourFormat = true;
    private boolean isClockRunning = true;

    public MyWindow() {

        setTitle("Chrono - Digital Clock");
        setSize(620, 440);
        setMinimumSize(new Dimension(500, 400));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        createGUI();
        updateClock();

        // Update the clock every second.
        timer = new Timer(1000, e -> updateClock());
        timer.start();

        // Stop the timer when the window closes.
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (timer != null) {
                    timer.stop();
                }
            }
        });
    }

    private void createGUI() {

        JPanel mainPanel = new JPanel(new BorderLayout(15, 20));
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(25, 30, 25, 30)
        );

        // ---------------- HEADER ----------------

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        headingLabel = new JLabel("CHRONO");
        headingLabel.setFont(new Font("SansSerif", Font.BOLD, 25));
        headingLabel.setForeground(accentColor);
        headingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("YOUR PERSONAL DIGITAL CLOCK");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        subtitleLabel.setForeground(secondaryColor);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(headingLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subtitleLabel);

        // ---------------- CLOCK CARD ----------------

        JPanel clockPanel = new JPanel();
        clockPanel.setLayout(new BoxLayout(clockPanel, BoxLayout.Y_AXIS));
        clockPanel.setBackground(cardColor);
        clockPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(52, 68, 94), 1
                        ),
                        BorderFactory.createEmptyBorder(25, 10, 25, 10)
                )
        );

        dayLabel = new JLabel();
        dayLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        dayLabel.setForeground(textColor);
        dayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        clockLabel = new JLabel("00:00:00");
        clockLabel.setFont(new Font("Monospaced", Font.BOLD, 48));
        clockLabel.setForeground(accentColor);
        clockLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        dateLabel = new JLabel();
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        dateLabel.setForeground(secondaryColor);
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        clockPanel.add(dayLabel);
        clockPanel.add(Box.createVerticalStrut(15));
        clockPanel.add(clockLabel);
        clockPanel.add(Box.createVerticalStrut(12));
        clockPanel.add(dateLabel);

        // ---------------- CONTROLS ----------------

        JPanel controlsPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 12, 5)
        );
        controlsPanel.setOpaque(false);

        startButton = createButton("START", accentColor, backgroundColor);
        stopButton = createButton(
                "STOP", new Color(255, 105, 120), backgroundColor
        );
        formatButton = createButton(
                "12-HOUR MODE", new Color(90, 120, 255), Color.WHITE
        );

        startButton.addActionListener(e -> startClock());
        stopButton.addActionListener(e -> stopClock());
        formatButton.addActionListener(e -> toggleTimeFormat());

        controlsPanel.add(startButton);
        controlsPanel.add(stopButton);
        controlsPanel.add(formatButton);

        // ---------------- STATUS ----------------

        statusLabel = new JLabel("● CLOCK RUNNING");
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        statusLabel.setForeground(accentColor);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(controlsPanel, BorderLayout.CENTER);
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);

        // ---------------- MAIN LAYOUT ----------------

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(clockPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        // Set initial button states.
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
    }

    private JButton createButton(
            String text, Color background, Color foreground) {

        JButton button = new JButton(text);

        button.setFont(new Font("SansSerif", Font.BOLD, 11));
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(135, 38));

        return button;
    }

    // Update time, day, and date.
    private void updateClock() {

        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter timeFormatter;

        if (is24HourFormat) {
            timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        } else {
            timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        }

        DateTimeFormatter dayFormatter =
                DateTimeFormatter.ofPattern("EEEE");

        DateTimeFormatter dateFormatter =
                DateTimeFormatter.ofPattern("dd MMMM yyyy");

        clockLabel.setText(now.format(timeFormatter));
        dayLabel.setText(now.format(dayFormatter));
        dateLabel.setText(now.format(dateFormatter));
    }

    // Start the clock.
    private void startClock() {

        if (timer != null && !timer.isRunning()) {
            timer.start();
        }

        isClockRunning = true;

        startButton.setEnabled(false);
        stopButton.setEnabled(true);

        statusLabel.setText("● CLOCK RUNNING");
        statusLabel.setForeground(accentColor);

        updateClock();
    }

    // Stop the clock.
    private void stopClock() {

        if (timer != null) {
            timer.stop();
        }

        isClockRunning = false;

        startButton.setEnabled(true);
        stopButton.setEnabled(false);

        statusLabel.setText("● CLOCK PAUSED");
        statusLabel.setForeground(new Color(255, 105, 120));
    }

    // Switch between 12-hour and 24-hour formats.
    private void toggleTimeFormat() {

        is24HourFormat = !is24HourFormat;

        if (is24HourFormat) {
            formatButton.setText("12-HOUR MODE");
        } else {
            formatButton.setText("24-HOUR MODE");
        }

        updateClock();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            MyWindow window = new MyWindow();
            window.setVisible(true);
        });
    }
}
```
