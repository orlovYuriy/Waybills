package ru.ospin.waybills;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by Yuriy on 03.01.2017.
 */

public class ServiceOfBill {

    private String number;
    private String name;
    private String status;

    public String getNumber() {
        if (number==null)
            return "";
        try {

            return URLDecoder.decode(number,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  number;
    }

    public String getName() {
        if (name==null)
            return "";
        try {
            return URLDecoder.decode(name,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  name;
    }

    public String getStatus() {
        return status;
    }
}
