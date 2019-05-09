package scene;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Document;
import model.Product;
import model.Total;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    private ComboBox organization;

    @FXML
    private ComboBox unit;

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
    private ComboBox responsiblePost;

    @FXML
    private ComboBox checkingPost;

    @FXML
    private TextField responsibleFace;

    @FXML
    private TextField checkingFace;

    @FXML
    private Button saveButton;

    @FXML
    public void initialize() {
/*        for (Product product : document.getProducts()) {
            mainTable.getItems().add(product);
            Total total = document.getTotal();
            costTable.getItems().add(total);
        }*/

        title.setText(title.getText() + " №" + document.getNumber() + " от " + getCurrentDate());

    }

    public void addLine() {
        if (Product.counter == 1) {
            Product product = new Product(Product.counter++);
            this.document.add(product);
            this.mainTable.getItems().add(product);
        }

        else if (Product.counter < Document.maxRows &&
                this.mainTable.getItems().get(this.mainTable.getItems().size() - 1).getTitle() != null) {
            Product product = new Product(Product.counter++);
            this.document.add(product);
            this.mainTable.getItems().add(product);
        }
    }

    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }


}
