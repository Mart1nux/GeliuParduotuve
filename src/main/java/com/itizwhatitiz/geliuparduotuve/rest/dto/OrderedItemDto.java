package com.itizwhatitiz.geliuparduotuve.rest.dto;

public class OrderedItemDto extends GenericDto {

    private Integer orderId;

    private Integer itemId;

    private Integer amount;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer customerId) {
        this.orderId = customerId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
