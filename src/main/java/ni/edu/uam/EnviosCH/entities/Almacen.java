package ni.edu.uam.EnviosCH.entities;

import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Getter @Setter
public class Almacen {
    @Id
    @Hidden
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, unique = true)
    @Required
    private String nombre;

    @Column(length = 50)
    private String ubicacion;

    @Required
    @Min(1)
    private int capacidad;

    @OneToMany(mappedBy = "almacen", cascade = CascadeType.ALL)
    @ListProperties("descripcion, peso, tipoEnvio, costoEnvio")
    private Collection<Paquete> paquetes = new ArrayList<>();

    @Depends("capacidad, paquetes")
    public String getNivelOcupacion() {
        int cantidadActual = (paquetes != null) ? paquetes.size() : 0;

        if (capacidad == 0) return "0%";

        double porcentaje = ((double) cantidadActual / capacidad) * 100;

        return String.format("%.2f%% (%d/%d)", porcentaje, cantidadActual, capacidad);
    }

    @AssertTrue(message = "El almacén ha excedido su capacidad máxima")
    private boolean isCapacidadRespetada() {
        int cantidadActual = (paquetes != null) ? paquetes.size() : 0;
        return cantidadActual <= capacidad;
    }

    @Override
    public String toString() {
        return nombre;
    }
}