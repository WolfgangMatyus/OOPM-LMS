package LMS.entities;

public class InterationalDelivery extends Delivery {

    private float custom;

    public InterationalDelivery(long id, String from, String to) {
        super(id, from, to);
    }

    public InterationalDelivery(long id, String from, String to, float custom) {
        super(id, from, to);
        this.custom = custom;
    }

    @Override
    public int getTotal() {
        if(getGoods().size() < 1)
            return -1;

        int total = 0;
        for(Item it : getGoods()) {
            total += it.totalValue();
        }
        return (int)(total * custom);
    }
}
