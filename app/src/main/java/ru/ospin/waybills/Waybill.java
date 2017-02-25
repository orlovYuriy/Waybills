package ru.ospin.waybills;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuriy on 14.12.2016.
 */

public class Waybill {

    private static final String TAG = "Waybill_TAG";
    public static final String SERVICES = "2";
    public static final String SERVICE = "3";

    static boolean isStart = true;

    private static List<Waybill> items = new ArrayList<>();

    private String orderNumber;
    private String customer;
    private String customerAddress;
    private String deliveryTime;
    private String arrivalTime;
    private String shipment;
    private List<GoodOfBill> goods = new ArrayList<>();
    private List<ServiceOfBill> services = new ArrayList<>();

    public List<ServiceOfBill> getServices() {
        return services;
    }

    public void setShipment(String shipment) {
        this.shipment = shipment;
    }

    public List<GoodOfBill> getGoods() {
        return goods;
    }

    public String getShipment() {
        if (shipment==null)
            return "";
        try {
            return URLDecoder.decode(shipment,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  shipment;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getCustomer() {
        if (customer==null)
            return "";
        try {
            return URLDecoder.decode(customer,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return customer;
    }

    public String getCustomerAddress() {
        if (customerAddress==null)
            return "";
        try {
            return URLDecoder.decode(customerAddress,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return customerAddress;
    }

    public String getDeliveryTime() {
        if (deliveryTime==null)
            return "";
        try {
            return URLDecoder.decode(deliveryTime,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return deliveryTime;
    }

    public String getArrivalTime() {
        if (arrivalTime==null)
            return "";
        try {
            return URLDecoder.decode(arrivalTime,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return arrivalTime;
    }

    public static Waybill getWaybill(String orderNumber){
        for (Waybill waybill : items) {
            if (waybill.getOrderNumber().equals(orderNumber)) {
                return waybill;
            }
        }
        return null;
    }


    public Waybill(String orderNumber, String customer, String customerAddress, String deliveryTime, String arrivalTime, String shipment) {
        this.orderNumber = orderNumber;
        this.customer = customer;
        this.customerAddress = customerAddress;
        this.deliveryTime = deliveryTime;
        this.arrivalTime = arrivalTime;
        this.shipment = shipment;

    }

    public static List<Waybill> getItems() {
        return items;
    }

    public static void parseItems(JSONObject jsonBody) throws IOException, JSONException {
        JSONObject waybillsJsonObject = jsonBody.getJSONObject("info");
        JSONArray waybillJsonArray = waybillsJsonObject.getJSONArray("orders");

        items.clear();
        for (int i = 0; i < waybillJsonArray.length(); i++) {
            JSONObject waybillJsonObject = waybillJsonArray.getJSONObject(i);
            Waybill item = new Gson().fromJson(waybillJsonObject.toString(), Waybill.class);

            item.conectServicesToGoods();

            items.add(item);
        }
    }
    
    public void conectServicesToGoods(){
        if (services.size()>0) {
            goods.add(new GoodOfBill("", "Услуги", SERVICES));

            for (ServiceOfBill serviceOfBill : services) {
                goods.add(new GoodOfBill(serviceOfBill.getNumber(), serviceOfBill.getName(), SERVICE));
            }
        }
        
    }
}
