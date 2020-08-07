import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class MainClass {
    static JFrame jFrame;
    static JPanel jPanel;
    static JTextArea jTextArea;
    static ArrayList<JButton> buttonArrayList = new ArrayList();

    public static void main(String[] args) {

        makeFrame();
        addScreen();
        setButtons();
        paintFrame();

    }

    private static void makeFrame() {
        jFrame = new JFrame("Calculator");
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setLayout(null);
        jFrame.setLocationRelativeTo(null);
        jFrame.setSize(320, 420);
    }

    private static void addScreen() {
        jPanel = new JPanel();
        jPanel.setSize(290, 130);
        jPanel.setLocation(10, 10);
        jPanel.setBackground(Color.LIGHT_GRAY);

        jTextArea = new JTextArea();
        jTextArea.setSize(290, 130);
        jTextArea.setBackground(Color.LIGHT_GRAY);
    }

    private static void paintFrame() {

        for(JButton button: buttonArrayList){
            button.setSize(50, 50);
            jFrame.getContentPane().add(button);
        }
        jPanel.add(jTextArea);
        jFrame.getContentPane().add(jPanel);
        jFrame.repaint();
    }

    private static void setButtons() {

        char []symbolArray = {'1', '2', '3', 'C', 'E', '4', '5', '6', '-', 'X', '7', '8', '9', '+', '/', '.', '0', '=', 'H', '_'};
        int positionOfSymbolArray = 0;
        int positionX = 10;
        int positionY = 150;
        JButton jButton;
        for( int i = 0; i < 4; i++){
            for(int j = 0; j < 5; j++){
                jButton = new JButton(String.valueOf(symbolArray[positionOfSymbolArray]));
                jButton.setSize(50, 50);
                jButton.setLocation(positionX, positionY);
                jButton.setBorderPainted(true);
                buttonArrayList.add(jButton);
                positionOfSymbolArray += 1;
                positionX += 60;
            }
            positionX = 10;
            positionY += 60;
        }
        buttonArrayList.remove(buttonArrayList.size()-1);

        addButtonActionListener();

    }

    private static void addButtonActionListener() {
        String symbols = "1234567890.";
        String operations = "+-X/";
        AtomicReference<String> currentOperation = new AtomicReference<>();
        AtomicReference<Double> previous = new AtomicReference<>((double) 0);
        AtomicReference<Double> current = new AtomicReference<>((double) 0);
        JTextArea historyTextArea = new JTextArea();
        for(JButton jButton: buttonArrayList){
            jButton.addActionListener((e)-> {
                if (symbols.contains(jButton.getText())){
                    jTextArea.append(String.valueOf(jButton.getText()));
                } else if(operations.contains(String.valueOf(jButton.getText()))) {
                    previous.set(Double.valueOf(jTextArea.getText()));
//                    System.out.println(previous.toString());
                    jTextArea.setText(" ");
                    currentOperation.set(jButton.getText());
                }
                if(jButton.getText().equals(String.valueOf('C'))){
                    jTextArea.setText(" ");
                } else if(jButton.getText().equals("E")){
                    System.exit(0);
                } else if(jButton.getText().equals("=")){
                    current.set(Double.valueOf(jTextArea.getText()));
//                    System.out.println(current.toString());
                    double result;
                    try {
                        result = switch (currentOperation.get()) {
                            case "+" -> previous.get() + current.get();
                            case "-" -> previous.get() - current.get();
                            case "X" -> previous.get() * current.get();
                            case "/" -> previous.get() / current.get();
                            default -> throw new IllegalStateException("Unexpected value: " + jButton.getText());
                        };
                    } catch (Exception exception) {
                        result = current.get();
                    }
                    current.set(0.0);
                    previous.set(0.0);
//                    System.out.println(String.valueOf(result));
                    jTextArea.setText(String.valueOf(result));
                    historyTextArea.append(String.valueOf(result) + '\n');
                } else if(jButton.getText().equals("H")){
                    JFrame historyFrame = new JFrame("History");
                    historyFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
                    historyFrame.setSize(100, 130);
                    historyFrame.setLayout(null);
                    historyFrame.setLocationRelativeTo(jFrame);

                    JPanel historyPanel = new JPanel();
                    historyPanel.setSize(100, 130);

                    historyTextArea.setSize(100, 130);

                    historyPanel.add(historyTextArea);
                    historyFrame.getContentPane().add(historyPanel);
                    historyFrame.setVisible(true);   
                }

            });
        }
    }
}
