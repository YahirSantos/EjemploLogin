package mx.edu.fesaragon.login;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class EjemploLoginApplication {

    private static final String URL = "http://localhost:8080/";

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        SpringApplication.run(EjemploLoginApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void abrirNavegador() {
        try {
            if (Desktop.isDesktopSupported()
                    && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(URL));
            } else {
                abrirConComandoDelSistema();
            }
        } catch (Exception e) {
            System.err.println("No se pudo abrir el navegador: " + e.getMessage());
        }
    }

    // Alternativa para Linux
    private void abrirConComandoDelSistema() throws IOException {
        String os = System.getProperty("os.name").toLowerCase();
        Runtime runtime = Runtime.getRuntime();

        if (os.contains("win")) {
            runtime.exec(new String[] {"rundll32", "url.dll,FileProtocolHandler", URL});
        } else if (os.contains("mac")) {
            runtime.exec(new String[] {"open", URL});
        } else {
            runtime.exec(new String[] {"xdg-open", URL});
        }
    }

}
