package scene;

import data.Data;
import data.Excel;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import javafx.util.converter.IntegerStringConverter;
import model.Document;
import model.Product;
import model.Total;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

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
            TableColumn <Product, Double>>>> remains;
    private ArrayList <TableColumn <Total, Double>> totals;

    @FXML
    public void initialize() {
        mainTable.setPlaceholder(new Label("Для начала работы установите отчётный период"));
        costTable.setPlaceholder(new Label("Для начала работы установите отчётный период"));
        document = new Document();

        remains = new ArrayList <>();
        totals = new ArrayList <>();

        Data data = new Data();

        Pair <ArrayList <String>, ArrayList <String>> header = data.getHeader(document);
        ArrayList <String> footer = data.getFooter();

        organization.getItems().addAll(header.getKey());
        unit.getItems().addAll(header.getValue());
        responsiblePost.getItems().addAll(footer);
        checkingPost.getItems().addAll(footer);

        setDate();

        title.setText(title.getText() + " №" + document.getNumber() + " от " + document.getDate());
        valueOCUD.setText(document.getOCUD());
        valueOCPO.setText(document.getOCPO());

        responsibleFace.setOnAction(event -> responsibleFaceFilling());
        checkingFace.setOnAction(event -> checkingFaceFilling());

        initMainTable();
    }

    @FXML
    public void organizationSelecting() {
        document.setOrganization(this.organization.getSelectionModel().getSelectedItem());
    }

    @FXML
    public void unitSelecting() {
        document.setUnit(this.unit.getSelectionModel().getSelectedItem());
    }

    @FXML
    public void responsiblePostSelecting() {
        document.setResponsiblePost(this.responsiblePost.getSelectionModel().getSelectedItem());
    }

    public void checkingPostSelecting() {
        document.setCheckingPost(this.checkingPost.getSelectionModel().getSelectedItem());
    }

    @FXML
    public void responsibleFaceFilling() {
        responsibleFace.textProperty().addListener((observableValue, oldValue, newValue)
            -> document.setCheckingFace(newValue));
    }

    @FXML
    public void checkingFaceFilling() {
        checkingFace.textProperty().addListener((observable, oldValue, newValue)
            -> document.setCheckingFace(newValue));
    }

    @FXML
    public void dateFromAction() {
        document.setDateFrom(getDate(dateFrom.getValue()));

        if (dateFrom.getValue() != null && dateTo.getValue() != null) {
            countMonthDifference();

            for (Product product : document.getProducts()) {
                setRemains(product);
            }

            setSum();
        }
    }

    @FXML
    public void dateToAction() {
        document.setDateTo(getDate(dateTo.getValue()));

        if (dateFrom.getValue() != null && dateTo.getValue() != null) {

            if (countMonthDifference()) {
                ObservableList<Product> list = mainTable.getItems();

                if (list.isEmpty()) {
                    addRow();
                }

                else {

                    for (Product product : document.getProducts()) {
                        setRemains(product);
                    }

                    setSum();

                }
            }
        }
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

        createComboBoxCell(name, Product.getProductCodes());


        productCode.setCellFactory(TextFieldTableCell.forTableColumn());
        productCode.setEditable(false);

        createComboBoxCell(measures, Product.getMeasuresCodes());

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

                    Total total = costTable.getItems().get(0);

                    if (cell.getIndex() < Document.maxFrontSideRows) {
                        total.setSideSum(product.getCost(), document.getProducts(), product);
                    }

                    costTable.getItems().get(0).setSum(product.getCost(), document.getProducts(), product);
                    costTable.refresh();
                });

                return cell;
            }
        });

        mainTable.setEditable(true);
        costTable.setEditable(false);
    }

    private void createComboBoxCell(TableColumn <Product, String> column, HashMap <String, String> codes) {
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
                        addRow();
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

    private void addRow() {
        if (Product.counter == 1) {
            Product product = new Product(Product.counter++);
            Total total = new Total();
            setRemains(product);
            setSum();
            document.add(product);
            this.mainTable.getItems().add(product);
            this.costTable.getItems().add(total);
        }

        else if (Product.counter < Document.maxRows &&
                this.mainTable.getItems().get(this.mainTable.getItems().size() - 1).getTitle() != null) {
            Product product = new Product(Product.counter++);
            setRemains(product);
            document.add(product);
            this.mainTable.getItems().add(product);
        }
    }

    private void setRemains(Product product) {
        TreeMap <String, Pair <Integer, Double>> remains = new TreeMap <>();
        int index = 0;

        for (TableColumn <Product, Pair <TableColumn <Product, Integer>, TableColumn <Product, Double>>>
                column : this.remains) {
            remains.put(index + ", " + column.getText().substring(11), new Pair <>(0, 0.0));
            index++;
        }

        product.setRemains(remains);
    }

    private void setSum() {
        ArrayList <Double> totals = new ArrayList <>();

        while (totals.size() != this.totals.size()) {
            totals.add(0.0);
        }

        Total.setSum(totals);
        Total.setSideSum(totals);
    }

    private boolean countMonthDifference() {
        long difference = ChronoUnit.MONTHS.between(dateFrom.getValue().withDayOfMonth(1),
                dateTo.getValue().withDayOfMonth(1));

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
            addRemainsColumns(difference);
            return true;
        }

    }

    private void addRemainsColumns(long difference) {
        mainTable.getColumns().removeAll(remains);
        costTable.getColumns().removeAll(totals);

        remains.clear();
        totals.clear();

        LocalDate date = dateFrom.getValue();

        for (int i = 0; i <= difference; i++) {
            int index = i;

            String headerDate = getDate(date);

            TableColumn <Product,
                    Pair <TableColumn <Product, Integer>, TableColumn <Product, Double>>> mainTableColumn =
                    new TableColumn <>("Остатки на " + headerDate);
            mainTableColumn.setPrefWidth(mainTableColumnWidth);
            mainTableColumn.setResizable(false);

            String key = index + ", " + headerDate;

            Pair <TableColumn <Product, Integer>, TableColumn <Product, Double>> columnPair =
                    new Pair <>(new TableColumn <>("Количество"), new TableColumn <>("Сумма"));

            TableColumn <Product, Integer> remainsCount = columnPair.getKey();
            remainsCount.setCellValueFactory(
                    productIntegerCellDataFeatures -> new SimpleIntegerProperty(productIntegerCellDataFeatures
                            .getValue().getRemains().get(key).getKey()).asObject());

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
                        product.setRemainsCount(key, Integer.parseInt(field.getText()));
                        mainTable.getItems().get(cell.getIndex()).setRemainsOneSum(key, product.getCost() *
                                Integer.parseInt(field.getText()));
                        mainTable.refresh();

                        Total total = costTable.getItems().get(0);
                        double cost = product.getCost();
                        double oldValue = oldCount[0] * cost;
                        double newValue = Double.parseDouble(field.getText()) * cost;
                        double sum = total.getOneSum(index);
                        double sideSum = total.getOneSideSum(index);

                        if (document.getProducts().size() < Document.maxFrontSideRows) {
                            total.setOneSideSum(sideSum - oldValue + newValue, index);
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
                            .getValue().getRemains().get(key).getValue()).asObject());
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
            totals.add(costTableColumn);

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
        costTable.getColumns().addAll(totals);
    }

    private String getDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return date.format(formatter);
    }

    private void setDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        document.setDate(LocalDate.now().format(formatter));
    }

    private static Document getDocument() {
        return document;
    }

    private void showMessage(String message, String header) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Что-то пошло не так");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void save() {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Файл Microsoft Excel",
                "*.xlsx");
        fileChooser.getExtensionFilters().add(filter);

        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            Excel excel = new Excel(Controller.getDocument());
            excel.setOutput(file);
            excel.write();
        }
    }
}