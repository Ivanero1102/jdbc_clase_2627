package es.ciudadescolar.modelo;

public class Entrenador {

    private Integer id;
    private String nom;
    private String nacionalidad;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getNacionalidad() {
        return nacionalidad;
    }
    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    @Override
    public String toString() {
        return "Entrenador [id=" + id + ", nom=" + nom + ", nacionalidad=" + nacionalidad + "]";
    }
    
    public Entrenador(Integer id, String nom, String nacionalidad) {
        this.id = id;
        this.nom = nom;
        this.nacionalidad = nacionalidad;
    }

    public Entrenador() {
        //TODO Auto-generated constructor stub
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        Entrenador other = (Entrenador) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    
}
