package com.gui;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.v251.message.ADT_A01;
import ca.uhn.hl7v2.model.v251.segment.MSH;
import ca.uhn.hl7v2.model.v251.segment.PID;
import ca.uhn.hl7v2.parser.Parser;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

import javax.swing.*;

public class ButtonPanel extends JFrame{

    private static final int HEIGHT = 500;
    private static final int WIDTH = 300;
    private JButton greenButton;
    private JFrame buttonPanel;
    private JTextField[] textFields = new JTextField[10];
    private JLabel[] labels = new JLabel[10];
    private String[] labelNames = new String[]{"Imie", "Nazwisko", "Płeć", "Pesel", "Numer konta", "Adres", "Miejsce narodzin", "Narodowość", "Stan cywilny", "Nazwa pliku do zapisu"};
    public ButtonPanel() {
        greenButton = new GreenButton();

        for(int i=0; i<labels.length; i++){
            labels[i] = new JLabel(labelNames[i]);
            this.add(labels[i]);
            textFields[i]=new JTextField();
            this.add(textFields[i]);
        }

        this.setSize(WIDTH, HEIGHT);
        buttonPanel = this;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GridLayout layout = new GridLayout(0, 1);
        setLayout(layout);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        add(greenButton);
        setVisible(true);

        greenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ADT_A01 adt = new ADT_A01();
                try {
                    adt.initQuickstart("ADT", "A01", "P");
                } catch (ca.uhn.hl7v2.HL7Exception | IOException exc) {
                    System.out.println(exc.toString());
                }

                MSH mshSegment = adt.getMSH();
                try {
                    mshSegment.getSendingApplication().getNamespaceID().setValue("PatientDelivery");
                    mshSegment.getSequenceNumber().setValue("293");
                } catch (ca.uhn.hl7v2.model.DataTypeException exc) {
                    System.out.println(exc.toString());
                }

                PID pid = adt.getPID();
                try {
                    pid.getPatientName(0).getGivenName().setValue(textFields[0].getText());
                    pid.getPatientName(0).getFamilyName().getSurname().setValue(textFields[1].getText());
                    pid.getPatientAddress(0).getAddressRepresentationCode().setValue(textFields[2].getText());
                    pid.getPatientIdentifierList(0).getIDNumber().setValue(textFields[3].getText());
                    pid.getPatientAccountNumber().getIDNumber().setValue(textFields[4].getText());
                    pid.getAdministrativeSex().setValue(textFields[5].getText());
                    pid.getBirthPlace().setValue(textFields[6].getText());
                    pid.getNationality().getAlternateIdentifier().setValue(textFields[7].getText());
                    pid.getMaritalStatus().getIdentifier().setValue(textFields[8].getText());
                } catch (ca.uhn.hl7v2.model.DataTypeException exc) {
                    System.out.println(exc.toString());
                }

                HapiContext context = new DefaultHapiContext();
                Parser parser = context.getPipeParser();
                String filename = "F:\\" + textFields[textFields.length - 1].getText();
                try {
                    String encodedMessage = parser.encode(adt);
                    final Path path = Paths.get(filename);
                    Files.write(path, Arrays.asList(encodedMessage), StandardCharsets.UTF_8,
                            Files.exists(path) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
                } catch (final IOException | ca.uhn.hl7v2.HL7Exception exc) {
                    System.out.println(exc.toString());
                }
            }
        });
    }
}