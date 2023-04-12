package sidd0080.mgi.nasafinalproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import sidd0080.mgi.nasafinalproject.databinding.DetailsLayoutBinding;

public class MessageDetailsFragment extends Fragment {

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    NASAMessage selected;

    public MessageDetailsFragment(NASAMessage message) {
        selected = message;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        DetailsLayoutBinding binding = DetailsLayoutBinding.inflate(inflater);
        binding.cameraDetail.setText(selected.camera);
        binding.IDDetail.setText(selected.imageURL);

        ImageView fullImage = binding.timeDetail;
        String imageURL = selected.getImageURL();
        try {
            Picasso.get()
                    .load(imageURL)
                    .transform(new RoundedCornersTransformation(20, 0))
                    .into(fullImage);
        } catch(Exception e) {
            System.err.println("Couldn't load images.");
            e.printStackTrace();
        }

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Request the permission at runtime
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);
        }

        ImageView downloadImage = binding.downloadImage;
//        ImageView favoritesImage = binding.favoritesImage;
        downloadImage.setClickable(true);
//        favoritesImage.setClickable(true);

        downloadImage.setOnClickListener( click -> {
            downloadAndSaveImage(imageURL);
        });

        View view = binding.getRoot();
        view.setClickable(true);
        return view;

    }

    private void downloadAndSaveImage(String imageUrl) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

        Picasso.get().load(imageUrl).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                // Get the directory to save the photo to
                File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Create a new file in the directory with the given title
                File file = new File(directory, "IMG" + timeStamp + ".jpg");

                try {
                    // Create an output stream to write the photo data to the file
                    FileOutputStream outputStream = new FileOutputStream(file);

                    // Compress the bitmap into a JPEG and write it to the output stream
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

                    // Close the output stream
                    outputStream.close();

                    // Notify the MediaScanner that a new photo has been added, so it will show up in the Gallery app
                    MediaScannerConnection.scanFile(getActivity(), new String[]{file.toString()}, null, null);

                    // Show a toast message to indicate that the photo has been saved
                    Toast.makeText(getActivity(), "Photo saved to Gallery", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error saving photo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Error downloading image", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                // Do nothing
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted
                // Call your downloadAndSaveImage() method here
                String imageUrl = selected.getImageURL();
                downloadAndSaveImage(imageUrl);
            } else {
                // Permission has been denied
                Toast.makeText(getActivity(), "Permission denied to save photo", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
