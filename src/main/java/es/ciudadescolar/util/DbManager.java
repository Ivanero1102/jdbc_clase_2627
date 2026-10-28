package es.ciudadescolar.util;

import java.sql.Statement;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ciudadescolar.modelo.Entrenador;

public class DbManager {

    private static final String FICH_PROPERTIES = "conexion_mundial2026.properties";

    private static final String DRIVER = "driver";
    private static final String URL = "url";
    private static final String USER = "user";
    private static final String PWD = "password";
    private static final Logger LOG = LoggerFactory.getLogger(DbManager.class);

    private Connection conexion = null;

    public DbManager()
    {
        //recuperamos los parametros de conexion de la BD desde el properties
        Properties propiedades = new Properties();
        
        try {
            propiedades.load(new FileInputStream(FICH_PROPERTIES));
            LOG.debug("propiedad["+DRIVER+"]="+propiedades.getProperty(DRIVER));
            LOG.debug("propiedad["+URL+"]="+propiedades.getProperty(URL));
            LOG.debug("propiedad["+USER+"]="+propiedades.getProperty(USER));
            LOG.debug("propiedad["+PWD+"]="+propiedades.getProperty(PWD).length());

            //Registramos el driver
            Class.forName(propiedades.getProperty(DRIVER));

            //Establecer la conexion
            conexion = DriverManager.getConnection(propiedades.getProperty(URL), propiedades.getProperty(USER), propiedades.getProperty(PWD));
            LOG.debug("Se ha establecido correctamente la conexion conta la BD");

        } catch (FileNotFoundException e) {
            LOG.error("Error durante la configuracion de la base de BD: "+e.getMessage());
        } catch (IOException e) {
            LOG.error("Error durante la configuracion de la base de BD: "+e.getMessage());
        } catch (ClassNotFoundException e) {
            LOG.error("Error durante el registro del driver ["+propiedades.getProperty(DRIVER)+"]");
        } catch (SQLException e) {
            LOG.error("Error estableciendo la conexion con la BD: "+e.getMessage());
        }
    }

    public void cerrarBD(){
        if(conexion != null){
            try {
                conexion.close();
                LOG.debug("se ha cerrada correctamente la conexion con la BD");
            } catch (SQLException e) {
                LOG.error("Error cerrando la BD: "+e.getMessage());
            }
        }
    }

    private List<Entrenador> getTodosEntrenadores(){
        List<Entrenador> entrenadores = null;
        Statement stEntrenadores = null;
        ResultSet rstEntrenadores = null;
        Entrenador entrenador = null;

        if (conexion != null){
            try {
                stEntrenadores = conexion.createStatement();
                rstEntrenadores = stEntrenadores.executeQuery(SQL.SELECT_ENTRENADORES);

                if(rstEntrenadores.next()){
                    entrenadores = new ArrayList<Entrenador>();
                    do{
                        entrenador = new Entrenador(rstEntrenadores.getInt(1), rstEntrenadores.getString(2), rstEntrenadores.getString(3));
                        entrenadores.add(entrenador);
                    }while(rstEntrenadores.next());
                }
            } catch (SQLException e) {
                LOG.error("Error recuperando los entrenadores");
            }finally{
                if(rstEntrenadores != null && stEntrenadores != null){
                    try {
                        rstEntrenadores.close();
                        stEntrenadores.close();
                    } catch (Exception e) {
                        LOG.error("Error cerrando resultset y/o statement");
                    }
                }
            }
        }
        return entrenadores;
    }
}
