/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.naiz.eus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.naiz.eus.db.DatabaseHandler;
import com.naiz.eus.model.Berria;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A fragment representing a single step in a wizard. The fragment shows a dummy
 * title indicating the page number, along with some dummy text.
 *
 * <p>
 * This class is used by the {@link CardFlipActivity} and
 * {@link ScreenSlideActivity} samples.
 * </p>
 */
@SuppressLint("NewApi")
public class ScreenSlidePageFragment extends Fragment {
	/**
	 * The argument key for the page number this fragment represents.
	 */
	  private Animator mCurrentAnimator;
	  private int mShortAnimationDuration;
	public static final String ARG_PAGE = "page";
	private static final String Linkak = "links";
	private String searchURL;
	private Berria b = new Berria();
	public WebView Berriatxt;
	public WebView Iruzkinatxt;
	public ImageView Irudia;

	/**
	 * The fragment's page number, which is set to the argument value for
	 * {@link #ARG_PAGE}.
	 */
	private int mPageNumber;
	// private int uneko;
	private ArrayList<String> searchURLak;
	private DatabaseHandler db;
    public ScreenSlidePageFragment newInstance(int someInt, ArrayList<String> someTitle) {
    	ScreenSlidePageFragment fragmentDemo = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, someInt);
        args.putStringArrayList(Linkak, someTitle);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }
	/**
	 * Factory method for this fragment class. Constructs a new fragment for the
	 * given page number.
	 */
	public static ScreenSlidePageFragment create(int pageNumber,
			ArrayList<String> Links) {
		ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_PAGE, pageNumber);

		System.out.println("SlideSlidePageFrag - create:pageNumber="+ pageNumber);
		System.out.println("SlideSlidePageFrag - create:links=" + Links.size());

		args.putStringArrayList(Linkak, Links);
		fragment.setArguments(args);
		return fragment;
	}

	public ScreenSlidePageFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPageNumber = getArguments().getInt(ARG_PAGE);
		// uneko = getArguments().getInt("uneko");

		System.out.println("SlideSlidePageFrag - onCreate:mPageNumber ="
				+ mPageNumber);
		// System.out.println("SlideSlidePageFrag - onCreate:uneko ="+uneko);
		System.out.println("SlideSlidePageFrag - onCreate:links="
				+ getArguments().getStringArrayList(Linkak).size());

		searchURLak = getArguments().getStringArrayList(Linkak);
		System.out.println("SlideSlidePageFrag - oncreate: searchURLak ="
				+ searchURLak.size());

		if (mPageNumber < searchURLak.size()) {
			searchURL = searchURLak.get(mPageNumber);
			System.out.println("SlideSlidePageFrag - oncreate: searchURL ="
					+ searchURL);
		} else {
			searchURL = searchURLak.get(searchURLak.size() - 1);
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout containing a title and body text.
		final ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.fragment_screen_slide_page, container, false);
		// searchURL = searchURLak.get(mPageNumber);
		// // Set the title view to show the page number.
		// ((TextView) rootView.findViewById(android.R.id.text1)).setText(
		// getString(R.string.title_template_step, mPageNumber + 1));
		db = new DatabaseHandler(getActivity());
		try {
			db.createDataBase();
			db.close();
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println("gaizki db");
		}

		final TextView Saila = (TextView) rootView.findViewById(R.id.textSaila);
		final TextView Azpitit = (TextView) rootView
				.findViewById(R.id.textAzpitit);
		Berriatxt = (WebView) rootView.findViewById(R.id.berriatxt);
		Iruzkinatxt = (WebView) rootView.findViewById(R.id.iruzkinatxt);
		final TextView ExtraInfo = (TextView) rootView
				.findViewById(R.id.textnaiz);
		final TextView Titularra = (TextView) rootView
				.findViewById(R.id.textTitularra);
		Irudia = (ImageView) rootView
		.findViewById(R.id.berrirudia);
		
	

		ThreadClass thread = new ThreadClass(this);
		thread.start();
		// wait for thread to finish
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Irudia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomImageFromThumb(Irudia, b.getImage(),rootView);
            }
        });
		if (b != null) {
			Saila.setText(b.getSaila());
			Azpitit.setText(b.getSubtitle());
			Titularra.setText(b.getTitle());
			ExtraInfo.setText(b.getExtraInfo());
			//System.out.println("b.getBerria()0= "+b.getBerria());

			Berriatxt.loadDataWithBaseURL(null, b.getBerria(), "text/html", "utf-8",null);
			Berriatxt.setBackgroundColor(Color.TRANSPARENT);
			Iruzkinatxt.loadDataWithBaseURL(null, b.getIruzkina(), "text/html", "utf-8",null);
			Iruzkinatxt.setBackgroundColor(Color.TRANSPARENT);
			if (b.getBerria()==null||b.getBerria()==""||b.getBerria()==" "){
				System.out.println("b.getBerria() 1= "+b.getBerria().toString());
				AsinkTask thread2 = new AsinkTask();
				URL url = null;
				thread2.execute(url);
			}else{
				WebSettings settings = Berriatxt.getSettings();
				settings.setSupportZoom(false);
				settings.setBuiltInZoomControls(false);
				settings.setDefaultTextEncodingName("utf-8");
				settings.setDefaultFontSize(ScreenSlideActivity.testutamaina);
				Berriatxt.getSettings().setJavaScriptEnabled(true);
				System.out.println("badago getBerria()2 ");
				// Berria.setText(b.getBerria());
				Irudia.setImageBitmap(b.getImage());
			}
			if (b.getImage()==null){
				Irudia.setVisibility(View.GONE);
				System.out.println("Gone");
			}
		}
		return rootView;
	}

	/**
	 * Returns the page number represented by this fragment object.
	 */
	public int getPageNumber() {
		return mPageNumber;
	}

	private class AsinkTask extends AsyncTask<URL, Integer, Long> {

		@Override
		protected void onPostExecute(Long result) {
			super.onPostExecute(result);
			if (ScreenSlidePageFragment.this.getActivity() == null)
				return;
			WebSettings settings = Berriatxt.getSettings();
			settings.setSupportZoom(false);
			settings.setBuiltInZoomControls(false);
			settings.setDefaultTextEncodingName("utf-8");
			settings.setDefaultFontSize(ScreenSlideActivity.testutamaina);
			Berriatxt.getSettings().setJavaScriptEnabled(true);
			Berriatxt.setBackgroundColor(Color.TRANSPARENT);
			// Berriatxt.setWebViewClient(new WebViewClient());
			//Berriatxt.setWebChromeClient(new WebChromeClient());
			
//			WebViewClient mWebClient = new WebViewClient(){
//
//		        @Override
//		        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//		            if(url.startsWith("http://www.naiz.eus")){
//		            	ScreenSlidePageFragment fragmentDemo = new ScreenSlidePageFragment();
//		            	ArrayList<String> a = new ArrayList<String>();
//		            	a.add(url);
//
////		            	Activity ac=super.getClass()getParentFragment();
////				  		  FragmentManager fragmentManager = getActivity().getFragmentManager();
////				        	 fragmentManager.beginTransaction()
////				  		     .replace(R.id.pager, fragmentDemo.newInstance(1, a))
////				  		     .commit();
//
//		                return true;
//		            }
//
//		                else{
//		                    view.loadUrl(url);
//		                }
//		                return true;
//		            }
//		       };
//		       Berriatxt.setWebViewClient(mWebClient);
			
			if (b.getBerria() != null) {
				b.setBerria(b.getBerria().replaceAll("href=\"/", "href=\"http://www.naiz.eus/"));
				b.setBerria(b.getBerria().replaceAll("src=\"/", "width=\"100%\" height=\"auto\" src=\"http://www.naiz.eus/"));
			} else {
				b.setBerria("");
			}
			// TODO INTENT BERRIA SORTU BERRIAREKIN
			String html = "<html><body style='text-align:justify;'>"+ b.getBerria() + "</body></html>";

			if (ScreenSlidePageFragment.this.isVisible()) {
			Berriatxt.loadDataWithBaseURL(null, html, "text/html", "utf-8",null);
			// Berria.setText(b.getBerria());
			Irudia.setImageBitmap(b.getImage());
			}
			////IRUZKIN zatia
//			if (b.getIruzkina() != null) {
//				b.setIruzkina(b.getIruzkina().replaceAll("href=\"/", "href=\"http://www.naiz.eus/"));
//				b.setIruzkina(b.getIruzkina().replaceAll("src=\"/", "src=\"http://www.naiz.eus/"));
//			} else {
//				b.setBerria("");
//			}
			// TODO INTENT BERRIA SORTU IRUZKINAREKIN
//			String htmlIruzkina ="<html>"+ b.getIruzkina()+"</html>";
//
//			if (ScreenSlidePageFragment.this.isVisible()) {
//				Iruzkinatxt.loadDataWithBaseURL(null, htmlIruzkina, "text/html", "utf-8",null);
//			}
			//////////////////
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			if (b.getImage()!=null){
				b.getImage().compress(CompressFormat.PNG, 0, stream);
			}
			db.eguneratuBerria(html,stream.toByteArray(),b.getLink());
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
		}

		protected Long doInBackground(URL... urls) {
			Document doc = null;
			System.out.println("searchURL: " + searchURL);
			try {
				int i = 0;
				while (i < 50 && doc == null) {
					doc = Jsoup.connect(searchURL).get();
					i++;
				}
				// Connect to the web site
				if (doc != null) {
					if (b.getTitle() == null) {
						Elements produktu_izenb = doc
								.select("div[class^=title]");
						Elements produktu_desk = doc
								.select("div[class*=abstract]");
						Elements albiste_info = doc
								.select("div[class*=extra-info]");
						Elements albiste_saila = doc
								.select("span[class=section]");
						Elements irudidiv = doc.select("div[class*=big-photo]");
						Elements produktu_irudiak = irudidiv.select("img");
						String text_p_izena = "";
						String html_berria = "";
						if (produktu_izenb.first() != null) {
							if (produktu_izenb.first().select("span").text() == "") {
								// ordainpekoa bada
								text_p_izena = produktu_izenb.get(1).text();
								html_berria = produktu_izenb.first().text();
							} else {
								text_p_izena = produktu_izenb.first().text();
							}
						}
						b.setTitle(text_p_izena);
						String produktu_linka = "";
						String weba = "";
						Elements produktu_linkak = produktu_izenb.select("a");
						if (produktu_linkak.first() != null) {
							produktu_linka = produktu_linkak.first().attr(
									"href");
							if (produktu_linka.length() > 1) {
								if (produktu_linka.startsWith("/")) {
									weba = "http://www.naiz.eus";
								}
							}
							b.setLink(weba + produktu_linka);
						}

						String text_produktu_desk = "";
						if (produktu_desk.first() != null) {
							text_produktu_desk = produktu_desk.first().text();
						}
						b.setSubtitle(text_produktu_desk);
						String produktu_irudia = "";
						if (produktu_irudiak.first() != null) {
							produktu_irudia = produktu_irudiak.first().attr(
									"src");
							if (produktu_irudia.startsWith("/")) {
								produktu_irudia = "http://www.naiz.eus"
										+ produktu_irudia;
							}
							Bitmap bm = MainActivity
									.getBitmapFromURL(produktu_irudia);
							b.setImage(bm);
						}

						String Info = albiste_info.text();
						b.setExtraInfo(Info.replaceAll("\\|", " | "));

						String text_albiste_saila = "";
						if (albiste_saila.first() != null) {
							text_albiste_saila = albiste_saila.first().text();
						}
						b.setSaila(text_albiste_saila);
						String albiste_sail_link = "";
						weba = "";
						Elements albiste_sail_linkE = albiste_saila.select("a");
						if (albiste_sail_linkE.first() != null) {
							albiste_sail_link = albiste_sail_linkE.first()
									.attr("href");
							if (albiste_sail_link.length() > 1) {
								if (albiste_sail_link.startsWith("/")) {
									weba = "http://www.naiz.eus";
								}
							}
							b.setSailLinka(weba + produktu_linka);
						}
					}
					/// <Iruzkina>
/*
					String html_ruzkina = "";

					Element head = doc.head();
						Elements iruzkinDENA = doc.select("div[class*=widget full_article]");
						Elements scriptak = iruzkinDENA.select("script");
						Elements ak = iruzkinDENA.select("a");
						Element a = ak.last();
						if (a!=null){
						System.out.println("iruzkina String:"+head.toString()+"<body>"+a.toString()+scriptak.last().toString()+"</body>");
						html_ruzkina = head+"<body class=\"actualidad-page\"><div id=\"content-section\"><div class=\"container\"><div id=\"main-content\"><div class=\"span-12\"><div class=\"widget full_article\"><a name='comments'></a><script type=\"text/javascript\">"+scriptak.last().html()+"</script></div></div></div></div></div></body>";}
						System.out.println("iruzkina:" + html_ruzkina);
						b.setIruzkina(html_ruzkina);
		*/				
					//// </Iruzkina>
					String html_berria = "";
						Elements berriaNaiz = doc
								.select("div[class*=report-text]");
						Elements berriaGara = doc
								.select("div[class*=col-sm-6 last]");
						Elements berriaElkarriz = doc
								.select("div[class*=new-content]");
						if (berriaNaiz.first() != null) {
							html_berria = berriaNaiz.first().outerHtml();
						} else if (berriaGara.first() != null) {
							html_berria = berriaGara.first().outerHtml();
						} else if (berriaElkarriz.first() != null) {
							html_berria = berriaElkarriz.first().outerHtml();
						}
						//System.out.println("SuSBs aurretik"+html_berria);
						if (html_berria==null||html_berria==""){
			                Elements erosi = doc.select("div[class*=subscription-access]");
			                html_berria = erosi.first().outerHtml();
			                if (html_berria==null||html_berria==""){
								Elements berriabestetan = doc.select("div[class*=span-5 last]");
				                html_berria = berriabestetan.first().outerHtml();
				                if (html_berria==null||html_berria==""){
									Elements berriabesteta = doc.select("div[class*=last]");
					                html_berria = berriabesteta.first().outerHtml();
								}
							}
			    		}
						b.setBerria(html_berria.trim());
					}
				} catch (IOException | NullPointerException e1) {
				e1.printStackTrace();
			}
			return null;
		}

		}
	
	class ThreadClass extends Thread {
		Fragment cl;

		public ThreadClass(Fragment cl) {
			this.cl = cl;
		}

		public void run() {
			Document doc = null;
			System.out.println("searchURL: " + searchURL);
			try {
				lortudbBerria();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void lortudbBerria() throws ParseException {
			try {
				b = db.getBerria(searchURL);
			} catch (SQLiteException | IOException e) {
				e.printStackTrace();
			}

		}
	}

	 private void zoomImageFromThumb(final View thumbView, Bitmap imageResId,ViewGroup rootView) {
	        // If there's an animation in progress, cancel it immediately and proceed with this one.
	        if (mCurrentAnimator != null) {
	            mCurrentAnimator.cancel();
	        }

	        // Load the high-resolution "zoomed-in" image.
	        final ImageView expandedImageView = (ImageView) rootView.findViewById(R.id.expanded_image);
	        expandedImageView.setImageBitmap(imageResId);

	        // Calculate the starting and ending bounds for the zoomed-in image. This step
	        // involves lots of math. Yay, math.
	        final Rect startBounds = new Rect();
	        final Rect finalBounds = new Rect();
	        final Point globalOffset = new Point();

	        // The start bounds are the global visible rectangle of the thumbnail, and the
	        // final bounds are the global visible rectangle of the container view. Also
	        // set the container view's offset as the origin for the bounds, since that's
	        // the origin for the positioning animation properties (X, Y).
	        thumbView.getGlobalVisibleRect(startBounds);
	        rootView.findViewById(R.id.container).getGlobalVisibleRect(finalBounds, globalOffset);
	        startBounds.offset(-globalOffset.x, -globalOffset.y);
	        finalBounds.offset(-globalOffset.x, -globalOffset.y);

	        // Adjust the start bounds to be the same aspect ratio as the final bounds using the
	        // "center crop" technique. This prevents undesirable stretching during the animation.
	        // Also calculate the start scaling factor (the end scaling factor is always 1.0).
	        float startScale;
	        if ((float) finalBounds.width() / finalBounds.height()
	                > (float) startBounds.width() / startBounds.height()) {
	            // Extend start bounds horizontally
	            startScale = (float) startBounds.height() / finalBounds.height();
	            float startWidth = startScale * finalBounds.width();
	            float deltaWidth = (startWidth - startBounds.width()) / 2;
	            startBounds.left -= deltaWidth;
	            startBounds.right += deltaWidth;
	        } else {
	            // Extend start bounds vertically
	            startScale = (float) startBounds.width() / finalBounds.width();
	            float startHeight = startScale * finalBounds.height();
	            float deltaHeight = (startHeight - startBounds.height()) / 2;
	            startBounds.top -= deltaHeight;
	            startBounds.bottom += deltaHeight;
	        }

	        // Hide the thumbnail and show the zoomed-in view. When the animation begins,
	        // it will position the zoomed-in view in the place of the thumbnail.
	        thumbView.setAlpha(0f);
	        expandedImageView.setVisibility(View.VISIBLE);

	        // Set the pivot point for SCALE_X and SCALE_Y transformations to the top-left corner of
	        // the zoomed-in view (the default is the center of the view).
	        expandedImageView.setPivotX(0f);
	        expandedImageView.setPivotY(0f);

	        // Construct and run the parallel animation of the four translation and scale properties
	        // (X, Y, SCALE_X, and SCALE_Y).
	        AnimatorSet set = new AnimatorSet();
	        set
	                .play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left,
	                        finalBounds.left))
	                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top,
	                        finalBounds.top))
	                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale, 1f))
	                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale, 1f));
	        set.setDuration(mShortAnimationDuration);
	        set.setInterpolator(new DecelerateInterpolator());
	        set.addListener(new AnimatorListenerAdapter() {
	            @Override
	            public void onAnimationEnd(Animator animation) {
	                mCurrentAnimator = null;
	            }

	            @Override
	            public void onAnimationCancel(Animator animation) {
	                mCurrentAnimator = null;
	            }
	        });
	        set.start();
	        mCurrentAnimator = set;

	        // Upon clicking the zoomed-in image, it should zoom back down to the original bounds
	        // and show the thumbnail instead of the expanded image.
	        final float startScaleFinal = startScale;
	        expandedImageView.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	                if (mCurrentAnimator != null) {
	                    mCurrentAnimator.cancel();
	                }

	                // Animate the four positioning/sizing properties in parallel, back to their
	                // original values.
	                AnimatorSet set = new AnimatorSet();
	                set
	                        .play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left))
	                        .with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top))
	                        .with(ObjectAnimator
	                                .ofFloat(expandedImageView, View.SCALE_X, startScaleFinal))
	                        .with(ObjectAnimator
	                                .ofFloat(expandedImageView, View.SCALE_Y, startScaleFinal));
	                set.setDuration(mShortAnimationDuration);
	                set.setInterpolator(new DecelerateInterpolator());
	                set.addListener(new AnimatorListenerAdapter() {
	                    @Override
	                    public void onAnimationEnd(Animator animation) {
	                        thumbView.setAlpha(1f);
	                        expandedImageView.setVisibility(View.GONE);
	                        mCurrentAnimator = null;
	                    }

	                    @Override
	                    public void onAnimationCancel(Animator animation) {
	                        thumbView.setAlpha(1f);
	                        expandedImageView.setVisibility(View.GONE);
	                        mCurrentAnimator = null;
	                    }
	                });
	                set.start();
	                mCurrentAnimator = set;
	            }
	        });
	    }
}