/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.antoniodominguez.editorialmangas.entities;

import es.antoniodominguez.editorialmangas.entities.Editorial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author anton
 */
@Entity
@Table(name = "MANGA")
@NamedQueries({
    @NamedQuery(name = "Manga.findAll", query = "SELECT m FROM Manga m"),
    @NamedQuery(name = "Manga.findById", query = "SELECT m FROM Manga m WHERE m.id = :id"),
    @NamedQuery(name = "Manga.findByNombre", query = "SELECT m FROM Manga m WHERE m.nombre = :nombre"),
    @NamedQuery(name = "Manga.findByAutor", query = "SELECT m FROM Manga m WHERE m.autor = :autor"),
    @NamedQuery(name = "Manga.findByIsbn", query = "SELECT m FROM Manga m WHERE m.isbn = :isbn"),
    @NamedQuery(name = "Manga.findByEmailEditorial", query = "SELECT m FROM Manga m WHERE m.emailEditorial = :emailEditorial"),
    @NamedQuery(name = "Manga.findByFechaPublicaion", query = "SELECT m FROM Manga m WHERE m.fechaPublicaion = :fechaPublicaion"),
    @NamedQuery(name = "Manga.findByCapitulos", query = "SELECT m FROM Manga m WHERE m.capitulos = :capitulos"),
    @NamedQuery(name = "Manga.findByVolumen", query = "SELECT m FROM Manga m WHERE m.volumen = :volumen"),
    @NamedQuery(name = "Manga.findByNumpaginas", query = "SELECT m FROM Manga m WHERE m.numpaginas = :numpaginas"),
    @NamedQuery(name = "Manga.findByPrecio", query = "SELECT m FROM Manga m WHERE m.precio = :precio"),
    @NamedQuery(name = "Manga.findByIdioma", query = "SELECT m FROM Manga m WHERE m.idioma = :idioma"),
    @NamedQuery(name = "Manga.findByRestriccionEdad", query = "SELECT m FROM Manga m WHERE m.restriccionEdad = :restriccionEdad"),
    @NamedQuery(name = "Manga.findByEstado", query = "SELECT m FROM Manga m WHERE m.estado = :estado"),
    @NamedQuery(name = "Manga.findByLogo", query = "SELECT m FROM Manga m WHERE m.logo = :logo")})
public class Manga implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "AUTOR")
    private String autor;
    @Basic(optional = false)
    @Column(name = "ISBN")
    private String isbn;
    @Column(name = "EMAIL_EDITORIAL")
    private String emailEditorial;
    @Column(name = "FECHA_PUBLICAION")
    @Temporal(TemporalType.DATE)
    private Date fechaPublicaion;
    @Column(name = "CAPITULOS")
    private Short capitulos;
    @Column(name = "VOLUMEN")
    private Short volumen;
    @Column(name = "NUMPAGINAS")
    private Short numpaginas;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PRECIO")
    private BigDecimal precio;
    @Column(name = "IDIOMA")
    private String idioma;
    @Column(name = "RESTRICCION_EDAD")
    private Boolean restriccionEdad;
    @Column(name = "ESTADO")
    private String estado;
    @Column(name = "LOGO")
    private String logo;
    @JoinColumn(name = "EDITORIAL", referencedColumnName = "ID")
    @ManyToOne
    private Editorial editorial;

    public Manga() {
    }

    public Manga(Integer id) {
        this.id = id;
    }

    public Manga(Integer id, String nombre, String autor, String isbn) {
        this.id = id;
        this.nombre = nombre;
        this.autor = autor;
        this.isbn = isbn;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getEmailEditorial() {
        return emailEditorial;
    }

    public void setEmailEditorial(String emailEditorial) {
        this.emailEditorial = emailEditorial;
    }

    public Date getFechaPublicaion() {
        return fechaPublicaion;
    }

    public void setFechaPublicaion(Date fechaPublicaion) {
        this.fechaPublicaion = fechaPublicaion;
    }

    public Short getCapitulos() {
        return capitulos;
    }

    public void setCapitulos(Short capitulos) {
        this.capitulos = capitulos;
    }

    public Short getVolumen() {
        return volumen;
    }

    public void setVolumen(Short volumen) {
        this.volumen = volumen;
    }

    public Short getNumpaginas() {
        return numpaginas;
    }

    public void setNumpaginas(Short numpaginas) {
        this.numpaginas = numpaginas;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Boolean getRestriccionEdad() {
        return restriccionEdad;
    }

    public void setRestriccionEdad(Boolean restriccionEdad) {
        this.restriccionEdad = restriccionEdad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Editorial getEditorial() {
        return editorial;
    }

    public void setEditorial(Editorial editorial) {
        this.editorial = editorial;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Manga)) {
            return false;
        }
        Manga other = (Manga) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "es.antoniodominguez.editorialmangas.entities.Manga[ id=" + id + " ]";
    }
    
}
