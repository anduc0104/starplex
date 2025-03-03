package com.cinema.starplex.ui.functions;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Function;

public class TablePaginationUtility<T> {
    private final TableView<T> tableView;
    private final Pagination pagination;
    private final int rowsPerPage;
    private ObservableList<T> masterData = FXCollections.observableArrayList();
    private Function<Integer, List<T>> dataLoader;
    private int totalItems = 0;
    private final Label infoLabel = new Label();
    private final VBox container;

    public TablePaginationUtility(TableView<T> tableView, int rowsPerPage) {
        this.tableView = tableView;
        this.rowsPerPage = rowsPerPage;

        //tinh toan so de dem so trang
        int pageCount = calculatePageCount(totalItems, rowsPerPage);

        // tao pagination control
        this.pagination = new Pagination(pageCount, 0);
        this.pagination.setPageFactory(this::createPage);

        // tao container for table, pagination, and info label
        this.container = new VBox(5);
        this.container.getChildren().addAll(tableView, pagination, infoLabel);

        // Style info label
        infoLabel.getStyleClass().add("pagination-info-label");
    }

    public VBox getContainer() {
        return container;
    }

    //gan master data cho bang va update paging
    public void setItems(List<T> items) {
        this.masterData.setAll(items);
        this.totalItems = items.size();
        updatePagination();
        createPage(pagination.getCurrentPageIndex());

    }

    //gan data loader roi se duoc goi toi load data cho moi page - loaded lazyly tu db
    //param dataLoader A danh index 1 page roi tra ra du lieu cho trang do
    //param totalItems la tong so items cua tat ca cac page
    public void setDataLoader(Function<Integer, List<T>> dataLoader, int totalItems) {
        this.dataLoader = dataLoader;
        this.totalItems = totalItems;
        updatePagination();
        createPage(pagination.getCurrentPageIndex());
    }

    //    update pagination control dua vao data hien tai
    private void updatePagination() {
        int pageCount = calculatePageCount(totalItems, rowsPerPage);
        pagination.setPageCount(pageCount);
//        infoLabel.setText("Page " + (pagination.getCurrentPageIndex() + 1) + " of " + pageCount);
        if (pagination.getCurrentPageIndex() >= pageCount) {
            pagination.setCurrentPageIndex(Math.max(pageCount - 1, 0));
        }
        updateInfoLabel();
    }

    //    tao mot page co data dua vao index cua moi trang
    //param pageIndex de tao index
    //tra ve node container
    private VBox createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, totalItems);

        ObservableList<T> pageData;
        if (dataLoader != null) {
            // dung data loader function de lay data cho this page
            pageData = FXCollections.observableArrayList(dataLoader.apply(pageIndex));
        } else {
            //dung master data va trich xuat sang trang hien tai
            if (fromIndex <= masterData.size()) {
                pageData = FXCollections.observableArrayList(
                        masterData.subList(fromIndex, Math.min(toIndex, masterData.size()))
                );
            } else {
                pageData = FXCollections.observableArrayList();
            }
        }
        //update bang voi page data
        tableView.setItems(pageData);
        //update info label
        updateInfoLabel();
        return null;
    }

    // update info label dua vao vi tri hien tai cua page
    private void updateInfoLabel() {
        int currentPage = pagination.getCurrentPageIndex() + 1;
        int totalPages = pagination.getPageCount();
        int startItem = (pagination.getCurrentPageIndex() * rowsPerPage) + 1;
        int endItem = Math.min(startItem + rowsPerPage - 1, totalItems);

        if (totalItems > 0) {
            infoLabel.setText(String.format("Showing %d to %d entries (Page %d of %d)", startItem, endItem, totalItems, currentPage, totalPages));
        } else {
            infoLabel.setText("No data available");
        }
    }

    //tinh toan so luong pages can duoc dua vao tong so items va rows cua moi page
    //param totalItems total number cua items
    //param rowsPerPage so rows cua moi page
    //tra ve so luong pages can
    private int calculatePageCount(int totalItems, int rowsPerPage) {
        return (totalItems + rowsPerPage - 1) / rowsPerPage;
    }

    //lam moi trang hien tai, khi data co su thay doi
    public void refresh() {
        createPage(pagination.getCurrentPageIndex());
    }

    //lay page index hien tai
    //tra ve mot trang index hien tai
    public int getCurrentPageIndex() {
        return pagination.getCurrentPageIndex();
    }

    //gan page index hien tai
    //param pageIndex trang index can gan
    public void setCurrentPageIndex(int pageIndex) {
        if (pageIndex >= 0 && pageIndex < pagination.getPageCount()) {
            pagination.setCurrentPageIndex(pageIndex);
        }
    }
    //tang page index len 1, lay so dong moi trang
    //tra ve so dong cho moi trang
    public int getRowsPerPage() {
        return rowsPerPage;
    }

    //lay tong so items
    //tra ve mot so items
    public int getTotalItems() {
        return totalItems;
    }

    //lay pagination control
    //tra ve pagination control
    public Pagination getPagination() {
        return pagination;
    }
}
