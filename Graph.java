package org.example.cs203projekat25363anjapopovic;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;

public class Graph extends Application {

    private static final double RADIUS = 20;
    private int nextNodeId = 1;
    private Queue<Integer> availableIds = new PriorityQueue<>();
    private Circle startCircle = null;
    private Circle selectedCircle = null;
    private Map<Integer, Circle> nodes = new HashMap<>();
    private Map<Integer, Text> nodeLabels = new HashMap<>();
    private Map<Circle, Map<Circle, Line>> edges = new HashMap<>();
    private Map<Integer, List<Integer>> adjacencyList = new HashMap<>();
    private Pane graphPane;

    private final TextField tfInput = new TextField();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        graphPane = new Pane();
        Scene scene = new Scene(graphPane, 600, 400);

        scene.setOnMouseClicked(this::handleMouseClick);
        scene.setOnKeyPressed(this::handleKeyPress);

        tfInput.setFocusTraversable(false);
        Button btnBFS = new Button("BFS");
        Button btnDFS = new Button("DFS");
        Button btnReset = new Button("Reset");
        HBox hBox = new HBox(tfInput, btnBFS, btnDFS, btnReset);
        hBox.setSpacing(5);
        hBox.setPadding(new Insets(10));
        graphPane.getChildren().add(hBox);

        btnBFS.setOnAction(e -> {
            String input = tfInput.getText();
            if (input.matches("\\d+")) {
                int startNode = Integer.parseInt(input);
                List<Integer> result = bfs(startNode);
                showAlert("BFS Order", result.toString());
            } else {
                showAlert("Error", "Invalid input. Please enter a valid node number.");
            }
        });

        btnDFS.setOnAction(e -> {
            String input = tfInput.getText();
            if (input.matches("\\d+")) {
                int startNode = Integer.parseInt(input);
                List<Integer> result = dfs(startNode);
                showAlert("DFS Order", result.toString());
            } else {
                showAlert("Error", "Invalid input. Please enter a valid node number.");
            }
        });

        btnReset.setOnAction(e -> resetGraph());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulacija BFS i DFS");
        primaryStage.show();
    }

    private void handleMouseClick(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            Point2D point = new Point2D(event.getX(), event.getY());
            if (!isPointInAnyNode(point)) {
                addNode(point);
            } else {
                selectNode(point);
            }
        }
    }

    private void addNode(Point2D point) {
        int nodeId;
        if (!availableIds.isEmpty()) {
            nodeId = availableIds.poll();
        } else {
            nodeId = nextNodeId++;
        }

        Circle circle = new Circle(point.getX(), point.getY(), RADIUS, Color.LIGHTBLUE);
        circle.setStroke(Paint.valueOf("black"));
        Text text = new Text(point.getX() - 5, point.getY() + 5, String.valueOf(nodeId));
        nodes.put(nodeId, circle);
        nodeLabels.put(nodeId, text);
        adjacencyList.put(nodeId, new ArrayList<>());

        circle.setOnMousePressed(this::handleMousePressed);
        circle.setOnMouseReleased(this::handleMouseReleased);

        graphPane.getChildren().addAll(circle, text);
    }

    private void handleMousePressed(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            startCircle = (Circle) event.getSource();
        }
    }

    private void handleMouseReleased(MouseEvent event) {
        if (startCircle != null) {
            Circle endCircle = getNodeAtPoint(event.getX(), event.getY());

            if (endCircle != null && startCircle != endCircle) {
                drawEdge(startCircle, endCircle);
            }

            startCircle = null;
        }
    }

    private Circle getNodeAtPoint(double x, double y) {
        for (Circle node : nodes.values()) {
            if (node.contains(new Point2D(x, y))) {
                return node;
            }
        }
        return null;
    }

    private boolean isPointInAnyNode(Point2D point) {
        for (Circle node : nodes.values()) {
            if (node.contains(point)) {
                return true;
            }
        }
        return false;
    }

    private void selectNode(Point2D point) {
        for (Circle node : nodes.values()) {
            if (node.contains(point)) {
                selectedCircle = node;
                break;
            }
        }
    }

    private void drawEdge(Circle src, Circle dest) {
        if (edges.containsKey(src) && edges.get(src).containsKey(dest)) {
            return;
        }
        double srcX = src.getCenterX();
        double srcY = src.getCenterY();
        double destX = dest.getCenterX();
        double destY = dest.getCenterY();

        double dx = destX - srcX;
        double dy = destY - srcY;

        double length = Math.sqrt(dx * dx + dy * dy);
        dx /= length;
        dy /= length;

        double startX = srcX + dx * RADIUS;
        double startY = srcY + dy * RADIUS;
        double endX = destX - dx * RADIUS;
        double endY = destY - dy * RADIUS;

        Line line = new Line(startX, startY, endX, endY);
        line.setStroke(Color.BLACK);

        graphPane.getChildren().add(line);

        edges.computeIfAbsent(src, k -> new HashMap<>()).put(dest, line);
        edges.computeIfAbsent(dest, k -> new HashMap<>()).put(src, line);

        int srcId = getNodeId(src);
        int destId = getNodeId(dest);
        adjacencyList.get(srcId).add(destId);
        adjacencyList.get(destId).add(srcId);
    }

    private int getNodeId(Circle circle) {
        for (Map.Entry<Integer, Circle> entry : nodes.entrySet()) {
            if (entry.getValue().equals(circle)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.DELETE && selectedCircle != null) {
            deleteNode(selectedCircle);
            selectedCircle = null;
        }
    }

    private void deleteNode(Circle circle) {
        if (edges.containsKey(circle)) {
            for (Map.Entry<Circle, Line> entry : edges.get(circle).entrySet()) {
                Circle connectedNode = entry.getKey();
                Line line = entry.getValue();

                graphPane.getChildren().remove(line);
                edges.get(connectedNode).remove(circle);
            }
            edges.remove(circle);
        }

        graphPane.getChildren().remove(circle);
        Integer nodeId = null;
        for (Map.Entry<Integer, Circle> entry : nodes.entrySet()) {
            if (entry.getValue().equals(circle)) {
                nodeId = entry.getKey();
                break;
            }
        }
        if (nodeId != null) {
            Text label = nodeLabels.get(nodeId);
            graphPane.getChildren().remove(label);
            nodes.remove(nodeId);
            nodeLabels.remove(nodeId);
            availableIds.add(nodeId);
            adjacencyList.remove(nodeId);

            for (List<Integer> neighbors : adjacencyList.values()) {
                neighbors.remove(nodeId);
            }
        }
    }

    private void resetGraph() {
        for (Circle node : nodes.values()) {
            graphPane.getChildren().remove(node);
        }
        for (Text label : nodeLabels.values()) {
            graphPane.getChildren().remove(label);
        }

        for (Map<Circle, Line> map : edges.values()) {
            for (Line line : map.values()) {
                graphPane.getChildren().remove(line);
            }
        }

        tfInput.clear();
        nodes.clear();
        nodeLabels.clear();
        edges.clear();
        adjacencyList.clear();
        availableIds.clear();
        nextNodeId = 1;

        showAlert("Reset Graph", "The graph has been reset.");
    }

    private List<Integer> bfs(int startNode) {
        List<Integer> result = new ArrayList<>();
        if (!nodes.containsKey(startNode)) return result;

        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();

        queue.add(startNode);
        visited.add(startNode);

        while (!queue.isEmpty()) {
            int node = queue.poll();
            result.add(node);
            for (int neighbor : adjacencyList.get(node)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        return result;
    }

    private List<Integer> dfs(int startNode) {
        List<Integer> result = new ArrayList<>();
        if (!nodes.containsKey(startNode)) return result;

        Stack<Integer> stack = new Stack<>();
        Set<Integer> visited = new HashSet<>();

        stack.push(startNode);
        visited.add(startNode);

        while (!stack.isEmpty()) {
            int node = stack.pop();
            result.add(node);
            for (int neighbor : adjacencyList.get(node)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    stack.push(neighbor);
                }
            }
        }
        return result;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

