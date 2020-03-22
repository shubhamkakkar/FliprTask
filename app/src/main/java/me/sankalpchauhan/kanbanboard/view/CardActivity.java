package me.sankalpchauhan.kanbanboard.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.sankalpchauhan.kanbanboard.BuildConfig;
import me.sankalpchauhan.kanbanboard.R;
import me.sankalpchauhan.kanbanboard.fragments.DateTimePickerDialog;
import me.sankalpchauhan.kanbanboard.model.Board;
import me.sankalpchauhan.kanbanboard.model.BoardList;
import me.sankalpchauhan.kanbanboard.util.Constants;
import me.sankalpchauhan.kanbanboard.viewmodel.CardActivityViewModel;

import static me.sankalpchauhan.kanbanboard.util.Constants.PICK_IMAGE_CAMERA;
import static me.sankalpchauhan.kanbanboard.util.Constants.PICK_IMAGE_GALLERY;

public class CardActivity extends AppCompatActivity {
    String boardid, listid;
    BoardList boardList;
    private InputStream inputStreamImg;
    File destination;
    private String imgPath = null;
    private Bitmap attachmentBMP;
    ImageView titleIV;
    CardActivityViewModel cardActivityViewModel;
    String atachmentUrl=null;
    TextView attachmentTV, dueDateTv;
    Button createButton;
    ProgressBar progressBar;
    Date selecteDate=null;
    EditText cardTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        getIntentData();
        initBoardActivityViewModel();
        titleIV = findViewById(R.id.eventimage);
        attachmentTV = findViewById(R.id.attachment_picker);
        dueDateTv = findViewById(R.id.date_picker);
        createButton = findViewById(R.id.create_BTN);
        progressBar = findViewById(R.id.uploadProgress);
        cardTitle = findViewById(R.id.card_title);

        attachmentTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beginImage();
            }
        });

        dueDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateTimePickerDialog dateTimePickerDialog = new DateTimePickerDialog();
                dateTimePickerDialog.show(getSupportFragmentManager(), "datetimepicker");
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(cardTitle.getText())) {
                    cardActivityViewModel.createCard(CardActivity.this, boardid, cardTitle.getText().toString(), listid, atachmentUrl, selecteDate);
                } else {
                    Toast.makeText(CardActivity.this, "A card must have a title", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getIntentData(){
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        boardid = bundle.getString("BoardId");
        listid = bundle.getString("ListId");
        boardList = (BoardList) bundle.getSerializable("BoardList");
    }

    private void initBoardActivityViewModel() {
        cardActivityViewModel = new ViewModelProvider(this).get(CardActivityViewModel.class);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        inputStreamImg = null;
        if (requestCode == PICK_IMAGE_CAMERA) {
            try {
                Uri selectedImage = data.getData();
                attachmentBMP = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                attachmentBMP.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                Log.e("Activity", "Pick from Camera::>>> ");

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                destination = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/" , "IMG_" + timeStamp + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    Log.e("Check", "Hello");
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imgPath = destination.getAbsolutePath();
                titleIV.setImageBitmap(attachmentBMP);
                if(destination!=null) {
                    cardActivityViewModel.getImageUrl(this, destination, listid);
                    progressBar.setVisibility(View.VISIBLE);
                    createButton.setVisibility(View.INVISIBLE);
                    cardActivityViewModel.urlLiveData.observe(this, newUrl->{
                        atachmentUrl = newUrl;
                        Log.e(Constants.TAG, atachmentUrl);
                        progressBar.setVisibility(View.GONE);
                        createButton.setVisibility(View.VISIBLE);
                    });
                }
                else{
                    Toast.makeText(this, "Please Choose A Image", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY) {
            if(data!=null) {
                Uri selectedImage = data.getData();

                try {
                    attachmentBMP = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    attachmentBMP.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    Log.e("Activity", "Pick from Gallery::>>> ");
                    imgPath = getRealPathFromURI(selectedImage);
                    destination = new File(imgPath.toString());
                    titleIV.setImageBitmap(attachmentBMP);

                    if(destination!=null) {
                        cardActivityViewModel.getImageUrl(this, destination, listid);
                        progressBar.setVisibility(View.VISIBLE);
                        createButton.setVisibility(View.INVISIBLE);
                        cardActivityViewModel.urlLiveData.observe(this, newUrl->{
                            atachmentUrl = newUrl;
                            Log.e(Constants.TAG, atachmentUrl);
                            progressBar.setVisibility(View.GONE);
                            createButton.setVisibility(View.VISIBLE);
                        });
                    }
                    else{
                        Toast.makeText(this, "Please Choose A Image", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        Log.e("IMAGE", cursor.getString(column_index));
        return cursor.getString(column_index);
    }

    //permissions
    public void beginImage() {
        if ((ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) &&
                (ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) &&
                (ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED))  {
            Log.i("1", "Permission is not granted");
            if
            (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA) &&
                    (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) &&  (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
                Log.i("REQUEST", "Requesting permission....");
                ActivityCompat.requestPermissions(CardActivity.this,
                        new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        1332);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1332);
            }
        } else {
            final CharSequence[] options = {"Take Photo", "Choose From Gallery","Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Option");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("Take Photo")) {
                        dialog.dismiss();
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, PICK_IMAGE_CAMERA);
                    } else if (options[item].equals("Choose From Gallery")) {
                        dialog.dismiss();
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                    } else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[]
                                                   grantResults) {
        switch (requestCode) {
            case 1332: {
                if (grantResults.length > 0
                        && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    Log.i("1", "Permission is granted");
                    final CharSequence[] options = {"Take Photo", "Choose From Gallery","Cancel"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Select Option");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (options[item].equals("Take Photo")) {
                                dialog.dismiss();
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, PICK_IMAGE_CAMERA);
                            } else if (options[item].equals("Choose From Gallery")) {
                                dialog.dismiss();
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                            } else if (options[item].equals("Cancel")) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();

                } else {
                    Log.i("1", "Permission is again not granted");
                    Snackbar mySnackbar =
                            Snackbar.make(findViewById(android.R.id.content),
                                    "Please enable the permissions",
                                    Snackbar.LENGTH_INDEFINITE);
                    mySnackbar.setAction("ENABLE", view -> startActivity(new
                            Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + BuildConfig.APPLICATION_ID))));
                    mySnackbar.show();
                }
                return;
            } }
    }

    public void formatDateForDB(int day, int month, int year, int hour, int minute,int second){
        selecteDate =new Date(year-1900, month-1, day, hour, minute, second);
        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        sfd.format(selecteDate);
    }

}
