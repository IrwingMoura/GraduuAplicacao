package graduuaplicacao.graduuaplicacao.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import graduuaplicacao.graduuaplicacao.Adapters.AdapterItem;
import graduuaplicacao.graduuaplicacao.Model.Item;
import graduuaplicacao.graduuaplicacao.R;

public class RecyclerTeste extends AppCompatActivity {

    RecyclerView list;
    RecyclerView.LayoutManager layoutManager;
    List<Item> items = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_teste);

        list = (RecyclerView)findViewById(R.id.recycler);
        list.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);
        
        setData();

    }

    private void setData() {

        for(int i=0;i<20;i++){

            if(i%2==0)
            {
                Item item = new Item("This is item" + (i+1), "This is child item"+ (i+1), true);
                items.add(item);
            }else {
                Item item = new Item("This is item" + (i + 1), "", false);
                items.add(item);
            }
        }

        AdapterItem adapterItem = new AdapterItem(items);
        list.setAdapter(adapterItem);

    }


}
