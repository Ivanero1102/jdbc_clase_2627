package es.ciudadescolar.util;

import java.sql.Statement;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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

    public List<Entrenador> getTodosEntrenadores(){
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

    public List<Entrenador> getTodosEntrenadores2(){
        List<Entrenador> entrenadores = null;
        Entrenador entrenador = null;

        if (conexion != null){

            /* 
            En este metodo hacemos lo mismo que en la opcion 1, pero usando try with resource
            que cierra automaticamente los recursos intanciados dentro del try()
            siemore que implementen la interfaz autoclosable()
             */
            try (Statement stEntrenadores = conexion.createStatement(); 
                ResultSet rstEntrenadores = stEntrenadores.executeQuery(SQL.SELECT_ENTRENADORES);){

                if(rstEntrenadores.next()){
                    entrenadores = new ArrayList<Entrenador>();
                    do{
                        entrenador = new Entrenador(rstEntrenadores.getInt(1), rstEntrenadores.getString(2), rstEntrenadores.getString(3));
                        entrenadores.add(entrenador);
                    }while(rstEntrenadores.next());
                }
            } catch (SQLException e) {
                LOG.error("Error recuperando los entrenadores");
            }
        }
        return entrenadores;
    }

    public List<Entrenador> getEntrenadoresPorNombre(String nombreEntrenador){
        List<Entrenador> entrenadoresPorNombre = null;
        Entrenador entrenador = null;
        PreparedStatement pstEntrenadoresNombre = null;
        ResultSet rstEnternadoresNombre = null;
        try {
            pstEntrenadoresNombre = conexion.prepareStatement(SQL.SELECT_ENTRENADORES_POR_NOMBRE);
            pstEntrenadoresNombre.setString(1, nombreEntrenador);
            rstEnternadoresNombre = pstEntrenadoresNombre.executeQuery();
            if(rstEnternadoresNombre.next()){
                entrenadoresPorNombre = new ArrayList<Entrenador>();
                do{
                    entrenador = new Entrenador(rstEnternadoresNombre.getInt(1), rstEnternadoresNombre.getString(2), rstEnternadoresNombre.getString(3));
                    entrenadoresPorNombre.add(entrenador);
                }while(rstEnternadoresNombre.next());
            }else{
                LOG.warn("No se han encontrado entrenadores con nombre [" +nombreEntrenador+ "]");
            }
        } catch (SQLException e) {
            LOG.error("Error durante la recuperacion de entrenadores por nombre [" +nombreEntrenador+"]" + e.getMessage());
        } finally {
            if(conexion != null && rstEnternadoresNombre != null && pstEntrenadoresNombre != null){
                try {
                    rstEnternadoresNombre.close();
                    pstEntrenadoresNombre.close();
                } catch (SQLException e) {
                    LOG.error("Error durante la liberaciond de recursos durante la consulta de entrenadores por nombre");
                }
            }
        }
        return entrenadoresPorNombre;
    }

    public boolean altaNuevoEntrenador(String nombre, String nacionalidad){
        boolean resultado = false;
        int numRegistrosNuevos = -1;
        try(PreparedStatement pstNuevoEntrenador = conexion.prepareStatement(SQL.INSERT_ENTRENADOR);){
            pstNuevoEntrenador.setString(1, nombre);
            pstNuevoEntrenador.setString(2, nacionalidad);
            numRegistrosNuevos = pstNuevoEntrenador.executeUpdate();

            if(numRegistrosNuevos == 1){
                LOG.debug("La inserción se ha realziado correctamente");
                resultado = true;
            }else{
                LOG.warn("La insercion no se ha llevado a cabo del entrenador ["+nombre+"] ["+nacionalidad+"]");
            }
        } catch (Exception e) {
            LOG.error("Error durante la insercion de un nuevo entrenador ["+nombre+"] ["+nacionalidad+"]"+e.getMessage());
        }

        return resultado;
    }

    public boolean cambiarNacionalidadEntrenador(int id, String nacionalidad){
        boolean resultado = false;
        int numRegistrosNuevos = -1;
        try(PreparedStatement pstActualizarEntrenador = conexion.prepareStatement(SQL.UPDATE_ENTRENADOR_NACIONALIDAD);){
            pstActualizarEntrenador.setString(1, nacionalidad);
            pstActualizarEntrenador.setInt(2, id);
            numRegistrosNuevos = pstActualizarEntrenador.executeUpdate();

            if(numRegistrosNuevos == 1){
                LOG.debug("La actualizacion se ha realziado correctamente");
                resultado = true;
            }else{
                LOG.warn("La actualizacion no se ha llevado a cabo del entrenador ["+id+"] ["+nacionalidad+"]");
            }
        } catch (Exception e) {
            LOG.error("Error durante la actualizacion de un nuevo entrenador ["+id+"] ["+nacionalidad+"]"+e.getMessage());
        }

        return resultado;
    }

    public boolean borrarEntrenador(int id){
    boolean resultado = false;
    int numRegistrosNuevos = -1;
        try(PreparedStatement pstBorrarEntrenador = conexion.prepareStatement(SQL.DELETE_ENTRENADOR);){
            pstBorrarEntrenador.setInt(1, id);
            numRegistrosNuevos = pstBorrarEntrenador.executeUpdate();

            if(numRegistrosNuevos == 1){
                LOG.debug("El borrado se ha realziado correctamente");
                resultado = true;
            }else{
                LOG.warn("El borrado no se ha llevado a cabo del entrenador ["+id+"]");
            }
        } catch (Exception e) {
            LOG.error("Error durante el borrado de un entrenador ["+id+"]"+e.getMessage());
        }

        return resultado;
    }

    public boolean generaReportePartidos(int id_equipo){
        boolean resultado = false;
        try (CallableStatement cstReporte = conexion.prepareCall(SQL.SP_CALENDARIO_EQUIPO);){
            cstReporte.setInt(1,id_equipo);
            cstReporte.execute();
            LOG.debug("Se ha invocado correctamente al procedimiento almacenado para general el reporte de partido");
            resultado = true;
        } catch (Exception e) {
            LOG.error("Error durante la generacion del reporte de partidos para el equipo [" +id_equipo+ "]" +e.getMessage());
        }
        return resultado;
    }

    public String getResultadoPartido(int id_partido, String resultado){
        try (CallableStatement cstResultado = conexion.prepareCall(SQL.FN_RESULTADO_PARTIDO)){
            cstResultado.setString(1, resultado);
            cstResultado.setInt(2, id_partido);
            cstResultado.execute();
        } catch (Exception e) {
            
        }
        return resultado;
    }
}
