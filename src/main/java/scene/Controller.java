package scene;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.Pair;
import javafx.util.converter.IntegerStringConverter;
import model.Document;
import model.Product;
import model.Total;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

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
    private TableColumn <Product, String> name;

    @FXML
    private TableColumn <Product, String> productCode;

    @FXML
    private TableColumn <Product, String> measures;

    @FXML
    private TableColumn <Product, String> measuresCode;

    @FXML
    private TableColumn <Product, Double> cost;

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

    private ArrayList <TableColumn <Product, Pair <TableColumn <Product, Integer>,
            TableColumn <Product, Double>>>> remains = new ArrayList<>();
    private ArrayList <TableColumn <Total, Double>> total = new ArrayList<>();

    @FXML
    public void initialize() {
        mainTable.setPlaceholder(new Label("Для начала работы с таблицей установите отчётный период"));
        costTable.setPlaceholder(new Label("Для начала работы с таблицей установите отчётный период"));
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

        initMainTable();
    }

    private void initMainTable() {
        number.setCellValueFactory(new PropertyValueFactory<>("position"));
        name.setCellValueFactory(new PropertyValueFactory<>("title"));
        productCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        measures.setCellValueFactory(new PropertyValueFactory<>("measures"));
        measuresCode.setCellValueFactory(new PropertyValueFactory<>("OKEI"));
        cost.setCellValueFactory(new PropertyValueFactory<>("cost"));

        initMainTableEditableColumns();
    }

    private void initMainTableEditableColumns() {
        number.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        number.setOnEditCommit(event ->
                event.getTableView().getItems().get(event.getTablePosition().getRow()).setPosition(event.getNewValue()));

        createComboBox(name, Product.getProductCodes());


        productCode.setCellFactory(TextFieldTableCell.forTableColumn());
        productCode.setEditable(false);

        createComboBox(measures, Product.getMeasuresCodes());

        measuresCode.setCellFactory(TextFieldTableCell.forTableColumn());
        measuresCode.setEditable(false);

        cost.setCellFactory(new Callback<>() {

            @Override
            public TableCell <Product, Double> call(TableColumn <Product, Double> parameters) {

                final TextField field = new TextField();
                field.setMaxWidth(cost.getMaxWidth());
                field.setPrefWidth(cost.getWidth());

                TableCell <Product, Double> cell = new TableCell<>() {

                    @Override
                    protected void updateItem(Double reason, boolean empty) {

                        super.updateItem(reason, empty);

                        if (empty) {
                            setGraphic(null);
                        }

                        else {
                            field.setText(Double.toString(reason));
                            setGraphic(field);
                        }
                    }
                };

                field.setOnAction(event -> {
                    Product product = mainTable.getItems().get(cell.getIndex());
                    product.setCost(Double.parseDouble(field.getText()));
                    mainTable.getItems().get(cell.getIndex()).setRemainsSum(product.getCost());
                    mainTable.refresh();

                    costTable.getItems().get(0).setSum(product.getCost(), product);
                    costTable.refresh();
                });

                return cell;
            }
        });

        mainTable.setEditable(true);
        costTable.setEditable(false);
    }

    private void createComboBox(TableColumn <Product, String> column, HashMap <String, String> codes) {
        ObservableList <String> items = FXCollections.observableArrayList(codes.keySet());
        column.setCellFactory(new Callback <>() {

            @Override
            public TableCell <Product, String> call(TableColumn <Product, String> parameters) {

                final ComboBox <String> comboBox = new ComboBox<>(items);

                comboBox.setMaxWidth(column.getMaxWidth());
                comboBox.setPrefWidth(column.getWidth());

                TableCell <Product, String> cell = new TableCell <>() {

                    @Override
                    protected void updateItem(String reason, boolean empty) {

                        super.updateItem(reason, empty);

                        if (empty) {
                            setGraphic(null);
                        }

                        else {
                            comboBox.setValue(reason);
                            setGraphic(comboBox);
                        }
                    }
                };

                comboBox.setOnAction(event -> {
                    Product product = mainTable.getItems().get(cell.getIndex());

                    if (column.getText().equals("Наименование товара")) {
                        product.setTitle(comboBox.getSelectionModel().getSelectedItem());
                        mainTable.getItems().get(cell.getIndex())
                                .setCode(codes.get(product.getTitle()));
                        addLine();
                    }

                    else {
                        product.setMeasures(comboBox.getSelectionModel().getSelectedItem());
                        mainTable.getItems().get(cell.getIndex())
                                .setOKEI(codes.get(product.getMeasures()));
                    }

                    mainTable.refresh();
                });

                return cell;
            }
        });
    }

    private void addLine() {
        if (Product.counter == 1) {
            Product product = new Product(Product.counter++);
            Total total = new Total();
            document.add(product);
            this.mainTable.getItems().add(product);
            this.costTable.getItems().add(total);
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

        if (document.getDateTo() != null) {
            countMonthDifference();
        }
    }

    @FXML
    public void dateToAction() {
        document.setDateTo(dateTo.getValue());

        if (countMonthDifference()) {
            ObservableList<Product> list = mainTable.getItems();

            if (list.isEmpty()) {
                addLine();
            }
        }
    }

    private boolean countMonthDifference() {
        long difference = ChronoUnit.MONTHS.between(document.getDateFrom().withDayOfMonth(1),
                document.getDateTo().withDayOfMonth(1));

        if (difference < 1) {
            showMessage("Минимальный выбранный период должен составлять 1 месяц",
                    "Ошибка нижней границы интервала");
            return false;
        }

        else if (difference > 5) {
            showMessage("Максимальный выбранный период должен составлять 5 месяцев", "Ошибка " +
                    "верхней границы интервала");
            return false;
        }

        else {
            addColumns(difference);
            return true;
        }

    }

    private void addColumns(long difference) {
        mainTable.getColumns().removeAll(remains);
        costTable.getColumns().removeAll(total);

        remains.clear();
        total.clear();

        LocalDate date = document.getDateFrom();

        for (int i = 0; i <= difference; i++) {
            int index = i;
            String headerDate = getDate(date);

            TableColumn <Product,
                    Pair <TableColumn <Product, Integer>, TableColumn <Product, Double>>> mainTableColumn =
                    new TableColumn <>("Остатки на " + headerDate);
            mainTableColumn.setPrefWidth(mainTableColumnWidth);
            mainTableColumn.setResizable(false);

            Pair <TableColumn <Product, Integer>, TableColumn <Product, Double>> columnPair =
                    new Pair <>(new TableColumn <>("Количество"), new TableColumn <>("Сумма"));

            TableColumn <Product, Integer> remainsCount = columnPair.getKey();
            remainsCount.setCellValueFactory(
                    productIntegerCellDataFeatures -> new SimpleIntegerProperty(productIntegerCellDataFeatures
                            .getValue().getRemains().get(index).getKey()).asObject());

            remainsCount.setCellFactory(new Callback<>() {

                @Override
                public TableCell <Product, Integer> call(TableColumn <Product, Integer> parameters) {

                    final int[] oldCount = new int[1];
                    final TextField field = new TextField();
                    field.setMaxWidth(cost.getMaxWidth());
                    field.setPrefWidth(cost.getWidth());

                    TableCell <Product, Integer> cell = new TableCell<>() {

                        @Override
                        protected void updateItem(Integer reason, boolean empty) {

                            super.updateItem(reason, empty);

                            if (empty) {
                                setGraphic(null);
                            }

                            else {
                                field.setText(Integer.toString(reason));
                                setGraphic(field);
                                oldCount[0] = reason;
                            }
                        }
                    };

                    field.setOnAction(event -> {
                        Product product = mainTable.getItems().get(cell.getIndex());
                        product.setRemainsCount(Integer.parseInt(field.getText()), index);
                        mainTable.getItems().get(cell.getIndex()).setRemainsOneSum(product.getCost() *
                                Integer.parseInt(field.getText()), index);
                        mainTable.refresh();

                        Total total = costTable.getItems().get(0);
                        double cost = product.getCost();
                        double oldValue = oldCount[0] * cost;
                        double newValue = Double.parseDouble(field.getText()) * cost;
                        double sum = total.getOneSum(index);

                        if (document.getProducts().size() < Document.maxFrontSideRows) {
                            total.setSideSum(sum - oldValue + newValue, index);
                        }

                        costTable.getItems().get(0).setOneSum(sum - oldValue + newValue, index);
                        costTable.refresh();

                    });

                    return cell;
                }
            });

            TableColumn <Product, Double> remainsSum = columnPair.getValue();
            remainsSum.setCellValueFactory(
                    productIntegerCellDataFeatures -> new SimpleDoubleProperty(productIntegerCellDataFeatures
                            .getValue().getRemains().get(index).getValue()).asObject());
            remainsSum.setEditable(false);

            mainTableColumn.getColumns().add(remainsCount);
            mainTableColumn.getColumns().add(remainsSum);

            remains.add(mainTableColumn);

            TableColumn <Total, Double> costTableColumn =
                    new TableColumn<>("На " + headerDate);
            costTableColumn.setPrefWidth(costTableColumnWidth);
            costTableColumn.setResizable(false);
            costTableColumn.setCellValueFactory(productIntegerCellDataFeatures
                    -> new SimpleDoubleProperty(productIntegerCellDataFeatures
                    .getValue().getSum().get(index)).asObject());
            costTableColumn.setEditable(false);
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
        alert.setTitle("Что-то пошло не так");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void save() {
        //TODO: реализовать сохранение документа в формате .xls
    }

}
