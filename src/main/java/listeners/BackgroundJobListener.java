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
import entities.Usuario;

import java.util.LinkedList;
import entities.Investigador;
import logic.LogicUsuario;

@WebListener
public class BackgroundJobListener implements ServletContextListener {

    private ScheduledExecutorService scheduler;
    private LogicRegistro logicRegistro = new LogicRegistro();
    private LogicEmail logicEmail = new LogicEmail();
    private LogicUsuario logicUsuario = new LogicUsuario();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        
        // Configuramos la hora de ejecución (Ejemplo: 23:00)
        ZonedDateTime ahora = ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));
        ZonedDateTime proximaEjecucion = ahora.withHour(23).withMinute(0).withSecond(0);

        // Si ya pasaron las 23:00 hoy, programamos para mañana
        if (ahora.isAfter(proximaEjecucion)) {
            proximaEjecucion = proximaEjecucion.plusDays(1);
        }

        long delayInicial = Duration.between(ahora, proximaEjecucion).toSeconds();

        // Ejecutar cada 24 horas (en segundos)
        scheduler.scheduleAtFixedRate(() -> {
            try {
                System.out.println("⏳ Iniciando envío de resumen diario...");
                enviarResumenAdministradores();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } ,0, 20, TimeUnit.SECONDS); //, delayInicial, TimeUnit.DAYS.toSeconds(1), TimeUnit.SECONDS);
    }

    
    private void enviarResumenAdministradores() {
        LinkedList<Registro> registrosAprobadosHoy = logicRegistro.findRegistrosAprobadosHoy();
        
        if (registrosAprobadosHoy == null || registrosAprobadosHoy.isEmpty()) {
            System.out.println("ℹ️ No hubo registros aprobados hoy. Saltando envío.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        
        sb.append("Registros Aprobados:\n\n");
        
        for (Registro registro : registrosAprobadosHoy) {
            sb.append("- ").append(registro.getBestia().getNombre())
              .append(": ").append(registro.getContenido().getIntroduccion())
              .append("\n");
        }
        
        String mensajeCompleto = sb.toString();

        LinkedList<Investigador> investigadores = logicUsuario.getCorreosInvestigadoresYRecibNot();
        
        // ya estamos en un hilo del scheduler
        for (Investigador investigador : investigadores) {
            try {
            	if(investigador.getRecibirNotificaciones() == true) {
            		logicEmail.notificarRegistrosAprobadosHoy(investigador.getCorreo(), mensajeCompleto);
            	}
            } catch (Exception e) {
                System.out.println("❌ Falló envío a: " + investigador.getCorreo());
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