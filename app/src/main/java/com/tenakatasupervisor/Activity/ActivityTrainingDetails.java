package com.tenakatasupervisor.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.rtoshiro.view.video.FullscreenVideoLayout;
import com.tenakatasupervisor.Base.BaseActivity;
import com.tenakatasupervisor.Models.ModelSuccess;
import com.tenakatasupervisor.Models.TrainingViewModel;
import com.tenakatasupervisor.Network.Authentication;
import com.tenakatasupervisor.R;
import com.tenakatasupervisor.Utilities.HRAppConstants;
import com.tenakatasupervisor.Utilities.HRLogger;
import com.tenakatasupervisor.Utilities.HRUrlFactory;
import com.tenakatasupervisor.databinding.ActivityTrainingDetailsBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


    public class ActivityTrainingDetails extends BaseActivity implements View.OnClickListener {
        ActivityTrainingDetailsBinding binding;
        Intent intent;
        private Context context;
        private FullscreenVideoLayout videoLayout;

        @Override
        public void onClick(int viewId, View view) {

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            binding = DataBindingUtil.setContentView(this, R.layout.activity_training_details);
            context = this;
            intent = getIntent();
            // Toast.makeText(this,intent.getStringExtra("id"),Toast.LENGTH_LONG).show();
            hitApi();
            binding.tvHeadLeft.setOnClickListener(this);
            binding.trainingNextbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   if (pagevalidation()) {
                       hitApiRating();
                   }
                }
            });

            videoLayout = (FullscreenVideoLayout) findViewById(R.id.videoview);
            videoLayout.setActivity(this);


        }
        private boolean pagevalidation() {

            if(binding.ratingBar6.getRating()<.5){
                HRLogger.showSneckbar(binding.constraintLayout,"Please Fill Rating Bar");
                return false;
            }

            return true;
        }

        private void hitApi() {
            showLoader();

            final JSONObject jsonObject = new JSONObject();
            try {

                jsonObject.put("id", intent.getStringExtra("id"));
                jsonObject.put("role", "supervisor");


            } catch (JSONException e) {
                e.printStackTrace();
            }

            Authentication.object(this, HRUrlFactory.generateUrlWithVersion(HRAppConstants.URL_TRAINING_VIEW), this, jsonObject);
        }

        private void hitApiRating() {
            showLoader();

            final JSONObject jsonObject = new JSONObject();
            try {

                jsonObject.put("id", intent.getStringExtra("id"));
                jsonObject.put("role", "supervisor");
                jsonObject.put("rating", binding.ratingBar6.getRating());


            } catch (JSONException e) {
                e.printStackTrace();
            }

            Authentication.object(this, HRUrlFactory.generateUrlWithVersion(HRAppConstants.URL_TRAINING_RATING), this, jsonObject);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_head_left:
                    finish();
                    break;

            }
        }

        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }

        @Override
        public void onTaskError(String errorMsg) {
            super.onTaskError(errorMsg);

        }

        @Override
        public void onTaskSuccess(Object responseObj) {
            super.onTaskSuccess(responseObj);
            if (responseObj instanceof TrainingViewModel) {
                TrainingViewModel model = (TrainingViewModel) responseObj;
                binding.tvTrainingDetailsDescription.setText(model.getResult().get(0).getDescription());
                binding.tvTrainingDetailsTitle.setText(model.getResult().get(0).getTitle());
                Uri videoUri1 = Uri.parse("https://www.radiantmediaplayer.com/media/bbb-360p.mp4");
                Uri videoUri = Uri.parse(model.getResult().get(0).getVideo());
                try {

                    videoLayout.setVideoURI(videoUri1);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            if (responseObj instanceof ModelSuccess) {
                Toast.makeText(this, "Thank You for Rating...", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
