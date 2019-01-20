//package graduuaplicacao.graduuaplicacao.Adapters;
//
//import android.content.Context;
//import android.content.Intent;
//import android.support.design.widget.AppBarLayout;
//import android.util.SparseBooleanArray;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.ListAdapter;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.annotation.GlideModule;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.like.LikeButton;
//
//import java.util.ArrayList;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//import graduuaplicacao.graduuaplicacao.Model.Evento;
//import graduuaplicacao.graduuaplicacao.R;
//
//public class EventosAdapter extends ArrayAdapter<Evento> implements AppBarLayout.OnOffsetChangedListener{
//
//
//    private ArrayList<Evento> eventos;
//    private Context context;
//
//    private String[] imageUrls;
//
//    private SparseBooleanArray itemStateArray = new SparseBooleanArray();
//
//    private LikeButton btnLike;
//    FirebaseAuth mAuth = FirebaseAuth.getInstance();
//
//    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
//
//    private int mMaxScrollSize;
//
//
//    public EventosAdapter(Context c, ArrayList<Evento> objects) {
//
//        super(c, 0, objects);
//        this.context = c;
//        this.eventos = objects;
//    }
//
//    static class ViewHolder {
//        TextView txtViewNome;
//        TextView txtViewHoraInicio;
//        TextView txtViewData;
//        CheckBox btnLike;
//        Button btnShare;
//    }
//
////    public ImageListAdapter (Context context, String[] imageUrls) {
////        super(context, R.layout.style_card_view_new, imageUrls);
////        this.context = context;
////        this.imageUrls = imageUrls;
////    }
//
//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//        View view = convertView;
//        final ViewHolder holder;
//        holder = new ViewHolder();
//
//
//
//        if(eventos != null) {
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
//            view = inflater.inflate(R.layout.style_card_view_new, parent, false);
//
//            CircleImageView imagemPerfilCard = (CircleImageView) view.findViewById(R.id.imagemPerfilCard);
//            holder.txtViewNome = (TextView) view.findViewById(R.id.txtViewNome);
//            holder.txtViewHoraInicio = (TextView) view.findViewById(R.id.txtViewHoraInicio);
//            holder.txtViewData = (TextView) view.findViewById(R.id.txtViewData);
//            holder.btnLike = (CheckBox) view.findViewById(R.id.likeIcon);
//            holder.btnShare = (Button) view.findViewById(R.id.btnShare);
//
//            view.setTag(holder);
//
//
//            holder.btnShare.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(Intent.ACTION_SEND);
//                    intent.setType("text/plain");
//                    String sharedSubject = ("Venha para o evento " + eventos.get(position).getNome());
//                    intent.putExtra(Intent.EXTRA_SUBJECT, sharedSubject);
//                    intent.putExtra(Intent.EXTRA_TEXT, "Venha participar do evento " + eventos.get(position).getNome().toUpperCase() + " no dia " + eventos.get(position).getData() + ". "
//                            + "Clique no link a seguir para ser redirecionado: GERAR LINK DINAMICO DO EVENTO");
//                    context.startActivity(Intent.createChooser(intent, "share_using"));
//                }
//            });
////
//
//            if (convertView == null) {
////                holder.btnLike = (CheckBox) convertView.findViewById(R.id.likeIcon);
//                view.setTag(R.id.likeIcon, holder.btnLike);
//            }
//
//            holder.btnLike.setTag(position);
//            holder.btnLike.setChecked(eventos.get(position).isChecked());
//
//            holder.btnLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                                                         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                                                             int getPosition = (Integer) buttonView.getTag();
//                                                             holder.btnLike.setChecked(buttonView.isChecked() ? true : false);
//                                                             eventos.get(getPosition).setChecked(buttonView.isChecked() ? true : false);
//                                                         }
//                                                     });
//
//            FirebaseUser user = mAuth.getCurrentUser();
//            String userID = user.getUid();
//
//
//            StorageReference teste = storageReference.child("Users/").child(userID).child("imagem");
//
//            String url = "https://image.freepik.com/icones-gratis/silhueta-usuario-masculino_318-35708.jpg";
//            Glide.with(view).load(url).into(imagemPerfilCard);
//
////            GlideApp.with(view).load(url).into(imagemPerfilCard);
//
//
//            Evento evento2 = eventos.get(position);
//
//            holder.txtViewNome.setText(evento2.getNome());
//            holder.txtViewHoraInicio.setText("- ÁS " + evento2.getHoraInicio().toString());
//            holder.txtViewData.setText(evento2.getData().toString());
//
//
//        }
//
//        return view;
//    }
//
//    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
//        if (mMaxScrollSize == 0)
//            mMaxScrollSize = appBarLayout.getTotalScrollRange();
//
//        int percentage = (Math.abs(i)) * 100 / mMaxScrollSize;
//
//    }
//}