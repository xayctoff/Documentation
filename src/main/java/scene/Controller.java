package scene;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Document;

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
    private TableView <Document> mainTable;

    @FXML
    private TableColumn <Document, Integer> number;

    @FXML
    private TableColumn <Document, Integer> productCode;

    @FXML
    private TableColumn <Document, Integer> name;

    @FXML
    private TableColumn <Document, Integer> measures;

    @FXML
    private TableColumn <Document, Integer> measuresCode;

    @FXML
    private TableColumn <Document, Integer> cost;

    @FXML
    private TableView <Document> costTable;

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
}
