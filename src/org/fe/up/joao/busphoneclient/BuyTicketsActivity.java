package org.fe.up.joao.busphoneclient;

import org.fe.up.joao.busphoneclient.helper.ComService;
import org.fe.up.joao.busphoneclient.helper.JSONHelper;
import org.fe.up.joao.busphoneclient.model.Ticket;
import org.fe.up.joao.busphoneclient.model.User;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This activity presents the user with
 * options to buy tickets of multiple types, while
 * showing the account balance and total
 * cost of the tickets to buy.
 * @author joao
 *
 */
public class BuyTicketsActivity  extends Activity {
	
	private int t1_amount = 0;
	private int t2_amount = 0;
	private int t3_amount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		String url = "info";
		if (Ticket.prices[0] == -1) {
			new ComService(url, this, "getPricesHandler", true);
		} else {
			updatePrices(Ticket.prices[0], Ticket.prices[1], Ticket.prices[2]);
		}
	}
	
	/**
	 * Callback function for getPrices
	 */
	public void getPricesHandler(String jsonString) {
		
		JSONObject json = JSONHelper.string2JSON(jsonString);
		Log.v("MyLog", jsonString);
		try {
			double t1Price = Double.valueOf(json.getString("t1"));
			double t2Price = Double.valueOf(json.getString("t2"));
			double t3Price = Double.valueOf(json.getString("t3"));
			
			Ticket.prices[0] = t1Price;
			Ticket.prices[1] = t2Price;
			Ticket.prices[2] = t3Price;
			
			updatePrices(t1Price, t2Price, t3Price);
			
		} catch (Exception e) {
			Log.v("MyLog", e.getMessage());
			Toast.makeText(this, "Connection failed (invalid response)", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Update prices on screen
	 * @param t1Price
	 * @param t2Price
	 * @param t3Price
	 */
	private void updatePrices(double t1Price, double t2Price, double t3Price) {
		setContentView(R.layout.activity_buy_tickets);
		
		((TextView) findViewById(R.id.t1_price)).setText("x " + t1Price + getString(R.string.euros));
		((TextView) findViewById(R.id.t2_price)).setText("x " + t2Price + getString(R.string.euros));
		((TextView) findViewById(R.id.t3_price)).setText("x " + t3Price + getString(R.string.euros));
		
		Log.v("MyLog", "Prices updated: " + Ticket.prices[0] + "; " + Ticket.prices[1] + "; " + Ticket.prices[2]);
		
		((NumberPicker) findViewById(R.id.t1_number_picker)).setMaxValue(10);
		((NumberPicker) findViewById(R.id.t2_number_picker)).setMaxValue(10);
		((NumberPicker) findViewById(R.id.t3_number_picker)).setMaxValue(10);
		
		
		((NumberPicker) findViewById(R.id.t1_number_picker)).setOnScrollListener(new NumberPicker.OnScrollListener() {
	        @Override
	        public void onScrollStateChange(NumberPicker numberPicker, int scrollState) {
	            if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
	            	t1_amount = numberPicker.getValue();
	            	updateTotal(t1_amount, Ticket.prices[0], R.id.t1_price_total);
	            }
	        }
	    });
		((NumberPicker) findViewById(R.id.t2_number_picker)).setOnScrollListener(new NumberPicker.OnScrollListener() {
	        @Override
	        public void onScrollStateChange(NumberPicker numberPicker, int scrollState) {
	            if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
	            	t2_amount = numberPicker.getValue();
	            	updateTotal(t2_amount, Ticket.prices[1], R.id.t2_price_total);
	            }
	        }
	    });
		((NumberPicker) findViewById(R.id.t3_number_picker)).setOnScrollListener(new NumberPicker.OnScrollListener() {
	        @Override
	        public void onScrollStateChange(NumberPicker numberPicker, int scrollState) {
	            if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
	            	t3_amount = numberPicker.getValue();
	                updateTotal(t3_amount, Ticket.prices[2], R.id.t3_price_total);
	            }
	        }
	    });
		findViewById(R.id.button_buy_confirm).setEnabled(false);
	}

	private void updateTotal(int amount, double price, int priceLabelID) {
		((TextView) findViewById(priceLabelID)).setText(amount*price + getString(R.string.euros));
		
		Double totalPrice = t1_amount*Ticket.prices[0] + t2_amount*Ticket.prices[1] + t3_amount*Ticket.prices[2];
		Log.v("MyLog", "Amounts changed: " + t1_amount + "; " + t2_amount + "; " + t3_amount);
		Log.v("MyLog", "Total changed: " + totalPrice + "€");
		((TextView) findViewById(R.id.total_price_label)).setText(totalPrice + getString(R.string.euros));
		
		if (t1_amount+t2_amount+t3_amount < 1) {
			findViewById(R.id.button_buy_confirm).setEnabled(false);
		} else {
			findViewById(R.id.button_buy_confirm).setEnabled(true);
		}
	}
	
	/**
	 * Buy Button handler
	 */
	public void buyTickets(View v){
		// get 'users/:id/buy/:nt1/:nt2/:nt3/t/:token'
		String url = String.format("users/%s/buy/%d/%d/%d/t/%s", User.getID(), t1_amount, t2_amount, t3_amount, User.getToken());
		new ComService(url, this, "returnFromBuyingTicketsHanlder", true);
	}
	
	/**
	 * Cancel Button handler
	 */
	public void cancelBuyTickets(View v){
		Log.v("MyLog", "Canceled buy tickets");
		this.finish();
	}
	
	/**
	 * The returnFromBuyingTicketsHanlder hanlder
	 */
	public void returnFromBuyingTicketsHanlder(String result) {
		Log.v("MyLog", "Tickets buy result: " + result);
		// Parse the tickets
//		JSONObject json = JSONHelper.string2JSON(result);
//		User.parseTickets(json);
//		User.updateTicketsDB(this);
		this.finish();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			/*NavUtils.navigateUpFromSameTask(this);*/
			this.finish();
			return true;
	
		}
		return super.onOptionsItemSelected(item);
	}
	
	
}
