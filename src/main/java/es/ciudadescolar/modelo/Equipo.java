package es.ciudadescolar.modelo;

public class Equipo {
    private int idEquipo;
    private String nomEquipo;
    private char grupoEquipo; 
    private String confEquipo;

    public int getIdEquipo() {
        return idEquipo;
    }
    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }
    public String getNomEquipo() {
        return nomEquipo;
    }
    public void setNomEquipo(String nomEquipo) {
        this.nomEquipo = nomEquipo;
    }
    public char getGrupoEquipo() {
        return grupoEquipo;
    }
    public void setGrupoEquipo(char grupoEquipo) {
        this.grupoEquipo = grupoEquipo;
    }
    public String getConfEquipo() {
        return confEquipo;
    }
    public void setConfEquipo(String confEquipo) {
        this.confEquipo = confEquipo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + idEquipo;
        result = prime * result + ((nomEquipo == null) ? 0 : nomEquipo.hashCode());
        result = prime * result + grupoEquipo;
        result = prime * result + ((confEquipo == null) ? 0 : confEquipo.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Equipo other = (Equipo) obj;
        if (idEquipo != other.idEquipo)
            return false;
        if (nomEquipo == null) {
            if (other.nomEquipo != null)
                return false;
        } else if (!nomEquipo.equals(other.nomEquipo))
            return false;
        if (grupoEquipo != other.grupoEquipo)
            return false;
        if (confEquipo == null) {
            if (other.confEquipo != null)
                return false;
        } else if (!confEquipo.equals(other.confEquipo))
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "Equipo [idEquipo=" + idEquipo + ", nomEquipo=" + nomEquipo + ", grupoEquipo=" + grupoEquipo
                + ", confEquipo=" + confEquipo + "]";
    }
    
}
