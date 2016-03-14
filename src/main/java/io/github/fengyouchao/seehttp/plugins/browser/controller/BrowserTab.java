package io.github.fengyouchao.seehttp.plugins.browser.controller;

import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCharacterCombination;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;

/**
 * The class <code>BrowserTab</code> represents tab of browser.
 *
 * @author Youchao Feng
 * @version 1.0
 * @date Mar 03,2016 2:48 PM
 */
public class BrowserTab extends Tab {

  final private WebView webView = new WebView();
  final private WebEngine engine;
  private String currentUrl = "";
  private BrowserController browserController;
  private Line progressLine = new Line();
  private ContextMenu tabMenu;
  private MenuItem newTabItem = new MenuItem("New Tab");
  private MenuItem reloadItem = new MenuItem("Reload Page");
  private MenuItem closeItem = new MenuItem("Close Tab");
  private MenuItem closeOtherItem = new MenuItem("Close Other Tab");

  public BrowserTab(BrowserController browserController) {
    this.browserController = browserController;
    this.setClosable(true);
    this.setText("New Tab");
    StackPane stackPane = new StackPane();
    stackPane.getChildren().addAll(webView, progressLine);
    progressLine.setLayoutX(0);
    progressLine.setLayoutY(0);
    stackPane.setAlignment(Pos.TOP_LEFT);
    this.setContent(stackPane);
    engine = webView.getEngine();
    engine.locationProperty().addListener((observable, oldValue, newValue) -> {
      browserController.getLocationFiled().setText(newValue);
      currentUrl = newValue;
    });
    engine.titleProperty().addListener((observable, oldValue, newValue) -> {
      if(newValue!=null && !newValue.equalsIgnoreCase("")){
        setText(newValue);
      }
    });

    this.selectedProperty().addListener((observable, oldValue, newValue) -> {
      if(newValue){
        browserController.getLocationFiled().setText(currentUrl);
      }
    });
    engine.setCreatePopupHandler(p -> browserController.createNewTab().getEngine());
    progressLine.visibleProperty().bind(engine.getLoadWorker().runningProperty());
    engine.getLoadWorker().progressProperty().addListener((observable, oldValue, newValue) -> {
      double progress = newValue.doubleValue();
      progressLine.setEndX(stackPane.getWidth() * progress);
    });
    initTabMenu();
  }



  private void initTabMenu(){
    newTabItem.setAccelerator(new KeyCharacterCombination("Ctrl+T"));
    newTabItem.setOnAction(event -> getBrowserController().createNewTab());
    closeItem.setAccelerator(new KeyCharacterCombination("Ctrl+W"));
    closeItem.setOnAction(event -> getBrowserController().closeTab(this));
    closeOtherItem.setOnAction(event -> getBrowserController().closeOtherTab(this));
    reloadItem.setAccelerator(new KeyCharacterCombination("Ctrl+R"));
    reloadItem.setOnAction(event -> engine.reload());
    tabMenu = new ContextMenu();
    tabMenu.getItems().addAll(newTabItem,new SeparatorMenuItem(), reloadItem, new SeparatorMenuItem(), closeItem, closeOtherItem);
    this.setContextMenu(tabMenu);
    this.setClosable(true);
  }

  public WebView getWebView() {
    return webView;
  }

  public WebEngine getEngine() {
    return engine;
  }

  public void go(String url) {
    engine.load(url);
  }

  public void reload() {
    engine.reload();
  }

  public void goBack() {
    WebHistory history = engine.getHistory();
    System.out.println(history.getCurrentIndex());
    history.go(-1);
    System.out.println(history.getCurrentIndex());
  }

  public void goForward() {
    WebHistory history = engine.getHistory();
    System.out.println(history.getCurrentIndex());
    history.go(1);
  }

  public String getCurrentUrl() {
    return currentUrl;
  }

  public void setCurrentUrl(String currentUrl) {
    this.currentUrl = currentUrl;
  }

  public BrowserController getBrowserController() {
    return browserController;
  }

  public void setBrowserController(BrowserController browserController) {
    this.browserController = browserController;
  }
}
