package mx.wedevelop.guernica;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import mx.wedevelop.guernica.sqlite.DBDataSource;
import mx.wedevelop.guernica.sqlite.model.Order;
import mx.wedevelop.guernica.sqlite.model.ProductType;
import mx.wedevelop.guernica.sqlite.model.ReportItem;
import mx.wedevelop.guernica.sqlite.model.Shift;
import mx.wedevelop.guernica.sqlite.model.User;
import mx.wedevelop.guernica.sqlite.model.WorkShift;
import mx.wedevelop.guernica.sqlite.model.helper.WorkDay;

/**
 * Created by root on 22/07/16.
 */
public class GuernicaController {

    private static GuernicaController controller = null;

    public static GuernicaController getController(Context context) {
        if(controller == null)
            controller = new GuernicaController(context);
        return controller;
    }

    private DBDataSource dataSource;
    private Shift currentShift;
    private User currentUser;
    private List<Order> orderList;
    private List<ProductType> productTypeList;

    private double earnings = 0;
    private int sells = 0;

    private GuernicaController(Context context) {
        dataSource = new DBDataSource(context);
        dataSource.open();
    }

    public List<ProductType> getProductTypes() {
        if(productTypeList == null)
            productTypeList = dataSource.ProductType.findAll();
        return productTypeList;
    }

    public Shift findShift(int id) { return dataSource.Shift.get(id); }

    public List<Shift> getShiftList() {
        return dataSource.Shift.findAll();
    }

    public List<Shift> findUnsubmittedShifts() {
        return dataSource.Shift.findAllUnsubmitted();
    }

    public List<ProductType> getShiftListWithSummary(int shiftId) {
        return dataSource.ProductType.findSummary(shiftId);
    }

    public void loginUser(User user) {
        currentUser = dataSource.User.findOrCreate(user);
        openShift();
    }

    public void openShift() {
        currentShift = dataSource.Shift.findOrCreateLatestShift(currentUser);
        orderList = dataSource.Order.findAll(currentShift.getId());
        earnings = 0;
        sells = 0;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public void save(ProductType productType) {
        dataSource.ProductType.save(productType);
    }

    public void remove(ProductType productType) {
        dataSource.ProductType.remove(productType.getId());
    }

    public void save(Order order) {
        dataSource.Order.save(order);
        orderList.add(0, order);
    }

    public void remove(Order order) {
        dataSource.Order.remove(order.getId());
    }

    public Shift getCurrentShift() {
        return currentShift;
    }

    public void closeShift() {
        currentShift.setEndTime(new Date());
        dataSource.Shift.update(currentShift);
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void addOrder(Order order) {
        dataSource.Order.save(order);
        orderList.add(0, order);
    }

    public void resetStatics() {
        sells = 0;
        earnings = 0.0;
    }

    public void updateStatics() {
        resetStatics();
        for(Order order : orderList) {
            sells ++;
            earnings += order.getCost();
        }
    }

    public double getEarnings() {
        return earnings;
    }

    public int getSells() {
        return sells;
    }

    public List<ReportItem> findShiftSummary() {
        return dataSource.Report.findShiftSummary();
    }

    public List<ReportItem> findDailySummary() {
        return dataSource.Report.findDailySummary();
    }

    public List<ReportItem> findWeeklySummary() {
        return dataSource.Report.findWeeklySummary();
    }

    public List<ReportItem> findMonthlySummary() {
        return dataSource.Report.findMonthlySummary();
    }

    public List<WorkShift> findWorkShift() {
        return dataSource.WorkShift.findAll();
    }

    public List<WorkDay> findAllWorkDays() {
        return dataSource.WorkShift.findAllWorkDays();
    }

    public void updateWorkDays(List<WorkDay> workDayList) {
        dataSource.WorkShift.update(workDayList);
    }
}
