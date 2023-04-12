package algonquin.cst2335.finalproject;

import static android.app.DownloadManager.COLUMN_ID;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.Manifest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Bitmap bitmap; // Declare bitmap as a class-level field
    private static final int PERMISSION_REQUEST_CODE = 100;
    private ListView listView;
    private EditText widthEditText;
    private EditText heightEditText;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String WIDTH_KEY = "width";
    private static final String HEIGHT_KEY = "height";

    private void refreshList() {
        // Get a reference to the ListView
        ListView listView = findViewById(R.id.list_view);

        // Get the list of JPG files in the directory
        File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] files = directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".jpg");
            }
        });

        // Create an ArrayList to hold the file names
        ArrayList<String> fileNames = new ArrayList<>();

        // Add the file names to the ArrayList
        for (File file : files) {
            fileNames.add(file.getName());
        }

        // Create an ArrayAdapter to display the file names in the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, fileNames);

        // Set the adapter on the ListView
        listView.setAdapter(adapter);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button getImageButton = findViewById(R.id.img_button);
        final ImageView imageView = findViewById(R.id.imageview);
        listView = findViewById(R.id.list_view);

        widthEditText = findViewById(R.id.width_edittext);
        heightEditText = findViewById(R.id.height_edittext);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedWidth = sharedPreferences.getInt(WIDTH_KEY, 0);
        int savedHeight = sharedPreferences.getInt(HEIGHT_KEY, 0);

        if (savedWidth > 0 && savedHeight > 0) {
            widthEditText.setText(String.valueOf(savedWidth));
            heightEditText.setText(String.valueOf(savedHeight));
        }


        getImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the width and height values from the EditText views
                EditText widthEditText = findViewById(R.id.width_edittext);
                EditText heightEditText = findViewById(R.id.height_edittext);
                int width = Integer.parseInt(widthEditText.getText().toString());
                int height = Integer.parseInt(heightEditText.getText().toString());

                // Build the URL for the image
                String url = "https://placekitten.com/" + width + "/" + height;

                // Create a new thread to load the image from the server
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Connect to the server and open an input stream
                            URL imageUrl = new URL(url);
                            HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
                            InputStream inputStream = connection.getInputStream();

                            // Decode the input stream into a bitmap
                            bitmap = BitmapFactory.decodeStream(inputStream);

                            // Update the UI on the main thread
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Set the bitmap as the image for the ImageView
                                    imageView.setImageBitmap(bitmap);
                                }
                            });

                            // Close the input stream and disconnect from the server
                            inputStream.close();
                            connection.disconnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });




        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the app has permission to write to external storage
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // Create a new file in the device's Pictures directory
                    File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    // Generate a unique file name using a random number generator
                    // Generate a random number
                    Random rand = new Random();
                    int randomNumber = rand.nextInt(1000);

// Create a new file with the random number as part of the name
                    File newImage = new File(pictureDirectory, "My_Image_" + randomNumber + ".jpg");

                    try {
                        // Compress the image to JPEG format
                        FileOutputStream fos = new FileOutputStream(newImage);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        fos.flush();
                        fos.close();

                        // Show a toast message to indicate that the image has been saved
                        Toast.makeText(MainActivity.this, "Image saved to Pictures directory", Toast.LENGTH_SHORT).show();

                        // Add the image filename to the database
                        ImageDatabaseHelper db = new ImageDatabaseHelper(MainActivity.this);
                        db.addImage(newImage.getName());

                        // Refresh the list view to display the new image filename
                        List<String> imageFilenames = db.getAllImages();
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, imageFilenames);
                        listView.setAdapter(adapter);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Request permission to write to external storage
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the filename of the selected image
                String filename = (String) parent.getItemAtPosition(position);

                // Get the image file from the device's Pictures directory
                File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File file = new File(pictureDirectory, filename);

                // Decode the image file into a bitmap
                Bitmap savedBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                // Set the saved bitmap as the image for the ImageView
                imageView.setImageBitmap(savedBitmap);

                // Update the bitmap object used to set the image in the ImageView
                bitmap = savedBitmap;

                // Get the width and height of the saved image
                int savedWidth = savedBitmap.getWidth();
                int savedHeight = savedBitmap.getHeight();

                // Get the date the image was saved
                Date date = new Date(file.lastModified());

                // Create a new dialog to show the image and its metadata
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(filename);
                builder.setMessage("Width: " + savedWidth + "\nHeight: " + savedHeight + "\nDate saved: " + date);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dismiss the dialog
                        dialog.dismiss();
                    }
                });

                // Set the saved image as the dialog's view
                ImageView imageView = new ImageView(MainActivity.this);
                imageView.setImageBitmap(savedBitmap);
                builder.setView(imageView);

                // Show the dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });



                // Refresh the list view to display the updated list of files
                refreshList();
            }




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, save the image
                Button saveButton = findViewById(R.id.save_button);
                saveButton.performClick();
            } else {
                // Permission denied, show a toast message
                Toast.makeText(this, "Permission denied, image not saved", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onGetButtonClicked(View v) {
        int width = Integer.parseInt(widthEditText.getText().toString());
        int height = Integer.parseInt(heightEditText.getText().toString());

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(WIDTH_KEY, width);
        editor.putInt(HEIGHT_KEY, height);
        editor.apply();
    }


}


/*









Implement a "Delete" button to allow the user to delete a saved image from the database.

Use SharedPreferences to save the last values for width and height that were searched and show them the next time the user runs the application.
 */