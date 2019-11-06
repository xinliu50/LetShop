package com.example.letshop.ui.settings;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.letshop.HomeActivity;
import com.example.letshop.Prevalent.Prevalent;
import com.example.letshop.R;
import com.example.letshop.ResetPasswordActivity;
import com.example.letshop.ui.items.ItemsFragment;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsFragment extends Fragment {

    private SettingsViewModel mViewModel;
    private View root;

    private CircleImageView profileImageView;
    private EditText fullNameEditText, userPhoneEditText, addressEditText;
    private TextView profileChangeTextBtn, closeTextBtn, saveTextButton;
    private Uri imageUri;
    private String myUrl = "";
    private StorageReference storageProfilePictureRef;
    private String checker = "";
    private StorageTask uploadTask;
    private final static int Gallery_Pick = 1;
    private Button securityQuestionBtn;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.settings_fragment, container, false);

        InitialUI();

        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Fragment itemFragment = new Fragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, new ItemsFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        saveTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checker.equals("clicked")){
                    userInfoSaved();
                }else{
                    updateOnlyUserInfo();
                }
            }
        });

        profileChangeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = "clicked";

//                CropImage.activity(imageUri)
//                        .setAspectRatio(1,1)
//                        .start(Fra);
//                CropImage.activity()
//                        .start(getContext(),this);

                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, Gallery_Pick);
            }
        });

        securityQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ResetPasswordActivity.class);
                intent.putExtra("check", "settings");
                startActivity(intent);
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode== Activity.RESULT_OK && data != null){
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            imageUri = result.getUri();
//            profileImageView.setImageURI(imageUri);
//        }else{
//            Toast.makeText(getActivity(),"Try again...", Toast.LENGTH_LONG).show();
//
//            Fragment settingFragment = new Fragment();
//            FragmentTransaction transaction = getFragmentManager().beginTransaction();
//            transaction.replace(R.id.nav_settings, settingFragment);
//            transaction.addToBackStack(null);
//            transaction.commit();
//        }

        if(requestCode == Gallery_Pick && resultCode == Activity.RESULT_OK && data != null){
            Uri ImageUri = data.getData();
            CropImage.activity(ImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(getContext(),this);
        }
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == Activity.RESULT_OK){
                imageUri = result.getUri();
                profileImageView.setImageURI(imageUri);

            }else{
            Toast.makeText(getActivity(),"Try again...", Toast.LENGTH_LONG).show();

            Fragment settingFragment = new Fragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.nav_settings, settingFragment);
            transaction.addToBackStack(null);
            transaction.commit();
          }
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        // TODO: Use the ViewModel
    }

    private void InitialUI() {
        profileImageView = (CircleImageView) root.findViewById(R.id.settings_profile_images);
        fullNameEditText = (EditText)root.findViewById(R.id.settings_full_name);
        userPhoneEditText = (EditText)root.findViewById(R.id.settings_phone_number);
        addressEditText = (EditText)root.findViewById(R.id.settings_address);
        profileChangeTextBtn = (TextView) root.findViewById(R.id.profile_image_change_btn);
        closeTextBtn = (TextView) root.findViewById(R.id.close_settings_btn);
        saveTextButton = (TextView) root.findViewById(R.id.update_account_settings);
        securityQuestionBtn = (Button)root.findViewById(R.id.security_questions_btn);

        storageProfilePictureRef = FirebaseStorage.getInstance().getReference().child("Profile_pictures");
        userInfoDisplay(profileImageView,fullNameEditText,userPhoneEditText,addressEditText);
    }

    private void userInfoDisplay(final CircleImageView profileImageView, final EditText fullNameEditText, final EditText userPhoneEditText, final EditText addressEditText) {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.child("image").exists()){
                        String image = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();
                        String phone = dataSnapshot.child("phone").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(profileImageView);
                        fullNameEditText.setText(name);
                        userPhoneEditText.setText(phone);
                        addressEditText.setText(address);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateOnlyUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name",fullNameEditText.getText().toString());
        userMap.put("address",addressEditText.getText().toString());
        userMap.put("phoneOrder",userPhoneEditText.getText().toString());
        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);


//        Fragment itemFragment = new Fragment();
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.replace(R.id.nav_items, itemFragment);
//        transaction.addToBackStack(null);
//        transaction.commit();

        startActivity(new Intent(getActivity(), HomeActivity.class));

        Toast.makeText(getActivity(),"Profile info updated successfully...",Toast.LENGTH_LONG).show();
    }

    private void userInfoSaved() {
        if(TextUtils.isEmpty(fullNameEditText.getText().toString())){
            Toast.makeText(getActivity(),"Name is mandatory", Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(addressEditText.getText().toString())){
            Toast.makeText(getActivity(),"Address is mandatory", Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(userPhoneEditText.getText().toString())){
            Toast.makeText(getActivity(),"Phone is mandatory", Toast.LENGTH_LONG).show();
        }else if(checker.equals("clicked")){
            uploadImage();
        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait......");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(imageUri != null){
            final StorageReference fileRef = storageProfilePictureRef
                    .child(Prevalent.currentOnlineUser.getPhone() + ".jpg");

            uploadTask = fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("name",fullNameEditText.getText().toString());
                        userMap.put("address",addressEditText.getText().toString());
                        userMap.put("phoneOrder",userPhoneEditText.getText().toString());
                        userMap.put("image",myUrl);
                        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

                        progressDialog.dismiss();

//                        Fragment itemFragment = new Fragment();
//                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                        transaction.replace(R.id.nav_items, itemFragment);
//                        transaction.addToBackStack(null);
//                        transaction.commit();

                        startActivity(new Intent(getActivity(), HomeActivity.class));

                        Toast.makeText(getActivity(),"Profile info updated successfully...",Toast.LENGTH_LONG).show();
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(),"Error...",Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
        else{
            Toast.makeText(getActivity(),"Image not selected...",Toast.LENGTH_LONG).show();

        }
    }
}
