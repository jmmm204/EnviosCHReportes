package ni.edu.uam.EnviosCH.entities;

import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.Required;
import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Getter @Setter
public class PaqueteFragil extends Paquete {


    // Nivel de fragilidad basado en una escala Likert (1-5)
    @Min(value = 1, message = "El nivel minimo de fragilidad es 1")
    @Max(value = 5, message = "El nivel maximo de fragilidad es 5")
    @Required
    private int nivelFragilidad;

    public String getDescripcionFragilidad() {
        switch (nivelFragilidad) {
            case 1: return "1 - Resistencia Alta";
            case 2: return "2 - Resistencia Media";
            case 3: return "3 - Delicado";
            case 4: return "4 - Muy Fragil";
            case 5: return "5 - Extremadamente Fragil";
            default: return "No definido";
        }
    }
}