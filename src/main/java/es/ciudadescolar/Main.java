package es.ciudadescolar;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ciudadescolar.modelo.Entrenador;
import es.ciudadescolar.util.DbManager;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {

        DbManager db = new DbManager();

        List<Entrenador> entrenadores = db.getTodosEntrenadores();
        LOG.info("Listado de entrenadores recuperados de la version 1");
        for (Entrenador ent:entrenadores){
            LOG.info(ent.toString());
        }
        
        List<Entrenador> entrenadores2 = db.getTodosEntrenadores2();
        LOG.info("Listado de entrenadores recuperados de la version 2");
        for (Entrenador ent:entrenadores2){
            LOG.info(ent.toString());
        }

        String nombre = "Thomas Christiansen";
        List<Entrenador> entrenadoresNombre = db.getEntrenadoresPorNombre(nombre);
        LOG.info("Listado de entrenadores recuperados cuyo nombre sea Thomas Christiansen");
        for (Entrenador ent:entrenadoresNombre){
            LOG.info(ent.toString());
        }

        if(db.altaNuevoEntrenador("Manolo Garcia", "Español")){
            LOG.info("La inserción se ha realziado correctamente de Manolo Garcia con nacionalidad Español");
        }else{
            LOG.warn("No se ha insertado el entrenador Manolo Garcia con nacionaliad Español");
        }

        if(db.cambiarNacionalidadEntrenador(63, "Ingles")){
            LOG.info("La actualizacion se ha realziado correctamente del entrenador con id 63 con nacionalidad Ingles");
        }else{
            LOG.warn("No se ha actualizado el entrenador con id 63 con nacionaliad Ingles");
        }

        if(db.borrarEntrenador(64)){
            LOG.info("El borrado se ha realziado correctamente del entrenador con id 64");
        }else{
            LOG.warn("No se ha borrado el entrenador con id 64");
        }

        if(db.generaReportePartidos(1)){
            LOG.info("Se ha generado correctamente el reporte del equipo 1");
        }else{
            LOG.warn("No se ha generado el reporte");
        }

        String resultado=null;
        db.getResultadoPartido(1, resultado);

        db.cerrarBD();
    }
}