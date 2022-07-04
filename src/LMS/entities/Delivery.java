package LMS.entities;

import java.sql.Time;
import java.util.HashSet;
import java.util.Set;

import LMS.provided.*;

/**
 * A delivery within the Logistics Management System.<br>
 * <br>
 * 
 * A delivery collects goods at a certain time and place and after transport
 * delivers them at another time and place. A delivery is transported by a
 * specific carrier and identified by a unique id.<br>
 * <br>
 * 
 * The usual life cycle is
 * <ul>
 * <li>create a delivery with id, pick up and drop off location</li>
 * <li>add goods</li>
 * <li>assign a carrier</li>
 * <li>collect</li>
 * <li>deliver</li>
 * </ul>
 *
 */
public abstract class Delivery {

	private long id;
	private String from;
	private String to;
	private DateTime collected;
	private DateTime delivered;
	private Vehicle carrier;
	private java.util.Set<Item> goods = new HashSet<>();


	public Delivery(long id, String from, String to){
		if(id <= 0)
			throw new IllegalArgumentException();
		this.from = ensureNonNullNonEmpty(from);
		this.id = ensureGreaterZero(id);
		this.to = ensureNonNullNonEmpty(to);
	}

	private final String ensureNonNullNonEmpty(String str){
		if(str == null || str.isEmpty())
			throw new IllegalArgumentException();
		return str;
	}

	public long ensureGreaterZero(long id){
		if(id <= 0)
			throw new IllegalArgumentException();
		return id;
	}

	public abstract int getTotal();

	protected final float totalMass(){
		if(goods.size() == 0)
			return -1;

		float mass = 0;
		for(Item it : goods){
			mass += it.totalMass();
		}
		return mass;
	}

	public boolean isCollected(){
		return collected != null;
	}

	public boolean isDelivered(){
		return delivered != null;
	}

	private boolean isAssigned(){
		return carrier != null;
	}

	public final boolean addGoods(Item item){
		if (isAssigned() || isDelivered() || isCollected())
			return false;
		return goods.add(item);
	}

	public final boolean addGoods(Iterable<Item> items){
		if (isAssigned() || isDelivered() || isCollected())
			return false;
		boolean added = false;
		for (Item it : items){
			if(goods.add(it));
				added = true;
		}
			return added;
	}

	public java.util.Set<Item> getGoods(){
		Set<Item> copy = new HashSet<>();
		for(Item it : goods) {
			copy.add(it);
		}
		return copy;
	}

	public final void assignCarrier(Vehicle v){
		if(v != null && v.getMaxLoad() >= totalMass())
			carrier = v;
	}

	public final boolean collect(DateTime toc){
		if(carrier == null || isCollected() || goods.size() < 1)
			return false;

		collected = new DateTime(toc);
		return true;
	}

	public final boolean deliver(DateTime tod){
		if(isDelivered() || !isCollected())
			return false;

		delivered = new DateTime(tod);
		return true;
	}

	public int compareTo(Delivery arg0){
		return Long.compare(this.id, arg0.id);
	}

	/**
	 * creates a string representation of this delivery.<br>
	 * 
	 * @ProgrammingProblem.Hint provided
	 * 
	 */
	@Override
	public String toString() {
		return String.format(
				"%d from \"%10.10s\" to \"%10.10s\" " + "[%scollected, %sdelivered] (%.2fkg, EUR %.2f)\n" + "%s", id,
				from, to, isCollected() ? "" : "not ", isDelivered() ? "" : "not ", totalMass(), getTotal() / 100.,
				goods == null ? "no stock" : goods);
	}

}
