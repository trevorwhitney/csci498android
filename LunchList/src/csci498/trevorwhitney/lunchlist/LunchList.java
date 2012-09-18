package csci498.trevorwhitney.lunchlist;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.TabActivity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class LunchList extends TabActivity {
	
	List<Restaurant> restaurants = new ArrayList<Restaurant>();
	RestaurantAdapter adapter = null;
	Restaurant current = null;
	EditText name = null;
	EditText address = null;
	EditText notes = null;
	RadioGroup types = null;
	RestaurantHelper helper = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.activity_lunch_list);
	  helper = new RestaurantHelper(this);
	  
	  name = (EditText)findViewById(R.id.name);
	  address = (EditText)findViewById(R.id.address);
	  notes = (EditText)findViewById(R.id.notes);
	  types = (RadioGroup)findViewById(R.id.types);
	  
	  Button save = (Button)findViewById(R.id.save_btn);
	  save.setOnClickListener(onSave);
	  
	  ListView list = (ListView)findViewById(R.id.restaurants_list);
	  adapter = new RestaurantAdapter();
	  list.setAdapter(adapter);
	  list.setOnItemClickListener(onListClick);
	  
	  TabHost.TabSpec spec = getTabHost().newTabSpec("tag1");
	  spec.setContent(R.id.restaurants_list);
	  spec.setIndicator("List", getResources().getDrawable(
	  		R.drawable.list));
	  getTabHost().addTab(spec);
	  
	  spec = getTabHost().newTabSpec("tag2");
	  spec.setContent(R.id.details_form);
	  spec.setIndicator("Details", getResources().getDrawable(
	  		R.drawable.restaurant));
	  getTabHost().addTab(spec);
	  
	  getTabHost().setCurrentTab(0);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		helper.close();
	}
	
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
	}

	private View.OnClickListener onSave = new View.OnClickListener() {	
		public void onClick(View v) {
    	String type = null;

			switch (types.getCheckedRadioButtonId()) {
			case R.id.type_in:
				type = "dine_in";
				break;
			
			case R.id.type_out:
				type = "take_out";
				break;
				
			case R.id.type_del:
				type = "delivery";
				break;
			}
			
			helper.insert(name.getText().toString(), 
					address.getText().toString(), type,
					notes.getText().toString());
			
			//clear form for next entry
			name.setText("");
			address.setText("");
			types.check(R.id.type_out);
		}
	};
	
	private AdapterView.OnItemClickListener onListClick = new
			AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent,
						View view, int position, long id) {
					current = restaurants.get(position);
					name.setText(current.getName());
					address.setText(current.getAddress());
					notes.setText(current.getNotes());
					
					if (current.getType().equals("dine_in")) {
						types.check(R.id.type_in);
					}
					else if (current.getType().equals("take_out")) {
						types.check(R.id.type_out);
					}
					else {
						types.check(R.id.type_del);
					}
					
					getTabHost().setCurrentTab(1);
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
