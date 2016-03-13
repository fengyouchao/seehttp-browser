package io.github.fengyouchao.seehttp.plugins.browser.controller;

import io.github.fengyouchao.seehttp.plugins.browser.controller.BrowserTab;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The class <code></code>
 *
 * @author Youchao Feng
 * @version 1.0
 * @date Mar 03,2016 12:01 PM
 */
public class BrowserController implements Initializable {

  @FXML
  private TabPane tabPane;

  @FXML
  private Tab newTab;
  @FXML
  private TextField locationFiled;
  private int index = 0;

  public void goBackAction(){
    getSelectTab().goBack();
  }

  public void goForwardAction(){
    getSelectTab().goForward();
  }

  public void reloadAction(){
    getSelectTab().reload();
  }



  @Override
  public void initialize(URL location, ResourceBundle resources) {
    //    System.setProperty("socksProxyHost","localhost");
    //    System.setProperty("socksProxyPort","1080");
    createNewTab();
    locationFiled.setOnAction(event -> {
      BrowserTab tab = (BrowserTab) tabPane.getSelectionModel().getSelectedItem();
      String url = locationFiled.getText();
      if (!url.startsWith("http://") && !url.startsWith("https://")) {
        url = "http://" + url;
      }
      tab.go(url);
    });
    tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
    newTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        createNewTab();
      }
    });
    locationFiled.requestFocus();
  }

  public BrowserTab createNewTab() {
    locationFiled.setText("");
    locationFiled.setId(index+"");
    ObservableList<Tab> tabs = tabPane.getTabs();
    int index = tabs.size() - 1;
    BrowserTab tab = new BrowserTab(this);
    tabs.add(index, tab);
    tabPane.getSelectionModel().select(index);
    locationFiled.requestFocus();
    return tab;
  }

  public BrowserTab getSelectTab(){
    return (BrowserTab) tabPane.getSelectionModel().getSelectedItem();
  }

  public TabPane getTabPane() {
    return tabPane;
  }

  public Tab getNewTab() {
    return newTab;
  }

  public TextField getLocationFiled() {
    return locationFiled;
  }

}
