package lk.petalpink.petalpink.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConfigService {

    @Value("${delivery.fee}")
    private double deliveryFee;

    @Value("${delivery.add_cost_per_1kg}")
    private double addCostPer1Kg;

    @Value("${payment.tier.0to5000}")
    private int tier0To5000;

    @Value("${payment.tier.5001to10000}")
    private int tier5001To10000;

    @Value("${payment.tier.10001to20000}")
    private int tier10001To20000;

    @Value("${payment.tier.20001to50000}")
    private int tier20001To50000;

    @Value("${is_print}")
    private int isPrint;

    @Value("${auto_generate_id}")
    private int autoGenerateId;

    public int getAutoGenerateId() {
        return autoGenerateId;
    }

    public int getIsPrint() {
        return isPrint;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public double getAddCostPer1Kg() {
        return addCostPer1Kg;
    }

    public int getPaymentTierRate(double orderAmount) {
        if (orderAmount <= 5000) return tier0To5000;
        else if (orderAmount <= 10000) return tier5001To10000;
        else if (orderAmount <= 20000) return tier10001To20000;
        else return tier20001To50000;
    }

    // Individual tier getters
    public int getTier0To5000() { return tier0To5000; }
    public int getTier5001To10000() { return tier5001To10000; }
    public int getTier10001To20000() { return tier10001To20000; }
    public int getTier20001To50000() { return tier20001To50000; }

    @Value("${courier_bags_subcategory}")
    private int courierBagsSubcategory;

    public int getCourierBagsSubcategory() {
        return courierBagsSubcategory;
    }

    @Value("${is_show_courier_bags}")
    private int isShowCourierBags;

    public int getIsShowCourierBags() {
        return isShowCourierBags;
    }
}