package scene;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Document;
import model.Product;
import model.Total;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;

public class Controller {

    private static Document document;

    /*  Шапка документа  */
    @FXML
    private Label title;

    @FXML
    private Label valueOCUD;

    @FXML
    private Label valueOCPO;

    @FXML
    private ComboBox <String> organization;

    @FXML
    private ComboBox <String> unit;

    @FXML
    private DatePicker dateFrom;

    @FXML
    private DatePicker dateTo;

    /*  Поля таблицы  */
    @FXML
    private TableView <Product> mainTable;

    @FXML
    private TableColumn <Product, Integer> number;

    @FXML
    private TableColumn <Product, Integer> productCode;

    @FXML
    private TableColumn <Product, Integer> name;

    @FXML
    private TableColumn <Product, Integer> measures;

    @FXML
    private TableColumn <Product, Integer> measuresCode;

    @FXML
    private TableColumn <Product, Integer> cost;

    @FXML
    private TableView <Total> costTable;

    /*  Подвал документа  */
    @FXML
    private ComboBox <String> responsiblePost;

    @FXML
    private ComboBox <String> checkingPost;

    @FXML
    private TextField responsibleFace;

    @FXML
    private TextField checkingFace;

    @FXML
    private Button saveButton;

    private ArrayList <TableColumn <Product, Integer>> remains = new ArrayList<>();
    private ArrayList <TableColumn <Total, Double>> total = new ArrayList<>();

    @FXML
    public void initialize() {
        document = new Document();
        title.setText(title.getText() + " №" + document.getNumber() + " от " + getCurrentDate());
        Product.init();
        document.getHeaderFromFile();
        document.getFooterFromFile();
        valueOCUD.setText(Integer.toString(document.getOCUD()));
        valueOCPO.setText(Integer.toString(document.getOCPO()));

        for (String item : document.getOrganization()) {
            organization.getItems().add(item);
        }

        for (String item : document.getUnit()) {
            unit.getItems().add(item);
        }

        for (String item : document.getCheckingPost()) {
            checkingPost.getItems().add(item);
        }

        for (String item : document.getResponsiblePost()) {
            responsiblePost.getItems().add(item);
        }

    }

    public void addLine() {
        if (Product.counter == 1) {
            Product product = new Product(Product.counter++);
            document.add(product);
            this.mainTable.getItems().add(product);
        }

        else if (Product.counter < Document.maxRows &&
                this.mainTable.getItems().get(this.mainTable.getItems().size() - 1).getTitle() != null) {
            Product product = new Product(Product.counter++);
            document.add(product);
            this.mainTable.getItems().add(product);
        }
    }

    @FXML
    public void dateFromAction() {
        document.setDateFrom(dateFrom.getValue());
    }

    @FXML
    public void dateToAction() {
        document.setDateTo(dateTo.getValue());

        long difference = ChronoUnit.MONTHS.between(document.getDateFrom().withDayOfMonth(1),
                document.getDateTo().withDayOfMonth(1));

        if (difference < 1) {
            showMessage("Минимальный выбранный период должен составлять 1 месяц");
        }

        else if (difference > 5) {
            showMessage("Максимальный выбранный период должен составлять 5 месяцев");
        }

        else {
            remains.clear();
            total.clear();
            LocalDate date = document.getDateTo();

            for (int i = 0; i < difference; i++) {
                remains.add(new TableColumn<>("Остатки на " + getDate(date.minusMonths(i + 1))));
                total.add(new TableColumn<>("На " + getDate(date.minusMonths(i + 1))));
            }

            mainTable.getColumns().addAll(remains);
            costTable.getColumns().addAll(total);
        }

    }

    private String getCurrentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate date = LocalDate.now();
        return date.format(formatter);
    }

    private String getDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return date.format(formatter);
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setContentText(message);
        alert.showAndWait();
    }


}
