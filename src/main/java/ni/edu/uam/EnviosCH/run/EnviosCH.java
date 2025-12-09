package ni.edu.uam.EnviosCH.run;

import org.openxava.util.*;

/**
 * Ejecuta esta clase para arrancar la aplicación.
 *
 * Con OpenXava Studio/Ecslipse: Botón derecho del ratón > Run As > Java Application
 */

public class EnviosCH {

	public static void main(String[] args) throws Exception {
		DBServer.start("EnviosCH-db"); // Para usar tu propia base de datos comenta esta línea y configura src/main/webapp/META-INF/context.xml
		AppServer.run("EnviosCH"); // Usa AppServer.run("") para funcionar en el contexto raíz
	}

}