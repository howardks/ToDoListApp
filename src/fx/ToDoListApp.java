package fx;

import java.util.Stack;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ToDoListApp extends Application {

	private static ObservableList<ToDoItem> toDoList = FXCollections.observableArrayList();
	private static ListView<ToDoItem> toDoListView = new ListView<>(toDoList);
	private static TextField tfAdd = new TextField();
	private static TextField tfCurrent = new TextField();
	private static ComboBox<Integer> cbPriority = new ComboBox<>();
	private Button btnAdd = new Button("Add");
	private Button btnComplete = new Button("Complete!");
	private Button btnRemove = new Button("Remove");
	private static ObservableList<String> completedItems = FXCollections.observableArrayList();
	private static ListView<String> completedItemsListView = new ListView<String>(completedItems);
	private Button btnClear = new Button("Clear");

	@Override
	public void start(Stage primaryStage) {

		// Set up panes for mainTab
		HBox exteriorPane = new HBox();
		exteriorPane.setAlignment(Pos.CENTER);
		exteriorPane.setStyle("-fx-background: lightgray");
		VBox leftPane = new VBox();
		leftPane.setSpacing(10);
		leftPane.setAlignment(Pos.TOP_LEFT);
		VBox rightPane = new VBox();
		rightPane.setSpacing(10);
		rightPane.setAlignment(Pos.TOP_RIGHT);
		exteriorPane.getChildren().add(leftPane);
		exteriorPane.getChildren().add(rightPane);
		
		leftPane.setPadding(new Insets(10, 10, 10, 10));
		rightPane.setPadding(new Insets(10, 10, 10, 10));

		// Left pane stuff
		toDoListView.setMaxHeight(260);
		toDoListView.setMaxWidth(240);
		toDoListView.setStyle("-fx-border-width: 1;-fx-border-color: black;-fx-border-radius: 3;");
		leftPane.getChildren().add(new Label("To Do: "));
		leftPane.getChildren().add(toDoListView);

		// Upper pane stuff for the top of the right pane
		VBox upperPane = new VBox();
		upperPane.setStyle("-fx-padding: 10;-fx-border-width: 1;-fx-border-color: black;-fx-border-radius: 3;");
		upperPane.setSpacing(10);
		upperPane.getChildren().add(new Label("New Item: "));
		upperPane.getChildren().add(tfAdd);
		tfAdd.setStyle("-fx-border-width: 1;-fx-border-color: black;-fx-border-radius: 3;");
		upperPane.getChildren().add(new Label("Priority: "));
		cbPriority.getItems().addAll(1, 2, 3);
		cbPriority.setStyle("-fx-border-width: 1;-fx-border-color: black;-fx-border-radius: 3;");
		upperPane.getChildren().add(cbPriority);
		upperPane.getChildren().add(btnAdd);
		btnAdd.setStyle("-fx-border-width: 1;-fx-border-color: black;-fx-border-radius: 3;");
		rightPane.getChildren().add(upperPane);

		// Lower pane stuff for the bottom of the right pane
		VBox lowerPane = new VBox();
		lowerPane.setPrefHeight(100);
		lowerPane.setSpacing(10);
		lowerPane.setAlignment(Pos.BOTTOM_LEFT);
		lowerPane.getChildren().add(new Label("Selected: "));
		lowerPane.getChildren().add(tfCurrent);
		tfCurrent.setStyle("-fx-border-width: 1;-fx-border-color: black;-fx-border-radius: 3;");
		tfCurrent.setEditable(false);
		
		// Bottom pane stuff for the bottom of the Lower pane
		HBox bottomPane = new HBox();
		bottomPane.setSpacing(26);
		bottomPane.getChildren().add(btnComplete);
		btnComplete.setStyle("-fx-border-width: 1;-fx-border-color: black;-fx-border-radius: 3;");
		btnComplete.setPrefWidth(76);
		bottomPane.getChildren().add(btnRemove);
		btnRemove.setStyle("-fx-border-width: 1;-fx-border-color: black;-fx-border-radius: 3;");
		btnRemove.setPrefWidth(76);
		lowerPane.getChildren().add(bottomPane);
		rightPane.getChildren().add(lowerPane);

		// Event handlers for mainTab
		toDoListView.setOnMouseClicked(e -> update());
		btnAdd.setOnAction(e -> add());
		btnComplete.setOnAction(e -> complete());
		btnRemove.setOnAction(e -> remove());
		
		// Set up panes for completedTab
		VBox completedPane = new VBox();
		completedPane.setPadding(new Insets(10, 10, 10, 10));
		//completedPane.setAlignment(Pos.CENTER);
		completedPane.setSpacing(10);
		Label lblCompletedItems = new Label("Completed Items:");
		completedPane.getChildren().add(lblCompletedItems);
		lblCompletedItems.setPadding(new Insets(0, 0, 0, 72));
		HBox completedPaneBottom = new HBox();
		completedPaneBottom.setSpacing(10);
		completedPaneBottom.setAlignment(Pos.BOTTOM_CENTER);
		completedPaneBottom.getChildren().add(completedItemsListView);
		completedItemsListView.setMaxSize(240, 260);
		completedItemsListView.setStyle("-fx-border-width: 1;-fx-border-color: black;-fx-border-radius: 3;");
		completedPaneBottom.getChildren().add(btnClear);
		btnClear.setStyle("-fx-border-width: 1;-fx-border-color: black;-fx-border-radius: 3;");
		completedPane.getChildren().add(completedPaneBottom);
		
		// Event handlers for completedTab
		btnClear.setOnAction(e -> completedItems.clear());

		// Set up TabPane
		TabPane tabPane = new TabPane();
		Tab mainTab = new Tab();
		mainTab.setText("To Do");
		mainTab.setContent(exteriorPane);
		Tab completedTab = new Tab();
		completedTab.setText("Completed");
		completedTab.setContent(completedPane);
		tabPane.getTabs().add(mainTab);
		tabPane.getTabs().add(completedTab);
		tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

		// Window stuff
		Scene scene = new Scene(tabPane, 460, 340);
		primaryStage.setTitle("To Do List"); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage
	}

	public static void add() { // Add item to the List
		if (!tfAdd.getText().isBlank() && cbPriority.getValue() != null) {
			String element = tfAdd.getText();
			int priority = cbPriority.getValue();

			toDoList.add(new ToDoItem(element, priority));
			tfAdd.setText("");
		}
		update();
	}

	public static void complete() { // Mark item as completed
		int index = toDoListView.getSelectionModel().getSelectedIndex();
		if (index > -1) {
			toDoList.get(index).setCompleted(true);
		}

		update();
	}
	
	public static void remove() {
		int index = toDoListView.getSelectionModel().getSelectedIndex();
		if (index > -1) {
			toDoList.remove(index);
		}
	}

	public static void update() { // Remove completed items and resort the List, update tfCurrent

		for (int i = 0; i < toDoList.size(); i++) {
			if (toDoList.get(i).isCompleted() == true) {
				completedItems.add(0, toDoList.get(i).getElement());
				toDoList.remove(i);
			}
		}
		java.util.Collections.sort(toDoList);
		ToDoItem current = toDoListView.getSelectionModel().getSelectedItem();
		if (current != null) {
			tfCurrent.setText(current.getElement());
		} else {
			tfCurrent.setText(null);
		}
	}
}

class ToDoItem implements java.lang.Comparable<ToDoItem> {
	private String element;
	private boolean completed;
	private int priority;

	public ToDoItem(String element, int priority) {
		setElement(element);
		setPriority(priority);
		setCompleted(false);
	}

	@Override
	public String toString() {
		return String.format("%-36s Priority: %d", getElement(), getPriority(), isCompleted());
	}

	public String getElement() {
		return element;
	}

	public void setElement(String element) {
		this.element = element;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	@Override
	public int compareTo(ToDoItem o) {
		if (this.getPriority() > o.getPriority()) {
			return 1;
		} else if (this.getPriority() == o.getPriority()) {
			return 0;
		} else {
			return -1;
		}
	}
}