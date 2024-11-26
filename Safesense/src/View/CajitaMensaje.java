package View;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class CajitaMensaje extends HBox {
    public CajitaMensaje(String mensaje, boolean esEnviado) {
        super();

        Label etiquetaMensaje = new Label(mensaje);
        etiquetaMensaje.setWrapText(true);
        // Limita el ancho del Label para asegurar que el texto se envuelva.
        etiquetaMensaje.setMaxWidth(300); // Este es el ancho máximo antes de que el texto se envuelva.

        if (esEnviado) {
            this.setAlignment(Pos.CENTER_RIGHT);
            etiquetaMensaje.setStyle("-fx-background-color: lightblue; -fx-padding: 5;");
        } else {
            this.setAlignment(Pos.CENTER_LEFT);
            etiquetaMensaje.setStyle("-fx-background-color: lightgrey; -fx-padding: 5;");
        }

        this.getChildren().add(etiquetaMensaje);
        HBox.setHgrow(etiquetaMensaje, Priority.ALWAYS);

        // Asegúrate de que el HBox se ajuste en altura según el contenido.
        this.setPrefHeight(USE_COMPUTED_SIZE);
        this.setMaxHeight(USE_PREF_SIZE);

        // Establecer el espaciado y el relleno puede ayudar a manejar el crecimiento vertical correctamente.
        this.setSpacing(10); // Ajusta este valor según sea necesario.
        this.setPadding(new Insets(5, 10, 5, 10)); // Ajusta los valores de Insets según sea necesario.

        // Asegúrate de que el VBox o cualquier otro contenedor padre también permita el crecimiento en altura de sus hijos.
    }
}

