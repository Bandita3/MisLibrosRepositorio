package com.mycompany.mislibros;

import com.mycompany.mislibros.igu.Principal;

public class MisLibros {
    public static void main(String[] args) {
        /*hacemos visible la ventana principal, creando una instancia de nuestra
        ventana*/
        Principal princ = new Principal();  
        princ.setVisible(true);
        princ.setLocationRelativeTo(null);
    }
}
