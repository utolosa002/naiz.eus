package com.naiz.eus;

import com.naiz.eus.R;
import com.naiz.eus.adapter.NavDrawerListAdapter;
import com.naiz.eus.db.DatabaseHandler;
import com.naiz.eus.login.FacebookFragment;
import com.naiz.eus.login.GoogleFragment;
import com.naiz.eus.login.LoginFragment;
import com.naiz.eus.model.Berria;
import com.naiz.eus.model.NavDrawerItem;
import com.naiz.eus.widget.WidgetProvider;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

@SuppressLint("NewApi")//11 api onartzen du, 10 ez
public class MainActivity extends FragmentActivity {
	private DrawerLayout mDrawerLayout;
	public static ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	public static int albisteKop = 0;
	public static ArrayList<NavDrawerItem> navDrawerItems;
	public static NavDrawerListAdapter adapter;
	public static String searchURL="http://www.naiz.eus/";
	public static boolean hasieran;
	public static boolean herrian=false;
	public static String saila;
	public static String unekoHerria="";
	public static int testutamaina=15;
	public DatabaseHandler db = new DatabaseHandler(this);
	private String img;
	Menu menua;
	public static final String MyPREFERENCES = "MyPrefs" ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		/////////////////widget////////////////////////////////
		//TODO- ez da aktualizatzen widgeta hasterakoan??? ezta zerbitzua martxan
		Intent intent = new Intent(this,WidgetProvider.class);
		intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		 int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), WidgetProvider.class));
		// Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
		// since it seems the onUpdate() is only fired on that:
		//int[] ids = {widgetId};
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
		sendBroadcast(intent);
		//////////////////////DB///////////////////////
		try {
			db.createDataBase();
			db.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("gaizki db");
		}catch (SQLiteCantOpenDatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("gaizki db");
		}
		ArrayList<Berria> alb =null;
		try {
			alb = db.getBerriak("naizazala");
		} catch (SQLiteException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int albkop=0;
		if (alb!=null){
			albkop=alb.size();
		}
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1),true, Integer.toString(albkop)));
		//Gara
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1) ));//, true, "50+"));
		//info7
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1) ));//, true, "50+"));
		//Gaur8
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
		//Kazeta
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
		//mediabask
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
		// Eguraldia
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
		// Gogokoak
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons.getResourceId(7, -1)));
		// Blogak
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[8], navMenuIcons.getResourceId(8, -1)));
		// Iritzia
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[9], navMenuIcons.getResourceId(9, -1)));
		// naiz+
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[10], navMenuIcons.getResourceId(10, -1)));
		// Agenda
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[11], navMenuIcons.getResourceId(11, -1)));
		// Sartu
		
		SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		String defValue = "";
		String balue="";
		balue = sharedpreferences.getString("nameKey", defValue);
		//Toast.makeText(this.getApplicationContext(), "balue"+balue+"defvaule"+defValue, Toast.LENGTH_SHORT).show();
		if (balue!=""){
			if(!(navDrawerItems==null||navDrawerItems.size()==0)){
				navDrawerItems.add(new NavDrawerItem(navMenuTitles[12], navMenuIcons.getResourceId(12, -1)));
				navDrawerItems.set(navDrawerItems.size()-1, new NavDrawerItem(balue,  navMenuIcons.getResourceId(12, -1)));
			}
		}else{
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[12], navMenuIcons.getResourceId(12, -1)));
		}	

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, //nav menu toggle icon
				R.string.app_name // nav drawer open - description for accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		menua=menu;
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_login:
				if(db.erabIrudiaBadu()){
					System.out.println(db.erabIrudiaBadu()+" db.erabIrudiaBadu()");
					byte[] b = db.erabIrudiaLortu();
					System.out.println(b+" db.erabIrudiaLortu()");
					Drawable image = new BitmapDrawable(getResources(),BitmapFactory.decodeByteArray(b, 0, b.length));
					menua.findItem(R.id.action_login).setIcon(image);
				}
			AukeratuLoginModua();
			return true;
			
		case R.id.action_textsize_seekbar:
			AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
			LayoutInflater inflater = MainActivity.this.getLayoutInflater();
			View v=inflater.inflate(R.layout.global, null);
			builder2.setView(v).setTitle(R.string.testu_tamaina_tit);
			SeekBar sbetxtsize = (SeekBar)v.findViewById(R.id.textsize_seekbar);
			sbetxtsize.setMax(50);
			sbetxtsize.setProgress(testutamaina);
			sbetxtsize.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
				}
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
				}
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
					testutamaina=progress;
					if (AlbisteBatFragment.Berriatxt!=null){
						WebSettings settings = AlbisteBatFragment.Berriatxt.getSettings();
		            	settings.setDefaultFontSize(MainActivity.testutamaina);
		            }
		            if(BlogBatFragment.postTxt!=null){
		            	WebSettings settings = BlogBatFragment.postTxt.getSettings();
		            	settings.setDefaultFontSize(MainActivity.testutamaina);
		            }
		        }
			});
			builder2.show();
			return true;
		case R.id.action_add_favorites:
			db.changeFavHerria(unekoHerria);
			if(db.isFav(unekoHerria)){
				System.out.println(unekoHerria+"db.isFav(unekoHerria)"+db.isFav(unekoHerria));
				menua.findItem(R.id.action_add_favorites).setIcon(R.drawable.ic_action_important);
			}else{
				System.out.println(unekoHerria+"db.isnotFav(unekoHerria)"+db.isFav(unekoHerria));
				menua.findItem(R.id.action_add_favorites).setIcon(R.drawable.ic_action_not_important);
			}
			return true;
		case R.id.action_refresh:
			DatabaseHandler db = new DatabaseHandler(this);
			try {
				db.createDataBase();
				db.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.out.println("gaizki db");
			}
			try {
				db.setEguneratzeData("2015-01-01 00.00",saila,MainActivity.albisteKop);
			} catch (SQLiteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			AlbisteFragment fragment = new AlbisteFragment(saila, searchURL,img);
			if (saila=="naizazala"){
        	hasieran=true;
        	}
        	if (fragment != null) {
				FragmentManager fragmentManager = getSupportFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
			}
				return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void AukeratuLoginModua() {
		// TODO Auto-generated method stub
		SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		String defValue = "";
		String balue="";
		balue = sharedpreferences.getString("nameKey", defValue);
		if (balue==""){
		CharSequence login[] = new CharSequence[] {"naiz:", "Google", "Facebook", "Erregistratu"};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Hasi saioa:");
		builder.setItems(login, new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		    	if (which==0){
//			    	String link6 = "http://www.naiz.eus/eu/suscripcion/entrar";
//					WebViewFragment fragment = new WebViewFragment(link6);
					LoginFragment fragment = new LoginFragment();
			    	FragmentManager fragmentManager = getSupportFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
		    	}else if (which==1){
					GoogleFragment fragment = new GoogleFragment();
				    FragmentManager fragmentManager = getSupportFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
				}else if (which==2){
					FacebookFragment fragment = new FacebookFragment();
				    FragmentManager fragmentManager = getSupportFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
				}else{
			    	String link6 = "https://www.naiz.eus/eu/suscripcion/registro";
					WebViewFragment fragment = new WebViewFragment(link6);
					//ErregistratuFragment fragment = new ErregistratuFragment();
					FragmentManager fragmentManager = getSupportFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
				}
			}
		});
		builder.show();
		}else{
			LoginFragment fragment = new LoginFragment();
	    	FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_login).setVisible(!drawerOpen);
		if(db.erabIrudiaBadu()){
			byte[] b = db.erabIrudiaLortu();
			Drawable image = new BitmapDrawable(getResources(),BitmapFactory.decodeByteArray(b, 0, b.length));
			menua.findItem(R.id.action_login).setIcon(image);
//		}else{
//			menua.findItem(R.id.action_login).setIcon(R.drawable);
		}
		menu.findItem(R.id.action_textsize_seekbar).setVisible(!drawerOpen);
		menu.findItem(R.id.action_textsize_seekbar).setVisible(!herrian);
		menu.findItem(R.id.action_add_favorites).setVisible(herrian);
		if(db.isFav(unekoHerria)){
			System.out.println(unekoHerria+"db1.isFav(unekoHerria)"+db.isFav(unekoHerria));
			menua.findItem(R.id.action_add_favorites).setIcon(R.drawable.ic_action_important);
		}else{
			System.out.println(unekoHerria+"db1.isnotFav(unekoHerria)"+db.isFav(unekoHerria));
			menua.findItem(R.id.action_add_favorites).setIcon(R.drawable.ic_action_not_important);
		}
		menu.findItem(R.id.action_refresh).setVisible(!drawerOpen);
		menu.findItem(R.id.action_refresh).setVisible(hasieran);
		menu.findItem(R.id.action_refresh).setVisible(!herrian);
	//	menu.findItem(R.menu.main).setVisible(false);
		return super.onPrepareOptionsMenu(menu);

	
	}
	@Override
	public void onBackPressed() {
		SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		String defValue = "";
		String balue="";
		balue = sharedpreferences.getString("nameKey", defValue);
		//Toast.makeText(this.getApplicationContext(), "balue"+balue+"defvaule"+defValue, Toast.LENGTH_SHORT).show();
		if (balue!=""){
			if(!(navDrawerItems==null||navDrawerItems.size()==0)){
				navDrawerItems.set(navDrawerItems.size()-1, new NavDrawerItem(balue,  R.drawable.ic_s));
				
			}
		}else{
			navDrawerItems.set(navDrawerItems.size()-1, new NavDrawerItem("Sartu",  R.drawable.ic_s));
		}	

		adapter.notifyDataSetChanged();
        mDrawerList.setAdapter(adapter);
		if(!hasieran){
			Fragment fragment;
			if(herrian){
				fragment = new EguraldiaFragment();
				hasieran=false;
			}else{
				fragment = new NaizFragment();
				hasieran=true;
			}
			if (fragment != null) {
				herrian=false;
				FragmentManager fragmentManager = getSupportFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
			}
		}else{
			 finish();
		}
	}
	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		hasieran=false;
		switch (position) {
		case 0:
//	        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//	        	fragment = new LoliAlbisteFragment();
//	        	hasieran=true;
//			}else{
			saila="naizazala";
        	hasieran=true;
			herrian = false;
        	img="http://www.naiz.eus/images/logo.png";
        	fragment = new NaizFragment();
        	break;

		case 1:
			saila="gara";
        	searchURL="http://www.naiz.eus/hemeroteca/gara";
        	img="http://www.naiz.eus/media/asset_publics/resources/000/037/726/original/GARA_EUS.png";
        	herrian = false;
        	fragment = new GaraFragment();
		break;
		case 2:
			saila="i7";
        	searchURL="http://info7.naiz.eus/";
        	img="http://info7.naiz.eus/media/asset_publics/resources/000/184/514/original/logo_luzea.png";
        	herrian = false;
        	fragment = new Info7Fragment();
		break;
		case 3:
			saila="g8";
        	searchURL="http://www.naiz.eus/hemeroteca/gaur8";
        	img="http://www.naiz.eus/media/asset_publics/resources/000/135/174/original/G8.png";
        	herrian = false;
        	fragment = new Gaur8Fragment();
		break;
		case 4:
			saila="kazeta";
        	searchURL="http://kazeta.naiz.eus/eu";
        	img="http://kazeta.naiz.eus/media/asset_publics/resources/000/135/205/original/kazeta.png";
        	herrian = false;
        	fragment = new KazetaFragment();
		break;
		case 5:
			saila="mediabask";
        	img="http://mediabask.naiz.eus/media/asset_publics/resources/000/135/808/original/logo_mediabask.png";
        	searchURL="http://mediabask.naiz.eus/eu";
        	herrian = false;
        	fragment = new MediaBaskFragment();
		break;
		case 6:
			herrian = false;
        	fragment = new EguraldiaFragment();
			break;
		case 7:
			herrian = false;
        	fragment = new FavFragment();
			break;
		case 8:
			herrian = false;
        	fragment = new BlogakFragment("http://www.naiz.eus/eu/iritzia/blogs");
			break;
		case 9:
			String link1 = "http://www.naiz.eus/eu/iritzia";
			herrian = false;
        	fragment = new IritziaFragment(link1);
			break;
		case 10:
			String link2 = "http://www.naiz.eus/eu/naizplus";
			herrian = false;
        	fragment = new WebViewFragment(link2);
			break;
		case 11:
			String link3 = "http://www.naiz.eus/eu/agenda";
			herrian = false;
        	fragment = new WebViewFragment(link3);
			break;
		case 12:
//			String link6 = "http://www.naiz.eus/eu/suscripcion/entrar";
			//fragment = new WebViewFragment(link6);
//        	fragment = new HarpidetzaFragment();
			herrian = false;
			AukeratuLoginModua();
			break;
		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	class ThreadClass extends Thread {
		MainActivity   cl;
		  public ThreadClass(MainActivity mainActivity){
		       this.cl = mainActivity;
		    }
		public void run() {
    		Document doc = null;
    		try {
    			int i=0;
    			while (i<5 && doc==null){
    			doc = Jsoup.connect(searchURL).get();
    			i++;
    			}
    		
    		// Connect to the web site <div id="login_form
    		Elements izenburua = doc.select("div[id^=login_form]");
//    		System.out.println("izenb:"+izenburua.size());
    		if(!izenburua.isEmpty()){
    			System.out.println("Sartu mainActi 279");
    		//	 Toast.makeText(getApplicationContext(),"Sartu mainActi 279", Toast.LENGTH_LONG).show();
    			if(!(navDrawerItems==null||navDrawerItems.size()==0)){
    				navDrawerItems.set(navDrawerItems.size()-1, new NavDrawerItem("Sartu2", R.drawable.ic_s));//navMenuIcons.getResourceId(4, -1)));
    			}
    		}else{
    		//	 Toast.makeText(this,"Sartu mainActi 282", Toast.LENGTH_LONG).show();
    			System.out.println("Sartu mainActi 282");
//    			System.out.println("izenb2:"+izenburua.size());
//    			System.out.println("izenb3:"+doc.select("input[class*=check-username-avaibility]").size());
    			String loginName=doc.select("input[class*=check-username-avaibility]").first().attr("value");
    			if(!(navDrawerItems==null||navDrawerItems.size()==0)){
    				navDrawerItems.set(navDrawerItems.size()-1, new NavDrawerItem(loginName,  R.drawable.ic_s));
    			}
    		}
    		} catch (IOException e1) {
    			e1.printStackTrace();
    		}
            
              System.out.println("linka: ");
            
        }
 	}
	public static Bitmap getBitmapFromURL(String src) {
	    try {
	        URL url = new URL(src);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);
	        return myBitmap;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
}
