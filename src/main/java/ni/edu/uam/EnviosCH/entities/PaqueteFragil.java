package ni.edu.uam.EnviosCH.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter @Setter
public class PaqueteFragil extends Paquete {

    private int nivelFragilidad;
}