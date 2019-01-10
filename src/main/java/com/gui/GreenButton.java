package com.gui;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.v251.message.ADT_A01;
import ca.uhn.hl7v2.model.v251.segment.MSH;
import ca.uhn.hl7v2.model.v251.segment.PID;
import ca.uhn.hl7v2.parser.Parser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

class GreenButton extends JButton implements ActionListener {

    GreenButton() {
        super("Do pliku");
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        ADT_A01 adt = new ADT_A01();
        try {
            adt.initQuickstart("ADT", "A01", "P");
        } catch (ca.uhn.hl7v2.HL7Exception | IOException exc) {
        }

        MSH mshSegment = adt.getMSH();
        try {
            mshSegment.getSendingApplication().getNamespaceID().setValue("PatientDelivery");
            mshSegment.getSequenceNumber().setValue("293");
        } catch (ca.uhn.hl7v2.model.DataTypeException exc) {
        }

        PID pid = adt.getPID();
        try {
            pid.getPatientName(0).getGivenName().setValue(ButtonPanel.textFields[0].getText());
            pid.getPatientName(0).getFamilyName().getSurname().setValue(ButtonPanel.textFields[1].getText());
            pid.getPatientAddress(0).getAddressRepresentationCode().setValue(ButtonPanel.textFields[2].getText());
            pid.getPatientIdentifierList(0).getIDNumber().setValue(ButtonPanel.textFields[3].getText());
            pid.getPatientAccountNumber().getIDNumber().setValue(ButtonPanel.textFields[4].getText());
        } catch (ca.uhn.hl7v2.model.DataTypeException exc) {
        }

        HapiContext context = new DefaultHapiContext();
        Parser parser = context.getPipeParser();
        String filename = "F:\\" + ButtonPanel.textFields[ButtonPanel.textFields.length - 1].getText();
        try {
            String encodedMessage = parser.encode(adt);
            final Path path = Paths.get(filename);
            Files.write(path, Arrays.asList(encodedMessage), StandardCharsets.UTF_8,
                    Files.exists(path) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
        } catch (final IOException | ca.uhn.hl7v2.HL7Exception exc) {
        }
    }
}
