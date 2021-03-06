package com.tenakatasupervisor.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.tenakatasupervisor.Activity.ActivityDashboard;
import com.tenakatasupervisor.Base.BaseFragment;
import com.tenakatasupervisor.Models.LoginModel;
import com.tenakatasupervisor.Models.ProfileModel;
import com.tenakatasupervisor.Network.Authentication;
import com.tenakatasupervisor.R;
import com.tenakatasupervisor.Utilities.HRAppConstants;
import com.tenakatasupervisor.Utilities.HRPrefManager;
import com.tenakatasupervisor.Utilities.HRValidationHelper;
import com.tenakatasupervisor.Utilities.IntentHelper;
import com.tenakatasupervisor.databinding.FragmentProfileBinding;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;
import static com.tenakatasupervisor.Activity.ActivityDashboard.profilepicpath;

public class FragmentProfile extends BaseFragment {
    private Context context;
    private FragmentProfileBinding binding;
    String name,mobile,email;
    private Uri image_uris;

    static Callback callback;

    public FragmentProfile(Callback callback) {
        super();
        FragmentProfile.callback =callback;

    }

    public FragmentProfile(Runnable runnable) {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_profile, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();


        if (HRPrefManager.getInstance(context).getUserDetail().getResult() != null) {

            binding.viewUserName.setText(HRValidationHelper.optional(HRPrefManager.getInstance(context).getUserDetail().getResult().getName()));
            binding.tvUserid.setText("ID : " + HRValidationHelper.optional(HRPrefManager.getInstance(context).getUserDetail().getResult().getId()));
            binding.tvMobile.setText(HRValidationHelper.optional(HRPrefManager.getInstance(context).getUserDetail().getResult().getPhone()));
            binding.tvEmail.setText(HRValidationHelper.optional(HRPrefManager.getInstance(context).getUserDetail().getResult().getEmail()));
            String path=HRPrefManager.getInstance(context).getUserDetail().getResult().getImage();


            Glide.with(this)
                    .load(path)
                    .apply(new RequestOptions()
                            .transform(new CircleCrop(),new RoundedCorners(30)).placeholder(R.drawable.avator_profile))
                    .into(binding.profileImage);


            Glide.with(this)
                    .load(path)
                    .apply(new RequestOptions()
                            .transform(new CircleCrop(),new RoundedCorners(30)).placeholder(R.drawable.avator_profile))
                    .into(binding.ivProfile);



            name = binding.viewUserName.getText().toString();
            mobile = binding.tvMobile.getText().toString();
            email = binding.tvEmail.getText().toString();

            binding.viewUserNameEditText.setText(binding.viewUserName.getText().toString());
            binding.mobileEditText.setText(binding.tvMobile.getText().toString());
            binding.emailEditText.setText(binding.tvEmail.getText().toString());

            binding.editButtonn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editFunctionality(name, mobile, email);
                }
            });
            binding.button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    apiHit();
                }
            });

            binding.changeprofiletv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    captureImage();
                }
            });

        }
    }

    private void editFunctionality(String name, String mobile, String email) {

        binding.emailEditText.setHint(email);
        //  binding.mobileEditText.setHint(mobile);
        binding.viewUserNameEditText.setHint(name);
        binding.view1111.setVisibility(View.VISIBLE);
        binding.view11.setVisibility(View.GONE);
        binding.view13.setVisibility(View.GONE);
        binding.view1313.setVisibility(View.VISIBLE);
        binding.emailEditText.setVisibility(View.VISIBLE);
        //   binding.mobileEditText.setVisibility(View.VISIBLE);
        binding.viewUserNameEditText.setVisibility(View.VISIBLE);
        binding.tvEmail.setVisibility(View.GONE);
        //   binding.tvMobile.setVisibility(View.GONE);
        binding.viewUserName.setVisibility(View.GONE);

        binding.button3.setVisibility(View.VISIBLE);
        binding.editButtonn.setVisibility(View.GONE);

    }

    private void captureImage() {
        TedPermission.with(getActivity())
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        CropImage.activity(image_uris).start(context,FragmentProfile.this);
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {

                    }
                })
                .setDeniedMessage(getString(R.string.txt_permission_denied_message))
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    private void apiHit() {

        if (isValidate()) {
            Authentication.ProfilemultiPartRequest(profilepicpath,
                    (HRAppConstants.URL_PROFILE), this,
                    binding.mobileEditText.getText().toString(),
                    HRPrefManager.getInstance(context).getUserDetail().getResult().getId(),
                    binding.viewUserNameEditText.getText().toString(),
                    binding.emailEditText.getText().toString(), "supervisor");
        }


    }

    @Override
    public void onTaskSuccess(Object responseObj) {
        dismissLoader();


        if (responseObj instanceof LoginModel) {
            LoginModel model = (LoginModel) responseObj;
            onSaved();

      //  Toast.makeText(getActivity(),HRPrefManager.getInstance(context).getUserDetail().getResult().getToken(),Toast.LENGTH_LONG).show();

            model.getResult().setToken(HRPrefManager.getInstance(context).getUserDetail().getResult().getToken());
            model.getResult().setCountry_code(HRPrefManager.getInstance(context).getUserDetail().getResult().getCountry_code());
            model.getResult().setEmail(model.getResult().getEmail());
            model.getResult().setId(HRPrefManager.getInstance(context).getUserDetail().getResult().getId());
            if (model.getResult().getImage()!=null){
                String path=model.getResult().getImage();
                /*Glide.with(this)
                        .load(path)
                        .apply(new RequestOptions()
                                .transform(new CircleCrop(),new RoundedCorners(30)).placeholder(R.drawable.avator_profile))
                        .into(binding.profileImage);*/
                model.getResult().setImage(model.getResult().getImage());
                //     model.getResult().setImage(HRPrefManager.getInstance(context).getUserDetail().getResult().getImage());
            }
            else {
                model.getResult().setImage(HRPrefManager.getInstance(context).getUserDetail().getResult().getImage());
            }

            if (model.getResult().getName()!=null){
                model.getResult().setName(model.getResult().getName());
                binding.viewUserName.setText(model.getResult().getName());
                //   model.getResult().setName(HRPrefManager.getInstance(context).getUserDetail().getResult().getName());
            }
            else {
                model.getResult().setName(HRPrefManager.getInstance(context).getUserDetail().getResult().getName());
            }
            if (model.getResult().getEmail()!=null){
                model.getResult().setEmail(model.getResult().getEmail());
                //  model.getResult().setEmail(HRPrefManager.getInstance(context).getUserDetail().getResult().getEmail());
                binding.tvEmail.setText(model.getResult().getEmail());
            }
            else {
                model.getResult().setEmail(HRPrefManager.getInstance(context).getUserDetail().getResult().getEmail());
            }
            model.getResult().setPhone(HRPrefManager.getInstance(context).getUserDetail().getResult().getPhone());
            model.getResult().setRole(HRPrefManager.getInstance(context).getUserDetail().getResult().getRole());
            HRPrefManager.getInstance(context).setUserDetail(model);

            callback.onChangeName();

            startActivity(IntentHelper.getDashboard(context));
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {



                   /* File compressedImageFile = Compressor.getDefault(context).compressToFile(new File(result.getUri().getEncodedPath()));
                    Uri x=Uri.fromFile(new File(compressedImageFile.getPath()));
                    profilepicpath=String.valueOf(x.getPath());*/
                    try {
                        profilepicpath=   String.valueOf(new Compressor(context).compressToFile(
                                new File(result.getUri().getPath())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Glide.with(this)
                            .load(profilepicpath)
                            .apply(new RequestOptions()
                                    .transform(new CircleCrop(),new RoundedCorners(30)).placeholder(R.drawable.avator_profile))
                            .into(binding.profileImage);
                    Glide.with(this)
                            .load(profilepicpath)
                            .apply(new RequestOptions()
                                    .transform(new CircleCrop(),new RoundedCorners(30)).placeholder(R.drawable.avator_profile))
                            .into(binding.ivProfile);
                    apiHit();

                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isValidate() {
        if (HRValidationHelper.isNull(binding.viewUserNameEditText.getText().toString())) {
            Toast.makeText(context, "Enter User Name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (HRValidationHelper.isNull(binding.emailEditText.getText().toString())) {
            Toast.makeText(context, "Enter Email address", Toast.LENGTH_SHORT).show();
            return false;
       /* }else if (!HRValidationHelper.isValidEmail(binding.emailEditText.getText().toString())) {
            Toast.makeText(context, "Enter Valid Email address", Toast.LENGTH_SHORT).show();
            return false;*/
        }else if (HRValidationHelper.isNull(binding.mobileEditText.getText().toString())) {
            Toast.makeText(context, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onTaskError(String errorMsg) {
        super.onTaskError(errorMsg);
        dismissLoader();
      //  Toast.makeText(getActivity(), "noo", Toast.LENGTH_LONG).show();
    }


    private void onSaved() {

        binding.emailEditText.setHint(email);
        binding.tvMobile.setText(mobile);
        binding.viewUserNameEditText.setHint(name);
        binding.view1111.setVisibility(View.GONE);
        binding.view11.setVisibility(View.VISIBLE);
        binding.view13.setVisibility(View.VISIBLE);
        binding.view1313.setVisibility(View.GONE);
        binding.emailEditText.setVisibility(View.GONE);
        binding.mobileEditText.setVisibility(View.GONE);
        binding.viewUserNameEditText.setVisibility(View.GONE);
        binding.tvEmail.setVisibility(View.VISIBLE);
        binding.tvMobile.setVisibility(View.VISIBLE);
        binding.viewUserName.setVisibility(View.VISIBLE);
        binding.button3.setVisibility(View.GONE);
        binding.editButtonn.setVisibility(View.VISIBLE);

    }


    public interface Callback{
        void onChangeName();
    }
}
