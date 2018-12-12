package graduuaplicacao.graduuaplicacao.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import graduuaplicacao.graduuaplicacao.Adapters.CardViewAdapter;
import graduuaplicacao.graduuaplicacao.Model.CardsModel;
import graduuaplicacao.graduuaplicacao.R;

public class PrincipalActivity extends AppCompatActivity {

    private ArrayList<CardsModel> mCardsList;

    private RecyclerView mRecyclerView;
    private CardViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Button mButtonInsert;
//    private Button mRemoveButton;
    private EditText mEditTextInsert, mEditTextRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view_eventos);

        createCardsList();
        buildRecyclerView();
        setButtons();

    }


    public void insertItem(int position) {

        mCardsList.add(position, new CardsModel(R.drawable.ic_launcher_foreground, "Novo item na posição" + position, "Segunda linha"));
        mAdapter.notifyItemInserted(position);
    }

    public void removeItem(int position) {

        mCardsList.remove(position);
        mAdapter.notifyItemRemoved(position);
    }

    public void changeItem(int position, String text) {
        mCardsList.get(position).changeText1(text);
        mAdapter.notifyItemChanged(position);
    }

    public void createCardsList() {
        mCardsList = new ArrayList<>();
        mCardsList.add(new CardsModel(R.drawable.ic_launcher_foreground, "Line 1", "Line 2"));
        mCardsList.add(new CardsModel(R.drawable.ic_launcher_foreground, "Line 3", "Line 4"));
        mCardsList.add(new CardsModel(R.drawable.ic_launcher_foreground, "Line 5", "Line 6"));
        mCardsList.add(new CardsModel(R.drawable.ic_launcher_foreground, "Line 7", "Line 8"));

    }

    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new CardViewAdapter(mCardsList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setItemOnClickListener(new CardViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                changeItem(position, "Alterado");
            }

            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
            }
        });
    }

    public void setButtons() {


        mButtonInsert = (Button) findViewById(R.id.button_insert);
//        mRemoveButton = (Button) findViewById(R.id.button_remove);

        mEditTextInsert = (EditText) findViewById(R.id.caixa_inserir_card);
//        mEditTextRemove = (EditText) findViewById(R.id.caixa_remover_card);


        mButtonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                int position = Integer.parseInt(mEditTextInsert.getText().toString());
//                insertItem(position);
                Intent intent = new Intent(PrincipalActivity.this, RegistroDeEventoActivity.class);
                startActivity(intent);

            }
        });


//        mRemoveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                int position = Integer.parseInt(mEditTextRemove.getText().toString());
//                removeItem(position);
//
//            }
//        });

    }

    


}
