module com.example.sortingdisplay {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.sortingdisplay to javafx.fxml;
    exports com.example.sortingdisplay;
}