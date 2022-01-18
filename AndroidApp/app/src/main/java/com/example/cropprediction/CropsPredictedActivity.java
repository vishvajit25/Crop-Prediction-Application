package com.example.cropprediction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CropsPredictedActivity extends AppCompatActivity {

    ProgressBar progressBar;
    double value;
    int val=0;
    Handler handler=new Handler();
    Button b1;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ProgressDialog progressDialog;
    private ProgressBar loadPredicted;
    private List<com.example.cropprediction.CropDetails> crop_details;

    String PREF_NAME_IOT = "IOT_FRAGMENT";
    String PREF_NAME_PREDICT = "PREDICT_FRAGMENT";
    SharedPreferences saveInstance, farmSaveInstance, locationSaveInstance;
    String crop_image;

    TextView tv,tw1;

    boolean isNewRequest = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_crops_predicted);

        tv=findViewById(R.id.tv1);
        progressBar=findViewById(R.id.prog);
        tw1=findViewById(R.id.tw);
        b1=findViewById(R.id.b1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CropsPredictedActivity.this, see_more.class));
            }
        });

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                startProgress();
            }

        });
        thread.start();

        progressDialog = new ProgressDialog(this);

        crop_details = new ArrayList<>();

        //displaying progress dialog while fetching images
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.dismiss();

        saveInstance = getSharedPreferences(PREF_NAME_IOT, MODE_PRIVATE);
        farmSaveInstance = getSharedPreferences(PREF_NAME_PREDICT, MODE_PRIVATE);

        try {

            Toast.makeText(this, "Locn: " + farmSaveInstance.getString("latitude", null) + " " +
                    farmSaveInstance.getString("longitude", null), Toast.LENGTH_SHORT).show();

            String crop = " ";

            SharedPreferences sf=getSharedPreferences("PREDICT_FRAGMENT", Context.MODE_PRIVATE);
            String state=sf.getString("state","Tamilnadu");
            String crop_season=sf.getString("string_crop_season","Kharif");
            String month=sf.getString("string_predict_month","NOV");
            Toast.makeText(getApplicationContext(),state+" "+crop_season,Toast.LENGTH_SHORT).show();

            if (state.equals("Andaman and Nicobar Islands")) {
                if (crop_season.equals("Rabi") || month.equals("JAN") || month.equals("FEB") || month.equals("NOV") || month.equals("DEC") ||
                        month.equals("जनवरी") || month.equals("फ़रवरी") || month.equals("नवम्बर") || month.equals("दिसम्बर")) {
                    crop = "'Black pepper', 'Cashewnut', 'Maize', 'Sunflower'";
                } else if (crop_season.equals("Kharif") || month.equals("JUL") || month.equals("AUG") || month.equals("SEP") || month.equals("OCT") ||
                        month.equals("जुलाई") || month.equals("अगस्त") || month.equals("सितम्बर") || month.equals("अक्टूबर")) {
                    crop = "'Arecanut', 'Other Kharif pulses', 'Rice'";
                }
                else if(crop_season.equals("zaid") || month.equals("MAR") || month.equals("APR") || month.equals("MAY") || month.equals("JUN") ||
                        month.equals("मार्च") || month.equals("अप्रैल") || month.equals("मई") || month.equals("जून")) {
                    crop = "'Rice', 'Sugarcane'";
                }
                else if(crop_season.equals("whole year")) {
                    crop = "'Banana','Dry ginger', 'Tapioca', 'Arecanut'," +
                            " 'Black pepper', 'Turmeric'";
                }
            }
            else if (state.equals("Andhra Pradesh") || state.equals("Telangana")) {
                if (crop_season.equals("Rabi") || month.equals("JAN") || month.equals("FEB") || month.equals("NOV") || month.equals("DEC") ||
                        month.equals("जनवरी") || month.equals("फ़रवरी") || month.equals("नवम्बर") || month.equals("दिसम्बर")) {
                    crop = "'Brinjal', 'Castor seed', 'Coriander', 'Cotton(lint)', 'Potato'," +
                            "'Sweet potato', 'Tobacco', 'Tomato'";
                } else if (crop_season.equals("Kharif") || month.equals("JUL") || month.equals("AUG") || month.equals("SEP") || month.equals("OCT") ||
                        month.equals("जुलाई") || month.equals("अगस्त") || month.equals("सितम्बर") || month.equals("अक्टूबर")) {
                    crop = "'Urad', 'Gram', 'Onion', 'Sapota'";
                }
                else if(crop_season.equals("zaid") || month.equals("MAR") || month.equals("APR") || month.equals("MAY") || month.equals("JUN") ||
                        month.equals("मार्च") || month.equals("अप्रैल") || month.equals("मई") || month.equals("जून")) {
                    crop = "'Turmeric', 'Niger seed'";
                }
                else if(crop_season.equals("whole year")) {
                    crop = "'Pome Fruit', 'Tomato','Sannhamp'";
                }
            }
            else if (state.equals("Arunachal Pradesh")) {
                if (crop_season.equals("Rabi") || month.equals("JAN") || month.equals("FEB") || month.equals("NOV") || month.equals("DEC") ||
                        month.equals("जनवरी") || month.equals("फ़रवरी") || month.equals("नवम्बर") || month.equals("दिसम्बर")) {
                    crop = "'Wheat', 'Groundnut', 'Rapeseed &Mustard', 'Sugarcane'";
                } else if (crop_season.equals("Kharif") || month.equals("JUL") || month.equals("AUG") || month.equals("SEP") || month.equals("OCT") ||
                        month.equals("जुलाई") || month.equals("अगस्त") || month.equals("सितम्बर") || month.equals("अक्टूबर")) {
                    crop = "'Maize', 'Rice', 'Small millets','Soyabean', 'Sunflower'";
                }
                else if(crop_season.equals("zaid") || month.equals("MAR") || month.equals("APR") || month.equals("MAY") || month.equals("JUN") ||
                        month.equals("मार्च") || month.equals("अप्रैल") || month.equals("मई") || month.equals("जून")) {
                    crop = "'Safflower', 'Wheat', 'Rice', 'Tapioca'";
                }
                else if(crop_season.equals("whole year")) {
                    crop = "'Dry chillies', 'Dry ginger', 'Turmeric', 'Oilseeds'";
                }
            }
            else if (state.equals("Assam") || state.equals("Arunachal Pradesh") || state.equals("Meghalaya")) {
                if (crop_season.equals("Rabi") || month.equals("JAN") || month.equals("FEB") || month.equals("NOV") || month.equals("DEC") ||
                        month.equals("जनवरी") || month.equals("फ़रवरी") || month.equals("नवम्बर") || month.equals("दिसम्बर")) {
                    crop = "Gram', 'Linseed', 'Black pepper', 'Dry chillies', 'Ginger', 'Pineapple', 'Sweet potato'";
                } else if (crop_season.equals("Kharif") || month.equals("JUL") || month.equals("AUG") || month.equals("SEP") || month.equals("OCT") ||
                        month.equals("जुलाई") || month.equals("अगस्त") || month.equals("सितम्बर") || month.equals("अक्टूबर")) {
                    crop = "'Arhar/Tur', 'Sugarcane'";
                }
                else if(crop_season.equals("zaid") || month.equals("MAR") || month.equals("APR") || month.equals("MAY") || month.equals("JUN") ||
                        month.equals("मार्च") || month.equals("अप्रैल") || month.equals("मई") || month.equals("जून")) {
                    crop = "'Rice', 'Paddy'";
                }
                else if(crop_season.equals("whole year")) {
                    crop = "'Sweet potato', 'Niger seed', 'Sesamum', " +
                            "'Orange', 'Papaya', 'Pineapple'";
                }
            }
            else if (state.equals("Bihar") || state.equals("Jharkhand") || state.equals("Sikkim") || state.equals("West Bengal")) {
                if (crop_season.equals("Rabi") || month.equals("JAN") || month.equals("FEB") || month.equals("NOV") || month.equals("DEC") ||
                        month.equals("जनवरी") || month.equals("फ़रवरी") || month.equals("नवम्बर") || month.equals("दिसम्बर")) {
                    crop = "'Barley', 'Gram', 'Linseed', 'Peas & beans (Pulses)', 'Potato'";
                } else if (crop_season.equals("Kharif") || month.equals("JUL") || month.equals("AUG") || month.equals("SEP") || month.equals("OCT") ||
                        month.equals("जुलाई") || month.equals("अगस्त") || month.equals("सितम्बर") || month.equals("अक्टूबर")) {
                    crop = "'Arhar/Tur', 'Jute', 'Maize', 'Mesta', 'Other Kharif pulses'";
                }
                else if(crop_season.equals("zaid") || month.equals("MAR") || month.equals("APR") || month.equals("MAY") || month.equals("JUN") ||
                        month.equals("मार्च") || month.equals("अप्रैल") || month.equals("मई") || month.equals("जून")) {
                    crop = "'Rice', 'Maize'";
                }
                else if(crop_season.equals("whole year")) {
                    crop = "'Banana', 'Coriander', 'Tobacco'";
                }
            }
            else if (state.equals("Chandigarh") || state.equals("Jammu and Kashmir") || state.equals("Himachal Pradesh")) {
                if (crop_season.equals("Rabi") || month.equals("JAN") || month.equals("FEB") || month.equals("NOV") || month.equals("DEC") ||
                        month.equals("जनवरी") || month.equals("फ़रवरी") || month.equals("नवम्बर") || month.equals("दिसम्बर")) {
                    crop = "'Gram', 'Masoor', 'Rapeseed &Mustard'";
                } else if (crop_season.equals("Kharif") || month.equals("JUL") || month.equals("AUG") || month.equals("SEP") || month.equals("OCT") ||
                        month.equals("जुलाई") || month.equals("अगस्त") || month.equals("सितम्बर") || month.equals("अक्टूबर")) {
                    crop = "'Arhar/Tur', 'Maize', 'Moong(Green Gram)', 'Rice', 'Urad'";
                }
                else if(crop_season.equals("zaid") || month.equals("MAR") || month.equals("APR") || month.equals("MAY") || month.equals("JUN") ||
                        month.equals("मार्च") || month.equals("अप्रैल") || month.equals("मई") || month.equals("जून")) {
                    crop = "'Rice', 'Maize'";
                }
                else if(crop_season.equals("whole year")) {
                    crop = "'Onion', 'Potato'";
                }
            }
            else if (state.equals("Chhattisgarh") || state.equals("Maharashtra") || state.equals("Madhya Pradesh") || state.equals("Odisha")) {
                if (crop_season.equals("Rabi") || month.equals("JAN") || month.equals("FEB") || month.equals("NOV") || month.equals("DEC") ||
                        month.equals("जनवरी") || month.equals("फ़रवरी") || month.equals("नवम्बर") || month.equals("दिसम्बर")) {
                    crop = "'Rapeseed &Mustard', 'Safflower', 'Urad', 'Wheat'";
                } else if (crop_season.equals("Kharif") || month.equals("JUL") || month.equals("AUG") || month.equals("SEP") || month.equals("OCT") ||
                        month.equals("जुलाई") || month.equals("अगस्त") || month.equals("सितम्बर") || month.equals("अक्टूबर")) {
                    crop = "'Ragi', 'Rice', 'Sesamum', 'Small millets', 'Soyabean'";
                }
                else if(crop_season.equals("zaid") || month.equals("MAR") || month.equals("APR") || month.equals("MAY") || month.equals("JUN") ||
                        month.equals("मार्च") || month.equals("अप्रैल") || month.equals("मई") || month.equals("जून")) {
                    crop = "'Rapeseed &Mustard', 'Wheat'";
                }
                else if(crop_season.equals("whole year")) {
                    crop = "'Sugarcane', 'Sweet potato', 'Turmeric'";
                }
            }
            else if (state.equals("Goa")) {
                if (crop_season.equals("Rabi") || month.equals("JAN") || month.equals("FEB") || month.equals("NOV") || month.equals("DEC") ||
                        month.equals("जनवरी") || month.equals("फ़रवरी") || month.equals("नवम्बर") || month.equals("दिसम्बर")) {
                    crop = "'Groundnut', 'Other  Rabi pulses', 'Rice'";
                } else if (crop_season.equals("Kharif") || month.equals("JUL") || month.equals("AUG") || month.equals("SEP") || month.equals("OCT") ||
                        month.equals("जुलाई") || month.equals("अगस्त") || month.equals("सितम्बर") || month.equals("अक्टूबर")) {
                    crop = "'Groundnut', 'Other Kharif pulses', 'Ragi', 'Rice'";
                }
                else if(crop_season.equals("zaid") || month.equals("MAR") || month.equals("APR") || month.equals("MAY") || month.equals("JUN") ||
                        month.equals("मार्च") || month.equals("अप्रैल") || month.equals("मई") || month.equals("जून")) {
                    crop = "Maize', 'Onion'";
                }
                else if(crop_season.equals("whole year")) {
                    crop = "'Ragi', 'Black pepper', 'Cashewnut'";
                }
            }
            else if (state.equals("Gujarat")) {
                if (crop_season.equals("Rabi") || month.equals("JAN") || month.equals("FEB") || month.equals("NOV") || month.equals("DEC") ||
                        month.equals("जनवरी") || month.equals("फ़रवरी") || month.equals("नवम्बर") || month.equals("दिसम्बर")) {
                    crop = "'Gram', 'Jowar', 'Rapeseed &Mustard'";
                } else if (crop_season.equals("Kharif") || month.equals("JUL") || month.equals("AUG") || month.equals("SEP") || month.equals("OCT") ||
                        month.equals("जुलाई") || month.equals("अगस्त") || month.equals("सितम्बर") || month.equals("अक्टूबर")) {
                    crop = "'Urad', 'Castor seed', 'Cotton(lint)', 'Maize', 'Other Cereals & Millets', 'Rice'";
                }
                else if(crop_season.equals("zaid") || month.equals("MAR") || month.equals("APR") || month.equals("MAY") || month.equals("JUN") ||
                        month.equals("मार्च") || month.equals("अप्रैल") || month.equals("मई") || month.equals("जून")) {
                    crop = "Maize', 'Onion'";
                }
                else if(crop_season.equals("whole year")) {
                    crop = "'Rice', 'Sesamum', 'Sugarcane'";
                }
            }
            else if (state.equals("Haryana") || state.equals("Uttar Pradesh") || state.equals("Punjab") || state.equals("Uttarakhand")) {
                if (crop_season.equals("Rabi") || month.equals("JAN") || month.equals("FEB") || month.equals("NOV") || month.equals("DEC") ||
                        month.equals("जनवरी") || month.equals("फ़रवरी") || month.equals("नवम्बर") || month.equals("दिसम्बर")) {
                    crop = "'Wheat', 'Barley', 'Gram', 'Peas & beans (Pulses)', 'Rapeseed &Mustard', 'Sunflower'";
                } else if (crop_season.equals("Kharif") || month.equals("JUL") || month.equals("AUG") || month.equals("SEP") || month.equals("OCT") ||
                        month.equals("जुलाई") || month.equals("अगस्त") || month.equals("सितम्बर") || month.equals("अक्टूबर")) {
                    crop = "'Cotton(lint)', 'Groundnut', 'Jowar', 'Moth', 'Sesamum', 'Urad', 'Moong(Green Gram)', 'Horse-gram' 'Castor seed', 'Sannhamp', 'Gram'";
                }
                else if(crop_season.equals("zaid") || month.equals("MAR") || month.equals("APR") || month.equals("MAY") || month.equals("JUN") ||
                        month.equals("मार्च") || month.equals("अप्रैल") || month.equals("मई") || month.equals("जून")) {
                    crop = "'Sannhamp', 'Coriander', 'Cotton(lint)', 'Guar seed";
                }
                else if(crop_season.equals("whole year")) {
                    crop = "'Sugarcane', 'Dry chillies', 'Onion', 'Potato', 'Garlic','Turmeric'";
                }
            }
            else if (state.equals("Karnataka")) {
                if (crop_season.equals("Rabi") || month.equals("JAN") || month.equals("FEB") || month.equals("NOV") || month.equals("DEC") ||
                        month.equals("जनवरी") || month.equals("फ़रवरी") || month.equals("नवम्बर") || month.equals("दिसम्बर")) {
                    crop = "'Onion', 'Safflower', 'Sunflower', 'Wheat', 'Potato', 'Ragi', 'Moong(Green Gram)', 'Urad', 'Bajra'";
                } else if (crop_season.equals("Kharif") || month.equals("JUL") || month.equals("AUG") || month.equals("SEP") || month.equals("OCT") ||
                        month.equals("जुलाई") || month.equals("अगस्त") || month.equals("सितम्बर") || month.equals("अक्टूबर")) {
                    crop = "'Horse-gram', 'Jowar', 'Maize', 'Moong(Green Gram)', 'Niger seed', 'Onion', 'Rapeseed &Mustard', 'Rice', 'Sesamum', 'Soyabean'," +
                            " 'Sunflower', 'Mesta', 'Other Kharif pulses', 'Small millets'";
                }
                else if(crop_season.equals("zaid") || month.equals("MAR") || month.equals("APR") || month.equals("MAY") || month.equals("JUN") ||
                        month.equals("मार्च") || month.equals("अप्रैल") || month.equals("मई") || month.equals("जून")) {
                    crop = "'Gram', 'Groundnut', 'Horse-gram', 'Jowar', 'Linseed', 'Maize'";
                }
                else if(crop_season.equals("whole year")) {
                    crop = "'Arecanut', 'Coconut ', 'Coriander', 'Dry chillies', 'Garlic', 'Turmeric', 'Banana', 'Onion', 'Sugarcane', 'Sweet potato'," +
                            " 'Sannhamp', 'Arcanut (Processed)', 'Atcanut (Raw)', 'Brinjal', 'Citrus Fruit', 'Grapes', 'Mango', 'Other Fresh Fruits', 'Papaya'," +
                            " 'Pome Fruit', 'Tomato', 'Cotton(lint)', 'Mesta', 'Cashewnut', 'Tobacco', 'Black pepper', 'Dry ginger', 'Potato', 'Tapioca'";
                }
            }
            else if (state.equals("Kerala")) {
                if (crop_season.equals("Rabi") || month.equals("JAN") || month.equals("FEB") || month.equals("NOV") || month.equals("DEC") ||
                        month.equals("जनवरी") || month.equals("फ़रवरी") || month.equals("नवम्बर") || month.equals("दिसम्बर")) {
                    crop = "'Onion', 'Safflower', 'Sunflower', 'Wheat', 'Other  Rabi pulses', 'Rapeseed &Mustard', 'Dry chillies', 'Cotton(lint)'," +
                            " 'Cowpea(Lobia)', 'Paddy', 'Rice', 'Peas & beans (Pulses)', 'Potato', 'Ragi', 'Moong(Green Gram)', 'Urad', 'Bajra'";
                } else if (crop_season.equals("Kharif") || month.equals("JUL") || month.equals("AUG") || month.equals("SEP") || month.equals("OCT") ||
                        month.equals("जुलाई") || month.equals("अगस्त") || month.equals("सितम्बर") || month.equals("अक्टूबर")) {
                    crop = "'Sesamum', 'Small millets', 'Ragi', 'Jowar', 'Other Cereals & Millets', 'Cotton(lint)', 'Groundnut'";
                }
                else if(crop_season.equals("zaid") || month.equals("MAR") || month.equals("APR") || month.equals("MAY") || month.equals("JUN") ||
                        month.equals("मार्च") || month.equals("अप्रैल") || month.equals("मई") || month.equals("जून")) {
                    crop = "'Rice'";
                }
                else if(crop_season.equals("whole year")) {
                    crop = "'Snak Guard', 'Potato', 'Cowpea(Lobia)', 'Moong(Green Gram)', 'Pump Kin', 'Sesamum', 'Dry chillies', 'Tea', 'Cardamom', 'Garlic', " +
                            "'Coffee', 'Arhar/Tur', 'Maize', 'Wheat', 'Onion', 'Ragi', 'Blackgram', 'Horse-gram', 'Tobacco', 'Jowar', 'Groundnut'," +
                            " 'Small millets', 'Soyabean' 'Dry ginger', 'Potato', 'Tapioca'";
                }
            }
            else if (state.equals("Rajasthan")) {
                if (crop_season.equals("Rabi") || month.equals("JAN") || month.equals("FEB") || month.equals("NOV") || month.equals("DEC") ||
                        month.equals("जनवरी") || month.equals("फ़रवरी") || month.equals("नवम्बर") || month.equals("दिसम्बर")) {
                    crop = "'Barley', 'Gram', 'Linseed', 'Rapeseed &Mustard', 'Wheat', 'Masoor', 'Other  Rabi pulses', 'Peas & beans (Pulses)'," +
                            "'other oilseeds', 'Tobacco', 'Sunflower', 'Small millets'";
                } else if (crop_season.equals("Kharif") || month.equals("JUL") || month.equals("AUG") || month.equals("SEP") || month.equals("OCT") ||
                        month.equals("जुलाई") || month.equals("अगस्त") || month.equals("सितम्बर") || month.equals("अक्टूबर")) {
                    crop = "'Bajra', 'Cotton(lint)', 'Groundnut', 'Jowar', 'Maize', 'Onion', 'Rice', 'Sesamum', 'Sugarcane', 'Arhar/Tur', 'Castor seed'," +
                            " 'Moong(Green Gram)', 'Moth', 'Other Kharif pulses', 'Sannhamp'";
                }
                else if(crop_season.equals("zaid") || month.equals("MAR") || month.equals("APR") || month.equals("MAY") || month.equals("JUN") ||
                        month.equals("मार्च") || month.equals("अप्रैल") || month.equals("मई") || month.equals("जून")) {
                    crop = "'Tobacco', 'Sunflower', 'Small millets'";
                }
                else if(crop_season.equals("whole year")) {
                    crop = "'Coriander', 'Dry chillies', 'Garlic', 'Onion', 'Potato', 'Sugarcane', 'Sweet potato', 'Guar seed', 'Sannhamp', 'Tapioca'," +
                            " 'Citrus Fruit', 'Mango', 'Other Fresh Fruits', 'Other Vegetables', 'Pome Fruit', 'other oilseeds', 'Banana', 'Tobacco', 'Papaya'," +
                            " 'Water Melon', 'Oilseeds total', 'other fibres', 'Grapes', 'Turmeric', 'Orange', 'Dry ginger'";
                }
            }
            else if (state.equals("Mizoram") || state.equals("Manipur") || state.equals("Nagaland") || state.equals("Tripura")) {
                if (crop_season.equals("Rabi") || month.equals("JAN") || month.equals("FEB") || month.equals("NOV") || month.equals("DEC") ||
                        month.equals("जनवरी") || month.equals("फ़रवरी") || month.equals("नवम्बर") || month.equals("दिसम्बर")) {
                    crop = "'Rapeseed &Mustard', 'Tobacco', 'Wheat', 'Other  Rabi pulses', 'other oilseeds', 'Potato', 'Rice', 'Maize', 'Masoor'," +
                            " 'Moong(Green Gram)', 'Peas & beans (Pulses)', 'Urad', 'Arhar/Tur', 'Gram'";
                } else if (crop_season.equals("Kharif") || month.equals("JUL") || month.equals("AUG") || month.equals("SEP") || month.equals("OCT") ||
                        month.equals("जुलाई") || month.equals("अगस्त") || month.equals("सितम्बर") || month.equals("अक्टूबर")) {
                    crop = "'Arhar/Tur', 'Coconut ', 'Groundnut', 'Kapas', 'Maize', 'Peas & beans (Pulses)', 'Rapeseed &Mustard', 'Sesamum'," +
                            " 'Soyabean', 'Cotton(lint)', 'Other Kharif pulses', 'other oilseeds', 'Potato', 'Rice', 'Tapioca', 'Moong(Green Gram)'";
                }
                else if(crop_season.equals("zaid") || month.equals("MAR") || month.equals("APR") || month.equals("MAY") || month.equals("JUN") ||
                        month.equals("मार्च") || month.equals("अप्रैल") || month.equals("मई") || month.equals("जून")) {
                    crop = "'Urad', 'Gram', 'Sunflower'";
                }
                else if(crop_season.equals("whole year")) {
                    crop = "'Rice', 'Sugarcane', 'Tapioca', 'Kapas', 'Tobacco', 'Potato'";
                }
            }
            else if (state.equals("Tamil Nadu")) {
                if (crop_season.equals("Rabi") || month.equals("JAN") || month.equals("FEB") || month.equals("NOV") || month.equals("DEC") ||
                        month.equals("जनवरी") || month.equals("फ़रवरी") || month.equals("नवम्बर") || month.equals("दिसम्बर")) {
                    crop = "'Rapeseed & Mustard',  'Wheat', 'Rice', 'Maize'";
                } else if (crop_season.equals("Kharif") || month.equals("JUL") || month.equals("AUG") || month.equals("SEP") || month.equals("OCT") ||
                        month.equals("जुलाई") || month.equals("अगस्त") || month.equals("सितम्बर") || month.equals("अक्टूबर")) {
                    crop = "'Ragi', 'Sunflower', 'Urad', 'Varagu'";
                }
                else if(crop_season.equals("zaid") || month.equals("MAR") || month.equals("APR") || month.equals("MAY") || month.equals("JUN") ||
                        month.equals("मार्च") || month.equals("अप्रैल") || month.equals("मई") || month.equals("जून")) {
                    crop = "'Urad', 'Gram', 'Sunflower'";
                }
                else if(crop_season.equals("whole year")) {
                    crop = "'Coconut ', 'Coriander', 'Cotton(lint)',  'Guar seed', 'Arecanut'";
                }
            }

            tv.setText("The crops that can be sown are "+crop);


        } finally {

        }


    }



    public void updateUI (List<com.example.cropprediction.CropDetails> crop_details){
        adapter = new com.example.cropprediction.CropAdapter(getApplicationContext(), crop_details);
        //adding adapter to recyclerview
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }


    public void startProgress() {
        while (val <= 99.3) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setProgress(val);
                    tw1.setText(val + " %");
                }
            });
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            val++;
        }
    }


}


