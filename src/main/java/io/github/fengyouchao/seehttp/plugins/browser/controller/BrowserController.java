package io.github.fengyouchao.seehttp.plugins.browser.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The class <code></code>
 *
 * @author Youchao Feng
 * @version 1.0
 * @date Mar 03,2016 12:01 PM
 */
public class BrowserController implements Initializable {

  @FXML private TabPane tabPane;

  @FXML private Tab newTab;
  @FXML private TextField locationFiled;
  private int index = 0;
  private boolean selectNextTabMode = false;
  private boolean selectPreviousTabMode = false;

  public void goBackAction() {
    getSelectTab().goBack();
  }

  public void goForwardAction() {
    getSelectTab().goForward();
  }

  public void reloadAction() {
    getSelectTab().reload();
  }

  public void handleKeyBoardShortcut(KeyEvent event) {
    if (event.isControlDown() || event.isMetaDown()) {
      if (event.getCode() == KeyCode.L) {
        locationFiled.requestFocus();
        locationFiled.selectAll();
      } else if (event.getCode() == KeyCode.T) {
        createNewTab();
      } else if (event.getCode() == KeyCode.W) {
        closeTab(getSelectTab());
      } else if (event.getCode() == KeyCode.R) {
        getSelectTab().reload();
      } else if (event.getCode() == KeyCode.TAB) {
        if (event.isShiftDown()) {
          selectPreTab();
        } else {
          selectNextTab();
        }
      }
    }
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
    newTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        createNewTab();
        locationFiled.requestFocus();
      }
    });

    tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
    tabPane.setOnKeyPressed(event -> {
      if (event.isControlDown() && event.getCode() == KeyCode.TAB) {
        if (event.isShiftDown()) {
          selectPreviousTabMode = true;
        } else {
          selectNextTabMode = true;
        }
      }
      if (event.isConsumed()) {
        selectNextTabMode = false;
        selectPreviousTabMode = false;
      }
    });
    tabPane.setOnKeyReleased(event -> {
      selectNextTabMode = false;
      selectPreviousTabMode = false;
    });
    tabPane.getSelectionModel().selectedIndexProperty()
        .addListener((observable, oldValue, newValue) -> {
          int index1 = newValue.intValue();
          if (index1 == tabPane.getTabs().size() - 1) {
            if (selectNextTabMode) {
              index1 = 0;
            }
            if (selectPreviousTabMode) {
              index1 = tabPane.getTabs().size() - 2;
            }
            tabPane.getSelectionModel().select(index1);
          }
        });
  }

  public BrowserTab createNewTab() {
    locationFiled.setText("");
    locationFiled.setId((index++) + "");
    ObservableList<Tab> tabs = tabPane.getTabs();
    int index = tabs.size() - 1;
    BrowserTab tab = new BrowserTab(this);
    tabs.add(index, tab);
    tabPane.getSelectionModel().select(index);
    return tab;
  }

  public void selectNextTab() {
    int selectIndex = tabPane.getSelectionModel().getSelectedIndex();
    selectIndex++;
    if (selectIndex == tabPane.getTabs().size() - 1) {
      selectIndex = 0;
    }
    tabPane.getSelectionModel().select(selectIndex);
  }

  public void selectPreTab() {
    int selectIndex = tabPane.getSelectionModel().getSelectedIndex();
    selectIndex--;
    if (selectIndex == 0) {
      selectIndex = tabPane.getTabs().size() - 2;
    }
    tabPane.getSelectionModel().select(selectIndex);
  }

  public void closeTab(BrowserTab tab) {
    int index = tabPane.getTabs().indexOf(tab);
    tabPane.getTabs().remove(tab);
    if (index == tabPane.getTabs().size() - 1) {
      index--;
      if (index < 0) {
        index = 0;
      }
    }
    tabPane.getSelectionModel().select(index);
  }

  public void closeOtherTab(BrowserTab keepTab) {
    int index = tabPane.getTabs().indexOf(keepTab);
    List<Tab> removedTabs = new ArrayList<>();
    for (int i = 0; i < tabPane.getTabs().size() - 1; i++) {
      if (i != index) {
        removedTabs.add(tabPane.getTabs().get(i));
      }
    }
    for (Tab tab : removedTabs) {
      tabPane.getTabs().remove(tab);
    }
  }


  public BrowserTab getSelectTab() {
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
