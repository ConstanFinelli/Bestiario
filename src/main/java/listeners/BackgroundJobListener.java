package listeners;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.time.ZonedDateTime;
import java.time.Duration;
import java.time.ZoneId;
import logic.LogicEmail;
import logic.LogicRegistro; // O la lógica de tus bestias
import entities.Registro;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import entities.Investigador;
import logic.LogicUsuario;

@WebListener
public class BackgroundJobListener implements ServletContextListener {
	private static final Logger logger = Logger.getLogger(BackgroundJobListener.class.getName());
	private static final boolean MODO_PRUEBA = false;
	
    private ScheduledExecutorService scheduler;
    private LogicRegistro logicRegistro = new LogicRegistro();
    private LogicEmail logicEmail = new LogicEmail();
    private LogicUsuario logicUsuario = new LogicUsuario();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        
        if (MODO_PRUEBA) {

            scheduler.scheduleAtFixedRate(
                () -> {
                    try {
                        logger.log(Level.INFO, "Iniciando envío programado de resumen diario (modo prueba).");
                        enviarResumenAdministradores();
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "Error en ejecución programada de prueba del resumen diario", e);
                    }
                },
                5,
                80,
                TimeUnit.SECONDS
            );

        } else {

            ZonedDateTime ahora =
                    ZonedDateTime.now(
                        ZoneId.of("America/Argentina/Buenos_Aires")
                    );

            ZonedDateTime proximaEjecucion =
                    ahora.withHour(23)
                         .withMinute(0)
                         .withSecond(0)
                         .withNano(0);

            if (ahora.isAfter(proximaEjecucion)) {
                proximaEjecucion = proximaEjecucion.plusDays(1);
            }

            long delayInicial =
                    Duration.between(ahora, proximaEjecucion).toSeconds();

            scheduler.scheduleAtFixedRate(
                () -> {
                    try {
                        enviarResumenAdministradores();
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "Error en ejecución programada del resumen diario", e);
                    }
                },
                delayInicial,
                TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS
            );
        }
    }

    
    private void enviarResumenAdministradores() {
        LinkedList<Registro> registrosAprobadosHoy = logicRegistro.findRegistrosAprobadosHoy();
        
        if (registrosAprobadosHoy == null || registrosAprobadosHoy.isEmpty()) {
            return;
        }

        LinkedList<Investigador> investigadores = logicUsuario.getCorreosInvestigadoresYRecibNot();
        
        // ya estamos en un hilo del scheduler
        for (Investigador investigador : investigadores) {
            try {
            	if(investigador.getRecibirNotificaciones() == true) {
            		logicEmail.notificarRegistrosAprobadosHoy(investigador.getCorreo(), registrosAprobadosHoy);
            	}
            } catch (Exception e) {
                logger.log(Level.WARNING, "Falló envío de resumen a: " + investigador.getCorreo(), e);
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
    }
}