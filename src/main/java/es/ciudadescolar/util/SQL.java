package es.ciudadescolar.util;

public class SQL {
    //Todas las consultas SQL qie lancemos desde nuestra aplicacion deben indicar los campos a recuperar
    protected static final String SELECT_ENTRENADORES = "SELECT id_entrenador, nombre, nacionalidad FROM entrenadores";
    protected static final String SELECT_ENTRENADORES_POR_NOMBRE = "SELECT e.id_entrenador, e.nombre, e.nacionalidad FROM entrenadores e WHERE e.nombre = ?";
    protected static final String INSERT_ENTRENADOR = "INSERT INTO entrenadores(nombre, nacionalidad) VALUES (?,?)";
    protected static final String UPDATE_ENTRENADOR_NACIONALIDAD = "UPDATE entrenadores SET nacionalidad = ? WHERE id_entrenador = ?";
    protected static final String DELETE_ENTRENADOR = "DELETE FROM entrenadores WHERE id_entrenador = ?";
    
}
