package guiFrame.pageCtrl.boOrRePage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import Engine.basicOperation.basicOperation;
import guiFrame.controlManager.controlManager;
import guiFrame.pageCtrl.pageCtrl;
import guiFrame.tableType.libRecord.libRecord;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import libObject.Entry.Entry;
import Engine.bookManagement.bookManagement;
import Engine.userManagement.userManagement;

public class boOrRePage extends pageCtrl
{
    @FXML
    private TextField cardId;
    @FXML
    private TextField bookId;
    @FXML
    private TextField borrowDate;
    @FXML
    private TextField dueDate;
    @FXML
    private TextField returnDate;

    private TableColumn cidCol = new TableColumn("cardId");
    private TableColumn bidCol = new TableColumn("bookId");
    private TableColumn dbCol = new TableColumn("dateBorrow");
    private TableColumn ddCol = new TableColumn("dateDue");
    private TableColumn drCol = new TableColumn("dateReturn");
    private TableColumn spCol = new TableColumn("supervisior");
    private Stage pane = new Stage();
    private Label label= new Label();
    private TableView table = new TableView();

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
       pane.setTitle("Record");
       pane.setMinWidth(650);
       pane.setMaxWidth(650);
       pane.setMinHeight(650);
       pane.setMaxHeight(650);

       label.setFont(new Font("Arial",20));

       table.setEditable(false);
       table.getColumns().addAll(cidCol,bidCol,dbCol,ddCol,drCol,spCol);

       cidCol.setMinWidth(100);
       bidCol.setMinWidth(100);
       dbCol.setMinWidth(100);
       ddCol.setMinWidth(100);
       drCol.setMinWidth(100);
       spCol.setMinWidth(100);

       cidCol.setCellValueFactory(new PropertyValueFactory<>("cardId"));
       bidCol.setCellValueFactory(new PropertyValueFactory<>("bookId"));
       dbCol.setCellValueFactory(new PropertyValueFactory<>("dateBorrow"));
       ddCol.setCellValueFactory(new PropertyValueFactory<>("dateDue"));
       drCol.setCellValueFactory(new PropertyValueFactory<>("dateReturn"));
       spCol.setCellValueFactory(new PropertyValueFactory<>("supervisior"));

       final VBox vbox = new VBox();
       vbox.setSpacing(5);
       vbox.setPadding(new Insets(10, 0, 0, 10));
       vbox.getChildren().addAll(label, table);

       Scene scene = new Scene(new Group());
       ((Group) scene.getRoot()).getChildren().addAll(vbox);

       pane.setScene(scene);
    }

    

    public void showCardInfo()
    {
        String id = cardId.getText();
        ArrayList<Entry> result = userManagement.getUserRecord(id);
        ArrayList<libRecord> temp = new ArrayList<libRecord>();
        for(Entry k : result)
        {
            temp.add(new libRecord(k.getCardId(),k.getBookId(),k.getDateB(),k.getDateD(),k.getDateR(),k.getSupervisor()));
        }
        ObservableList<libRecord> data = FXCollections.observableArrayList(temp);
        label.setText("Record Of Card : "+id);
        table.setItems(data);
        pane.show();
    }
    public void showBookInfo()
    {
        System.out.println("book ID : "+bookId.getText());
    }
    public void toBorrow()
    {
        String Bid=bookId.getText();
        String Cid=cardId.getText();
        String bDate=borrowDate.getText();
        String dDate=dueDate.getText();
        String message="User with ID "+Cid+" want to borrow book with ID "+Bid+"\n"+
                    "borrowDate="+bDate+"\n"+"dueDate="+dDate;
        //System.out.println("User with ID "+Cid+" want to borrow book with ID "+Bid);

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,message);
        Optional<ButtonType> result = confirmation.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK)
        {
            String state = bookManagement.borrowBook(Bid,Cid,bDate, dDate);
            message = state;
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Result");
            info.setHeaderText("SPLibrary");
            info.setContentText(message);
            info.show();
        }
    }
    public void toReturn()
    {
        String Bid=bookId.getText();
        String Cid=cardId.getText();
        String rDate=returnDate.getText();

        String message="User with ID "+Cid+" want to return book with ID "+Bid+"\n"+
                    "returnDate="+rDate;

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,message);
        Optional<ButtonType> result = confirmation.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK)
        {
            boolean state = bookManagement.returnBook(Bid, Cid, rDate);
            if(state)message="Done!";
            else message="Failed! Please Check Again! ";
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Result");
            info.setHeaderText("SPLibrary");
            info.setContentText(message);
            info.show();
        }
    }

}