package programminglife.gui.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import programminglife.ProgrammingLife;
import programminglife.gui.Alerts;
import programminglife.model.Bookmark;
import programminglife.model.GenomeGraph;
import programminglife.parser.GraphParser;
import programminglife.utility.Console;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Controller for loading bookmarks.
 */
public class GuiLoadBookmarkController implements Observer {
    private GuiController guiController;

    @FXML private Button btnOpenBookmark;
    @FXML private Button btnDeleteBookmark;
    @FXML private Button btnCreateBookmark;
    @FXML private Button btnShowInfo;
    @FXML private Accordion accordionBookmark;
    private List<TableView<Bookmark>> tableViews;

    /**
     * Initialize method for BookmarkController.
     */
    @FXML
    @SuppressWarnings("unused")
    public void initialize() {
        btnCreateBookmark.setDisable(true);
        initButtons();
    }

    /**
     * Checks whether the user has selected a bookmark.
     *
     * @return True if selected, false otherwise.
     */
    private Bookmark checkBookmarkSelection() {
        Bookmark bookmark;
        for (TableView<Bookmark> tableView : tableViews) {
            if (tableView.getSelectionModel().getSelectedItem() != null) {
                bookmark = tableView.getSelectionModel().getSelectedItem();
                return bookmark;
            }
        }
        Alerts.warning("No bookmark selected");
        return null;
    }

    /**
     * Initializes the buttons in the window.
     */
    private void initButtons() {
        btnOpenBookmark.setOnAction(event -> openBookmark());
        btnDeleteBookmark.setOnAction(event -> buttonDelete());
        btnCreateBookmark.setOnAction(event -> createBookmark());
        btnShowInfo.setOnAction(event -> showInfo());
    }

    /**
     * Handles the actions of the open bookmark button.
     */
    private void openBookmark() {
        Bookmark bookmark = checkBookmarkSelection();
        if (bookmark != null) {
            guiController.setText(bookmark.getNodeID());
            if (guiController.getFile() == null
                    || !bookmark.getPath().equals(guiController.getFile().getAbsolutePath())) {
                File file = new File(bookmark.getPath());
                guiController.setFile(file);
                guiController.openFile(file).addObserver(this);
            } else {
                guiController.draw();
            }
            ((Stage) btnOpenBookmark.getScene().getWindow()).close();
            Console.println("Loaded bookmark " + bookmark.getBookmarkName()
                    + " Center Node: " + bookmark.getNodeID() + " Radius: " + bookmark.getRadius());
        }
    }

    /**
     * Handles the actions of the delete bookmark button.
     */
    private void buttonDelete() {
        Bookmark bookmark = checkBookmarkSelection();
        if (bookmark != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            DialogPane pane = alert.getDialogPane();
            if (ProgrammingLife.getShowCSS()) {
                pane.getStylesheets().add("/Alerts.css");
            } else {
                pane.getStylesheets().removeAll();
            }
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Do you really want to delete bookmark: \"" + bookmark.getBookmarkName() + "\"?");
            Optional<ButtonType> result = alert.showAndWait();
            result.ifPresent(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    BookmarkController.deleteBookmark(bookmark.getGraphName(), bookmark.getBookmarkName());
                    Console.println("Deleted bookmark " + bookmark.getBookmarkName()
                            + " Center Node: " + bookmark.getNodeID() + " Radius: " + bookmark.getRadius());
                    initBookmarks();
                } else {
                    alert.close();
                }
            });
        }
    }

    /**
     * Called when create bookmark button is triggered.
     * Creates a new bookmark and stores it.
     */
    private void createBookmark() {
        try {
            FXMLLoader loader = new FXMLLoader(ProgrammingLife.class.getResource("/CreateBookmarkWindow.fxml"));
            AnchorPane page = loader.load();
            if (ProgrammingLife.getShowCSS()) {
                page.getStylesheets().add("/CreateBookmark.css");
            } else {
                page.getStylesheets().removeAll();
            }
            GuiCreateBookmarkController gc = loader.getController();
            gc.setGuiController(guiController);
            Scene scene = new Scene(page);
            Stage bookmarkDialogStage = new Stage();
            bookmarkDialogStage.setResizable(false);
            bookmarkDialogStage.setScene(scene);
            bookmarkDialogStage.setTitle("Create Bookmark");
            bookmarkDialogStage.initOwner(ProgrammingLife.getStage());
            bookmarkDialogStage.showAndWait();
            initBookmarks();
        } catch (IOException e) {
            Alerts.error("This bookmark cannot be created.");
        }
    }

    /**
     * Shows the complete info of a bookmark.
     */
    private void showInfo() {
        Bookmark bookmark = checkBookmarkSelection();
        if (bookmark != null) {
            Alerts.infoBookmarkAlert(String.format("Name: %s"
                            + "%nNode ID: %d%nDescription: %s", bookmark.getBookmarkName(),
                    bookmark.getNodeID(), bookmark.getDescription()));
        }
    }

    /**
     * Creates the tableview with the menu's for the bookmarks.
     *
     * @param graph     String the graph for which we have bookmarks.
     * @param bookmarks List of bookmarks that are created for the graphs.
     */
    private void createTableView(String graph, List<Bookmark> bookmarks) {
        TableColumn<Bookmark, String> tableColumn = new TableColumn<>("Name");
        tableColumn.setId("Name" + graph);
        tableColumn.setPrefWidth(120);
        tableColumn.setResizable(false);

        TableColumn<Bookmark, String> tableColumn1 = new TableColumn<>("Description");
        tableColumn1.setId("Description" + graph);
        tableColumn1.setPrefWidth(455);
        tableColumn1.setResizable(false);

        TableView<Bookmark> tableView = new TableView<>();
        tableView.getColumns().add(0, tableColumn);
        tableView.getColumns().add(1, tableColumn1);

        tableView.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                openBookmark();
            }
        });

        tableViews.add(tableView);

        AnchorPane anchorPane = new AnchorPane(tableView);
        AnchorPane.setBottomAnchor(tableView, 0.d);
        AnchorPane.setTopAnchor(tableView, 0.d);
        AnchorPane.setLeftAnchor(tableView, 0.d);
        AnchorPane.setRightAnchor(tableView, 0.d);

        TitledPane titledPane = new TitledPane();
        titledPane.setText(graph);

        titledPane.setContent(anchorPane);
        accordionBookmark.getPanes().add(titledPane);

        ObservableList<Bookmark> bookmarksList = FXCollections.observableArrayList();
        for (Bookmark bm : bookmarks) {
            bookmarksList.addAll(bm);
        }
        tableColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        tableColumn1.setCellValueFactory(cellData -> cellData.getValue().getDescriptionProperty());
        tableView.setItems(bookmarksList);
    }

    /**
     * Initializes the bookmarks from the different graphs.
     */
    void initBookmarks() {
        accordionBookmark.getPanes().clear();
        if (ProgrammingLife.getShowCSS()) {
            accordionBookmark.getParent().getStylesheets().add("/Bookmark.css");
        } else {
            accordionBookmark.getParent().getStylesheets().removeAll();
        }

        tableViews = new ArrayList<>();

        Map<String, List<Bookmark>> bookmarks = BookmarkController.loadAllBookmarks();
        for (Map.Entry<String, List<Bookmark>> graphBookmarks : bookmarks.entrySet()) {
            createTableView(graphBookmarks.getKey(), graphBookmarks.getValue());
        }
        if (guiController.getGraphController().getGraph() != null) {
            String graphName = guiController.getGraphController().getGraph().getID();
            for (TitledPane pane : accordionBookmark.getPanes()) {
                if (pane.getText().equals(graphName)) {
                    accordionBookmark.setExpandedPane(pane);
                }
            }
        }
    }

    /**
     * Sets the guicontroller for controlling the menu.
     * Is used for setting center node and radius text fields.
     *
     * @param guiController The gui controller
     */
    void setGuiController(GuiController guiController) {
        this.guiController = guiController;
    }

    /**
     * Sets the create bookmark button to active when a file is opened.
     */
    public void setBtnCreateBookmarkActive() {
        btnCreateBookmark.setDisable(false);
    }

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof GraphParser) {
            if (arg instanceof GenomeGraph) {
                Platform.runLater(() -> guiController.draw());
            }
        }
    }
}
