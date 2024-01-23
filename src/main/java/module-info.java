module com.chat.chatgrupal {
    requires javafx.controls;
    requires javafx.fxml;
            
        requires org.controlsfx.controls;
                            
    opens com.chat.chatgrupal to javafx.fxml;
    exports com.chat.chatgrupal;
}