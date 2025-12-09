package ni.edu.uam.EnviosCH.entities;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter @Setter
public class PaqueteElectronico extends Paquete {

    @Column(length = 50)
    private String tipoDispositivo;
}