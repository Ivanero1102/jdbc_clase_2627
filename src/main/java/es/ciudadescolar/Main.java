package es.ciudadescolar;

import es.ciudadescolar.util.DbManager;

public class Main {
    public static void main(String[] args) {
        DbManager db = new DbManager();

        db.cerrarBD();
        
    }
}