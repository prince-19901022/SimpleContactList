package com.scl.neutrino.simplecontactlist;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FooterListener{

    private RecyclerView contactListRV;
    private ContactListAdapter adapter;
    private RecyclerView.LayoutManager lManager;

    private Database db;

    private ArrayList<Person> donorDataset;
    private ArrayList<Person> recieverDataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Populating Database With Contacts Available in Device
        db = new Database(this , Database.DATABASE_VERSION);
        db.insertContact(getContactsFromDevice());
        donorDataset = new ArrayList<>();
        recieverDataset = new ArrayList<>();
        donorDataset = db.getAllContact();

        contactListRV = (RecyclerView) findViewById(R.id.clRV);
        adapter = new ContactListAdapter( recieverDataset, MainActivity.this);
        lManager = new LinearLayoutManager(this);

        contactListRV.setItemAnimator(new DefaultItemAnimator());
        //contactListRV.addItemDecoration();
        contactListRV.setLayoutManager(lManager);
        contactListRV.setAdapter(adapter);

        OnLoadMore();
    }

    @Override
    public boolean OnLoadMore() {
        boolean canLoadMore = true;

        int itr ;
        if(donorDataset.size() >= 10){
            itr = 10;
        }else{
            itr = donorDataset.size();
        }
        Toast.makeText(this , "Iter : "+itr+" Size : "+donorDataset.size(),Toast.LENGTH_SHORT).show();
        for(int i = 0; i < itr; i++){

            try{
                recieverDataset.add(donorDataset.remove(i));
            }catch(IndexOutOfBoundsException Ex){
                //Toast.makeText(this , "Iter : "+i+" Size : "+donorDataset.size(),Toast.LENGTH_SHORT).show();
            }
        }

        if(donorDataset.isEmpty()){
            canLoadMore = false;
        }
        adapter.notifyDataSetChanged();
        return canLoadMore;
    }


    private ArrayList<Person> getContactsFromDevice(){

        ArrayList<Person> personList = new ArrayList<>();

        String[] projection2 = new String[] {
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        };

        String where2 = ContactsContract.CommonDataKinds.Phone._ID + " != ''" ;
        Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection2, where2, null, null);
        if(phoneCursor.getCount() > 0){

            while(phoneCursor.moveToNext()){

                String name = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNum = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                personList.add(new Person(name , phoneNum));
            }
        }
        phoneCursor.close();

        return personList;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
