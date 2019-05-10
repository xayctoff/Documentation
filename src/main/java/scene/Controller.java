package scene;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Document;
import model.Product;
import model.Total;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Controller {

    private static final int mainTableColumnWidth = 135;
    private static final int costTableColumnWidth = 200;
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
            showMessage("Минимальный выбранный период должен составлять 1 месяц", "Ошибка интервала");
        }

        else if (difference > 5) {
            showMessage("Максимальный выбранный период должен составлять 5 месяцев", "Ошибка интервала");
        }

        else {
            mainTable.getColumns().removeAll(remains);
            costTable.getColumns().removeAll(total);

            remains.clear();
            total.clear();

            LocalDate date = document.getDateFrom();

            for (int i = 0; i <= difference; i++) {

                String headerDate = getDate(date);

                TableColumn <Product, Integer> mainTableColumn =
                        new TableColumn <>("Остатки на " + headerDate);
                mainTableColumn.setPrefWidth(mainTableColumnWidth);
                mainTableColumn.setResizable(false);
                remains.add(mainTableColumn);

                TableColumn <Total, Double> costTableColumn =
                        new TableColumn<>("На " + headerDate);
                costTableColumn.setPrefWidth(costTableColumnWidth);
                costTableColumn.setResizable(false);
                total.add(costTableColumn);

                date = date.plusMonths(1);

                if (date.getDayOfMonth() == 30 && date.plusDays(1).getDayOfMonth() == 31) {
                    date = date.plusDays(1);
                }

                else if (date.getDayOfMonth() == 28) {
                    date = date.plusDays(3);
                }

                else if (date.getDayOfMonth() == 29) {
                    date = date.plusDays(2);
                }

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

    private void showMessage(String message, String header) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
