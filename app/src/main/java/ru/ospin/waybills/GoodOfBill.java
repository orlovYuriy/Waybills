package ru.ospin.waybills;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by Yuriy on 15.12.2016.
 */

public class GoodOfBill {

    private String number;
    private String name;
    private String unit;
    private String count;

    public GoodOfBill(String number, String name, String unit, String count) {
        this.number = number;
        this.name = name;
        this.unit = unit;
        this.count = count;
    }

    public GoodOfBill(String number, String name, String unit) {
        this.number = number;
        this.name = name;
        this.unit = unit;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        if (name==null)
            return "";
        try {
            return URLDecoder.decode(name,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return  name;
    }

    public String getUnit() {
        if (unit==null)
            return "";
        try {
            return URLDecoder.decode(unit,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  unit;
    }

    public String getCount() {
        if (count==null)
            return "";
        try {
            return URLDecoder.decode(count,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  count;
    }
}
