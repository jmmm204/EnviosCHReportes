package ni.edu.uam.EnviosCH.actions;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.openxava.actions.JasperReportBaseAction;
import org.openxava.jpa.XPersistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImprimirPaquetesAction extends JasperReportBaseAction {

    @Override
    protected String getJRXML() {
        return "EnviosCH.jrxml";
    }

    @Override
    protected Map getParameters() throws Exception {
        Map parameters = new HashMap();
        parameters.put("TITULO_REPORTE", "LISTADO GENERAL DE PAQUETERÍA");
        return parameters;
    }

    @Override
    protected JRDataSource getDataSource() throws Exception {
        List paquetes = XPersistence.getManager()
                .createQuery("from Paquete")
                .getResultList();

        return new JRBeanCollectionDataSource(paquetes);
    }
}