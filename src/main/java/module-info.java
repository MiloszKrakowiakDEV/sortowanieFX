module pl.gornik.sortingdisplay {
    requires javafx.controls;
    requires javafx.fxml;


    opens pl.gornik.sortingdisplay to javafx.fxml;
    exports pl.gornik.sortingdisplay;
}