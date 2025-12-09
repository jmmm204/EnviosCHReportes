package ni.edu.uam.EnviosCH.entities;

import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.Hidden;
import org.openxava.annotations.ListProperties;
import org.openxava.annotations.Required;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Getter @Setter
public class AgenteAduanal {

    @Id
    @Hidden
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    @Required
    private String nombre;

    @Column(length = 50)
    @Required
    private String especialidad;

    @OneToMany(mappedBy = "agenteAsignado", cascade = CascadeType.ALL)
    @ListProperties("descripcion, peso, almacen.nombre")
    private Collection<Paquete> paquetesACargo = new ArrayList<>();

    @Override
    public String toString() {
        return nombre;
    }
}