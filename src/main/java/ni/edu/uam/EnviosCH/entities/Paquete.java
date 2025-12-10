package ni.edu.uam.EnviosCH.entities;

import lombok.Getter;
import lombok.Setter;
import org.openxava.jpa.XPersistence;
import javax.validation.ValidationException;
import org.openxava.annotations.*;
import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter
public abstract class Paquete {

    @Id
    @Hidden
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    @Required
    private String descripcion;

    @Required
    @Min(value = 0, message = "El peso no puede ser negativo")
    private double peso;

    @Enumerated(EnumType.STRING)
    @Required
    private TipoEnvio tipoEnvio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "almacen_id")
    @DescriptionsList(descriptionProperties = "nombre")
    @Required
    private Almacen almacen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agente_id")
    @DescriptionsList(descriptionProperties = "nombre, especialidad")
    @Required
    private AgenteAduanal agenteAsignado;

    @Depends("peso, tipoEnvio")
    @Stereotype("MONEY")
    public BigDecimal getCostoEnvio() {
        // Si no hay peso, el costo es 0
        if (peso == 0) return BigDecimal.ZERO;

        double tarifaBase = 150.0;
        double multiplicador = 1.0;

        // Determinamos el multiplicador según el tipo de envío
        if (tipoEnvio != null) {
            switch (tipoEnvio) {
                case EXPRESS: multiplicador = 2.5; break;
                case ESTANDAR: multiplicador = 1.5; break;
                case ECONOMICO: multiplicador = 1.0; break;
            }
        }

        // Tarifa por libra: C$ 20.00
        double total = tarifaBase + (peso * 20 * multiplicador);
        return new BigDecimal(total);
    }

    @AssertTrue(message = "Los envíos Express no pueden pesar más de 100lb por regulación aduanera")
    private boolean isPesoValidoParaExpress() {
        if (this.tipoEnvio == TipoEnvio.EXPRESS && this.peso > 100) {
            return false;
        }
        return true;
    }

    @PrePersist
    @PreUpdate
    public void prepararGuardado() {
        // Estandarizamos descripciones a mayusculas
        if (this.descripcion != null) {
            this.descripcion = this.descripcion.toUpperCase();
        }

        // Control de inventario
        if (almacen == null || almacen.getId() == null) return;

        // Consulta JPQL para contar paquetes actuales en la bodega destino
        String jpql = "select count(p) from Paquete p where p.almacen.id = :id";
        Long cantidadActual = (Long) XPersistence.getManager()
                .createQuery(jpql)
                .setParameter("id", almacen.getId())
                .getSingleResult();

        boolean hayEspacio;

        if (this.getId() == null) {
            // La cantidad actual debe ser menor al limite
            hayEspacio = cantidadActual < almacen.getCapacidad();
        } else {
            // Se permite igualdad para no bloquear el propio paquete
            hayEspacio = cantidadActual <= almacen.getCapacidad();
        }

        if (!hayEspacio) {
            throw new javax.validation.ValidationException(
                    "¡ALMACÉN LLENO! La bodega " + almacen.getNombre() +
                            " ya tiene " + cantidadActual + " paquetes (Máx: " + almacen.getCapacidad() + ")."
            );
        }
    }
}