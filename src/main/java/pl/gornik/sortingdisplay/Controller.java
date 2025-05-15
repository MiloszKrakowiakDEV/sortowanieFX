package pl.gornik.sortingdisplay;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Controller {
    @FXML
    private BarChart<String, Integer> chartArray;

    @FXML
    private ComboBox<String> comboSort;

    @FXML
    private Spinner<Integer> spinCount;

    @FXML
    private Spinner<Integer> spinMax;

    @FXML
    private Spinner<Integer> spinMin;

    private Integer[] arr;

    private int size;
    private int min;
    private int max;
    private String sort;

    private XYChart.Series<String, Integer> series;

    public void initialize() {
        chartArray.getXAxis().setLabel("Index");
        chartArray.getYAxis().setLabel("Value");
        chartArray.setTitle("Array sorting");
        ObservableList<String> sortingList = FXCollections.observableList(new ArrayList<>());
        sortingList.add("Bubble sort");
        sortingList.add("Insertion sort");
        sortingList.add("Selection sort");
        sortingList.add("Heap sort");
        sortingList.add("Quick sort");
        comboSort.setItems(sortingList);
        comboSort.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                switch (newVal) {
                    case "Bubble sort" -> {
                        sort = "bubble";
                    }
                    case "Selection sort" -> {
                        sort = "selection";
                    }
                    case "Insertion sort" -> {
                        sort = "insertion";
                    }
                    case "Heap sort" -> {
                        sort = "heap";
                    }
                    case "Quick sort" -> {
                        sort = "quick";
                    }
                }
            }
        });
        chartArray.setAnimated(false);
        SpinnerValueFactory.IntegerSpinnerValueFactory countFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(100, 1000);
        countFactory.setAmountToStepBy(100);
        spinCount.setValueFactory(countFactory);
        SpinnerValueFactory.IntegerSpinnerValueFactory minFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 100);
        SpinnerValueFactory.IntegerSpinnerValueFactory maxFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(100, 1000);
        minFactory.setAmountToStepBy(10);
        maxFactory.setAmountToStepBy(100);
        spinMax.setValueFactory(maxFactory);
        spinMin.setValueFactory(minFactory);
    }

    public void sort() throws InterruptedException {
        if (sort != null && sort != "") {
            generateArr(spinCount.getValue(), spinMax.getValue(), spinMin.getValue());
            generateChart();
            switch (sort) {
                case "quick" -> quickSort(arr, 0, arr.length - 1);
                case "heap" -> heapSort(arr);
                case "insertion" -> insertionSort(arr);
                case "selection" -> selectionSort(arr);
                case "bubble" -> bubbleSort(arr);
            }
        }
    }

    public void updateChart() {
        for (int i = 0; i < arr.length; i++) {
            series.getData().get(i).setYValue(arr[i]);
        }
    }

    public void generateArr(int size, int max, int min) {
        arr = new Integer[size];
        Random rand = new Random();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = rand.nextInt(min, max + 1);
        }
    }

    public void generateChart() {
        chartArray.getData().clear();
        ((CategoryAxis) chartArray.getXAxis()).getCategories().clear();

        series = new XYChart.Series<>();

        for (int i = 0; i < arr.length; i++) {
            ((CategoryAxis) chartArray.getXAxis()).getCategories().add(String.valueOf(i));
            series.getData().add(new XYChart.Data<>(String.valueOf(i), arr[i]));
        }

        chartArray.getData().add(series);
    }

    public void bubbleSort(Integer[] arr) {
        new Thread(() -> {
            int n = arr.length;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n - i - 1; j++) {
                    if (arr[j] > arr[j + 1]) {
                        int temp = arr[j];
                        arr[j] = arr[j + 1];
                        arr[j + 1] = temp;

                        int[] snapshot = Arrays.stream(arr).mapToInt(Integer::intValue).toArray();
                        javafx.application.Platform.runLater(() -> {
                            updateChart();
                        });
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }


    public void selectionSort(Integer[] arr) {
        new Thread(() -> {
            int n = arr.length;
            for (int i = 0; i < n; i++) {
                int minIndex = i;
                for (int j = i + 1; j < n; j++) {
                    if (arr[j] < arr[minIndex]) {
                        minIndex = j;
                    }
                }
                int temp = arr[i];
                arr[i] = arr[minIndex];
                arr[minIndex] = temp;
                int[] snapshot = Arrays.stream(arr).mapToInt(Integer::intValue).toArray();
                javafx.application.Platform.runLater(() -> {
                    updateChart();
                });

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    public void insertionSort(Integer[] arr) {
        new Thread(() -> {
            int n = arr.length;
            for (int i = 1; i < n; i++) {
                int key = arr[i];
                int j = i - 1;
                while (j >= 0 && arr[j] > key) {
                    arr[j + 1] = arr[j];
                    j--;
                    int[] snapshot = Arrays.stream(arr).mapToInt(Integer::intValue).toArray();
                    javafx.application.Platform.runLater(() -> {
                        updateChart();
                    });

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                arr[j + 1] = key;
            }
        }).start();
    }


    public void quickSort(Integer[] arr, int low, int high) {
        new Thread(() -> {
            if (low < high) {
                int pivotIndex = partition(arr, low, high);
                quickSort(arr, low, pivotIndex - 1);
                quickSort(arr, pivotIndex + 1, high);
            }
        }).start();
    }

    private int partition(Integer[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                int[] snapshot = Arrays.stream(arr).mapToInt(Integer::intValue).toArray();
                javafx.application.Platform.runLater(() -> {
                    updateChart();
                });

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }

    public void heapSort(Integer[] arr) {
        new Thread(() -> {
            int n = arr.length;
            for (int i = n / 2 - 1; i >= 0; i--) {
                heapify(arr, n, i);
            }
            for (int i = n - 1; i > 0; i--) {
                int temp = arr[0];
                arr[0] = arr[i];
                arr[i] = temp;
                heapify(arr, i, 0);
            }
        }).start();
    }

    private void heapify(Integer[] arr, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && arr[left] > arr[largest]) {
            largest = left;
        }
        if (right < n && arr[right] > arr[largest]) {
            largest = right;
        }

        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;
            int[] snapshot = Arrays.stream(arr).mapToInt(Integer::intValue).toArray();
            javafx.application.Platform.runLater(() -> {
                updateChart();
            });

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            heapify(arr, n, largest);
        }
    }

}