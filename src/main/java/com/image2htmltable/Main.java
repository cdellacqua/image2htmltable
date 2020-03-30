package com.image2htmltable;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;


public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            System.out.println("HTML file saved (" + convert(new File(args[0])) + ")");
        } else {
            final JFrame frame = new JFrame();
            frame.setSize(1000, 100);
            final FlowLayout layout = new FlowLayout();
            layout.setAlignment(FlowLayout.CENTER);
            frame.setLayout(layout);
            final JLabel label = new JLabel("Asking for an image file");
            frame.add(label);
            frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
            final JButton openFileButton = new JButton();
            openFileButton.setText("Open generated file");
            openFileButton.setEnabled(false);
            frame.add(openFileButton);
            frame.setVisible(true);

            final JFileChooser chooser = new JFileChooser();
            chooser.setDialogType(JFileChooser.OPEN_DIALOG);
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setMultiSelectionEnabled(false);
            chooser.setDialogTitle("Pick an image (png or jpg)...");
            chooser.setFileFilter(new FileFilter() {
                private final String[] VALID_EXTS = new String[] { ".jpg", ".jpeg", ".png" };

                @Override
                public boolean accept(File file) {
                    if (file.isDirectory()) {
                        return true;
                    }
                    for (String extension: VALID_EXTS) {
                        if (file.getName().endsWith(extension)) {
                            return true;
                        }
                    }
                    return false;
                }

                @Override
                public String getDescription() {
                    return null;
                }
            });
                        
            if (chooser.showDialog(chooser, "Convert") == JFileChooser.APPROVE_OPTION) {
                label.setText("Generating HTML...");
                final String outputFilePath = convert(chooser.getSelectedFile());
                openFileButton.setEnabled(true);
                openFileButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent l) {
                        try {
                            Desktop.getDesktop().open(new File(outputFilePath));
                        } catch (IOException ex) {
                            openFileButton.setEnabled(false);
                            label.setText(label.getText() + "\r\n Unable to open file");
                        }
                    }
                });

                label.setText("HTML file saved (" + outputFilePath + ")");
            } else {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        }
    }
    
    private static String convert(File file) throws IOException {
        BufferedImage img = ImageIO.read(file);
        String outputFilePath = file.getAbsolutePath()+ ".html";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {

            writer.write(
                    "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n" +
                    "    <meta charset=\"utf-8\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                    "    <style>\n" +
                    "        html, body {\n" +
                    "            margin:0;\n" +
                    "            padding:0\n" +
                    "        }\n" +
                    "        .img-html {\n" +
                    "            padding:0;\n" +
                    "            margin:0;\n" +
                    "            border-collapse: collapse;\n" +
                    "            table-layout:fixed;\n" +
                    "            border:0;\n" +
                    "            width: " + img.getWidth() + "px;\n" +
                    "            height: " + img.getHeight() + "px;\n" +
                    "        }\n" +
                    "        .img-html tr {\n" +
                    "            padding:0;\n" +
                    "            margin:0;\n" +
                    "            height: 1px;\n" +
                    "        }\n" +
                    "        .img-html td {\n" +
                    "            height: 1px;\n" +
                    "            width: 1px;\n" +
                    "            padding:0;\n" +
                    "            margin:0;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body>\n");


            writer.write("<table class=\"img-html\">");
            for (int y = 0; y < img.getHeight(); y++) {
                writer.write("<tr>");
                for (int x = 0; x < img.getWidth(); x++) {
                    Color color = new Color(img.getRGB(x, y));
                    writer.write("<td bgcolor=\"" +
                        String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue()) +
                        "\"></td>"
                    );
                }
                writer.write("</tr>");
            }
            writer.write("</table>");


            writer.write(
                "</body>\n" +
                "</html>\n"
            );
        }

        return outputFilePath;
    }

}
