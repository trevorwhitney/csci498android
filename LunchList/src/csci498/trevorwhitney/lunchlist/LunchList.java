package csci498.trevorwhitney.lunchlist;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class LunchList extends Activity {
	
	List<Restaurant> restaurants = new ArrayList<Restaurant>();
	RestaurantAdapter adapter = null;
	EditText name = null;
	EditText address = null;
	RadioGroup types = null;
	ViewFlipper flipper = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.activity_lunch_list);
	  
	  name = (EditText)findViewById(R.id.name);
	  address = (EditText)findViewById(R.id.address);
	  types = (RadioGroup)findViewById(R.id.types);
	  flipper = (ViewFlipper)findViewById(R.id.flipper);
	  
	  Button save = (Button)findViewById(R.id.save_btn);
	  save.setOnClickListener(onSave);
	  
	  Button flipToDetails = (Button)findViewById(R.id.view_details);
	  flipToDetails.setOnClickListener(onFlipClick);
	  
	  Button flipToList = (Button)findViewById(R.id.view_list);
	  flipToList.setOnClickListener(onFlipClick);
	  
	  
	  ListView list = (ListView)findViewById(R.id.restaurant_list);
	  adapter = new RestaurantAdapter();
	  list.setAdapter(adapter);
	  list.setOnItemClickListener(onListClick);
	}

	private View.OnClickListener onSave = new View.OnClickListener() {	
		public void onClick(View v) {
    	Restaurant restaurant = new Restaurant();
			restaurant.setName(name.getText().toString());
			restaurant.setAddress(address.getText().toString());

			switch (types.getCheckedRadioButtonId()) {
			case R.id.type_in:
				restaurant.setType("dine_in");
				break;
			
			case R.id.type_out:
				restaurant.setType("take_out");
				break;
				
			case R.id.type_del:
				restaurant.setType("delivery");
				break;
			}
			
			adapter.add(restaurant);
			
			//clear form for next entry
			name.setText("");
			address.setText("");
			types.check(R.id.type_out);
		}
	};
	
	private View.OnClickListener onFlipClick = new View.OnClickListener() {
		public void onClick(View v) {
			Button flipToDetails = (Button)findViewById(R.id.view_details);
			
			if (v == flipToDetails) {
				flipper.showNext();
			}
			else {
				flipper.showPrevious();
			}
		}
	};
	
	private AdapterView.OnItemClickListener onListClick = new
			AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent,
						View view, int position, long id) {
					Restaurant restaurant = restaurants.get(position);
					name.setText(restaurant.getName());
					address.setText(restaurant.getAddress());
					
					if (restaurant.getType().equals("dine_in")) {
						types.check(R.id.type_in);
					}
					else if (restaurant.getType().equals("take_out")) {
						types.check(R.id.type_out);
					}
					else {
						types.check(R.id.type_del);
					}
					
					flipper.showNext();
				}
			};
	
	class RestaurantAdapter extends ArrayAdapter<Restaurant> {
		
		RestaurantAdapter() {
			super(LunchList.this,
					android.R.layout.simple_list_item_1,
					restaurants);
		}
		
		public View getView(int position, View convertView,
				ViewGroup parent) {
			View row = convertView;
			RestaurantHolder holder = null;
			
			if (row == null) {
				LayoutInflater inflater = getLayoutInflater();
				row = inflater.inflate(R.layout.restuarant_row, null);
				holder = new RestaurantHolder(row);
				row.setTag(holder);
			}
			else {
				holder = (RestaurantHolder)row.getTag();
			}
			
			holder.populateList(restaurants.get(position));
			
			return row;
		}
		
	}
	
	static class RestaurantHolder {
		private TextView name = null;
		private TextView address = null;
		private ImageView icon = null;
		
		RestaurantHolder(View row) {
			name = (TextView)row.findViewById(R.id.name);
			address = (TextView)row.findViewById(R.id.address);
			icon = (ImageView)row.findViewById(R.id.icon);
		}
		
		void populateList(Restaurant restaurant) {
			name.setText(restaurant.getName());
			address.setText(restaurant.getAddress());
			
			if (restaurant.getType().equals("dine_in")) {
				icon.setImageResource(R.drawable.ball_red);
			}
			else if (restaurant.getType().equals("take_out")) {
				icon.setImageResource(R.drawable.ball_yellow);
			}
			else {
				icon.setImageResource(R.drawable.ball_green);
			}
		}
	}

}
