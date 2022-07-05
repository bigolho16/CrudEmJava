/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.agenda.helper;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author loboMal
 */
public class FormataCamposHelper extends PlainDocument {

    @Override
    public void insertString(int arg0, String arg1, AttributeSet arg2) throws BadLocationException {
        super.insertString(arg0, arg1.replaceAll("[^0-9]", ""), arg2); //To change body of generated methods, choose Tools | Templates.
    }

    public void addPlaceholder(JTextField input, String texto) {
        
        /*
         * Por padrão define a cor do placeholder para cinza
         */
        input.setText(texto);
        input.setForeground(Color.gray);
        
        input.addFocusListener(new FocusListener () {
            /*
             * Quando voce dar foco
             */
            @Override
            public void focusGained(FocusEvent arg0) {
                if (input.getText().equals(texto)) {
                    input.setText("");
                    input.setForeground(Color.BLACK);
                }
            }
            /*
             * Quando voce tira o foco
             */
            @Override
            public void focusLost(FocusEvent arg0) {
                if (input.getText().isEmpty()) {
                    input.setText(texto);
                    input.setForeground(Color.GRAY);
                }
            }
        });
        
    }
    
    
    
}
