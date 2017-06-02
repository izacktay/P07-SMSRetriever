package sg.edu.rp.c347.p07_smsretriever;


import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class WordFragment extends Fragment {


    TextView tvText;
    Button btnRetrieve;
    EditText etText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_word, container,false);

        tvText = (TextView)view.findViewById(R.id.tvWord);
        btnRetrieve = (Button)view.findViewById(R.id.btnRetrieveWord);
        etText = (EditText)view.findViewById(R.id.etWord);

        btnRetrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Create all messages URI
                Uri uri = Uri.parse("content://sms");

                // The columns we want
                //  date is when the message took place
                //  address is the number of the other party
                //  body is the message content
                //  type 1 is received, type 2 sent
                String[] reqCols = new String[]{"date", "address", "body", "type"};

                // Get Content Resolver object from which to
                //  query the content provider
                ContentResolver cr = getActivity().getContentResolver();

                // The filter String
                String word = etText.getText().toString();
                String filter="body LIKE ?";
                String[] findWord = word.split(" ");
                // The matches for the ?


                for (int i = 0; i < findWord.length; i++){

                    findWord[i] = "%" + findWord[i] + "%";
                    if (i != 0){
                        filter += "OR BODY LIKE ?";
                    }
                }

                // Fetch SMS Message from Built-in Content Provider
                Cursor cursor = cr.query(uri, reqCols, filter, findWord, null);
                String smsBody = "";
                if (cursor.moveToFirst()) {
                    do {
                        long dateInMillis = cursor.getLong(0);
                        String date = (String) DateFormat
                                .format("dd MMM yyyy h:mm:ss aa", dateInMillis);
                        String address = cursor.getString(1);
                        String body = cursor.getString(2);
                        String type = cursor.getString(3);
                        if (type.equalsIgnoreCase("1")) {
                            type = "Inbox:";
                        } else {
                            type = "Sent:";
                        }
                        smsBody += type + " " + address + "\n at " + date
                                + "\n\"" + body + "\"\n\n";
                    } while (cursor.moveToNext());
                }
                tvText.setText(smsBody);
            }
        });


        return view;
    }
}
