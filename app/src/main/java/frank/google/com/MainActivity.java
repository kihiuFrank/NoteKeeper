package frank.google.com;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

/**
 * Created by Kihiu.
 */

public class MainActivity extends AppCompatActivity {
    public static final String NOTE_POSITION = "frank.google.com.NOTE_POSITION";
    public static final int POSITION_NOT_SET = -1;
    private NoteInfo mNote;
    private boolean isNewNote;
    private Spinner mSpinnerCourses;
    private EditText mTextNoteTitle;
    private EditText mTextNoteText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSpinnerCourses = findViewById(R.id.spinner_courses);

        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        ArrayAdapter<CourseInfo> adapterCourses = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses);
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCourses.setAdapter(adapterCourses);

        readDisplayStateValues();

        mTextNoteTitle = findViewById(R.id.text_note_title);
        mTextNoteText = findViewById(R.id.text_note_text);
        if (!isNewNote) {
            displayNote(mSpinnerCourses, mTextNoteTitle, mTextNoteText);
        }

    }

    private void displayNote(Spinner spinnerCourses, EditText textNoteText, EditText textNoteTitle) {
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        int courseIndex = courses.indexOf(mNote.getCourse());
        spinnerCourses.setSelection(courseIndex);
        textNoteText.setText(mNote.getText());
        textNoteTitle.setText(mNote.getTitle());
    }

    private void readDisplayStateValues() {
        Intent intent = getIntent();
        int position = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);
        isNewNote = position == POSITION_NOT_SET;

        if (!isNewNote) {
            mNote = DataManager.getInstance().getNotes().get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_mail) {
            sendEmail();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendEmail() {
         CourseInfo course = (CourseInfo) mSpinnerCourses.getSelectedItem();
         String subject = mTextNoteTitle.getText().toString();
         String text = "Check out this email. Its fantastic \" \n" +
                 course.getTitle() + "\"\n" + mTextNoteText.getText().toString();

         Intent intent = new Intent(Intent.ACTION_SEND);
         intent.setType("message/rfc2822");
         intent.putExtra(Intent.EXTRA_TEXT,subject);
         intent.putExtra(Intent.EXTRA_TEXT,text);
         startActivity(intent);
    }
}
