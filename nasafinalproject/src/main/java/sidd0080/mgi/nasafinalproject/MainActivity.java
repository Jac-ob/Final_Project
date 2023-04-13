package sidd0080.mgi.nasafinalproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import sidd0080.mgi.nasafinalproject.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    NASARoomViewModel nasaModel;
    ArrayList<NASAMessage> allMessages;
    ActivityMainBinding binding;
    Handler mainHandler;
    private RecyclerView.Adapter<MyRowHolder> myAdapter;
    NASAMessageDAO mDAO;
    MessageDetailsFragment  nasaFragment;
    List<String> listURL = new ArrayList<String>();
    List<String> listRover = new ArrayList<String>();
    List<String> listCamera = new ArrayList<String>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        TextView messageText;
        messageText = binding.recycleView.findViewById(R.id.rover_name);

        switch( item.getItemId()){
            case R.id.addFavorites:

                try {
                    NASAMessage selectedMessage = nasaModel.selectedMessage.getValue();
                    MyRowHolder selectedRow = nasaModel.selectedRow.getValue();
                    if( selectedMessage != null){
                        int position = selectedRow.getAdapterPosition();
                        NASAMessage thisMessage = allMessages.get(position);

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                        // method with build pattern
                        builder.setMessage(thisMessage.rover)
                                .setTitle("Do you want to favorite this article?")
                                .setNegativeButton("No", (dialog, cl) -> {
                                })
                                .setPositiveButton("Yes", (dialog, cl) -> {

                                    Executor thread = Executors.newSingleThreadExecutor();
                                    thread.execute(() -> {
                                        mDAO.insertMessage(thisMessage);
                                    });

                                    Snackbar.make(messageText, "Article: " + position + " has been added to favorites",
                                            Snackbar.LENGTH_LONG).setAction("Undo", clk -> {
                                        thread.execute(() -> {
                                            mDAO.deleteMessage(thisMessage);
                                        });
                                        //                                allMessages.add(position, thisMessage);
                                        nasaModel.roverNames.getValue().add(position, thisMessage);
                                        myAdapter.notifyItemInserted(position);
                                    }).show();
                                }).create().show();
                    }
                    else{
                        Toast.makeText(MainActivity.this,"please select a message",Toast.LENGTH_LONG).show();
                    }
                }
                catch (IndexOutOfBoundsException e){
                    Toast.makeText(MainActivity.this,"please select a message",Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.removeFavorites:

                try {
                    NASAMessage selectedMessage = nasaModel.selectedMessage.getValue();
                    MyRowHolder selectedRow = nasaModel.selectedRow.getValue();
                    if( selectedMessage != null){
                        int position = selectedRow.getAdapterPosition();
                        NASAMessage thisMessage = allMessages.get(position);

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        // method with build pattern
                        builder.setMessage(thisMessage.rover)
                                .setTitle("Do you want to remove this article from favorites?")
                                .setNegativeButton("No", (dialog, cl) -> {
                                })
                                .setPositiveButton("Yes", (dialog, cl) -> {

                                    Executor thread = Executors.newSingleThreadExecutor();
                                    thread.execute(() -> {
                                        mDAO.deleteMessage(thisMessage);
                                    });
                                    nasaModel.roverNames.getValue().remove(thisMessage);
                                    myAdapter.notifyItemRemoved(position);

                                    Snackbar.make(messageText, "Article: " + position + " has been removed from favorites",
                                            Snackbar.LENGTH_LONG).setAction("Undo", clk -> {
                                        thread.execute(() -> {
                                            mDAO.insertMessage(thisMessage);
                                        });
                                        nasaModel.roverNames.getValue().add(position, thisMessage);
                                        myAdapter.notifyItemInserted(position);
                                    }).show();
                                }).create().show();
                    }

                    else{
                        Toast.makeText(MainActivity.this,"please select a message",Toast.LENGTH_LONG).show();
                    }
                }

                catch (IndexOutOfBoundsException e){
                    Toast.makeText(MainActivity.this,"please select a message",Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.about:
                Context context = getApplicationContext();
                CharSequence text = "Version 1.0, created by Hasan Siddiqui";
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(context,text,duration).show();
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.myToolbar);

        mainHandler = new Handler(Looper.getMainLooper());

        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class,
                "NASADatabase").build();
        mDAO = db.cmDAO();

        // get values from ChatRoomViewModel and assign the values to allMessages
        nasaModel = new ViewModelProvider(this).get(NASARoomViewModel.class);
        allMessages = nasaModel.roverNames.getValue();

        // initiate allMessages if the messages declared in ChatRoomViewModel is null
        if(allMessages == null)
        {
            nasaModel.roverNames.postValue( allMessages = new ArrayList<NASAMessage>() );

        }

        // Observer for chatModel
        nasaModel.selectedMessage.observe(this, (newMessageValue) -> {

            nasaFragment = new MessageDetailsFragment( newMessageValue );
            FragmentManager fMgr = getSupportFragmentManager();
            FragmentTransaction tx = fMgr.beginTransaction();
            tx.add(R.id.fragmentLocation, nasaFragment);
            tx.addToBackStack("Back to previous activity");
            tx.commit(); //This line actually loads the fragment into the specified FrameLayout

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentLocation, nasaFragment)
                    .commit();
        });

        //set a layout manager for the rows to be aligned vertically using only 1 column
        binding.recycleView.setLayoutManager( new LinearLayoutManager(this));

        EditText editText = findViewById(R.id.searchEditText);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String textToSave = editText.getText().toString();
        editor.putString("savedText", textToSave);

        editor.apply();

        //SendButton: add the message typed in the edittext and time to the allMessages
        binding.searchButton.setOnClickListener( click -> {
            
            // Clears NASARoomViewModel and RecyclerView
            clearList();

            // Removes old data and fetches new data
            String searchDate = binding.searchEditText.getText().toString();
            FetchData fetchData = new FetchData(listURL, listRover, listCamera, searchDate, nasaModel);
            for(int i = fetchData.listRover.size() -1; i >= 0; i--) {
                fetchData.listRover.remove(i);
                fetchData.listURL.remove(i);
                fetchData.listCamera.remove(i);
            }
            System.out.println("Rover list: " + fetchData.listRover);
            System.out.println("URL list: " + fetchData.listURL);
            System.out.println("URL list: " + fetchData.listCamera);
            fetchData.execute();

            myAdapter.notifyItemInserted(allMessages.size() - 1);
            binding.searchEditText.setText("");

        });

        binding.favoritesButton.setOnClickListener( click -> {
            clearList();
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute( () -> {
                allMessages.addAll(mDAO.getAllMessages());

                // load RecycleView
                runOnUiThread( () ->{
                    binding.recycleView.setAdapter(myAdapter);
                });
            });
        });

        binding.recycleView.setAdapter( myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View root;
                if (viewType == 0)
                    root = getLayoutInflater().inflate(R.layout.nasa_message, parent, false);
                else
                    root = getLayoutInflater().inflate(R.layout.receive_message, parent, false);

                return new MyRowHolder(root);
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                NASAMessage object = allMessages.get(position);
                holder.roverText.setText(object.getRover());
                holder.articleText.setText("Article: " + position);

                String imageURL = object.getImageURL();
//                System.out.println(imageURL);
                try {
                    Picasso.get()
                            .load(imageURL)
                            .into(holder.imageView);
                } catch(Exception e) {
                    System.err.println("Couldn't load images.");
                    e.printStackTrace();
                }
            }

            @Override
            public int getItemCount() {

                return allMessages.size();
            }

            @Override
            public int getItemViewType(int position) {
                NASAMessage object = allMessages.get(position);

                if (object.getIsSentButton() == true) // if(object.getIsSentButton() == true)
                    return 0; //0 represents send, text on the left
                else
                    return 1;//1 represents receive, text on the right
            }
        });
    }

    private void clearList() {
        try {
            if (allMessages.size() != 0) {
                for(int i = allMessages.size()-1; i >= 0; i--){
                    allMessages.remove(i);
                    myAdapter.notifyItemRemoved(i);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadAndSaveImage(String imageUrl, String title) {
        Picasso.get().load(imageUrl).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                // Get the directory to save the photo to
                File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Create a new file in the directory with the given title
                File file = new File(directory, title + ".jpg");

                try {
                    // Create an output stream to write the photo data to the file
                    FileOutputStream outputStream = new FileOutputStream(file);

                    // Compress the bitmap into a JPEG and write it to the output stream
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

                    // Close the output stream
                    outputStream.close();

                    // Notify the MediaScanner that a new photo has been added, so it will show up in the Gallery app
                    MediaScannerConnection.scanFile(MainActivity.this, new String[] {file.toString()}, null, null);

                    // Show a toast message to indicate that the photo has been saved
                    Toast.makeText(MainActivity.this, "Photo saved to Gallery", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error saving photo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Error downloading image", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                // Do nothing
            }
        });
    }

    int position;
    class MyRowHolder extends RecyclerView.ViewHolder{
        TextView roverText;
        TextView articleText;
        TextView imageText;
        ImageView imageView;
        public MyRowHolder(@NonNull View itemView){
            super(itemView);
            roverText = itemView.findViewById(R.id.rover_name);
            articleText = itemView.findViewById(R.id.article_number);
//            imageText = itemView.findViewById(R.id.);
            imageView = itemView.findViewById(R.id.thumbnail);

            itemView.setOnClickListener( click ->{

                // Which row was click
                position = getAdapterPosition();
                NASAMessage selected = allMessages.get(position);
                nasaModel.selectedMessage.postValue( selected );
                nasaModel.selectedRow.postValue(this);
            });
        }
    }
}