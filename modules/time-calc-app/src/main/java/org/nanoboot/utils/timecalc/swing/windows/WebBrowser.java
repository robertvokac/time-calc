package org.nanoboot.utils.timecalc.swing.windows;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.nanoboot.utils.timecalc.app.TimeCalcConfiguration;
import org.nanoboot.utils.timecalc.utils.common.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author pc00289
 * @since 26.04.2024
 */
public class WebBrowser extends Application {

    private static final String BROWSE = "browse.";
    public static final String DIGIT = "DIGIT";
    private static Stage stage = null;

    private static Map<String, String> urls = null;
    private static TimeCalcConfiguration timeCalcConfiguration;

    public static void show(TimeCalcConfiguration timeCalcConfigurationIn) {
        timeCalcConfiguration = timeCalcConfigurationIn;
        if (urls == null) {
            urls = new HashMap<>();
            for (int i = 0; i <= 9; i++) {
                String key = BROWSE + DIGIT + i;
                String value = timeCalcConfiguration.getProperty(
                        key);
                //System.out.println(key + "=" + value);
                if (value != null && !value.isBlank()) {
                    urls.put(key, value);
                }
            }
        }
        if (stage == null) {
            Thread thread = new Thread(WebBrowser::startBrowser);
            thread.setDaemon(true);
            thread.start();
        } else {
            Platform.runLater(() -> stage.show());
        }
    }

    private static void startBrowser() {
        launch(WebBrowser.class);
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Web Browser");

        WebView webView = new WebView();

        webView.setContextMenuEnabled(true);
        WebEngine engine = webView.getEngine();
        engine.setOnError(event -> System.out.println(event.getMessage()));
        engine.setOnAlert(event -> System.out.println(event.getData()));

        //        // local error console
        //        com.sun.javafx.webkit.WebConsoleListener.setDefaultListener(
        //                (webview, message, lineNumber, sourceId) -> System.out
        //                        .println("Console: [" + sourceId + ":" + lineNumber + "] " + message));
        //
        //        webView.getEngine().locationProperty().addListener(
        //                (observable, oldValue, newValue) -> System.out.println(newValue));

        engine.load("https://pocasi.cz/praha");

        VBox vBox = new VBox(webView);

        Scene scene = new Scene(vBox, 1400, 800);
        vBox.setMinHeight(800);
        webView.setMinHeight(800);

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.F5) {
                engine.reload();
            }
            if (e.isControlDown() && e.getCode() == KeyCode.R) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Run website");
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    String value = result.get();
                    if (!value.startsWith("http")) {
                        value = "https://" + value;
                        System.out.println("Loading: " + value);
                    }
                    engine.load(value);
                }
            }

            if (e.isControlDown() && e.getCode() == KeyCode.S) {
                Integer[] digits = new Integer[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
                ChoiceDialog<Integer>
                        choiceDialog = new ChoiceDialog<>(digits[0], digits);
                choiceDialog.showAndWait();
                Integer selectedItem = choiceDialog.getSelectedItem();
                if (selectedItem != null) {
                    String key = BROWSE + DIGIT + selectedItem;
                    String value = engine.getLocation();
                    timeCalcConfiguration.setProperty(key, value);
                    if (urls.containsKey(key)) {
                        urls.replace(key, value);
                    } else {
                        urls.put(key, value);
                    }
                }

            }

            if (e.isControlDown() && e.getCode() == KeyCode.W) {

                StringBuilder sb = new StringBuilder("These urls are saved: ");
                sb.append("\n");
                List<String> list = new ArrayList<>();
                for (String key : urls.keySet()) {
                    if (!key.contains(DIGIT)) {
                        continue;
                    }
                    String value = urls.get(key);
                    if (value == null || value.isBlank()) {
                        continue;
                    }
                    list.add(key.replace(BROWSE, "").replace(DIGIT, "") + "="
                             + value + "\n");

                }
                Collections.sort(list);
                for(String s :list) {
                    sb.append(s);
                }
                Utils.showNotification(sb.toString(), 30000, 400);
            }

            for (int i = 0; i <= 9; i++) {
                if (!e.getCode().name().contains(DIGIT)) {
                    break;
                }
                if (e.getCode().name().equals(DIGIT + i)) {
                    String key = BROWSE + DIGIT + i;
                    if (urls.containsKey(key)) {
                        String value = urls.get(key);
                        if (value != null && !value.isBlank()) {
                            if (!value.startsWith("http")) {
                                value = "https://" + value;
                            }
                            engine.load(value);
                        }
                    }
                }

            }

        });
        primaryStage.setScene(scene);
        primaryStage.setTitle("Web Browser");

        stage = primaryStage;
        stage.show();
        Platform.setImplicitExit(false);
        stage.setOnCloseRequest(event -> stage.hide());

        //        Thread t = new Thread(() -> {
        //            while (true) {
        //                System.out.println("ahoj" + stage.isShowing());
        //                try {
        //                    Thread.sleep(100);
        //                } catch (InterruptedException e) {
        //                    e.printStackTrace();
        //                }
        //            }
        //        });
        //        t.setDaemon(true);
        //        t.start();

    }
}
